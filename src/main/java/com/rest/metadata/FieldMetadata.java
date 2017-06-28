package com.rest.metadata;

import java.lang.reflect.Field;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class FieldMetadata {

	public enum Type {
		STRING, DATETIME, BOOLEAN, COLLECTION, NUMBER, ENUM, OBJECT;

		public static Type getType(Class<?> klass) {
			if (klass == String.class) {
				return STRING;
			} else if (Date.class.isAssignableFrom(klass) || Temporal.class.isAssignableFrom(klass)) {
				return DATETIME;
			} else if (Number.class.isAssignableFrom(klass)) {
				return NUMBER;
			} else if (Collection.class.isAssignableFrom(klass)) {
				return COLLECTION;
			} else if (klass.isEnum()) {
				return ENUM;
			}
			return OBJECT;
		}
	}

	protected String name;

	protected Type type;

	public List<FieldMetadata> getMetadata(Class klass) {
		// ObjectMapper mapper = new Object
		List<FieldMetadata> result = new ArrayList<>();
		List<Field> allFields = Arrays.asList(klass.getFields());
		for (Field field : allFields) {
			// if(field.ann)
		}
		return null;

	}

}
