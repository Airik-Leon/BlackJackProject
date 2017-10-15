package com.skilldistillery.cards.blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
public class ThreeDeckShoe extends Deck {
	private ArrayList<Card> unsortedCards; 
	private Stack<Card> deckOfCards; 
	private Stack<Card> discardedCards; 
	private Card card; 
	public ThreeDeckShoe() {
		unsortedCards = new  ArrayList<>(156);
		deckOfCards =   new Stack<>();
		discardedCards = new Stack<>();
	}
	public Stack<Card> getDeckOfCards() {
		return deckOfCards;
	}
	public void setDeckOfCards(Stack<Card> deckOfCards) {
		this.deckOfCards = deckOfCards;
	}
	public Card drawACard() {
		Card newCard    = deckOfCards.pop();
		discardedCards.push(newCard);
		return newCard;
	}
	@Override
	public void createDeck() {
		for(int suit = 0; suit< 4; suit++) {
			for( int rank = 0; rank<=12; rank++) {
				card = new Card(Suits.values()[suit].toString(), Ranks.values()[rank].getPrimaryValue(),Ranks.values()[rank].toString());
				unsortedCards.add(card);
			}
		}
		for(int suit = 0; suit< 4; suit++) {
			for( int rank = 0; rank<=12; rank++) {
				 card = new Card(Suits.values()[suit].toString(), Ranks.values()[rank].getPrimaryValue(),Ranks.values()[rank].toString());
				unsortedCards.add(card);
			}
		}
		for(int suit = 0; suit< 4; suit++) {
			for( int rank = 0; rank<=12; rank++) {
				 card = new Card(Suits.values()[suit].toString(), Ranks.values()[rank].getPrimaryValue(),Ranks.values()[rank].toString());
				unsortedCards.add(card);
			}
		}
		loadCardsIntoStack();
	}
	protected void loadCardsIntoStack() {
		Collections.shuffle(unsortedCards);
		Collections.shuffle(unsortedCards);
		Collections.shuffle(unsortedCards);
		for(int i = 0; i < unsortedCards.size(); i++) {
			deckOfCards.push(unsortedCards.get(i));
		}
	}
	public void shuffle() {
		//move cards to list for shuffling
		unsortedCards = new ArrayList<>();
		for(int i = 0; i < discardedCards.size(); i++) {
			unsortedCards.add(discardedCards.pop()); 
		}
		//move good cards to discarded deck to preserve order
		for(int i = 0; i <deckOfCards.size();i++) {
			discardedCards.push(deckOfCards.pop());
		}
		//Shuffle and load shuffled cards into deck
		Collections.shuffle(unsortedCards);
		Collections.shuffle(unsortedCards);
		Collections.shuffle(unsortedCards);
		for(int i = 0; i < unsortedCards.size(); i++) {
			deckOfCards.push(unsortedCards.get(i)); 
		}
		//Add old deck to the stack
		for(int i = 0; i < discardedCards.size(); i++) {
			deckOfCards.push(discardedCards.pop());
		}
		System.out.println("Deck has been shuffled partner. ");
	}
	public int deckSize() {
		return  deckOfCards.size();
	}
}
