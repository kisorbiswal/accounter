<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
 "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="i18n" uri="/WEB-INF/i18n.tld"%>
<%@page pageEncoding="UTF-8" %>

<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<title> <i18n:i18n msg='companyPassword'/> | Accounter
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible" />
<link rel="shortcut icon" href="/images/favicon.ico" />
<%
	String error =(String) request.getAttribute("error");
	String showResetLink =(String) request.getAttribute("showResetLink");
%>
<%@ include file="./feedback.jsp" %>
<link type="text/css" href="../css/ss.css?version=<%= version%>" rel="stylesheet" />
<link type="text/css" href="../css/cmxform.css?version=<%= version%>" rel="stylesheet" />

 
<script type="text/javascript" >
$(document).ready(function() {
	$('#submitButton').click(function() {
		$("#accounterForm").validate({
			rules: {
				companyPassword: {
					required: true 
			 }
			}
		});
	});
});	
</script>
</head>
	<body>
	<div id="commanContainer">
		   <img class="accounterLogo" src="/images/Accounter_logo_title.png" alt="Accounter logo" />
		   	<c:if test="${error!=null}"> 
			<div id="login_success" class="common-box">
				<span>${error}</span>
			</div>
  		  </c:if>
		 <form class="accounterform" id="accounterForm" method="post" action="companypassword">
								<div class="mid-login-box1">
							   <div >
								     <div><i18n:i18n msg='companyPassword'/> : </div>
									 <div>
										<input id="mid-box"  type="password" name="companyPassword"   value="" class="reset_password" />								
									 </div>
									 <div id="reset_hint_box">
									 
									 </div>
									 
								   <div class="OkButton">
			  						   <input type="submit"  value="<i18n:i18n msg='submit'/>" name="submit" class="allviews-common-button" style="width:60px" id="submitButton" />
			  						    
		     								 <a href="/main/company/password/recovery" ><i18n:i18n msg='resetPassword'/></a>
		   								 
			 						 </div>
								  </div>
								</div>
					</form>
					</div>
	<div class="down-test" id="down"></div>
		
	 
	<%@ include file="./scripts.jsp" %>
		</body>
</html>