<%@page import="java.security.Principal"%>
<%@page import="com.kma8794.athena.engine.entity.Team"%>
<%@page import="com.kma8794.athena.services.Environment"%>
<%@page import="com.kma8794.athena.engine.entity.Unit"%>
<%@page import="com.kma8794.athena.engine.entity.Game"%>
<%@page import="com.kma8794.athena.services.impl.EnvironmentSingleton"%>
<%@page import="com.kma8794.athena.engine.entity.Board"%>
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
<script src="scripts/dragscrollable.js" type="text/javascript"></script>
<link rel="STYLESHEET" href="css/basic.css" type="text/css" />
<link rel="STYLESHEET" href="css/game.css" type="text/css" />

<%
	Environment env = EnvironmentSingleton.getEnvironment();
	Game game = env.getUniqueObject(Integer.valueOf(request.getParameter("id")));
	
	Board board = game.getBoard();
	
	Principal principal = request.getUserPrincipal();
	String user = (principal==null)?"guest":principal.getName();
%>
<style type="text/css">
html, body, #positioning_table {
	height: 99%;
}

#positioning_table {
 table-layout: fixed;
}

#board {
	width: 100%;
	height: 100%;
	min-height: 100%;
	overflow: auto;
}

.game_table {
	border-collapse: collapse;
	width: 100%;
	height: 100%;
	overflow: scroll;
}

.game_table td {
	width: 40px;
	height: 40px;
	border: 1px solid black;
	padding: 0px;
	cursor: pointer;
	font-size: 10px;
	text-align: center;
	vertical-align: bottom;
}

.game_table td div.team1 {
	background-color: #8888FF;
}

.game_table td div.team2 {
	background-color: #FF8888;
}

.game_table td.selected div{
	background-color: orange;
}

.game_table td.canMove {
	background-color: blue;
}

.game_table td.canAttack {
	cursor: url("images/attack.gif"), auto;
}

.game_table td div {
	position: relative;
	height: 40px;
	width: 40px;
}



.game_table td div img {
	position: absolute;
	left: 0px;
	top: 0px;
	z-index: 5;
}

.game_table td div.team<%=game.getTeams()[1].getId() %> img {
	-moz-transform: scaleX(-1);
	-o-transform: scaleX(-1);
	-webkit-transform: scaleX(-1);
	transform: scaleX(-1);
	filter: FlipH;
	-ms-filter: "FlipH";
}

.game_table td div b {
	position: absolute;
	right: 9px;
	bottom: 0px;
	z-index: 10;
	color: white;
}

.game_table td div.team<%=game.getTeams()[1].getId() %> b {
	position: absolute;
	right: auto;
	left: 10px;
}
</style>
</head>
<body>

	<table id="positioning_table" width="100%"><tr>
	<td width="80%">
	
	<div id="board">
	<table class="game_table">
		<% for (int ycoord = 0; ycoord < board.getSize(); ycoord++) { %>
		<tr>
			<% for (int xcoord = 0; xcoord < board.getSize(); xcoord++) { 
				Unit u = board.getUnit(xcoord, ycoord);
				String tdClass=(u==null)?"":"unit team"+u.getTeam().getId();
			%>
			<td class="x<%=xcoord %> y<%=ycoord %>">
				<input type="hidden" name="x" value="<%=xcoord %>" />
				<input type="hidden" name="y" value="<%=ycoord %>" />
				<div class="<%=tdClass%>">
					<% if (u!=null) { %>
						<b><%= u.getCurrentUnits()%></b>
						<img src="<%= u.getUnitType().getImg()%>">
						<input type="hidden" name="id" value="<%=u.getId() %>" />
					<% }%>
				</div>
			</td>
			<% } %>
		</tr>
		<% } %>
	</table>
	</div>
	</td>
	<td style="vertical-align: top;" width="20%">
		<div>
			You logged in as: <%=user %>
		</div>
		<div>
			Turn: <span id="turnLabel"></span>
		</div>
		<div>			
			<input id="endTurnButton" style="margin-bottom: 10px;" type="button" name="endTurn" value="End Turn" onclick="javascript: endTurn()"/>
		</div>
		
	</td>
	</tr>
	</table>
</body>

<script type="text/javascript">
var boardSize = <%=game.getBoard().getSize() %>;
var currentTeam = <%=game.getCurrentTeamTurn()!=null?game.getCurrentTeamTurn().getId():new Integer(-1) %>;
var selectedField = null;

var haveRightToMove = false;

var teamsObject = {};

<%
for (Team team : game.getTeams()) {
%>
	teamsObject[<%=team.getId()%>] = new Team(<%=team.getId()%>);
<%
	for (Unit unit : team.getUnits()) {
%>
		teamsObject[<%=team.getId()%>].addUnit(<%=unit.getId()%>, <%=unit.getRange()%>);
<%		
	}
}
%>

