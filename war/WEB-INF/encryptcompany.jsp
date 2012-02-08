<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="i18n" uri="/WEB-INF/i18n.tld"%>
<%@page pageEncoding="UTF-8" %>
<html>
<head>
<title>Accounter | Subscription </title>
<link type="text/css" href="../../css/ss.css" rel="stylesheet" />
<%
String info =(String) request.getAttribute("info");
%>
</head>
<body>
<table id="commanContainer">
<tr>
<td>
<c:if test="${info!=null}"> 
<div id="login_success" class="common-box">
	<span>${info}</span>
</div>
</c:if>
<div class="form-box">
<form id="encrpt_form" action="/main/encryption" method="post">
<table>
<tr><td><input type="hidden" name="on0" value="Companies list">Subscription options</td></tr><tr><td>
<c:if test="${companeyList != null}">
<select name="companyname">
<c:forEach var="company" items="${companeyList}">
<option value="${company}">${company}</option> 
</c:forEach> 
</c:if>

</table>Enter password
<input type="password" name="pass1"  >Confirm password
<input type="password" name="pass2"  >
<input type="submit" name="submit" value="submit">
<div>
</form>
</div>
</td>
</tr>
</table>
</body>
</html>