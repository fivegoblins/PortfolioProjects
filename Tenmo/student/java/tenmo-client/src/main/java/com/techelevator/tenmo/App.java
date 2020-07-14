package com.techelevator.tenmo;

import java.math.BigDecimal;
import java.util.Scanner;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.OutgoingTransfer;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.TenmoService;
import com.techelevator.tenmo.services.TenmoServiceException;
import com.techelevator.view.ConsoleService;

public class App {

private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	
    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private TenmoService tenmoService = new TenmoService(API_BASE_URL);

    public static void main(String[] args) throws TenmoServiceException {
    	App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL));
    	app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService) {
		this.console = console;
		this.authenticationService = authenticationService;
	}

	public void run() throws TenmoServiceException {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");
		
		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() throws TenmoServiceException {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				
				if (viewTransferHistory().length >= 1) {
					displayTransferByTransferId();
				}
				
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
					sendBucks();
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
		try {
			BigDecimal balance = tenmoService.getAccountBalance(currentUser);
			System.out.println("\nYour current balance is: $" + balance);
		} catch (TenmoServiceException e) {
			System.out.println(e.getMessage());
		}
		
		
	}

	private Transfer[] viewTransferHistory() {
		Transfer[] transfers = {};
		
		try {
			transfers = tenmoService.getTransferHistory(currentUser);
			sortTransfers(transfers);
			
			displayTransfers(transfers);
			
		} catch (TenmoServiceException e) {
			System.out.println(e.getMessage());
		}	
		
		return transfers;
		
	}
	
	private void printBanner() {
		System.out.println("------------------------------------------------------------------");
		System.out.printf("%-12s%-12s%-12s%-12s%-12s%-12s\n","ID #", "TYPE", "STATUS", "FROM", "TO", "AMOUNT");
		System.out.println("------------------------------------------------------------------");
	}
	
	private void sortTransfers(Transfer[] transfers) {
		int n = transfers.length;
		Transfer temp;
		
		for (int i = 0; i < n; i++) {
			for (int j = 1; j < (n - i); j++) {
				if (transfers[j - 1].getId() > transfers[j].getId()) {
					temp = transfers[j - 1];
					transfers[j - 1] = transfers[j];
					transfers[j] = temp;
				}
			}
		}
	}
	
	
	private void printTransfer(Transfer transfer) {
		System.out.printf("%-12s%-12s%-12s%-12s%-12s%-12s\n", transfer.getId(), transfer.getTransferType(), 
				transfer.getTransferStatus(), transfer.getAccountFrom(), transfer.getAccountTo(), 
				transfer.getAmount());
	}
	
	private void displayTransfers(Transfer[] transfers) {
		sortTransfers(transfers);
		if (transfers.length > 0) {
			printBanner();
			
			for (Transfer transfer: transfers) {
		
					printTransfer(transfer);
			}
			
			} else {
				
				System.out.println("You do not have any pending requests.");
			}
	}

	
	private void displayTransferByTransferId() {
		try {		
			System.out.println("\nPlease enter transfer ID to view details: ");
			Scanner userInput = new Scanner(System.in);
			long transferId = Long.parseLong(userInput.nextLine());
			
			Transfer transfer = tenmoService.getTransferByTransferId(currentUser, transferId);
			
			if (transfer == null) {
				System.out.printf("Could not find record of this transfer. Please choose one from the list: ");
				transferId = Long.parseLong(userInput.nextLine());
				transfer = tenmoService.getTransferByTransferId(currentUser, transferId);
			}
			
			printBanner();
		    printTransfer(transfer);
			
		} catch (TenmoServiceException e) {
			System.out.println(e.getMessage());
		}
	}
	

	private void viewPendingRequests() {
		Transfer[] transfers = {};
		
		try {
			transfers = tenmoService.getPendingRequests(currentUser);
			sortTransfers(transfers);
			
			displayTransfers(transfers);
			
			if (transfers.length > 0) acceptOrRejectRequest(transfers);
			
			} catch (TenmoServiceException e) {
				System.out.println(e.getMessage());
			}	
	}
	
	private void acceptOrRejectRequest(Transfer[] transfers) throws TenmoServiceException {
		Scanner in = new Scanner(System.in);
		System.out.print("\nEnter the ID of a request to accept/reject it: ");
		long id = Long.parseLong(in.nextLine());
		
		Transfer selectedTransfer = null;
		
		for (Transfer transfer: transfers) {
			if (transfer.getId() == id) {
				selectedTransfer = transfer;
			}
		}
		
		System.out.print("\nPress A to accept request, or R to reject: ");
		String action = in.nextLine();
		
		if (!action.equalsIgnoreCase("A") && !action.equalsIgnoreCase("R")) {
			System.out.print("\nInvalid input. Press A to accept or R to reject: ");
			action = in.nextLine();
		} else if (action.equalsIgnoreCase("A")) {
			accept(selectedTransfer, id);
		} else if (action.equalsIgnoreCase("R")) {
			reject(id);
		}
	}

	private void reject(long id) {
		OutgoingTransfer transfer = tenmoService.updateStatus(currentUser, id, 3);
		if (transfer != null) {
			System.out.println("\nRequest rejected!");
		} else {
			System.out.println("\nSomething went wrong");
		}
	}
	
	private void accept(Transfer selectedTransfer, long id) throws TenmoServiceException {
		OutgoingTransfer transfer = tenmoService.updateStatus(currentUser, id, 2);
		if (transfer != null) {
			System.out.println("\nRequest accepted!");
		} else {
			System.out.println("\nSomething went wrong.");
		}
		User[] users = tenmoService.listAllUsers(currentUser);
		long userId = 0;
		for (User user: users) {
			if (selectedTransfer.getAccountFrom() == user.getUsername() ) {
				userId = user.getId();
			}
		}
		tenmoService.update(currentUser, userId, selectedTransfer.getAmount());
		tenmoService.updateRequestBalance(userId, selectedTransfer.getAmount());
	}
	
	private void sendBucks() throws TenmoServiceException {
		// TODO Auto-generated method stub
		try {
			User[] users = displayUserList();
			
			Scanner in = new Scanner(System.in);
			System.out.print("\nEnter id of the user you are sending to: ");
			long input = 0;
			try {
				input = Long.parseLong(in.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Please enter a user ID number.");
			}
			
			boolean userFound = false;
			
			for (int i = 0; i < users.length; i++) {
				if (input == users[i].getId()) {
					userFound = true;
				}
			}
			
			if (userFound == false)  {
				System.out.print("\nInvalid user selected. Please enter a user ID from the list: ");
				input = Long.parseLong(in.nextLine());
			}
			
			System.out.print("\nEnter amount : ");
			BigDecimal response;
			try {
				response = new BigDecimal(in.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Please enter a number.");
				response = new BigDecimal(in.nextLine());
			}
			
			BigDecimal balance = tenmoService.getAccountBalance(currentUser);
			if (balance.compareTo(response) == -1) {
				System.out.print("\nYou do not have enough TE bucks to send this request. Please enter a smaller amount: ");
				response = new BigDecimal(in.nextLine());
			}
			
			makeTransfer(currentUser, input, response);
			
		} catch (TenmoServiceException e) {
			System.out.println(e.getMessage());
		}	
	}
	
		
	private OutgoingTransfer makeTransfer(AuthenticatedUser currentUser, long userId, BigDecimal amount) throws TenmoServiceException {
		OutgoingTransfer transfer = tenmoService.addTransfer(currentUser, userId, amount);
		
		if (transfer != null) {
			System.out.println("\nSent successfully!");
		} else {
			System.out.println("\nSomething went wrong");
		}
		
		tenmoService.update(currentUser, userId, amount);
		tenmoService.updateReceiver(currentUser, userId, amount);
		
		return transfer;
	}
	
	private OutgoingTransfer makeRequest(AuthenticatedUser currentUser, long userId, BigDecimal amount) throws TenmoServiceException {
		OutgoingTransfer transfer = tenmoService.addRequest(currentUser, userId, amount);
		
		if (transfer != null) {
			System.out.println("\nRequest sent successfully!");
		} else {
			System.out.println("\nSomething went wrong");
		}
		
		return transfer;
	}
	
	private User[] displayUserList() {
		User[] users = {};
		try {
			users = tenmoService.listAllUsers(currentUser);
			
			System.out.println("-------------------------------");
			System.out.printf("%-12s%-12s\n","ID #", "USERNAME");
			System.out.println("-------------------------------");
			
			for (User user : users) {
				if (user.getId() != (long)currentUser.getUser().getId()) {
				System.out.printf("%-12s%-12s\n", user.getId(), user.getUsername());
				}
			}
		} catch (TenmoServiceException e) {
			System.out.println(e.getMessage());
		}
		return users;
		
	}

	private void requestBucks() throws TenmoServiceException {
		User[] users = displayUserList();
		
		Scanner in = new Scanner(System.in);
		System.out.print("\nEnter id of the user you are requesting from: ");
		long input = Long.parseLong(in.nextLine());
		
		boolean userFound = false;
		
		for (int i = 0; i < users.length; i++) {
			if (input == users[i].getId()) {
				
				userFound = true;
			}
		}
		
		if (userFound == false)  {
			
			System.out.print("\nInvalid user selected. Please enter a user from the list: ");
			input = Long.parseLong(in.nextLine());
			
		}
		
		System.out.print("\nEnter amount : ");
		BigDecimal response;
		try {
			response = new BigDecimal(in.nextLine());
		} catch (NumberFormatException e) {
			System.out.print("Please enter a number: ");
			response = new BigDecimal(in.nextLine());
		}
		
		makeRequest(currentUser, input, response);
		
	}
	
	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
		
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}