function selectCurrentTurn(team, userWithTurn) {
	if (selectedField!=null) {
		selectField(selectedField);
	}
	haveRightToMove = ('<%=user%>' == userWithTurn);
	currentTeam = team;
	$('#turnLabel').html(userWithTurn);
	
	if (!haveRightToMove) {
		feedback();
		$('#endTurnButton').attr("disabled", "disabled");
	} else {
		$('#endTurnButton').removeAttr("disabled");
	}
}

function displayPossibleMoves(x, y, actions, range) {
	x = parseInt(x);
	y = parseInt(y);
	$('.game_table td').removeClass("canMove");
	$('.game_table td').removeClass("canAttack");
	if (x==-1 || y==-1 || actions==-1) {
		return;
	}
	var max = Math.floor(actions);
	for (var i=Math.max(0, x-max); i<= Math.min(boardSize-1, x+max); i++ ) {
		for (var j=Math.max(0, y-max); j<= Math.min(boardSize-1, y+max); j++ ) {
			var dist = Math.sqrt((i-x)*(i-x)+(j-y)*(j-y));
			if (dist<=actions) {
				// find td
				var neededTd = $('.game_table td.x'+i+'.y'+j);
				if (!neededTd.children('div').hasClass("unit")) {
					neededTd.addClass("canMove");
				};
			};
		};
	};
	var range = Math.floor(range);
	for (var i=Math.max(0, x-range); i<= Math.min(boardSize-1, x+range); i++ ) {
		for (var j=Math.max(0, y-range); j<= Math.min(boardSize-1, y+range); j++ ) {
			var neededTd = $('.game_table td.x'+i+'.y'+j);
			if (neededTd.children('div').hasClass("unit") && !neededTd.children('div').hasClass('team'+currentTeam)) {
				var dist = Math.sqrt((i-x)*(i-x)+(j-y)*(j-y));
				if (dist<=range) {
					neededTd.addClass("canAttack");
				}
			}
		}
			 		
	}
}

function displayPossibleMovesForSelected() {
	var id = selectedField.find("div input[name='id']").val();
	id = parseInt(id);
	var act = teamsObject[currentTeam].getUnit(id).actions;
	var rng = teamsObject[currentTeam].getUnit(id).range;
	displayPossibleMoves(selectedField.find("input[name='x']").val(), selectedField.find("input[name='y']").val(), act, rng);
}

function selectField(jqtd) {
	if (selectedField!=null && selectedField[0] == jqtd[0]) {
		selectedField.removeClass('selected');
		selectedField = null;
		displayPossibleMoves(-1, -1, -1, -1);
	} else {
		if (selectedField!=null) {
			selectedField.removeClass('selected');
		}
		jqtd.addClass('selected');	
		selectedField = jqtd;
		displayPossibleMovesForSelected();
	}
} 

function displayUnitMove(from, to) {
	var fromJQ = from.children('div');
	var toJQ = to.children('div');
	
	fromJQ.appendTo(to);
	toJQ.appendTo(from);
	/*
	to.children('div').attr('class', from.children('div').attr('class'));
	from.children('div').attr('class', '');
	
	to.children("div").html(from.children("div").html());
	from.children("div").html("");
	*/
	
	if (selectedField!=null && selectedField[0] == from[0]) {
		selectField(to);	
	}
	
}

function updateUnitOnBoard(teamId, unitId, curUnits) {
	var element = $('.game_table td div.team'+teamId+" input[name='id'][value='"+unitId+"']").parent();
	if (curUnits>0) {
		element.find('b').html(''+curUnits);
	} else {
		// delete from board
		element.remove();
	}
}

function endTurnHandler(resp) {
    if (resp.result=='ok') {
    	for (var i=0; i < resp.units.length; i++) {
    		var cur = resp.units[i];
    		var jsUnit = teamsObject[resp.new_team].getUnit(cur.id);
    		jsUnit.update(cur.currentUnits, cur.currentMorale, cur.curActions, cur.canAttack);
        }
    	selectCurrentTurn(resp.new_team, resp.new_player);
    } else {
    	alert('Some internal error occured on the server '+data);
    }
} 

function unitAttackHandler(resp) {
	if (resp.result=="ok") {
		for (var i=0; i < resp.unit_updates.length; i++) {
			var u = resp.unit_updates[i];
			var jsUnit = teamsObject[u.teamId].getUnit(u.id);
			jsUnit.update(u.currentUnits, u.currentMorale, u.curActions, u.canAttack);
			updateUnitOnBoard(u.teamId, u.id, u.currentUnits);
			if (u.currentUnits<=0) {
				teamsObject[u.teamId].removeUnit(u.id);
				displayPossibleMovesForSelected();
			}
		}
	}
} 

