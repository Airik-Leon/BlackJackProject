package com.skilldistillery.cards.blackjack;
import java.util.Scanner;
public class BlackJackEngine implements CardGames{
	final double INSURANCE_RATE  =.5;
	final double ODDS = 1.5;
	int HouseBank = 1_000_000;
	private Player user; 
	private  Dealer gameDealer;
	private Scanner userInput; 
	private int playerWager = 0; 
	private double playerInsurance= 0; 
	
	public BlackJackEngine() {
		 user = new Player();
		 gameDealer = new Dealer();  
		 userInput = new Scanner(System.in);
		 InitialGreetings();
	}
	@Override
	public void startGame() {
		String input = null; 

		//Get the bet from the player...Player can't bet more than their purse
		do {
			playerWager  = user.placeBet(userInput, playerWager);
			if(playerWager > user.getPurse()) {
				System.out.println("You can't place a bet greater than your purse. Try another bet. ");
			}
			else {
				user.setPurse(user.getPurse() - playerWager);
			}
		}while(playerWager > user.getPurse());
		
		//Shuffle deck when cards get low
		if(gameDealer.getDeckSize() <= 10) {
			gameDealer.shuffle();
		}
		//Beginning of game first deal
		user.drawCard( gameDealer.playerDrawCard(), user.getPlayerHand().HandValue()  );
		gameDealer.houseDraw(gameDealer.handValue());
		user.drawCard(gameDealer.playerDrawCard(), user.getPlayerHand().HandValue());
		user.showPlayerHand();
		gameDealer.houseDraw(gameDealer.handValue());
		
		//Check to see if the Dealer potentially could have 21 on deal and offer insurance
		System.out.println("Dealer's top card is: " + gameDealer.TopCard());
		System.out.println();
		if(gameDealer.TopCard().getNumber()== Ranks.ACE.getPrimaryValue()) {
			System.out.println("The house has a " + gameDealer.TopCard().getSuit() + " " + gameDealer.TopCard().getName()  +" Would you like to buy insurance? y/n");
			if(user.buyInsurnace(userInput, input)) {
					playerInsurance= playerWager * INSURANCE_RATE; 
					user.setPurse((user.getPurse() - playerInsurance));
			}
		}
		//Display warning  to signal to player not to hit...But they can if they want to
		if(user.getPlayerHand().HandValue() == 21) {
			System.out.println("21 before the house flip sounds like lucky day for you already.");
			user.setPurse(user.getPurse() + (playerWager *  ODDS));
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
				user.showPlayerHand();
			}
		//Special to prevent player from knowing and counting the dealers bottom card when player bust.
			if(user.getPlayerHand().HandValue() > 21) {
				System.out.println("Greedy hands never helped anyone in a fight friend. Maybe better luck next time.");
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
		System.out.println("Deal again? ");
		resetGame();
		userInput.close();
	}
	public boolean dealerHit(int handValue, int playerHandValue) {
		if(handValue == 21) {
			return false;
		}
		else if(handValue > 21){
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
		if(loadGame(user.getName())) {
			user= getPlayer();
			System.out.println("Ah ha! Back again  " +  user.getName() + " Its the  accent  ain't it.  Welcome back.");
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
			}
			else {
				System.out.println("To bad you didn't get the insurance. But, that is okay I have never been a fan of finance and banks too");
			}
			resetGame();
		}
		else if(gameDealer.handValue() == 21 && gameDealer.handValue() == user.getPlayerHand().HandValue()) {
			System.out.println("Looks like the house works in mysterious ways");
			resetGame();
		}
		else if(gameDealer.handValue() == user.getPlayerHand().HandValue()){
			System.out.println("When push comes to shove the house gets its way");
			user.setPurse(user.getPurse() +playerWager);
			resetGame();
		}
		else if(gameDealer.handValue() > 21) {
			System.out.println("Ain't that something? Don't s'pose you have them river fairy's helping ya. ");
			user.setPurse(user.getPurse()+ (playerWager * ODDS));
			resetGame();
		}
		else if(gameDealer.handValue() > user.getPlayerHand().HandValue() && (gameDealer.handValue() <= 21)) {
			System.out.println("The house wins fair and square partner good luck next time. ");
			resetGame();
		}
		else if(user.getPlayerHand().HandValue() > gameDealer.handValue() && (user.getPlayerHand().HandValue() <=21)) {
			System.out.println("Coming into the lions den and winning like that. Impressive");
			resetGame();
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
		return false; 
	}

	@Override
	public boolean loadGame(String name) {
		return false;
	}
	@Override
	public Player getPlayer() {
		return null;
	}
}
