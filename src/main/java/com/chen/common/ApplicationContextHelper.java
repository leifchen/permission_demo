package com.chen.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * 从Spring获取Bean的工具类
 * @Author LeifChen
 * @Date 2018-04-11
 */
@Component("applicationContextHelper")
@Lazy(false)
public class ApplicationContextHelper implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    /**
     * 通过指定类获取Bean
     * @param clazz 目标类
     * @param <T>
     * @return
     */
    public static <T> T popBean(Class<T> clazz) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(clazz);
    }

    /**
     * 通过类名和类类型获取Bean
     * @param name 目标类名
     * @param clazz 目标类类型
     * @param <T>
     * @return
     */
    public static <T> T popBean(String name, Class<T> clazz) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(name, clazz);
    }
}

