package com.reactivespring.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.reactivespring.document.Item;

import reactor.core.publisher.Mono;

/**
 * 
 * ItemReactiveRepository.java
 *
 * @author Lyes Sefiane
 * @email lyes.sefiane@gmail.com
 * @date 12-30-2019 15:27:07
 *
 */
public interface ItemReactiveRepository extends ReactiveMongoRepository<Item, String> {

	Mono<Item> findByDescription(String itemDescription);

	Mono<Item> findByPriceEquals(double price);
}
