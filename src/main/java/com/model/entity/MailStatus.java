package com.model.entity;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonValue;

import javax.persistence.*;

import static com.model.entity.MailStatus.Status.QUEUED;

/**
 * Created by Julia on 07.06.2017.
 */
@Entity
@Table(name = "mail_status")
public class MailStatus extends AbstractEntity implements Identifiable{
    public enum Status{
        QUEUED, SENT, ERROR;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name ="status")
    @Enumerated(EnumType.STRING)
    private Status status = QUEUED;

    @Column(name ="fails")
    private int fails;

    @OneToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "mail_item_id")
    @JsonIgnore
    private MailItem mailItem;

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getFails() {
        return fails;
    }

    public void setFails(int fails) {
        this.fails = fails;
    }

    @JsonIgnore
    public MailItem getMailItem() {
        return mailItem;
    }

    @JsonIgnore
    public void setMailItem(MailItem mailItem) {
        this.mailItem = mailItem;
    }


}
