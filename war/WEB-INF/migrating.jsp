<html>
<head>
<%@ include file="./feedback.jsp" %>
<link type="text/css" href="../css/ss.css" rel="stylesheet">
<script type="text/JavaScript">
function timedRefresh(timeoutPeriod) {
	setTimeout("location.reload(true);",timeoutPeriod);
}
</script>
<style type="text/css">
        body { overflow:hidden; text-align: center; }
</style>
</head>
<body onload="timedRefresh(3000);">
<span style="color:#000000;line-height:38.5;">Apologies for the delay, Bizantra is migrating your data to a newer version. Please do not close the application.</span>

<script type="text/javascript" charset="utf-8">
			var is_ssl = ("https:" == document.location.protocol);
			var asset_host = is_ssl ? "https://s3.amazonaws.com/getsatisfaction.com/" : "http://s3.amazonaws.com/getsatisfaction.com/";
			document.write(unescape("%3Cscript src='" + asset_host + "javascripts/feedback-v2.js' type='text/javascript'%3E%3C/script%3E"));
		</script>
		 
</body>
</html>
