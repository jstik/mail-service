package com.rest.filter;
import org.codehaus.jackson.JsonNode;

import java.lang.reflect.Field;

/**
 * Created by Julia on 23.06.2017.
 */
public class CollectionFilter implements ColumnFilter {
    private ColumnFilter value;

    public CollectionFilter(ColumnFilter value) {
        this.value = value;
    }

    @Override
    public void setValue(JsonNode node, Field field) {
        value.setValue(node, field);
    }

    @Override
    public boolean isValid() {
        return value.isValid();
    }
}
