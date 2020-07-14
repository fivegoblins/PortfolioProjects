package com.techelevator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Scanner;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.model.Campground;
import com.techelevator.model.CampgroundDAO;
import com.techelevator.model.Park;
import com.techelevator.model.ParkDAO;
import com.techelevator.model.Reservation;
import com.techelevator.model.ReservationDAO;
import com.techelevator.model.Site;
import com.techelevator.model.SiteDAO;
import com.techelevator.model.jdbc.JDBCCampgroundDAO;
import com.techelevator.model.jdbc.JDBCParkDAO;
import com.techelevator.model.jdbc.JDBCReservationDAO;
import com.techelevator.model.jdbc.JDBCSiteDAO;
import com.techelevator.view.Menu;

public class CampgroundCLI {
	
	private static final String CAMPGROUND_MENU_OPTION_SEARCH_FOR_AVAILABLE_RESERVATION = "Search for Available Reservation";
	private static final String CAMPGROUND_MENU_OPTION_RETURN = "Return to Previous Screen";
	
	private static final String[] CAMPGROUND_MENU_OPTIONS = new String[] {CAMPGROUND_MENU_OPTION_SEARCH_FOR_AVAILABLE_RESERVATION, 
																			CAMPGROUND_MENU_OPTION_RETURN};
	
