package com.skilldistillery.cards.blackjackTest;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.skilldistillery.cards.blackjack.Card;
import com.skilldistillery.cards.blackjack.Dealer;
import com.skilldistillery.cards.blackjack.Deck;
import com.skilldistillery.cards.blackjack.Player;
public class BlackJackTests {
	Dealer xDealer; 
	Player xPlayer;
	Card xCard; 
	@Before
	public void setUp() {
		 xPlayer = new Player();
		 xDealer = new Dealer();
		 xCard = new Card();
		 xDealer.createDeck();	
	}

	@After
	public void tearDown() throws Exception {
		xPlayer = null; 
		xDealer = null; 
		xCard = null; 
	}
	@Test
	public void test_AddedCard_IS_CARD_IN_PLAYER_HAND() {
		xCard = xDealer.getSingleDeck().deckTopCard();
		xPlayer.getPlayerHand().addCard(xDealer.playerDrawCard());

		assertEquals(xPlayer.getPlayerHand().getHand().get(0).hashCode(), xCard.hashCode());
	}
	@Test
	public void test_Deck_Has_Same_Cards_After_Shuffle() {
		Deck xOldDeck = xDealer.getSingleDeck();
		System.out.println(xOldDeck.deckSize());
		//Draw 30 cards then shuffle; 
		for(int i =0; i < 30; i++) {
			xOldDeck.drawACard();
		}
		//shuffle method moves cards 
		xDealer.shuffle();
		System.out.println(xOldDeck.deckSize());
		assertEquals(xOldDeck.hashCode(), xDealer.getSingleDeck().hashCode());
	}

}
