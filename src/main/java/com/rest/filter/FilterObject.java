package com.rest.filter;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Julia on 23.06.2017.
 */
@Target({FIELD})
@Retention(RUNTIME)
public @interface FilterObject {

    String name();

    String targetFieldName();
}
