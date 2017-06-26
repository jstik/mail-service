package com.rest.filter;
import java.lang.reflect.Field;

import org.codehaus.jackson.JsonNode;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;

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

	@Override
	public BooleanExpression createPredicate(Column column, EntityPathBase qEntity) throws Exception {
		return this.value.createPredicate(column, qEntity);
	}
}
