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
String paypalButtonId =(String) request.getAttribute("paypalButtonId");
Boolean isSandBoxPaypal = (Boolean) request.getAttribute("isSandBoxPaypal");
String emailId = (String) request.getAttribute("emailId");
 if(emailId ==null){
	 emailId="";
 }
if(isSandBoxPaypal ==null){
	isSandBoxPaypal= true;
}
%>
 
</head>
<body>
<table id="commanContainer">
<tr>
<td>
 <img src="/images/Accounter_logo_title.png" class="accounterLogo" alt = "accounter logo"/>
<c:if test="${info!=null}"> 
<div id="login_success" class="common-box">
	<span>${info}</span>
</div>
</c:if>
<div class="form-box">


<% if(isSandBoxPaypal){ %> 
<form action="https://www.sandbox.paypal.com/cgi-bin/webscr" method="post">
<input type="hidden" name="hosted_button_id" value="<%= paypalButtonId%>">
<%}else{%>
<form action="https://www.paypal.com/cgi-bin/webscr" method="post">
<input type="hidden" name="hosted_button_id" value="<%= paypalButtonId%>">
<%}%>
<input type="hidden" name="cmd" value="_s-xclick">
<input type="hidden" name="custom"  value = "<%= emailId %>"   >
<table>
<tr><td><input type="hidden" name="on0" value="Subscription options">Subscription options</td></tr><tr><td><select name="os0">
	<option value="One user monthly">One user monthly : $5.00USD - monthly</option>
	<option value="One user yearly">One user yearly : $50.00USD - yearly</option>
	<option value="2 users monthly">2 users monthly : $10.00USD - monthly</option>
	<option value="2 users yearly">2 users yearly : $100.00USD - yearly</option>
	<option value="5 users monthly">5 users monthly : $25.00USD - monthly</option>
	<option value="5 users yearly">5 users yearly : $250.00USD - yearly</option>
	<option value="Unlimited Users monthly">Unlimited Users monthly : $100.00USD - monthly</option>
	<option value="Unlimited Users yearly">Unlimited Users yearly : $1,000.00USD - yearly</option>
</select> </td></tr>
</table>
<input type="hidden" name="currency_code" value="USD">
<input type="image" src="https://www.sandbox.paypal.com/en_US/i/btn/btn_subscribeCC_LG.gif" border="0" name="submit" alt="PayPal - The safer, easier way to pay online!">
<img alt="" border="0" src="https://www.sandbox.paypal.com/en_US/i/scr/pixel.gif" width="1" height="1">
</form>




</div>
</td>
</tr>
</table>
</body>
</html>