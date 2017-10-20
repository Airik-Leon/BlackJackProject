package com.skilldistillery.cards.blackjack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Deck {
	protected ArrayList<Card> unsortedCards; 
	protected Stack<Card> deckOfCards; 
	protected Stack<Card> discardedCards; 
	protected Card card; 
	
	public Deck() {
		unsortedCards = new  ArrayList<>(50);
		deckOfCards =   new Stack<>();
		discardedCards = new Stack<>();
	}
	public Deck(ArrayList<Card> incomingDeck) {
		this.unsortedCards = incomingDeck;
		this.deckOfCards = new Stack<Card>(); 
		this.discardedCards = new Stack<Card>(); 
	}
	public Stack<Card> getDeckOfCards() {
		return deckOfCards;
	}
	public void setDeckOfCards(Stack<Card> deckOfCards) {
		this.deckOfCards = deckOfCards;
	}
	public Card drawACard() {
		return card = discardedCards.push(deckOfCards.pop());
	}
	public void createDeck() {
		for(int suit = 0; suit< 4; suit++) {
			for( int rank = 0; rank<=12; rank++) {
				Card card = new Card(Suits.values()[suit].toString(), 
						Ranks.values()[rank].getPrimaryValue(),Ranks.values()[rank].toString(), Suits.values()[suit].getUnicode());
				unsortedCards.add(card);
			}
		}
		loadCardsIntoStack();
	}
	public Card deckTopCard() {
		return deckOfCards.peek();
	}
	protected void loadCardsIntoStack() {
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
		return deckOfCards.size();
	}
}
