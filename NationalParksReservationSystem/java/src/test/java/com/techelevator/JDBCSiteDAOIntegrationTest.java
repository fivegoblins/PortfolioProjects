package com.techelevator;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.model.Site;
import com.techelevator.model.jdbc.JDBCSiteDAO;

public class JDBCSiteDAOIntegrationTest extends DAOIntegrationTest {
	
	private JDBCSiteDAO dao;
	
	@Before
	public void setup() {
		String sqlInsertSite = "INSERT INTO site (campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities) VALUES (7, 6, 5, true, 15, true)";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(DAOIntegrationTest.dataSource); 
		jdbcTemplate.update(sqlInsertSite);
		dao = new JDBCSiteDAO(dataSource);
	}
	
	@Test
	public void test_return_all_sites() {
		List<Site> results = dao.getAllAvailableSites();
		assertNotNull(results);
		assertEquals(623, results.size());
	}
	
	@Test
	public void test_park_id_3_returns_6_sites() {
		List<Site> results = dao.getAllAvailableSitesByParkId(3);
		assertNotNull(results);
		assertEquals(6, results.size());
	}
	
	@Test
	public void test_find_available_sites() {
		List<Site> results = dao.findAvailableSites(3, LocalDate.of(2021, 06, 25), LocalDate.of(2021, 06, 27));
		assertNotNull(results);
		assertEquals(5, results.size());
	}
	

}
