<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<title> Reset Password | Accounter
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible">
<link rel="shortcut icon" href="../images/favicon.ico" />
<% String version = application.getInitParameter("version"); %>
<link type="text/css" href="/css/ss.css?version=<%= version%>" rel="stylesheet">
<link type="text/css" href="/css/cmxform.css?version=<%= version%>" rel="stylesheet">

<script type="text/javascript" src="/jscripts/jquery-1.6.2.js"></script>
<script src="/jscripts/jquery.validate.js" type="text/javascript"></script>
<script type="text/javascript" charset="utf-8">
	var is_ssl = ("https:" == document.location.protocol);
	var asset_host = is_ssl ? "https://s3.amazonaws.com/getsatisfaction.com/" : "http://s3.amazonaws.com/getsatisfaction.com/";
	document.write(unescape("%3Cscript src='" + asset_host + "javascripts/feedback-v2.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript" charset="utf-8">
	var feedback_widget_options = {};

	feedback_widget_options.display = "overlay";  
  	feedback_widget_options.company = "vimukti";
	feedback_widget_options.placement = "left";
	feedback_widget_options.color = "#222";
	feedback_widget_options.style = "idea";
	
	var feedback_widget = new GSFN.feedback_widget(feedback_widget_options);
</script>
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
				newPassword: "required",
				confirmPassword: {
					required: true,
					equalTo: "#mid-box"
				}
			},
			messages: {
				newPassword: "please enter your password",
				confirmPassword: {
					required: "please confirm your password",
					equalTo: "Please enter the same password as above"
				}
			}
		});
	});
});	
	
</script>
</head>
	<body>
		<div class="login clearfix" id="login" >
	    <div class ="body-container">
	      <%@ include file="./header.jsp" %>
			<div class="middle-part" id="cen">
				<div class="middle-signup-box" id="mid-1">
					<div class ="Welcome-Accounter-box" style="padding:10px">
							<h2 style="margin:0 0 10px 0px">Reset Password</h2>
								<div class="reset-login-box">
								<form id="accounterForm" method="post" action="/resetpassword">
								<c:if test="${successMessage != null}">
								<span style="color: #3299A4; line-height: 1.5;">
								${successMessage} </span>
								</c:if>
								<c:if test="${errorMessage != null}">
								<span style="color: #CC0000; line-height: 1.5;">
								${errorMessage} </span>
								</c:if>
								<c:if test="${successMessage == null}">
								<c:if test="${errorMessage == null}">
								<div class="mid-login-box1">
							   <table>
							      <tr>
								     <td>New Password </td>
									 <td>
										<input id="mid-box"  type="password" name="newPassword" tabindex="1" value="">								
									 </td>
								  </tr>
								  <tr>
								<tr>
									 <td>Confirm Password </td>
									 <td>
										<input id="mid-box1"  type="password" name="confirmPassword" tabindex="2" value="">
									</td>
								  </tr>
								   <tr>
								  <td>
								      
								  </td>
								  <td>
								     <ul class="reset-ok-button">
										<li><span class="signup-submit-left"></span></li>
										<li><input type="submit" tabindex="3" value="Ok" name="ok" class="signup-submit-mid forget-but" id="submitButton"></li>
										<li><span class="signup-submit-right"></span></li>
									 </ul>
								     <ul class="reset-cancel-button">
										<li><span class="signup-submit-left"></span></li>
										<li><input type="button" tabindex="4" value="Cancel" name="cancel" class="signup-submit-mid forget-but" onClick="location.href='/login'"></li>
										<li><span class="signup-submit-right"></span></li>
									 </ul>
								  </td>
								  </tr>
								  </table>
								</div>
								</c:if>
								</c:if>
								</div>
									
					</form>
				
				</div>
			</div>
				<div class="down-test" id="down"></div>
			<%@ include file="./footer.jsp" %>
		</div>
		</div>
		</body>
</html>