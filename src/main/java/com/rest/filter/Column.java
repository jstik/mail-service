package com.rest.filter;

import com.google.common.collect.Lists;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Julia on 22.06.2017.
 */
public class Column {
    private static Log logger = LogFactory.getLog(Column.class);

    enum Type {
        STRING {
            @Override
            public ColumnFilter getColumnFilter(Field field) {
                return new StringFilter();
            }
        },
        DATETIME {
            @Override
            public ColumnFilter getColumnFilter(Field field) {
                return new DateTimeFilter();
            }
        },
        COLLECTION {
            @Override
            public ColumnFilter getColumnFilter(Field field) {
                Type type = Type.getType(FilterUtil.getGenericType(field));
                return new CollectionFilter(type.getColumnFilter(field));
            }
        },
        NUMBER{
            @Override
            public ColumnFilter getColumnFilter(Field field) {
                return new NumberFilter();
            }
        },
        ENUM {
            @Override
            public ColumnFilter getColumnFilter(Field field) {
                return new EnumFilter();
            }
        };

        public  abstract ColumnFilter getColumnFilter(Field field);
        public static Type getType(Class<?> klass) {
            if (klass == String.class) {
                return STRING;
            } else if (Date.class.isAssignableFrom(klass) || Temporal.class.isAssignableFrom(klass)) {
                return DATETIME;
            } else if (Number.class.isAssignableFrom(klass)) {
                return NUMBER;
            } else if(Collection.class.isAssignableFrom(klass)){
               return COLLECTION;
            } else {
                return ENUM;
            }
        }

    }


    private Object value;
    private List<Field> path;
    private Field field;

    public Column(){
    }


    public static List<Column> getColumns(ArrayNode arrayNode, Class klass){
        ArrayList<JsonNode> nodes = Lists.newArrayList(arrayNode.getElements());
        return nodes.stream().map(n -> {
            JsonNode columnName = n.findValue("name");
            if(StringUtils.isEmpty(columnName)){
               logger.warn( " Could find value of 'name' field in incomming json ");
               return null;
            }
            Column column = FilterUtil.createColumn(columnName, klass);
            if(column != null){
                Type type = Type.getType(column.getField().getType());
                ColumnFilter columnFilter = type.getColumnFilter(column.getField());
                column.setValue(columnFilter);
                JsonNode value = n.findValue("value");
                if(value != null) {
                    columnFilter.setValue(value, column.getField());
                } else {
                    logger.error("Couldn't find field with name value");
                }
            }
            return column;
        }).collect(Collectors.toList());


    }

    public String getName() {
        return field.getName();
    }


    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public List<Field> getPath() {
        return path;
    }

    public void setPath(List<Field> path) {
        this.path = path;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Type getType(){
        return Type.getType(field.getType());
    }

    public Type getValueType(){
        Type type = Type.getType(field.getType());
        if(type == Type.COLLECTION){
           return Type.getType(FilterUtil.getGenericType(field));
        }
        return type;
    }
}
