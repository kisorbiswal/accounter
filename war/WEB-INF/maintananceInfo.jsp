<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Under Maintanance | Accounter
</title>
<meta content="text/html; charset=UTF-8" http-equiv="Content-Type" />
<meta content="IE=100" http-equiv="X-UA-Compatible" />
<link rel="shortcut icon" href="/images/favicon.ico" />
<script src="/jscripts/jquery-1.7.min.js" type="text/javascript"></script>
<link type="text/css" href="/css/ss.css" rel="stylesheet" />
<script  type="text/javascript" >
$(document).ready(function() {
	$('#notifyMe').click(function(){
		$('#successmsgDiv').addClass("display-none");
		$('#enterEmailDiv').removeClass('display-none');
	})
	$('#submitButton').click(function(){
	
		var email =$('#emailField').val();
		if(	IsValidEmail(email)){
			$.post('/main/maintanaceinform', {email: email},
				function(data) {
				if(data !=null){
					 if(data=="fail"){
						 $('#emailField').val("");
						 $('#successmsgDiv').addClass("display-none");
						 $('#errorMsg').removeClass("display-none");
					}else{
						$('#emailField').val("");
						 $('#errorMsg').addClass("display-none");
						$('#successmsgDiv').removeClass("display-none");
						$('#enterEmailDiv').addClass('display-none');
					 }
				}
					
				}
			)
		}
		else{
			$('#emailField').val("");
			 $('#errorMsg').removeClass("display-none");
		}
	});
	function IsValidEmail(email)
	 	{
	 	var filter = /^([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
	 	return filter.test(email);
	 	}
});
</script>
</head>
<body>
  <div id="maintananceContainer">
   <div class="maintanance_subcont">
    <img src="/images/Accounter_logo_title.png" class="accounterLogo" alt="Accounter logo" />
    <div class="unavailable_page">
      <div class="maintainence_logo">
        <img src="/images/maintainence_show.jpg" class="accounterLogo" alt= "Maintanance"/> 
      </div>
      <div class="maintainence_message">
	    <h3>Sorry! Accounter is down for maintanance.</h3>
	    <h5>We are performing maintenance on our systems to provide you with better service of Accounter.</h5>
	    <span> Please visit again later.For more info, visit </span>
	    <a target="_blank"  href="http://blog.accounterlive.com" style="color:RoyalBlue">blog.accounterlive.com</a>
	    <br/>
	    <a id="notifyMe" style="color:RoyalBlue">notify when working</a>
	    <div id="enterEmailDiv" class= "display-none">
	    <form id="submitForm">
	    <span>Please enter your emailid</span>
	    <input id="emailField"type="text" />
	    <input id= "submitButton" type="button" class="allviews-common-button" value ="submit"/></form>
	    </div>
	    <div class ="display-none" id="successmsgDiv">successfully registered your email</div>
	    <div class ="display-none" id="errorMsg">Please Enter Valid Email Id</div>
	</div>
   </div>
   </div>
  </div>
  <%@ include file="./scripts.jsp" %>
</body>
</html>