package com.laigeoffer.pmhub.system.controller;

import com.laigeoffer.pmhub.base.core.config.pmhubConfig;
import com.laigeoffer.pmhub.base.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页
 *
 * @author canghe
 */
@RestController("/system/dashboard")
public class SysIndexController {
    /**
     * 系统基础配置
     */
    @Autowired
    private pmhubConfig pmhubConfig;

    /**
     * 访问首页，提示语
     */
    @RequestMapping("/")
    public String index() {
        return StringUtils.format("欢迎使用 pmhub，当前版本：v{}，请通过前端地址访问。", pmhubConfig.getName(), pmhubConfig.getVersion());
    }
}
