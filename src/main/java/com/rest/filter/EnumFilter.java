package com.rest.filter;
import java.lang.reflect.Field;

import org.codehaus.jackson.JsonNode;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.EnumPath;

/**
 * Created by Julia on 23.06.2017.
 */
public class EnumFilter implements ColumnFilter{
    private Object value;

    @Override
    public void setValue(JsonNode node, Field field) {
        JsonNode filterValue = node.findValue("filterValue");
        if(!StringUtils.isEmpty(filterValue)){
            return;
        }
        Class<Enum> type = (Class<Enum>) field.getType();
        value = Enum.valueOf(type, filterValue.asText());
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

		EnumPath path = (EnumPath) qField.get(baseEntity);
		return path.eq(value);
	}
}
