<%@page import="java.security.Principal"%>
<%@page import="com.peyto.athena.engine.entity.Game"%>
<%@page import="com.peyto.athena.services.GameFactory"%>
<%@page import="com.peyto.athena.services.Environment"%>
<%@page import="com.peyto.athena.services.impl.EnvironmentSingleton"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	Environment env = EnvironmentSingleton.getEnvironment();

	Principal principal = request.getUserPrincipal();
	
	if (principal!=null) {
		String user = principal.getName();
		
		Integer gameId = Integer.valueOf(request.getParameter("id"));
		Integer playerNumber = Integer.valueOf(request.getParameter("player_number"));
		String playerName = request.getParameter("player_name");
		
		
		Game game = env.getUniqueObject(gameId);
		game.setPlayer(playerNumber, playerName);
		
		
		String redirectURL = "game.jsp?id="+gameId;
	    response.sendRedirect(redirectURL);
	} else {
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Strategy Game</title>
<body>
	<h1>Please authorize to create games</h1>
</body>
<br />
<a href="./">Back to home page</a>
</html>
<%  }  %>