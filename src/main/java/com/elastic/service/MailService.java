package com.elastic.service;

import com.elastic.model.Mail;
import com.elastic.repository.MailRepository;
import com.model.entity.MailItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Julia on 27.07.2017.
 */
@Component
public class MailService {
    @Autowired
    private MailRepository repository;

    public void save(MailItem mailItem, List<String> tags){
        repository.save(new Mail(mailItem, tags));
    }


}
