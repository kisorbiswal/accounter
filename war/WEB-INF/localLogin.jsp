<html>
<head>
<title> Login | Accounter
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible">
<link rel="shortcut icon" href="../images/favicon.ico" />
<link type="text/css" href="../css/ss.css?version=<%= version%>" rel="stylesheet">
<script type="text/javascript" src="/jscripts/jquery-1.6.2.js"></script>
<script src="/jscripts/jquery.validate.js" type="text/javascript"></script>
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
				emailId: "required",
				password: "required",
				companyName: "required"
				},
			messages: {
				emailId: "please enter your Email",
				password: "please enter password",
				companyName: "please enter company name",
				}
			});
		});
	});	
</script>


<script  type="text/javascript" >
	function loadImage(){
		document.getElementById("hiddenDiv").style.display="block";
    		document.getElementById("login").style.display="none";
	}	
</script>
</head>
	<body>
	<%
			
			Cookie cookies[] = request.getCookies();			
			String emailId = null, password=null, staySignIn=null;
			if (cookies != null && cookies.length > 3) {				
				int i =-1;
				while (++i < cookies.length) {
					if (cookies[i].getName().equals("staySignIn"))
						staySignIn = cookies[i].getValue();	
					if (cookies[i].getName().equals("emailId"))
						emailId = cookies[i].getValue();
					if (cookies[i].getName().equals("password"))
						password = cookies[i].getValue();
				}	
					
				if (staySignIn != null && !staySignIn.equalsIgnoreCase("on")) {
						emailId = null;
						password = null;
				}
			}	

			String identityID = (String) session.getAttribute("identityID");			
			if (identityID != null) {
				RequestDispatcher dispatcher = request.getRequestDispatcher("/accounter");
				dispatcher.forward(request, response);				
			}
	%>
		<div class="login clearfix" id="login" >
	    <div class ="body-container">
	      <%@ include file="./header.jsp" %>
			<div class="middle-part" id="cen">
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
								<form id="accounterForm" method="post" action="/main/login" onsubmit="return loadImage()">
								
								<c:if test="${message != null}">
								<span style="background: #CC0000; color: #F2F3F5; line-height: 1.5;">
								${message} </span>
								</c:if>
								<div class="mid-login-box1">
							   <table>
							      <tr>
								     <td>Email</td>
									 <td>

										<input id="mid-box"  type="text" name="emailId" tabindex="1" value="<%=emailId != null ? emailId : "" %>"> 								
									 </td>
								  </tr>
								  <tr>
									<td>Password </td>
									<td>
									     <input id="mid-box1"  type="password" name="password" tabindex="2" value="<%=password != null ? password : "" %>">
									 
									</td>
								  </tr>
								  <tr>
								  	<c:if test="${companyName == null}">
								<tr>
									 <td>Company Name </td>
									 <td>
										<input id="mid-box2"  type="text" name="companyName" tabindex="3">
									</td>
								  </tr>
								    </c:if>
								  </table>
								</div>
										<div class ="fields-table">
											<input id="checkbox1" type="checkbox" tabindex="4" name="staySignIn" checked/> Stay signed in 
											<a href="/main/forgotpassword" id="forget-link1" tabindex="5"> Forgot your password?</a></br>
											<input id="submitButton" type="submit" class="signup-but" name="login" value="Login" tabindex="6"/>
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
								<p class="text-style"> Don't have a Accounter Login?</p></td>
								</tr>
								<tr>
								<td>
								<a class="link-color" href="/site/signup" tabindex="8"> Try Accounter free</a></td>
								<td></td><td></td><td></td><td></td>
								<td> <a class="link-color1" href="/site/termsandconditions" tabindex="9" > Terms of Use</a></td>
								 </tr>
								</table>
								
		          		
					</form>
				
				</div>
				</div><div class="login-box-shadow"></div>
			</div>
			<div style="margin-top: 10px;text-align: right;">
					<script type="text/javascript" src="https://seal.godaddy.com/getSeal?sealID=j3yAgRDXC0qfu3z4J9psqkMFAAaNWdtnwa12xlAaX24wXCiHRa4D4SkC">
						</script>
			</div>
				<div class="down-test" id="down"></div>
			<%@ include file="./footer.jsp" %>
		</div>
		</div>
		
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
		</body>
</html>