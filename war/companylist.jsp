<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head><title>Accounter</title></head>
  <body>
    <div>
     <form action="/companysetup" method="post">
     <% java.util.List<String> list=(java.util.List<String>)req.getAttribute("companeyList") %>
     <% for(String name:list){ %>
     <input type="text" value=<%= name %>/>
      <% } %>
     </form>
    </div>
  </body>
</html>