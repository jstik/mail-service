package com.rest;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;

@Service
public interface JsonManager {

	public JsonSchema generateSchema(Class<?> klass) throws JsonMappingException;

}
