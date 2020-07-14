package com.techelevator.model;

import java.util.List;

public interface CampgroundDAO {

	
	public List<Campground> getAllCampgroundsByParkId(int parkId);
	//retrieves any campgrounds with park_id fk matching the parkId parameter 
	//and returns them in a List of Campground objects 
	
	public Campground getCampgroundById(int campgroundId);
	//retrieves campground with id matching campgroundId param and returns a Campground object
	
}
