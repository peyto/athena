package com.peyto.athena.modules.protocol;

import java.util.ArrayList;
import java.util.List;

import com.peyto.athena.engine.entity.Unit;

public class FullGameRefresh {
	final Integer current_team;
	final String current_player;
	final boolean started;
	final List<UnitData> units;
	
	public FullGameRefresh(boolean started, Integer idTeam, String playerLogin, List<Unit> team1, List<Unit> team2) {
		this.current_team = idTeam;
		this.current_player = playerLogin;
		this.started = started;
		this.units = new ArrayList<UnitData>();
		if (team1 != null) {
			for (Unit unit : team1) {
				if (Double.isNaN(unit.getCurrentMorale()) || Double.isNaN(unit.getCurActions())) {
					System.out.println("alert");
				}
				units.add(new UnitData(unit));
			}
		}
		if (team2 != null) {
			for (Unit unit : team2) {
				if (Double.isNaN(unit.getCurrentMorale()) || Double.isNaN(unit.getCurActions())) {
					System.out.println("alert");
				}
				units.add(new UnitData(unit));
			}
		}
	}
}
