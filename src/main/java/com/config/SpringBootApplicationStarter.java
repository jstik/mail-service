package com.config;

import javax.xml.ws.Endpoint;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.util.jndi.JndiContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.JmsTransactionManager;

import com.service.MailServiceImpl;

/**
 * Created by Julia on 03.06.2017.
 */
@SpringBootApplication
/*@ImportResource("applicationContext.xml")*/
/*@EnableJms*/
@ImportResource({"spring-email.xml","spring-db.xml"})
@ComponentScan(basePackages = "com.*")
@EnableJpaRepositories(value = "com.dao.*")
@EntityScan(value = "com.model.entity.*")
public class SpringBootApplicationStarter {
    @Autowired
    @Qualifier("pooledConnectionFactory")
    PooledConnectionFactory  pooledConnectionFactory;

    public static void main(String[] args) {
        Endpoint.publish("http://localhost:9999/ws/mailSender", new MailServiceImpl());
        SpringApplication.run(SpringBootApplicationStarter.class, args);
    }

   @Bean(initMethod = "start", destroyMethod = "stop", name = "broker")
    public BrokerService broker() throws Exception {
        BrokerService broker = new BrokerService();
// configure the broker
        //broker.setUseShutdownHook(false);
        broker.setPersistent(false);
        broker.setBrokerName("fred");
        broker.addConnector("tcp://localhost:61617");
        return broker;
    }

    @Bean(name = "jmsListenerContainerFactory")
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(
            DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, pooledConnectionFactory);
        return factory;
    }
    @Bean(name="jmsConnectionFactory")
    public ActiveMQConnectionFactory activeMQConnectionFactory(@Qualifier("broker") BrokerService brokerService ){
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
        camelContext.addComponent("activeMq",
                component);
        return camelContext;
    }


    public JmsTransactionManager jmsTransactionManager(@Qualifier("jmsConnectionFactory") ActiveMQConnectionFactory activeMQConnectionFactory){
       return new JmsTransactionManager(activeMQConnectionFactory);
    }
}
