<script type="text/javascript">
$(document).ready(function() {
	$("#login-id").click(function(){
		$("#login-id").addClass("login-hover");
		$("#home-bg").removeAttr("class");
	});
	var urlpath = location.pathname;
	var paths = urlpath.split("/");
	pageName = paths[paths.length - 1];
	if (pageName == "home") {
		$("#home-bg").addClass("home-jbg");
	} else if (pageName == "support") {
		$("#sup-bg").addClass("sup-jbg");
        $("#home-bg").removeAttr("class");
	} else if (pageName == "features") {
		$("#fea-bg").addClass("fea-jbg");
        $("#home-bg").removeAttr("class");
	}

});
</script>
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
					    <a href="/site/home" id="home-bg" class="active">Home</a>
					 </li> 
					 <li>
					    <a href="/site/features" id="fea-bg">Features</a>
					 </li> 
					 <li>
						<a href="/site/support" id="sup-bg">Support</a>
					</li>
					 <li>
					    <a href="http://blog.accounterlive.com/" id="blog-bg" target ="_blank" >Blog</a>
					 </li>
			  </ul>
				</div>
				<div class="remaining-header-part-login">
					<ul>
					   <li><a href="/login" id="login-id">Login</a></li>
					</ul>
				</div>
			   </div>
			</div>
			
			
			<!--<table style="width:100%" class="menu-table">
			<tr style="width:100%">
			<td style="width:100%">
				<div class="logo-image">
					<a href="/"><img src="/sites/Accounter_logo_title.png">
					</a>
				</div>
				</td>
				</tr>
				<tr style="width:100%">
				<td class="menu-bar-login">
				<div class="menu-bar">
				<ul>
					 <li>
					 <a href="/site/home" id="home-bg" >Home</a>
					 </li> 
					 <li>
					   <a href="/site/features" id="fea-bg" >Features</a>
					 </li> 
					 <li>
						<a href="/site/support" id="sup-bg">Support</a>
					</li>
			  </ul>
				</div>
				<div class="remaining-header-part-login">
					<ul>
					   <li><a href="/site/login" id="login-id">Login</a></li>
					</ul>
				</div>
				</td>
				</tr>
				</table>-->
			</div>
		
