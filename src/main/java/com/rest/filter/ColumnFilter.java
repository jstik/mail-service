package com.rest.filter;



import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.codehaus.jackson.JsonNode;
import org.springframework.util.ReflectionUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;

/**
 * Created by Julia on 23.06.2017.
 */
interface ColumnFilter {

    void setValue(JsonNode node, Field field);

    boolean isValid();

	BooleanExpression createPredicate(Column column, EntityPathBase qEntity) throws Exception;

	default EntityPathBase getEntityPathBase(Column column, EntityPathBase qEntity) {
		EntityPathBase baseEntity = qEntity;
		if (column.getPath() != null && !column.getPath().isEmpty()) {
			for (Field field : column.getPath()) {
				String name = field.getName();
				Method qMethod = ReflectionUtils.findMethod(baseEntity.getClass(), name);
				baseEntity = (EntityPathBase) ReflectionUtils.invokeMethod(qMethod, baseEntity);
			}
		}
		return baseEntity;
	}
}
