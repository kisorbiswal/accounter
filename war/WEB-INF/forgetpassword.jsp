<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title> Forgotten Password
</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="./feedback.jsp" %>
<title>Forget Password</title>
<script type="text/javascript">
function validation(formObject){
	var emailId=document.getElementById("emailId").value;
	if(emailId==null||emailId==""){
		errorDataShow();
		return false;
	}
	
	else if(emailId.indexOf("@")<1||emailId.indexOf(".")<1){
		errorDataShow();
		return false;
	}
	var companyName=document.getElementById("companyName").value;
	if(companyName==null||companyName==""){
		errorDataShow1();
		return false;
	}
}
function errorDataShow1(){
	var msg="Please Enter valid Company Name";
	document.getElementById("hdError1").innerHTML=msg;
		document.getElementById("companyName").focus();
} 
function errorDataShow(){
		var msg="Please Enter valid email address";
		document.getElementById("hdError").innerHTML=msg;
		document.getElementById("emailId").focus();
}
function labelHide(){
	document.getElementById("hdError").innerHTML="";
	document.getElementById("hdError1").innerHTML="";
	document.getElementById("servermsg").innerHTML="";
}
</script>
<style>
.bizantralogo{
padding-bottom: 2px;
	padding-right: 10px;
	padding-top: 12px;
}
.emailBox{
	border:2px solid #B3B3FF;
	float:left;
	margin:6px;
	padding:6px;
}
.emailLabel{
	margin:10px;
	padding:6px;
}
.companyLabel{
padding-top:20px;	
	
}
.hdError{
	color:#55F055;
	font-family:Helvetica;
	padding-left:0px;
}
.hdError1{
	color:#55F055;
	font-family:Helvetica;
	
}
.servermsg{
	color:#7A16B7;
	padding-left:350px;
	margin:50px;
}
</style>
</head>
<body>
    <h1>Please Contact To Administrator</h1>
	 <!-- <div class="bizantralogo">
	<img src="/images/bizantra-image.png" alt="Bizantra" />
	<h4>Password Assistance</h4>
	<span>Please enter your email Address.If you are valid user then you will get your passord through mail</span><br/>
	<div class="emailBox">
		<form  action="/do/forgetpassowrd" name="emailForm" id="emailForm" onsubmit="return validation(this)" method="POST">
			<table><tr><td><b>Email:</b></td><td> <input type="text" name="emailId" id="emailId" onClick="labelHide()" onChange="labelHide()" /></td></tr>
			<tr><td><b>Company Name:</b></td><td> <input type="text" name="companyName" id="companyName" onClick="labelHide()"/></td></tr>
			<tr><td></td><td align="right"><input type="submit" id="submitButton" value="submit" /></td></tr></table>
		</from> 
	</div>
	<div class="emailLabel"><label id="hdError" class="hdError"></label>
	</div>
	<div class="companyLabel"><label id="hdError1" class="hdError1"></label></div>
	</div> -->
	<br/><br/><br/><br/><br/><br/><p style="margin:6px;"><a href="/bizantra" style="color:blue">Back to Login</a></p>
	<div class="servermsg" id="servermsg">${message}</div>
	
    <!-- Footer Section-->
	
	<div id="mainFooter"   >
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