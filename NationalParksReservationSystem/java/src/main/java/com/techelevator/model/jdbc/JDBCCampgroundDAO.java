package com.techelevator.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Campground;
import com.techelevator.model.CampgroundDAO;

public class JDBCCampgroundDAO implements CampgroundDAO {
	
	private JdbcTemplate jdbcTemplate;
	
	//CONSTRUCTOR
	public JDBCCampgroundDAO (DataSource datasource) {
		this.jdbcTemplate = new JdbcTemplate(datasource);
	}

	
	//INHERITED METHODS
	@Override
	public List<Campground> getAllCampgroundsByParkId(int parkId) {
		ArrayList<Campground> campgrounds = new ArrayList<>();
		
		String findCampgroundsInPark = "SELECT campground_id, park_id, name, " + 
										"open_from_mm, open_to_mm, daily_fee " +
										"FROM campground " +
										"WHERE park_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(findCampgroundsInPark, parkId);
		
		while(results.next()) {
			Campground campground = mapRowToCampground(results);
			campgrounds.add(campground);
	
		}
		
		return campgrounds;
	}

	@Override
	public Campground getCampgroundById(int campgroundId) {
		Campground campground = null;
		
		String findCampgroundById = "SELECT campground_id, park_id, name, " +
									"open_from_mm, open_to_mm, daily_fee " +
									"FROM campground " +
									"WHERE campground_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(findCampgroundById, campgroundId);
		
		if (results.next()) {
			campground = mapRowToCampground(results);
		}
		
		return campground;
	}
	
	
	//PRIVATE METHODS
	private Campground mapRowToCampground(SqlRowSet results) {
		Campground campground = new Campground();
		
		campground.setCampgroundId(results.getInt("campground_id"));
		campground.setParkId(results.getInt("park_id"));
		campground.setName(results.getString("name"));
		campground.setOpenMonth(results.getString("open_from_mm"));
		campground.setOpenMonth((results.getString("open_from_mm")));
		campground.setClosedMonth(results.getString("open_to_mm"));
		campground.setDailyFee(results.getBigDecimal("daily_fee"));
		
		return campground;
	}

}
