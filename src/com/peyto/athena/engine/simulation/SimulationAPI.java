package com.peyto.athena.engine.simulation;

import java.lang.reflect.Field;

import com.peyto.athena.engine.controllers.FormulasAndBallance;
import com.peyto.athena.engine.controllers.fight.AttackResult;
import com.peyto.athena.engine.entity.Unit;
import com.peyto.athena.engine.entity.UnitType;

public class SimulationAPI {
	
	public AttackResult attack(Unit attack, Unit defense, boolean byBow) {
		AttackResult r = FormulasAndBallance.attack(attack, defense, 1, byBow);
		return r;
	}
	
	public AttackResult attack(UnitType attack, UnitType defense, boolean byBow) {
		AttackResult r = FormulasAndBallance.attack(setUnit(attack), setUnit(defense), 1, byBow);
		return r;
	}
	
	public String curActions(Unit unit) {
		return unit.getUnitType().toString() + " ("+unit.getUnitType().getSpeed()+") =" +FormulasAndBallance.countActions(unit);
	}
	
	public String curActions(UnitType unit) {
		return curActions(setUnit(unit));
	}
	
	public Unit setUnit(UnitType type, int unitCount, int curMorale) {
		Unit u = setUnit(type);
		
		Field count;
		try {
			count = Unit.class.getDeclaredField("currentUnits");
			
			count.setAccessible(true);
			count.set(u, unitCount);
			
			Field morale = Unit.class.getDeclaredField("currentMorale");
			count.setAccessible(true);
			morale.set(u, curMorale);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return u;
	}
	
	public Unit setUnit(UnitType type) {
		return new Unit(null, 1, type);
	}
	
	private static void simulateAttacks(SimulationAPI simulation) {
		System.out.println("Rebels vs Rebels");
		System.out.println(simulation.attack(UnitType.Rebels, UnitType.Rebels, false));
		System.out.println(simulation.attack(UnitType.Rebels, UnitType.Rebels, false));
		System.out.println(simulation.attack(UnitType.Rebels, UnitType.Rebels, false));
		System.out.println(simulation.attack(UnitType.Rebels, UnitType.Rebels, false));
		System.out.println(simulation.attack(UnitType.Rebels, UnitType.Rebels, false));
		
		System.out.println("HSword vs HSwords");
		System.out.println(simulation.attack(UnitType.HeavySwords, UnitType.HeavySwords, false));
		System.out.println(simulation.attack(UnitType.HeavySwords, UnitType.HeavySwords, false));
		System.out.println(simulation.attack(UnitType.HeavySwords, UnitType.HeavySwords, false));
		System.out.println(simulation.attack(UnitType.HeavySwords, UnitType.HeavySwords, false));
		System.out.println(simulation.attack(UnitType.HeavySwords, UnitType.HeavySwords, false));
		
		System.out.println("Rebels vs Knights");
		System.out.println(simulation.attack(UnitType.Rebels, UnitType.Knights, false));
		System.out.println(simulation.attack(UnitType.Rebels, UnitType.Knights, false));
		System.out.println(simulation.attack(UnitType.Rebels, UnitType.Knights, false));
		System.out.println(simulation.attack(UnitType.Rebels, UnitType.Knights, false));
		System.out.println(simulation.attack(UnitType.Rebels, UnitType.Knights, false));
		
		System.out.println("Archers vs Rebels");
		System.out.println(simulation.attack(UnitType.Archers, UnitType.Rebels, true));
		System.out.println(simulation.attack(UnitType.Archers, UnitType.Rebels, true));
		System.out.println(simulation.attack(UnitType.Archers, UnitType.Rebels, true));
		System.out.println(simulation.attack(UnitType.Archers, UnitType.Rebels, true));
		System.out.println(simulation.attack(UnitType.Archers, UnitType.Rebels, true));
		
		System.out.println("Archers vs Swords");
		System.out.println(simulation.attack(UnitType.Archers, UnitType.Swords, true));
		System.out.println(simulation.attack(UnitType.Archers, UnitType.Swords, true));
		System.out.println(simulation.attack(UnitType.Archers, UnitType.Swords, true));
		System.out.println(simulation.attack(UnitType.Archers, UnitType.Swords, true));
		System.out.println(simulation.attack(UnitType.Archers, UnitType.Swords, true));
		
		System.out.println("Archers vs Knights");
		System.out.println(simulation.attack(UnitType.Archers, UnitType.Knights, true));
		System.out.println(simulation.attack(UnitType.Archers, UnitType.Knights, true));
		System.out.println(simulation.attack(UnitType.Archers, UnitType.Knights, true));
		System.out.println(simulation.attack(UnitType.Archers, UnitType.Knights, true));
		System.out.println(simulation.attack(UnitType.Archers, UnitType.Knights, true));
		
		System.out.println("Some cases:");
		System.out.println("Rebels vs Archers");
		System.out.println(simulation.attack(UnitType.Rebels, UnitType.Archers, false));
		System.out.println("Swords vs Archers");
		System.out.println(simulation.attack(UnitType.Swords, UnitType.Archers, false));
		System.out.println("Pikes vs Archers");
		System.out.println(simulation.attack(UnitType.Pikes, UnitType.Archers, false));
		System.out.println("Horses vs Archers");
		System.out.println(simulation.attack(UnitType.Horses, UnitType.Archers, false));
		System.out.println("Horses vs Phalanx");
		System.out.println(simulation.attack(UnitType.Horses, UnitType.Phalanx, false));
		System.out.println("Horses vs Swords");
		System.out.println(simulation.attack(UnitType.Horses, UnitType.Swords, false));
	}
	
	private static void sumulateActions(SimulationAPI simulation) {
		System.out.println(simulation.curActions(UnitType.Rebels));
		System.out.println(simulation.curActions(UnitType.Rebels));
		System.out.println(simulation.curActions(UnitType.Rebels));
		System.out.println(simulation.curActions(UnitType.Rebels));
		System.out.println(simulation.curActions(UnitType.Rebels));
		
		System.out.println(simulation.curActions(UnitType.Swords));
		System.out.println(simulation.curActions(UnitType.Swords));
		System.out.println(simulation.curActions(UnitType.Swords));
		System.out.println(simulation.curActions(UnitType.Swords));
		System.out.println(simulation.curActions(UnitType.Swords));
		
		System.out.println(simulation.curActions(UnitType.Phalanx));
		System.out.println(simulation.curActions(UnitType.Phalanx));
		System.out.println(simulation.curActions(UnitType.Phalanx));
		System.out.println(simulation.curActions(UnitType.Phalanx));
		System.out.println(simulation.curActions(UnitType.Phalanx));
		
		System.out.println(simulation.curActions(UnitType.HeavySwords));
		System.out.println(simulation.curActions(UnitType.HeavySwords));
		System.out.println(simulation.curActions(UnitType.HeavySwords));
		System.out.println(simulation.curActions(UnitType.HeavySwords));
		System.out.println(simulation.curActions(UnitType.HeavySwords));
		
		System.out.println(simulation.curActions(UnitType.Horses));
		System.out.println(simulation.curActions(UnitType.Horses));
		System.out.println(simulation.curActions(UnitType.Horses));
		System.out.println(simulation.curActions(UnitType.Horses));
		System.out.println(simulation.curActions(UnitType.Horses));
		
		System.out.println(simulation.curActions(UnitType.Knights));
		System.out.println(simulation.curActions(UnitType.Knights));
		System.out.println(simulation.curActions(UnitType.Knights));
		System.out.println(simulation.curActions(UnitType.Knights));
		System.out.println(simulation.curActions(UnitType.Knights));
		
	}
	
	public static void main(String[] args) {
		SimulationAPI simulation = new SimulationAPI();
		simulateAttacks(simulation);
		System.out.println("------ ACTIONS -----");
		sumulateActions(simulation);
	}
}
