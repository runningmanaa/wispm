package com.laigeoffer.pmhub.base.security.annotation;

import com.laigeoffer.pmhub.base.security.aspect.DistributedLockAspect;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author zw
 * @description EnableDistributedLock 元注解，开启分布式锁功能
 * @create 2024-06-17-10:56
 */
@Target(ElementType.TYPE)               // 指定了 EnableDistributedLock 注解可以应用于类、接口声明
@Retention(RetentionPolicy.RUNTIME)     // 指定了 EnableDistributedLock 注解在运行时是可用的
@Documented                             // 表明 EnableDistributedLock 注解会被 javadoc 工具记录，注解的文档将被包含在 javadoc 中

// 导入 Spring 框架的组件。
// 当使用 @EnableDistributedLock 注解时
// Spring 容器会自动注册 DistributedLockAspect 作为一个 Bean
// 从而激活分布式锁的 AOP 功能。
@Import({DistributedLockAspect.class})
public @interface EnableDistributedLock {
}
