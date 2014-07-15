<%@page import="java.security.Principal"%>
<%@page import="com.kma8794.athena.engine.Player"%>
<%@page import="com.kma8794.athena.engine.entity.Game"%>
<%@page import="com.kma8794.athena.services.GameFactory"%>
<%@page import="com.kma8794.athena.services.Environment"%>
<%@page import="com.kma8794.athena.services.impl.EnvironmentSingleton"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Join Game</title>
<script src="scripts/jquery-1.9.1.js" type="text/javascript"></script>
<script src="scripts/game-engine.js" type="text/javascript"></script>
<link rel="STYLESHEET" href="css/basic.css" type="text/css" />
<%
	Environment env = EnvironmentSingleton.getEnvironment();

	Game game = env.getUniqueObject(Integer.valueOf(request.getParameter("id")));
	int gameId = game.getId();
	
	Principal principal = request.getUserPrincipal();
	String user = (principal!=null)?principal.getName():"";
%>
<style type="text/css">
.players_table {
	border: 1;
	border-collapse: collapse;
	margin-bottom: 20px;
}

.players_table td {
	padding: 5px 15px;
}

a.selected {
	color:red;
}
</style>

</head>
<body>
<h1>Game id = <b><%=gameId %></b></h1>

<table ><tr><td>
Please select your army!


<br />
<br />

<table class="players_table" border="1" style="border-collapse: collapse; ">
	<% for (int i=0; i<game.getPlayers(); i++) { 
		Player player = game.getPlayer(i);
		String name = (player!=null)?player.getLogin():"";
	%>
	<tr>
		<td>Player <%=i %></td>
		<% if (player!=null) {%>
			<td><%=name %></td>
		<% } else if (principal!=null) { %>
			<td><a id="select<%=i %>" href="javascript: selectArmy(<%=i%>, '<%=user%>')">Select</a></td>
		<% } else {%>
			<td></td>
		<% }%>
	</tr>
	<% } %>
	
</table>

</td>
<td style="vertical-align: top; padding-left: 25px;">or <a href="game.jsp?id=<%=gameId%>">view the game as visitor</a></td>
</tr></table>
<form id="action_form" action="join_game_2.jsp">
	<input type="hidden" name="id" value="<%=gameId%>">
	<input type="hidden" name="player_number" value="">
	<input type="hidden" name="player_name" value="">
	
	<input type="submit" value="Join game">
</form>

<br />
<a href="./">Back to home page</a>
</body>	
<script type="text/javascript">

var selected = null;

function deselectPrevious() {
	if (selected!=null) {
		$('#select'+selected).text('Select');
		$('#select'+selected).removeClass('selected');
	}	
}

function selectArmy(n, name) {
	deselectPrevious();
	$('#action_form input[name="player_number"]').val(n);
	$('#action_form input[name="player_name"]').val(name);
	
	selected = n;
	$('#select'+selected).text(name);
	$('#select'+selected).addClass('selected');
}
</script>
</html>