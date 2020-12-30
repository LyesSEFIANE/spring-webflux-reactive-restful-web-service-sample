package com.reactivespring.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.reactivespring.handler.ItemHandler;

/**
 * 
 * ItemRouter.java
 *
 * @author Lyes Sefiane
 * @email lyes.sefiane@gmail.com
 * @date 12-31-2019 14:24:39
 *
 */
@Configuration
public class ItemRouter {

	@Bean
	public RouterFunction<ServerResponse> itemsRouter(ItemHandler itemHandler) {
		return RouterFunctions.route(GET("/v1/items"), itemHandler::getAllItems)//
				.andRoute(GET("/v1/items/{id}"), itemHandler::getItem)
				.andRoute(POST("/v1/items"), itemHandler::createItem)
				.andRoute(DELETE("/v1/items/{id}"), itemHandler::deleteItem)
				.andRoute(PUT("/v1/items/{id}"), itemHandler::updateItem);
	}

}
