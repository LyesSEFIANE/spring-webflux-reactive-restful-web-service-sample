package com.reactivespring.handler;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.reactivespring.document.Item;
import com.reactivespring.repository.ItemReactiveRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 
 * ItemHandlerTest.java
 *
 * @author Lyes Sefiane
 * @email lyes.sefiane@gmail.com
 * @date 12-31-2019 15:16:18
 *
 */
@AutoConfigureWebTestClient
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@DirtiesContext
@ActiveProfiles("test")
@SpringBootTest
public class ItemHandlerTest {

	private static final Logger log = LoggerFactory.getLogger(ItemHandlerTest.class);

	@Autowired
	WebTestClient webTestClient;

	@Autowired
	ItemReactiveRepository itemReactiveRepository;

	List<Item> items = Arrays.asList(//
			new Item(null, "Samsung TV", 400.0), //
			new Item(null, "LG TV", 420.0), //
			new Item("01", "Apple Watch", 800.0));

	@BeforeAll
	public void setup() {
		itemReactiveRepository//
				.deleteAll()//
				.thenMany(Flux.fromIterable(items))//
				.flatMap(itemReactiveRepository::save)//
				.doOnNext(savedElement -> log.info("Saved Item = {}", savedElement)).blockLast();
	}
  
	@Test
	@Order(2)
	public void getExistingItem() {
		webTestClient//
				.get()//
				.uri("/v1/items/{id}", "01")//
				.exchange()//
				.expectStatus()//
				.isOk()//
				.expectHeader().contentType(MediaType.APPLICATION_JSON)//
				.expectBody().jsonPath("$.price", 800.0);
	}

	@Test
	@Order(3)
	public void getNonExistingItem() {
		webTestClient//
				.get()//
				.uri("/v1/items/{id}", "AB")//
				.exchange()//
				.expectStatus()//
				.isNotFound();
	}

	@Test
	@Order(4)
	public void createItem() {
		Item createdItem = new Item(null, "Iphone X", 999.99);

		webTestClient//
				.post()//
				.uri("/v1/items")//
				.contentType(MediaType.APPLICATION_JSON)//
				.body(Mono.just(createdItem), Item.class)//
				.exchange()//
				.expectStatus()//
				.isCreated();
	}

	@Test
	@Order(5)
	public void deleteItem() {
		webTestClient//
				.delete()//
				.uri("/v1/items/{id}", "01")//
				.accept(MediaType.APPLICATION_JSON)//
				.exchange()//
				.expectStatus()//
				.isOk()//
				.expectBody(Void.class);
	}

	@Test
	@Order(6)
	public void updateItem() {
		Item item = new Item(null, "Apple Watch II", 1000.00);
		webTestClient//
				.put()//
				.uri("/v1/items/{id}", "01")//
				.accept(MediaType.APPLICATION_JSON)//
				.body(Mono.just(item), Item.class).exchange()//
				.expectStatus()//
				.isOk()//
				.expectBody().jsonPath("$.price", 1000.00);
	}

}
