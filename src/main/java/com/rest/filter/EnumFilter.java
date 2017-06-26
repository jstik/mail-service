package com.rest.filter;
import org.codehaus.jackson.JsonNode;
import org.springframework.util.StringUtils;
import java.lang.reflect.Field;

/**
 * Created by Julia on 23.06.2017.
 */
public class EnumFilter implements ColumnFilter{
    private Object value;

    @Override
    public void setValue(JsonNode node, Field field) {
        JsonNode filterValue = node.findValue("filterValue");
        if(!StringUtils.isEmpty(filterValue)){
            return;
        }
        Class<Enum> type = (Class<Enum>) field.getType();
        value = Enum.valueOf(type, filterValue.asText());
    }

    @Override
    public boolean isValid() {
        return value != null;
    }
}
