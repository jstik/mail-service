package com.config;

import javax.annotation.Resource;
import javax.xml.ws.Endpoint;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerPlugin;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.apache.activemq.security.AuthenticationUser;
import org.apache.activemq.security.SimpleAuthenticationPlugin;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.util.jndi.JndiContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.JmsTransactionManager;

import com.service.MailServiceImpl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julia on 03.06.2017.
 */
@SpringBootApplication
@ImportResource({"spring-email.xml", "spring-db.xml"})
@ComponentScan(basePackages = "com.*")
@EnableJpaRepositories(value = "com.dao.*")
@EntityScan(value = "com.model.entity.*")
@PropertySource(value = {"classpath:project.properties"})

public class SpringBootApplicationStarter {
    @Autowired
    @Qualifier("pooledConnectionFactory")
    PooledConnectionFactory pooledConnectionFactory;
    @Resource
    public Environment env;

    public static void main(String[] args) {
        Endpoint.publish("http://localhost:9999/ws/mailSender", new MailServiceImpl());
        SpringApplication.run(SpringBootApplicationStarter.class, args);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer
    propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean(initMethod = "start", destroyMethod = "stop", name = "broker")
    public BrokerService broker() throws Exception {
        SimpleAuthenticationPlugin authentication = new SimpleAuthenticationPlugin();
        List<AuthenticationUser> users = new ArrayList<AuthenticationUser>();
        users.add(new AuthenticationUser("admin", "admin", "admins,publishers,consumers"));
        authentication.setUsers(users);
        BrokerService broker = new BrokerService();
        broker.setPersistent(false);
        broker.setBrokerName(env.getProperty("broker.name"));
        broker.addConnector(env.getProperty("broker.url"));
        // broker.setPlugins(new BrokerPlugin[]{authentication});
        return broker;
    }

    @Bean(name = "jmsListenerContainerFactory")
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(
            DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, pooledConnectionFactory);
        return factory;
    }

    @Bean(name = "jmsConnectionFactory")
    public ActiveMQConnectionFactory activeMQConnectionFactory(@Qualifier("broker") BrokerService brokerService) {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("vm://" + brokerService.getBrokerName());
        factory.setTrustAllPackages(true);
        return factory;
    }

    @Bean(initMethod = "start", destroyMethod = "stop", name = "pooledConnectionFactory")
    public PooledConnectionFactory getPooledConnectionFactory(ActiveMQConnectionFactory jmsConnectionFactory) {
        PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
        pooledConnectionFactory.setMaxConnections(8);
        pooledConnectionFactory.setConnectionFactory(jmsConnectionFactory);
        return pooledConnectionFactory;
    }


    @Bean(initMethod = "start", destroyMethod = "stop", name = "camelContext")
    public CamelContext getCamelContext() throws Exception {
        JndiContext jndiContext = new JndiContext();
        CamelContext camelContext = new DefaultCamelContext(jndiContext);
        ActiveMQComponent component = ActiveMQComponent.activeMQComponent("vm://fred?broker.persistent=false");
        camelContext.addComponent("activemq", component);
        return camelContext;
    }


    public JmsTransactionManager jmsTransactionManager(@Qualifier("jmsConnectionFactory") ActiveMQConnectionFactory activeMQConnectionFactory) {
        return new JmsTransactionManager(activeMQConnectionFactory);
    }
}
