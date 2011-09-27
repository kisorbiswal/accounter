<link type="text/css" rel="stylesheet" href="../css/Finance.css?version=<%= version%>">
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
	<div id="contact"> </div>
