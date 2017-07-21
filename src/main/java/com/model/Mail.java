package com.model;

import com.model.entity.MailStatus;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.Email;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by Julia on 06.06.2017.
 */
@XmlRootElement
public class  Mail implements Serializable {
    private String from;

    @Email
    private String to;
    private String subject;
    private String body;
    private String[] cc;

    @XmlElement
    private String uuid = UUID.randomUUID().toString();

    public Mail(String from, String to, String subject, String body) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    public Mail() {
    }

    public String getFrom() {
        return from;
    }

    @XmlElement
    public void setFrom(String from) {
        this.from = from;
    }


    public String getTo() {
        return to;
    }

    @XmlElement
    public void setTo(String to) {
        this.to = to;
    }


    public String getSubject() {
        return subject;
    }

    @XmlElement
    public void setSubject(String subject) {
        this.subject = subject;
    }


    public String getBody() {
        return body;
    }

    @XmlElement
    public void setBody(String body) {
        this.body = body;
    }


    public String[] getCc() {
        return cc;
    }

    @JsonIgnore
    public String getCommaSeparatedCC(){
        if(this.cc == null)
            return "";
        return String.join(",", Arrays.asList(cc));
    }

    @XmlAttribute
    public void setCc(String[] cc) {
        this.cc = cc;
    }

    public String getUuid() {
        return uuid;
    }
}
