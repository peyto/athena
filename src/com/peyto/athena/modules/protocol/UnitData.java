package com.peyto.athena.modules.protocol;

import com.peyto.athena.engine.entity.Unit;

public class UnitData {
	int id;
	int teamId;
	int currentUnits;
	double currentMorale;
	double curActions;
	boolean canAttack;
	
	public UnitData(int id, int teamId, int currentUnits, double currentMorale, double curActions, boolean canAttack) {
		this.id = id;
		this.teamId = teamId;
		this.currentUnits = currentUnits;
		this.currentMorale = currentMorale;
		this.curActions = curActions;
		this.canAttack = canAttack;
	}
	
	public UnitData(Unit unit) {
		this.id = unit.getId();
		this.teamId = unit.getTeam().getId();
		this.currentUnits = unit.getCurrentUnits();
		this.currentMorale = unit.getCurrentMorale();
		this.curActions = unit.getCurActions();
		this.canAttack = unit.isCanAttack();
	}
	
	
}
