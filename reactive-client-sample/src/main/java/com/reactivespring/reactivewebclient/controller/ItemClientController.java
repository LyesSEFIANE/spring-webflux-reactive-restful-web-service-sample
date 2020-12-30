package com.reactivespring.reactivewebclient.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.reactivespring.reactivewebclient.domain.Item;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * 
 * ItemClientController.java
 *
 * @author Lyes Sefiane
 * @email lyes.sefiane@gmail.com
 * @date 01-03-2020 20:40:34
 *
 */
@RestController
@RequestMapping("/client/items")
public class ItemClientController {

	private WebClient webClient = WebClient.create("http://localhost:8080");

	@GetMapping()
	public Flux<Item> getAllItems() {
		return webClient//
				.get()//
				.uri("/v1/items")//
				.retrieve()//
				.bodyToFlux(Item.class)//
				.log();
	}

	@GetMapping("/{id}")
	public Mono<Item> getItem(@PathVariable String id) {
		return webClient.get().uri("/v1/items/{id}", id)//
				.retrieve()//
				.onStatus(HttpStatus::isError, clientResponse -> {
					return clientResponse.bodyToMono(String.class).flatMap(e -> {
						throw new RuntimeException(e);
					});
				})//
				.bodyToMono(Item.class)//
				.log();

	}

	@PostMapping()
	public Mono<Item> createItem(@RequestBody Item createdItem) {
		return webClient//
				.post()//
				.uri("/v1/items")//
				.contentType(MediaType.APPLICATION_JSON)//
				.body(Mono.just(createdItem), Item.class)//
				.retrieve()//
				.bodyToMono(Item.class)//
				.log();

	}

	@PutMapping("/{id}")
	public Mono<Item> updateItem(@PathVariable String id, @RequestBody Item item) {
		return webClient//
				.put()//
				.uri("/v1/items/{id}", id)//
				.contentType(MediaType.APPLICATION_JSON)//
				.body(Mono.just(item), Item.class)//
				.retrieve()//
				.bodyToMono(Item.class)//
				.log();

	}

	@DeleteMapping("/{id}")
	public Mono<Void> deleteItem(@PathVariable String id) {
		return webClient//
				.delete()//
				.uri("/v1/items/{id}", id)//
				.retrieve()//
				.bodyToMono(Void.class)//
				.log();
	}

}
