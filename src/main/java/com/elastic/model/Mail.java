package com.elastic.model;

import com.model.entity.MailItem;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Julia on 27.07.2017.
 */
@Document( indexName = "mails" , type = "mail")
public class Mail {

    @Field(type = FieldType.Long )
    private Long mailId;
    @Field private String from;
    @Field private String to;
    @Field private String subject;
    @Field private String body;
    @Field private List<String> cc;
    @Field private String type;
    @Id
    @Field private String uuid;
    @Field  private String tenantId;

    public Mail() {
    }
    public Mail(MailItem mailItem, List<String> tags) {
        this.mailId = mailItem.getId();
        this.from = mailItem.getFrom();
        this.to = mailItem.getTo();
        this.body = mailItem.getBody();
        this.cc = Arrays.asList(mailItem.getCc().split(","));
        this.uuid = mailItem.getUuid();
        this.subject = mailItem.getSubject();
        this.type = mailItem.getType();
        this.tags  = tags;
        this.tenantId = mailItem.getTenantId();
    }


    @Field(type = FieldType.Nested)
    private List<String> tags = new ArrayList<>();

    public Long getMailId() {
        return mailId;
    }

    public void setMailId(Long mailId) {
        this.mailId = mailId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getCc() {
        return cc;
    }

    public void setCc(List<String> cc) {
        this.cc = cc;
    }
}
