package com.rest.filter;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.codehaus.jackson.JsonNode;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        this.to = StringUtils.isEmpty(to) ? null : LocalDateTime.parse(to.asText(), formatter);
        JsonNode from = node.findValue("from");
        this.from = StringUtils.isEmpty(from) ? null : LocalDateTime.parse(from.asText(), formatter);
    }

    @Override
    public boolean isValid() {
        return from != null || to !=null;
    }
}
