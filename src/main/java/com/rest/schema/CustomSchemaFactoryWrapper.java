package com.rest.schema;

import java.time.temporal.Temporal;
import java.util.Date;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.customProperties.ValidationSchemaFactoryWrapper;
import com.fasterxml.jackson.module.jsonSchema.factories.ObjectVisitor;
import com.fasterxml.jackson.module.jsonSchema.factories.ObjectVisitorDecorator;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema;
import com.fasterxml.jackson.module.jsonSchema.types.StringSchema;

public class CustomSchemaFactoryWrapper extends SchemaFactoryWrapper {
	private CustomValidationSchemaFactoryWrapper fw = new CustomValidationSchemaFactoryWrapper();

	private static class CustomValidationSchemaFactoryWrapper extends ValidationSchemaFactoryWrapper {
		@Override
		public JsonSchema addValidationConstraints(JsonSchema schema, BeanProperty prop) {
			return super.addValidationConstraints(schema, prop);
		}

	}



	public CustomSchemaFactoryWrapper() {

	}

	@Override
	public JsonObjectFormatVisitor expectObjectFormat(JavaType convertedType) {
		JsonObjectFormatVisitor expectObjectFormat = fw.expectObjectFormat(convertedType);

		return new ObjectVisitorDecorator((ObjectVisitor) super.expectObjectFormat(convertedType)) {
			private JsonSchema getPropertySchema(BeanProperty writer) {
				return ((ObjectSchema) getSchema()).getProperties().get(writer.getName());
			}

			@Override
			public void optionalProperty(BeanProperty writer) throws JsonMappingException {
				super.optionalProperty(writer);

				fw.addValidationConstraints(getPropertySchema(writer), writer);
				addDatePattern(getPropertySchema(writer), writer);
			}

			@Override
			public void property(BeanProperty writer) throws JsonMappingException {
				super.property(writer);
				// expectObjectFormat.property(writer);

				fw.addValidationConstraints(getPropertySchema(writer), writer);
				addDatePattern(getPropertySchema(writer), writer);
			}
		};
	}

	protected JsonSchema addDatePattern(JsonSchema schema, BeanProperty prop) {
		JsonFormat jsonFormat = prop.getAnnotation(JsonFormat.class);
		if (jsonFormat == null)
			return schema;

		JavaType type = prop.getType();
		if (!Date.class.isAssignableFrom(type.getRawClass()) && !Temporal.class.isAssignableFrom(type.getRawClass())) {
			return schema;
		}
		if (schema.isStringSchema() && !StringUtils.isEmpty(jsonFormat.pattern())) {
			StringSchema stringSchema = schema.asStringSchema();
			stringSchema.setPattern(jsonFormat.pattern());
		}
		return schema;

	}

}
