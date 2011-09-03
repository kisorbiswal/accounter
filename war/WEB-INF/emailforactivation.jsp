<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
  <head>
        <meta content="IE=100" http-equiv="X-UA-Compatible">
		<link rel="shortcut icon" href="../images/favicon.ico" />
		<% String version = application.getInitParameter("version"); %>
		<link type="text/css" href="../css/ss.css?version=<%= version%>" rel="stylesheet">
		<link type="text/css" href="../css/cmxform.css?version=<%= version%>" rel="stylesheet">
		<script type="text/javascript" src="/jscripts/jquery-1.6.2.js"></script>
		<script src="/jscripts/jquery.validate.js" type="text/javascript"></script>
		<script  type="text/javascript" >
			$(document).ready(function() {
				$('#submitButton').click(function() {
					$("#emailActivationForm").validate({
						rules: {
						emailid:{ 
						required :true,
						email: true
						}},
						messages: {
						emailid: "Please enter an valid email address" 
						}
					});
				});
				$('#emailidBox').keypress(function(e) {
					  if (e.keyCode == '9') {
					     e.preventDefault();
					    $('#submitButton').focus();
					   }
					});
			});
		</script>
  </head>
    <body>
	    <div id="commanContainer">
		 <img src="../images/Accounter_logo_title.png" class="accounterLogo" />
		  <c:if test="${errormessage!=null}">
			<div id="login_success" class="common-box">
				<span>${errormessage}</span>
			</div>
  		  </c:if>
  		  
		 <form id = "emailActivationForm" action="/main/emailforactivation" method="post">
		    <div class="reset-header">
			   <h2>Resend activation code</h2>
			</div>
			<div>
			  <label>Enter registered Email-ID:</label>
			  <input id = "emailidBox" type="text" name="emailid">
			</div>
			<div class="reset-button">
			   <input type="submit" tabindex="3" value="Resend Activation Code" name="resend" class="allviews-common-button" id="submitButton">
			</div>
		 </form>
		 
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