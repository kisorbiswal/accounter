<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!-- The HTML 4.01 Transitional DOCTYPE declaration-->
<!-- above set at the top of the file will set     -->
<!-- the browser's rendering engine into           -->
<!-- "Quirks Mode". Replacing this declaration     -->
<!-- with a "Standards Mode" doctype is supported, -->
<!-- but may lead to some differences in layout.   -->

<html>

  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta content="IE=100" http-equiv="X-UA-Compatible">
    <script type="text/javascript" charset="utf-8">
  		var is_ssl = ("https:" == document.location.protocol);
  		var asset_host = is_ssl ? "https://s3.amazonaws.com/getsatisfaction.com/" : "http://s3.amazonaws.com/getsatisfaction.com/";
	</script>
		
    <% String version = application.getInitParameter("version"); %>
    <!--<script type="text/javascript" src="https://getfirebug.com/firebug-lite.js"></script>-->

    <script type="text/javascript">
    var tabsEnabled=["Hr","Finance","Operations","Marketing","Sales","Users","Workflows","Purchases"];
    var helpurl="${helpUrl}";
    </script>
    <script src="/jscripts/jquery-1.6.2.js" type="text/javascript"></script>
    <script src="/jscripts/jquery.validate.js" type="text/javascript"></script>
    <script type="text/javascript" src="/jscripts/jquery.contactable.packed.js"></script>
   
    <!--                                                               -->
    <!-- Consider inlining CSS to reduce the number of requested files -->
    <!--                                                               -->
	<link type="text/css" rel="stylesheet" href="../css/Finance.css?version=<%= version%>">
	<link type="text/css" rel="stylesheet" href="../css/calendar.css?version=<%= version%>">
	 
	<%
   String app = request.getHeader( "Nativeapp" );
   boolean isNative = ( app != null && !app.equals(""));
   if( isNative ){ %>
   <link type="text/css" rel="stylesheet" href="../css/native.css?version=<%= version%>">
   <% } %>
	
    <!--                                           -->
    <!-- Any title is fine                         -->
    <!--                                           -->
    <title>Accounter</title>
    
    <!--CSS for loading message at application Startup-->
    <style type="text/css">
        #loading {
            border: 1px solid #ccc;
            position: absolute;
            left: 42%;
            top: 35%;
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
			line-height:1.5;
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
			
		.hiddenPic {
			display:none; 
			float:left;
			margin-top:25%;
			margin-left:50%;
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
  <body>
  <div id="contact" style="visibility:hidden"> </div>
<div id="hiddenDiv" class="hiddenPic">
		<img src="/images/loader.gif" 
  			alt="Loading" title="Loading">
    		</div>

  	<!--add loading indicator while the app is being loaded-->
	<div id="loadingWrapper" style="visibility:visible">
	<div id="loading">
	    <div class="loadingIndicator">
	        <img src="/images/Main-page-loading-bar.gif" /><br/>
	    </div>
	</div>
	</div>
  
  	<script type="text/javascript">document.getElementById('loadingMsg');</script>
	<!--include the application JS-->
	<script type="text/javascript">document.getElementById('loadingMsg');</script>
	
	<!--<table class="header" id="mainHeader" style="visibility:hidden">
	   <tr>
	      <td width="25%"><img src="/images/Accounter_logo_title.png" /></td>
	      <td width="50%"><div class="companyName">${companyName}</div></td>
	      <td width="25%">
	        <ul>
	           <li><img src="images/User.png" /><a href="">${userName}</a></li>
	           <li><img src="images/Help.png" /><a href='http://help.accounter.com'>Help</a></li>
	           <li><img src="images/logout.png" /><a href='/main/logout'>Logout</a></li>
	        </ul>
	      </td>
	   </tr>
	</table>-->
	<div id="mainWindow"></div>
	<script type="text/javascript" language="javascript" src="/accounter/accounter.nocache.js"></script>
	<div id="mainFooter" style="visibility:hidden" >
	    <div>
	       <span>&copy 2011 Vimukti Technologies Pvt Ltd</span> |
	       <a target="_blank" href="/site/termsandconditions"> Terms & Conditions </a> |
	       <a target="_blank" href="/site/privacypolicy"> Privacy Policy </a> |
	       <a target="_blank" href="/site/support"> Support </a>
	    </div>
	</div>
	
    <!-- OPTIONAL: include this if you want history support -->
    <iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1' style="position:absolute;width:0;height:0;border:0"></iframe>
    <iframe id="__printingFrame" style="width: 0; height: 0; border: 0"></iframe>
	<jsp:include page="/WEB-INF/meteor.jsp"></jsp:include>
	<iframe id="__printingFrame" style="width: 0; height: 0; border: 0"></iframe>
	 <iframe id="__printingFrame" style="width: 0; height: 0; border: 0">
        </iframe>
        <iframe id="__generatePdfFrame" style="width: 0; height: 0; border: 0">
           
        </iframe>
        
<script type="text/javascript">
$(document).ready(function(){
	$(function(){
		$('#contact').contactable({
	recipient: 'test@test.com',
	subject: 'A Feeback Message'
});
});
})

	var _gaq = _gaq || [];
		_gaq.push(['_setAccount', 'UA-24502570-1']);
		_gaq.push(['_trackPageview']);
	
	(function() {
		var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
		ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
		var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
	})();

	</script>

		
		<script type="text/javascript" charset="utf-8">
			if (typeof accounter != 'undefined'){
				accounter.reloadMenu();
			}
		</script>
		
		<script type="text/javascript" charset="utf-8">
			function SampleFunction(parameter){
				parameter.reloadMenu();
			}
		</script>

	<script type="text/javascript" charset="utf-8">
			var is_ssl = ("https:" == document.location.protocol);
			var asset_host = is_ssl ? "https://s3.amazonaws.com/getsatisfaction.com/" : "http://s3.amazonaws.com/getsatisfaction.com/";
		</script>
		
		<script type="text/javascript" charset="utf-8">
			
			function MacReload(){
					window.location = '/macreload';
			}
  		</script>
		

 </body>
</html>
