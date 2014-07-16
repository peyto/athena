package com.peyto.athena.engine.controllers;

public class FormulasAndBallance {
	public static int[] getUnitsHittedByAttack(double attackEff, int attUnits, int defenseUnits, double defEff) {
		/* Formula: 
		 * 2*attack vs 1*defense (%attackers looses + %defense losses = 100%)
		 * 1 defense alive response
		 */
		System.out.print("Attack: ["+((int)attackEff)+", "+attUnits+"] x ["+((int)defEff)+", "+defenseUnits+"]");
		double attPoints = 2*attackEff*attUnits;
		double defPoints = defEff*defenseUnits;
		
		double defLosses = defenseUnits*Math.pow(1-defPoints/(attPoints+defPoints), 2);
		double attLosses = attUnits*Math.pow(1-attPoints/(attPoints+defPoints), 2);
		
		double attPoints2Iter = attackEff*attUnits;
		double defPoints2Iter = defEff*(defenseUnits-defLosses);
		
		double attlosses2 = attUnits*Math.pow(1-attPoints2Iter/(attPoints2Iter+defPoints2Iter), 2);
		
		System.out.println("   =>   Result: Att["+Math.round(attUnits-attLosses-attlosses2)+"] Def["+Math.round(defenseUnits-defLosses)+"]");
		return new int[]{(int)Math.round(attLosses+attlosses2), (int)Math.round(defLosses)};
	}
	
	public static void main(String[] args) {
		getUnitsHittedByAttack(6, 100, 100, 6);
		getUnitsHittedByAttack(18, 100, 100, 6);
		getUnitsHittedByAttack(10, 100, 100, 15);
		getUnitsHittedByAttack(6, 90, 20, 6);
		getUnitsHittedByAttack(6, 20, 90, 6);
		getUnitsHittedByAttack(16, 20, 100, 4);
		getUnitsHittedByAttack(6, 89, 5, 6);
		
	}
}
