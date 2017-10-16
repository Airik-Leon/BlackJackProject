package com.skilldistillery.cards.blackjack;
import java.util.LinkedList;
import java.util.List;

public class Hand {
	private List<Card> currentCards; 
	public Hand() {
		currentCards = new LinkedList<Card>(); 
	}
	public void addCard(Card newCard) {
		currentCards.add(newCard);
	}
	public void bestHand() {
		int totalAces = 0; 
		for(int i = 0; i < currentCards.size(); i++) {
			if(currentCards.get(i).getSuit().equals(Ranks.ACE.toString())) {
				totalAces +=1; 
			}
		}
		if(totalAces <= 1) {
			//means good hand
		}
		else {
			int count = 0; 
			for(int i = 0; i < currentCards.size(); i++) {
				if((currentCards.get(i).getNumber() + count > 21  && currentCards.get(i).getSuit().equals(Ranks.ACE.toString()))){
					count+=currentCards.get(i).getNumber();
					currentCards.get(i).setNumber(Ranks.ACE.getSecondaryValue());
				}
			}
		}
	}
	public void RemoveHand() {
		currentCards = new LinkedList<>(); 
	}
	public int HandValue() {
		int currentHand = 0; 
		for(int i  =0; i < currentCards.size(); i++) {
			 currentHand += currentCards.get(i).getNumber(); 
		}
		return currentHand;
	}
	public List<Card> getHand(){
		return currentCards;
	}
}
