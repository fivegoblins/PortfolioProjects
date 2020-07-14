package com.techelevator.model;

import java.math.BigDecimal;

public class Campground {
	
	//DATA MEMBERS
	private int campgroundId;
	private int parkId;
	private String name;
	private String openMonth;
	private String closedMonth;
	private BigDecimal dailyFee;
	
	
	//GETTERS
	public int getCampgroundId() {
		return campgroundId;
	}
	public int getParkId() {
		return parkId;
	}
	public String getName() {
		return this.name;
	}
	public String getOpenMonth() {
		return openMonth;
	}
	public String getClosedMonth() {
		return closedMonth;
	}
	public BigDecimal getDailyFee() {
		return dailyFee;
	}
	
	
	//SETTERS
	public void setCampgroundId(int campgroundId) {
		this.campgroundId = campgroundId;
	}
	public void setParkId(int parkId) {
		this.parkId = parkId;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setOpenMonth(String openMonth) {
		this.openMonth = openMonth;
	}
	public void setClosedMonth(String closedMonth) {
		this.closedMonth = closedMonth;
	}
	public void setDailyFee(BigDecimal dailyFee) {
		this.dailyFee = dailyFee;
	}
	

}