	private static final String PARK_MENU_OPTION_VIEW_CAMPGROUNDS = "View Campgrounds";
	private static final String PARK_MENU_OPTION_SEARCH_FOR_RESERVATION = "Confirm Existing Reservation";
	private static final String PARK_MENU_OPTION_SEARCH_ALL_RESERVATIONS = "Search All Upcoming Reservations";
	private static final String PARK_MENU_OPTION_SEARCH_AVAILABLE_CAMPSITES = "Search Available Campsites";
	private static final String PARK_MENU_OPTION_RETURN = "Return to Previous Screen";
	private static final String[] PARK_MENU_OPTIONS = new String[] { PARK_MENU_OPTION_VIEW_CAMPGROUNDS, 
																		PARK_MENU_OPTION_SEARCH_FOR_RESERVATION,
																		PARK_MENU_OPTION_SEARCH_ALL_RESERVATIONS,
																		PARK_MENU_OPTION_SEARCH_AVAILABLE_CAMPSITES,
																		PARK_MENU_OPTION_RETURN};


	
	private Menu menu;
	private static CampgroundDAO campgroundDAO;
	private static ParkDAO parkDAO;
	private static SiteDAO siteDAO;
	private static ReservationDAO reservationDAO;
	private static Scanner input;
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		CampgroundCLI application = new CampgroundCLI(dataSource);
		application.run();
	}

	public CampgroundCLI(DataSource datasource) {
		this.menu = new Menu(System.in, System.out);
		
		// create your DAOs here	
		 campgroundDAO = new JDBCCampgroundDAO(datasource);
		 parkDAO = new JDBCParkDAO(datasource);
		 siteDAO = new JDBCSiteDAO(datasource);
		 reservationDAO = new JDBCReservationDAO(datasource);
	}

	public void run() {
		displayGreeting();
		displayAllParks();
		displayHeading("Select a Park for Further Details>>> ");
		
		input = new Scanner(System.in);
		
		while(true) {
			
			String choice = input.nextLine();
			if (choice.equalsIgnoreCase("Q")) {
				System.exit(0);
			} else {
				displayParkInfo(choice);
			}
		}
		
	}
	
	private void displayAllParks() {
		List<Park> parks = parkDAO.getAllParks();
		
		int number = 0;
		
		for(Park p: parks) {
			number++;
			System.out.println(number + ". " + p.getName());
		}
		
		System.out.println("Q. Quit");
	}
	
	private void displayGreeting() {
		System.out.println("Welcome to the National Parks Reservation System\n");
	}
	
	private void displayHeading(String heading) {
		System.out.println("\n" + heading + "\n");
	}
	
	private void displayParkInfo(String parkChoice) {
		List<Park> parks = parkDAO.getAllParks();
		Park selectedPark = parks.get(Integer.valueOf(parkChoice) -1);
		
		StringBuilder sb = new StringBuilder(selectedPark.getDescription());

		int i = 0;
		while (i + 80 < sb.length() && (i = sb.lastIndexOf(" ", i + 80)) != -1) {
		    sb.replace(i, i + 1, "\n");
		}
		
		displayHeading("Park Details:");
	
		System.out.println(selectedPark.getName() + " National Park");
		System.out.println("Location: " + selectedPark.getLocation());
		System.out.println("Established: " + selectedPark.getEstablishDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
		System.out.println("Area: " + selectedPark.getArea() + " sq. km.");
		System.out.println("Annual Visitors: "  + selectedPark.getVisitors());
		System.out.println("\n" + sb.toString());
		
		
		displayParkMenu(selectedPark);
	}
	
	private void displayParkMenu(Park selectedPark) {
		displayHeading("Select a command>>> ");
		String choice = (String)menu.getChoiceFromOptions(PARK_MENU_OPTIONS);
		
		if(choice.equals(PARK_MENU_OPTION_VIEW_CAMPGROUNDS)) {
			handleViewAllCampgrounds(selectedPark);
		} else if (choice.equals(PARK_MENU_OPTION_SEARCH_FOR_RESERVATION)) {
			handleSearchForReservation(selectedPark);
		} else if (choice.equals(PARK_MENU_OPTION_SEARCH_ALL_RESERVATIONS)) {
			handleSearchAllUpcomingReservations(selectedPark);
		} else if (choice.equals(PARK_MENU_OPTION_SEARCH_AVAILABLE_CAMPSITES)) {
			handleSearchAllAvailableCampsiteReservations(selectedPark);
		} else if (choice.equals(PARK_MENU_OPTION_RETURN)) {
			displayAllParks();
		}
	}
	
	private void displayCampgroundMenu(List<Campground> campgrounds) {
		displayHeading("Select a Command>>>");
		
		String choice = (String)menu.getChoiceFromOptions(CAMPGROUND_MENU_OPTIONS);
		
		if(choice.equals(CAMPGROUND_MENU_OPTION_RETURN)) {
			displayAllParks();
		} else if (choice.equals(CAMPGROUND_MENU_OPTION_SEARCH_FOR_AVAILABLE_RESERVATION)) {
			handleFindAvailableSites(campgrounds);
		}
	}
	
	private void handleViewAllCampgrounds(Park selectedPark) {
		
		List<Campground> campgrounds = campgroundDAO.getAllCampgroundsByParkId(selectedPark.getParkId());
		int number = 0;
		
		displayHeading(selectedPark.getName() + " National Park Campgrounds: ");
		System.out.printf("%-8s%-28s%-16s%-16s%-12s\n", "Number", "Name", "Open", "Closed", "Daily Fee");
		

		for (Campground c: campgrounds) {
			String openMonth = Month.of(Integer.parseInt(c.getOpenMonth())).toString();
			String closedMonth = Month.of(Integer.parseInt(c.getClosedMonth())).toString();
			number++;
			System.out.printf("%-8s%-28s%-16s%-16s%-12s\n", number, c.getName(), openMonth, 
					closedMonth, String.format("%.2f", c.getDailyFee()));
		}
		
		displayCampgroundMenu(campgrounds);
	}
	
	
	private void displayAvailableSites(List<Site> sites, BigDecimal cost, LocalDate departureDate, LocalDate arrivalDate) {
		if (sites.size() > 0) {
			displayHeading("Results Matching Your Search Criteria:");
			System.out.printf("%-12s%-12s%-12s%-12s%-12s\n","Site #", "Occupancy", "Acessible", "RV Length", "Utility", "Cost");
			
			for(Site site: sites) {
				System.out.printf("%-12s%-12s%-12s%-12s%-12s\n", site.getSiteNumber(), site.getMaxOccupancy(), site.getMaxRvLength(), 
						site.isAccessible(), site.isHasUtilities(), String.format("%.2f", cost));
			}
			
			handleBookReservation(sites, departureDate, arrivalDate);
			
		} else {
			displayHeading("There are no campsites available for the requested date range.");
		}
		
	}
	
	private void handleBookReservation(List<Site> sites, LocalDate departure, LocalDate arrival) {
		input = new Scanner(System.in);
		
		System.out.print("\nWhich campsite would you like to reserve?>>> ");
		String selection = input.nextLine();
		Site siteSelection = new Site();
		
		//System.out.println(sites.get(1).getSiteId());
		
		for (int i = 0; i < sites.size(); i++) {
			int number = sites.get(i).getSiteNumber();
			if (number == (Integer.valueOf(selection))) {
				siteSelection = sites.get(i);
			}
		}
		
		System.out.print("What name should the reservation be made under?>>> ");
		String name = input.nextLine();
		
		Reservation reservation = new Reservation();
		reservation.setSiteId(siteSelection.getSiteId());
		reservation.setFromDate(arrival);
		reservation.setToDate(departure);
		reservation.setName(name);
		
		reservationDAO.addReservation(reservation);
		List<Reservation> confirmed = reservationDAO.readBackReservation(name, arrival, departure);
		
		for(Reservation res: confirmed) {
			System.out.print("The reservation has been made. The confirmation number is " 
					+ res.getReservationId() + ".");
		}
	}
		
	
	private void handleFindAvailableSites(List<Campground> campgrounds) {
		input = new Scanner(System.in);
		//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
		
		System.out.print("Select a campground>>> ");
		String selection = input.nextLine();
		
		
		Campground campgroundSelection = campgrounds.get(Integer.valueOf(selection) - 1);
			
		System.out.print("What is the arrival date? MM-DD-YYYY>>> ");
		String arrival = input.nextLine();
		LocalDate arrivalDate = LocalDate.parse(arrival, formatter);
		
		System.out.print("What is the departure date? MM-DD-YYYY>>> ");
		String departure = input.nextLine();
		LocalDate departureDate = LocalDate.parse(departure, formatter);
		
		System.out.print("\nWould you like to view advanced search options? Y/N ");
		String response = input.nextLine();
		
		Long daysOfStay = arrivalDate.until(departureDate, ChronoUnit.DAYS);
		BigDecimal cost = campgroundSelection.getDailyFee().multiply(new BigDecimal(daysOfStay));
		
		if (response.equalsIgnoreCase("Y")) {
			handleAdvancedSearch(cost, departureDate, arrivalDate, campgroundSelection);
		} else {
			List<Site> availableSites = siteDAO.findAvailableSites(campgroundSelection.getCampgroundId(), arrivalDate, departureDate);
			displayAvailableSites(availableSites, cost, departureDate, arrivalDate);
		}
	}
	
	private void handleAdvancedSearch(BigDecimal cost, LocalDate departureDate, LocalDate arrivalDate, Campground campgroundSelection) {
		Scanner input = new Scanner(System.in);
		
		System.out.print("How many occupants?>>> ");
		int maxOccupants = Integer.parseInt(input.nextLine());
		
		System.out.print("Do you require wheelchair accessibility? Y/N ");
		String response = input.nextLine();
		boolean needsAccessibility;
		if(response.equalsIgnoreCase("Y")) {
			needsAccessibility = true;
		} else {
			needsAccessibility = false;
		}
		
		int rvLength = 0;
		
		System.out.print("Will you have an RV? Y/N ");
		String answer = input.nextLine();
		if(response.equalsIgnoreCase("Y")) {
			System.out.print("What is the length of your RV?>>> ");
			rvLength = Integer.parseInt(input.nextLine());
		}
		
		System.out.print("Will you need a utility hookup? Y/N ");
		String choice = input.nextLine();
		boolean needsUtility;
		
		if(choice.equalsIgnoreCase("Y")) {
			needsUtility = true;
		} else {
			needsUtility = false;
		}
		
		List<Site> availableSites = siteDAO.advancedSearchAvailableSites(campgroundSelection.getCampgroundId(), 
				maxOccupants, needsAccessibility, rvLength, needsUtility, arrivalDate, departureDate);
		displayAvailableSites(availableSites, cost, departureDate, arrivalDate);
		
	}
	
	private void handleSearchForReservation(Park park) {
		input = new Scanner(System.in);
		
		System.out.print("What is the reservation confirmation number?>>> ");
		int reservationId = Integer.parseInt(input.nextLine());
		
		Reservation reservation = reservationDAO.getReservationById(reservationId);	
		
		if (reservation != null) {
			System.out.print(reservation.getName() + " has reserved a campsite from " + 
				reservation.getFromDate() + " until " + reservation.getToDate() + ". " + 
					"The reservation was made on " + reservation.getCreateDate() + ".");
		} else {
			System.out.print("Unable to find a reservation with that confirmation number.\n");
			displayParkMenu(park);
		}
	}
	
	private void handleSearchAllUpcomingReservations(Park park) {
		List<Reservation> upcomingReservations = reservationDAO.getAllReservationsByParkId(park.getParkId());
		if (upcomingReservations.size() > 0) {
			System.out.printf("%-36s%-16s%-16s%-16s\n", "Name", "Conf. #", "Check-in", "Check-out");
			for (Reservation reservation: upcomingReservations) {
				System.out.printf("%-36s%-16s%-16s%-16s\n", reservation.getName(), reservation.getReservationId(), 
						reservation.getFromDate(), reservation.getToDate());
			}
		} else {
			System.out.println("There are no upcoming reservations this month.");
		}
		
	}
	
	private void handleSearchAllAvailableCampsiteReservations(Park park) {
		input = new Scanner(System.in);
		//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
		
//		System.out.print("Select a campground>>> ");
//		String selection = input.nextLine();
//		Campground campgroundSelection = campgrounds.get(Integer.valueOf(selection) - 1);
			
		System.out.print("What is the arrival date? MM-DD-YYYY>>> ");
		String arrival = input.nextLine();
		LocalDate arrivalDate = LocalDate.parse(arrival, formatter);
		
		System.out.print("What is the departure date? MM-DD-YYYY>>> ");
		String departure = input.nextLine();
		LocalDate departureDate = LocalDate.parse(departure, formatter);
		
//		Long daysOfStay = arrivalDate.until(departureDate, ChronoUnit.DAYS);
		BigDecimal cost = null;
//		BigDecimal cost = campgroundSelection.getDailyFee().multiply(new BigDecimal(daysOfStay));
		
		List<Site> availableSites = siteDAO.findAvailableSitesByParkId(park.getParkId(), arrivalDate, departureDate);
		displayAvailableSites(availableSites, cost, departureDate, arrivalDate);
	}
}
