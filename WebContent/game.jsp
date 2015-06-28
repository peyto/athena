<%@page import="ru.kma8794.athena.engine.entity.UnitType"%>
<%@page import="java.security.Principal"%>
<%@page import="com.peyto.athena.engine.entity.Team"%>
<%@page import="com.peyto.athena.services.Environment"%>
<%@page import="com.peyto.athena.engine.entity.Unit"%>
<%@page import="com.peyto.athena.engine.entity.Game"%>
<%@page import="com.peyto.athena.services.impl.EnvironmentSingleton"%>
<%@page import="com.peyto.athena.engine.entity.Board"%>
<%@page import="com.peyto.athena.engine.math.*"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Strategy Game</title>
<script src="scripts/compartible.js" type="text/javascript"></script>
<script src="scripts/jquery-1.9.1.js" type="text/javascript"></script>
<script src="scripts/game-engine.js" type="text/javascript"></script>
<script src="scripts/dragscrollable.js" type="text/javascript"></script>
<link rel="STYLESHEET" href="css/basic.css" type="text/css" />
<link rel="STYLESHEET" href="css/game.css" type="text/css" />

<%
	Environment env = EnvironmentSingleton.getEnvironment();
	int id = Integer.valueOf(request.getParameter("id"));
	Game game = env.getUniqueObject(id);
	int zoom = 2;
	if (request.getParameter("zoom")!=null) {
		zoom = Integer.valueOf(request.getParameter("zoom"));
	}
	
	float scrollLeft = -1;
	float scrollTop = -1;
	int selectedX = -1;
	int selectedY = -1;
	if (request.getParameter("scrollLeft")!=null) {
		scrollLeft = Float.valueOf(request.getParameter("scrollLeft"));
	}
	if (request.getParameter("scrollTop")!=null) {
		scrollTop = Float.valueOf(request.getParameter("scrollTop"));
	}
	if (request.getParameter("selectedX")!=null) {
		selectedX = Integer.valueOf(request.getParameter("selectedX"));
	}
	if (request.getParameter("selectedY")!=null) {
		selectedY = Integer.valueOf(request.getParameter("selectedY"));
	}
	
	
	Board board = game.getBoard();
	
	Principal principal = request.getUserPrincipal();
	String user = (principal==null)?"guest":principal.getName();
%>
<style type="text/css">

.game_table div div.team<%=game.getTeams()[1].getId() %> img {
	-moz-transform: scaleX(-1);
	-o-transform: scaleX(-1);
	-webkit-transform: scaleX(-1);
	transform: scaleX(-1);
	filter: FlipH;
	-ms-filter: "FlipH";
}

.game_table div div.team<%=game.getTeams()[1].getId() %> b {
	position: absolute;
	right: auto;
	left: 10px;
}

<%
	int sizeX = game.getBoard().getSizeX();
%>
.game_table {
	width: <%=zoom==1? 30*sizeX+200 : 70*sizeX+200 %>px;
}

.game_table div div b {
	top: <%=zoom==1? 22 : 88 %>px;
	right: <%=zoom==1? 8 : 30 %>px;
}

.hex-row.even {
	margin-left: <%=zoom==1? 16 : 36 %>px;
}
.hex-row.first {
	margin-top: <%=zoom==1? 25 : 50 %>px;
}

.hexagon {
    width: <%=zoom==1? 30 : 70 %>px;
    height: <%=zoom==1? 60 : 140 %>px;
    margin: <%=zoom==1? -34 : -78 %>px 0 0 0px;
    }
