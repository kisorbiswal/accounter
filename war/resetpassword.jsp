<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head><title>Accounter</title></head>
  <body>
    <div>
        <form action="/activation" method="post">
        	<% String message=(String)req.getAttribute("message") %>
        	<input type="text" name="error" value=<%= message %>/>
        	
        	<% String emailId=(String)req.getAttribute("emailId") %>
        	<input type="text" name="emailId" value=<%= emailId %>/>
        	
         	<input type="text" name="password" />
			<input type="text" name="confirm" />
			<input type="submit" value="Reset and Continue" />
        </form>
    </div>
  </body>
</html>