package com.model.entity;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.model.Mail;
import com.rest.View;
import com.rest.filter.FilterObject;

/**
 * Created by Julia on 07.06.2017.
 */
@Entity
@Table(name = "mail_item")
public class MailItem extends AbstractEntity implements Identifiable, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")

    private Long id;

    @Column(name = "m_from")
	@NotNull
    private String from;

    @Column(name = "m_to")
    private String to;

    private String subject;

    @JsonView(value = {View.Details.class})
    @Column(name = "body", columnDefinition ="NCLOB" )
    private String body;

    private String cc;

    private String type;

    @Column(name = "uuid")
    @JsonIgnore
    private String uuid;

    @Column(name = "tenant_id")
    @JsonIgnore
    private String tenantId;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "mailItem", cascade = CascadeType.ALL)
    @JsonIgnore
    @FilterObject(name = "status", targetFieldName = "status")
    private MailStatus mailStatus;

    public static MailItem createMailItem(Mail mail) {
        MailItem item = new MailItem();
        item.setBody(mail.getBody());
        item.setCc(mail.getCommaSeparatedCC());
        item.setFrom(mail.getFrom());
        item.setTo(mail.getTo());
        item.setSubject(mail.getSubject());
        item.setUuid(mail.getUuid());
        MailStatus mailStatus = new MailStatus();
        mailStatus.setMailItem(item);
        item.setMailStatus(mailStatus);
        return item;
    }

    @Override
	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public MailStatus getMailStatus() {
        return mailStatus;
    }

    public void setMailStatus(MailStatus mailStatus) {
        this.mailStatus = mailStatus;
    }

    @Override
	public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @JsonProperty(value = "status")
    public MailStatus.Status getStatus(){
        return this.mailStatus.getStatus();
    }
}
