package com.peyto.athena.engine.controllers;

import java.util.Random;

import com.peyto.athena.engine.controllers.fight.AttackResult;
import com.peyto.athena.engine.controllers.fight.AttackerData;
import com.peyto.athena.engine.entity.Unit;
import com.peyto.athena.engine.entity.UnitType;

public class FormulasAndBallance {
	
	
	/**
	 * Result is in [0.8; 2]
	 * 0 = -0.2, 6 = 0, 20 = 1
	 * @param morale
	 * @return
	 */
	private static double countMoraleEffect(double morale) {
		// morale %: 4/3000x^2 + 1/30x -0.2 = 0
		return 1 + ((float) morale /30 * (0.04*morale + 1) - 0.2);
	}
	
	private static AttackerData countAttackingData(Unit thisUnit, UnitType opponent, boolean bows) {
		
		double moraleEffect = countMoraleEffect(thisUnit.getCurrentMorale());
		double luckPart = thisUnit.getSecretLuck() * 3 / 2;
		double randomPart = ((new Random()).nextGaussian());
		double defBonus = Math.pow(1 + (float) thisUnit.getUnitType().getDefenseArmor() / 40, 2);
		
		int baseAttack = 0;
		if (bows) {
			baseAttack = thisUnit.getUnitType().getAttackBows();
		} else {
			switch (opponent.getUnitTypeHorse()) {
			case Infantry:
				baseAttack = thisUnit.getUnitType().getAttackInfantry();
				break;
			case Cavalary:
				baseAttack = thisUnit.getUnitType().getAttackHorse();
				break;
			}
		}
		
		return new AttackerData(Math.pow(baseAttack, 1.5), defBonus, moraleEffect, luckPart, randomPart);
		
	}
	
	
	private static AttackResult getUnitsHittedByAttack(AttackerData attack, AttackerData defense, int attUnits, int defenseUnits, double defKoeff, boolean attackedByBow) {
		double attackEff = 
			attack.getAttackingPoints() *  // base attack 
			(attackedByBow? 1: 1.15 * attack.getMoraleEffect()) + // if archer - morale is not important 
			attack.getLuckPart() + 
			attack.getRandomPart();
		double defEff = defense.getAttackingPoints() * defense.getMoraleEffect() + defense.getLuckPart() + defense.getRandomPart();
		
		if (attackedByBow) {
			attackEff = attackEff - defense.getLuckPart() - defense.getRandomPart();
		}
		double attPoints = attackEff * attUnits / defense.getDefBonus();
		double defPoints = defEff * defenseUnits / attack.getDefBonus();
		
		double defLosses, attLosses;
		// defBonus correlates with hit point
		if (attackedByBow) {
			// (0.7-1.05)^2 => (0.49-1.1, normal)
			double accuracy = (((new Random()).nextDouble())*0.35+0.7)*(((new Random()).nextDouble())*0.35+0.7);
			// defense armor is even more important against archers
			double arrowHit = accuracy * attPoints/ Math.pow(defense.getDefBonus(), 1.5);
			attLosses = 0;
			defLosses = arrowHit / (60 * defense.getDefBonus());
		} else {
			attLosses = defPoints / (60 * attack.getDefBonus());
			defLosses = attPoints / (60 * defense.getDefBonus());
		}
		
		System.out.println("   =>   Result: Att["+Math.round(attUnits-attLosses)+"] Def["+Math.round(defenseUnits-defLosses)+"]");
		return new AttackResult((int) Math.round(attLosses), (int) Math.round(defLosses), attPoints, defPoints, attack, defense);
	}
	
	public static double countActions(Unit unit) {
		// [0.9; 1.5]
		double moraleK = (countMoraleEffect(unit.getCurrentMorale())-1)/2+1;
		// normal [-1; 1] In 3 times more important, than simple random
		double luckPart = unit.getSecretLuck()*3;
		// normal [-1; 1] TODO Do we need it at all? We have secret luck which is already random
		double randomPart = ((new Random()).nextGaussian());
		double result = ((double) unit.getUnitType().getSpeed()*moraleK + (luckPart + randomPart)*0.5) * 0.75;
		if (result < 1)
			result = 1.0d;
		return result;
	}
	
	public static AttackResult attack(Unit attack, Unit defense, double defKoeff, boolean byBow) {
		AttackerData att = countAttackingData(attack, defense.getUnitType(), byBow);
		AttackerData def = countAttackingData(defense, attack.getUnitType(), byBow);
		AttackResult result = getUnitsHittedByAttack(att, def, attack.getCurrentUnits(), defense.getCurrentUnits(), defKoeff, byBow);
		return result;
	}
		
}
