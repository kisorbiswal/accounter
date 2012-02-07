<%@ page import="java.util.*" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="i18n" uri="/WEB-INF/i18n.tld"%>
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
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <link rel="shortcut icon" href="/images/favicon.ico" />
    <meta content="IE=100" http-equiv="X-UA-Compatible" />
    <%@ include file="./feedback.jsp" %>
    <script type="text/javascript" charset="utf-8">
  		var is_ssl = ("https:" == document.location.protocol);
  		var asset_host = is_ssl ? "https://s3.amazonaws.com/getsatisfaction.com/" : "http://s3.amazonaws.com/getsatisfaction.com/";
  		
  		var AccounterMessages={
  		<%
  		HashMap<String,String> messages=(HashMap<String,String>)request.getAttribute("messages");
  		if(messages!=null){
	  		for(String key:messages.keySet()){
	  			String value=messages.get(key);
	  				%>'<%= key %>': '<%= value %>',
	  				<%
	  		}
  		}
  		%>
  			last: 'end'
  		};
  		
  		var accounter_locale={
  		<%
  		HashMap<String,String> accounterLocale=(HashMap<String,String>)request.getAttribute("accounterLocale");
  		if(accounterLocale!=null){
	  		for(String key:accounterLocale.keySet()){
	  			String value=accounterLocale.get(key);
	  			if(value.startsWith("['")){
	  				%>'<%= key %>': <%= value %>,
	  				<%	  			
	  			}else{
	  				%>'<%= key %>': '<%= value %>',
	  				<%
	  			}
  			}
  		}
  		%>
  			last: 'end'
  		};
		<%
			boolean isRTL=(Boolean) request.getAttribute("isRTL");
  		%>
		var isRTL=<%= isRTL %>;
		document.body.style.direction=isRTL?"rtl":"ltr";
	</script>
		
    
    <!--<script type="text/javascript" src="https://getfirebug.com/firebug-lite.js"></script>-->

    <script type="text/javascript">
    var tabsEnabled=["Hr","Finance","Operations","Marketing","Sales","Users","Workflows","Purchases"];
    var helpurl="${helpUrl}";
    
    </script>
    <!--                                                               -->
    <!-- Consider inlining CSS to reduce the number of requested files -->
       
	<link type="text/css" rel="stylesheet" href="../css/calendar.css?version=<%= version%>" />
	 
	<%
   String app = request.getHeader( "Nativeapp" );
   boolean isNative = ( app != null && !app.equals(""));
    Boolean isIpad =  (Boolean)request.getSession().getAttribute("IpadApp");
   if( isNative){ %>
   	<link type="text/css" rel="stylesheet" href="../css/native.css?version=<%= version%>" />
   <% } else if(isIpad != null && isIpad ){%>
  	 <link type="text/css" rel="stylesheet" href="../css/TabletClientcss.css?version=<%= version%>" />
  <%}
   %>
	
	
    <!--                                           -->
    <!-- Any title is fine                         -->
    <!--                                           -->
    <title>Accounter</title>
    
    <!--CSS for loading message at application Startup-->
    <style type="text/css">
        #loading {
            position: absolute;
            left: 42%;
            top: 35%;
            padding: 2px;
            z-index: 20001;
            height: auto;
            border: 1px solid #CCCCCC;
            box-shadow: 0 0 10px #1A5158;
            background: none repeat scroll 0 0 white;
            border-radius: 10px 10px 10px 10px;
        }

        #loading a {
            color: #225588;
        }
        #loading .loadingIndicator div{
                color: #1A5158;
			    font-size: 20px;
			    font-weight: normal;
			    padding-left: 10px;
        }
        #loading .loadingIndicator {
			 color: #444444;
			 font-family: tahoma,arial,helvetica;
			 font-weight: bold;
			 height: auto;
			 line-height: normal;
			 margin: 0;
			 padding: 10px;
            
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
<div id="hiddenDiv" class="hiddenPic">
		<img src="/images/loader.gif" 
  			alt="Loading" title="Loading" />
    		</div>

  	<!--add loading indicator while the app is being loaded-->
	<div id="loadingWrapper" style="visibility:visible">
	<div id="loading">
	    <div class="loadingIndicator">
	       <div class="loadingIndicator">
             <div><i18n:i18n msg='loadingAccounter'/>,</div>
	         <img src="/images/Main-page-loading.gif" alt="Main page">
             <div><i18n:i18n msg='pleasewait'/>....</div>
	      </div>
	    </div>
	</div>
	</div>
  
  	<script type="text/javascript">document.getElementById('loadingMsg');</script>
	<!--include the application JS-->
	<script type="text/javascript">document.getElementById('loadingMsg');</script>
	
	<!--<table class="header" id="mainHeader" style="visibility:hidden">
	   <tr>
	      <td width="25%"><img src="/images/Accounter_logo_title.png" alt="Accounter logo"/></td>
	      <td width="50%"><div class="companyName">${companyName}</div></td>
	      <td width="25%">
	        <ul>
	           <li><img src="/images/User.png" /><a href="">${userName}</a></li>
	           <li><img src="/images/Help.png" /><a href='http://help.accounter.com'>Help</a></li>
	           <li><img src="/images/logout.png" /><a href='/main/logout'>Logout</a></li>
	        </ul>
	      </td>
	   </tr>
	</table>-->
	<div id="mainWindow"></div>
			<script type="text/javascript" charset="utf-8">
			function MacReload(){
				 if(window.macclient){
				 	window.macclient.reloadMenu();
				 }
			}
  		</script>
  		<script type="text/javascript" charset="utf-8">
			function ClearMacMenu(){
				 if(window.macclient){
				 	window.macclient.clearMenu();
				 }
			}
  		</script>
	<script type="text/javascript" language="javascript" src="/accounter.client/accounter.client.nocache.js"></script>

	<div id="mainFooter" style="visibility:hidden" >
	    <div>
	       <span>&copy 2011 Vimukti Technologies Pvt Ltd</span> |
	       <a target="_blank" href="/site/termsandconditions"> Terms & Conditions </a> |
	       <a target="_blank" href="/site/privacypolicy"> Privacy Policy </a> |
	       <a id="support" target="_blank" href="/site/support"> Support </a>
	    </div>
	    <script type="text/javascript" >
	    <% 
			Long subscription=(Long)request.getAttribute("subscription");
			String goPId=(String)request.getAttribute("goPremiumId");
		%>
			var subscription=<%= subscription %>;
			var goPId="<%= goPId %>";
		    if(subscription==null){
				$("#support").after('|<a target="_blank" href="/site/subscription/gopremium?email_enc='+goPId+'">Go Premium</a>'); 
			}else{
				$("#support").after('|<a target="_blank" href="/site/subscriptionmanagement">Subscription Management</a>'); 
			}
	    </script>
	</div>
	
    <!-- OPTIONAL: include this if you want history support -->
    <iframe src="javascript:''" id="__gwt_historyFrame" style="position:absolute;width:0;height:0;border:0"></iframe>
	 <iframe id="__printingFrame" style="width: 0; height: 0; border: 0">
        </iframe>
        <iframe id="__generatePdfFrame" style="width: 0; height: 0; border: 0">
           
        </iframe>
        
 
	<%@ include file="./scripts.jsp" %>

 </body>
</html>
