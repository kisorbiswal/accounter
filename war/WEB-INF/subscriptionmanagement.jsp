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

    	String useremailIds =(String) request.getAttribute("userIdsList");
    	String expDate =(String) request.getAttribute("ExpiredDate");
    	Integer subscriptionType =(Integer) request.getAttribute("premiumType");
  	%>
  	<title>Subscription Management</title>
  </head>
  <body>
  <div id="commanContainer" style="width:420px;  font-size: 13px;">
	<form id="subscription_complition_form" method="post"  class="form-box"  action="/site/subscriptionmanagement">
	<table cellspacing="10">
	<tr>
	</tr><tr></tr><tr>
	<td> Subscription expire date : </td> 
	<td>${expDate}</td>
	</tr><tr></tr><tr>
	<td> Subscription type : </td> 
	<td id="subscriptionTypevalue"></td></tr><tr></tr><tr>
	<td>Users invited by you : </td> 
	<td id="emailIdsList"></td>
	</tr></tr><tr>
	<tr>
	<input type="hidden" name="subscriptionType" value="${subscriptionType}" >
	<td> Subscription Type : </td>
	<td><select  id="subScriptionTypeCombo" disabled>
    <option value="One User Monthly Subscription">One user </option>
    <option value="One User Yearly Subscription">Two users </option>
    <option value="Two Users Monthly Subscription">Five users </option>
    <option value="Two Users Yearly Subscription">Unlimited </option>
	</select></td>
	</tr>
	<tr>
	<td> Users Emails : </td>
	<td> <textarea id="mailIdsTextArea"  name="userMailds" onsubmit="";></textarea> </td>
	</tr>
	<tr>
	<td> <p id="error" style="color:red;"> </p> </td></tr>
	</table>
	
	
		<div class="subscribtionManagementButton" align="center">
   			<input id="submitButton" type="submit" class="allviews-common-button" name="login" value="Save SubScription"/></form>
   			<form id="gopremiumForm" method="post" action="/site/subscription/gopremium">
   			<input id="goPremiumButton" class="allviews-common-button" type="submit" name="premium" value="Upgrade Premium"/></form>
		</div>
		</form>
		<script type="text/javascript">
                  var useremailIds= '<%= useremailIds%>';
                  var useremailIdsArray =useremailIds.split(',');
                  var finalstring ="";
                  for(var i=0;i<useremailIdsArray.length;i++){
                	  finalstring = finalstring +"\n"+useremailIdsArray[i];
                  }
                  finalstring = finalstring.substring(1);
				$('#mailIdsTextArea').val(finalstring);
				$('#emailIdsList').text(finalstring);
				$('#subscriptionTypevalue').text(document.getElementById('subScriptionTypeCombo').options[${subscriptionType}].value);
			 
			document.getElementById('subScriptionTypeCombo').options[${subscriptionType}].selected = true;
			$('#submitButton').click(function(){
			if(validate()){
			$('#error').text("");
			$('#subscription_complition_form').submit();
			}else{
			 $('#error').text("maximum number of users exceeded");
			 return false;
			}
			})
			function validateEmail(elementValue){  
			   var emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;  
			   if (emailPattern.test(elementValue)){
			   return true;
			   }  else{
			   return false;
			   }
 			}  
 			
 			function validate(){
 			var type =${subscriptionType};
 			var maxLimit =1; 
 			if(type ==0 || type==1){
 			maxLimit =1;
 			}else if(type ==2 || type==3){
 			maxLimit =2;
 			}else if(type ==4 || type==5){
 			maxLimit =5;
 			}
 			$('#mailIdsTextArea').val().replace(/^\s+|\s+$/, '');
 			var textArray =$('#mailIdsTextArea').val().split('\n');
 			var emailCount =0;
 			for(var i=0; i<=textArray.length; i++){
 			if(validateEmail(textArray[i])){
 			emailCount = emailCount+1;
 			}
 			}
 			 if(emailCount>maxLimit){
 			 return false;
 			 }else {
 			 return true;
 			 }
}
		</script>
	 </form>  
	 </div>
  </body>
</html> 