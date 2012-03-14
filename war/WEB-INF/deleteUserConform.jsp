<%@ page import="java.util.*" %>
<%@ page import="com.vimukti.accounter.core.*" %>
<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix="i18n" uri="/WEB-INF/i18n.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!-- The HTML 4.01 Transitional DOCTYPE declaration-->
<!-- above set at the top of the file will set     -->
<!-- the browser's rendering engine into           -->
<!-- "Quirks Mode". Replacing this declaration     -->
<!-- with a "Standards Mode" doctype is supported, -->
<!-- but may lead to some differences in layout.   -->

<html>

  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <script src="/jscripts/jquery-1.7.min.js" type="text/javascript"></script>
	<script src="/jscripts/jquery.validate.js" type="text/javascript"></script>
	<link type="text/css" href="../css/ss.css" rel="stylesheet" />
    <%
    	Set<String> users =(Set<String>) request.getAttribute("userIdsList");
  	%>
  	<title>Delete User</title>
  </head>
  <body>
  <div id="commanContainer" style="width:420px;  font-size: 13px;">
 	 <img src="/images/Accounter_logo_title.png" class="accounterLogo" alt = "accounter logo"/>
		<form id="subscription_complition_form" method="post"  class="form-box"  action="/main/subsdeleteuserconform">
			<table cellspacing="10">
				<tr>				
	    			<% if(users != null){%>
	    				Do you really wants to delete following users?
	    				<br />
		   				<% for(String s:users) { %>
			   				<li>
		   						<%=s%>
		   					</li>
		   				<% } %>
	    			<% } %>
				</tr>
			</table>		
			<div>
		    	<div style="float:right;">
		   			<input type="button" class="allviews-common-button" value="Cancel" onclick="parent.location='/main/subscriptionmanagement'" />
				</div>
				<div style="float: right; width: 75px;">
		   			<input type="submit" class="allviews-common-button" value="Okay" />
				</div>
			</div>
	 	</form>  
	</div>
  </body>
</html> 