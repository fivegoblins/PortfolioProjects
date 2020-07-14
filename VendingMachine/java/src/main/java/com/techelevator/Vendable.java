package com.techelevator;

public abstract class Vendable {
	
	private String slot;
	private String itemName;
	private double price;
	private int numberAvailable = 5;
	private String type;
	
	
	public Vendable(String slot, String itemName, double price, String type) {
		this.slot = slot;
		this.itemName = itemName;
		this.price = price;
		this.type = type;
	}

	public void decrement() {
		numberAvailable --;
		
	}

	public String getSlot() {
		return slot;
	}


	public String getItemName() {
		return itemName;
	}


	public double getPrice() {
		return price;
	}
	
	public String getType() {
		return type;
	}


	public int getNumberAvailable() {
		return numberAvailable;
	}

}
