package com.config.security;

import com.google.common.collect.Lists;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.util.Collections;

/**
 * Created by Julia on 13.07.2017.
 */

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class OAuth2ResourceServerConfig extends GlobalMethodSecurityConfiguration {
    @Primary
    @Bean
    public RemoteTokenServices tokenService() {
        RemoteTokenServices tokenService = new RemoteTokenServices();
        tokenService.setCheckTokenEndpointUrl(
                "http://localhost:8084/oauth/check_token");
        tokenService.setClientId("mail_service");
        tokenService.setClientSecret("secret");
        return tokenService;
    }

    @Bean
    @Primary
    public ResourceServerConfiguration mailResource(){
        ResourceServerConfiguration resource = new ResourceServerConfiguration();
        resource.setOrder(2);
        ResourceServerConfigurer configurer = new ResourceServerConfigurer() {
            @Override
            public void configure(ResourceServerSecurityConfigurer resources) throws Exception {

            }

            @Override
            public void configure(HttpSecurity http) throws Exception {
                http.authorizeRequests().antMatchers(
                        "/bower_components/**",
                        "/mail.app.js", "/mail-list/**", "/table/**", "/app.css").permitAll()
                        .antMatchers("/**").authenticated()
                        .and().exceptionHandling()
                        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("http://localhost:8084/login?client_id=mail_service&clientRedirect=true"))
                        .and()
                        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
            }
        };
        resource.setConfigurers(Collections.singletonList(configurer));
        return resource;
    }
/*
    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return new OAuth2MethodSecurityExpressionHandler();
    }*/
}
