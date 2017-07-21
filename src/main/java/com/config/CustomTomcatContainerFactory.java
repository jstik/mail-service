package com.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.apache.tomcat.util.http.LegacyCookieProcessor;
import org.springframework.boot.context.embedded.EmbeddedServletContainer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;

import javax.activation.DataSource;
import javax.jdo.annotations.Value;
import java.io.File;

/**
 * Created by Julia on 07.06.2017.
 */
public class CustomTomcatContainerFactory extends TomcatEmbeddedServletContainerFactory {
    private String jdbcUrl;
    private String jdbcDriver;
    private String user;
    private String password;

    @Override
    protected TomcatEmbeddedServletContainer getTomcatEmbeddedServletContainer(
            Tomcat tomcat) {
        tomcat.enableNaming();
        return super.getTomcatEmbeddedServletContainer(tomcat);
    }

    public EmbeddedServletContainer getEmbeddedServletContainer(
            ServletContextInitializer... initializers) {
        TomcatEmbeddedServletContainer container = (TomcatEmbeddedServletContainer)super.getEmbeddedServletContainer(initializers);
        container.getTomcat().enableNaming();
        return container;
    }



    @Override
    protected void postProcessContext(Context context) {
        context.setCookieProcessor(new LegacyCookieProcessor());
        ContextResource resource = new ContextResource();
        resource.setName("jdbc/mainDB");
        resource.setType(com.zaxxer.hikari.HikariDataSource.class.getName());
        resource.setProperty("driverClassName", jdbcDriver);
        resource.setProperty("jdbcUrl", jdbcUrl);
        resource.setProperty("username", user);
        resource.setProperty("password", password);
        resource.setProperty("factory", "org.apache.naming.factory.BeanFactory");
        context.getNamingResources().addResource(resource);
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public void setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
