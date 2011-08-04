<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!-- The HTML 4.01 Transitional DOCTYPE declaration-->
<!-- above set at the top of the file will set     -->
<!-- the browser's rendering engine into           -->
<!-- "Quirks Mode". Replacing this declaration     -->
<!-- with a "Standards Mode" doctype is supported, -->
<!-- but may lead to some differences in layout.   -->

<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">

<!--                                           -->
<!-- Any title is fine                         -->
<!--                                           -->
<title>DefBiz</title>
<script type="text/javascript">
    String.prototype.trim = function() { return this.replace(/^\s*/, "").replace(/\s*$/, ""); };
    	function validation(formObject){
    		var flag=true;
    	
            var Name=document.getElementById("name").value;

    		var search=Name.search(/[A-Z]/);
    		if(Name==null||Name==""){
    			document.getElementById("nameLabel").innerHTML="Enter valid Company Name";
    			flag=false;
    		}
    		var space=Name.trim().split(" ").length;
    		
    		if(space>1){
    			document.getElementById("nameLabel").innerHTML="Company Name must not contain space ";
    			flag=false;
    		}
    		if(search+1>0)
    		{
    		    document.getElementById("nameLabel").innerHTML="Capital letters are not allowed in Company Name";
    			flag=false;
    		}
    		var UsersNos=document.getElementById("nooofusers").value;
    		if(UsersNos==null||UsersNos==""||!UsersNos.match("[0-9]+")){
    			document.getElementById("noOfUserLabel").innerHTML="Enter valid Company Users";
    			flag=false;
    		}
    		var email=document.getElementById("emailid").value;
    		if(email==null||email==""||email.indexOf("@")<1||email.indexOf(".")<1){
    			document.getElementById("emailLabel").innerHTML="Please enter valid email";
    			flag=false;
    		}
    		var pwd=document.getElementById("pwd").value;
    		var cnfrmpwd=document.getElementById("con_pwd").value;
    		if(pwd==null||cnfrmpwd==null||pwd==""||cnfrmpwd==""||pwd!=cnfrmpwd||cnfrmpwd.length<6){
    			document.getElementById("passwordLabel").innerHTML="Enter valid password and same in confirm password with at least 6 character";
    			flag=false;
    		}
    		var companypwd=document.getElementById("companypwd").value;
    		if(companypwd==null || companypwd.length<6){
    			document.getElementById("companyPasswordLabel").innerHTML="Enter valid password ";
    			flag=false;
    		}
    		
    		var user=document.getElementById("userName").value;
    		if(user==null||user==""){
    		     document.getElementById("userNameLabel").innerHTML="Enter User Name";
    		     flag=false;
    		}
    		if(flag){
    			loadingImage();
    			formObject.submit();
    			
    		}
    		return false;
    	}
    	function loadingImage(){
    		document.getElementById("hiddenDiv").style.display="block";
    		document.getElementById("formDiv").style.display="none";
    	}

    	function resetDates(monthComboBoxName,daysComboBoxName){
        	
    		var monthCombobox = document.getElementById(monthComboBoxName);
    		var month = monthCombobox.value;
    		
    		var noOfdays;
    		var date = new Date();
    		var presentYear = date.getFullYear();
    		if(month == 0 || month == 2 || month == 4 || month == 6 || month == 7 || month == 9 || month == 11){
    			noOfdays = 31;
    		}else
    		if(month == 3 || month == 5 || month == 8 || month == 10 ){
    			noOfdays = 30;
    		}else
    		if(month == 1 && (presentYear % 4) == 0){
    			noOfdays = 29;
    		}else{
    			noOfdays = 28;
    		}
    		
    		var daysComboBox = document.getElementById(daysComboBoxName);
    		var i;
    		
    		for(i=daysComboBox.options.length-1;i>=0;i--)
    		{
    			daysComboBox.remove(i);
    		}
    		
    		
    		for(i=1;i<=noOfdays;i++){
    			var optn = document.createElement("OPTION");
    			optn.text = i;
    			optn.value = i;
    			daysComboBox.options.add(optn);
    		}
    		
    	}

    	function companyPassword(){
   
    		var companypsw = document.getElementById("companypwd").value;
    	
    		if (window.XMLHttpRequest)
    		  {// code for IE7+, Firefox, Chrome, Opera, Safari
    		  xmlhttp=new XMLHttpRequest();
    		  }
    		else
    		  {// code for IE6, IE5
    		  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    		  }
    	
    		xmlhttp.open("GET","companyAuthentication?companypsw="+companypsw,false);
    		
    		xmlhttp.send();
    		xmlDoc=xmlhttp.responseXML; 
    		var message = xmlDoc.getElementsByTagName("message")[0].childNodes[0].nodeValue;
    		document.getElementById("companyPasswordLabel").innerHTML = "<span id=red>"+message+" </span> ";       		
   	}
           		
    </script>
