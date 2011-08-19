<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
        <meta content="IE=100" http-equiv="X-UA-Compatible">
		<link rel="shortcut icon" href="../images/favicon.ico" />
		<% String version = application.getInitParameter("version"); %>
      <link type="text/css" rel="stylesheet" href="../css/ss.css?version=<%= version%>">
  </head>
  <body>
  <div id="commanContainer">
	<div>
		<img src="../images/Accounter_logo_title.png" class="accounterLogo" />
	</div>
    <div >
       
       <c:if test="${message != null}">
       		<div class="common-box create-company-message">${message}</div>
        </c:if>
        
	   <form action="/deletecompany" method="post">
             <input type="radio" name="delete" value="deleteUser" checked>
             	Delete company from this user
            <br>
            <input type="radio" name="delete" value="deleteAllUsers">
            Delete company from all users
            <br>
            <br>
            <div>
            <input type="submit" value="Delete">
            <input type="button" value="Cancel" onclick="parent.location='/companies'">
            </div>
        </FORM>
	    
    </div>
   </div>
   
  </body>
</html>