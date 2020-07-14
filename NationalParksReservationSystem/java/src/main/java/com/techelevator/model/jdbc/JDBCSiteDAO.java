package com.techelevator.model.jdbc;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Site;
import com.techelevator.model.SiteDAO;

public class JDBCSiteDAO implements SiteDAO{
	
	private JdbcTemplate jdbcTemplate;
	
	// CONSTRUCTOR
	public JDBCSiteDAO (DataSource datasource) {
		this.jdbcTemplate = new JdbcTemplate(datasource);
	}
	
	
	//INHERITED METHODS
	@Override
	public List<Site> getAllAvailableSitesByParkId(int parkId) {
		ArrayList<Site> sites = new ArrayList<>();
		
		String sqlSitesByParkId = "SELECT site.site_id, site.campground_id, site.site_number, "
				+ "site.max_occupancy, site.accessible, site.max_rv_length, site.utilities "
				+ "FROM site "
				+ "JOIN campground ON site.campground_id = campground.campground_id "
				+ "WHERE campground.park_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSitesByParkId, parkId);

		while(results.next()) {
			Site site = mapRowToSite(results);
			sites.add(site);
		}
		return sites;
	}
	
	@Override
	public List<Site> getAllAvailableSites() {
		ArrayList<Site> sites = new ArrayList<>();
		
		String sqlFindAllSites = "SELECT site_id, campground_id, site_number, "
								+ "max_occupancy, accessible, max_rv_length, utilities "
								+ "FROM site";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindAllSites);
		
		while(results.next()) {
			Site site = mapRowToSite(results);
			sites.add(site);
		}
			
		return sites;
	}
	
	public List<Site> findAvailableSites(int campgroundId, LocalDate arrivalDate, LocalDate departureDate) {
		ArrayList<Site> available = new ArrayList<>();
		
		String sqlGetAvailableSites =  
				"SELECT site.site_id, site.campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities " + 
				"FROM site " + 
				"LEFT JOIN reservation " + 
				"ON site.site_id = reservation.site_id " + 
				"JOIN campground " + 
				"ON site.campground_id = campground.campground_id " + 
				"WHERE campground.campground_id = ? " + 
				"AND (reservation.to_date < ? OR reservation.from_date > ?) " +
				"OR (reservation.reservation_id IS NULL) " +
				"LIMIT 5";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAvailableSites, campgroundId, arrivalDate, departureDate);
		
		while(results.next()) {
			Site site = mapRowToSite(results);
			available.add(site);
		}
		
		return available;
	}
	
	public List<Site> advancedSearchAvailableSites(int campgroundId, int maxOccupants, 
			boolean needsAccessibility, int rvLength, boolean needsUtility, LocalDate arrivalDate, LocalDate departureDate) {
		ArrayList<Site> available = new ArrayList<>();
		
		String getAdvancedSearchAvailableSites = "SELECT site.site_id, site.campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities " + 
													"FROM site " + 
													"LEFT JOIN reservation " + 
													"ON site.site_id = reservation.site_id " + 
													"JOIN campground " + 
													"ON site.campground_id = campground.campground_id " + 
													"WHERE campground.campground_id = ? " + 
													"AND max_occupancy >= ? AND accessible = ? AND max_rv_length >= ? AND utilities = ? " +
													"AND (reservation.to_date < ? OR reservation.from_date > ?) " +
													"OR (reservation.reservation_id IS NULL) " +
													"LIMIT 5";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(getAdvancedSearchAvailableSites, campgroundId, maxOccupants, needsAccessibility,
				rvLength, needsUtility, arrivalDate, departureDate);
		
		while(results.next()) {
			Site site = mapRowToSite(results);
			available.add(site);
		}
		
		return available;
		
	}
	
	public List<Site> findAvailableSitesByParkId(int parkId, LocalDate arrivalDate, LocalDate departureDate) {
		ArrayList<Site> available = new ArrayList<>();
		
		String sqlGetAvailableSites =  
				"SELECT site.site_id, site.campground_id, site_number, max_occupancy, "
				+ "accessible, max_rv_length, utilities FROM site "
				+ "LEFT JOIN reservation ON site.site_id = reservation.site_id "
				+ "JOIN campground ON site.campground_id = campground.campground_id "
				+ "WHERE campground.park_id = ? "
				+ "AND ((reservation.to_date < ? OR reservation.from_date > ?) "
				+ "OR (reservation.reservation_id IS NULL)) "
				+ "LIMIT 5";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAvailableSites, parkId, arrivalDate, departureDate);
		
		while(results.next()) {
			Site site = mapRowToSite(results);
			available.add(site);
		}
		
		return available;
	}
	
	
	

	
	
	
	
	
	
	//PRIVATE METHODS
	private Site mapRowToSite(SqlRowSet results) {
		Site site = new Site();
		
		site.setSiteId(results.getInt("site_id"));
		site.setCampgroundId(results.getInt("campground_id"));
		site.setSiteNumber(results.getInt("site_number"));
		site.setMaxOccupancy(results.getInt("max_occupancy"));
		site.setAccessible(results.getBoolean("accessible"));
		site.setMaxRvLength(results.getInt("max_rv_length"));
		site.setHasUtilities(results.getBoolean("utilities"));
		
		return site;
	}
	
}
