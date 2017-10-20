package com.skilldistillery.cards.blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
public class ThreeDeckShoe extends Deck {
	public ThreeDeckShoe() {
		unsortedCards = new  ArrayList<>(156);
		deckOfCards =   new Stack<>();
		discardedCards = new Stack<>();
	}
	@Override
	public void createDeck() {
		for(int suit = 0; suit< 4; suit++) {
			for( int rank = 0; rank<=12; rank++) {
				card = new Card(Suits.values()[suit].toString(),
						Ranks.values()[rank].getPrimaryValue(),Ranks.values()[rank].toString(),Suits.values()[suit].getUnicode());
				unsortedCards.add(card);
			}
		}
		for(int suit = 0; suit< 4; suit++) {
			for( int rank = 0; rank<=12; rank++) {
				card = new Card(Suits.values()[suit].toString(),
						Ranks.values()[rank].getPrimaryValue(),Ranks.values()[rank].toString(),Suits.values()[suit].getUnicode());
				unsortedCards.add(card);
			}
		}
		for(int suit = 0; suit< 4; suit++) {
			for( int rank = 0; rank<=12; rank++) {
				card = new Card(Suits.values()[suit].toString(),
						Ranks.values()[rank].getPrimaryValue(),Ranks.values()[rank].toString(),Suits.values()[suit].getUnicode());
				unsortedCards.add(card);
			}
		}

		loadCardsIntoStack();
	}
}
