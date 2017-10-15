package com.skilldistillery.cards.blackjack;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
public class BlackJackEngine implements CardGames{
	final double INSURANCE_RATE  =.5;
	final double ODDS = 1.5;
	int HouseBank = 1_000_000;
	private Player user; 
	private  Dealer gameDealer;
	private Scanner userInput; 
	private int playerWager = 0; 
	private int splitWage =0; 
	private double playerInsurance= 0; 
	String input; 
	
	public BlackJackEngine() {
		 user = new Player();
		 gameDealer = new Dealer();  
		 userInput = new Scanner(System.in);
		 InitialGreetings();
	}
	@Override
	public void startGame() {

		 input = null; 
		playerWager = 0; 
		//Get the bet from the player...Player can't bet more than their purse
		do {
			playerWager  = user.placeBet(userInput, playerWager);
			if(playerWager > user.getPurse()) {
				System.out.println("You can't place a bet greater than your purse. Try another bet. ");
			}
			else {
				user.setPurse(user.getPurse() - playerWager);
				HouseBank += playerWager;
				break;
			}
		}while(playerWager > user.getPurse());
		
		//Shuffle deck when cards get low
		if(gameDealer.getDeckSize() <= 10) {
			gameDealer.shuffle();
		}
		//Beginning of game first deal
		//PlayerDraws
		user.drawCard( gameDealer.playerDrawCard(), user.getPlayerHand().HandValue()  );
		//Dealer Draws
		gameDealer.houseDraw(gameDealer.handValue());
		//Player Draws
		user.drawCard(gameDealer.playerDrawCard(), user.getPlayerHand().HandValue());
		//DealerDraws
		gameDealer.houseDraw(gameDealer.handValue());
		user.showPlayerHand();
		System.out.println("Dealer's top card is: " + gameDealer.TopCard());
		System.out.println();
		
		//Check to see if Split
		if(user.getPlayerHand().getHand().get(0).getSuit().equals( user.getPlayerHand().getHand().get(1).getSuit())) {
			//Create new split hand
			user.splitPairs();
			// if Player has a split hand activate the interface
			if(user.getSplitHand() != null) {
				splitInterface(); 
			}
		}
		//Check to see if the Dealer potentially could have 21 on deal and offer insurance
		if(gameDealer.TopCard().getNumber()== Ranks.ACE.getPrimaryValue()) {
			System.out.println("The house has a " + gameDealer.TopCard().getSuit() + " " + gameDealer.TopCard().getName()  +" Would you like to buy insurance? y/n");
			if(user.buyInsurnace(userInput, input)) {
					playerInsurance= playerWager * INSURANCE_RATE; 
					user.setPurse((user.getPurse() - playerInsurance));
					HouseBank +=playerInsurance;
			}
		}
		//Display warning  to signal to player not to hit...But they can if they want to
		if(user.getPlayerHand().HandValue() == 21) {
			System.out.println("21 before the house flip sounds like lucky day for you already.");
			user.setPurse(user.getPurse() + (playerWager *  ODDS));
			HouseBank -= playerWager;
		}
		//Allow player to doubleDown
		playerWager = user.doubleDown(playerWager, userInput);
		//Player begins hit process
		while(user.hit(userInput, input, user.getPlayerHand().HandValue())) {
				if(user.isDoubleDown()) {
					user.setDoubleDown(false);
					user.drawCard(gameDealer.playerDrawCard(), user.getPlayerHand().HandValue());
					user.showPlayerHand();
					break;
				}
				else {
					user.drawCard(gameDealer.playerDrawCard(), user.getPlayerHand().HandValue());
					user.showPlayerHand();
				}
			}
		//Special to prevent player from knowing and counting the dealers bottom card when player bust.
			if(user.getPlayerHand().HandValue() > 21) {
				System.out.println("Greedy hands never helped anyone in a fight friend. Maybe better luck next time.");
				user.setPurse(user.getPurse() - playerWager);
				HouseBank += playerWager;
				resetGame();
			}
			//Dealer begins hit process
		while(dealerHit(gameDealer.handValue(), user.getPlayerHand().HandValue())) {
			if(gameDealer.getDeckSize() <=10) {
				gameDealer.shuffle();
			}
		}
		//Determine winner
		WinningConditions();
		if(user.getPurse() == 0) {
			System.out.println("You need to leave. No loitering around here without money.");
			endGame();
		}
		System.out.println("Deal again? y/n ");
		input = userInput.next().toLowerCase(); 
		if(input.equals("y")) {
			System.out.println(gameDealer.getDeckSize());
			resetGame(); 	
		}
		else {
			System.out.println(gameDealer.getDeckSize());
			recordAndShow();
			endGame();
		}
	}
	public boolean dealerHit(int handValue, int playerHandValue) {
		if(handValue == 21) {
			return false;
		}
		else if(handValue > 21){
			return false;
		}
		else if(handValue > playerHandValue) {
			return false; 
		}
		else if(handValue <=16 && (handValue < playerHandValue &&(playerHandValue <=21))) {
			//Hit on 16 below or dealer is less than the Player
			gameDealer.houseDraw(gameDealer.handValue());
			return true;
		}
		else if(handValue >=16 && (handValue < playerHandValue && (playerHandValue <= 21))) {
			gameDealer.houseDraw(gameDealer.handValue());
			return true;
		}
		else if(gameDealer.getHand().getHand().get(0).getSuit().equals(Ranks.ACE.toString()) || gameDealer.getHand().getHand().get(1).getSuit().equals( Ranks.ACE.toString()) ) {
			//Soft 17 rule
			if(handValue == 17) {
				gameDealer.houseDraw(gameDealer.handValue());
				return true;
			}
		}
		return false;
	}
	public void InitialGreetings() {
		System.out.println(
				"Howdy there Partner! The name is: " + gameDealer.getName() + " welcome to the black jack tables. I have been working up a mighty appetite for dealing friend. ");
		System.out.println("So cowboy? You need a name! Now tell me what is your name?  " );
		user.setName(userInput.nextLine());
		if(loadGame()) {
			System.out.println("Ah ha! Back again  " +  user.getName() + " Its the  accent  ain't it? Nah, well the tables are always hot. Welcome back.");
		}
		else {
			System.out.println("Well it is pleasure to  meet you " +  user.getName() + " now that we are acquainted. I feel obliged to give you 100 on the house for your first time");
			user.setPurse(100);
			System.out.println("Players purse received "  +  user.getPurse());
		}
		gameDealer.createDeck();
	}
	public void WinningConditions(){
		gameDealer.showDealerHand();
		if(gameDealer.handValue() == 21 && gameDealer.getHand().getHand().size() <=2 && gameDealer.handValue() != user.getPlayerHand().HandValue()) {
			System.out.println("Hmm... 21, it ain't your day friend. House wins");
			if(playerInsurance > 0) {
				System.out.println("But, your forsight is the key to  making it at this casino ringo. Good eyes");
				user.setPurse(user.getPurse() + (playerInsurance * 2));
				HouseBank -= playerInsurance *2; 
			}
			else {
				System.out.println("To bad you didn't get the insurance. But, that is okay I have never been a fan of finance and banks too");
			}
//			resetGame();
		}
		else if(gameDealer.handValue() == 21 && gameDealer.handValue() == user.getPlayerHand().HandValue()) {
			System.out.println("Looks like the house works in mysterious ways");
//			resetGame();
		}
		else if(gameDealer.handValue() == user.getPlayerHand().HandValue()){
			System.out.println("When push comes to shove the house gets its way");
//			user.setPurse(user.getPurse() +playerWager);
//			resetGame();
		}
		else if(gameDealer.handValue() > 21) {
			System.out.println("Ain't that something? Don't s'pose you have them river fairy's helping ya. ");
			user.setPurse(user.getPurse()+ (playerWager * ODDS));
			HouseBank -= playerWager *ODDS;
//			resetGame();
		}
		else if(gameDealer.handValue() > user.getPlayerHand().HandValue() && (gameDealer.handValue() <= 21)) {
			System.out.println("The house wins fair and square partner good luck next time. ");
//			user.setPurse(user.getPurse() - playerWager);
			HouseBank +=playerWager;
//			resetGame();
		}
		else if(user.getPlayerHand().HandValue() > gameDealer.handValue() && (user.getPlayerHand().HandValue() <=21)) {
			System.out.println("Coming into the lions den and winning like that. Impressive");
			user.setPurse(user.getPurse() + (playerWager * ODDS));
			HouseBank -= playerWager;
//			resetGame();
		}
	}
	public void splitInterface() {
		System.out.println("You have a pair would  you like to split your cards? y/n" );
		input = userInput.next(); 
		
		if(input.equals("y")){
			splitWage = playerWager;
			user.setPurse(user.getPurse() - splitWage);
			splitWage = user.doubleDown(splitWage, userInput);
			while(user.hit(userInput, input, user.getSplitHand().HandValue())) {
				if(user.isDoubleDown()) {
					user.setDoubleDown(false);
					user.drawCardSplitHand(gameDealer.playerDrawCard(), user.getSplitHand().HandValue());
					user.showSplitHand();
					break;
				}
				else {
					user.drawCardSplitHand(gameDealer.playerDrawCard(),  user.getSplitHand().HandValue());
					user.showSplitHand();
				}
			}
		}
		else {
			return;
		}
	}
	public void recordAndShow() {
		userInput.close();
		System.out.println("It was a pleasure to have you here with us " + user.getName());
		if(saveGame()) {
			System.out.println("Your can come back anytime and your purse will be: " + user.getPurse() );
			System.out.println("==Record vs the House==");
			if(user.getPurse() < 100) {
				System.out.println("You have lost: $" + (100 - user.getPurse()));
			}
			else if(user.getPurse() == 100){
				System.out.println("Ultimately you get to walk away with out having lost anything 4" + (user.getPurse() - 100));
			}
			else {
				System.out.println("Goodness Gracious! You are walking out of here with $" + ((user.getPurse() - 100) + user.getPurse()) + " off the house and in your pocket.");
			}
		}
		else {
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
		//Clean name and remove emptySpaces
		String cleanName = cleanNameForFile();
		File playerSave = new File("playerSaves/" +cleanName+".txt");
		FileWriter fw = null; 
		BufferedWriter bw = null; 
		try {
	       fw = new FileWriter(playerSave.getAbsolutePath());
	       bw = new BufferedWriter(fw); 
	      bw.write(user.getName() + "-" + user.getPurse() + "-" + HouseBank);
	      bw.close();
	      return true; 
	  }
	  catch (IOException e) {
	      System.err.println(e.getMessage());
	  }
		return false; 
	}
	public String cleanNameForFile() {
		char[] cleanNameArray = user.getName().toCharArray();
		String cleanName = "";
		for(int i = 0; i < cleanNameArray.length; i++ ) {
			if(cleanNameArray[i] == ' ') {
				cleanNameArray[i]  = '_';
			}
			cleanName +=cleanNameArray[i];
		}
		return cleanName; 
	}
	@Override
	public boolean loadGame() {
		String[] playerData = null;
		String cleanName = cleanNameForFile();
		
		try{
			FileReader fileName = new FileReader("playerSaves/"+ cleanName+".txt");
			BufferedReader in = new BufferedReader(fileName);
			String line;
            while (( line= in.readLine()) != null) {
            		playerData = line.split("-");
                }
            user.setName(playerData[0]);
            user.setPurse(Double.parseDouble(playerData[1]));
            HouseBank = Integer.parseInt(playerData[2]);
            in.close();
            return true; 
            }
        catch (IOException e) {
        	System.out.println("New Player: " + user.getName());
        	return false; 
        }
	}
	public boolean playerExists() {
		return false; 
	}
	@Override
	public Player getPlayer() {
		return null;
	}
}