<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<title> Forgotten Password
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible">

<% String version = application.getInitParameter("version"); %>
<link rel="shortcut icon" href="../images/favicon.ico" />
<link type="text/css" href="../css/ss.css?version=<%= version%>" rel="stylesheet">
<link type="text/css" href="../css/cmxform.css?version=<%= version%>" rel="stylesheet">
<script type="text/javascript" src="/jscripts/jquery-1.6.2.js"></script>
<script src="/jscripts/jquery.validate.js" type="text/javascript"></script>

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
	<%@ include file="./feedback.jsp" %>
		<div id="commanContainer">
		   <img src="../images/Accounter_logo_title.png" class="accounterLogo" />
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
		
		<script type="text/javascript" charset="utf-8">
			var is_ssl = ("https:" == document.location.protocol);
			var asset_host = is_ssl ? "https://s3.amazonaws.com/getsatisfaction.com/" : "http://s3.amazonaws.com/getsatisfaction.com/";
			document.write(unescape("%3Cscript src='" + asset_host + "javascripts/feedback-v2.js' type='text/javascript'%3E%3C/script%3E"));
		</script>
		 
		</body>
</html>