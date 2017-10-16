package com.skilldistillery.cards.blackjack;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Player {
	//Class Fields
	private double purse; 
	private String name; 
	private Hand playerHand; 
	private Hand SplitHand;
	private boolean doubleDown;
	//User input capabilities
	private Scanner userInput; 
	private String inputString; 
	private int inputInt; 
	private Double inputDouble; 
	//End Class variables
	//Constructors
	public Player() {
		this.purse = 0; 
		this.name = ""; 
		this.playerHand = new Hand();
		this.doubleDown= false;
	}
	public Player(String name, int purse, Hand playerHand) {
		this.name = name; 
		this.purse = purse; 
		this.playerHand = playerHand; 
	}
	//Class Behavior
	public boolean hit(int handValue) {
		if(isDoubleDown()) {
			return true;
		}
		if(handValue >=21 || handValue == 21) {
			return false;
		}
		else if(handValue < 21) {
			System.out.println("Would you like to hit. y/n");
			setUserString(userInput);
			inputString.toLowerCase();
			if(inputString.equals("y")) {
				return true;
			}
		}
		return false;
	}
	public boolean dealAgain() {
		System.out.println("Deal again? y/n ");
		setUserString(userInput);
		inputString = inputString.toLowerCase();
		if(inputString.equals("y")) {
			return true; 
		}
		else {
			return false; 
		}
	}
	public void drawCardSplitHand(Card newCard, int handValue) {
		playerHand.bestHand();
		if(newCard.getSuit().equals(Ranks.ACE.toString())) {
			if(newCard.getNumber() + handValue > 21) {
				newCard.setNumber(Ranks.ACE.getSecondaryValue());
				SplitHand.addCard(newCard);
			}
			else {
				SplitHand.addCard(newCard);
			}
		}
		else {
			SplitHand.addCard(newCard);
		}
	}
	public void drawCard(Card newCard, int handValue) {
		playerHand.bestHand();
		if(newCard.getSuit().equals(Ranks.ACE.toString())) {
			if(newCard.getNumber() + handValue > 21) {
				newCard.setNumber(Ranks.ACE.getSecondaryValue());
				playerHand.addCard(newCard);
			}
			else {
				playerHand.addCard(newCard);
			}
		}
		else {
			playerHand.addCard(newCard);			
		}
	}
	public boolean buyInsurnace(){
		setUserString(userInput);
		inputString =   inputString.toLowerCase();
		if(inputString.equals("y")) {
			return true;
		}
		return false;
	}
	public double placeBet( ) {
		System.out.println("How much would like to put down? ");
		 setUserDouble(userInput);
		return getInputDouble();
	}
	public boolean isSplitPairs(double playerWager) {
		System.out.println("You have a pair would  you like to split your cards? y/n" );
		setUserString(userInput);
		inputString = inputString.toLowerCase();
		if(inputString.equals("y")) {
			if((getPurse() - playerWager) < 0) {
				System.out.println("Not enough money to double down with.");
				System.out.println("Current purse is: $" + getPurse());
				setDoubleDown(false);
				return false; 
			}
			else {
				SplitHand = new Hand(); 
				SplitHand.addCard(playerHand.getHand().get(1));
				this.playerHand.getHand().remove(1);
				return true; 
			}
		}
		else {
			return false; 
		}
	}
	public Hand getSplitHand() {
		return this.SplitHand; 
	}
	public double  doubleDown(double playerWage) {
		System.out.println("Would you like to double down? y /n");
		setUserString(userInput);
		 if(inputString.equals("y")) {
			if((getPurse() - playerWage) < 0) {
				System.out.println("Not enough money to double down with.");
				System.out.println("Current purse is: $" + getPurse());
				setDoubleDown(false);
				return playerWage;
			}
			else{
				setDoubleDown(true);
				return playerWage;	
			}
		}
		else {
			setDoubleDown(false);
			return playerWage;
		}
	}
	public boolean isDoubleDown() {
		return doubleDown;
	}
	public void setDoubleDown(boolean doubleDown) {
		this.doubleDown = doubleDown;
	}
	//Getters/Setters
	public double getPurse() {
		return purse;
	}
	public  void setPurse(double purse) {
		this.purse = purse;
		System.out.println("Your purse is now: $" + getPurse());
	}
	public String getName() {
		return name;
	}
	public void setName() {
		setUserString(userInput);
		name = inputString; 
	}
	public void loadNameFromFile(String nameFromFile) {
		this.name = nameFromFile;
	}
	public Hand getPlayerHand() {
		return playerHand;
	}
	public void resetHand() {
		playerHand.RemoveHand();
		if(SplitHand != null) {
			SplitHand.RemoveHand();
		}
	}
	public void setPlayerHand(Hand playerHand) {
		this.playerHand = playerHand;
	}
	public Scanner getUserInput() {
		return userInput;
	}
	public boolean setUserString(Scanner userInput) {
		boolean badInput = false; 
		do {
			try {
				userInput = new Scanner(System.in);
				this.inputString = userInput.nextLine();
			}
			catch(Exception error) {
				System.out.println("User Input must be a character value ");
			}
		}while(badInput);
		return true;
	}
	public boolean setUserDouble(Scanner userInput) {
		boolean badInput= true; 
		do {
			try {
				userInput = new Scanner(System.in);
				this.inputDouble = userInput.nextDouble();
				badInput = false; 
			}
			catch(InputMismatchException error) {
				System.out.println("You must enter a number between " + Double.MAX_VALUE + "-" + Double.MIN_VALUE);
				badInput = true; 
			}
		}while(badInput);
		return true;
	}
	public void setSplitHand(Hand splitHand) {
		SplitHand = splitHand;
	}
	public void closeInputScanner() {
		userInput.close();
	}
	public String getInputString() {
		return inputString;
	}
	public int getInputInt() {
		return inputInt;
	}
	public double getInputDouble() {
		return inputDouble;
	}
	//Print methods for Hand
	public void showPlayerHand(){
		System.out.println("===Cards in hand===");
		for(Card  card: playerHand.getHand()) {
			System.out.println(card);
		}
		System.out.println("=================");
		System.out.println("The math friend is: "   + playerHand.HandValue()+"\n");
	}
	public void showSplitHand() {
		System.out.println("===Cards in Split hand===");
		for(Card  card: SplitHand.getHand()) {
			System.out.println(card);
		}
		System.out.println("=================");
		System.out.println("The math friend is: "   + SplitHand.HandValue()+"\n");
	}
}
