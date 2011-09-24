<html>
<head>
<meta content="IE=100" http-equiv="X-UA-Compatible">
<link type="text/css" href="../css/Finance.css" rel="stylesheet">
<script type="text/javascript" src="/jscripts/jquery-1.6.2.js"></script>
<script type="text/javascript" src="/jscripts/jquery.contactable.packed.js"></script>
<script src="/jscripts/jquery.validate.js" type="text/javascript"></script>
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
</head>
	<body>
	<div id="contact"> </div>
		</body>
</html>