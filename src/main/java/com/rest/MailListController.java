package com.rest;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dao.MailItemRepository;
import com.google.common.collect.Lists;
import com.model.entity.MailItem;
import com.rest.filter.Column;

/**
 * Created by Julia on 22.06.2017.
 */
@Controller
public class MailListController {
    @Autowired
    MailItemRepository mailItemRepository;

    /*@RequestMapping("/")
    public Object test(){
        return "WEB-INF/jsp/welcome.jsp";
    }*/
    @RequestMapping("/")
    public Object showMailList(){
        return "/index.html";
    }

    @RequestMapping(value = "/mails/mails.json", consumes = "application/json")
    public @ResponseBody Object getMails(@RequestParam(name = "filter") String filter ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode arrayNode = objectMapper.readValue(filter, ArrayNode.class);
        List<Column> columns = Column.getColumns(arrayNode, MailItem.class);

        MailListResponse response = new MailListResponse();
        Iterable<MailItem> all = mailItemRepository.findAll();

        PageRequest pageable = new PageRequest(0, 30);
      /*  Predicate predicate  = BooleanExpression.allOf()
        mailItemRepository.findAll()*/
        response.setData(Lists.newArrayList(all));
        return response;
    }


}
