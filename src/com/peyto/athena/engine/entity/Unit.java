package com.peyto.athena.engine.entity;

import java.util.Random;

import com.peyto.athena.engine.controllers.FormulasAndBallance;
import com.peyto.athena.engine.controllers.fight.AttackResult;

public class Unit {
	private Integer id;
	private String name;
	private Team team;
	private UnitType unitType;
	
	private int currentUnits;
	private double currentMorale;
	private double curActions;
	private boolean canAttack;
	
	private double secretLuck;
	
	// top, right, bottom, left
	// TODO use it
	private boolean[] flangsAttacked = new boolean[6];
	
	public Unit(Team team, int groupNumber, UnitType unitType) {
		super();
		id = (new Random()).nextInt();
		name = unitType.toString() + " " + groupNumber;
		this.team = team;
		this.unitType = unitType;
		currentUnits = unitType.units;
		currentMorale = unitType.moraleBase;
		
		secretLuck = (new Random()).nextGaussian();
	}
	
	private double countCurAction() {
		return FormulasAndBallance.countActions(this);
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
	public void attack(Unit defense, double flanksDefensed, int attackedFromFlank, boolean attackedByBow) {
		// def/(2)^fa, flankDefensed: def*(3/2)^fd
		// can be archers
		if (attackedFromFlank!=-1) {
			defense.flangsAttacked[attackedFromFlank] = true;
		}
		
		
		double defKoeff = 1;

		/*
		int flanksAttacked = defense.getFlanksAttacked();
		if (flanksAttacked>1) defKoeff = defKoeff/ Math.pow(2, (flanksAttacked-1));
		if (flanksDefensed>0) defKoeff = defKoeff*Math.pow(1.5, flanksDefensed);
		*/
		
		// Both sides are hurted
		// But attackers can't be flanked
		AttackResult res = FormulasAndBallance.attack(this, defense, defKoeff, attackedByBow);
		
		// count new morale
		// TODO MORALE!!!!
		this.countMorale(res.getHarmDefender(), res.getHarmAttacker());
		defense.countMorale(res.getHarmAttacker(), res.getHarmDefender());
		
		//  losses
		this.currentUnits -= res.getHarmAttacker();
		defense.currentUnits -= res.getHarmDefender();

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
			curActions = countCurAction();
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
	
	public int getRangeMin() {
		return unitType.getRangeMin();
	}
	
	public int getRangeMax() {
		return unitType.getRangeMax();
	}
	
	public String getImgSrc() {
		return unitType.getImg();
	}
	
	public double getSecretLuck() {
		return secretLuck;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
}
