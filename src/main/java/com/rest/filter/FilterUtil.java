package com.rest.filter;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.rest.filter.Column.Type;

/**
 * Created by Julia on 23.06.2017.
 */
public class FilterUtil {
    private static Log logger = LogFactory.getLog(FilterUtil.class);

    public static Class<?> getGenericType(Field field){
        ParameterizedType genericType = (ParameterizedType)field.getGenericType();
        return (Class<?>) genericType.getActualTypeArguments()[0];
    }


    public static Column createColumn(JsonNode node, Class klass) {
        if(node == null || ! node.isTextual()){
            logger.warn("Filter field is Incorrect. It should contains textual name field ");
            return null;
        }
        String columnName = node.getTextValue();

        Field field = ReflectionUtils.findField(klass,columnName);
        if(field == null) {
            List<Field> fields = Arrays.asList(klass.getDeclaredFields());
            for (Field f : fields) {
                if (!f.isAnnotationPresent(FilterObject.class))
                    continue;
                FilterObject annotation = f.getAnnotation(FilterObject.class);
                if (annotation.name().equals(columnName)) {
                    Column column = new Column();
                    processField(column, f, klass);
                    return column;
                }
            }
        }else {
            Column column = new Column();
            column.setField(field);
            return column;
        }
        return null;
    }

    private static void processField(Column column, Field field, Class main) {
        FilterObject annotation = field.getAnnotation(FilterObject.class);
        String target = annotation.targetFieldName();
        column.getPath().add(field);
        Class<?> type = Collection.class.isAssignableFrom(field.getType()) ?  getGenericType(field) : field.getType();
        Field childField = ReflectionUtils.findField(type, target);
        if(childField == null){
            throw  new IllegalArgumentException(" Couldn't find field with name  "
                    + target + ". Class name " + main.getCanonicalName());
        }
        if(childField.isAnnotationPresent(FilterObject.class)){
            processField(column, childField, main);
        } else {
            column.setField(childField);
        }
    }

	public static List<Column> getColumns(ArrayNode arrayNode, Class klass) {
		ArrayList<JsonNode> nodes = Lists.newArrayList(arrayNode.getElements());
		return nodes.stream().map(n -> {
			JsonNode columnName = n.findValue("name");
			if (StringUtils.isEmpty(columnName)) {
				logger.warn(" Could find value of 'name' field in incomming json ");
				return null;
			}
			Column column = FilterUtil.createColumn(columnName, klass);
			if (column != null) {
				Type type = Type.getType(column.getField().getType());
				ColumnFilter columnFilter = type.getColumnFilter(column.getField());
				column.setValue(columnFilter);
				JsonNode value = n.findValue("value");
				if (value != null) {
					columnFilter.setValue(value, column.getField());
				} else {
					logger.error("Couldn't find field with name value");
				}
			}
			return column;
		}).filter(Objects::nonNull).filter(c-> c.getValue().isValid())
                .collect(Collectors.toList());

	}

}
