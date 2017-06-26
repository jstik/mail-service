package com.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * Created by Julia on 05.06.2017.
 */
@Component
public class SimpleMailSender {

    @Autowired
    private JavaMailSender mailSender;


    public void sendEmail(String from, String to, String subject, String msg){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(msg);
        mailSender.send(message);
    }


}
