<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title> Login | Accounter
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible">

<link rel="shortcut icon" href="../images/favicon.ico" />
<% String version = application.getInitParameter("version"); %>
<link type="text/css" href="../css/ss.css?version=<%= version%>" rel="stylesheet">
<script type="text/javascript" src="/jscripts/jquery-1.6.2.js"></script>
<script src="/jscripts/jquery.validate.js" type="text/javascript"></script>
<link type="text/css" href="../css/cmxform.css?version=<%= version%>" rel="stylesheet">
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

   
</head>
	<body>
	<%@ include file="./feedback.jsp" %>
     <div id="commanContainer">
		   <img src="../images/Accounter_logo_title.png" class="accounterLogo" />
		   <c:if test="${not empty errorMsg}">
		   <div id="login_error" class="common-box">
				<span>${errorMsg} </span>
		   </div>
		   </c:if>	
		   <form id="accounterForm" method="post" action="/adminlogin">
		      <div class="email_password">
			    <label>Email</label>
				<br>
				<input id="mid-box"  type="text" name="emailId" tabindex="1">
			  </div>
			  <div class="email_password">
			    <label>Password</label>
				<br>
				<input id="mid-box1"  type="password" name="password" tabindex="2">
			  </div>
			  <div class="loginbutton">
			     <input id="submitButton" style="width:60px" type="submit" class="allviews-common-button" name="login" value="Login" tabindex="6"/>
			  </div>
		   </form>
		   <div class="form-bottom-options">
		      <a href="/forgotpassword" id="forget-link1" tabindex="5"> Lost your password?</a>
		   </div>
		</div>
		
	</div>
<script type="text/javascript">
</script>
		</body>
</html>