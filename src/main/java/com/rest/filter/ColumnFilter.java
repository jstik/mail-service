package com.rest.filter;



import org.codehaus.jackson.JsonNode;

import java.lang.reflect.Field;

/**
 * Created by Julia on 23.06.2017.
 */
interface ColumnFilter {

    void setValue(JsonNode node, Field field);

    boolean isValid();
}
