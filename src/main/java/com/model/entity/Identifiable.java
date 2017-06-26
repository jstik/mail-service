package com.model.entity;

import java.util.UUID;

/**
 * Created by Julia on 09.06.2017.
 */
public interface Identifiable {
   String uuid = UUID.randomUUID().toString();

    Long getId();

    default String getUuid(){
        return uuid;
    }
}

