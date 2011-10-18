<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title> Login | Accounter
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible" />

<link rel="shortcut icon" href="/images/favicon.ico" />

<%@ include file="./feedback.jsp" %>
<link type="text/css" href="../css/ss.css" rel="stylesheet" />
<link type="text/css" href="../css/cmxform.css?version=<%= version%>" rel="stylesheet" />
<script  type="text/javascript" >
	$(document).ready(function() {
	$('#submitButton').click(function() {
	 $("#submitButton").addClass("login-focus");
	$("#accounterForm").validate({
		rules: {
			emailId: "required",
			password: "required",
			},
		messages: {
			emailId: "please enter your Email",
			password: "please enter password",
			}
		});
	});
	 
});	
</script>

<%
   String app = request.getHeader( "Nativeapp" );
   boolean isNative = ( app != null && !app.equals(""));
   if( isNative ){ %>
   <link type="text/css" rel="stylesheet" href="../css/nativeLogin.css?version=<%= version%>">
   <% } %>
</head>
	<body>
     <div id="commanContainer">
		   <img src="/images/Accounter_logo_title.png" class="accounterLogo" alt ="accounter logo"/>
		   <c:if test="${message != null}">
		   <div id="login_error" class="common-box">
				<span>${message} </span>
		   </div>
		   </c:if>	
		   <form id="accounterForm" method="post" action="/main/login">
		      <div class="email_password">
			    <label>Email</label>
				<br></br>
				<input id="mid-box"  type="text" name="emailId" tabindex="1" />
			  </div>
			  <div class="email_password">
			    <label>Password</label>
				<br></br>
				<input id="mid-box1"  type="password" name="password" tabindex="2" />
			  </div>
			  <div class="rememberMe">
			    <input id="checkbox1" type="checkbox" tabindex="4" name="staySignIn"/> 
				<label>Remember Me</label>
			  </div>
			  <div class="loginbutton">
			     <input id="submitButton" style="width:60px" type="submit" class="allviews-common-button" name="login" value="Login" tabindex="6"/>
			  </div>
		   </form>
		   <div class="form-bottom-options">
		      <a href="/main/forgotpassword" id="forget-link1" tabindex="5"> Lost your password?</a>
		   </div>
		    <div class="form-bottom-options">
		      <a href="/main/signup" id="signUp-link1" tabindex="6"> Sign up Accounter?</a>
		   </div>
		</div>
		
	     <!-- Footer Section-->
		
		<div id="mainFooter"  >
	    <div>
	       <span>&copy 2011 Vimukti Technologies Pvt Ltd</span> |
	       <a target="_blank" href="/site/termsandconditions"> Terms & Conditions </a> |
	       <a target="_blank" href="/site/privacypolicy"> Privacy Policy </a> |
	       <a target="_blank" href="/site/support"> Support </a>
	    </div>
	</div>
 

<%@ include file="./scripts.jsp" %>

		</body>
</html>