package com.techelevator.model;

import java.time.LocalDate;
import java.util.List;


public interface SiteDAO {

	//	returns a list of available camp sites from a parkId
	public List<Site> getAllAvailableSitesByParkId(int parkId);
	
	// returns a list of available camp sites from all parks
	public List<Site> getAllAvailableSites();
	
	public List<Site> findAvailableSites(int campgroundId, LocalDate arrivalDate, LocalDate departureDate);
	
	public List<Site> findAvailableSitesByParkId(int parkId, LocalDate arrivalDate, LocalDate departureDate);
	
	public List<Site> advancedSearchAvailableSites(int campgroundId, int maxOccupants, 
			boolean needsAccessibility, int rvLength, boolean needsUtility, LocalDate arrivalDate, LocalDate departureDate);
}
