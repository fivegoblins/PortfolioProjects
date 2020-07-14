package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import com.techelevator.view.Menu;

public class VendingMachineCLI {

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_EXIT = "Exit";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, MAIN_MENU_EXIT };

	
	
	private static final String PURCHASE_MENU_FEED_MONEY = "Feed Money";
	private static final String PURCHASE_MENU_SELECT_PRODUCT = "Select Product";
	private static final String PURCHASE_MENU_FINISH_TRANSACTION = "Finish Transaction";
	private static final String[] PURCHASE_MENU_OPTIONS = {PURCHASE_MENU_FEED_MONEY, PURCHASE_MENU_SELECT_PRODUCT, PURCHASE_MENU_FINISH_TRANSACTION};
	
	
	private Menu menu;
	private static Money cashFlow;
	private List<Vendable> vendables = new ArrayList<>();


	public VendingMachineCLI(Menu menu, Money cashFlow) {
		
		this.menu = menu;
		this.cashFlow = cashFlow;
		
	}

	public void run() throws Exception {
		loadVendingMachineItems();	
		
		while (true) {
			
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);

			if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
				// display vending machine items
				displayVendingMachineItems();
				
			} else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
				String selection = (String) menu.getChoiceFromOptions(PURCHASE_MENU_OPTIONS);
					if(selection.equals(PURCHASE_MENU_FEED_MONEY)) {
						
						System.out.println("Please put your money into the machine");
						Scanner moneyFeed = new Scanner(System.in);
						String moneyAccepted = moneyFeed.nextLine();
						double money = Double.parseDouble(moneyAccepted);
						
						cashFlow.feedMoney(money);
						
						auditLogger("FEED", "MONEY", money, cashFlow.getMoneyProvided());
						
						System.out.printf("\nCurrent Money Provided: $" + "%.2f", cashFlow.getMoneyProvided());
					} else if(selection.equals(PURCHASE_MENU_SELECT_PRODUCT)) {
						
						displayVendingMachineItems();
						
						System.out.println("Please select an item to purchase: ");
						Scanner chooseItem = new Scanner(System.in);
						String itemChosen = chooseItem.nextLine();
						
						chooseItem(itemChosen);
					} else if (selection.equals(PURCHASE_MENU_FINISH_TRANSACTION)) {
						
						auditLogger("RETURN", "CHANGE", cashFlow.getMoneyRemaining(), 0.00);

						cashFlow.returnChange(cashFlow.getMoneyRemaining());
					}
			} else if (choice.equals(MAIN_MENU_EXIT)) {
				
				System.out.println("Goodbye!");
				System.exit(1);
			}
		}
	}
	
	public void loadVendingMachineItems() throws IOException {

		File fileInput = new File("vendingmachine.csv");
		Scanner fileScanner = new Scanner(fileInput);
		while(fileScanner.hasNextLine()) {
			String[] line = fileScanner.nextLine().split("\\|");  // a1|Potato crisps|10.00|Chip
			String slot = line[0]; //a1
			String name = line[1]; //name
			double price = Double.parseDouble(line[2]);  //10.00
			String type = line[3]; //chip
			if (type.equals("Chip")) {
				vendables.add(new Chips(slot, name, price, type));
			} else if (type.equals("Candy")) {
				vendables.add(new Candy(slot, name, price, type));
			} else if (type.equals("Gum")) {
				vendables.add(new Gum(slot, name, price, type));
			} else {
				vendables.add(new Beverage(slot, name, price, type));
			}
		}
	}
	
	public void displayVendingMachineItems() {
		for (Vendable v: vendables) {
			System.out.println(v.getSlot() +
					" " + v.getItemName() + 
					" $" + v.getPrice() + 
					" | Available: " + v.getNumberAvailable());
		}
	}
	
	
	public void chooseItem(String item) throws Exception {
		
		boolean isFound = false;
		
		for(Vendable slot: vendables) {
			if(item.equals(slot.getSlot())) {
				isFound = true;
				if(slot.getNumberAvailable() == 0) {
					System.out.println("This item is SOLD OUT - please choose another");
				} else {
					slot.decrement();
					
					cashFlow.moneyRemainingAfterPurchase(slot.getPrice());
					
					auditLogger(slot.getItemName(), slot.getSlot(), cashFlow.getMoneyProvided(), cashFlow.getMoneyRemaining());
					
					System.out.println(slot.getItemName() + " $" + slot.getPrice()); 
					System.out.printf("Money Remaining: $" +  "%.2f\n", cashFlow.getMoneyRemaining());
						
					if(slot.getType().equals("Chip")) {
						System.out.println("Crunch Crunch, Yum!");
					} else if (slot.getType().equals("Candy")) {
						System.out.println("Munch Munch, Yum!");
					} else if (slot.getType().equals("Gum")) {
						System.out.println("Chew Chew, Yum!");
					} else {
						System.out.println("Glug Glug, Yum!");
					} break;
				}
			} 
		}
		if (!isFound) {
			System.out.println("Invalid entry - please try again");
		}
	}
	
	public void auditLogger(String message, String message2, double firstPrice, double secondPrice) throws Exception {
		Logger log = new Logger(System.getProperty("user.dir")+"\\log.txt");
		
		log.Write(LocalDate.now() + " " + LocalTime.now() + " " + message + " " + message2 +
				" $" + firstPrice + " $" + secondPrice);
	}

	public static void main(String[] args) throws Exception {

		Menu menu = new Menu(System.in, System.out);
		Money cashFlow = new Money();
		VendingMachineCLI cli = new VendingMachineCLI(menu, cashFlow);
		
		cli.run();
	}
}





















