package com.skilldistillery.cards.blackjack;

import java.util.Scanner;

public class Player {
	//Class Fields
	private double purse; 
	private String name; 
	private Hand playerHand; 
	private Hand SplitHand;
	private boolean doubleDown;
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
	public boolean hit(Scanner userInput, String input,int handValue) {
		if(isDoubleDown()) {
			return true;
		}
		if(handValue >=21 || handValue == 21) {
			return false;
		}
		else if(handValue < 21) {
			System.out.println("Would you like to hit. y/n");
			input =   userInput.next().toLowerCase();
			if(input.equals("y")) {
				return true;
			}
		}
		return false;
	}
	public void drawCardSplitHand(Card newCard, int handValue) {
		if(newCard.getSuit().equals(Ranks.ACE.toString())) {
			if(newCard.getNumber() + handValue > 21) {
				newCard.setNumber(Ranks.ACE.getSecondaryValue());
				playerHand.addCard(newCard);
			}
		}
		SplitHand.addCard(newCard);
	}
	public void drawCard(Card newCard, int handValue) {
		if(newCard.getSuit().equals(Ranks.ACE.toString())) {
			if(newCard.getNumber() + handValue > 21) {
				newCard.setNumber(Ranks.ACE.getSecondaryValue());
				playerHand.addCard(newCard);
			}
		}
		playerHand.addCard(newCard);
	}
	public boolean buyInsurnace(Scanner userInput, String input){
		input =   userInput.next().toLowerCase();
		if(input.equals("y")) {
			return true;
		}
		return false;
	}
	public int placeBet(Scanner userInput, int input) {
		System.out.println("How much would like to put down? ");
		input = userInput.nextInt();
		return input; 
	}
	public void splitPairs() {
		SplitHand = new Hand(); 
		SplitHand.addCard(playerHand.getHand().get(1));
		this.playerHand.getHand().remove(1);
	}
	public Hand getSplitHand() {
		return this.SplitHand; 
	}
	public int  doubleDown(int playerWage, Scanner userInput) {
		String input; 
		int newWager;
		System.out.println("Would you like to double down? y /n");
		input = userInput.next().toLowerCase();
		if(input.equals("y")) {
			newWager = playerWage *2; 
			setDoubleDown(true);
		}
		else {
			newWager = playerWage;
			setDoubleDown(false);
		}
		return newWager;
	}
	public boolean isDoubleDown() {
		return doubleDown;
	}
	public void setDoubleDown(boolean doubleDown) {
		this.doubleDown = doubleDown;
	}
	public boolean quitGame() {
		System.out.println("Are you ready to quit partner?"); 
		return false; 
	}
	//Getters/Setters
	public double getPurse() {
		return purse;
	}
	public  void setPurse(double purse) {
		this.purse = purse;
		System.out.println("Your purse is now: " + getPurse());
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public void showPlayerHand(){
		System.out.println("===Cards in hand===");
		for(Card  card: playerHand.getHand()) {
			System.out.println(card);
		}
		System.out.println("The math friend is: "   + playerHand.HandValue()+"\n");
	}
	public void showSplitHand() {
		System.out.println("===Cards in Split hand===");
		for(Card  card: SplitHand.getHand()) {
			System.out.println(card);
		}
		System.out.println("The math friend is: "   + SplitHand.HandValue()+"\n");
	}
}
