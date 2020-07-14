package com.techelevator;

import static org.junit.Assert.*;

import org.junit.Test;

public class MoneyTest {
	Money money = new Money();

	@Test
	public void moneyProvidedShouldInitiallyBe0() {
		double expected = 0;
		assertEquals(expected, money.getMoneyProvided(), 0.001);
	}
	
	@Test
	public void moneyRemainingShouldInitiallyBe0() {
		double expected = 0;
		assertEquals(expected, money.getMoneyRemaining(), 0.001);

	}
	
	@Test
	public void testMoneyRemainingAfterPurchase() {
		double price = 3.75;
		double price2 = 5.00;
		
		assertEquals(-5.00, money.moneyRemainingAfterPurchase(price2), 0.001);
		assertEquals(-3.75, money.moneyRemainingAfterPurchase(price), 0.001);
	}
	
	@Test
	public void feedMoneyAddsMoneyToMoneyProvided() {
		double amount = 10.00;
		
		assertEquals(10.00, money.feedMoney(amount), 0.001);
		assertEquals(12.00, money.feedMoney(2), 0.001);
		assertEquals(20.00, money.feedMoney(8), 0.001);
		assertEquals(20.25, money.feedMoney(0.25), 0.01);
	}
	
	@Test
	public void returnChangeShouldReturnTotalCoins() {
		double amount = 5;
		double amount2 = 3.65;
		
		assertEquals(20, money.returnChange(amount), 0.001);
		assertEquals(16, money.returnChange(amount2), 0.001);
	}
	
	
	
	

}