</style>
</head>
<body>

	<table id="positioning_table" width="100%"><tr>
	<td class="left-field">
	
	<div id="board">
	<div class="game_table">
		<% for (int ycoord = 0; ycoord < board.getSizeY(); ycoord++) { %>
		<div class="hex-row <%=(ycoord % 2 == 1 )? "even": "" %> <%=(ycoord == 0 )? "first": "" %>" >
			<% for (int xcoord = 0; xcoord < board.getSizeX(); xcoord++) { 
				NormalizedHexagonCoordinates norm = HexUtils.convertToNormalized(xcoord, ycoord);
				Unit u = board.getUnit(norm.getX(), norm.getY());
				//String fieldClass = "grass";
				String fieldClass = board.getFieldTypeWithPermission(norm.getX(), norm.getY(), user);
				String tdClass=(u==null)?"":"unit team"+u.getTeam().getId();
			%>
			
			<div class="hexagon">
				<div class="game-cell hexagon-in1 x<%=norm.getX() %> y<%=norm.getY() %>">
					<input type="hidden" name="x" value="<%=norm.getX() %>" />
					<input type="hidden" name="y" value="<%=norm.getY() %>" />
					<div class="hexagon-in2 <%=fieldClass%> <%=tdClass%>">
						<div> <!-- Div for opacity selection -->
						<% if (u!=null) { %>
							<b><%= u.getCurrentUnits()%></b>
							<% if (zoom==2) {%> <img src="images/<%= u.getUnitType().getImg()%>_50.png"> <% } %>
							<input type="hidden" name="id" value="<%=u.getId() %>" />
						<% }%>
						</div>
					</div>
				</div>
			</div>
			<% } %>
		</div>
		<% } %>
	</div>
	</div>
	</td>
	<td style="vertical-align: top;" width="20%">
		<div class="menu">
			<div>
				You logged in as: <%=user %>
			</div>
		</div>
		
		<div class="turn_control">
			<div>
				Turn: <span id="turnLabel"></span>
			</div>
			<div>			
				<input id="endTurnButton" style="margin-bottom: 10px;" type="button" name="endTurn" value="End Turn" onclick="javascript: endTurn()"/>
			</div>
		</div>
		
		<div id="unit-info">
			<div class="unit-img-holder">
				<img border="1" src="images/empty_125.png">
			</div>
			<div class="cur_status">
				<div class="name"></div>
				<div class="number"></div>
				<table>
					<tr>
						<td width="22px;">
						<div class="can_attack">
							<img src="images/2swords_20.png" style="display: none">
						</div>
						</td>
						<td>
						<div class="morale">
							<img src="images/morale_20.png" >
							<div>
								<div class="meter nostripes">
									<span style="width: 0%"></span>
								</div>
							</div>
						</div>
						</td>
					</tr>
					<tr>
						<td>
						<div class="actions"></div>
						</td>
						<td>
							<div class="fatigue">
							<img src="images/attr_end_20.png" >
							</div>
						</td>
					</tr>
				</table>
				
				
			</div>
			<div class="unit-chars">
				<table width=100%>
					<tr>
						<td width="33%">
							<div><img border="0" src="images/attr_att_i_20.png">
								<div class="attr-att_i"></div>
							</div> 
						</td>
						<td width="33%">
							<div><img border="0" src="images/attr_att_c_20.png">
								<div class="attr-att_h"></div>
							</div>
						</td>
						<td width="33%">
							<div><img border="0" src="images/attr_bow_20.png">
								<div class="attr-bow"></div>
							</div>
						</td>
					</tr>
					<tr>
					<td>
						<div><img border="0" src="images/attr_sp_20.png">
								<div class="attr-sp"></div>
							</div> 
						</td>
						<td>
							<div><img border="0" src="images/attr_def_20.png">
								<div class="attr-def"></div>
							</div>
						</td>
						<td>
							<div><img border="0" src="images/attr_end_20.png">
								<div class="attr-end"></div>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</td>
	</tr>
	</table>
</body>

<script type="text/javascript">
var currentTeam = <%=game.getCurrentTeamTurn()!=null?game.getCurrentTeamTurn().getId():new Integer(-1) %>;
var selectedField = null;
var zoom = <%= zoom%>;
var zoomStep = <%= zoom==1 ? 30 : 80%>;

var haveRightToMove = false;

var unitTypeCollection = new UnitTypeCollection();
var teamsObject = {};

