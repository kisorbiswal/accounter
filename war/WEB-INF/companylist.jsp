<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="i18n" uri="/WEB-INF/i18n.tld"%>
<html>
  <head>
  <title><i18n:i18n msg='companieslist'/>| Accounter
  </title>
        <meta content="IE=100" http-equiv="X-UA-Compatible" />
		<link rel="shortcut icon" href="/images/favicon.ico" />
		<%@ include file="./feedback.jsp" %>
		<link type="text/css" href="../css/ss.css?version=<%= version%>" rel="stylesheet" />
		<%	boolean isConListRTL=(Boolean) request.getAttribute("isRTL");	%>
		<%	Boolean enableEncryption=(Boolean) request.getAttribute("enableEncryption");	%>
		<%	enableEncryption=enableEncryption==null?false:enableEncryption;	%>
		<%	Boolean isPaid=(Boolean) request.getAttribute("isPaid");	%>
		<%	String userEmail=(String) request.getAttribute("emailId");	%>
		<%	isPaid=isPaid==null?false:isPaid;	%>
		<%	enableEncryption=enableEncryption&&isPaid;	%>
	<script type="text/javascript">
		window.onload=function(){
		document.body.style.direction=(<%= isConListRTL %>)?"rtl":"ltr";
		};
		
		$(document).ready(function() {
		var isPaid=${isPaid};
		var userEmail='<%=userEmail%>';
		if(isPaid){
       $('#logoutlink').after('<a style="padding-left:25px" href="/site/subscriptionmanagement"><i18n:i18n msg='subscribtionManagement'/></a>');
       }
       else{
        $('#logoutlink').after('<a target="_blank" href="/site/subscription/gopremium?emailId='+userEmail+'">Go Premium</a>');
       }
       
       });
		function goto(comp){
			$(document).ready(function() {
				var params= {
					companyId: comp,
					isTouch: touchDeviceTest()
				};
				document.location = '/main/companies' + '?' + $.param(params);
			});
		};
		function createCompany(){
			$(document).ready(function() {
				var params= {
					isTouch: touchDeviceTest(),
					create:true
				};
				document.location = '/main/companies'+'?'+ $.param(params);
			});
		};
		function touchDeviceTest() {
			var el = document.createElement('div');
			el.setAttribute('ongesturestart', 'return;');
			if(typeof el.ongesturestart == "function"){
				return true;
			}else {
				return false;
			}
		}
		
	</script>
  </head>
  <body>
  <table id="commanContainer" class="companies-list-page">
  <tr>
  <td>
	<div>
		<img style="float:left" src="/images/Accounter_logo_title.png" class="accounterLogo" alt="loading" />
	</div>
	<div style="float:right">
	 <%@ include file="./locale.jsp" %>
	</div>
	<div class="company_name_action">
			<i18n:i18n msg='clickOnTheCompanyNameToOpen'/>
	</div>
    <div class="company_lists" style="clear:both">
        <c:if test="${message != null}">
       		<div class="common-box create-company-message">${message}</div>
        </c:if>
       <div class="form-box">
        <c:if test="<%= isPaid%>">
      	<div> <a onClick=createCompany() href="#" class="create_new_company"><i18n:i18n msg='createNewCompany'/></a></div>
      	</c:if>
      	<ul><li>
	    <c:if test="${companeyList != null}">
		   <c:forEach var="company" items="${companeyList}">
			   <c:set var='url' value="/main/companies?companyId=${company.id}"/>
			    <c:set var='deleteurl' value="/main/deletecompany?companyId=${company.id}"/>
			   <div class="companies-list"><a onClick=goto(${company.id}) href="#">${company.preferences.tradingName} - ${company.registeredAddress.countryOrRegion} </a> <a class="delete_company" href= '${deleteurl}' ><i18n:i18n msg='delete'/></a></div>
		   </c:forEach> 
	    </c:if>
	    </li>
	   </ul>
	  </div>
    </div>
    <div class="form-bottom-options">
      <a style="float:left" id="logoutlink" href="/main/logout"><i18n:i18n msg='logout'/></a>
      <a style="float:right" href="/main/deleteAccount"><i18n:i18n msg='deleteAccount'/></a>
      <c:if test="<%= enableEncryption %>">
    	 <a href="/main/encryption"><i18n:i18n msg='encryption'/></a>
      </c:if>
    </div>
      </td>
   </tr>
</table>
   
   <!-- Footer Section-->
   
   <div id="mainFooter"  >
   <div>
	       <span><i18n:i18n msg='atTherateCopy'/></span> |
	       <a target="_blank" href="/site/termsandconditions"><i18n:i18n msg='termsConditions'/></a> |
	       <a target="_blank" href="/site/privacypolicy"><i18n:i18n msg='privacyPolicy'/></a> |
	       <a target="_blank" href="/site/support"><i18n:i18n msg='support'/></a>
   </div>
</div>
   <div id="appVersions">
	    <div>
	       <span>Access Accounter from </span>
	       <a target="_blank" href="https://market.android.com/details?id=com.vimukti.accounter"> Android </a> |
	       <a target="_blank" href="http://www.windowsphone.com/en-US/apps/6a8b2e3f-9c72-4929-9053-1262c6204d80"> Windows Phone </a> |
		   <a target="_blank" href="http://itunes.apple.com/us/app/accounter/id466388076?ls=1&mt=8"> iPhone </a> |
		   <a target="_blank" href="https://appworld.blackberry.com/webstore/content/67065/?lang=en"> Black Berry </a> |
		   <a target="_blank" href="http://itunes.apple.com/us/app/accounter/id447991983?ls=1&mt=12"> iPad </a> |
		   <a target="_blank" href="http://itunes.apple.com/us/app/accounter/id447991983?ls=1&mt=12"> Mac OS </a>
		   </div>
	</div>
<%@ include file="./scripts.jsp" %>
  </body>
</html>