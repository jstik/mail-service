package com.mail;

import com.dao.MailItemRepository;
import com.model.Mail;
import com.model.entity.MailItem;
import com.model.entity.MailStatus;
import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.DefaultMessage;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by Julia on 08.06.2017.
 */
@Component

public class MailManager{
    @Autowired
    CamelContext camelContext;

    public void toDBQueue(Mail mail) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringWriter sw = new StringWriter();
        mapper.writeValue(sw, mail);

        DefaultMessage message = new DefaultMessage();
        message.setBody(sw.toString());
        DefaultExchange exchange = new DefaultExchange(camelContext);
        exchange.setIn(message);
        ProducerTemplate template = camelContext.createProducerTemplate();
        template.send( "activemq:queue:mailToDB",  exchange);
    }


}
