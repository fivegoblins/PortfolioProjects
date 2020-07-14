package com.techelevator;

import java.math.BigDecimal;
import java.time.LocalDate;
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
import com.techelevator.model.Site;
import com.techelevator.model.SiteDAO;
import com.techelevator.model.jdbc.JDBCCampgroundDAO;
import com.techelevator.model.jdbc.JDBCParkDAO;
import com.techelevator.model.jdbc.JDBCSiteDAO;
import com.techelevator.view.Menu;

public class TestCampgroundCLI {
	
	private static final String CAMPGROUND_MENU_OPTION_SEARCH_FOR_AVAILABLE_RESERVATION = "Search for Available Reservation";
	private static final String CAMPGROUND_MENU_OPTION_RETURN = "Return to Previous Screen";
	
	private static final String[] CAMPGROUND_MENU_OPTIONS = new String[] {CAMPGROUND_MENU_OPTION_SEARCH_FOR_AVAILABLE_RESERVATION, 
																			CAMPGROUND_MENU_OPTION_RETURN};
	
	private static final String PARK_MENU_OPTION_VIEW_CAMPGROUNDS = "View Campgrounds";
	private static final String PARK_MENU_OPTION_SEARCH_FOR_RESERVATION = "Search for Reservation";
	private static final String PARK_MENU_OPTION_RETURN = "Return to Previous Screen";
	private static final String[] PARK_MENU_OPTIONS = new String[] { PARK_MENU_OPTION_VIEW_CAMPGROUNDS, 
																		PARK_MENU_OPTION_SEARCH_FOR_RESERVATION, 
																		PARK_MENU_OPTION_RETURN};


	
	private Menu menu;
	private static CampgroundDAO campgroundDAO;
	private static ParkDAO parkDAO;
	private static SiteDAO siteDAO;
	private static Scanner input;

	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		TestCampgroundCLI application = new TestCampgroundCLI(dataSource);
		application.run();
	}

	public TestCampgroundCLI(DataSource datasource) {
		this.menu = new Menu(System.in, System.out);
		
		// create your DAOs here	
		 campgroundDAO = new JDBCCampgroundDAO(datasource);
		 parkDAO = new JDBCParkDAO(datasource);
		 siteDAO = new JDBCSiteDAO(datasource);
	}

	public void run() {
		displayGreeting();
		displayAllParks();
		displayHeading("Select a Park for Further Details>>>");
		
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
	
	private void displayParkInfo(String parkChoice) {
		List<Park> parks = parkDAO.getAllParks();
		Park selectedPark = parks.get(Integer.valueOf(parkChoice) -1);
		
		displayHeading("Park Details:");
	
		System.out.println(selectedPark.getName() + " National Park");
		System.out.println("Location: " + selectedPark.getLocation());
		System.out.println("Established: " + selectedPark.getEstablishDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
		System.out.println("Area: " + selectedPark.getArea() + " sq. km.");
		System.out.println("Annual Visitors: "  + selectedPark.getVisitors());
		System.out.println("\n\n" + selectedPark.getDescription());
		
		
		displayParkMenu(selectedPark);
	}
	
	private void displayGreeting() {
		System.out.println("Welcome to the National Parks Reservation System\n");
	}
	
	private void displayHeading(String heading) {
		System.out.println("\n" + heading + "\n");
	}
	
	private void displayParkMenu(Park selectedPark) {
		displayHeading("Select a command:");
		String choice = (String)menu.getChoiceFromOptions(PARK_MENU_OPTIONS);
		
		if(choice.equals(PARK_MENU_OPTION_VIEW_CAMPGROUNDS)) {
			handleViewAllCampgrounds(selectedPark);
		} else if (choice.equals(PARK_MENU_OPTION_SEARCH_FOR_RESERVATION)) {
			handleSearchForReservation();
		} else if (choice.equals(PARK_MENU_OPTION_RETURN)) {
			 clearConsole();
			 run();
		}
	}
	
	public final static void clearConsole()	{
	    try {Runtime.getRuntime().exec("clear");
	    }
	    catch (final Exception e)
	    {
	        //  Handle any exceptions.
	    }
	}
	
	private void handleViewAllCampgrounds(Park selectedPark) {
		
		List<Campground> campgrounds = campgroundDAO.getAllCampgroundsByParkId(selectedPark.getParkId());
		int number = 0;
		
		displayHeading(selectedPark.getName() + " National Park Campgrounds: ");
		System.out.println("\tName\t\tOpen\t\tClose\t\tDaily Fee\t\t");

		
		for (Campground c: campgrounds) {
			number++;
			System.out.println("#" + number + ". " + c.getName() + "\t\t" + c.getOpenMonth() + 
					"\t\t" + c.getClosedMonth() + "\t\t$" + String.format("%.2f", c.getDailyFee()) + "\n");
		}
		
		displayCampgroundMenu(campgrounds);
	}
	
	private void displayCampgroundMenu(List<Campground> campgrounds) {
		displayHeading("Select a Command:");
		
		String choice = (String)menu.getChoiceFromOptions(CAMPGROUND_MENU_OPTIONS);
		
		if(choice.equals(CAMPGROUND_MENU_OPTION_RETURN)) {
			displayAllParks();
		} else if (choice.equals(CAMPGROUND_MENU_OPTION_SEARCH_FOR_AVAILABLE_RESERVATION)) {
			handleFindAvailableSites(campgrounds);
		}
	}
	
	private void displayAvailableSites(List<Site> sites, BigDecimal cost) {
		displayHeading("Results Matching Your Search Criteria:");
		displayHeading("Site #\t\tOccupancy\tAcessible\tRV Length\tUtility\t\tCost");
		
		for(Site site: sites) {
			System.out.println(site.getSiteNumber() + "\t\t" + site.getMaxOccupancy() + "\t\t" + site.getMaxRvLength() + 
					"\t\t" + site.isAccessible() + "\t\t" + site.isHasUtilities() + "\t\t$" + String.format("%.2f", cost) + "\n");
		}
	}
		
	
	private void handleFindAvailableSites(List<Campground> campgrounds) {
		input = new Scanner(System.in);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
		
		System.out.println("Which campground would you  like to reserve? (0 to cancel): ");
		String selection = input.nextLine();
		Campground campgroundSelection = campgrounds.get(Integer.valueOf(selection) - 1);
		
		System.out.println("What is the arrival date? MM-DD-YYYY");
		String arrival = input.nextLine();
		LocalDate arrivalDate = LocalDate.parse(arrival, formatter);
		
		System.out.println("What is the departure date? MM-DD-YYYY");
		String departure = input.nextLine();
		LocalDate departureDate = LocalDate.parse(departure, formatter);
		
		Long daysOfStay = arrivalDate.until(departureDate, ChronoUnit.DAYS);
		BigDecimal cost = campgroundSelection.getDailyFee().multiply(new BigDecimal(daysOfStay));
		
		List<Site> availableSites = siteDAO.findAvailableSites(campgroundSelection.getCampgroundId(), arrivalDate, departureDate);
		displayAvailableSites(availableSites, cost);
		
	}
	
	private void handleSearchForReservation() {
		
	}
	
	
}
;