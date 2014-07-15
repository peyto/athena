package com.kma8794.athena.engine.entity;

import java.util.ArrayList;
import java.util.List;

public class Team {
	Integer id;
	private List<Unit> units = new ArrayList<Unit>();
	
	public Team(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}
	
	public List<Unit> getUnits() {
		return units;
	}
	
	public void setUnits(Unit... unitArr) {
		for (Unit u: unitArr) {
			this.units.add(u);
		}
	}
	
	public void removeKilledUnit(Unit unit) {
		units.remove(unit);
	}
	
	public void generateNewTurn(Board board) {
		for (Unit unit : units) {
			unit.generateNewTurn(board);
		}
		
	}
}
