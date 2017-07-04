package com.rest.filter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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
    private List<Object> value = new ArrayList<>();

    @Override
    public void setValue(JsonNode node, Field field) {
        Enum[] enums = ((Class<Enum>) field.getType()).getEnumConstants();
        for (Enum anEnum : enums) {
            JsonNode value = node.findValue(anEnum.name());
            if(value == null)
                continue;
            if(value.isBoolean() && !value.getBooleanValue())
                continue;
            this.value.add(anEnum);
        }
    }

    @Override
    public boolean isValid() {
        return !value.isEmpty();
    }

	@Override
	public BooleanExpression createPredicate(Column column, EntityPathBase qEntity) throws Exception {
		if (!isValid())
			return null;
		EntityPathBase baseEntity = getEntityPathBase(column, qEntity);

		String name = column.getField().getName();
		Field qField = ReflectionUtils.findField(baseEntity.getClass(), name);

		EnumPath path = (EnumPath) qField.get(baseEntity);
		return path.in(value);
	}
}
