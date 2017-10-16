package com.skilldistillery.cards.blackjack;

public enum Suits {
	CLUBS('\u2663'),
	HEARTS('\u2665'),
	DIAMONDS('\u2666'),
	SPADES('\u2660');
	private char unicode; 
	Suits(char unicode) {
		this.unicode = unicode;
	}
	public char getUnicode() {
		return unicode;
	}
}
