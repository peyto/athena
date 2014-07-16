package com.peyto.athena.modules;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.peyto.athena.engine.controllers.GameController;
import com.peyto.athena.engine.controllers.GameControllerImpl;
import com.peyto.athena.services.Environment;
import com.peyto.athena.services.GameFactory;
import com.peyto.athena.services.ResourceGenerator;
import com.peyto.athena.services.impl.EnvironmentSingleton;
import com.peyto.athena.services.impl.ResourceGeneratorImpl;

public class GamePrototypeStartup extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	public void init(ServletConfig config) throws ServletException {
		Environment env = EnvironmentSingleton.getEnvironment();
		
		
		GameController gc = new GameControllerImpl(); 
		env.addService(GameController.class, gc);
		
		ResourceGenerator ids = new ResourceGeneratorImpl();
		env.addService(ResourceGenerator.class, ids);
		
		GameFactory gf = new GameFactory(env, ids);
		env.addService(GameFactory.class, gf);
		
		System.out.println("Started!!!!!!!!!!1");
	}
}
