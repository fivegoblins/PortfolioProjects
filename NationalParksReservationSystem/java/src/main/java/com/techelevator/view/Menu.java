package com.techelevator.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class Menu {
	private PrintWriter out;
	private Scanner input;
	
	public Menu(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output);
		this.input = new Scanner(input);
	}
	
	public Object getChoiceFromOptions(Object[] options) {
		Object choice = null;
		
		while(choice == null) {
			displayMenuOptions(options);
			choice = getChoiceFromInput(options);
		}
		
		return choice;
	}
	
	private Object getChoiceFromInput(Object[] options) {
		Object choice = null;
		String userInput = input.nextLine();
		
		try {
			int selected = Integer.valueOf(userInput);
			
			if (selected <= options.length) {
				choice = options[selected - 1];
			}
		} catch(NumberFormatException exception) {
		}
		
		if (choice == null) {
			out.println(userInput + "is not a valid selection.");
		}
		
		return choice;
	}
	
	private void displayMenuOptions(Object[] options) {
		for(int i = 0; i < options.length; i++) {
			int number = i + 1;
			out.println(number + ". " + options[i]);
		}
		
		
		out.flush();
	}

}
