<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
 "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>Get Support | Accounter
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible">
<% String version = application.getInitParameter("version"); %>
<link rel="shortcut icon" href="../images/favicon.ico" />
<link type="text/css" href="../css/ss.css?version=<%= version%>" rel="stylesheet">
<script type="text/javascript" src="/jscripts/jquery-1.6.2.js"></script>
<script src="/jscripts/jquery.validate.js" type="text/javascript"></script>
<link type="text/css" href="../css/cmxform.css?version=<%= version%>" rel="stylesheet">
<script  type="text/javascript" >
$.validator.setDefaults({
	submitHandler: function() {
		$('#contactform').submit();
	}
});
$.validator.addMethod("no_special_characters", function(value, element) {
    return this.optional(element) ||
        value.match(/^[a-zA-Z0-9@_ ]*$/);
}, "shouldn't contain any special characters");

	$(document).ready(function() {
	$('#blog-bg').click(function(event){
		event.preventDefault();
		$('.menu-bar').find('a').removeClass("header-hover");
		$(this).addClass("header-hover");
		window.open("http://blog.accounterlive.com/","_blank");
	});
		$('#submitButton').click(function() {
			$("#contactform").validate({
		rules: {
			name: {
				required: true,
				no_special_characters: true
			},
			subject: {
				required: true,
				no_special_characters: true
			},
			emailId: {
				required: true,
				email: true
			},
			message: {
				required: true,
			}
		},
		messages: {
			name: {
				required: "please enter your name",
				no_special_characters: "name should not contain any special characters"
			},
			emailId: "please enter a valid email",
			subject: {
				required: "please enter subject",
				no_special_characters: "subject should not contain any special characters"
			},
			message: {
				required: "please enter message",
				no_special_characters: "message should not contain any special characters"
			}
		}
	});
		});
	});	
</script>


</head>
<body>
	<div class ="body-container">
			<div class ="main_body-container">
			<div class ="header" id="top">
			
			<div class="menu-table">
			   <div class="acc_logo_image">
			       <div class="logo-image">
					  <a href="/"><img src="/images/Accounter_logo_title.png"></a>
				   </div>
			   </div>
			   <div class="menu-bar-login">
			      <div class="menu-bar">
				<ul>
					 <li>
					    <a href="/site/home" id="home-bg" >Home</a>
					 </li> 
					 <li>
					    <a href="/site/features" id="fea-bg">Features</a>
					 </li> 
					 <li>
						<a href="/site/support" id="sup-bg" class="header-hover">Support</a>
					</li>
					 <li>
					    <a href="http://blog.accounterlive.com/" id="blog-bg" target ="_blank" >Blog</a>
					 </li>
			  </ul>
				</div>
				<div class="remaining-header-part-login">
					<ul>
					   <li><a href="/main/login" id="login-id">Login</a></li>
					</ul>
				</div>
			   </div>
			</div>
			</div>
			<div class="middle-part" id="cen">
			  <div class="support_info_view">
				<div class="pricing-table" id="support-width">
					<div>
					<p class="sites-header">Support</p>
					</div>
					<div id="contact_cont" style="margin-top: 0px;">
				<div id="contact_content">
					<b>Our office address:</b><br>
						<br>
							&nbsp;&nbsp;&nbsp;Vimukti Technologies Private Limited,<br>
							&nbsp;&nbsp;&nbsp;Thirupathi Reddy Plaza,<br>
							&nbsp;&nbsp;&nbsp;Plot no.2, 4th floor,<br>
							&nbsp;&nbsp;&nbsp;G- Block Colony, Kapra,<br>
							&nbsp;&nbsp;&nbsp;Main Road, ECIL,<br>
							&nbsp;&nbsp;&nbsp;Hyderabad -500062<br>
				</div>
				<div id="contact_form_cont" class="contact_form_cont">
					<form id="contactform" name="contactform" method="post" action="/site/support">
						<fieldset class="fieldset">
						<c:if test="${message != null}">
								<span style="color: #3299A4; line-height: 1.5;">
								${message} </span>
						</c:if>
						<c:if test="${errormessage != null}">
								<span style="color: red; line-height: 1.5;">
								${errormessage} </span>
						</c:if>
							<table>
							 	<tr>
									 <td style="width : 100px">Name <span id=red>*</span> </td>
									 <td>
										<input id="mid-box"  type="text" tabindex="1" name="name">
					
									 </td>
								</tr>
								<tr>
									 <td style="width : 100px">Email <span id=red>*</span> </td>
									 <td>
										<input id="mid-box1"  type="text" tabindex="2" name="emailId">
					
									 </td>
								</tr>
								<tr>
									 <td style="width : 100px">Subject <span id=red>*</span> </td>
									 <td>
										<input id="mid-box2"  type="text" tabindex="3" name="subject">
					
									 </td>
								</tr>
								<tr>
									 <td style="width : 100px; vertical-align: top;">Message <span id=red>*</span> </td>
									 <td>
										<textarea class="support_textarea" id="message" name="message" style="margin-top : 10px;" tabindex="4"></textarea>
									 </td>
								</tr>
								<tr>
								   <td></td>
								   <td>
								      <ul class="signup-submit-button support_send">
								         <li><span class="signup-submit-left"></span></li>
								         <li><input id="submitButton"  class="signup-submit-mid" type="submit" value="Send"  tabindex="5"></li>
								         <li><span class="signup-submit-right"></span></li>
								      </ul>
								   </td>
								</tr>
							</table>
							
							</fieldset>
							</form>
					</div>

	</div>
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
		</div>
</body>
</html>