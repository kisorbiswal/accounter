<html>
  <head>
        <meta content="IE=100" http-equiv="X-UA-Compatible">
		<link rel="shortcut icon" href="../images/favicon.ico" />
		<% String version = application.getInitParameter("version"); %>
		<link type="text/css" href="/css/ss.css?version=<%= version%>" rel="stylesheet">
  </head>
    <body>
	    <div class ="body-container">
	      <div class="main_body-container">
		       <%@ include file="./header.jsp" %>
			    <div id='cen' class="middle-part">
			       <div id='mid-1' class="middle-signup-box" >
			         <div class="reset_activation_code">
			            <h2>Reset Activation Code</h2>
			            <div class="activation_code_box">
							<form action="/activation" method="post">
							    <table width="100%">
								  <tr>
								    <td><span>Enter valid activation code:</span></td>
								    <td><input type="text" name="code"></td>
								  </tr> 
								  <tr>
								    <td></td>
								    <td>
								     <ul class="activate-ok-button">
										<li><span class="signup-submit-left"></span></li>
										<li><input type="submit" tabindex="3" value="Activate" name="activate" class="signup-submit-mid forget-but" id="submitButton"></li>
										<li><span class="signup-submit-right"></span></li>
									 </ul>
									</td>
								  </tr>
							   </table>
							</form>
						  </div>
					  </div>
				  </div>
				</div>
				<%@ include file="./footer.jsp" %>
			</div>
	   </div>
	</body>
</html>