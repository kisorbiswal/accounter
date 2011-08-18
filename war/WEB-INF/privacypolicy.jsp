<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
 "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title> Privacy Policy | Accounter
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible">
<% String version = application.getInitParameter("version"); %>
<link rel="shortcut icon" href="../images/favicon.ico" />
<link type="text/css" href="../css/ss.css?version=<%= version%>" rel="stylesheet">
<script type="text/javascript" src="/jscripts/jquery-1.6.2.js"></script>
</head>
<body>
	<div class ="body-container">
		<%@ include file="./header.jsp" %>
			<div class="middle-part" id="cen">
				<div class="pricing-table" id="privacy-layout">
					<div>
						<p class="sites-header">Privacy Policy</p>
					</div>
					<div class="adjust-padding" style="margin-top:25px;">
						<p>&nbsp&nbsp&nbsp&nbsp&nbsp;Your privacy is important to us. To better protect your privacy we provide this notice explaining our online information practices and the choices you can make about the way your information is collected and used. </p>	
					</div>
					<br>
					<div class="adjust-padding">
						<p>&nbsp&nbsp&nbsp&nbsp&nbsp;In Accounter & in our business model we deal with Accounter & all its affiliates, you share personal and financial information with us. We guarantee you that this information, which is provided by you will be kept confidential. We assure you that all the information will be protected. </p>
					</div>
					<br>
					<div class="adjust-padding">
						<p>&nbsp&nbsp&nbsp&nbsp&nbsp;The information you provide when communicating or transacting with us will never be leaked anywhere. We do not sell personal information about current or former customers to any third parties, and we do not disclose it to third parties unless necessary to process a transaction, service an account or as otherwise permitted by law.</p>
					</div>
				</div>
			</div>
		<div class="down-test" id="down">
		</div>
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