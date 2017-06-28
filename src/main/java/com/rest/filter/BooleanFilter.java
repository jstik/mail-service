package com.rest.filter;

import java.lang.reflect.Field;

import org.codehaus.jackson.JsonNode;
import org.springframework.util.ReflectionUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.EntityPathBase;

public class BooleanFilter implements ColumnFilter {
	private Boolean value;

	@Override
	public void setValue(JsonNode node, Field field) {
		JsonNode filterValue = node.findValue("filterValue");
		if (filterValue != null && filterValue.isBoolean()) {
			this.value = filterValue.asBoolean();
		}

	}

	@Override
	public boolean isValid() {
		return value != null;
	}

	@Override
	public BooleanExpression createPredicate(Column column, EntityPathBase qEntity) throws Exception {
		if (!isValid())
			return null;
		EntityPathBase baseEntity = getEntityPathBase(column, qEntity);

		String name = column.getField().getName();
		Field qField = ReflectionUtils.findField(baseEntity.getClass(), name);

		BooleanPath path = (BooleanPath) qField.get(baseEntity);
		return path.eq(value);
	}

}
