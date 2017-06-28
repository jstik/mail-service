package com.model.entity;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Created by Julia on 07.06.2017.
 */
@MappedSuperclass
public class AbstractEntity {

//2017-06-15T10:25:53.000Z
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @Column(name = "created"/*, columnDefinition="TIMESTAMP"*/)
    protected LocalDateTime created = LocalDateTime.now(ZoneOffset.UTC);

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @Column(name = "updated"/*, columnDefinition="TIMESTAMP"*/)
    protected LocalDateTime updated = LocalDateTime.now(ZoneOffset.UTC);

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }
}
