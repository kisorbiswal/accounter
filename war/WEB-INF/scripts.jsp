<div id="contact"></div>
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
	
<!--Start of Zopim Live Chat Script-->
<script type="text/javascript">
window.$zopim||(function(d,s){var z=$zopim=function(c){z._.push(c)},$=z.s=
d.createElement(s),e=d.getElementsByTagName(s)[0];z.set=function(o){z.set.
_.push(o)};z._=[];z.set._=[];$.async=!0;$.setAttribute('charset','utf-8');
$.src='//cdn.zopim.com/?5ORBx6zz1Cq3OWAmSSLzY2AtW9vSmSOx';z.t=+new Date;$.
type='text/javascript';e.parentNode.insertBefore($,e)})(document,'script');

 $zopim(function() {
 	 $zopim.livechat.noFlash = true;
<%
{
	String email=(String)request.getSession().getAttribute("emailId");
	String userName=(String)request.getAttribute("userName");
	String companyName=(String)request.getAttribute("companyName");
	if(email!=null){
	%>
		 $zopim.livechat.setEmail('<%=email %>');
	<%
	}
	if(userName!=null){
	%>
		$zopim.livechat.setName('<%=userName %>');
	<%
	}
	if(companyName!=null){
	%>
		$zopim.livechat.setNotes('<%=companyName %>');
	<%
	}
	
	
}
%>
});

</script>
<!--End of Zopim Live Chat Script-->