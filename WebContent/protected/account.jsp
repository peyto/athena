<%@page import="java.security.Principal"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<title>Login Page</title>
</head>
<%
	Principal principal = request.getUserPrincipal();
	String user = (principal==null)?"guest":principal.getName();
%>
<h1>Success!</h1>
<br/>
<h3>You logged in as: <%=user %></h3>
<br/>
<br/>
<a href="../">Back to home page</a>

</body>
</html>