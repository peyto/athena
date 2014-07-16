package com.peyto.athena.modules.protocol;

import java.util.ArrayList;
import java.util.List;

import com.peyto.athena.engine.entity.Unit;

public class EndTurnResult extends ActiveAction{
	
	final Integer new_team;
	final String new_player;
	final List<UnitData> units;
	
	public EndTurnResult(String result, Integer new_team, String new_player, List<Unit> serverUnits) {
		super(result);
		this.new_team = new_team;
		this.new_player = new_player;
		this.units = new ArrayList<UnitData>();
		if (serverUnits != null) {
			for (Unit unit : serverUnits) {
				units.add(new UnitData(unit.getId(), unit.getTeam().getId(), unit.getCurrentUnits(), unit.getCurrentMorale(), unit.getCurActions(), unit.isCanAttack()));
			}
		}
	}
	
}
