package com.techelevator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.model.Campground;
import com.techelevator.model.jdbc.JDBCCampgroundDAO;

public class JDBCCampgroundIntegrationTest extends DAOIntegrationTest {
	
	private JDBCCampgroundDAO dao;
	
	@Before
	public void setup() {
//		String sqlInsertCampground = "INSERT INTO site (campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities) VALUES (7, 6, 5, true, 15, true)";
//		JdbcTemplate jdbcTemplate = new JdbcTemplate(DAOIntegrationTest.dataSource); 
//		jdbcTemplate.update(sqlInsertCampground);
		dao = new JDBCCampgroundDAO(dataSource);
	}

	@Test
	public void test_park_id_2_returns_3_campgrounds() {
		List<Campground> results = dao.getAllCampgroundsByParkId(2);
		assertNotNull(results);
		assertEquals(3, results.size());
	}
	
	@Test
	public void test_campground_id_2_returns_seawall() {
		Campground results = dao.getCampgroundById(2);
		assertNotNull(results);
		String campName = results.getName();
		System.out.println(campName);
		assertEquals("Seawall", results.getName());
	}
	
}
