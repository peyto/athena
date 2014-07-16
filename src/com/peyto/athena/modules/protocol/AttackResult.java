package com.peyto.athena.modules.protocol;

import java.util.ArrayList;
import java.util.List;

public class AttackResult extends ActiveAction{
	
	List<UnitData> unit_updates;
	
	public AttackResult(String result, UnitData attacker, UnitData defender) {
		super(result);
		this.unit_updates = new ArrayList<UnitData>();
		unit_updates.add(attacker);
		unit_updates.add(defender);
	}
	
	public AttackResult(String error) {
		super(error);
	}
	
	public String getResult() {
		return result;
	}
	
	public List<UnitData> getUnit_updates() {
		return unit_updates;
	}
	
	
}
