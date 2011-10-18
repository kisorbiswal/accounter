<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
  <title>Delete company| Accounter
  </title>
        <meta content="IE=100" http-equiv="X-UA-Compatible" />
		<link rel="shortcut icon" href="/images/favicon.ico" />
		
		<%@ include file="./feedback.jsp" %>
		<link type="text/css" href="../css/ss.css" rel="stylesheet" />
  </head>
  <body>
  <div id="commanContainer">
	<div>
		<img src="/images/Accounter_logo_title.png" class="accounterLogo" alt ="Accounter logo"/>
	</div>
    <div >
       
       <c:if test="${message != null}">
       		<div class="common-box create-company-message">${message}</div>
        </c:if>
        
	   <form action="/main/deletecompany" method="post">
	  	 	<c:if test="${canDeleteFromSingle}">
            	<input type="radio" name="delete" value="deleteUser" checked />
            		Delete company from this user
            </c:if>
           	<c:if test="${canDeleteFromAll}">
            <br />
            <input type="radio" name="delete" value="deleteAllUsers" />
            Delete company from all users
            </c:if>
            <br />
            <br />
            <div class="company_list_buttons">
	            <input type="submit" value="Delete" class="allviews-common-button" />
	            <input type="button" value="Cancel" class="allviews-common-button" onclick="parent.location='/main/companies'" />
            </div>
        </form>
	    
    </div>
   </div>
    <!-- Footer Section-->
   
   <div id="mainFooter"  >
   <div>
      <span>&copy 2011 Vimukti Technologies Pvt Ltd</span> |
      <a target="_blank" href="/site/termsandconditions"> Terms & Conditions </a> |
      <a target="_blank" href="/site/privacypolicy"> Privacy Policy </a> |
      <a target="_blank" href="/site/support"> Support </a>
   </div>
</div>
<%@ include file="./scripts.jsp" %>
  </body>
</html>