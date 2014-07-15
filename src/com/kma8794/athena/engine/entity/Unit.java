package com.kma8794.athena.engine.entity;

import java.util.Random;

import com.kma8794.athena.engine.controllers.FormulasAndBallance;

public class Unit {
	Integer id;
	Team team;
	UnitType unitType;
	
	int currentUnits;
	double currentMorale;
	double curActions;
	boolean canAttack;
	
	double secretLuck;
	
	// top, right, bottom, left
	// TODO use it
	boolean[] flangsAttacked = new boolean[4];
	
	public Unit(Team team, UnitType unitType) {
		super();
		id = (new Random()).nextInt();
		this.team = team;
		this.unitType = unitType;
		currentUnits = 100;
		currentMorale = unitType.moraleBase;
		
		secretLuck = (new Random()).nextGaussian();
	}
	
	private double countCurAction(int baseSpeed) {
		double result = 0;
		// TODO make formula
		result = ((double) baseSpeed + (double) secretLuck * 2 + (double) currentMorale / 5 + ((new Random()).nextDouble() - 0.5) * 4) * 0.6;
		if (result < 1)
			result = 1.5d;
		return result;
	}
	
	/**
	 * Can be plus or minus (attack or defense)
	 * 
	 * @param harmed
	 * @return
	 */
	private void countMorale(int harmed, int ownHarm) {
		// TODO implement
		double res = 0;
		if (ownHarm!=0) {
			res = (double)harmed/(double)ownHarm-1;
		} else {
			res = 0;
		}
		
		
		currentMorale += res;
		
		if (currentMorale > 20)
			currentMorale = 20;
		if (currentMorale < 0)
			currentMorale = 0;
	}
	
	private double countAttackingPoints(UnitType defense) {
		int baseAttack = 0;
		
		switch (defense) {
		case Archers:
		case Clubs:
		case Rebels:
		case Swords:
		case Pikes:
		case HeavySwords:
		case Phalanx:
			baseAttack = this.getUnitType().getAttackInfantry();
			break;
		case HorseArchers:
		case Horses:
		case Knights:
			baseAttack = this.getUnitType().getAttackHorse();
			break;
		}
		
		/* 0.8 - 1 - 1.7*/
		double moraleEffect = (currentMorale*currentMorale)/400 - currentMorale/200 + 0.8;
		double attEff = baseAttack*moraleEffect + secretLuck*3/2 + ((new Random()).nextDouble()-1);
		return attEff;
		
	}
	
	private double countDefensivePoints(UnitType attack) {
		int baseDefense = 0;
		
		switch (attack) {
		case Archers:
		case HorseArchers:
			baseDefense = this.getUnitType().getDefArchers();
			break;
		case Clubs:
		case Rebels:
		case Swords:
		case Pikes:
		case HeavySwords:
		case Phalanx:
			baseDefense = this.getUnitType().getDefenseInfantry();
			break;
		case Horses:
		case Knights:
			baseDefense = this.getUnitType().getDefHorse();
			break;
		}
		
		double moraleEffect = (currentMorale*currentMorale)/400 - currentMorale/200 + 0.8;
		double attEff = baseDefense*moraleEffect + secretLuck*3/2 + ((new Random()).nextDouble()-1);
		return attEff;
	}
	
	
	
	public int getFlanksAttacked() {
		int res = 0;
		for (int i=0; i<flangsAttacked.length; i++) {
			if (flangsAttacked[i]) res++;
		}
		return res;
	}
	
	/**
	 * @param defense
	 * @param diag
	 * @param flankAttacked
	 * @param flankDefensed
	 */
	public void attack(Unit defense, double flanksDefensed, int attackedFromFlank, boolean firstAttackOfTheGame) {
		// def/(2)^fa, flankDefensed: def*(3/2)^fd
		defense.flangsAttacked[attackedFromFlank] = true;
		
		double attPoints = countAttackingPoints(defense.getUnitType());
		double defPoints = defense.countDefensivePoints(this.getUnitType());
		
		
		int flanksAttacked = defense.getFlanksAttacked();
		
		
		double defKoeff = 1;
		if (flanksAttacked>1) defKoeff = defKoeff/ Math.pow(2, (flanksAttacked-1));
		if (flanksDefensed>0) defKoeff = defKoeff*Math.pow(1.5, flanksDefensed);
		
		// Both sides are hurted
		// But attackers can't be flanked
		int[] units_hitted = FormulasAndBallance.getUnitsHittedByAttack(attPoints, firstAttackOfTheGame?this.currentUnits/2:this.currentUnits, defense.currentUnits, defKoeff*defPoints);
		
		// count new morale
		this.countMorale(units_hitted[1], units_hitted[0]);
		defense.countMorale(units_hitted[0], units_hitted[1]);
		
		//  losses
		this.currentUnits -= units_hitted[0];
		defense.currentUnits -= units_hitted[1];

		if (this.currentUnits < 0)
			this.currentUnits = 0;
		if (defense.currentUnits < 0)
			defense.currentUnits = 0;
		this.canAttack = false;
	}
	
	public void generateNewTurn(Board board) {
		canAttack = true;
		for (int i=0; i<flangsAttacked.length; i++) flangsAttacked[i] = false;
		if (board.isTouchedByOpponents(this)) {
			curActions = 1;
		} else {
			curActions = countCurAction(unitType.getSpeed());
		}
	}
	
	public void makeMove(double distance) {
		this.curActions -= distance;
	}
	
	public void touchedOpponentDuringTurn() {
		this.curActions = 0;
	}
	
	public int getCurrentUnits() {
		return currentUnits;
	}
	
	public UnitType getUnitType() {
		return unitType;
	}
	
	public Team getTeam() {
		return team;
	}
	
	public Integer getId() {
		return id;
	}
	
	public double getCurrentMorale() {
		return currentMorale;
	}
	
	public double getCurActions() {
		return curActions;
	}
	
	public boolean isCanAttack() {
		return canAttack;
	}
	
	public int getRange() {
		return unitType.getRange();
	}
	
	public static void main(String[] args) {
		for (int i=0; i<1000; i++) {
			Unit unit = new Unit(new Team(1), UnitType.Clubs);
			System.out.println(unit.countAttackingPoints(UnitType.Clubs));
		}
		
	}
	
}
