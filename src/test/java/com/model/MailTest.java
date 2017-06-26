package com.model;

import core.SpringAware;
import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.DefaultMessage;
import org.junit.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.util.UriComponentsBuilder;
import org.unitils.spring.annotation.SpringBeanByName;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Created by Julia on 06.06.2017.
 */
public class MailTest extends SpringAware {

    @SpringBeanByName
    CamelContext camelContext;

    @Test
    public void testMarshal() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Mail.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        StringWriter stringWriter = new StringWriter();
        Mail mail = new Mail("jkrylova@yandex.com",
                "me@yandex.com",
                "test message",
                "<br> Привет!! </br>");
        mail.setCc(new String[]{"helo@mail.ru" , "he@mail.com"});
        jaxbMarshaller.marshal(mail, stringWriter);
        jaxbMarshaller.marshal(mail, System.out);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Mail unmarshal = (Mail)unmarshaller.unmarshal(new StringSource(stringWriter.toString()));
    }

    @Test
    public void testSendMailInMQ() throws Exception{
        final JAXBContext jaxbContext = JAXBContext.newInstance(Mail.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        StringWriter stringWriter = new StringWriter();
        Mail mail = new Mail("ЮлияКрылова",
                "jkrylova@yandex.com",
                "test message",
                "<br> Привет!! </br>");
        jaxbMarshaller.marshal(mail, stringWriter);
        DefaultMessage msg = new DefaultMessage();
        msg.setBody(stringWriter.toString());
        DefaultExchange exchange = new DefaultExchange(camelContext);
        exchange.setIn(msg);
        ProducerTemplate template = camelContext.createProducerTemplate();
        template.send("activemq:queue:mail",  exchange);

        RouteBuilder builder = new RouteBuilder() {
            @Override
            public void configure() throws Exception {
              from("activemq:queue:mail").log("--getting mail  ")
                      //.bean(JAXBEmailProcessor.class, "convertSimple" )
                      .process(new Processor() {
                          @Override
                          public void process(Exchange exchange) throws Exception {
                              Message in = exchange.getIn();
                              JAXBContext jaxbContext = JAXBContext.newInstance(Mail.class);
                              Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                              Mail mail = (Mail)unmarshaller.unmarshal(new StringSource(in.getBody(String.class)));
                              SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
                              simpleMailMessage.setFrom(mail.getFrom());
                              simpleMailMessage.setTo(mail.getTo());
                              simpleMailMessage.setSubject(mail.getSubject());
                              simpleMailMessage.setText(mail.getBody());
                              simpleMailMessage.setCc(mail.getCc());
                              exchange.getOut().setBody(simpleMailMessage);
                          }
                      })
                      .to("activemq:queue:mailProcessed");
            }
        };
        camelContext.addRoutes(builder);
        Thread.currentThread().sleep(10000);
    }


    @Test

    public void testSendEmailViaActiveMQ() throws Exception {
        final JAXBContext jaxbContext = JAXBContext.newInstance(Mail.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        StringWriter stringWriter = new StringWriter();

        Mail mail = new Mail("va@mail.com",
                "jkrylova@yandex.com",
                "test message",
                "<br> Привет!! </br>");
        jaxbMarshaller.marshal(mail, stringWriter);
        DefaultMessage msg = new DefaultMessage();
        msg.setBody(stringWriter.toString());

        final DefaultExchange exchange = new DefaultExchange(camelContext);
        exchange.setIn(msg);
        ProducerTemplate template = camelContext.createProducerTemplate();
        template.send("activemq:queue:mail-test",  exchange);

        RouteBuilder mailRouter = new RouteBuilder() {
            @Override
            public void configure() throws Exception {
               from("activemq:queue:mail-test")
                       .process(new Processor() {
                   @Override
                   public void process(Exchange in) throws Exception {
                       Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                       Mail mail = (Mail) unmarshaller.unmarshal(new StringSource(in.getIn().getBody(String.class)));
                       UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();
                       String cc ="";
                       if (mail.getCc() != null)
                           cc = String.join(",", Arrays.asList(mail.getCc()));
                       uriBuilder.scheme("smtps").host("smtp.gmail.com")
                               .port(587).queryParam("username", "jkrylova.mail@gmail.com")
                               .queryParam("password", "cb2a18GO")

                               .queryParam("from", mail.getFrom())
                               .queryParam("to", mail.getTo())
                               //.queryParam("cc", StringEscapeUtils.escapeHtml(cc))
                       ;
                       ProducerTemplate producerTemplate = camelContext.createProducerTemplate();

                       Message email = in.getIn();
                       email.setHeader("Subject", mail.getSubject());
                       email.setHeader("contentType", "text/html");
                       email.setBody(mail.getBody());
                       producerTemplate.send(uriBuilder.toUriString(), email.getExchange());
                   }
               });
            }
        };

        camelContext.addRoutes(mailRouter);
        Thread.currentThread().sleep(10000);
    }

    @Test
    public void DateTest() throws JAXBException {
        LocalDateTime localDateTime = LocalDateTime.now();
        JAXBContext jaxbContext = JAXBContext.newInstance(XmlTest.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        StringWriter stringWriter = new StringWriter();
        jaxbMarshaller.marshal(new XmlTest(), stringWriter);
        String result = stringWriter.toString();
    }


}