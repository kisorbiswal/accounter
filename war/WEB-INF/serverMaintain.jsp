<html>
<head>
<body>
<div>
<c:if test="${message != null}">
   <div class="common-box create-company-message">${message}</div>
</c:if>
<form id = "maintananceForm" method="post" action="/maintanance">
<input type="checkbox" name="option1" >Server under maintainace <br>
<label>Please enter your admin password:
<input type = "Password"  id = "adminPassword"  name ="password" ><br>
<input type = "Submit" value = "Submit " id = "submitButton"  >
</form></div>
</body>
</head>
</html>