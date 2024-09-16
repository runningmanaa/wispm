package com.laigeoffer.pmhub.base.security.aspect;

import com.laigeoffer.pmhub.base.core.exception.UtilException;
import com.laigeoffer.pmhub.base.core.utils.StringUtils;
import com.laigeoffer.pmhub.base.security.annotation.DistributedLock;
import com.laigeoffer.pmhub.base.security.pojo.ILock;
import com.laigeoffer.pmhub.base.security.service.redisson.IDistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author zw
 * @description DistributedLockAspect
 * @create 2024-06-17-10:20
 */
@Aspect
@Slf4j
@Component
public class DistributedLockAspect {

    @Resource
    private IDistributedLock distributedLock;

    /**
     * SpEL表达式解析
     */
    private SpelExpressionParser spelExpressionParser = new SpelExpressionParser();

    /**
     * 用于获取方法参数名字
     */
    private DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    /**
     * 定义一个切点，匹配所有使用了 @DistributedLock 注解的方法
     */
    @Pointcut("@annotation(com.laigeoffer.pmhub.base.security.annotation.DistributedLock)")
    public void distributorLock() {
    }

    /**
     * 定义一个环绕通知，在匹配到的切点方法执行前后执行
     * 这个通知中，实现了分布式锁的获取和释放逻辑
     * @param pjp: 被拦截的方法
     * @return
     * @throws Throwable
     */
    @Around("distributorLock()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        DistributedLock distributedLock = this.getDistributedLock(pjp); // 获取 @DistributedLock 注解的实例
        String lockKey = this.getLockKey(pjp, distributedLock);         // 生成一个用于分布式锁的 key
        ILock lockObj = null;
        try {
            // 加锁，tryLok = true, 并且tryTime > 0时，尝试获取锁，获取不到超时异常
            if (distributedLock.tryLok()) {
                if(distributedLock.tryTime() <= 0){
                    throw new UtilException("tryTime must be greater than 0");
                }
                lockObj = this.distributedLock.tryLock(lockKey, distributedLock.tryTime(), distributedLock.lockTime(), distributedLock.unit(), distributedLock.fair());
            } else {
                lockObj = this.distributedLock.lock(lockKey, distributedLock.lockTime(), distributedLock.unit(), distributedLock.fair());
            }

            if (Objects.isNull(lockObj)) {
                throw new UtilException("Duplicate request for method still in process");
            }

            return pjp.proceed();
        } catch (Exception e) {
            throw e;
        } finally {
            // 解锁
            this.unLock(lockObj);
        }
    }

    /**
     * 从当前的连接点 pjp 中提取 @DistributedLock 注解实例
     * @param pjp
     * @return
     * @throws NoSuchMethodException
     */
    private DistributedLock getDistributedLock(ProceedingJoinPoint pjp) throws NoSuchMethodException {
        String methodName = pjp.getSignature().getName();      // 获取被拦截方法的名称
        Class clazz = pjp.getTarget().getClass();              // 获取被拦截目标对象的 Class 对象。
        Class<?>[] par = ((MethodSignature) pjp.getSignature()).getParameterTypes();  // 获取被拦截方法的参数类型数组。
        Method lockMethod = clazz.getMethod(methodName, par);  // 根据方法名称和参数类型数组，获取被拦截方法的 Method 对象

        // 获取方法上声明的 @DistributedLock 注解实例，并返回
        DistributedLock distributedLock = lockMethod.getAnnotation(DistributedLock.class);
        return distributedLock;
    }

    /**
     * 解锁
     *
     * @param lockObj
     */
    private void unLock(ILock lockObj) {
        if (Objects.isNull(lockObj)) {
            return;
        }

        try {
            this.distributedLock.unLock(lockObj);
        } catch (Exception e) {
            log.error("分布式锁解锁异常", e);
        }
    }

    /**
     * 获取 lockKey
     *
     * @param pjp
     * @param distributedLock
     * @return
     */
    private String getLockKey(ProceedingJoinPoint pjp, DistributedLock distributedLock) {
        //  从 @DistributedLock 注解实例中获取 key 和 keyPrefix 属性
        String lockKey = distributedLock.key();
        String keyPrefix = distributedLock.keyPrefix();

        if (StringUtils.isBlank(lockKey)) {
            throw new UtilException("Lok key cannot be empty");
        }
        if (lockKey.contains("#")) {
            this.checkSpEL(lockKey);        // 验证 SpEL 表达式是否有效
            MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
            Object[] args = pjp.getArgs();  // 获取被拦截方法的参数值数组
            lockKey = getValBySpEL(lockKey, methodSignature, args);
        }
        lockKey = StringUtils.isBlank(keyPrefix) ? lockKey : keyPrefix + lockKey;
        return lockKey;
    }

    /**
     * SpEL 表达式校验
     *
     * @param spEL
     * @return
     */
    private void checkSpEL(String spEL) {
        try {
            ExpressionParser parser = new SpelExpressionParser();
            parser.parseExpression(spEL, new TemplateParserContext());
        } catch (Exception e) {
            log.error("spEL表达式解析异常", e);
            throw new UtilException("Invalid SpEL expression [" + spEL + "]");
        }
    }

    /**
     * 解析spEL表达式
     *
     * @param spEL
     * @param methodSignature
     * @param args
     * @return
     */
    private String getValBySpEL(String spEL, MethodSignature methodSignature, Object[] args) {
        String[] paramNames = nameDiscoverer.getParameterNames(methodSignature.getMethod()); // 获取被拦截方法的参数名数组
        if (paramNames == null || paramNames.length < 1) {
            throw new UtilException("Lok key cannot be empty");
        }

        // 解析传入的 SpEL 表达式字符串，得到一个 Expression 对象
        Expression expression = spelExpressionParser.parseExpression(spEL);

        // 创建表达式上下文对象，作为 SpEL 表达式解析的上下文环境
        EvaluationContext context = new StandardEvaluationContext();
        // 给上下文对象赋值，被拦截方法参数名：参数值
        for (int i = 0; i < args.length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }

        // 评估表达式，并获取表达式的值作为 SpEL 解析结果。可能是一个方法参数，或者是基于一个或多个方法参数计算得出的值。
        Object value = expression.getValue(context);
        if (value == null) {
            throw new UtilException("The parameter value cannot be null");
        }
        return value.toString();  // 返回值作为分布式锁的 key
    }
}
