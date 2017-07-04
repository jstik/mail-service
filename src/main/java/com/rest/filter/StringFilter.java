package com.rest.filter;
import java.lang.reflect.Field;

import org.codehaus.jackson.JsonNode;
import org.springframework.util.ReflectionUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.util.StringUtils;

/**
 * Created by Julia on 22.06.2017.
 */
public class StringFilter implements ColumnFilter {
    private String value;

    @Override
    public void setValue(JsonNode node, Field field) {
        JsonNode filterValue = node.findValue("text");
        if(filterValue == null){
            return;
        }
        value = filterValue.asText();
    }

    @Override
    public boolean isValid() {
        return !StringUtils.isEmpty(value);
    }

	@Override
	public BooleanExpression createPredicate(Column column, EntityPathBase qEntity) throws Exception {
		if (!isValid())
			return null;
		EntityPathBase baseEntity = getEntityPathBase(column, qEntity);

		String name = column.getField().getName();
		Field qField = ReflectionUtils.findField(baseEntity.getClass(), name);

		StringPath path = (StringPath) qField.get(baseEntity);
		return path.contains(value);
	}
}
