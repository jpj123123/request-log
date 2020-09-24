package com.jpj.config;

import com.jpj.xss.LoggerFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.DispatcherType;

/**
 * @Description: ControllerLog
 * @Author: jingpj <690176705@qq.com>
 * @Date: 2020-09-23 17:17
 */
public class ControllerLogConfig {

    @ConditionalOnWebApplication
    @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new LoggerFilter());
        registration.addUrlPatterns("/*");
        registration.setName("xssFilter");
        registration.setOrder(-1);
        return registration;
    }
}
