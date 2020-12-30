package com.reactivespring.repository;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import com.reactivespring.document.Item;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * 
 * ItemReactiveRepositoryTest.java
 *
 * @author Lyes Sefiane
 * @email lyes.sefiane@gmail.com
 * @date 12-30-2019 15:30:03
 *
 */
@DataMongoTest
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@DirtiesContext
@ActiveProfiles("test")
public class ItemReactiveRepositoryTest {

	private static final Logger log = LoggerFactory.getLogger(ItemReactiveRepositoryTest.class);

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
	@Order(1)
	public void getAllItems() {
		StepVerifier.create(itemReactiveRepository.findAll())//
				.expectSubscription()//
				.expectNextCount(3)//
				.verifyComplete();
	}

	@Test
	@Order(2)
	public void getItemById() {
		StepVerifier.create(itemReactiveRepository.findById("01"))//
				.expectSubscription()//
				.expectNextMatches(
						item -> item.getDescription().equalsIgnoreCase("Apple Watch") && item.getPrice() == 800.0)
				.verifyComplete();
	}

	@Test
	@Order(3)
	public void findItemByDescription() {
		StepVerifier.create(itemReactiveRepository.findByDescription("Apple Watch").log())//
				.expectSubscription()//
				.expectNextCount(1)//
				.verifyComplete();
	}

	@Test
	@Order(4)
	public void findItemByPriceEquals() {
		StepVerifier.create(itemReactiveRepository.findByPriceEquals(400.0).log())//
				.expectSubscription()//
				.expectNextCount(1)//
				.verifyComplete();
	}

	@Test
	@Order(5)
	public void saveItem() {
		Item item = new Item(null, "Google Home Mini", 30.0);
		Mono<Item> savedItem = itemReactiveRepository.save(item).log();
		StepVerifier.create(savedItem)//
				.expectSubscription()//
				.expectNextMatches(currentItem -> Objects.nonNull(currentItem.getId())//
						&& currentItem.getDescription().equalsIgnoreCase("Google Home Mini"))//
				.verifyComplete();
	}

	@Test
	@Order(6)
	public void updateItem() {
		Mono<Item> updatedItem = itemReactiveRepository.findByDescription("LG TV")//
				.map(item -> {
					item.setPrice(520.0);
					return item;
				})//
				.flatMap(item -> itemReactiveRepository.save(item))//
				.log();

		StepVerifier.create(updatedItem)//
				.expectSubscription()//
				.expectNextMatches(currentUpdatedItem -> currentUpdatedItem.getPrice() == 520.0)//
				.verifyComplete();
	}

	@Test
	@Order(7)
	public void deleteItem() {
		Mono<Void> deletedItem = itemReactiveRepository.findById("01")
				.flatMap(item -> itemReactiveRepository.delete(item)).log();

		StepVerifier.create(deletedItem)//
				.expectSubscription()//
				.expectNextCount(0)//
				.verifyComplete();

	}
}
