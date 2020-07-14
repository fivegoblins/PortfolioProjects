package com.techelevator;

public class Money {
	private double moneyProvided = 0;
	private double moneyRemaining = 0;
	
	
	public double getMoneyProvided() {
		return this.moneyProvided;
	}
	
	public double getMoneyRemaining() {
		return this.moneyRemaining;
	}
	

	public double moneyRemainingAfterPurchase(double price) {
		return this.moneyRemaining = (moneyProvided - price);
	}
	
	public double feedMoney(double amount) {
		return this.moneyProvided = this.getMoneyProvided() + amount;
	}
	
	public double returnChange(double amount) {
		
			double temp = (amount * 100);
		    double coins = temp;

		    int quarters = (int)(temp/25);
		    coins %= 25;
		    int dimes = (int)(coins/10);
		    coins %= 10;
		    int nickels = (int)(coins/5);
		    coins %= 5;
		    
		    this.moneyProvided = 0;
		    
		    System.out.println("Your change is: \nQuarters = " + quarters + 
		    		"\nDimes = " + dimes + 
		    		"\nNickels = " + nickels);
		    
		    return quarters + dimes + nickels;
		   
	}
	
	
}
