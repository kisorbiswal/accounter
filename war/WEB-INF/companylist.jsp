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
    <div class="company_lists">
       
       
        <c:if test="${message != null}">
       		<div class="common-box">${message}</div>
        </c:if>
       <div class="form-box">
      	<div> <a href="/createcompany">Create New Company </a></div>
      	<ul>
	    <c:if test="${companeyList != null}">
		   <c:forEach var="company" items="${companeyList}">
			   <c:set var='url' value="/companies?companyId=${company.id}"/>
			   <li><a href=${url}>${company.companyName}</a></li>
		   </c:forEach>
	    </c:if>
	    
	   </ul>
	  </div>
    </div>
   </div>
  </body>
</html>