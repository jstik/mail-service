package com.config;

import com.config.security.OAuth2TokenFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Created by Julia on 22.06.2017.
 */
@Configuration
@EnableWebMvc
@ImportResource("spring-web.xml")
@ComponentScan("com.*")
public class WebConfig {

    @Bean
    public FilterRegistrationBean someFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new OAuth2TokenFilter());
        registration.addUrlPatterns("/*");
        //registration.addInitParameter("paramName", "paramValue");
        registration.setName("OAuth2TokenFilter");
       // registration.setOrder(1);
        return registration;
    }
}
