package com.techelevator.model.jdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Reservation;
import com.techelevator.model.ReservationDAO;

public class JDBCReservationDAO implements ReservationDAO {

	private JdbcTemplate jdbcTemplate;
	
	// CONSTRUCTOR
	public JDBCReservationDAO (DataSource datasource) {
		this.jdbcTemplate = new JdbcTemplate(datasource);
	}

	
	// INHERITED METHODS
	public List<Reservation> getAllReservationsByParkId(int parkId){
		ArrayList<Reservation> reservations = new ArrayList<>();
		
		String sqlReservationsByParkId = "SELECT reservation.reservation_id, reservation.site_id, reservation.name, reservation.from_date, "
										+ "reservation.to_date, reservation.create_date "
										+ "FROM reservation "
										+ "JOIN site ON reservation.site_id = site.site_id "
										+ "JOIN campground ON site.campground_id = campground.campground_id "
										+ "WHERE campground.park_id = ? "
										+ "AND reservation.from_date IN(CURRENT_DATE, (CURRENT_DATE + INT '30'))";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlReservationsByParkId, parkId);

		while(results.next()) {
			Reservation reservation = mapRowToReservation(results);
			reservations.add(reservation);
		}
		return reservations;
	}
	

	public Reservation getReservationById(int reservationId){
		Reservation reservation = null;
		
		String sqlReservationsById = "SELECT reservation.reservation_id, reservation.site_id, reservation.name, reservation.from_date, "
										+ "reservation.to_date, reservation.create_date "
										+ "FROM reservation "
										+ "WHERE reservation_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlReservationsById, reservationId);

		if (results.next()) {
			reservation = mapRowToReservation(results);
		}
		return reservation;
	}

	public Reservation addReservation(Reservation newReservation) {
		String sqlInsertReservation = "INSERT INTO reservation (site_id, name, from_date, to_date, create_date) VALUES (?, ?, ?, ?, CURRENT_DATE)";
		jdbcTemplate.update(sqlInsertReservation, newReservation.getSiteId(), newReservation.getName(), newReservation.getFromDate(),
				newReservation.getToDate());
		return newReservation;
	}
	
	public List<Reservation> readBackReservation(String name, LocalDate fromDate, LocalDate toDate) {
		String readBackReservation = "SELECT * FROM reservation WHERE name = ? AND from_date = ? AND to_date = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(readBackReservation, name, fromDate, toDate);
		
		ArrayList<Reservation> reservations = new ArrayList<>();
		
		if(results.next()) {
			Reservation reservation = mapRowToReservation(results);
			reservations.add(reservation);
		}
		return reservations;
	}
	
	//PRIVATE METHODS
	private Reservation mapRowToReservation(SqlRowSet results) {
		Reservation reservation = new Reservation();
		
		reservation.setReservationId(results.getInt("reservation_id"));
		reservation.setSiteId(results.getInt("site_id"));
		reservation.setName(results.getString("name"));
		reservation.setFromDate(results.getDate("from_date").toLocalDate());
		reservation.setToDate(results.getDate("to_date").toLocalDate());
		reservation.setCreateDate(results.getDate("create_date").toLocalDate());
		
		return reservation;
	}
	
}
