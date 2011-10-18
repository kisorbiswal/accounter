<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>Server Maintanace| Accounter
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible" />
<link rel="shortcut icon" href="/images/favicon.ico" />
<%@ include file="./feedback.jsp" %>
<link type="text/css" href="/css/ss.css" rel="stylesheet" />
</head>
<body>
    <div id="commanContainer">
		<img src="/images/Accounter_logo_title.png" class="accounterLogo" alt="Accounter logo" />
		<c:if test="${message != null}">
		   <div class="common-box create-company-message">${message}</div>
		</c:if>
		<form id = "maintananceForm" method="post" action="/main/maintanance">
			<div class="text_box_margin">
				<label>Please enter your admin password:</label>
				<input type = "password"  id = "adminPassword"  name ="password" />
			</div>
			<c:choose>
			    <c:when test='${CheckedValue == "true"}'>
			       <input type="checkbox" name="option1" value="CheckedValue" checked="" /><label>Server under maintainace</label> <br>
			    </c:when>
			    <c:otherwise>
			       <input type="checkbox" name="option1" /><label>Server under maintainace</label> <br />
			    </c:otherwise>
		    </c:choose>
			<div class="OkButton">
			    <input type = "submit" class="allviews-common-button" value = "Submit " id = "submitButton"  />
			</div>
		</form>
	</div>
	
	<%@ include file="./scripts.jsp" %>
</body>
</html>