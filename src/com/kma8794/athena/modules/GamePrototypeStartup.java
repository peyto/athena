package com.kma8794.athena.modules;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.kma8794.athena.engine.controllers.GameController;
import com.kma8794.athena.engine.controllers.GameControllerImpl;
import com.kma8794.athena.services.Environment;
import com.kma8794.athena.services.GameFactory;
import com.kma8794.athena.services.ResourceGenerator;
import com.kma8794.athena.services.impl.EnvironmentSingleton;
import com.kma8794.athena.services.impl.ResourceGeneratorImpl;

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
