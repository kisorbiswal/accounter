<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<title> Cancel My Account
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible">

<link rel="shortcut icon" href="/images/favicon.ico" />

<%@ include file="./feedback.jsp" %>
<link type="text/css" href="../css/ss.css" rel="stylesheet">
</head>
<body>
  <div id="commanContainer" style="width:400px">
   <div class="maintanance_subcont">
    <img src="/images/Accounter_logo_title.png" class="accounterLogo" />
    <form id="cancelform" name="cancelform" method="post" action="/main/cancelform">
		<div>
			<ul>
				<li>
	    		<c:if test="${companeyList != null}">
		   			<c:forEach var="company" items="${companeyList}">
		   				<c:set var='ids' value="companeyList[${company}].id"/>
			   			<c:set var='url' value="/main/companies?companyId=${company.id}"/>
			   				<div class="companies-list"><a href= '${url}'>${company.preferences.tradingName}</a></div>
		   			</c:forEach>
	    		</c:if>
	    		</li>
	   		</ul>
		</div>	
	  	 <h3>You are about to cancel your Accounter account. Once you cancel, your data will be deleted immediately.</h3>
	    <div>
	   	    <div style="float:left">
	      		<input type="button" class="allviews-common-button" value="Keep Using Accounter" onclick="parent.location='/main/companies'"/>
	   		</div>
	   		<div style="float:right">
	      		<input type="submit" class="allviews-common-button" value="Cancel my Account" />
	   		</div>
	   	</div>
	  </form>
   </div>
  </div>
  <%@ include file="./scripts.jsp" %>
</body>
</html>