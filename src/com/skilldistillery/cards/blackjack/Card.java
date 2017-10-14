package com.skilldistillery.cards.blackjack;

public class Card {
	private String name; 
	private String suit; 
	private int number; 
	public Card() {
		
	}
	public Card(String name, int number, String suit) {
		this.name = name; 
		this.number = number; 
		this.suit = suit; 
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
		builder.append("Card [name=");
		builder.append(name);
		builder.append(", suit=");
		builder.append(suit);
		builder.append(", number=");
		builder.append(number);
		builder.append("]");
		return builder.toString();
	}
	
}
