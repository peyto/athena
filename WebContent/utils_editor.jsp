<%@page import="java.security.Principal"%>
<%@page import="ru.kma8794.athena.engine.math.NormalizedHexagonCoordinates"%>
<%@page import="ru.kma8794.athena.engine.math.HexUtils"%>
<%@page import="ru.kma8794.athena.engine.entity.FieldMap"%>
<%@page import="ru.kma8794.athena.services.impl.EnvironmentSingleton"%>
<%@page import="ru.kma8794.athena.services.Environment"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Map Editor</title>

<script src="scripts/compartible.js" type="text/javascript"></script>
<script src="scripts/jquery-1.9.1.js" type="text/javascript"></script>
<script src="scripts/dragscrollable.js" type="text/javascript"></script>
<link rel="STYLESHEET" href="css/basic.css" type="text/css" />
<link rel="STYLESHEET" href="css/editor-hex.css" type="text/css" />

<%
	String savedFilename = request.getParameter("filename");
	boolean loadSaved = false;
	FieldMap map = null;
	if (savedFilename!=null && !savedFilename.isEmpty()) {
		loadSaved = true;
		map = FieldMap.deSerialize(savedFilename);
	} else {
		// Create new Field
		int xsize = Integer.valueOf(request.getParameter("xsize"));
		int ysize = Integer.valueOf(request.getParameter("ysize"));
		// Create empty FieldMap
		map = FieldMap.getEmptyFieldMap(xsize, ysize);
	}

	
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
	
	Principal principal = request.getUserPrincipal();
	String user = (principal==null)?"guest":principal.getName();
%>
<style type="text/css">

<%
	int sizeX = map.getSizeX();
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
<form action="utils_editor_save.jsp" method="post">
	<input type="hidden" name="sizeX" value="<%=map.getSizeX() %>" />
	<input type="hidden" name="sizeY" value="<%=map.getSizeY() %>" />

	<table id="positioning_table" width="100%"><tr>
	<td class="left-field">
	
	<div id="board">
	<div class="game_table">
		<% for (int ycoord = 0; ycoord < map.getSizeY(); ycoord++) { %>
		<div class="hex-row <%=(ycoord % 2 == 1 )? "even": "" %> <%=(ycoord == 0 )? "first": "" %>" >
			<% for (int xcoord = 0; xcoord < map.getSizeX(); xcoord++) { 
				NormalizedHexagonCoordinates norm = HexUtils.convertToNormalized(xcoord, ycoord);
				//String fieldClass = "grass";
				String fieldClass = map.getFieldType(norm.getX(), norm.getY()).name();
			%>
			
			<div class="hexagon">
				<div class="game-cell hexagon-in1 x<%=norm.getX() %> y<%=norm.getY() %>">
					<input type="hidden" name="x" value="<%=norm.getX() %>" />
					<input type="hidden" name="y" value="<%=norm.getY() %>" />
					<input type="hidden" name="fieldtype" value="<%=fieldClass %>" />
					<div class="hexagon-in2 <%=fieldClass%> ">
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
		<div>
			<input type="radio" name="field" value="grass" checked>Grass<br>
			<input type="radio" name="field" value="forest">Forest<br>
			<input type="radio" name="field" value="water">Water<br>
			<input type="radio" name="field" value="swamp">Swamp<br>
			<input type="radio" name="field" value="road">Road<br>
		</div>
		<div style="margin-top: 10px;">
			
				<% String disabledStr = "disabled=\"disabled\""; %>
				<input type="text" name="filename" value="<%=loadSaved?savedFilename:"test.map" %>" <%= loadSaved?disabledStr:"" %>><br>
				<input type="submit" onclick="javascript: doSaveMap(filename);"  value="Save">
			
		</div>
		
		<div style="margin-top: 10px;">
			<input type="text" name="filenameSaveAs" value=""><br>
			<input type="submit" onclick="javascript: doSaveMap(filenameSaveAs);" value="Save as">
		</div>
		
	</td>
	</tr>
	</table>
</form>

</body>

<script type="text/javascript">
var selectedField = null;
var zoom = <%= zoom%>;
var zoomStep = <%= zoom==1 ? 30 : 80%>;

function doSaveMap(filenameObj) {
	var fileStr = filenameObj.value;
	$("input[name='filename']").val(fileStr);
	$("input[name='filename']").prop( "disabled", false );
}

function getFieldMapToApply() {
	var selected = $("input[type='radio'][name='field']:checked");
	if (selected.length > 0) {
	    selectedVal = selected.val();
	}
	return selectedVal;
}

function clickOnField(jqtd) {
	/*Plan:
		1. Get type of fieldMap selected
		2. Get field (div.hexagon-in2)
		3. Apply
	 */
	var fieldToApply = jqtd.children("div.hexagon-in2");
	
	fieldToApply.removeClass("grass");
	fieldToApply.removeClass("forest");
	fieldToApply.removeClass("water");
	fieldToApply.removeClass("swamp");
	fieldToApply.removeClass("road");
	
	var value = getFieldMapToApply();
	jqtd.children("input[name='fieldtype']").val(value);
	
	fieldToApply.addClass(value);	
}

/* We need the map to be stored on the server to apply new zoom */
function applyNewZoom(zoom, mouseX, mouseY) {
	<% if (!loadSaved) { %>
		return;
	<% } %>
	
	if (!confirm("Unsaved data will be lost, SAVE first! Do you want to continue?")) {
		return;
	}
	var left = $('#board').scrollLeft() + mouseX - 50;
	var top = $('#board').scrollTop() + mouseY - 50;
	var selectedX = selectedField!=null ? selectedField.find("input[name='x']").val() : -1;
	var selectedY = selectedField!=null ? selectedField.find("input[name='y']").val() : -1;
	//alert("New zoom, x="+left+" ("+(left/zoomStep)+"), y="+top+" ("+(top/zoomStep)+")");
	
	$('<form action="" method="GET"/>')
	.append($('<input type="hidden" name="filename" value="<%=savedFilename%>">')) 
	.append($('<input type="hidden" name="zoom" value="' + zoom + '">'))
    .append($('<input type="hidden" name="zoom" value="' + zoom + '">'))
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
	});
	
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
	
});

</script>
</html>