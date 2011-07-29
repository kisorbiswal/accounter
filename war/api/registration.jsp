<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!-- <%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> -->

<html>
  <head><title>Accounter</title></head>
  <body>
  <table>
  <!--<tr>
  <td> <h3>Company Information</h3></td>
 </tr>
   
    <td>Company:<select >
   <% java.util.List<String> list=(java.util.List<String>)req.getAttribute("message") %>
     <% for(String name:list){ %>
     <option<%= name %>/>
      <% } %></select></td>-->
	
   
   <tr>
   <td><h3>Application Information</h3></td>
   </tr>
   
   <tr>
   <td>Application Name:</td>
   <td><input type="text"/></td>
   </tr>
   <tr>
   <td>Description:</td>
   <td><input type="text" /></td>
   </tr>
   <tr>
   <td>Integration URL:</td>
   <td><input type="text"/></td>
   </tr>
   <tr>
   <td>Application Type:</td>
   <td><select>
   		<option>Web Application </option>
   		<option>Desktop Application</option>
   		<option>Mobile Application</option>
   		</select></td>
	</tr>
	<tr>
   <td>Application Use:</td>
   <td><input type="text"/></td>
   </tr>
   
   		
   <tr>
   <td><h3>Contact Information</h3></td>
   </tr>
   
   <tr>
   <td>Developer Contact Email:</td>
   <td><input type="text"/></td>
   </tr>
   <tr>
   <td>Contact Number:</td>
   <td><input type="text" /></td>
   </tr>
	</table>   
  </body>
</html>