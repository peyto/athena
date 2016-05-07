package com.peyto.athena.engine.entity;

import com.peyto.athena.engine.Player;
import com.peyto.athena.engine.math.HexUtils;
import com.peyto.athena.engine.math.NormalizedHexagonCoordinates;
import com.peyto.athena.modules.protocol.UpdateEvent;

public class Game {
	int id;
	Board board;
	GameStatus gameStatus;
	Team[] teams;
	Player[] players;
	
	UpdateEvent lastOccurredEvent = null;
	
	private Game(int id, int playersNum, Board board) {
		this.id = id;
		this.board = board;
		
		gameStatus = new GameStatus();		
		teams = new Team[] {new Team(1), new Team(2)};
		players = new Player[playersNum];
		
		fillDefaultUnits(board, teams[0], 2, board.getSizeX()/2-1, 1);
		fillDefaultUnits(board, teams[1], board.getSizeX()-3, board.getSizeY()/2-1, -1);
	}
	
	public Game(int id, int playersNum, int boardSizeX, int boardSizeY) {
		this(id, playersNum, new Board(boardSizeX, boardSizeY));
	}
	
	public Game(int id, int playersNum, String mapName) {
		this(id, playersNum, new Board(mapName));
	}
	
	public Team[] getTeams() {
		return teams;
	}
	
	private boolean allPlayersSetted() {
		for (int i=0; i<players.length; i++) {
			if (players[i]==null) return false;
		}
		return true;
	}
	
	private boolean playerInGame(String login) {
		for (int i=0; i<players.length; i++) {
			if (players[i]!=null && players[i].getLogin().equals(login)) {
				return true;
			}
		}
		return false;
	}
	
	public void setPlayer(int num, String player) {
		if (num < players.length && !playerInGame(player)) {
			Player p = new Player(player);
			if (players[num]==null) {
				players[num] = p;
				if (allPlayersSetted()) {
					gameStatus.setGameStarted();
					teams[0].generateNewTurn(getBoard());
					gameStatus.setCurrentTurnId(0);
					synchronized (this) {
						this.setLastOccurredEvent(new UpdateEvent("start", null));
						this.notifyAll();
					}
				}
			}
		}
	}
	
	public boolean isGameStarted() {
		return gameStatus.isGameStarted();
	}
	
	private void setNormalized(Board board, Unit u, int x, int y) {
		NormalizedHexagonCoordinates c = HexUtils.convertToNormalized(x, y);
		board.setUnitOnBoard(u, c.getX(), c.getY());
		
	}
	
