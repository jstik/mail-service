package com.config.security;


import com.config.discovery.LoadBalancerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
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
    @Autowired
    @Qualifier("consul")
    private LoadBalancerClient discoveryClient;

    @Primary
    @Bean
    public RemoteTokenServices tokenService() {
        ServiceInstance authInstance = discoveryClient.choose("auth-service");
        RemoteTokenServices tokenService = new RemoteTokenServices();
        tokenService.setCheckTokenEndpointUrl(authInstance.getUri() +
                "/oauth/check_token");
        tokenService.setClientId("mail_service");
        tokenService.setClientSecret("secret");
        return tokenService;
    }

    @Bean
    @Primary
    public ResourceServerConfiguration mailResource(){
        ServiceInstance authInstance = discoveryClient.choose("auth-service");
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
                        "/mail.app.js", "/mail-list/**", "/table/**",
                        "/app.css", "/swagger-ui.html", "/webjars/**",
                        "/health", "/configuration/ui", "/images/**", "/favicon.ico",
                        "/v2/api-docs",
                        "/swagger-resources/**", "/es/**"
                ).permitAll()
                        .antMatchers("/**").authenticated()
                        .and().exceptionHandling()
                        .authenticationEntryPoint(
                                new LoginUrlAuthenticationEntryPoint( authInstance.getUri() +
                                        "/login?client_id=mail_service&clientRedirect=true"))
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
