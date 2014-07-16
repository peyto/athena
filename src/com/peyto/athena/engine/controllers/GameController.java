package com.peyto.athena.engine.controllers;

import com.peyto.athena.engine.entity.Game;
import com.peyto.athena.modules.protocol.AttackResult;
import com.peyto.athena.modules.protocol.EndTurnResult;
import com.peyto.athena.modules.protocol.FullGameRefresh;
import com.peyto.athena.modules.protocol.MoveResult;
import com.peyto.athena.modules.protocol.UpdateEvent;

public interface GameController {
	
	public MoveResult move(Game game, int unitId, int x, int y);
	
	public EndTurnResult endTurn(Game game, int curTeamId);
	
	public AttackResult attack(Game game, int unitId, int x, int y);
	
	public FullGameRefresh refresh(Game game);
	
	public UpdateEvent getBlockingEvent(Game game);
}
