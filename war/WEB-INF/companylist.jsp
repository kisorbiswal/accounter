<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
        <meta content="IE=100" http-equiv="X-UA-Compatible">
		<link rel="shortcut icon" href="../images/favicon.ico" />
		<% String version = application.getInitParameter("version"); %>
      <link type="text/css" rel="stylesheet" href="../css/Finance.css?version=<%= version%>">
  </head>
  <body>
  <!--<table class="header">
	   <tr>
	      <td width="25%"><img src="/images/Accounter_logo_title.png" /></td>
	      <td width="50%"><div class="companyName">Company Lists</div></td>
	      <td width="25%">
	        <ul>
	           <li><img src="images/User.png" /><a href="">Welcome ${userName}</a></li>
	           <li><img src="images/Help.png" /><a href='http://help.accounter.com'>Help</a></li>
	           <li><img src="images/logout.png" /><a href='/do/logout'>Logout</a></li>
	        </ul>
	      </td>
	   </tr>
	</table>-->
	<div>
		<img src="../images/Accounter_logo_title.png" class="accounterLogo" />
	</div>
    <div class="company_lists">
       <ul>
       
        <c:if test="${message != null}">
       		<div>${message}</div>
        </c:if>
       
      	<div> <a href="/createcompany">Create New Company </a></div>
      	
	    <c:if test="${companeyList != null}">
		   <c:forEach var="company" items="${companeyList}">
			   <c:set var='url' value="/companies?companyId=${company.id}"/>
			   <h3><a href=${url}>${company.companyName}</a></h3>
		   </c:forEach>
	    </c:if>
	    
	   </ul>
    </div>
  </body>
</html>