<!--CSS for loading message at application Startup-->
<style type="text/css">
*{
  margin:0px;
  padding:0px;
}
form{
  font-weight:normal;
  font-family:sans-serif;
}
#commanContainer{
    margin: 7em auto;
    width: 320px;
}
#commanContainer label{
    color: #777777;
    font-size: 14px;
}
#commanContainer form{
    background: none repeat scroll 0 0 #FFFFFF;
    border: 1px solid #E5E5E5;
    border-radius: 3px 3px 3px 3px;
    box-shadow: 0 4px 10px -1px rgba(200, 200, 200, 0.7);
    font-weight: normal;
    margin-left: 8px;
    padding: 26px 24px 46px;
}
#commanContainer  input[type="text"],#commanContainer  input[type="password"],#commanContainer select{
    background: none repeat scroll 0 0 #FBFBFB;
    border: 1px solid #E5E5E5;
    box-shadow: 1px 1px 2px rgba(200, 200, 200, 0.2) inset;
    margin:2px 6px 16px 0px;
    outline: medium none;
    padding: 3px;
    width: 97%;
	height:40px;
}
.rememberMe{
    float:left;
}
.loginbutton,#forgot-login,.signup-submit,.reset-button{
    float:right;
	margin-right: 8px;
}
.allviews-common-button{
    background: none repeat scroll 0 0 #28757D;
    border: 1px solid #28757D;
    border-radius: 11px 11px 11px 11px;
    color: white;
    cursor: pointer;
    font-family: sans-serif;
    font-size: 13px;
    font-weight: bold;
    margin-top: -3px;
    padding: 3px 10px;
	text-shadow: 0 -1px 0 rgba(0, 0, 0, 0.3);
}
.loginbutton input:active,.loginbutton input:hover,.loginbutton input:focus{
    background:#1a5158;
	border:1px solid #9ebfc3;
}
.form-bottom-options{
    margin: 0 0 0 16px;
    padding: 16px 16px 0;
    text-shadow: 0 1px 0 #FFFFFF;
}
.form-bottom-options a,#commanContainer a{
   color:#28757D;
   text-decoration:underline;
}
.accounterLogo{
   padding:0 0 15px 10px;
}
.common-box{
    border-radius: 3px 3px 3px 3px;
    border-style: solid;
    border-width: 1px;
    margin: 0 0 16px 8px;
    padding: 12px;
    font-size:12px;
	font-family:sans-serif;
}
#login_error{
	background-color: #FFEBE8;
    border-color: #CC0000;
}
#forgot-password_error{
    background-color: #FFFFE0;
    border-color: #E6DB55;
}
.signup-container{
   width:500px !important;
   margin:0 auto !important;
}
.signup-submit{
   margin-top:15px;
}
.reset-header{
   color:#28757D;
   margin-bottom:10px;
}
</style>
</head>
<body>
<div id="commanContainer">	
<img src="../images/Accounter_logo_title.png" class="accounterLogo" />	
<div id="hiddenDiv" class="hiddenPic">
  <img src="/images/icons/loading-indicator.gif" alt="Loading" title="Loading" height="50" width="50">
</div>
<c:if test="${message != null}">
<div id="login_error" class="common-box">
  <b>${message} </b> 
</div> 
</c:if>	

<c:if test="${message==null}">
 <div id="formDiv">
	<form method="post" action="/createcompany"
		onsubmit="return validation(this)">
		 <strong> Create Company</strong><br>
	     <div>
		    <label>Name</label>
			<input type="text" name="name" id="name"
				onClick="document.getElementById('nameLabel').innerHTML='';" /></td>
		 </div>
		 <div>
		   <label>Company Type</label>
		   <select name="companyType">
				<option value="1">UK</option>
				<option value="0">US</option>
			</select>
		 </div>
	</form>
	</div>
</c:if>
</div>

</body>
</html>
