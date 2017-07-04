package com.rest.filter;

import java.lang.reflect.Field;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonNode;
import org.springframework.util.ReflectionUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;

/**
 * Created by Julia on 23.06.2017.
 */
public class NumberFilter implements ColumnFilter {
    private static Log logger = LogFactory.getLog(NumberFilter.class);
    private Number value;

    @Override
    public void setValue(JsonNode node, Field field) {
        JsonNode filterValue = node.findValue("value");
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

	@Override
	public BooleanExpression createPredicate(Column column, EntityPathBase qEntity) throws Exception {
		if (!isValid())
			return null;
		EntityPathBase baseEntity = getEntityPathBase(column, qEntity);

		String name = column.getField().getName();
		Field qField = ReflectionUtils.findField(baseEntity.getClass(), name);

		NumberPath path = (NumberPath) qField.get(baseEntity);
		return path.eq(value);
	}
}
