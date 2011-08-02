<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>Features | Accounter
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible">
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
<% String version = application.getInitParameter("version"); %>
<link rel="shortcut icon" href="../images/favicon.ico" />
<link type="text/css" href="../css/ss.css?version=<%= version%>" rel="stylesheet">

<link rel="stylesheet" type="text/css" href="../css/vertical-tabs-styles.css?version=<%= version%>">
<script type="text/javascript" src="/jscripts/jquery-1.6.2.js"></script>
<script type="text/javascript" src="/jscripts/vertical-tabs-script.js"></script>

<script type="text/javascript">
    // Load this script when page loads
    $(document).ready(function(){
	 // Set up a listener so that when anything with a class of 'tab' 
	 // is clicked, this function is run.
	 $('.tabContainer').find('a').click(function (event) {	
	   event.preventDefault();
	  // Remove the 'active' class from the active tab.
	  $('.left-side-options > .tabContainer > li').removeAttr('class');		  
	  // Add the 'active' class to the clicked tab.
	  $(this).parent().addClass('feature_active');
   });
 });
</script>
</head>
	<body>
		<div class ="body-container">
		<%@ include file="./header.jsp" %>
			<div class="middle-part" id="cen">
			 <div class="features_container">
				<div class="left-side-table" id="mid-left">
					<div class="left-side-options">
					  <ul  class="tabContainer">
				
	    <!-- The jQuery generated tabs go here -->

		</ul>			
					
					
				</div>
				</div>
				
				<div class="seperator"></div>
				<div class="Accounter-Business-Dashboard-table" id="mid-right">
				<div id="tabContent">
					
                     <div id="contentHolder">
        <!-- The AJAX fetched content goes here -->
              
                   </div>
					
					
					</div>
				</div>
				<div class="shadow-line"></div>
				</div>
			</div>
				<div class="down-test" id="down"></div>
			<%@ include file="./footer.jsp" %>
		</div>
		
<script type="text/javascript">

var _gaq = _gaq || [];
_gaq.push(['_setAccount', 'UA-24502570-1']);
_gaq.push(['_trackPageview']);

(function() {
var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
})();

</script>
</body>
</html>