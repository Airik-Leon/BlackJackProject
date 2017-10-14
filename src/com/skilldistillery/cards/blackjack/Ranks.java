package com.skilldistillery.cards.blackjack;

public enum Ranks {
		TWO(2), 
		THREE(3),
		FOUR(4),
		FIVE(5), 
		SIX(6),
		SEVEN(7),
		EIGHT(8),
		NINE(9),
		TEN(10), 
		JACK(10),
		QUEEN(10),
		KING(10),
		ACE(11, 1);
	private int value; 
	private int primaryValue;
	private int secondaryValue;
	private Ranks(int primaryValue){
		this.primaryValue= primaryValue;
		this.value = primaryValue;
	}
	private  Ranks(int primaryValue, int secondaryValue){
		this.value = primaryValue;
		this.primaryValue = primaryValue;
		this.secondaryValue = secondaryValue;
	}
	public int getPrimaryValue() {
		return this.primaryValue;
	}
	public void setPrimaryValue(int primaryValue) {
		this.primaryValue = primaryValue;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getSecondaryValue() {
		return secondaryValue;
	}
	public void setSecondaryValue(int secondaryValue) {
		this.secondaryValue = secondaryValue;
	}
		}
