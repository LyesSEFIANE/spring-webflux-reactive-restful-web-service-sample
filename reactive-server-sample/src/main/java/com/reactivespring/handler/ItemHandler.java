package com.reactivespring.handler;

import java.net.URI;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.reactivespring.document.Item;
import com.reactivespring.service.ItemService;

import reactor.core.publisher.Mono;

/**
 * 
 * ItemHandler.java
 *
 * @author Lyes Sefiane
 * @email lyes.sefiane@gmail.com
 * @date 01-01-2020 11:57:44
 *
 */
@Component
@Scope("prototype")
public class ItemHandler {

	@Autowired
	private ItemService itemService;

	/**
	 * Get All Item
	 * 
	 * @param serverRequest
	 * @return Mono<ServerResponse>
	 */
	public Mono<ServerResponse> getAllItems(ServerRequest serverRequest) {
		return defaultReadResponse(itemService.findAll(), MediaType.APPLICATION_STREAM_JSON);
	}

	/**
	 * Get Item
	 * 
	 * @param serverRequest
	 * @return Mono<ServerResponse>
	 */
	public Mono<ServerResponse> getItem(ServerRequest serverRequest) {
		String id = retrieveId(serverRequest);
		return defaultReadResponse(itemService.findById(id));
	}

	/**
	 * Create Item
	 * 
	 * @param serverRequest
	 * @return Mono<ServerResponse>
	 */
	public Mono<ServerResponse> createItem(ServerRequest serverRequest) {
		Mono<Item> itemMono = serverRequest//
				.bodyToMono(Item.class)//
				.flatMap(item -> itemService.createItem(item.getId(), item.getDescription(), item.getPrice()));
		return defaultWriteResponse(itemMono);
	}

	/**
	 * Delete Item
	 * 
	 * @param serverRequest
	 * @return Mono<ServerResponse>
	 */
	public Mono<ServerResponse> deleteItem(ServerRequest serverRequest) {
		Mono<Item> deletedItem = serverRequest//
				.bodyToMono(Item.class)//
				.flatMap(item -> itemService.deleteItem(item.getId()));
		return defaultWriteResponse(deletedItem);
	}

	/**
	 * Update Item
	 * 
	 * @param serverRequest
	 * @return Mono<ServerResponse>
	 */
	public Mono<ServerResponse> updateItem(ServerRequest serverRequest) {
		Mono<Item> itemToUpdate = serverRequest//
				.bodyToMono(Item.class)//
				.flatMap(item -> itemService.updateItem(retrieveId(serverRequest), item.getDescription(),
						item.getPrice()));
		return defaultReadResponse(itemToUpdate);
	}

	/**
	 * Default Read Response
	 * 
	 * @param publisher
	 * @return Mono<ServerResponse>
	 */
	private Mono<ServerResponse> defaultReadResponse(Publisher<Item> publisher) {
		return defaultReadResponse(publisher, MediaType.APPLICATION_JSON);
	}

	/**
	 * Default Read Response
	 * 
	 * @param publisher
	 * @param mediaType
	 * @return Mono<ServerResponse>
	 */
	private Mono<ServerResponse> defaultReadResponse(Publisher<Item> publisher, MediaType mediaType) {
		return ServerResponse//
				.ok()//
				.contentType(mediaType)//
				.body(publisher, Item.class);
	}

	/**
	 * Default Write Response
	 * 
	 * @param publisher
	 * @return Mono<ServerResponse>
	 */
	private Mono<ServerResponse> defaultWriteResponse(Publisher<Item> publisher) {
		return Mono.from(publisher)//
				.flatMap(item -> ServerResponse//
						.created(URI.create("/item/" + item.getId()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.build());
	}

	/**
	 * Retrieve Id
	 * 
	 * @param serverRequest
	 * @return String
	 */
	private String retrieveId(ServerRequest serverRequest) {
		return serverRequest.pathVariable("id");
	}
}
