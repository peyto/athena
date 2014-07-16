package com.peyto.athena.services;

import com.peyto.athena.engine.entity.Game;

public class GameFactory {
	
	private final Environment env;
	private final ResourceGenerator ids;
	
	public GameFactory(Environment env, ResourceGenerator ids) {
		this.ids = ids;
		this.env = env;
	}
	
	public Game createGame(int players_number) {
		Game game = new Game(ids.generateUniqueId(), players_number, 30);
		env.storeUniqueObject(game.getId(), game);
		
		return game;
	}
	
}
