<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<title> Reset Password | Accounter
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible">
<link rel="shortcut icon" href="../images/favicon.ico" />
<% String version = application.getInitParameter("version"); %>
<link type="text/css" href="../css/ss.css?version=<%= version%>" rel="stylesheet">
<link type="text/css" href="../css/cmxform.css?version=<%= version%>" rel="stylesheet">

<script type="text/javascript" src="/jscripts/jquery-1.6.2.js"></script>
<script src="/jscripts/jquery.validate.js" type="text/javascript"></script>
<script src="/jscripts/passwordStrength.js" type="text/javascript"></script>
<script type="text/javascript" >

$(document).ready(function() {
	$('#mid-box').attr('autocomplete', 'off');
	$('#mid-box').password_strength();
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
	
	$('.reset_password').click(function(){
	    $('.indication-box').remove();	
	     $('#reset_hint_box').append("<div class='indication-box'><div class='left-arrow'></div><div class='box-data'>Use 3 to 60characters, don't use your name. Use mix of lower/uppercase letters, numbers and special characters</div></div>");        
	    }).blur(function() {
	        $('.indication-box').remove();
	 });
	
});	
	
	 function CheckPassword(password)
	 {
 	    var strength = new Array();
 	    strength[0] = "Very Weak";
 	    strength[1] = "Weak";
 	    strength[2] = "Medium";
 	    strength[3] = "Strong";
 	    strength[4] = "Very Strong";
 	 
 	    var score = 1;
 	 
 	    if (password.length < 4)
 	        return strength[1];
 	 
 	    if (password.length >= 8)
 	        score++;
 	    if (password.length >= 12)
 	        score++;
 	    if (password.match(/\d+/))
 	        score++;
 	    if (password.match(/[a-z]/) &&
 	        password.match(/[A-Z]/))
 	        score++;
 	    if (password.match(/.[!,@,#,$,%,^,&,*,?,_,~,-,£,(,)]/))
 	        score++;
 	 
 	    return strength[score];
 	}
</script>
</head>
	<body>
	<%@ include file="./feedback.jsp" %>
	<div id="commanContainer">
		   <img class="accounterLogo" src="../images/Accounter_logo_title.png">
		   	<c:if test="${errormessage!=null}">
			<div id="login_success" class="common-box">
				<span>${errormessage}</span>
			</div>
  		  </c:if>
		 <form id="accounterForm" method="post" action="/main/resetpassword">
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
							   <div width="100%">
								     <div>New Password : </div>
									 <div>
										<input id="mid-box"  type="password" name="newPassword" onkeyup="CheckPassword(this.value)" tabindex="1" value="" class="reset_password">								
									 </div>
									 <div id="reset_hint_box">
									 
									 </div>
									 <div>Confirm Password : </div>
									 <div>
										<input id="mid-box1"  type="password" name="confirmPassword" tabindex="2" value="">
									</div>
								   <div class="OkButton">
			  						   <input type="submit" tabindex="6" value="Reset" name="login" class="allviews-common-button" style="width:60px" id="submitButton">
			 						 </div>
								  </div>
								</c:if>
								</c:if>
								</div>
					</form>
					</div>
	<div class="down-test" id="down"></div>
		
		<script type="text/javascript" charset="utf-8">
			var is_ssl = ("https:" == document.location.protocol);
			var asset_host = is_ssl ? "https://s3.amazonaws.com/getsatisfaction.com/" : "http://s3.amazonaws.com/getsatisfaction.com/";
			document.write(unescape("%3Cscript src='" + asset_host + "javascripts/feedback-v2.js' type='text/javascript'%3E%3C/script%3E"));
		</script>
		
		</body>
</html>