function unitMoveHandler(resp) {
	if (resp.result=="ok") {
		var jsUnit = teamsObject[currentTeam].getUnit(resp.unit_id);
		jsUnit.updateActions(resp.actions_left);
		var oldLocation = $('.game_table td.x'+resp.old_x+'.y'+resp.old_y);
		var confirmedLocation = $('.game_table td.x'+resp.new_x+'.y'+resp.new_y);
		displayUnitMove(oldLocation, confirmedLocation);
	}
	
} 

function unitAttack(from, where) {
	var toX = where.find("input[name='x']").val();
	var toY = where.find("input[name='y']").val();
	var fromX = selectedField.find("input[name='x']").val();
	var fromY = selectedField.find("input[name='y']").val();
	
	
	var unitId = from.find("div input[name='id']").val();
	// TODO for now. We'll use getRange from JS later to get range for archers
	
	var range = teamsObject[currentTeam].getUnit(parseInt(unitId)).range;
	if (Math.sqrt((fromX-toX)*(fromX-toX) + (fromY-toY)*(fromY-toY))<=range) {
		if (!confirm("Atack?")) return;
		$.ajax({
			url: "gameAction",
			type: "GET",
			data: { 
				'action' : 'attack',
				'id':'<%=game.getId()%>', 
				'unit':unitId,
				'toX':toX,
				'toY':toY
			},
			//dataType: "json",
			success: function (data, textStatus) {
				var resp = JSON.parse(data);
				unitAttackHandler(resp);
			}
		});
	}
}

function unitMove(from, to) {
	var unitId = from.find("div input[name='id']").val();
	var toX = to.find("input[name='x']").val();
	var toY = to.find("input[name='y']").val();
	
	$.ajax({
		url: "gameAction",
		type: "GET",
		data: { 
			'action' : 'move',
			'id':'<%=game.getId()%>', 
			'unit':unitId,
			'toX':toX,
			'toY':toY
		},
		//dataType: "json",
		success: function (data, textStatus) {
			var resp = JSON.parse(data);
			unitMoveHandler(resp);
		}
		});
	
}


function endTurn() {
	if (haveRightToMove) {
	if (!confirm('End turn?')) {
		return;
	} 
	$.ajax({
		url: "gameAction",
		type: "GET",
		data: { 
			'action' : 'end_turn',
			'id':'<%=game.getId()%>', 
			'cur_team':currentTeam
		},
		//dataType: "json",
		success: function (data, textStatus) {
			var resp = JSON.parse(data);
			endTurnHandler(resp);
		}
		});
	
	} else {
		alert("It's not your turn!");
	}
	
}

function refreshGameData() {
	$.ajax({
		url: "gameAction",
		type: "GET",
		data: { 
			'action' : 'refresh',
			'id':'<%=game.getId()%>', 
		},
		//dataType: "json",
		success: function (data, textStatus) {
			var resp = JSON.parse(data);
			if (resp.started) {
		        for (var i=0; i < resp.units.length; i++) {
		        	var cur = resp.units[i];
		        	var jsUnit = teamsObject[cur.teamId].getUnit(cur.id);
		        	jsUnit.update(cur.currentUnits, cur.currentMorale, cur.curActions, cur.canAttack);
		        }
		        selectCurrentTurn(resp.current_team, resp.current_player);
			} else {
				alert('Please wait until all players will join the game');
				feedback();
			}
	    } 
		});
}

function feedback() {
	$.ajax({
		url: "gameAction",
		type: "GET",
		data: { 
			'action' : 'feedback',
			'id':'<%=game.getId()%>', 
		},
		//dataType: "json",
		success: function (data, textStatus) {
			var resp = JSON.parse(data);
			if (resp.type=='start') {
				alert('The game have started!');
				refreshGameData();
			} else if (resp.type=='end_turn') {
				endTurnHandler(resp.event);
			} else if (resp.type=='move_result') {
				unitMoveHandler(resp.event);
				feedback();
			} else if (resp.type=='attack_result') {
				unitAttackHandler(resp.event);
				feedback();
			} else {
				alert('Unknown result ('+resp.type+')');
				feedback();
			} 
	    } 
		});
}



function makeUnitAction(where) {
	if (where.children('div').hasClass("unit") && !where.children('div').hasClass('team'+currentTeam)) {
		unitAttack(selectedField, where);
	} else {
		unitMove(selectedField, where);
	}
}

function clickOnField(jqtd) {
	if (jqtd.children('div').hasClass('team'+currentTeam)) {
		selectField(jqtd);
	} else {
		if (haveRightToMove) {
			if (selectedField!=null) {
				makeUnitAction(jqtd);
			}
		}
	}
}

$(document).ready(function () { 
	$('.game_table td').click(function() {
		clickOnField($( this ));
	});
	
	$('#board').dragscrollable();
	refreshGameData();
});

</script>
</html>