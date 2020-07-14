package com.techelevator;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.techelevator.model.Park;
import com.techelevator.model.jdbc.JDBCParkDAO;

public class JDBCParkIntegrationTest extends DAOIntegrationTest {

	private JDBCParkDAO dao;
	
	@Before
	public void setup() {
//		String sqlInsert = "INSERT INTO site (campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities) VALUES (7, 6, 5, true, 15, true)";
//		JdbcTemplate jdbcTemplate = new JdbcTemplate(DAOIntegrationTest.dataSource); 
//		jdbcTemplate.update(sqlInsertCampground);
		dao = new JDBCParkDAO(dataSource);
	}
	
	
	@Test
	public void test_retrieve_all_parks() {
		List<Park> results = dao.getAllParks();
		fail("Not yet implemented");
	}
	
	@Test
	public void test_retrieve_park_by_id() {
//		List<Park> results = dao.getParkById(_____):
		fail("Not yet implemented");
	}

	@Test
	public void test_search_park_by_name() {
//		dao.searchParkByName("________");
		fail("Not yet implemented");
	}
	

}
