<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<title> Forgotten Password
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible">
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
		<div class="login clearfix" id="login" >
	    <div class ="body-container">
	      <%@ include file="./header.jsp" %>
			<div class="middle-part" id="cen">
				<div class="middle-signup-box" id="mid-1">
					<div class ="Welcome-Accounter-box">
							<h2>Forgot Password</h2>
					<table class="login-fields-box">
					<tr>
						<td ><div class="login-box-left-top"></div></td>
						<td ><div  class="login-box-top-middle"></div></td>
						<td ><div class="login-box-right-top"></div></td>
					</tr>
					<tr>
						<td class="login-box-left-middle" ></td>
						<td class="login-box">
								<div class="mid-login-box">
								<form id="accounterForm" method="post" action="/forgotpassword">
								<c:if test="${successMessage != null}">
								<span style="color: #3299A4; line-height: 1.5;">
								${successMessage} </span>
								</c:if>
								<c:if test="${successMessage == null}">
								<c:if test="${errorMessage != null}">
								<span style="color: #CC0000; line-height: 1.5;">
								${errorMessage} </span>
								</c:if>
								<div class="mid-login-box1">
							   <table>
							      <tr>
								     <td>Email</td>
									 <td>
										<input id="mid-box"  type="text" name="emailId" tabindex="1" value="${emailId}">								
									 </td>
								  </tr>
								  <tr>
								<tr>
									 <td>Company ID </td>
									 <td>
										<input id="mid-box1"  type="text" name="companyName" tabindex="2" value="${companyName}">
									</td>
								  </tr>
								   <tr>
								  <td>
								 
								  </td>
								  <td>
								  
								  <ul class="forgot-cancel-button">
								         <li><span class="signup-submit-left"></span></li>
								         <li><input type="button" tabindex="4" value="Cancel" name="cancel" class="signup-submit-mid forget-but" onClick="location.href='/site/login.jsp'"></li>
								         <li><span class="signup-submit-right"></span></li>
								  </ul>
								  <ul class="forgot-ok-button">
								         <li><span class="signup-submit-left"></span></li>
								         <li><input type="submit" tabindex="3" value="OK" name="ok" class="signup-submit-mid forget-but" id="submitButton"></li>
								         <li><span class="signup-submit-right"></span></li>
								  </ul>
								  
								   <!--<input type="submit" tabindex="3" value="Ok" name="ok" class="forget-but" id="submitButton">
								  <input type="button" tabindex="4" value="Cancel" name="cancel" class="forget-but" onClick="location.href='/site/login.jsp'">-->
								  </td>
								  </tr>
								  </table>
								</div>
								</c:if>
								</div>
									</td>
										<td class="login-box-right-middle"></td>
										</tr>
										<tr>
										<td ><div class="login-box-left-bottom"></div></td>
										<td ><div class="login-box-bottom-middle"></div></td>
										<td ><div class="login-box-right-bottom"></div></td>
										</tr>	
					</table>
					</form>
				
				</div>
				</div><div class="login-box-shadow"></div>
			</div>
				<div class="down-test" id="down"></div>
			<%@ include file="./footer.jsp" %>
		</div>
		</div>
		</body>
</html>