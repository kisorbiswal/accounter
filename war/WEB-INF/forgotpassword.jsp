<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="i18n" uri="/WEB-INF/i18n.tld"%>
<%@page pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<title> <i18n:i18n msg='forgottenPassword'/>
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible" />

<link rel="shortcut icon" href="/images/favicon.ico" />
<%@ include file="./feedback.jsp" %>
<link type="text/css" href="../css/ss.css?version=<%= version%>" rel="stylesheet" />
<link type="text/css" href="../css/cmxform.css?version=<%= version%>" rel="stylesheet" />

<script  type="text/javascript" >

$(document).ready(function() {
	$('#submitButton').click(function() {
		$("#accounterForm").validate({
			rules: {
				emailId: {
					required: true,
					email: true
			},
			messages: {
				emailId: "<i18n:i18n msg='pleaseenteravalidemailaddress'/>"
				 
			}
			}
		});
	});
});	
	
</script>
</head>
	<body>
		<div id="commanContainer">
		   <img src="/images/Accounter_logo_title.png" class="accounterLogo" alt = "accounter logo"/>
		   <div id="forgot-password_error" class="common-box">
		      <p><i18n:i18n msg='forgotpasswordMsg'/></p>	
		   </div>
		   <form id="accounterForm" method="post" action="/main/forgotpassword">
		      <div>
			    <label><i18n:i18n msg='email'/></label>
				<br></br>
				<input id="mid-box"  type="text" name="emailId" tabindex="1" value="${emailId}" />
			  </div>
			  <div id="forgot-login">
			     <input type="submit" tabindex="3" value="<i18n:i18n msg='getNewPassword'/>" name="ok" class="allviews-common-button" id="submitButton" />
			  </div>
		   </form>
		   <div class="form-bottom-options">
		      <a href="/main/login" ><i18n:i18n msg='login'/></a>
		   </div>
		</div>
		
		
	     <!-- Footer Section-->
		
		<div id="mainFooter"  >
	    <div>
	       <span><i18n:i18n msg='atTherateCopy'/></span> |
	       <a target="_blank" href="/site/termsandconditions"><i18n:i18n msg='termsConditions'/></a> |
	       <a target="_blank" href="/site/privacypolicy"><i18n:i18n msg='privacyPolicy'/></a> |
	       <a target="_blank" href="/site/support"><i18n:i18n msg='support'/></a>
	    </div>
	</div>
		
		 
	<%@ include file="./scripts.jsp" %>
		</body>
</html>