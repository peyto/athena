<%@page import="java.security.Principal"%>
<%@page import="com.peyto.athena.engine.math.NormalizedHexagonCoordinates"%>
<%@page import="com.peyto.athena.engine.math.HexUtils"%>
<%@page import="com.peyto.athena.engine.entity.FieldMap"%>
<%@page import="com.peyto.athena.services.impl.EnvironmentSingleton"%>
<%@page import="com.peyto.athena.services.Environment"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	String fileName = request.getParameter("filename");
	
	FieldMap map = FieldMap.saveMapFromUtilsEditor(request);
	// Parse all params
	FieldMap.serialize(map, fileName);
	
	String redirectURL = "utils_editor.jsp?filename="+fileName;
	response.sendRedirect(redirectURL);
%>



</html>