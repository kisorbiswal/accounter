<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="i18n" uri="/WEB-INF/i18n.tld"%>
<%@page pageEncoding="UTF-8" %>
<html>
<head>
<title>Accounter | Subscription </title>
<link type="text/css" href="../css/ss.css" rel="stylesheet" />
</head>
<body>
<table id="commanContainer">
<tr>
<td>
<div class="form-box">
<form id="subscription_form" action=" https://www.sandbox.paypal.com/cgi-bin/webscr " method="post">
<input type="hidden" name="cmd" value="_s-xclick">
<input type="hidden" name="hosted_button_id" value="WU3NJFQJCM25G">
<table>
<tr><td><input type="hidden" name="on0" value="Subscription options">Subscription options</td></tr><tr><td><select name="os0">
    <option value="One user">One user : $5.00USD - monthly</option>
    <option value="One user">One user : $50.00USD - yearly</option>
    <option value="2 users">2 users : $10.00USD - monthly</option>
    <option value="2 users">2 users : $100.00USD - yearly</option>
    <option value="5 users">5 users : $25.00USD - monthly</option>
    <option value="5 users">5 users : $250.00USD - yearly</option>
    <option value="Unlimited Users">Unlimited Users : $100.00USD - monthly</option>
    <option value="Unlimited Users">Unlimited Users : $1,000.00USD - yearly</option>
</select> </td></tr>
</table>
<input type="hidden" name="currency_code" value="USD">
<div id="subscribe_button">
<input type="image" src="https://www.paypalobjects.com/en_GB/i/btn/btn_subscribeCC_LG.gif" border="0" name="submit" alt="PayPal — The safer, easier way to pay online.">
<img alt="" border="0" src="https://www.paypalobjects.com/en_GB/i/scr/pixel.gif" width="1" height="1">
<div>
</form>
</div>
</td>
</tr>
</table>
</body>
</html>