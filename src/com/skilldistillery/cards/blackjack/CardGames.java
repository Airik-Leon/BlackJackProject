package com.skilldistillery.cards.blackjack;

public interface CardGames {
public void startGame(); 
public void endGame(); 
public void resetGame();
public boolean saveGame(); 
public boolean loadGame(String name); 
public Player getPlayer();
}
