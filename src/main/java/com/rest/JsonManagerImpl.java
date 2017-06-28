package com.rest;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.rest.schema.CustomSchemaFactoryWrapper;

@Service
public class JsonManagerImpl implements JsonManager {

	@Override
	public JsonSchema generateSchema(Class<?> klass) throws JsonMappingException {
		CustomSchemaFactoryWrapper visitor = new CustomSchemaFactoryWrapper();
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.acceptJsonFormatVisitor(klass, visitor);
		return visitor.finalSchema();
	}

}
