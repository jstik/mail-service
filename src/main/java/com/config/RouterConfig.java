package com.config;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

/**
 * Created by Julia on 05.06.2017. TEST Configuration for camel + Active MQ
 */
// @Configuration
public class RouterConfig {
    @Autowired
    @Qualifier("camelContext")
    private
    CamelContext camelContext;

  /*  @Bean
    RouteBuilder timer() throws Exception {
        RouteBuilder routeBuilder = new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("timer:foo?period=1s").log("adadad ").process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        Message in = exchange.getIn();
                        in.setBody(" Hello !!!");
                    }
                })
                        .transform(body().append( " "+
                                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())))
                        .to("activemq:queue:incoming");
            }
        };
        camelContext.addRoutes(routeBuilder);
        return routeBuilder;
    }*/

    @Bean
    RouteBuilder  move() throws Exception {
        RouteBuilder routeBuilder = new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("activemq:queue:incoming")
                        .log("Route message from in queue to out queue with data ${body}")
                        .to("activemq:queue:outcoming");
            }
        };
        camelContext.addRoutes(routeBuilder);
        return routeBuilder;
    }
    @Bean
    RouteBuilder toConsole() throws Exception {
        RouteBuilder routeBuilder = new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("activemq:queue:outcoming").messageHistory()
                        .log("Print message to console")
                        .to("stream:out");
            }
        };
        camelContext.addRoutes(routeBuilder);
        return routeBuilder;
    }

    @Bean
    RouteBuilder toTestBean() throws Exception {
        RouteBuilder routeBuilder = new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("activemq:queue:start?asyncConsumer=true&concurrentConsumers=10")
                        .to("bean:testBean?method=hello")
                        .to("stream:out");
            }
        };
        camelContext.addRoutes(routeBuilder);
        ProducerTemplate template = camelContext.createProducerTemplate();
        for (int i = 0; i < 5; i++) {
            template.sendBody("activemq:queue:start", " _____________body______" + i);
        }


        return routeBuilder;
    }

}
