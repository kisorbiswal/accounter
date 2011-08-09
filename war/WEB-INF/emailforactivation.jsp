<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
  <head>
        <meta content="IE=100" http-equiv="X-UA-Compatible">
		<link rel="shortcut icon" href="../images/favicon.ico" />
		<% String version = application.getInitParameter("version"); %>
		<link type="text/css" href="../css/ss.css?version=<%= version%>" rel="stylesheet">
  </head>
    <body>
	    <div id="commanContainer">
		 <img src="../images/Accounter_logo_title.png" class="accounterLogo" />
		  <c:if test="${errormessage!=null}">
			<div id="login_success" class="common-box">
				<span>${errormessage}</span>
			</div>
  		  </c:if>
  		  
		 <form action="/emailforactivation" method="post">
		    <div class="reset-header">
			   <h2>Resend activation code</h2>
			</div>
			<div>
			  <label>Enter registered Email-ID:</label>
			  <input type="text" name="emailid">
			</div>
			<div class="reset-button">
			   <input type="submit" tabindex="3" value="Resend Activation Code" name="resend" class="allviews-common-button" id="submitButton">
			</div>
		 </form>
		 
     </div>
	</body>
</html>