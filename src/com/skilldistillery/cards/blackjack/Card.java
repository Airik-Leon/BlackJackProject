package com.skilldistillery.cards.blackjack;

public class Card {
	private String name; 
	private String suit; 
	private int number; 
	private char unicode; 
	public Card() {
		
	}
	public Card(String name, int number, String suit, char unicode) {
		this.name = name; 
		this.number = number; 
		this.suit = suit; 
		this.unicode =unicode; 
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSuit() {
		return suit;
	}
	public void setSuit(String suit) {
		this.suit = suit;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(suit);
		builder.append(" of " + name);
		builder.append(" " + number + " " +  unicode);
		return builder.toString();
	}
	
}
