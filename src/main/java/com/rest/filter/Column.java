package com.rest.filter;

import java.lang.reflect.Field;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;

/**
 * Created by Julia on 22.06.2017.
 */
public class Column {
    private static Log logger = LogFactory.getLog(Column.class);

	public enum Type {
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
		BOOLEAN {

			@Override
			public ColumnFilter getColumnFilter(Field field) {
				return new BooleanFilter();
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
		},
		OBJECT {

			@Override
			public ColumnFilter getColumnFilter(Field field) {
				// TODO Auto-generated method stub
				return null;
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
			} else if (klass.isEnum()) {
                return ENUM;
            }
			return OBJECT;
        }

    }


	private ColumnFilter value;
    private List<Field> path;
    private Field field;

    public Column(){
    }




    public String getName() {
        return field.getName();
    }


	public ColumnFilter getValue() {
        return value;
    }

	public void setValue(ColumnFilter value) {
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

	public BooleanExpression getPredicate(EntityPathBase qEntity) throws Exception {
		return this.value.createPredicate(this, qEntity);
	}
}
