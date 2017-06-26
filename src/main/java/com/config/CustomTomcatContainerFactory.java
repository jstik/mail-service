package com.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.springframework.boot.context.embedded.EmbeddedServletContainer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;

import javax.activation.DataSource;
import java.io.File;

/**
 * Created by Julia on 07.06.2017.
 */
public class CustomTomcatContainerFactory extends TomcatEmbeddedServletContainerFactory {
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
        ContextResource resource = new ContextResource();
        resource.setName("jdbc/mainDB");
        resource.setType(com.zaxxer.hikari.HikariDataSource.class.getName());
        resource.setProperty("driverClassName", "oracle.jdbc.driver.OracleDriver");
        resource.setProperty("jdbcUrl", "jdbc:oracle:thin:@localhost:1521/pdborcl");
        resource.setProperty("username", "sa");
        resource.setProperty("password", "sa");
        resource.setProperty("factory", "org.apache.naming.factory.BeanFactory");
        context.getNamingResources().addResource(resource);
    }
}
