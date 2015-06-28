package ru.kma8794.athena.engine.controllers.fight;

import ru.kma8794.athena.engine.controllers.fight.AttackerData;

public class AttackResult {
	int harmAttacker;
	int harmDefender;
	
	double totalAttPoints;
	double totalDefPoints;
	
	AttackerData attacker;
	AttackerData defender;
	
	public AttackResult(int harmAttacker, int harmDefender, double totalAttPoints, double totalDefPoints, AttackerData attacker, AttackerData defender) {
		super();
		this.harmAttacker = harmAttacker;
		this.harmDefender = harmDefender;
		this.totalAttPoints = totalAttPoints;
		this.totalDefPoints = totalDefPoints;
		this.attacker = attacker;
		this.defender = defender;
	}
	
	public int getHarmAttacker() {
		return harmAttacker;
	}
	
	public int getHarmDefender() {
		return harmDefender;
	}
	
	@Override
	public String toString() {
		return 
		harmAttacker + "   " + harmDefender + "    {"+totalAttPoints + "   " + totalDefPoints + "}    " + attacker+"  " + defender;
	}
	
	
}
