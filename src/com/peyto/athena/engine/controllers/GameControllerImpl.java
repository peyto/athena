package com.peyto.athena.engine.controllers;

import com.peyto.athena.engine.entity.Game;
import com.peyto.athena.engine.entity.Team;
import com.peyto.athena.engine.entity.Unit;
import com.peyto.athena.modules.protocol.AttackResult;
import com.peyto.athena.modules.protocol.EndTurnResult;
import com.peyto.athena.modules.protocol.FullGameRefresh;
import com.peyto.athena.modules.protocol.MoveResult;
import com.peyto.athena.modules.protocol.UnitData;
import com.peyto.athena.modules.protocol.UpdateEvent;

public class GameControllerImpl implements GameController {
	@Override
	public MoveResult move(Game game, int unitId, int x, int y) {
		System.out.println("[" + game + "] move (" + unitId + ", " + x + ", y" + ")");
		int curX = game.getBoard().getCoordinates(unitId)[0];
		int curY = game.getBoard().getCoordinates(unitId)[1];
		Unit unit = game.getBoard().getUnit(unitId);
		
		double distance = Math.sqrt((curX - x) * (curX - x) + (curY - y) * (curY - y));
		if (distance <= unit.getCurActions()) {
			// TODO Implement step-by-step logics
			unit.makeMove(distance);
			game.getBoard().moveUnit(unit, x, y);
			
			if (game.getBoard().isTouchedByOpponents(unit)) {
				unit.touchedOpponentDuringTurn();
			}
			int[] coordinates = game.getBoard().getCoordinates(unitId);
			MoveResult moveResult = new MoveResult("ok", unitId, curX, curY, coordinates[0], coordinates[1], unit.getCurActions());
			synchronized (game) {
				game.setLastOccurredEvent(new UpdateEvent("move_result", moveResult));
				game.notifyAll();
				return moveResult;
			}
		} else {
			return new MoveResult("error");
		}
	}
	
	@Override
	public AttackResult attack(Game game, int unitId, int x, int y) {
		System.out.println("[" + game + "] attack (" + unitId + ", " + x + ", y" + ")");
		int attX = game.getBoard().getCoordinates(unitId)[0];
		int attY = game.getBoard().getCoordinates(unitId)[1];
		Unit unitAttack = game.getBoard().getUnit(unitId);
		Unit unitDefense = game.getBoard().getUnit(x, y);
		
		if (Math.abs(attX - x) > 1 || Math.abs(attY - y) > 1) {
			return new AttackResult("error");
		}
		if (!unitAttack.isCanAttack()) {
			return new AttackResult("error");
		}
		double flangsDefensed = game.getBoard().flangsDefended(unitDefense);
		unitAttack.attack(unitDefense, flangsDefensed, game.getBoard().getAttackingFlank(unitAttack, unitDefense), game.isFirstAttackInGame());
		
		if (game.isFirstAttackInGame()) {
			game.setAttackHappened();
		}
		AttackResult attackRes = new AttackResult("ok", new UnitData(unitAttack), new UnitData(unitDefense));
		
		if (unitAttack.getCurrentUnits() <= 0) {
			unitAttack.getTeam().removeKilledUnit(unitAttack);
			game.getBoard().removeDeadUnit(unitAttack);
		}
		if (unitDefense.getCurrentUnits() <= 0) {
			unitDefense.getTeam().removeKilledUnit(unitDefense);
			game.getBoard().removeDeadUnit(unitDefense);
		}
		synchronized (game) {
			game.setLastOccurredEvent(new UpdateEvent("attack_result", attackRes));
			game.notifyAll();
			return attackRes;
		}
		
	}
	
	@Override
	public EndTurnResult endTurn(Game game, int curTeamId) {
		if (game.getCurrentTeamTurn().getId() == curTeamId) {
			Team newTeam = game.endTurn();
			EndTurnResult result = new EndTurnResult("ok", newTeam.getId(), game.getCurrentTurnPlayer().getLogin(), newTeam.getUnits());
			synchronized (game) {
				game.setLastOccurredEvent(new UpdateEvent("end_turn", result));
				game.notifyAll();
				return result;
			}
			
		} else {
			// curTeamId didn't mathced with server
			return new EndTurnResult("error", game.getCurrentTeamTurn().getId(), game.getCurrentTurnPlayer().getLogin(), null);
		}
	}
	
	@Override
	public FullGameRefresh refresh(Game game) {
		return new FullGameRefresh(game.isGameStarted(), 
				game.getCurrentTeamTurn() != null ? game.getCurrentTeamTurn().getId() : null, 
				game.getCurrentTurnPlayer() != null ? game.getCurrentTurnPlayer().getLogin() : null, 
				game.getTeams()[0].getUnits(), 
				game.getTeams()[1].getUnits());
	}
	
	@Override
	public UpdateEvent getBlockingEvent(Game game) {
		synchronized (game) {
			try {
				// Waiting for event to occur
				game.wait();
				UpdateEvent result = game.getLastOccurredEvent();
				return result;
			} catch (InterruptedException e) {
				return new UpdateEvent("error", null);
			}
			
		}
	}
}
