<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
        <meta content="IE=100" http-equiv="X-UA-Compatible">
		<link rel="shortcut icon" href="../images/favicon.ico" />
		<%@ include file="./feedback.jsp" %>
		<link type="text/css" href="../css/ss.css" rel="stylesheet">
  </head>
  <body>
  <div id="commanContainer">
	<div>
		<img src="../images/Accounter_logo_title.png" class="accounterLogo" />
		
	</div>
    <div class="company_lists">
       
       
        <c:if test="${message != null}">
       		<div class="common-box create-company-message">${message}</div>
        </c:if>
       <div class="form-box">
      	<div> <a href="/main/createcompany" class="create_new_company">Create New Company </a></div>
      	<ul>
	    <c:if test="${companeyList != null}">
		   <c:forEach var="company" items="${companeyList}">
			   <c:set var='url' value="/main/companies?companyId=${company.id}"/>
			    <c:set var='deleteurl' value="/main/deletecompany?companyId=${company.id}"/>
			    <c:choose>
				    <c:when test="${company.companyType == 0}">
				   		<c:set var='companyType' value="US"/>
				    </c:when>
				    <c:when test="${company.companyType == 1}">
				     	<c:set var='companyType' value="UK"/>
				    </c:when>
				    <c:when test="${company.companyType == 2}">
				        <c:set var='companyType' value="IND"/>
				    </c:when>
				    <c:otherwise>
				        <c:set var='companyType' value="OTH"/>
				    </c:otherwise>
				</c:choose>
			   <div class="companies-list"><a href=${url}>${company.companyName} - ${companyType}</a> <a class="delete_company" href=${deleteurl}>Delete</a></div>
		   </c:forEach>
	    </c:if>
	    
	   </ul>
	  </div>
    </div>
    <div class="form-bottom-options">
      <a href="/main/logout">Logout</a>
    </div>
   </div>
   
   <!-- Footer Section-->
   
   <div id="mainFooter"  >
   <div>
      <span>&copy 2011 Vimukti Technologies Pvt Ltd</span> |
      <a target="_blank" href="/site/termsandconditions"> Terms & Conditions </a> |
      <a target="_blank" href="/site/privacypolicy"> Privacy Policy </a> |
      <a target="_blank" href="/site/support"> Support </a>
   </div>
</div>
   
   <script type="text/javascript" charset="utf-8">
			var is_ssl = ("https:" == document.location.protocol);
			var asset_host = is_ssl ? "https://s3.amazonaws.com/getsatisfaction.com/" : "http://s3.amazonaws.com/getsatisfaction.com/";
		</script>
		 
  </body>
</html>