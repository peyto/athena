package com.kma8794.athena.modules;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import com.kma8794.athena.engine.controllers.GameController;
import com.kma8794.athena.engine.entity.Game;
import com.kma8794.athena.modules.protocol.ActiveAction;
import com.kma8794.athena.modules.protocol.GameAction;
import com.kma8794.athena.services.Environment;
import com.kma8794.athena.services.impl.EnvironmentSingleton;

public class AjaxGameHandler extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("called!");
		
		Environment env = EnvironmentSingleton.getEnvironment();
		
		GameController gc = env.getService(GameController.class);
		System.out.println("Action= "+req.getParameter("action"));
		GameAction action = GameAction.getGameAction(req.getParameter("action"));
		System.out.println("Action = "+action);
		Game game = env.getUniqueObject(Integer.valueOf(req.getParameter("id")));
		Object objToSerialize = null;
		boolean allowed = true;
		if (action==GameAction.MOVE || action==GameAction.ATTACK || action==GameAction.END_TURN) {
			// Check permissions
			allowed = false;
			Principal principal = req.getUserPrincipal();
			if (principal!=null) {
				String user = principal.getName();
				String curUser = game.getCurrentTurnPlayer().getLogin();
				if (user!=null && user.equals(curUser)) {
					allowed = true;
				}
			}
		}
		if (allowed) {
			switch (action) {
			case MOVE:
				int unitId = Integer.valueOf(req.getParameter("unit"));
				int x = Integer.valueOf(req.getParameter("toX"));
				int y = Integer.valueOf(req.getParameter("toY"));
				objToSerialize = gc.move(game, unitId, x, y);
				break;
			case ATTACK:
				int aunitId = Integer.valueOf(req.getParameter("unit"));
				int ax = Integer.valueOf(req.getParameter("toX"));
				int ay = Integer.valueOf(req.getParameter("toY"));
				objToSerialize = gc.attack(game, aunitId, ax, ay);
				break;
			case END_TURN:
				String curTeam = req.getParameter("cur_team");
				Integer curTeamId = Integer.parseInt(curTeam);
				objToSerialize = gc.endTurn(game, curTeamId);
				break;
			case REFRESH:
				objToSerialize = gc.refresh(game);
				break;
			case FEEDBACK:
				objToSerialize = gc.getBlockingEvent(game);
				break;
			default:
				resp.sendError(500);
				break;
			}
		} else {
			objToSerialize = new ActiveAction("error");
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(objToSerialize);
		
		System.out.println("result="+json);
		resp.getOutputStream().print(json);
		
	}
	
}
