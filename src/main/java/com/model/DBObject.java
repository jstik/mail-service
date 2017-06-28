package com.model;

import java.io.Serializable;

import com.model.entity.Identifiable;

/**
 * Created by Julia on 09.06.2017.
 */
public class DBObject implements Identifiable, Serializable {
    private Long id;
    private String uuid;
    private Class klass;

    public  DBObject(){

    }
    public DBObject(Identifiable identifiable){
      this.id = identifiable.getId();
      this.uuid = identifiable.getUuid();
      this.klass = identifiable.getClass();
    }


    @Override
	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
	public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Class getKlass() {
        return klass;
    }

    public void setKlass(Class klass) {
        this.klass = klass;
    }
}
