package com.techelevator.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Park;
import com.techelevator.model.ParkDAO;

public class JDBCParkDAO implements ParkDAO {
	
	private JdbcTemplate jdbcTemplate;
	
	//CONSTRUCTOR
	public JDBCParkDAO (DataSource datasource) {
		this.jdbcTemplate = new JdbcTemplate(datasource);
	}
	
	
	//INHERITED METHODS
	@Override
	public List<Park> getAllParks() {
		ArrayList<Park> parks = new ArrayList<>();
		
		String findAllParks = "SELECT park_id, name, location, " + 
							  "establish_date, area, visitors, description " +
							  "FROM park " +
							  "ORDER BY name ASC";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(findAllParks);
		
		while(results.next()) {
			Park park = mapRowToPark(results);
			parks.add(park);
		}
		
		return parks;
	}

	@Override
	public Park getParkById(int parkId) {
		Park park = null;
		
		String findParkById = "SELECT park_id, name, location, " +
							  "establish_date, area, visitors, description " +
							  "FROM park " +
							  "WHERE park_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(findParkById, parkId);
		
		if (results.next()) {
			park = mapRowToPark(results);
		}
		return park;
	}

	@Override
	public List<Park> searchParkByName(String name) {
		ArrayList<Park> parks = new ArrayList<>();
		
		String findParkByName = "SELECT park_id, name, location, " +
								"establish_date, area, visitors, description " +
								"FROM park " +
								"WHERE name = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(findParkByName, name);
		
		while(results.next()) {
			Park park = mapRowToPark(results);
			parks.add(park);
		}
		
		return parks;
	}
	
	//PRIVATE METHODS
	private Park mapRowToPark(SqlRowSet results) {
		Park park = new Park();
		
		park.setParkId(results.getInt("park_id"));
		park.setName(results.getString("name"));
		park.setLocation(results.getString("location"));
		park.setEstablishDate(results.getDate("establish_date").toLocalDate());
		park.setArea(results.getInt("area"));
		park.setVisitors(results.getInt("visitors"));
		park.setDescription(results.getString("description"));
		
		return park;
	}

}
