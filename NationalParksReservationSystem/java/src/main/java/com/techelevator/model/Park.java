package com.techelevator.model;

import java.time.LocalDate;

public class Park {
	
	//DATA MEMBERS
	private int parkId;
	private String name;
	private String location;
	private LocalDate establishDate;
	private int area;
	private int visitors;
	private String description;
	
	
	//GETTERS
	public int getParkId() {
		return parkId;
	}
	public String getName() {
		return name;
	}
	public String getLocation() {
		return location;
	}
	public LocalDate getEstablishDate() {
		return establishDate;
	}
	public int getArea() {
		return area;
	}
	public int getVisitors() {
		return visitors;
	}
	public String getDescription() {
		return description;
	}
	
	
	//SETTERS
	public void setParkId(int parkId) {
		this.parkId = parkId;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public void setEstablishDate(LocalDate establishDate) {
		this.establishDate = establishDate;
	}
	public void setArea(int area) {
		this.area = area;
	}
	public void setVisitors(int visitors) {
		this.visitors = visitors;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
