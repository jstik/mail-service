package com.rest.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonNode;

import java.lang.reflect.Field;

/**
 * Created by Julia on 23.06.2017.
 */
public class NumberFilter implements ColumnFilter {
    private static Log logger = LogFactory.getLog(NumberFilter.class);
    private Number value;

    @Override
    public void setValue(JsonNode node, Field field) {
        JsonNode filterValue = node.findValue("filterValue");
        if(filterValue == null){
            return;
        }
        if(!filterValue.isNumber()){
            logger.error("Must be a number");
            return;
        }
        value = filterValue.getNumberValue();
    }

    @Override
    public boolean isValid() {
        return value != null;
    }
}
