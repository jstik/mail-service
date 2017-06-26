package com.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by Julia on 08.06.2017.
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {
    static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext(){
       return applicationContext;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextProvider.applicationContext = applicationContext;
    }
}
