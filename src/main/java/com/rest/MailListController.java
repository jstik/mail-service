package com.rest;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import com.model.entity.QMailItem;
import com.querydsl.core.types.ExpressionUtils;
import com.rest.filter.Column;

/**
 * Created by Julia on 22.06.2017.
 */
@Controller
public class MailListController {
	private static Log logger = LogFactory.getLog(MailListController.class);
    @Autowired
	private MailItemRepository mailItemRepository;

    /*@RequestMapping("/")
    public Object test(){
        return "WEB-INF/jsp/welcome.jsp";
    }*/
    @RequestMapping("/")
    public Object showMailList(){
        return "/index.html";
    }

	@RequestMapping(value = "/mails/mails.json")
    public @ResponseBody Object getMails(@RequestParam(name = "filter") String filter ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode arrayNode = objectMapper.readValue(filter, ArrayNode.class);
        List<Column> columns = Column.getColumns(arrayNode, MailItem.class);
		Collection<com.querydsl.core.types.Predicate> predicates = columns.stream().map(c -> {
			try {
				return (com.querydsl.core.types.Predicate) c.getPredicate(QMailItem.mailItem);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("Couldn't create predicate", e);
				return null;
			}
		}).filter(ex -> ex != null).collect(Collectors.toList());
		PageRequest pageable = new PageRequest(0, 30);
		Iterable<MailItem> all;
		if (predicates.isEmpty()) {
			all = mailItemRepository.findAll(pageable);
		} else {
			all = mailItemRepository.findAll(ExpressionUtils.allOf(predicates), pageable);
		}
        MailListResponse response = new MailListResponse();
		/* QMailItem qMailItem = QMailItem.mailItem; */
		/* qMailItem.mailStatus().status.eq(right) */


      /*  Predicate predicate  = BooleanExpression.allOf()
        mailItemRepository.findAll()*/
        response.setData(Lists.newArrayList(all));
        return response;
    }


}