<% // Register UnitTypes 
for (UnitType e : UnitType.values()) { %>
unitTypeCollection.addUnitType('<%=e.toString()%>', <%=e.getSpeed()%>, <%=e.getAttackInfantry()%>, <%=e.getAttackHorse()%>, <%=e.getAttackBows()%>, <%=e.getDefenseArmor()%>, <%=e.getMoraleEndurance()%>);
<%}
%>

<%for (Team team : game.getTeams()) { %>
	teamsObject[<%=team.getId()%>] = new Team(<%=team.getId()%>);
<%	for (Unit unit : team.getUnits()) {%>
		teamsObject[<%=team.getId()%>].addUnit(<%=unit.getId()%>, '<%=unit.getUnitType().toString()%>', <%=unit.getCurrentUnits()%>,  <%=unit.getCurrentMorale()%>, <%=unit.getRangeMin()%>, <%=unit.getRangeMax()%>, '<%=unit.getImgSrc()%>', '<%=unit.getName()%>');
<%	}
}
%>

function setMoraleElement(morale) {
	var elem = $("#unit-info div.morale div.meter");
	elem.removeClass("green").removeClass("yellow").removeClass("orange").removeClass("red");
	if (morale==-1) {
		elem.children('span').width("0%");
		return;
	}
	
	if (morale<6) {
		elem.addClass("red");
	} else if (morale>=6 && morale<=10) {
		elem.addClass("orange");
	} else if (morale>10 && morale<=15) {
		elem.addClass("yellow");
	} else if (morale>15) {
		elem.addClass("green");
	}
	elem.children('span').width(Math.round(morale*5)+'%');
}

function displaySelectedUnit(jqtd) {
	if (jqtd==null) {
		// Remove unit info	
		$("#unit-info div.unit-img-holder img").attr("src","images/empty_125.png");
		$("#unit-info div.name").html(' ');
		$("#unit-info div.number").html(' ');
		$("#unit-info div.can_attack img").hide();
		$("#unit-info div.actions").html('');
		setMoraleElement(-1);
		
		$("#unit-info div.unit-chars div.attr-att_i").html('');
		$("#unit-info div.unit-chars div.attr-att_h").html('');
		$("#unit-info div.unit-chars div.attr-bow").html('');
		$("#unit-info div.unit-chars div.attr-sp").html('');
		$("#unit-info div.unit-chars div.attr-def").html('');
		$("#unit-info div.unit-chars div.attr-end").html('');
	} else {
		var unitId = jqtd.children('div.unit').find("div input[name='id']").val();
		var unitObj = teamsObject[currentTeam].getUnit(parseInt(unitId));
		var unitType = unitTypeCollection.getUnit(unitObj.unitType);
		
		var unitImage = "images/" + unitObj.imgSrc + "_125.png";
		
		$("#unit-info div.unit-img-holder img").attr("src",unitImage);
		$("#unit-info div.name").html('<b>'+unitObj.name+'</b>');
		$("#unit-info div.actions").html(Math.floor(unitObj.actions));
		if (unitObj.canAttack) {
			$("#unit-info div.can_attack img").show();
		} else {
			$("#unit-info div.can_attack img").hide();
		}
		setMoraleElement(unitObj.morale);
		
		
		$("#unit-info div.number").html(unitObj.units);
		
		$("#unit-info div.unit-chars div.attr-att_i").html(unitType.attackInf);
		$("#unit-info div.unit-chars div.attr-att_h").html(unitType.attackCav);
		$("#unit-info div.unit-chars div.attr-bow").html(unitType.attBow);
		$("#unit-info div.unit-chars div.attr-sp").html(unitType.speed);
		$("#unit-info div.unit-chars div.attr-def").html(unitType.defense);
		$("#unit-info div.unit-chars div.attr-end").html(unitType.endurance);
	}
}


