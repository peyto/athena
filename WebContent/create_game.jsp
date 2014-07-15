<%@page import="java.security.Principal"%>
<%@page import="com.kma8794.athena.engine.entity.Game"%>
<%@page import="com.kma8794.athena.services.GameFactory"%>
<%@page import="com.kma8794.athena.services.Environment"%>
<%@page import="com.kma8794.athena.services.impl.EnvironmentSingleton"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	Environment env = EnvironmentSingleton.getEnvironment();

	Principal principal = request.getUserPrincipal();
	
	if (principal!=null) {
		String user = principal.getName();
		Game game = env.getService(GameFactory.class).createGame(Integer.valueOf(request.getParameter("players_number")));
		int gameId = game.getId();
%>
		<jsp:forward page="join_game.jsp">
			<jsp:param name="id" value="<%=gameId %>"/>
		</jsp:forward>
<%
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