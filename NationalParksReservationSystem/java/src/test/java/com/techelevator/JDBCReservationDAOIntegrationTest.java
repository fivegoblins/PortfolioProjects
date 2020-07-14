package com.techelevator;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.techelevator.model.jdbc.JDBCReservationDAO;

public class JDBCReservationDAOIntegrationTest extends DAOIntegrationTest {

	private JDBCReservationDAO dao;
	
	@Before
	public void setup() {
//		String sqlInsert = "INSERT INTO site (campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities) VALUES (7, 6, 5, true, 15, true)";
//		JdbcTemplate jdbcTemplate = new JdbcTemplate(DAOIntegrationTest.dataSource); 
//		jdbcTemplate.update(sqlInsertCampground);
		dao = new JDBCReservationDAO(dataSource);
	}
	
	@Test
	public void getAllReservationsByParkId() {
		fail("Not yet implemented");
	}
	
	@Test
	public void getReservationById() {
		fail("Not yet implemented");
	}
	
	@Test
	public void addReservation() {
		fail("Not yet implemented");
	}

}
