<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>Free Online Accounting Software. Free Support | Accounter
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible">

<link rel="shortcut icon" href="../images/favicon.ico" />
<link type="text/css" href="../css/ss.css" rel="stylesheet">
<script src="/jscripts/jquery-1.6.2.js" type="text/javascript"></script>
<script type="text/javascript" src="/jscripts/stepcarousel.js"></script>
<script type="text/javascript">
			stepcarousel.setup({
				galleryid: 'mygallery', //id of carousel DIV
				beltclass: 'belt', //class of inner "belt" DIV containing all the panel DIVs
				panelclass: 'panel', //class of panel DIVs each holding content
				autostep: {enable:true, moveby:1, pause:3000},
				panelbehavior: {speed:1000, wraparound:true, wrapbehavior:'slide', persist:true},
				defaultbuttons: {enable: true, moveby: 1, leftnav: ['/images/Left-arrow_24x25.png', 127, 279], rightnav: ['/images/Right-arrow_24x25.png', -169, 279]},
				statusvars: ['statusA', 'statusB', 'statusC'], //register 3 variables that contain current panel (start), current panel (last), and total panels
				contenttype: ['inline'] //content setting ['inline'] or ['ajax', 'path_to_external_file']
				})
			</script>
</head>
	<body>
		<div class ="body-container">
		<%@ include file="./header.jsp" %>
			<div class="middle-part">
				<div class="upper-part" id="cen">
				<div class="left-middle">
				<div id="header">
				<div class="image-text-button" >
				<h1 align="center" class="account-sys-label">Now Accounting is easy!</h1>
				<p align="center" class="easiest-label">Work on your finances from anywhere, anytime, it's Free</p>
				<ul class="signup-for-free-button">
				   <li><span class="sign-up-left"></span></li>
				   <li><a href="/signup" id="but-g" class="sign-up-middle">Sign Up, Its Free</a></li>
				   <li><span class="sign-up-right"></span></li>
				</ul>
					</div>
			</div>  
        	</div>
        	<div class="right-middle">
					<div id="mygallery" class="stepcarousel">
			<div class="belt">
				<div class="panel"><img src="/images/Balancesheet.png" /></div>
				<div class="panel"><img src="/images/TrailBlance.png" /></div>
				<div class="panel"><img src="/images/Enterbillscreenshot.png" /></div>
				<div class="panel"><img src="/images/Invoice1.png" /></div>
				<div class="panel"><img src="/images/ProfitAndLoss.png" /></div>
			</div>
		</div>
		<p id="mygallery-paginate" style="width: 502px; text-align:center">
			<img style="cursor: pointer; margin-left: 5px; margin-right: 10px;" data-moveby="1" data-select="/images/Bullet-icon_10x11.png" data-over="/images/Bullet-icon_10x11.png" src="/images/Image-flow-indicator.png">
		</p>
		</div>
		</div>
		<div class="lower-part">
				
					<ol>
						<li><img class="icon1" src="/images/On-offline.png"/><p class="header_p_style"><b>Easy Online Accounting</b></p>
					 	<p>Keep track of your daily financial activities. Track where your money is coming from and going out quickly. You can login from anywhere, anytime.</p>
					  </li>
					  <li><img class="icon2" src="/images/Invoice.png"/><p class="header_p_style"><b>Invoicing</b></p>
					 	<p>Customize and send invoices for payments and record bills and expenses.</p>
					  </li>
						<li><img class="icon3" src="/images/Sharing.png"/><p class="header_p_style"><b>Easy Sharing</b></p>
					 	<p>Let your accountant, financial adviser or employee have access to your data by sharing or delegating the accounting duties to them. You can set different permission to different users.</p>
					 </li>
				    </ol>
				    <ol>
						<li><img class="icon4" src="/images/Decide.png"/><p class="header_p_style"><b>Make Quick Decisions</b></p>
					 	<p>With a glance of the dash board you can quickly understand what is working and what is not working with your business? The insightful reports available in the software makes it easy for you to take quick decisions.</p>
					 </li>
						<li><img class="icon5" src="/images/Contacts.png"/><p class="header_p_style"><b>Contacts and Inventory</b></p>
					 	<p>Manage and review your customers, suppliers and inventory items details easily.</p>
					  </li>
						<li><img class="icon6" src="/images/Credit-card.png"/><p class="header_p_style"><b>Bank and Credit card transactions</b></p>
					 	<p>Track your bank deposits, fund transfers, credit card charges and refunds. </p>
					  </li>
					  </ol>
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
