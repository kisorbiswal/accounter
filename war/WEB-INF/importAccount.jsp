
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<html>


  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">

    <!--                                           -->
    <!-- Any title is fine                         -->
    <!--                                           -->
    <title>Bizantra - Importing Account</title>
    
    <!--CSS for loading message at application Startup-->
    <script type="text/javascript">
    	function loadImage(){
    		document.getElementById("hiddenDiv").style.display="block";
    		document.getElementById("login").style.display="none";
    		return true;
    	}
    </script>
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
	 background-color:#FFFFFF;
	 padding:30px;
	 border:1px solid #000;
width:600px; }
 
 body{
 background:#d8dce0 none repeat scroll 0 0;
 font-size: 10pt;
 font-family:Arial Unicode MS,Arial,sans-serif;
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
fieldset{
	border:1px solid #000;
	padding:10px;
	background-color:#e1e0d6;
	margin:5px 0px;
	width:300px;
}

fieldset table td{
	/*float:left;*/
	table-layout:fixed;
white-space:nowrap;
width:204px;
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
#import_anchor{
 color:blue;
font-weight:normal;
margin-bottom:0;
margin-left:0;
margin-right:0;
margin-top:0;
text-decoration:underline;
}
.loginbox{
	float:right;
	width:350px;
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

#importform input[type="text"],#importform input[type="password"]{
	border:1px solid #9cb0ce;
}

#Import_Account{
text-align: right;
padding-right: 5px;
}

.loginbox a:hover{
	text-decoration:underline;
}
.hiddenPic {display:none; width:400px;margin:25% auto 0;text-align:center}
    </style>
    
    

  </head>

  <!--                                           -->
  <!-- The body can have arbitrary html, or      -->
  <!-- you can leave the body empty if you want  -->
  <!-- to create a completely dynamic UI.        -->
  <!--                                           -->
  <body>

<div id="hiddenDiv" class="hiddenPic">
<span style="color:#000000;line-height:1.5;">Apologies for the delay, Bizantra is creating your local database.<br><br></span>
		<img src="/images/icons/loading-indicator.gif" 
  			alt="Loading" title="Loading" 
    			height="50" width="50" >
    		</div>
<div class="login clearfix" id="login">
		<img src="/images/bizantraLogo.png"/>
		<div class="loginbox">
		<form id="importform" method="post" action="importidentity" onsubmit="return loadImage()">
			<fieldset>
			 <legend >Import Account</legend>
			<table>
				<tr>
				<c:if test="${message != null}" >
				<span style="background:#CC0000;color:#F2F3F5;line-height:1.5;"> ${message} </span>	
				</c:if>
				</tr>
			    <tr><td>Bizantra ID :</td>
			    <td ><input type="text" name="emailId"/></td></tr>
			    <tr><td>Password : </td>
			    <td ><input type="password" name="password"/></td></tr>
		       <tr><td>
			    Server :</td><td><input type="text" name="companyUrl"/>.bizantra.com
			    </td></tr>
			    <tr align="right"><td colspan="2"><input type="submit" name="login" value="Import"/><input type="button" onclick="window.location='/main/login';" value="Cancel"/>
			    </td></tr>
			</table>
		</fieldset>
		</form>
		</div>
		<p>
		Welcome to Bizantra!<br>
		</p>
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
