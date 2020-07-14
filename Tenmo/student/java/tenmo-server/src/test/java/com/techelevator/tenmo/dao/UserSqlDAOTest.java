package com.techelevator.tenmo.dao;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class UserSqlDAOTest {
	
	private static SingleConnectionDataSource dataSource;
	private UserSqlDAO dao;
	private JdbcTemplate jdbcTemplate;
	
	@BeforeClass
	public static void setUpDataSource() throws Exception {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("http://localhost:8080/");
		dataSource.setAutoCommit(false);
	}

	@AfterClass
	public static void closeDataSource() throws Exception {
		dataSource.destroy();
	}

	@Before
	public void setUp() throws Exception {
		dao = new UserSqlDAO(jdbcTemplate);
	}

	@After
	public void tearDown() throws Exception {
		dataSource.getConnection().rollback();
	}
	
	@Test
	public void createNewUser() {
		assertTrue(dao.create("testUser", "password"));
	}

}
