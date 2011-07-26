<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Template Reset</title>
</head>
<body>
<form action="templatereset" method="POST">
<table>
<tr><td colspan="2"><% if(request.getAttribute("status")!=null)  response.getWriter().println(request.getAttribute("status")); %></tr>
<tr><td>User name: </td><td><input name="username" type="text"/></td></tr>
<tr><td>Password: </td><td><input name="password" type="password"/></td></tr>
</table>
<input type="submit" value="submit"/>
</form>
</body>
</html>