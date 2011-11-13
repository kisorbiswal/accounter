<% 
String version = application.getInitParameter("version");
String isTouch = (String)request.getAttribute( "isTouch" );
%>
<script src="/jscripts/jquery-1.7.min.js" type="text/javascript"></script>
<script src="/jscripts/jquery.validate.js" type="text/javascript"></script>
<%
if("true".equals(isTouch)){
%>
<link type="text/css" rel="stylesheet" href="/css/Tabletcss.css?version=<%= version%>" />
<% }else{ %>
<link type="text/css" rel="stylesheet" href="/css/Finance.css?version=<%= version%>" />
<% } %>
<script type="text/javascript" src="/jscripts/jquery.contactable.packed.js"></script>
<script  type="text/javascript" >
	$(document).ready(function() {
	jQuery(function(){
		jQuery('#contact').contactable({
	recipient: 'test@test.com',
	subject: 'A Feeback Message'
});		
});
});	
var isTouch=<%= isTouch%>;
</script>