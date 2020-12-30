package com.reactivespring.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
 * Item.java
 *
 * @author Lyes Sefiane
 * @email lyes.sefiane@gmail.com
 * @date 12-30-2019 15:18:17
 *
 */
@Document
public class Item {

	@Id
	private String id;
	private String description;
	private double price;

	public Item(String id, String description, double price) {
		this.id = id;
		this.description = description;
		this.price = price;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Item [getId()=" + getId() + ", getDescription()=" + getDescription() + ", getPrice()=" + getPrice()
				+ "]";
	}

}
