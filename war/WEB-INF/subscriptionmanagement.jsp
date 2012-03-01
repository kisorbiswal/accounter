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

    	String users =(String) request.getAttribute("userIdsList");
    	String expDate =(String) request.getAttribute("expiredDate");
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
	<td><%= expDate %></td>
	</tr><tr></tr><tr>
	<td> Subscription type : </td> 
	<td id="subscriptionTypevalue"></td></tr><tr></tr><tr>
	<td>Users invited by you : </td> 
	<td id="emailIdsList"></td>
	</tr></tr><tr>
	<tr>
	<input type="hidden" name="subscriptionType" value="${subscriptionType} -1" >
	<td> Subscription Type : </td>
	<td><select  id="subScriptionTypeCombo" disabled>
    <option value="One User ">One user </option>
    <option value="Two User ">Two users </option>
    <option value="Five Users ">Five users </option>
    <option value="Unlimited Users ">Unlimited </option>
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
		$('document').ready(function(){
		   var users= <%= users%>;
		   var subscriptionType=<%= subscriptionType %>;
           var finalstring="";

			if(users.length>0){
				finalstring = users[0].emailId ;
                for(var i=1; i <  users.length; i++){
					finalstring +='\n'+ users[i].emailId ;
				}
				$('#mailIdsTextArea').val(finalstring);
				$('#emailIdsList').text(finalstring);
				$('#subscriptionTypevalue').text(document.getElementById('subScriptionTypeCombo').options[subscriptionType].value);
			 
				document.getElementById('subScriptionTypeCombo').options[subscriptionType].selected = true;
			}

			
			$('#submitButton').click(function(){
				var textArray =$('#mailIdsTextArea').val().split('\n');
				if(validate(textArray)){
					for(var i=0; i <  users.length; i++){
						var isExists=false;
						for(var j=0; j < textArray.length; j++){
							if(users[i].emailId ==textArray[j]){
								isExists=true;
								break;
							}
						}
						if(!isExists){
							if(users[i].isCreated ==true){
								var r=confirm("do you want to delete the exicting customer '"+users[i].emailId+"'");
								if (r!=true){
					  				return false;	
					  			}
							}
						}	
					}
					$('#error').text("");
					$('#subscription_complition_form').submit();
				}else{
					 $('#error').text("maximum number of users exceeded");
					 return false;
				}
			});	
								
			function validateEmail(elementValue){  
			   var emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;  
			   if (emailPattern.test(elementValue)){
			  		return true;
			   }  else{
			 		return false;
			   }
 			} 
 			
 			function validate(textArray){
 				var type =subscriptionType;
 				var maxLimit =1; 
 				if(type ==0 || type==1){
 					maxLimit =1;
 				}else if(type ==2 || type==3){
 					maxLimit =2;
 				}else if(type ==4 || type==5){
 					maxLimit =5;
 				}
 				$('#mailIdsTextArea').val().replace(/^\s+|\s+$/, '');
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
		  
		});
		</script>
	 </form>  
	 </div>
  </body>
</html> 