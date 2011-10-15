 <% String version = application.getInitParameter("version"); %>
<script src="/jscripts/jquery-1.6.4.min.js" type="text/javascript"></script>
<script src="/jscripts/jquery.validate.js" type="text/javascript"></script>
<link type="text/css" rel="stylesheet" href="/css/Finance.css?version=<%= version%>" />
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
</script>
