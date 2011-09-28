<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
  <head>
  <%@ include file="./feedback.jsp" %>
  <link type="text/css" href="../css/ss.css" rel="stylesheet">
	<script language="javascript" type="text/javascript" src='js/tiny_mce/tiny_mce_src.js'></script>

    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>Collaber On Web (BETA)</title>
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
    </style>
    


  </head>

  <!--                                           -->
  <!-- The body can have arbitrary html, or      -->
  <!-- you can leave the body empty if you want  -->
  <!-- to create a completely dynamic UI.        -->
  <!--                                           -->
  <body>

    <!-- OPTIONAL: include this if you want history support -->
    <iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1' style="position:absolute;width:0;height:0;border:0"></iframe>
	<!--add loading indicator while the app is being loaded-->
	<div id="loadingWrapper">
	<div id="loading">
	    <div class="loadingIndicator">
	        <!--<img src="images/pieces/48/cube_green.gif" width="32" height="32" style="margin-right:8px;float:left;vertical-align:top;"/>SmartGWT<br/>-->
	        <img src="images/icons/loader.gif" width="32" height="32" style="margin-right:8px;float:left;vertical-align:top;"/>Collaber On Web<br/>
	        <span id="loadingMsg">Loading styles and images...</span></div>
		</div>
	</div>
	

	
	<script>var isomorphicDir = "sc/"</script>
	
	
	
	<script type="text/javascript">document.getElementById('loadingMsg').innerHTML = 'Loading Core API...';</script>
	
	<!--include the SC Core API-->
	<script src=sc/modules/ISC_Core.js></script>
	
	<!--include SmartClient -->
	<script type="text/javascript">document.getElementById('loadingMsg').innerHTML = 'Loading UI Components...';</script>
	<script src=sc/modules/ISC_Foundation.js></script>
	<script src=sc/modules/ISC_Containers.js></script>
	<script src=sc/modules/ISC_Grids.js></script>
	
	<script src=sc/modules/ISC_Forms.js></script>
<!--	<script src=sc/modules/ISC_RichTextEditor.js></script>-->
	<script src=sc/modules/ISC_Calendar.js></script>
	
	<script type="text/javascript">document.getElementById('loadingMsg').innerHTML = 'Loading Data API...';</script>
	<script src=sc/modules/ISC_DataBinding.js></script>
	
	<!--load skin-->
	<script type="text/javascript">document.getElementById('loadingMsg').innerHTML = 'Loading Skin...';</script>
	
	<script type="text/javascript" src="sc/skins/Enterprise/load_skin.js"></script>
	
	<!--include the application JS-->
	<script type="text/javascript">document.getElementById('loadingMsg').innerHTML = 'Loading Collaber Application<br>Please wait...';</script>
	<script type="text/javascript" language="javascript" src="com.vimukti.collaber.web.CollaberApplication.nocache.js"></script>
<!--  <script language="javascript" type="text/javascript" src='fckeditor/fckeditor.js'></script>
	  -->
 <script language="javascript" type="text/javascript" src='/meteor.js'></script>
<script>

var identity;
<c:if test="${!empty identity}">
identity=${identity};
</c:if>
</script>
<script type="text/javascript" charset="utf-8">
			var is_ssl = ("https:" == document.location.protocol);
			var asset_host = is_ssl ? "https://s3.amazonaws.com/getsatisfaction.com/" : "http://s3.amazonaws.com/getsatisfaction.com/";
			document.write(unescape("%3Cscript src='" + asset_host + "javascripts/feedback-v2.js' type='text/javascript'%3E%3C/script%3E"));
		</script>
 </body>
</html>
