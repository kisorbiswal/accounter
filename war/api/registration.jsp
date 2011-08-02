<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!-- <%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> -->

<html>
  <head>
  <title>Registration</title>
  <script>
  function addApplication(){
  	alert('Application was added..');
  }
  </script>
  </head>
  
  <body>
  <form id="accounterForm" method="post" action="/apiregistration">
  <table>
   
   <tr>
   <td><h3>Application Information</h3></td>
   </tr>
   
   <tr>
   <td>Application Name:</td>
   <td><input type="text" name="applicationName" value="${param.applicationName}"></input></td>
   </tr>
   
   <tr>
   <td>Description:</td>
   <td><input type="text" name="description" value="${param.description}"/></td>
   </tr>
   
   <tr>
   <td>Integration URL:</td>
   <td><input type="text" name="integrationUrl" value="${param.integrationUrl}"/></td>
   </tr>
   
   <tr>
   <td>Application Type:</td>
   <td><select name="applicationType" value="${param.applicationType}" >
   		<option>Web Application </option>
   		<option>Desktop Application</option>
   		<option>Mobile Application</option>
   		</select></td>
	</tr>
	
   <tr>
   <td>Application Use:</td>
   <td><input type="text" name="applicationUse" value="${param.applicationUse}"/></td>
   </tr>
   
   <tr>
   <td><h3>Contact Information</h3></td>
   </tr>
   
   <tr>
   <td>Developer Contact Email:</td>
   <td><input type="text" name="developerEmailId" value="${param.developerEmailId}"/></td>
   </tr>
   
   <tr>
   <td>Contact Number:</td>
   <td><input type="text" name="contact" value="${param.contact}"/></td>
   </tr>
   
   </table> 
	  <div><input type="submit" name="Add Application" value="Add Application" /></div>
	  </form>
  </body>
</html>