function selectCurrentTurn(team, userWithTurn) {
	haveRightToMove = ('<%=user%>' == userWithTurn);
	// If the team changed, remove selection
	if (team!=currentTeam) {
		if (selectedField!=null) {
			selectField(selectedField);
		}
	}
	currentTeam = team;
	$('#turnLabel').html(userWithTurn);
	
	if (!haveRightToMove) {
		feedback();
		$('#endTurnButton').attr("disabled", "disabled");
	} else {
		$('#endTurnButton').removeAttr("disabled");
	}
}

function displayPossibleMoves(x0, y0, actions, rangeMin, rangeMax) {
	x0 = parseInt(x0);
	y0 = parseInt(y0);
	$('.game_table div.game-cell').removeClass("canMove");
	$('.game_table div.game-cell').removeClass("canAttack");
	$('.game_table div.game-cell').removeClass("shootMargin");
	$('.game_table div.game-cell').removeClass("canShoot");
	if (x0==-1 || y0==-1 || actions==-1) {
		return;
	}
	
	var actions = Math.floor(actions);
	
	/*
	for (|x-x0|<=n)
		for (|y-y0|<=n-|x-x0|})
			for (|z-z0|<=n-|x-x0|-|y-y0|)
	*/
	
	for (var x=x0-actions; x<= x0+actions; x++) {
		for (var y=y0-actions; y<= y0+actions; y++) {
			var curDelta = calculateHexDistance(x-x0, y-y0);
			if (curDelta <= actions) {
				var neededTd = $('.game_table div.game-cell.x'+x+'.y'+y);
				// Unless it's out of board
				if (neededTd.length !== 0 ) {
					// Conditions for displaying MOVE
					if (!neededTd.children('div').hasClass("unit")) {
						neededTd.addClass("canMove");
					};
				}
			}
		}
	}
	// ATTACK and SHOOT
	
	for (var x=x0-rangeMax; x<= x0+rangeMax; x++) {
		for (var y=y0-rangeMax; y<= y0+rangeMax; y++) {
			var curDelta = calculateHexDistance(x-x0, y-y0);
			var neededTd = $('.game_table div.game-cell.x'+x+'.y'+y);
			// Unless it's out of board
			if (neededTd.length !== 0 ) {
				// can ATTACK
				if (neededTd.children('div').hasClass("unit") && !neededTd.children('div').hasClass('team'+currentTeam)) {
					if (curDelta>=rangeMin && curDelta<=rangeMax) {
						if (curDelta>1) {
							neededTd.addClass("canShoot");
						} else {
							neededTd.addClass("canAttack");
						}
					}
				}
				else {
					if (rangeMin>1 && (curDelta==rangeMin || curDelta==rangeMax)) {
						neededTd.addClass("shootMargin");
					}
				}
			}
		}
	}
			 		
}

function displayPossibleMovesForSelected() {
	var id = selectedField.find("div input[name='id']").val();
	id = parseInt(id);
	var act = teamsObject[currentTeam].getUnit(id).actions;
	var rngMin = teamsObject[currentTeam].getUnit(id).rangeMin;
	var rngMax = teamsObject[currentTeam].getUnit(id).rangeMax;
	displayPossibleMoves(selectedField.find("input[name='x']").val(), selectedField.find("input[name='y']").val(), act, rngMin, rngMax);
}

function selectField(jqtd) {
	if (selectedField!=null && selectedField[0] == jqtd[0]) {
		selectedField.removeClass('selected');
		selectedField = null;
		displayPossibleMoves(-1, -1, -1, -1);
		displaySelectedUnit(null);
	} else {
		if (selectedField!=null) {
			selectedField.removeClass('selected');
		}
		jqtd.addClass('selected');	
		selectedField = jqtd;
		displayPossibleMovesForSelected();
		displaySelectedUnit(jqtd);
	}
} 

function displayUnitMove(from, to) {
	var fromJQ = from.children('div');
	var toJQ = to.children('div');
	
	fromJQ.appendTo(to);
	toJQ.appendTo(from);
	
	if (selectedField!=null && selectedField[0] == from[0]) {
		selectField(to);	
	}
	
}

