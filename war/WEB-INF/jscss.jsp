<%-- 
    Update:
    No need to include this any more. This will create a 'version' variable for application
    scope we can use this in any jsp file.
    
    Ex:
        <script type='text/javascript' src='my.js?version=${version}'></script>
    
    OLD Description:    
    Include this jsp in HEAD section. this will add JS and CSS tags in to the resulted HTML.
    The source urls will suffixed with a version parameter which will be taken from the servlet context[Deployment Descriptor, web.xml]
  
    USAGE:
    param: css , value: css files separated with comma followed by space [', '], Optional.
    param: js  , value: js files separated with comma followed by space [', '], Optional.

    @author: Srikanth.J
    
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="version"
	value="${servletContext.getInitParameter('version')}"
	scope="application" />

<%--<c:if test="${request.getParameter('css')}">
	<c:set var="cssFiles"
		value="${request.getParameter('css').toString().split(', ')}" />
	<c:forEach var="cssFile" items="${cssFiles}">
		<link type="text/css" rel="stylesheet"
			href="${cssfile}?version=${version}">
	</c:forEach>
</c:if>



<c:if test="${request.getParameter('js')}">
	<c:set var="jsFiles"
		value="${request.getParameter('js').toString().split(', ')}" />
	<c:forEach var="jsFile" items="${jsFiles}">
		<script type="text/javascript" src="${jsFile}?version=${version}"></script>
	</c:forEach>
</c:if>

--%>