
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!-- The HTML 4.01 Transitional DOCTYPE declaration-->
<!-- above set at the top of the file will set     -->
<!-- the browser's rendering engine into           -->
<!-- "Quirks Mode". Replacing this declaration     -->
<!-- with a "Standards Mode" doctype is supported, -->
<!-- but may lead to some differences in layout.   -->

<html>


  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <script type="text/javascript">
    var tabsEnabled=["Hr","Finance","Operations","Marketing","Sales","Users","Workflows","Purchases"];
    var helpurl="${helpUrl}";
    </script>
	<script  type="text/javascript" >
	function loadImage(){
		document.getElementById("hiddenDiv").style.display="block";
    		document.getElementById("login").style.display="none";
    		
	}
	
</script>
    <!--                                                               -->
    <!-- Consider inlining CSS to reduce the number of requested files -->
    <!--                                                               -->
    <link type="text/css" rel="stylesheet" href="../css/Defbiz.css">
	<link type="text/css" rel="stylesheet" href="../css/Finance.css">
	<link type="text/css" rel="stylesheet" href="../css/calendar.css">

    <!--                                           -->
    <!-- Any title is fine                         -->
    <!--                                           -->
    <title>Accounter</title>
    
    <!--CSS for loading message at application Startup-->
    <style type="text/css">
        body { overflow:hidden; text-align: center; }
        #loading {
            border: 1px solid #ccc;
            position: absolute;
            left: 45%;
            top: 40%;
            padding: 2px;
            z-index: 20001;
            height: auto;
        }

        #loading a {
            color: #225588;
        }
        

        #loading .loadingIndicator {
            background: white;
            font: bold 13px tahoma, arial, helvetica;
            padding: 10px;
            margin: 0;
            height: auto;
            color: #444;
        }

        #loadingMsg {
            font: normal 10px arial, tahoma, sans-serif;
        }
      
.login{
	 margin:140px auto;
	 background-color:#FFF;
	 padding:10px;
	 border:1px solid #4D2059;
	 width:600px;
	 text-align: left;
 }
 
 body{
 background:LightSteelBlue none repeat scroll 0 0;
 }
 
.image{
 	text-align:center;
 }
 
.clearfix:after{
	 content:".";
	 display:block;
	 height:0;
	 clear:both;
	 visibility:hidden;
	 }
.fieldset{
	border:1px solid #000;
	padding:10px;
	background-color:#e1e0d6;
	margin:5px 0px;
	width:300px;
}

.fieldset table td{
	float:left;
}
.login img[src="bizantraLogo.png"]{
	text-align:center;
}
.login img[src="lock.png"]{
	float:left;
}
p{
	text-align:left;
	clear:left;
	line-height:30px;
	margin:0px;
}
.loginbox{
	float:right;
	
}
.loginlabel{
	font-weight:bold;
	color:#c64933;
}
.loginbox a{
	font-weight:bold;
	color:#0000ff;
	margin-left:20px;
	text-decoration:none;
}

#bizantraForm input[type="text"],#bizantraForm input[type="password"]{
	border:1px solid #9cb0ce;
}

.loginbox a:hover{
	text-decoration:underline;
}
.hiddenPic {display:none; float:left;margin-top:25%;margin-left:50%;}
#import_anchor{
 color:blue;
font-weight:normal;
margin-bottom:0;
margin-left:0;
margin-right:0;
margin-top:0;
text-decoration:underline;
}
#Import_Account{
margin-left:200px;
}
    </style>
    
    

  </head>

  <!--                                           -->
  <!-- The body can have arbitrary html, or      -->
  <!-- you can leave the body empty if you want  -->
  <!-- to create a completely dynamic UI.        -->
  <!--                                           -->
  <body onload="bizantraForm.emailId.focus();">
  
<div id="hiddenDiv" class="hiddenPic">
		<img src="/images/icons/loading-indicator.gif" 
  			alt="Loading" title="Loading" 
    			height="50" width="50" >
    		</div>
<div class="login clearfix" id="login" >
		<img src='/images/bizantraLogo.png'/>
		<div class="loginbox">
		<form id="bizantraForm" method="post" action="login" onsubmit="return loadImage()">
			<fieldset class="fieldset">
			<c:if test="${message != null}">
				<span style="background: #CC0000; color: #F2F3F5; line-height: 1.5;">
				${message} </span>
			</c:if>
				<table>
		<tr>
			<td>Accounter ID :</td>
		</tr>
		<tr>
			<td><input type="text" name="emailId" /></td>
		</tr>
		<tr>
			<td>Password :</td>
		</tr>
		<tr>
			<td><input type="password" name="password" /></td>
		</tr>
		<c:if test="${companyName == null}">
			<tr>
				<td>Company Name :</td>
			</tr>
			<tr>
				<td><input type="text" name="companyName" /></td>
			</tr>
		</c:if>
		<tr>
			<!--<td><input type="checkbox" name="PersistentCookie" />Remember
			my ID on this system</td>-->
		</tr>
		<tr>
			<td><input type="submit" name="login" value="Login" /></td>
		</tr>
	</table>
		</fieldset>
		</form>
		</div>
		<p>
		Welcome to Accounter!<br>
		</p>
	</div>
	
	<script type="text/javascript" charset="utf-8">
			var is_ssl = ("https:" == document.location.protocol);
			var asset_host = is_ssl ? "https://s3.amazonaws.com/getsatisfaction.com/" : "http://s3.amazonaws.com/getsatisfaction.com/";
			document.write(unescape("%3Cscript src='" + asset_host + "javascripts/feedback-v2.js' type='text/javascript'%3E%3C/script%3E"));
		</script>
		<script type="text/javascript" src="/jscripts/jquery-1.6.2.js"></script>
		<script src="/jscripts/jquery.validate.js" type="text/javascript"></script>
		<%@ include file="./feedback.jsp" %>
 </body>
</html>
