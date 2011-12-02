<html>
<head>
<title>  Delete My Account
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible">

<link rel="shortcut icon" href="/images/favicon.ico" />

<%@ include file="./feedback.jsp" %>
<link type="text/css" href="../css/ss.css" rel="stylesheet">
</head>
<body>
<div id="commanContainer">	
	<div>
		<img src="/images/Accounter_logo_title.png" class="accounterLogo" alt="loading" />
	</div>
	
	<div>
		<form id="deleteform" name="deleteform" method="post" action="/main/deleteAccount">
		<div class="company-heading" style="text-align:center"><h3>Delete Account</h3></div>
		<div>
			<h4>We're really sorry to see you go.</h4><br \>
		</div>
		<span>Why are you leaving Accounter ? </span><br \>
		<div class="delete-account-options">
			<input id="tooslow" type="checkbox" name="tooslow" value="Too Slow" /> Too Slow<br />
			<input id="takelong" type="checkbox" name="takelong" value="Takes long time to learn" /> Takes long time to learn<br />
			<input id="mydata" type="checkbox" name="mydata" value="Can't get my data in" /> Can't get my data in<br />
			<input id="features" type="checkbox" name="features" value="Doesn't have the features I need" /> Doesn't have the features I need<br />
			<input id="wentoutof" type="checkbox" name="wentoutof" value="Went out of business" /> Went out of business<br />
			<input id="personalfinance" type="checkbox" name="personalfinance" value="I'm Looking for personal finance software" /> I'm Looking for personal finance software<br />
			<input id="nobusinessyet" type="checkbox" name="nobusinesssyet" value="I do not have a business yet" /> I do not have a business yet
		</div>
			<br \>
		    <h4>Other Reasons:</h4>
			<textarea class="delete-account-description" name="content"></textarea>
			<div>
	  			  <div class="signup-submit">
	      			<input type="button" class="allviews-common-button" value="Back" onclick="parent.location='/main/companies'" />
	  			 </div>
	  			 <div class="signup-submit">
	      			<input type="submit" class="allviews-common-button" value="Continue" />
	  			 </div>
			</div>
		</form>
	</div>
  <%@ include file="./scripts.jsp" %>
 </div>
</body>
</html>