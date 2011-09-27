<html>
<head>
<title>  Accounter Under Maintanance
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible">

<link rel="shortcut icon" href="../images/favicon.ico" />
<% String version = application.getInitParameter("version"); %>
<script type="text/javascript" src="/jscripts/jquery-1.6.2.js"></script>
<script src="/jscripts/jquery.validate.js" type="text/javascript"></script>
<link type="text/css" href="../css/ss.css?version=<%= version%>" rel="stylesheet">
</head>
<body>
<%@ include file="./feedback.jsp" %>
  <div id="maintananceContainer">
   <div class="maintanance_subcont">
    <img src="../images/Accounter_logo_title.png" class="accounterLogo" />
    <div class="unavailable_page">
      <div class="maintainence_logo">
        <img src="../images/maintainence_show.jpg" class="accounterLogo" /> 
      </div>
      <div class="maintainence_message">
	    <h3>Sorry! Your Company is Under Migration</h3>
	    <h5>We are performing migration on your company to provide you better service of Accounter.</h5>
	    <span> Please visit again later.For more info, visit </span>
	    <a  href="www.blog.accounterlive.com" style="color:RoyalBlue">blog.accounterlive.com</a>
	    
	</div>
   </div>
   </div>
  </div>
</body>
</html>