	private void fillDefaultUnits(Board board, Team team, int x0, int y0, int xn) {
		/*
		Unit t11 = new Unit(team, UnitType.Clubs);
		Unit t12 = new Unit(team, UnitType.Clubs);
		Unit t13 = new Unit(team, UnitType.Clubs);
		Unit t14 = new Unit(team, UnitType.Clubs);
		Unit t15 = new Unit(team, UnitType.Clubs);
		Unit t16 = new Unit(team, UnitType.Clubs);
		setNormalized(board, t11, x0+xn*3, y0-3);
		setNormalized(board, t12, x0+xn*3, y0-2);
		setNormalized(board, t13, x0+xn*3, y0-1);
		setNormalized(board, t14, x0+xn*2, y0-3);
		setNormalized(board, t15, x0+xn*2, y0-2);
		setNormalized(board, t16, x0+xn*2, y0-1);
		*/
		
		Unit t21 = new Unit(team, 1, UnitType.Rebels);
		Unit t22 = new Unit(team, 2, UnitType.Rebels);
		Unit t23 = new Unit(team, 3, UnitType.Rebels);
		
		Unit t24 = new Unit(team, 1, UnitType.Archers);
		Unit t25 = new Unit(team, 2, UnitType.Archers);
		Unit t26 = new Unit(team, 3, UnitType.Archers);


		setNormalized(board, t21, x0+xn*3, y0+2);
		setNormalized(board, t22, x0+xn*3, y0+3);
		setNormalized(board, t23, x0+xn*3, y0+4);
		setNormalized(board, t24, x0+xn*2, y0+2);
		setNormalized(board, t25, x0+xn*2, y0+3);
		setNormalized(board, t26, x0+xn*2, y0+4);
		
		Unit t31 = new Unit(team, 1, UnitType.Swords);
		Unit t32 = new Unit(team, 2, UnitType.Swords);
		Unit t33 = new Unit(team, 3, UnitType.Swords);
		Unit t34 = new Unit(team, 4, UnitType.Swords);
		setNormalized(board, t31, x0+xn*2, y0);
		setNormalized(board, t32, x0+xn*2, y0+1);
		setNormalized(board, t33, x0+xn*3, y0);
		setNormalized(board, t34, x0+xn*3, y0+1);
		
		Unit t41 = new Unit(team, 1, UnitType.Pikes);
		Unit t42 = new Unit(team, 2, UnitType.Pikes);
		Unit t43 = new Unit(team, 3, UnitType.Pikes);
		Unit t44 = new Unit(team, 4, UnitType.Pikes);
		setNormalized(board, t41, x0+xn, y0-3);
		setNormalized(board, t42, x0+xn, y0-2);
		setNormalized(board, t43, x0+xn, y0-1);
		setNormalized(board, t44, x0+xn, y0);
		
		
		Unit t51 = new Unit(team, 1, UnitType.HeavySwords);
		Unit t52 = new Unit(team, 2, UnitType.HeavySwords);
		Unit t53 = new Unit(team, 3, UnitType.HeavySwords);
		Unit t54 = new Unit(team, 4, UnitType.HeavySwords);
		setNormalized(board, t51, x0-1*xn, y0);
		setNormalized(board, t52, x0, y0);
		setNormalized(board, t53, x0-1*xn, y0+1);
		setNormalized(board, t54, x0, y0+1);
		
		Unit t61 = new Unit(team, 1, UnitType.Phalanx);
		Unit t62 = new Unit(team, 2, UnitType.Phalanx);
		Unit t63 = new Unit(team, 3, UnitType.Phalanx);
		Unit t64 = new Unit(team, 4, UnitType.Phalanx);
		setNormalized(board, t61, x0+xn, y0+1);
		setNormalized(board, t62, x0+xn, y0+2);
		setNormalized(board, t63, x0+xn, y0+3);
		setNormalized(board, t64, x0+xn, y0+4);
		
		Unit t71 = new Unit(team, 1, UnitType.Horses);
		Unit t72 = new Unit(team, 2, UnitType.Horses);
		Unit t73 = new Unit(team, 3, UnitType.Horses);
		Unit t74 = new Unit(team, 1, UnitType.HorseArchers);
		Unit t75 = new Unit(team, 1, UnitType.HorseArchers);
		setNormalized(board, t71, x0+2*xn, y0-5);
		setNormalized(board, t72, x0+1*xn, y0-5);
		setNormalized(board, t73, x0, y0-5);
		setNormalized(board, t74, x0+1*xn, y0+6);
		setNormalized(board, t75, x0+2*xn, y0+6);
		
		Unit t81 = new Unit(team, 1, UnitType.Knights);
		Unit t82 = new Unit(team, 2, UnitType.Knights);
		setNormalized(board, t81, x0, y0+3);
		setNormalized(board, t82, x0, y0+2);
		
		team.setUnits(//t11, t12, t13, t14, t15, t16,
				t21, t22, t23, t24, t25, t26,
				t31, t32, t33, t34, 
				t41, t42, t43, t44,
				t51, t52, t53, t54,
				t61, t62, t63, t64,
				t71, t72, t73, t74, t75,
				t81, t82
				
		);
	}
	
	public Board getBoard() {
		return board;
	}
	
	public Player getCurrentTurnPlayer() {
		if (gameStatus.getCurrentTurnId()!=-1) {
			return players[gameStatus.getCurrentTurnId()];
		} else {
			return null;
		}
		
	}
	
	public Team getCurrentTeamTurn() {
		if (gameStatus.getCurrentTurnId()!=-1) {
			return teams[gameStatus.getCurrentTurnId()];
		} else {
			return null;
		}
	}
	
	public Team endTurn() {
		int newIndex = gameStatus.getCurrentTurnId()+1;
		if (newIndex>=teams.length) newIndex=0;
		
		gameStatus.endTurnChecks();
		gameStatus.setCurrentTurnId(newIndex);
		
		teams[newIndex].generateNewTurn(getBoard());
		return teams[newIndex];
	}

	public int getPlayers() {
		return players.length;
	}
	
	public Player getPlayer(int num) {
		return players[num];
	}
	
	public int getId() {
		return id;
	}
	
	public synchronized void setLastOccurredEvent(UpdateEvent lastOccurredEvent) {
		this.lastOccurredEvent = lastOccurredEvent;
	}
	
	public synchronized UpdateEvent getLastOccurredEvent() {
		return lastOccurredEvent;
	}
}
