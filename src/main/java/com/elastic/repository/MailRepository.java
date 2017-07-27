package com.elastic.repository;

import com.elastic.model.Mail;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Created by Julia on 27.07.2017.
 */
public interface MailRepository extends ElasticsearchRepository<Mail, String> {
}