function updateUnitOnBoard(teamId, unitId, curUnits) {
	var element = $('.game_table div.game-cell div.team'+teamId+" input[name='id'][value='"+unitId+"']").parent();
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
		var oldLocation = $('.game_table div.game-cell.x'+resp.old_x+'.y'+resp.old_y);
		var confirmedLocation = $('.game_table div.game-cell.x'+resp.new_x+'.y'+resp.new_y);
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
	
	var rangeMin = teamsObject[currentTeam].getUnit(parseInt(unitId)).rangeMin;
	var rangeMax = teamsObject[currentTeam].getUnit(parseInt(unitId)).rangeMax;
	var distance = calculateHexDistance(fromX-toX,fromY-toY);
	if (distance>=rangeMin && distance<=rangeMax) {
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

function applyNewZoom(zoom, mouseX, mouseY) {
	var left = $('#board').scrollLeft() + mouseX - 50;
	var top = $('#board').scrollTop() + mouseY - 50;
	var selectedX = selectedField!=null ? selectedField.find("input[name='x']").val() : -1;
	var selectedY = selectedField!=null ? selectedField.find("input[name='y']").val() : -1;
	//alert("New zoom, x="+left+" ("+(left/zoomStep)+"), y="+top+" ("+(top/zoomStep)+")");
	
	$('<form action="" method="GET"/>')
    .append($('<input type="hidden" name="zoom" value="' + zoom + '">'))
    .append($('<input type="hidden" name="id" value="<%=id%>">'))
    .append($('<input type="hidden" name="scrollLeft" value="' + left/zoomStep + '">'))
    .append($('<input type="hidden" name="scrollTop" value="' + top/zoomStep + '">'))
    .append($('<input type="hidden" name="selectedX" value="' + selectedX + '">'))
    .append($('<input type="hidden" name="selectedY" value="' + selectedY + '">'))
    .appendTo($(document.body)) //it has to be added somewhere into the <body>
    .submit();
}

function moveToInitial(left, top, selectedX, selectedY) {
	if (left!=-1 && top!=-1) {
		leftA = Math.floor(left*zoomStep) + 50 - ($('.left-field').width()/2);
		topA = Math.floor(top*zoomStep) + 20 - ($('.left-field').height()/2);
		//alert('left = '+left+', top='+top);
		//alert("New zoom, x="+leftA+" ("+(left)+"), y="+topA+" ("+(top)+")");
		$('#board').scrollLeft(leftA);
		$('#board').scrollTop(topA);
	}
	if (selectedX!=-1 && selectedY!=-1) {
		clickOnField($('.game_table div.game-cell.x'+selectedX+'.y'+selectedY));
	}
}

var mouseX = 0;
var mouseY = 0;
function storeMousePosition(event) {
	mouseX = event.pageX;
	mouseY = event.pageY;
}

$(document).ready(function () { 
	
	moveToInitial(<%=scrollLeft%>, <%=scrollTop%>, <%=selectedX%>, <%=selectedY%>);
	
	$('#board').dragscrollable({   
		dragSelector:'>:first',
		acceptPropagatedEvent: true,
        preventDefault: true,
        clickDisablerSelector:'div.game-cell'
	});
	
	$('.game_table div.game-cell').on('click', function() {
		clickOnField($( this ));
	});
	
	$(document).mousemove(function(event) {
		storeMousePosition(event);
	})
	
	$('#board').on( 'DOMMouseScroll mousewheel', function ( event ) {
		  if( event.originalEvent.detail > 0 || event.originalEvent.wheelDelta < 0 ) { //alternative options for wheelData: wheelDeltaX & wheelDeltaY
		    //scroll down
		    if (zoom == 2) {
		    	applyNewZoom(1, mouseX, mouseY);
		    }
		  } else {
		    //scroll up
		    if (zoom == 1) {
		    	applyNewZoom(2, mouseX, mouseY);
		    }
		  }
		  //prevent page fom scrolling
		  return false;
		});
	
	refreshGameData();
});

</script>
</html>