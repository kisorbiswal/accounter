
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
    <script type="text/javascript" src="/jscripts/jquery-1.6.2.js"></script>
    <script src="/jscripts/jquery.validate.js" type="text/javascript"></script>
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
    <title>Bizantra</title>
    
    <!--CSS for loading message at application Startup-->
    <style type="text/css">
        body { overflow:hidden }
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
	 background-color:#f2f3f5;
	 padding:10px;
	 border:1px solid #000;
	 width:600px;
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
	background-color:#e9edf0;
	margin:5px 0px;
	width:300px;
}

.fieldset table td{
	float:left;
}
.login img[src="logo.png"]{
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
<!--add loading indicator while the app is being loaded-->
<%@ include file="./feedback.jsp" %>
	<div id="loadingWrapper">
		<div id="loading">
		    <div class="loadingIndicator">
		        <img src="/images/icons/loader.gif" width="32" height="32" style="margin-right:8px;float:left;vertical-align:top;"/>Bizantra Application<br/>
		        <span id="loadingMsg">Loading styles and images...</span>
		    </div>
		</div>
	</div>
  
  	<script type="text/javascript">document.getElementById('loadingMsg').innerHTML = 'Loading Skin...';</script>
	<!--include the application JS-->
	<script type="text/javascript">document.getElementById('loadingMsg').innerHTML = 'Loading Bizantra Application<br>Please wait...';</script>
	
	<script type="text/javascript" language="javascript" src="/bizantra/bizantra.nocache.js"></script>
    <!-- OPTIONAL: include this if you want history support -->
    <iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1' style="position:absolute;width:0;height:0;border:0"></iframe>
    <iframe id="__printingFrame" style="width: 0; height: 0; border: 0"></iframe>
	<jsp:include page="/meteor.jsp"></jsp:include>
	<iframe id="__printingFrame" style="width: 0; height: 0; border: 0"></iframe>
 	