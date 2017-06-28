package com.rest;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.customProperties.ValidationSchemaFactoryWrapper;
import com.model.entity.MailItem;
import com.rest.schema.CustomSchemaFactoryWrapper;

import core.SpringAware;


public class JsonSchemaTest extends SpringAware {
	@Test
	public void test() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		ValidationSchemaFactoryWrapper validationWrapper = new ValidationSchemaFactoryWrapper();
		CustomSchemaFactoryWrapper personVisitor = new CustomSchemaFactoryWrapper();

		mapper.acceptJsonFormatVisitor(MailItem.class, personVisitor);
		JsonSchema schema = personVisitor.finalSchema();
		String writeValueAsString = mapper.writeValueAsString(schema);
	}

}
