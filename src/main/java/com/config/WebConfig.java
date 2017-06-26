package com.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by Julia on 22.06.2017.
 */
@Configuration
@EnableWebMvc
@ImportResource("spring-web.xml")
@ComponentScan("com.*")
public class WebConfig {
}
