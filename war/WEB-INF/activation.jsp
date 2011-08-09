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
		  <c:if test="${successmessage!=null}">
			<div id="login_success" class="common-box">
				<span>${successmessage}</span>
			</div>
  		  </c:if>
  		  
		 <form action="/activation" method="post">
		    <div class="reset-header">
			   <h2>Activation Code</h2>
			</div>
			<div>
			  <label>Enter valid activation code</label>
			  <input type="text" name="code">
			</div>
			<div class="reset-button">
			   <input type="submit" tabindex="3" value="Activate" name="activate" class="allviews-common-button" id="submitButton">
			</div>
		 </form>
		 <div>
		 	<a href="/emailforactivation">Resend activation code</a>
		 </div>
     </div>
	</body>
</html>