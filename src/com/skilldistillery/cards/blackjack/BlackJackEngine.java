package com.skilldistillery.cards.blackjack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class BlackJackEngine implements CardGames {
	final double INSURANCE_RATE = .5;
	final double ODDS = 1.5;
	double HouseBank = 1_000_000;
	private Player user;
	private Dealer gameDealer;
	private double playerWager = 0;
	private double splitWage = 0;
	private double playerInsurance = 0;

	public BlackJackEngine() {
		user = new Player();
		gameDealer = new Dealer();
		gameDealer.createDeck();
		InitialGreetings();
	}

	@Override
	public void startGame() {
		playerWager = 0;
		// Get the bet from the player...Player can't bet more than their purse
		do {
			if (user.getPurse() <= 0) {
				System.out.println("You don't have enough money to place a bet: ");
				System.out.println("So, leave! Only patrons with money can loiter");
				endGame();
			} else {
				playerWager = user.placeBet();
				if (playerWager > user.getPurse()) {
					System.out.println("You can't place a bet greater than your purse. Try another bet. ");
				} else {
					user.setPurse(user.getPurse() - playerWager);
					HouseBank += playerWager;
					break;
				}
			}
		} while (playerWager > user.getPurse());
		// Shuffle deck when cards get low
		if (gameDealer.getDeckSize() <= 10) {
			gameDealer.shuffle();
		}
		// Beginning of game first deal
		// PlayerDraws
		user.drawCard(gameDealer.playerDrawCard(), user.getPlayerHand().HandValue());
		// Dealer Draws
		gameDealer.houseDraw(gameDealer.handValue());
		// Player Draws
		user.drawCard(gameDealer.playerDrawCard(), user.getPlayerHand().HandValue());
		// DealerDraws
		gameDealer.houseDraw(gameDealer.handValue());
		user.showPlayerHand();
		System.out.println("Dealer's top card is: " + gameDealer.TopCard());
		System.out.println();

		// Check to see if the Dealer potentially could have 21 on deal and offer
		// insurance
		if (gameDealer.TopCard().getNumber() == Ranks.ACE.getPrimaryValue()) {
			System.out.println("The house has a " + gameDealer.TopCard().getSuit() + " "
					+ gameDealer.TopCard().getName() + " Would you like to buy insurance? y/n");
			// Determine if user would like Insurance
			if (user.buyInsurnace()) {
				playerInsurance = playerWager * INSURANCE_RATE;
				user.setPurse((user.getPurse() - playerInsurance));
				HouseBank += playerInsurance;
			}
		}
		// Display warning to signal to player not to hit...But they can if they want to
		if (user.getPlayerHand().HandValue() == 21) {
			System.out.println("21 before the house flip sounds like lucky day for you already.");
			user.setPurse(user.getPurse() + (playerWager * ODDS));
			HouseBank -= playerWager;
			// Ask user if they want to play another hand
			if (user.dealAgain()) {
				resetGame();
			} else {// Otherwise save and quit game
				recordAndShow();
				endGame();
			}
		}
		// Check to see if Split
		if (user.getPlayerHand().getHand().get(0).getSuit().equals(user.getPlayerHand().getHand().get(1).getSuit())) {
			if (user.isSplitPairs(playerWager)) {
				// double the bet and deduct
				splitWage = playerWager;
				user.setPurse(user.getPurse() - splitWage);
				splitWage = user.doubleDown(splitWage);
				user.setSplitHand(playerHitSequence(user.getSplitHand()));
			}
		}
		// Ask and set whether player would like to double down
		user.doubleDown(playerWager);
		// Player begins hit process handles doubleDowns and Split hands
		user.setPlayerHand(playerHitSequence(user.getPlayerHand()));
		// Special to prevent player from knowing and counting the dealers bottom card
		// when player bust.
		if (user.getPlayerHand().HandValue() > 21) {
			System.out.println("Greedy hands never helped anyone in a fight friend. Maybe better luck next time.");
			if (user.dealAgain()) {
				resetGame();
			} else {// Otherwise save and quit game
				recordAndShow();
				endGame();
			}
		}
		// Dealer begins hit process
		while (dealerHitConditions(gameDealer.handValue(), user.getPlayerHand().HandValue())) {
			if (gameDealer.getDeckSize() <= 10) {
				gameDealer.shuffle();
			}
		}
		// Determine winner
		winningConditions();
	}

	public boolean dealerHitConditions(int handValue, int playerHandValue) {
		gameDealer.getHand().bestHand();
		if (handValue == 21) {
			return false;
		} else if (handValue > 21) {
			return false;
		} else if (handValue > playerHandValue) {
			return false;
		} else if (handValue <= 16 && (handValue < playerHandValue && (playerHandValue <= 21))) {
			// Hit on 16 below or dealer is less than the Player
			gameDealer.houseDraw(gameDealer.handValue());
			return true;
		} else if (handValue >= 16 && (handValue < playerHandValue && (playerHandValue <= 21))) {
			gameDealer.houseDraw(gameDealer.handValue());
			return true;
		} else if (gameDealer.getHand().getHand().get(0).getSuit().equals(Ranks.ACE.toString())
				|| gameDealer.getHand().getHand().get(1).getSuit().equals(Ranks.ACE.toString())) {
			// Soft 17 rule
			if (handValue == 17) {
				gameDealer.houseDraw(gameDealer.handValue());
				return true;
			}
		}
		return false;
	}

	public void InitialGreetings() {
		System.out.println("Howdy there Partner! The name is: " + gameDealer.getName()
				+ " welcome to the black jack tables. I have been working up a mighty appetite for dealing friend. ");
		System.out.println("So cowboy? You need a name! Now tell me what is your name?  ");
		user.setName();
		if (loadGame()) {
			System.out.println("Ah ha! Back again  " + user.getName()
					+ " Its the  accent  ain't it? Nah, well the tables are always hot. Welcome back.");
		} else {
			System.out.println("Well it is pleasure to  meet you " + user.getName()
					+ " now that we are acquainted. I feel obliged to give you $100 on the house for your first time");
			user.setPurse(100);
			System.out.println(user.getName() + " purse received:  $" + user.getPurse());
		}
	}

	public void winningConditions() {
		gameDealer.showDealerHand();
		if (gameDealer.handValue() == 21 && gameDealer.getHand().getHand().size() <= 2
				&& gameDealer.handValue() != user.getPlayerHand().HandValue()) {
			System.out.println("Hmm... 21, it ain't your day friend. House wins");
			if (playerInsurance > 0) {
				System.out.println("But, your forsight is the key to  making it at this casino ringo. Good eyes");
				user.setPurse(user.getPurse() + (playerInsurance * 2));
				HouseBank -= playerInsurance * 2;
			} else {
				System.out.println(
						"To bad you didn't get the insurance. But, that is okay I have never been a fan of finance and banks too.");
			}
		} else if (gameDealer.handValue() == 21 && gameDealer.handValue() == user.getPlayerHand().HandValue()) {
			System.out.println("Looks like the house works in mysterious ways");
			user.setPurse(user.getPurse() + playerWager);
		} else if (gameDealer.handValue() == user.getPlayerHand().HandValue()) {
			System.out.println("When push comes to shove the house gets its way.");
		} else if (gameDealer.handValue() > 21) {
			System.out.println("Ain't that something? Don't s'pose you have them river fairies helpin ya. ");
			user.setPurse(user.getPurse() + (playerWager * ODDS));
			HouseBank -= playerWager * ODDS;
		} else if (gameDealer.handValue() > user.getPlayerHand().HandValue() && (gameDealer.handValue() <= 21)) {
			System.out.println("The house wins fair and square partner good luck next time. ");
		} else if (user.getPlayerHand().HandValue() > gameDealer.handValue()
				&& (user.getPlayerHand().HandValue() <= 21)) {
			System.out.println("Coming into the lions den and winning like that. Impressive");
			user.setPurse(user.getPurse() + (playerWager * ODDS));
			HouseBank -= playerWager * ODDS;
		} else if (user.getPurse() == 0) {
			System.out.println("You need to leave. No loitering around here without money.");
			endGame();
		}
		// Ask user if they want to play another hand
		if (user.dealAgain()) {
			resetGame();
		} else {// Otherwise save and quit game
			recordAndShow();
			endGame();
		}
	}

	public Hand playerHitSequence(Hand x) {
		while (user.hit(x.HandValue())) {
			if (gameDealer.getDeckSize() < 10) {
				gameDealer.shuffle();
			}
			if (x.hashCode() == user.getPlayerHand().hashCode()) {
				if (user.isDoubleDown()) {
					user.setPurse(user.getPurse() - playerWager);
					playerWager += playerWager;
					user.setDoubleDown(false);
					user.drawCard(gameDealer.playerDrawCard(), user.getPlayerHand().HandValue());
					user.showPlayerHand();
					x = user.getPlayerHand();
					x.bestHand();
					return x;
				} else {
					user.drawCard(gameDealer.playerDrawCard(), user.getPlayerHand().HandValue());
					user.showPlayerHand();
					x = user.getPlayerHand();
					x.bestHand();
				}
			} else {
				if (user.isDoubleDown()) {
					user.setPurse(user.getPurse() - playerWager);
					playerWager += playerWager;
					user.setDoubleDown(false);
					user.drawCardSplitHand(gameDealer.playerDrawCard(), user.getSplitHand().HandValue());
					user.showSplitHand();
					x = user.getSplitHand();
					return x;
				} else {
					user.drawCardSplitHand(gameDealer.playerDrawCard(), user.getSplitHand().HandValue());
					// code to add a second split hand...
					// if(user.getSplitHand().getHand().get(0).getSuit().equals(user.getSplitHand().getHand().get(1).getSuit()))
					// {
					// if(user.isSplitPairs()) {
					// //double the bet and deduct
					// splitWage = playerWager;
					// user.setPurse(user.getPurse() - splitWage);
					// splitWage = user.doubleDown(splitWage);
					// user.setSplitHand(playerHitSequence(user.getSplitHand()));
					// }
					user.showSplitHand();
					x = user.getSplitHand();
				}
			}
		}
		x.bestHand();
		return x;
	}

	public void recordAndShow() {
		System.out.println("It was a pleasure to have you here with us " + user.getName());
		if (saveGame()) {
			System.out.println("Your can come back anytime and your purse will be: " + user.getPurse());
			System.out.println("==Record vs the House==");
			if (user.getPurse() < 100) {
				System.out.println("You have lost: $" + (100 - user.getPurse()));
			} else if (user.getPurse() == 100) {
				System.out.println(
						"Ultimately you get to walk away with out having lost anything 4" + (user.getPurse() - 100));
			} else {
				System.out.println("Goodness Gracious! You are walking out of here with $" + ((user.getPurse() - 100))
						+ " off the house and in your pocket.");
			}
		} else {
			System.out.println("Unable to save your progress. You can come back and get another $100 on the house");
		}
	}

	@Override
	public void resetGame() {
		user.resetHand();
		gameDealer.resetHand();
		startGame();
	}

	@Override
	public void endGame() {
		System.exit(0);
	}

	@Override
	public boolean saveGame() {
		// Clean name and remove emptySpaces
		String cleanName = cleanNameForFile();
		File playerSave = new File("playerSaves/" + cleanName + ".txt");
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(playerSave.getAbsolutePath());
			bw = new BufferedWriter(fw);
			bw.write(user.getName() + "-" + user.getPurse() + "-" + HouseBank);
			bw.close();
			return true;
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		return false;
	}

	public String cleanNameForFile() {
		char[] cleanNameArray = user.getName().toCharArray();
		String cleanName = "";
		for (int i = 0; i < cleanNameArray.length; i++) {
			if (cleanNameArray[i] == ' ') {
				cleanNameArray[i] = '_';
			}
			cleanName += cleanNameArray[i];
		}
		return cleanName;
	}

	@Override
	public boolean loadGame() {
		String[] playerData = null;
		String cleanName = cleanNameForFile();
		try {
			FileReader fileName = new FileReader("playerSaves/" + cleanName + ".txt");
			BufferedReader in = new BufferedReader(fileName);
			String line;
			while ((line = in.readLine()) != null) {
				playerData = line.split("-");
			}
			user.loadNameFromFile(playerData[0]);
			user.setPurse(Double.parseDouble(playerData[1]));
			HouseBank = Double.parseDouble(playerData[2]);
			in.close();
			return true;
		} catch (IOException e) {
			System.out.println("New Player: " + user.getName());
			return false;
		}
	}
}