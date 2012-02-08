<%@ page import="java.util.*" %>
<%@ page import="com.vimukti.accounter.core.*" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="i18n" uri="/WEB-INF/i18n.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!-- The HTML 4.01 Transitional DOCTYPE declaration-->
<!-- above set at the top of the file will set     -->
<!-- the browser's rendering engine into           -->
<!-- "Quirks Mode". Replacing this declaration     -->
<!-- with a "Standards Mode" doctype is supported, -->
<!-- but may lead to some differences in layout.   -->

<html>

  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="/jscripts/jquery-1.7.min.js" type="text/javascript"></script>
	<script src="/jscripts/jquery.validate.js" type="text/javascript"></script>
	<link type="text/css" href="../css/ss.css" rel="stylesheet" />
    <%
  		SubscriptionManagementData managementData=(SubscriptionManagementData)request.getAttribute("managementData");
  	%>
  	<title>Subscription Management</title>
  </head>
  <body>
  <div id="subscriptionContainer">
	<form id="subscription_complition_form" method="post"  class="form-box">
	<table cellspacing="10">
	<tr>
	<td> Admin Email : </td>
	<td>${managementData.adminMailId}</td>
	</tr>
	<tr>
	<td> Subscription Type : </td>
	<td><select name="subscriptionType" id="subScriptionType">
    <option value="One User Monthly Subscription">One user : $5.00USD - monthly</option>
    <option value="One User Yearly Subscription">One user : $50.00USD - yearly</option>
    <option value="Two Users Monthly Subscription">2 users : $10.00USD - monthly</option>
    <option value="Two Users Yearly Subscription">2 users : $100.00USD - yearly</option>
    <option value="Five Users Monthly Subscription">5 users : $25.00USD - monthly</option>
    <option value="Five Users Yearly Subscription">5 users : $250.00USD - yearly</option>
    <option value="Unlimited Users Monthly Subscription">Unlimited Users : $100.00USD - monthly</option>
    <option value="Unlimited Users Yearly Subscription">Unlimited Users : $1,000.00USD - yearly</option>
	</select></td>
	</tr>
	<tr>
	<td> Subscription Date : </td>
	<td> ${managementData.subscriptionDate}</td>
	</tr>
	<tr>
	<td> Users Emails : </td>
	<td> <textarea id="mailIdsTextArea" name="userMailds" onsubmit="";></textarea> </td>
	</tr>
	</table>
		<div class="subscribtionManagementButton" align="center">
   			<input id="submitButton" type="submit" class="allviews-common-button" name="login" value="Change SubScription"/>
		</div>
		<script type="text/javascript">
			if(${managementData.userMailds}.list.length>0){
				var mailids="";
				for(var i=0; i <  ${managementData.userMailds}.list.length; i++){
					var listItemDiv = ${managementData.userMailds}.list[i]+"\n";
					mailids=mailids+listItemDiv;
				}
				$('#mailIdsTextArea').val(mailids);
			}
			document.getElementById('#subScriptionType').options[${managementData.subscriptionType}].selected = true;
			
			function validateEmail(elementValue){  
			   var emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;  
			   return emailPattern.test(elementValue);  
 			}  
 			
 			$('#target').keydown(function() {
 			$('#target').val().split('\n');
			
			});

		</script>
	 </form>  
	 </div>
  </body>
</html> 