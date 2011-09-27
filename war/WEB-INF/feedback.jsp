<script src="/jscripts/jquery-1.6.2.js" type="text/javascript"></script>
<script src="/jscripts/jquery.validate.js" type="text/javascript"></script>
<link type="text/css" rel="stylesheet" href="../css/Finance.css?version=<%= version%>" />
<script type="text/javascript" src="/jscripts/jquery.contactable.packed.js"></script>
<link type="text/css" href="../css/ss.css" rel="stylesheet">
<script  type="text/javascript" >
	$(document).ready(function() {
	
	$('body').append('<div id="contact"></div>');
	
	jQuery(function(){
		jQuery('#contact').contactable({
	recipient: 'test@test.com',
	subject: 'A Feeback Message'
});
});
});	
</script>