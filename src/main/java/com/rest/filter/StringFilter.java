package com.rest.filter;
import org.codehaus.jackson.JsonNode;

import java.lang.reflect.Field;

/**
 * Created by Julia on 22.06.2017.
 */
public class StringFilter implements ColumnFilter {
    private String value;

    @Override
    public void setValue(JsonNode node, Field field) {
        JsonNode filterValue = node.findValue("filterValue");
        if(filterValue == null){
            return;
        }
        value = filterValue.asText();
    }

    @Override
    public boolean isValid() {
        return value != null;
    }
}
