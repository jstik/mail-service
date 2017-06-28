package com.service;

import java.io.IOException;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.config.ApplicationContextProvider;
import com.mail.MailManager;
import com.mail.MailManagerImpl;
import com.model.Mail;

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
            mailManager = ApplicationContextProvider.getApplicationContext().getBean(MailManagerImpl.class) ;
        }
        mailManager.toDBQueue(mail);
        return mail.getUuid();
    }

}
