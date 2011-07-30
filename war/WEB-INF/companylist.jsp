<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
      <meta http-equiv="content-type" content="text/html; charset=UTF-8">
      <title>Accounter</title>
      <link type="text/css" rel="stylesheet" href="/css/Finance.css?version=<%= version%>">
  </head>
  <body>
  <table class="header">
	   <tr>
	      <td width="25%"><img src="/images/Accounter_logo_title.png" /></td>
	      <td width="50%"><div class="companyName">Company Lists</div></td>
	      <td width="25%">
	        <ul>
	           <li><img src="images/User.png" /><a href="">Welcome ${userName}</a></li>
	           <li><img src="images/Help.png" /><a href='http://help.accounter.com'>Help</a></li>
	           <li><img src="images/logout.png" /><a href='/do/logout'>Logout</a></li>
	        </ul>
	      </td>
	   </tr>
	</table>
    <div class="company_lists">
       <ul>
	     <form action="/companysetup" method="post">
	     <% java.util.List<String> list=(java.util.List<String>)req.getAttribute("message") %>
	     <% for(String name:list){ %>
	       <li><a href="" ><%= name %> </a></li>
	      <% } %>
	     </form>
	   </ul>
    </div>
  </body>
</html>