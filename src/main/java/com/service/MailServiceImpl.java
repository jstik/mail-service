package com.service;

import com.config.ApplicationContextProvider;
import com.mail.MailManager;
import com.model.Mail;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.jws.WebService;
import javax.xml.bind.JAXBException;
import java.io.IOException;

/**
 * Created by Julia on 06.06.2017.
 */
@WebService(endpointInterface = "com.service.MailService")
@Service
public class MailServiceImpl implements MailService {
    @Autowired
    MailManager mailManager;

    @Override
    public String sendEmail(Mail mail) throws  IOException {
        if(mailManager == null) {
            mailManager = ApplicationContextProvider.getApplicationContext().getBean(MailManager.class) ;
        }
        mailManager.toDBQueue(mail);
        return mail.getUuid();
    }

}
