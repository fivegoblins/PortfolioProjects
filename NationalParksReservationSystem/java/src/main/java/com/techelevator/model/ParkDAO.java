package com.techelevator.model;

import java.util.List;

public interface ParkDAO {

	public List<Park> getAllParks();
	//retrieves all park entities from the database and returns them in a List of Park objects

	public Park getParkById(int parkId);
	//retrieves the park from the database with id matching the parkId parameter and returns a Park object
	
	public List<Park> searchParkByName(String name);
	//retrieves any parks from the database with name matching the name parameter and returns them in a List of Park objects
	
}
