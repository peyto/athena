package com.peyto.athena.engine.entity;

public class GameStatus {
	private boolean gameStarted = false;
	
	
	private int currentTurnId = -1;
	private boolean firstAttackInGame;
	
	private boolean pendingFirstTimeAttack = false;
	
	public void setGameStarted() {
		gameStarted = true;
	}
	
	public boolean isGameStarted() {
		return gameStarted;
	}
	
	public void setCurrentTurnId(int currentTeamTurn) {
		this.currentTurnId = currentTeamTurn;
		this.firstAttackInGame = true;
	}
	
	public void endTurnChecks() {
		if (pendingFirstTimeAttack) {
			firstAttackInGame = true;
		}
	}
	
	public int getCurrentTurnId() {
		return currentTurnId;
	}
	
	public boolean isFirstAttackInGame() {
		return firstAttackInGame;
	}
	
	public void setFirstAttackInGame() {
		// we are setting pending, and will change value at the end of the turn
		this.pendingFirstTimeAttack = true;
	}
}
