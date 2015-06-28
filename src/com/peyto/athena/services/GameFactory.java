package com.peyto.athena.services;

import com.peyto.athena.engine.entity.Game;

public class GameFactory {
	
	private final Environment env;
	private final ResourceGenerator ids;
	
	public GameFactory(Environment env, ResourceGenerator ids) {
		this.ids = ids;
		this.env = env;
	}
	
	public Game createGame(int players_number, String mapName) {
		Game game = null;
		if (mapName!=null && !mapName.equals("")) {
			game = new Game(ids.generateUniqueId(), players_number, mapName);
		} else {
			// For some reason map was not selected
			game = new Game(ids.generateUniqueId(), players_number, 50, 30);
		}
		env.storeUniqueObject(game.getId(), game);
		
		return game;
	}
	
}
