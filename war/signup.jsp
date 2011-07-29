<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head><title>Accounter</title></head>
  <body>
    <div>
        <form action="/signup" method="post">
        	<% String message=(String)req.getAttribute("message") %>
        	<input type="text" name="error" value=<%= message %>/>
         	<input type="text" name="emailId" />
			<input type="text" name="firstName" />
			<input type="text" name="lastName" />
			<input type="submit" value="SignUp" />
        </form>
    </div>
  </body>
</html>