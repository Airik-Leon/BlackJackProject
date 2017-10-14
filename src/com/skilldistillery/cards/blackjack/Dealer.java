package com.skilldistillery.cards.blackjack;
public class Dealer {
	private String[] PossibleDealerNames = {
			" \"Chance \" Long",
			" \"Devil\" Neal",
			"\"Massacre\" Barrett",
			"Gabriel \"the Bandit\"",
			"Gregory \"Dirty Hunter\" Jones",
			"Jeffrey \"the Demon\"",
			"Jim \"Black Master\" Lawson|",
			"Johnnie \"Bad Gravedigger\" Barnett",
			"Loco Miller",
			"Lucky Sullivan",
			"Mitchell \"Reverend\" Dixon",
			"Raymond \"Bull\" Ramsey",
			"Raymond \"Loco\" Stewart",
			"Surgeon Brown",
			"Walter Barnes \"the Doctor\""};
	private String name; 
	private Hand dealerHand =   new Hand();
	private Deck cardDeck =   new Deck();
	
	public Dealer() {
		int randomNumber = (int) (Math.random() * PossibleDealerNames.length -1); 
		this.name = PossibleDealerNames[randomNumber]; 
	}
	public void houseDraw(int handValue) {
		Card newCard = cardDeck.drawACard();
		if(newCard.getSuit().equals(Ranks.ACE.toString())) {
			if(newCard.getNumber() + handValue > 21) {
				newCard.setNumber(Ranks.ACE.getSecondaryValue());
				dealerHand.addCard(newCard);
			}
		}
		else {
			dealerHand.addCard(newCard);
		}
	}
	public void resetHand() {
		dealerHand.RemoveHand();
	}
	public int getDeckSize() {
		return cardDeck.deckSize();
	}
	public int handValue() {
		return dealerHand.HandValue(); 
	}
	public Card peekBottomCard() {
		return dealerHand.getHand().get(0);
	}
	public String getName() {
		return name;
	}	
	public void createDeck() {
		cardDeck.createDeck();
	}
	public void shuffle() {
		cardDeck.shuffle();
	}
	public Card playerDrawCard() {
		return cardDeck.drawACard();
	}
	public void showDealerHand(){
		System.out.println("===Dealers cards===");
		for(Card  card: dealerHand.getHand()) {
			System.out.println(card);
		}
		System.out.println("Dealer has: "  + dealerHand.HandValue());
		System.out.println();
	}
	public Card TopCard() {
		return dealerHand.getHand().get(dealerHand.getHand().size()-1);
	}
	public Hand getHand() {
		return this.dealerHand;
	}
}
