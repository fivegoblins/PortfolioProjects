package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {
	private long id;
	private long userId;
	private BigDecimal balance;
	
	//GETTERS
	public long getId() {
		return id;
	}
	
	public long getUserId() {
		return userId;
	}
	
	public BigDecimal getBalance() {
		return balance;
	}
	
	//SETTERS
	public void setId(long id) {
		this.id = id;
	}
	
	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
}
