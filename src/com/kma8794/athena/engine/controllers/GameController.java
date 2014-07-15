package com.kma8794.athena.engine.controllers;

import com.kma8794.athena.engine.entity.Game;
import com.kma8794.athena.modules.protocol.AttackResult;
import com.kma8794.athena.modules.protocol.EndTurnResult;
import com.kma8794.athena.modules.protocol.FullGameRefresh;
import com.kma8794.athena.modules.protocol.MoveResult;
import com.kma8794.athena.modules.protocol.UpdateEvent;

public interface GameController {
	
	public MoveResult move(Game game, int unitId, int x, int y);
	
	public EndTurnResult endTurn(Game game, int curTeamId);
	
	public AttackResult attack(Game game, int unitId, int x, int y);
	
	public FullGameRefresh refresh(Game game);
	
	public UpdateEvent getBlockingEvent(Game game);
}
