<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
 "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title> Terms and Conditions | Accounter
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
				<div class="pricing-table" id="mid-2">
					<div>
						<p class="sites-header">Terms and Conditions</p>
					</div>
					<div class="adjust-padding" style="margin-top:25px;">
						<p>&nbsp&nbsp&nbsp&nbsp&nbsp;&nbsp&nbsp&nbsp&nbsp&nbsp;These terms and conditions page describes the terms upon which you may access and use our Site. You affirm that you have read and accept all of the following terms and conditions, which may be updated by us from time to time without notice  by visiting our Site,.</p>	
					</div>
					<br>
					<div class="adjust-padding">
						<p>&nbsp&nbsp&nbsp&nbsp&nbsp;&nbsp&nbsp&nbsp&nbsp&nbsp;Accounter provides online accounting software as a service. We reserve the right to change our minds about things (like providing the Service for free). We own the copyrights, trademarks & all other intellectual property of Accounter. In our discretion, we may provide notices of changes to these terms and conditions or other matters by displaying notices or links to notice generally on our Site.</p>
					</div>
					<br>
					<div class="adjust-padding">
						<p>&nbsp&nbsp&nbsp&nbsp&nbsp;&nbsp&nbsp&nbsp&nbsp&nbsp;IN NO EVENT WILL THE SOFTWARE OWNERS / DESIGNERS, DISTRIBUTORS, PROGRAMMERS, OR ANY RELATED BUSINESSES, ENTITY, OR ORGANISATION, OR ITS SUPPLIERS BE LIABLE FOR ANY SPECIAL, CONSEQUENTIAL, INDIRECT, INCIDENTAL OR SIMILAR DAMAGES WHATSOEVER, INCLUDING ANY LOST PROFITS, BUSINESS INTERRUPTION, OR ANY OTHER PECUNIARY LOSS, ARISING OUT OF THE USE OR THE INABILITY TO USE THE SOFTWARE OR THE PROVISION OF OR FAILURE TO PROVIDE SUPPORT SERVICES, EVEN IF THE SOFTWARE OWNERS / DESIGNERS, DISTRIBUTORS, PROGRAMMERS, OR ANY RELATED BUSINESSES, ENTITY, OR ORGANISATION HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES. EVEN IF ANY INTERESTED PARTY HAS BEEN ADVISED OF ANY PROBLEMS, FAULTS OR ERRORS IN THE SOFTWARE PRODUCT.</p>
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