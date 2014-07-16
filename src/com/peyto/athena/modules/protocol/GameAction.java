package com.peyto.athena.modules.protocol;

public enum GameAction {
	MOVE ("move"),
	ATTACK ("attack"),
	END_TURN ("end_turn"),
	REFRESH("refresh"),
	FEEDBACK("feedback");
	
	private String parameter;
	
	private GameAction(String par) {
		this.parameter = par;
	}
	
	public static GameAction getGameAction(String par) {
		if (par==null) return null;
		if (par.equalsIgnoreCase(MOVE.parameter)) return MOVE;
		if (par.equalsIgnoreCase(ATTACK.parameter)) return ATTACK;
		if (par.equalsIgnoreCase(END_TURN.parameter)) return END_TURN;
		if (par.equalsIgnoreCase(REFRESH.parameter)) return REFRESH;
		if (par.equalsIgnoreCase(FEEDBACK.parameter)) return FEEDBACK;
		return null;
	}
}
