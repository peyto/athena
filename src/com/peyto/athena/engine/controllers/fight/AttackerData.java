package com.peyto.athena.engine.controllers.fight;

public class AttackerData {
	
	double attackingPoints;
	double defBonus;
	double moraleEffect;
	double luckPart;
	double randomPart;
	
	
	

	
	
	public AttackerData(double attackingPoints, double defBonus, double moraleEffect, double luckPart, double randomPart) {
		super();
		this.attackingPoints = attackingPoints;
		this.defBonus = defBonus;
		this.moraleEffect = moraleEffect;
		this.luckPart = luckPart;
		this.randomPart = randomPart;
	}



	public double getAttackingPoints() {
		return attackingPoints;
	}



	public double getDefBonus() {
		return defBonus;
	}



	public double getMoraleEffect() {
		return moraleEffect;
	}



	public double getLuckPart() {
		return luckPart;
	}



	public double getRandomPart() {
		return randomPart;
	}



	public String toString() {
		String s = "att: {base %.2f, moraleEffect %.2f luck %.2f, rnd %.2f} def: {%.2f}";
		return String.format(s, attackingPoints, moraleEffect, luckPart, randomPart, defBonus);
	}
}