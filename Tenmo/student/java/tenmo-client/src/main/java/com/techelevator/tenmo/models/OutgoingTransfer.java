package com.techelevator.tenmo.models;

import java.math.BigDecimal;

public class OutgoingTransfer {
	private long id;
	private long transferType;
	private long transferStatus;
	private long accountFrom;
	private long accountTo;
	private BigDecimal amount;
	
	public OutgoingTransfer() {
		
	}
	
	public OutgoingTransfer(long id) {
		super();
		this.id = id;
	}
	
	public long getId() {
		return id;
	}
	public long getTransferType() {
		return transferType;
	}
	public long getTransferStatus() {
		return transferStatus;
	}
	public long getAccountFrom() {
		return accountFrom;
	}
	public long getAccountTo() {
		return accountTo;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setTransferType(long transferType) {
		this.transferType = transferType;
	}
	public void setTransferStatus(long transferStatus) {
		this.transferStatus = transferStatus;
	}
	public void setAccountFrom(long accountFrom) {
		this.accountFrom = accountFrom;
	}
	public void setAccountTo(long accountTo) {
		this.accountTo = accountTo;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	

}
