package com.chen.config;

import com.chen.filter.LoginFilter;
import com.chen.filter.PermissionFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Filter的配置类
 * @Author LeifChen
 * @Date 2018-05-31
 */
@Configuration
public class FilterConfiguration {

    /**
     * 登录Filter
     * @return
     */
    @Bean
    public FilterRegistrationBean loginFilter() {
        FilterRegistrationBean filter = new FilterRegistrationBean(new LoginFilter());
        filter.addUrlPatterns("/sys/*", "/admin/*");
        return filter;
    }

    /**
     * 权限Filter
     * @return
     */
    @Bean
    public FilterRegistrationBean permissionFilter() {
        FilterRegistrationBean filter = new FilterRegistrationBean(new PermissionFilter());
        filter.addUrlPatterns("/sys/*", "/admin/*");
        filter.addInitParameter("exclusionUrls", "/sys/user/noAuth.page, /login.page");
        filter.addInitParameter("targetFilterLifecycle", "true");
        return filter;
    }
}
