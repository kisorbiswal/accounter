<html>
<head>
<body>
<div>
<c:if test="${message != null}">
   <div class="common-box create-company-message">${message}</div>
</c:if>
<form id = "maintananceForm" method="post" action="/maintanance">
<c:if test="${CheckedValue == false}">
<input type="checkbox" name="option1" >${CheckedValue} Server under maintainace <br>
</c:if>
<c:if test="${CheckedValue == true}">
<input type="checkbox" name="option1" checked="checked" >Server under maintainace <br>
</c:if>
<label>Please enter your admin password:
<input type = "Password"  id = "adminPassword"  name ="password" ><br>
<input type = "Submit" value = "Submit " id = "submitButton"  >
</form></div>
</body>
</head>
</html>