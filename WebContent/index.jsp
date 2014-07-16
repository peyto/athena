<%@page import="java.security.Principal"%>
<%@page import="com.peyto.athena.services.Environment"%>
<%@page import="com.peyto.athena.services.impl.EnvironmentSingleton"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Strategy Game</title>
<script src="scripts/jquery-1.9.1.js" type="text/javascript"></script>
<script src="scripts/game-engine.js" type="text/javascript"></script>
<link rel="STYLESHEET" href="css/basic.css" type="text/css" />
<%
	Environment env = EnvironmentSingleton.getEnvironment();
	Principal principal = request.getUserPrincipal();
	String user = (principal==null)?"guest":principal.getName();
%>

</head>
<body>
Welcome to Peyto!
<br />
<br />
You logged in as: <b><%=user %></b> 
<% if (principal==null) { %>(<a href="protected/account.jsp">Login/Account</a>) <% } %>
<br />
<br />
<form action="join_game.jsp">
	Input game id to join: <input name="id" type="text" /><input type="submit" value="Load">
</form>
<br />
<br />
<form action="create_game.jsp">
... Or create a new one: <select name="players_number">
		<option value="2" selected="selected">2</option>
		<option value="3">3</option>
		<option value="4">4</option>
	</select>
	<input type="submit" value="Create">
</form>
</body>	
</html>