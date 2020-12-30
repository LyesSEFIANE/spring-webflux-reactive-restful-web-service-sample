package com.reactivespring.reactivewebclient.domain;

/**
 * 
 * Item.java
 *
 * @author Lyes Sefiane
 * @email lyes.sefiane@gmail.com
 * @date 01-03-2020 20:40:53
 *
 */
public class Item {

	private String id;
	private String description;
	private double price;
	
	public Item() {
		super();
	}

	public Item(String id, String description, double price) {
		super();
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
}
