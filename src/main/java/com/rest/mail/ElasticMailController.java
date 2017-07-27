package com.rest.mail;

import com.elastic.model.Mail;
import com.elastic.repository.MailRepository;
import com.elastic.service.MailService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Julia on 27.07.2017.
 */
@Controller
public class ElasticMailController {
    @Autowired
    private MailService mailService;
    @Autowired
    private MailRepository repository;

    @RequestMapping(value = "/es/mails/all")
    public @ResponseBody
    Object getAllMails() {
        return Lists.newArrayList(repository.findAll());
    }

}
