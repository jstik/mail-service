package com.mail;

import java.io.IOException;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.DefaultMessage;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.model.DBObject;
import com.model.Mail;
import com.model.entity.MailItem;

/**
 * Created by Julia on 08.06.2017.
 */
@Component

public class MailManagerImpl implements MailManager {
    @Autowired
    CamelContext camelContext;

    @Override
	public void toDBQueue(Mail mail) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DefaultMessage message = new DefaultMessage();
		message.setBody(mapper.writeValueAsString(mail));
        DefaultExchange exchange = new DefaultExchange(camelContext);
        exchange.setIn(message);
        ProducerTemplate template = camelContext.createProducerTemplate();
		template.send("activemq:queue:mailToDB", exchange);
    }
    

	@Override
	public void toSendQueue(MailItem mail) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		DefaultMessage message = new DefaultMessage();
		message.setBody(mapper.writeValueAsString(new DBObject(mail)));
		DefaultExchange exchange = new DefaultExchange(camelContext);
		exchange.setIn(message);
		ProducerTemplate template = camelContext.createProducerTemplate();
		template.send("activemq:queue:mailToSend", exchange);
    }


}
