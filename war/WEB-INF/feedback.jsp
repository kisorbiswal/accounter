<% 
String version = application.getInitParameter("version");
String isTouch = (String)request.getAttribute( "isTouch" );
String emailId = (String)request.getSession().getAttribute( "emailId" );
%>
<script src="/jscripts/jquery-1.7.min.js" type="text/javascript"></script>
<script src="/jscripts/jquery.validate.js" type="text/javascript"></script>

<%
if("true".equals(isTouch)){
%>
<link type="text/css" rel="stylesheet" href="/css/Tabletcss.css?version=<%= version%>" />
<% }else{ %>
<link type="text/css" rel="stylesheet" href="/css/web.css?version=<%= version%>" />
<% } %>

<script type="text/javascript" src="/jscripts/jquery.contactable.packed.js"></script>
<script  type="text/javascript" >

	$(document).ready(function() {
	<%	boolean isFeedbackRTL=(Boolean) request.getAttribute("isRTL");	%>
	window.onload=function(){
	document.body.style.direction=(<%= isFeedbackRTL %>)?"rtl":"ltr";
	}				
	jQuery(function(){
		
		jQuery('#contact').contactable({
	recipient: 'test@test.com',
	subject: 'A Feeback Message',
	email:emailId
});		
});	
});
	var emailId="<%= emailId%>";
	if(emailId=="null"){
		emailId ="enter your email here"
	}
var isTouch=<%= isTouch%>;
</script>
