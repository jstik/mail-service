package com.rest.mail;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.config.SwaggerConfig;
import com.rest.OAuth2Util;
import com.utill.CookieUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.dao.MailItemRepository;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.collect.Lists;
import com.mail.MailManager;
import com.model.entity.MailItem;
import com.model.entity.QMailItem;
import com.querydsl.core.types.ExpressionUtils;
import com.rest.JsonManager;
import com.rest.filter.Column;
import com.rest.filter.FilterUtil;

import javassist.NotFoundException;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Julia on 22.06.2017.
 */
@Controller
public class MailListController {
	private static Log logger = LogFactory.getLog(MailListController.class);
	@Autowired
	private MailItemRepository mailItemRepository;
	@Autowired
	private JsonManager jsonManager;

	@Autowired
	private MailManager mailManager;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ApiOperation(value = "Entry point for loading index.htm",
			authorizations = {@Authorization(value = SwaggerConfig.securitySchemaOAuth2, scopes =
					{@AuthorizationScope( scope = SwaggerConfig.authorizationScopeGlobal,
							description = SwaggerConfig.authorizationScopeGlobalDesc)})})
	public Object showMailList(HttpServletResponse response) {
		String token = OAuth2Util.tokenValue();
		if (token != null) {
			CookieUtil.create(response, "Authorization", token, false, -1, null, false);
		}
		return "/index";
	}

	@RequestMapping(value = "/mails/schema/schema.json", method = RequestMethod.GET)
	@ApiOperation(value = "Return Json schema for mail item",
			authorizations = {@Authorization(value = SwaggerConfig.securitySchemaOAuth2, scopes =
					{@AuthorizationScope( scope = SwaggerConfig.authorizationScopeGlobal,
							description = SwaggerConfig.authorizationScopeGlobalDesc)})})
	public @ResponseBody Object generateMailSchema() throws JsonMappingException {
		return jsonManager.generateSchema(MailItem.class);
	}


	@RequestMapping(value = "/mails/mails.json", method = RequestMethod.GET)
	@ApiOperation(value = "Return mail list with given filtering",
			authorizations = {@Authorization(value = SwaggerConfig.securitySchemaOAuth2, scopes =
					{@AuthorizationScope( scope = SwaggerConfig.authorizationScopeGlobal,
							description = SwaggerConfig.authorizationScopeGlobalDesc)})})
	public @ResponseBody Object getMails(@RequestParam(name = "filter") String filter,
								  @RequestParam(name = "page") int page) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		ArrayNode arrayNode = objectMapper.readValue(filter, ArrayNode.class);
		List<Column> columns = FilterUtil.getColumns(arrayNode, MailItem.class);
		Collection<com.querydsl.core.types.Predicate> predicates = columns.stream().map(c -> {
			try {
				return (com.querydsl.core.types.Predicate) c.getPredicate(QMailItem.mailItem);
			} catch (Exception e) {
				logger.error("Couldn't create predicate", e);
				return null;
			}
		}).filter(Objects::nonNull).collect(Collectors.toList());
		PageRequest pageable = new PageRequest(page, 30);
		Iterable<MailItem> all;
		if (predicates.isEmpty()) {
			all = mailItemRepository.findAll(pageable);
		} else {
			all = mailItemRepository.findAll(ExpressionUtils.allOf(predicates), pageable);
		}
		MailListResponse response = new MailListResponse();
		response.setData(Lists.newArrayList(all));
		return response;
	}

	@RequestMapping(value = "/mails/delete", method = RequestMethod.POST)
	@ApiOperation(value = "Delete Mail",
			authorizations = {@Authorization(value = SwaggerConfig.securitySchemaOAuth2, scopes =
					{@AuthorizationScope( scope = SwaggerConfig.authorizationScopeGlobal,
							description = SwaggerConfig.authorizationScopeGlobalDesc)})})
	public @ResponseBody Object removeMail(@RequestBody List<Long> ids) {
		if (ids.isEmpty())
			return false;
		Iterable<MailItem> delete = mailItemRepository.findAll(QMailItem.mailItem.id.in(ids));
		mailItemRepository.delete(delete);
		return true;
	}

	@RequestMapping(value = "/mails/resend", method = RequestMethod.GET)
	@ApiOperation(value = "Resend Mail",
			authorizations = {@Authorization(value = SwaggerConfig.securitySchemaOAuth2, scopes =
					{@AuthorizationScope( scope = SwaggerConfig.authorizationScopeGlobal,
							description = SwaggerConfig.authorizationScopeGlobalDesc)})})
	public @ResponseBody Object resend(@RequestParam(name = "id") Long id) throws NotFoundException, IOException {
		MailItem mail = mailItemRepository.findOne(id);
		if (mail == null)
			throw new NotFoundException("Couldn't find mail item with id = " + id);
		mailManager.toSendQueue(mail);
		return true;
	}

}
