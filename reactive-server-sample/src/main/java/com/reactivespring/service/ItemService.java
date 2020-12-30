package com.reactivespring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.reactivespring.document.Item;
import com.reactivespring.repository.ItemReactiveRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 
 * ItemService.java
 *
 * @author Lyes Sefiane
 * @email lyes.sefiane@gmail.com
 * @date 01-01-2020 14:27:09
 *
 */
@Service
public class ItemService {

	@Autowired
	private ItemReactiveRepository itemReactiveRepository;

	/**
	 * Find All Items
	 * 
	 * @return Flux<Item>
	 */
	public Flux<Item> findAll() {
		return itemReactiveRepository.findAll();
	}

	/**
	 * Find By Id
	 * 
	 * @param id
	 * @return Mono<Item>
	 */
	public Mono<Item> findById(String id) {
		return itemReactiveRepository.findById(id)//
				.switchIfEmpty(itemNotFound(id));
	}

	/**
	 * Create Item
	 * 
	 * @param id
	 * @param description
	 * @param price
	 * @return Mono<Item>
	 */
	public Mono<Item> createItem(String id, String description, double price) {
		return itemReactiveRepository.save(new Item(id, description, price));
	}

	/**
	 * Delete Item
	 * 
	 * @param id
	 * @return Mono<Item>
	 */
	public Mono<Item> deleteItem(String id) {
		return findById(id)//
				.flatMap(item -> itemReactiveRepository.delete(item).thenReturn(item));
	}

	/**
	 * Update Item
	 * 
	 * @param id
	 * @param description
	 * @param price
	 * @return Mono<Item>
	 */
	public Mono<Item> updateItem(String id, String description, double price) {
		return findById(id)//
				.map(item -> new Item(item.getId(), description, price))//
				.flatMap(itemReactiveRepository::save);
	}

	/**
	 * Item Not Found
	 * 
	 * @param id
	 * @return Mono<ServerResponse>
	 */
	private Mono<? extends Item> itemNotFound(String id) {
		return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Item Id = [" + id + "] Not Found."));
	}

}
