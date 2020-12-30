package com.reactivespring.initialize;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.reactivespring.document.Item;
import com.reactivespring.repository.ItemReactiveRepository;

import reactor.core.publisher.Flux;

/**
 * 
 * ItemDataInitializer.java
 *
 * @author Lyes Sefiane
 * @email lyes.sefiane@gmail.com
 * @date 12-31-2019 14:19:59
 *
 */
@Component
@Profile("!test")
public class ItemDataInitializer implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(ItemDataInitializer.class);

	@Autowired
	private ItemReactiveRepository itemReactiveRepository;

	@Override
	public void run(String... args) throws Exception {
		initialDataSetup();
	}

	public List<Item> data() {
		return Arrays.asList(//
				new Item(null, "Samsung TV", 400.0), //
				new Item(null, "LG TV", 420.0), //
				new Item("01", "Apple Watch", 800.0));
	}

	private void initialDataSetup() {
		itemReactiveRepository.deleteAll()//
				.thenMany(Flux.fromIterable(data()))//
				.flatMap(itemReactiveRepository::save)//
				.subscribe(item -> log.info("Item inserted from CommandLineRunner = {}", item));
	}

}
