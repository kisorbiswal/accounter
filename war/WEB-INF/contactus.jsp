<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
 "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title> Contact Us | Accounter
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible">

<% String version = application.getInitParameter("version"); %>
<link rel="shortcut icon" href="../images/favicon.ico" />
<link type="text/css" href="../css/ss.css?version=<%= version%>" rel="stylesheet">
<script type="text/javascript" src="/jscripts/jquery-1.6.2.js"></script>
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
</head>
<body>
<%@ include file="./feedback.jsp" %>
	<div class ="body-container">
		<%@ include file="./header.jsp" %>
			<div class="middle-part" id="cen">
				<div class="pricing-table" id="contact-layout">
					<div>
						<p class="sites-header">Contact Us</p>
					</div>
					<div style="margin:30px;">
						<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Vimukti Technologies Private Limited,<br>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Thirupathi Reddy Plaza,<br>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Plot no.2, 4th floor,<br>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;G- Block Colony, Kapra,<br>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Main Road, ECIL, Hyderabad<br>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Andhra Pradesh, India -500062<br>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Phone: +91-9989696513<br>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Phone: +91-40-42005090
						</p>	
					</div>
				</div>
			</div>
		<div class="down-test" id="down">
		</div>
		<%@ include file="./footer.jsp" %>
	</div>
	
	<script type="text/javascript" charset="utf-8">
			var is_ssl = ("https:" == document.location.protocol);
			var asset_host = is_ssl ? "https://s3.amazonaws.com/getsatisfaction.com/" : "http://s3.amazonaws.com/getsatisfaction.com/";
			document.write(unescape("%3Cscript src='" + asset_host + "javascripts/feedback-v2.js' type='text/javascript'%3E%3C/script%3E"));
		</script>
		 
</body>
</html>