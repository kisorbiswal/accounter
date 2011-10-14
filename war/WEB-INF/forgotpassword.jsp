<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<title> Forgotten Password
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible">


<link rel="shortcut icon" href="/images/favicon.ico" />
<%@ include file="./feedback.jsp" %>
<link type="text/css" href="../css/ss.css" rel="stylesheet">
<link type="text/css" href="../css/cmxform.css?version=<%= version%>" rel="stylesheet">

<script  type="text/javascript" >
$.validator.setDefaults({
	submitHandler: function() {
		$('#accounterForm').submit();
	}
});

$(document).ready(function() {
	$('#submitButton').click(function() {
		$("#accounterForm").validate({
			rules: {
				emailId: {
					required: true,
					email: true
				},
				companyName: "required"
			},
			messages: {
				emailId: "please enter a valid Email",
				companyName: "please enter Company ID"
			}
		});
	});
});	
	
</script>
</head>
	<body>
		<div id="commanContainer">
		   <img src="/images/Accounter_logo_title.png" class="accounterLogo" />
		   <div id="forgot-password_error" class="common-box">
		      <p>Please enter your email address. You will receive a code to create a new password through the email.</p>	
		   </div>
		   <form id="accounterForm" method="post" action="/main/forgotpassword">
		      <div>
			    <label>E-mail<label>
				<br>
				<input id="mid-box"  type="text" name="emailId" tabindex="1" value="${emailId}">
			  </div>
			  <div id="forgot-login">
			     <input type="submit" tabindex="3" value="Get New Password" name="ok" class="allviews-common-button" id="submitButton">
			  </div>
		   </form>
		   <div class="form-bottom-options">
		      <a href="/main/login" >Login</a>
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