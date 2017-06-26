package com.mail;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultMessage;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.dao.MailItemRepository;
import com.model.DBObject;
import com.model.Mail;
import com.model.entity.MailItem;
import com.model.entity.MailStatus;

/**
 * Created by Julia on 08.06.2017.
 */
@Configuration
public class MailListener {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CamelContext camelContext;
    @Autowired
    MailItemRepository mailItemRepository;
    @Autowired
    private JavaMailSender mailSender;

    @Bean
    public RouteBuilder saveMailToDb() throws Exception {
        RouteBuilder routeBuilder = new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("activemq:queue:mailToDB").unmarshal().json(JsonLibrary.Jackson, Mail.class)
                        .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        Message message = exchange.getIn();
                        Mail mail = (Mail) message.getBody();
                        MailItem item = MailItem.createMailItem(mail);
                        try {
                            item = mailItemRepository.save(item);
                        } catch (Throwable e) {
                            logger.error("Couldn't save to Db", e);
                        }
                        DefaultMessage out = new DefaultMessage();
                        out.setBody(new DBObject(item));
                        exchange.setOut(out);
                    }
                }).marshal().json(JsonLibrary.Jackson).to("activemq:queue:mailToSend");
            }
        };
        camelContext.addRoutes(routeBuilder);
        return routeBuilder;
    }


    @Bean
    public RouteBuilder sendEmail() throws Exception {
        RouteBuilder routeBuilder = new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                onException(Exception.class).handled(true).marshal().json(JsonLibrary.Jackson) .to("activemq:queue:failedMails");
                from("activemq:queue:mailToSend")
                      .unmarshal().json(JsonLibrary.Jackson, DBObject.class).process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        Message message = exchange.getIn();
                        DBObject mail = (DBObject) message.getBody();
                        MailItem byUuid = mailItemRepository.findByUuid(mail.getUuid());
                        if (byUuid == null) {
                            ProducerTemplate producerTemplate = camelContext.createProducerTemplate();
                            producerTemplate.send("activemq:queue:mailToDB", exchange);
                            return;
                        }
                        SimpleMailMessage email = new SimpleMailMessage();
                        email.setFrom(byUuid.getFrom());
                        email.setTo(byUuid.getTo());
                        email.setSubject(byUuid.getSubject());
                        email.setText(byUuid.getBody());
                        email.setCc(byUuid.getCc());
                        try {
                            mailSender.send(email);
                            byUuid.getMailStatus().setStatus(MailStatus.Status.SENT);
                            mailItemRepository.save(byUuid);
                        } catch (Throwable e) {
                            logger.error("Couldn't sent email", e);
                            byUuid.getMailStatus().setFails(byUuid.getMailStatus().getFails() + 1);
                            byUuid.getMailStatus().setStatus(MailStatus.Status.ERROR);
                            mailItemRepository.save(byUuid);
                            DefaultMessage out = new DefaultMessage();
                            out.setBody(mail);
                            exchange.setOut(out);
                            throw  e;
                        }


                    }
                }).log("------Message Sent-------");

            }
        };
        camelContext.addRoutes(routeBuilder);
        return routeBuilder;
    }
}
