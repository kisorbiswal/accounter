<html>
<head>
<title> Login | Accounter
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
<link rel="shortcut icon" href="../images/favicon.ico" />
<% String version = application.getInitParameter("version"); %>
<link type="text/css" href="ss.css?version=<%= version%>" rel="stylesheet">
<script type="text/javascript" src="/jscripts/jquery-1.6.2.js"></script>
<script src="/jscripts/jquery.validate.js" type="text/javascript"></script>
<link type="text/css" href="cmxform.css?version=<%= version%>" rel="stylesheet">
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
   <link type="text/css" rel="stylesheet" href="/nativeLogin.css?version=<%= version%>">
   <% } %>
   
</head>
	<body>
		<div class="login clearfix" id="login" >
	    <div class ="body-container">
	      <%@ include file="./header.jsp" %>
			<div class="middle-part" id="cen">
			   <div style="padding-top:30px;">
				<div class="middle-signup-box" id="mid-1">
					<div class ="Welcome-Accounter-box">
							<h2>Welcome To Accounter</h2>
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
								<form id="accounterForm" method="post" action="/site/login">
								
								<c:if test="${message != null}">
								<span style="background: #CC0000; color: #F2F3F5; line-height: 1.5;">
								${message} </span>
								</c:if>
								<div class="mid-login-box1">
							   <table class="login-table">
							      <tr>
								     <td> <span>Email</span></td>
									 <td>

										<input id="mid-box"  type="text" name="emailId" tabindex="1"> 								
									 </td>
								  </tr>
								  <tr>
									<td><span>Password</span> </td>
									<td>
									     <input id="mid-box1"  type="password" name="password" tabindex="2">
									 
									</td>
								  </tr>
								
								  </table>
								</div>
										<div class ="fields-table">
											<input id="checkbox1" type="checkbox" tabindex="4" name="staySignIn" value="unchecked"/> Stay signed in 
											<a href="/site/forgotpassword" id="forget-link1" tabindex="5"> Forgot your password?</a></br>
											
											<ul class="login-submit-button">
											     <li><span class="signup-submit-left"></span></li>
											     <li><input id="submitButton" style="width:60px" type="submit" class="signup-submit-mid" name="login" value="Login" tabindex="6"/></li>
											     <li><span class="signup-submit-right"></span></li>
											 </ul>
											
											<!--<input id="submitButton" type="submit" class="signup-but" name="login" value="Login" tabindex="6"/>-->
										</div>			
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
					<table class="size-width">
						<tr>
							<td>
								<p class="text-style"> Don't have a Accounter Login?</p>
							</td>
							<td>
								<a class="link-color" href="/site/signup" tabindex="8"> SignUp Accounter for free</a>
							</td>
						</tr>
						<!--<tr>				
							<td></td><td></td><td></td><td></td>
							<td> <a class="link-color1" href="/site/termsandconditions" tabindex="9" > Terms of Use</a></td>
							 </tr>-->
					</table>
								
		          		
					</form>
				
				</div>
				</div></div><div class="login-box-shadow"></div>
			</div>
			<div class="login_logo_image" >
			<!--<div style="margin-top: 10px;text-align: right;">
					<script type="text/javascript" src="https://seal.godaddy.com/getSeal?sealID=j3yAgRDXC0qfu3z4J9psqkMFAAaNWdtnwa12xlAaX24wXCiHRa4D4SkC">
						</script>
			</div>-->
				<div class="down-test" id="down"></div>
				</div>
			<%@ include file="./footer.jsp" %>
		</div>
		</div>
		
<script type="text/javascript">

var _gaq = _gaq || [];
_gaq.push(['_setAccount', 'UA-24502570-1']);
_gaq.push(['_trackPageview']);

(function() {
var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
})();

</script>

		</body>
</html>