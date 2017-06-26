package com.mail;

import core.SpringAware;
import junit.framework.TestCase;
import org.junit.Test;
import org.unitils.spring.annotation.SpringBeanByType;

/**
 * Created by Julia on 06.06.2017.
 */
public class SimpleMailSenderTest extends SpringAware {

   @SpringBeanByType
   private SimpleMailSender mailSender;


   @Test//https://productforums.google.com/forum/#!topic/gmail/9KCgzXY4G_c
    public void testSendEmail() throws Exception {
        mailSender.sendEmail("jkrylova.mail@gmail.com",
                "jk.krylova@yandex.ru",
                "Testing123",
                "Testing only \n\n Hello Spring Email Sender");
    }

}