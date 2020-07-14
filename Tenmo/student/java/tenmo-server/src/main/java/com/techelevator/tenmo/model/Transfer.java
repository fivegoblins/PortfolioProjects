package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {
	
	private long id;
	private String transferType;
	private String transferStatus;
	private String accountFrom;
	private String accountTo;
	private BigDecimal amount;
	
	//GETTERS
	public long getId() {
		return id;
	}
	
	public String getTransferType() {
		return transferType;
	}
	
	public String getTransferStatus() {
		return transferStatus;
	}
	
	public String getAccountFrom() {
		return accountFrom;
	}
	
	public String getAccountTo() {
		return accountTo;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}
	
	//SETTERS
	public void setId(long id) {
		this.id = id;
	}
	
	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}
	
	public void setTransferStatus(String transferStatus) {
		this.transferStatus = transferStatus;
	}
	
	public void setAccountFrom(String accountFrom) {
		this.accountFrom = accountFrom;
	}
	
	public void setAccountTo(String accountTo) {
		this.accountTo = accountTo;
	}
	
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	
}
