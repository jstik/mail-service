package com.rest.filter;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.codehaus.jackson.JsonNode;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EntityPathBase;

/**
 * Created by Julia on 22.06.2017.
 */
public class DateTimeFilter implements ColumnFilter {

   private LocalDateTime from;
   private LocalDateTime to;

    public LocalDateTime getFrom() {
        return from;
    }

    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public void setTo(LocalDateTime to) {
        this.to = to;
    }

    @Override
    public void setValue(JsonNode node, Field field) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
        if(field.isAnnotationPresent(JsonFormat.class)){
            JsonFormat annotation = field.getAnnotation(JsonFormat.class);
            if(!StringUtils.isEmpty(annotation.pattern())){
                formatter = DateTimeFormatter.ofPattern(annotation.pattern());
            }

        };
        JsonNode to = node.findValue("to");
		this.to = to == null || to.isNull() ? null : LocalDateTime.parse(to.asText(), formatter);
        JsonNode from = node.findValue("from");
		this.from = from == null || from.isNull() ? null : LocalDateTime.parse(from.asText(), formatter);
    }

    @Override
    public boolean isValid() {
		return from != null || to != null;
    }

	@SuppressWarnings("unchecked")
	@Override
	public BooleanExpression createPredicate(Column column, EntityPathBase qEntity)
			throws Exception {
		if (!isValid())
			return null;
		EntityPathBase baseEntity = getEntityPathBase(column, qEntity);

		String name = column.getField().getName();
		Field qField = ReflectionUtils.findField(baseEntity.getClass(), name);

		DateTimePath<LocalDateTime> path = (DateTimePath<LocalDateTime>) qField.get(baseEntity);
		if (from != null && to != null) {
			return path.between(from, to);
		} else if (from != null) {
			return path.after(from);
		} else if (to != null) {
			return path.before(to);
		}

		return null;
	}
}

