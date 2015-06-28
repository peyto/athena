package com.peyto.athena.engine.entity;

public class GameStatus {
	private boolean gameStarted = false;
	
	private int currentTurnId = -1;
	
	public void setGameStarted() {
		gameStarted = true;
	}
	
	public boolean isGameStarted() {
		return gameStarted;
	}
	
	public void setCurrentTurnId(int currentTeamTurn) {
		this.currentTurnId = currentTeamTurn;
	}
	
	public void endTurnChecks() {
	}
	
	public int getCurrentTurnId() {
		return currentTurnId;
	}
	
}
