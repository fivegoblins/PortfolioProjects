package com.techelevator.model;

import java.time.LocalDate;
import java.util.List;

public interface ReservationDAO {

	//  Returns a list of reservations by parkId
	public List<Reservation> getAllReservationsByParkId(int parkId);
	
	//  Returns one reservation by the reservationId
	public Reservation getReservationById(int reservationId);
	
	//  Adds a reservation to the database and returns reservation_id as confirmation_id
	public Reservation addReservation(Reservation newReservation);
	
	//	Reads back the reservation that was added to the database and confirms the reservation_id
	public List<Reservation> readBackReservation(String name, LocalDate fromDate, LocalDate toDate);
	
}
