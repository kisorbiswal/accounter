<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="i18n" uri="/WEB-INF/i18n.tld"%>
<%@page pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<title> <i18n:i18n msg='companyPassword'/> | Accounter
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible" />

<link rel="shortcut icon" href="/images/favicon.ico" />
<%
	String error =(String) request.getAttribute("error");
	error=error==null?"":error;
%>
<%@ include file="./feedback.jsp" %>
<link type="text/css" href="../css/ss.css?version=<%= version%>" rel="stylesheet" />
<link type="text/css" href="../css/cmxform.css?version=<%= version%>" rel="stylesheet" />
</head>
<body>
<form action="companypassword" method="post">
<%= error %><br/>
Enter company password:
<input type="password" name="password" />
</form>
</body>
</html>