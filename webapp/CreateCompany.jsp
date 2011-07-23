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
body {
	overflow: hidden
}

#loading {
	border: 1px solid #ccc;
	position: absolute;
	left: 45%;
	top: 40%;
	padding: 2px;
	z-index: 20001;
	height: auto;
}

#loading a {
	color: #225588;
}

#loading .loadingIndicator {
	background: white;
	font: bold 13px tahoma, arial, helvetica;
	padding: 10px;
	margin: 0;
	height: auto;
	color: #444;
}

#loadingMsg {
	font: normal 10px arial, tahoma, sans-serif;
}

html body {
	font-family: Verdana, sans-serif;
	font-size: 13px;
}

html	table,tr,td {
	font-family: Verdana, sans-serif;
	font-size: 13px;
	border-spacing: 0pt;
}

body {
	font-size: 13px;
	background: LightSteelBlue none repeat scroll 0 0;
}

.login {
	margin: 140px auto;
	background-color: #f2f3f5;
	padding: 10px;
	border: 1px solid #000;
	width: 45%;
}

.image {
	text-align: center;
}

.clearfix:after {
	content: ".";
	display: block;
	height: 0;
	clear: both;
	visibility: hidden;
}

fieldset {
	border: 1px solid #000;
	padding: 10px;
	background-color: #e9edf0;
	margin: 5px 0px;
	width: 300px;
}

fieldset table td {
	float: left;
}

.login img[src="logo.png"] {
	text-align: center;
}

.login img[src="lock.png"] {
	float: left;
}

p {
	text-align: left;
	clear: left;
	line-height: 30px;
	margin: 0px;
}

.loginbox {
	float: right;
}

.loginlabel {
	font-weight: bold;
	color: #c64933;
}

.loginbox a {
	font-weight: bold;
	color: #0000ff;
	margin-left: 20px;
	text-decoration: none;
}

#formDiv input[type="text"],#formDiv input[type="password"] {
	border: 1px solid #9cb0ce;
}

.loginbox a:hover {
	text-decoration: underline;
}

#admin_info {
	font-size: large;
	padding-left: 10px;
}

#red {
	color: red;
}

.hiddenPic {
	display: none;
	float: left;
	margin-top: 25%;
	margin-left: 50%;
}

#middlePos {
	float: left;
	margin-top: 20%;
	margin-left: 40%;
}
</style>
</head>
<body>
<div id="hiddenDiv" class="hiddenPic"><img
	src="/images/icons/loading-indicator.gif" alt="Loading" title="Loading"
	height="50" width="50"></div>
<c:if test="${message!=null}">
	<div id="middlePos"><b>${message}</b></div>
</c:if>
<c:if test="${message==null}">
	<div id="formDiv">
	<form method="post" action="/createcompany"
		onsubmit="return validation(this)"><strong> Create
	Company</strong><br>
	<br>
	<table>
	<tr>
			<td>Display Name</td>
			<td><input type="text" name="displayName" id="displayName"/></td>
		</tr>
		<tr>
			<td>Name</td>
			<td><input type="text" name="name" id="name"
				onClick="document.getElementById('nameLabel').innerHTML='';" /></td>
			<td><label id="nameLabel" style="font-size: small"></label></td>
		</tr>
		<tr>
			<td>Number of users</td>
			<td><input type="text" name="nooofusers" id="nooofusers"
				onClick="document.getElementById('noOfUserLabel').innerHTML='';" /></td>
			<td><label id="noOfUserLabel" style="font-size: small"></label></td>
		</tr>
		<tr>
			<td>Number of Lite users</td>
			<td><input type="text" name="noofliteusers" id="noofliteusers"
				onClick="document.getElementById('noOfUserLabel').innerHTML='';" /></td>
			<td><label id="noOfLiteUserLabel" style="font-size: small"></label></td>
		</tr>
		<tr>
			<td>Company Type</td>
			<td><select name="companyType" style="width: 85%">
				<option value="1">UK</option>
				<option value="0">US</option>
			</select></td>
		</tr>
		<tr>
			<td>Password</td>
			<td><input type="password" name="companypwd" id="companypwd" onblur="companyPassword()"
				onClick="document.getElementById('companyPasswordLabel').innerHTML='';" /></td>
			<td><label id="companyPasswordLabel" style="font-size: small"></label></td>
		</tr>

		<tr>
			<td><span id="admin_info">Default User Info</span></td>
		</tr>
		<tr>
			<td>EmailID<span id=red>*</span></td>
			<td><input type="text" name="emailid" id="emailid"
				 /></td>
			<td><label id="emailLabel" style="font-size: small"></label></td>
		</tr>
		<tr>
			<td>Password<span id=red>*</span></td>
			<td><input type="password" name="password" id="pwd"
				onClick="document.getElementById('passwordLabel').innerHTML='';" /></td>
			<td><label id="passwordLabel" style="font-size: small"></label></td>
		</tr>
		<tr>
			<td>Confirm Password<span id=red>*</span></td>
			<td><input type="password" name="password" id="con_pwd" /></td>
		</tr>
		<tr>
			<td>User Name<span id=red>*</span></td>
			<td><input type="text" name="userName" id="userName" size="20"
				onClick="document.getElementById('passwordLabel').innerHTML='';" /></td>
			<td><label id="userNameLabel" style="font-size: small"></label></td>
		</tr>
		<tr>
			<td style="float: left;">Address</td>
			<td><textarea rows="3" cols="30" style="border-width: thin;"
				name="address"></textarea></td>
		</tr>
		<tr>
			<td>city</td>
			<td><input type="text" name="city" /></td>
		</tr>
		<tr>
			<td>Country</td>
			<td><input type="text" name="country" /></td>
		</tr>
		<tr>
			<td>Zip Code</td>
			<td><input type="text" name="zip" /></td>
		</tr>
		<tr>
			<td>Provence</td>
			<td><input type="text" name="provence" /></td>
		</tr>
		<tr>
			<td>Mobile No.</td>
			<td><input type="text" name="mobile" /></td>
		</tr>
		<tr>
			<td>Working ph.</td>
			<td><input type="text" name="workingPh" /></td>
		</tr>
		<tr>
			<td>Holiday year</td>
		</tr>
		<tr>
			<td>Start Date</td>

			<td><select name="startDateDate" id="startDateDate">
				<%
					for (int i = 1; i <= 31; i++) {
				%>
				<option value="<%=i%>"><%=i%></option>
				<%
					}
				%>
			</select> <select name="startDateMonth" id="startDateMonth"
				onChange="resetDates('startDateMonth','startDateDate')">
				<option value="0">January</option>
				<option value="1">February</option>
				<option value="2">March</option>
				<option value="3">April</option>
				<option value="4">May</option>
				<option value="5">June</option>
				<option value="6">July</option>
				<option value="7">August</option>
				<option value="8">September</option>
				<option value="9">October</option>
				<option value="10">November</option>
				<option value="11">December</option>
			</select></td>
		</tr>
			
		<tr>
		
		<td>Time Zone</td>
		<td><select name="timeZone" id="timeZone">
			
			<option value="England,GMT +0:00">England,GMT +0:00</option><option value="Alaska,GMT -9:00">Alaska,GMT -9:00</option><option value="California,GMT -8:00">California,GMT -8:00</option><option value="Colorado,GMT -7:00">Colorado,GMT -7:00</option><option value="Hawaii,GMT -10:00">Hawaii,GMT -10:00</option><option value="Illinois,GMT -6:00">Illinois,GMT -6:00</option><option value="Michigan,GMT -5:00">Michigan,GMT -5:00</option><option value="New York ,GMT -5:00">New York ,GMT -5:00</option><option value="Kabul,GMT +4:30">Kabul,GMT +4:30</option><option value="Algiers,GMT +1:00">Algiers,GMT +1:00</option><option value="Buenos Aires,GMT -3:00">Buenos Aires,GMT -3:00</option><option value="timeZonesn Capital Territory,GMT +10:00">timeZonesn Capital Territory,GMT +10:00</option><option value="New South Wales,GMT +10:00">New South Wales,GMT +10:00</option><option value="Northern Territory,GMT +9:30">Northern Territory,GMT +9:30</option><option value="Queensland,GMT +10:00">Queensland,GMT +10:00</option><option value="South timeZones,GMT +9:30">South timeZones,GMT +9:30</option><option value="Victoria,GMT +10:00">Victoria,GMT +10:00</option><option value="Western timeZones,GMT +8:00">Western timeZones,GMT +8:00</option><option value="Vienna,GMT +1:00">Vienna,GMT +1:00</option><option value="Albania,GMT + 1:00">Albania,GMT + 1:00</option><option value="American Samoa,GMT - 11:00">American Samoa,GMT - 11:00</option><option value="Andorra,GMT + 1:00">Andorra,GMT + 1:00</option><option value="Angola,GMT + 1:00">Angola,GMT + 1:00</option><option value="Anguilla,GMT - 4:00">Anguilla,GMT - 4:00</option><option value="Antarctica,GMT + 0:00">Antarctica,GMT + 0:00</option><option value="Antigua and Barbuda,GMT - 4:00">Antigua and Barbuda,GMT - 4:00</option><option value="Armenia,GMT + 4:00">Armenia,GMT + 4:00</option><option value="Aruba,GMT - 4:00">Aruba,GMT - 4:00</option><option value="Austria,GMT + 1:00">Austria,GMT + 1:00</option><option value="Azerbaijan,GMT + 4:00">Azerbaijan,GMT + 4:00</option><option value="Nassau,GMT -5:00">Nassau,GMT -5:00</option><option value="Dhaka,GMT +6:00">Dhaka,GMT +6:00</option><option value="Minsk,GMT +2:00">Minsk,GMT +2:00</option><option value="Brussels,GMT +1:00">Brussels,GMT +1:00</option><option value="La Paz,GMT -4:00">La Paz,GMT -4:00</option><option value="S?o Paulo,GMT -3:00">S?o Paulo,GMT -3:00</option><option value="Sofia,GMT +2:00">Sofia,GMT +2:00</option><option value="Bahrain,GMT + 3:00">Bahrain,GMT + 3:00</option><option value="Barbados,GMT - 4:00">Barbados,GMT - 4:00</option><option value="Belize,GMT - 6:00">Belize,GMT - 6:00</option><option value="Benin,GMT + 1:00">Benin,GMT + 1:00</option><option value="Bermuda,GMT - 4:00">Bermuda,GMT - 4:00</option><option value="Bhutan,GMT + 6:00">Bhutan,GMT + 6:00</option><option value="Bosnia and Herzegovina,GMT + 1:00">Bosnia and Herzegovina,GMT + 1:00</option><option value="Botswana,GMT + 2:00">Botswana,GMT + 2:00</option><option value="British Indian Ocean Territory,GMT + 6:00">British Indian Ocean Territory,GMT + 6:00</option><option value="Brunei,GMT + 8:00">Brunei,GMT + 8:00</option><option value="Burkina Faso,GMT + 0:00">Burkina Faso,GMT + 0:00</option><option value="Burundi,GMT + 2:00">Burundi,GMT + 2:00</option><option value="Bouvet Island,GMT + 0:00">Bouvet Island,GMT + 0:00</option><option value="Alberta,GMT -7:00">Alberta,GMT -7:00</option><option value="British Columbia,GMT -8:00">British Columbia,GMT -8:00</option><option value="Manitoba,GMT -6:00">Manitoba,GMT -6:00</option><option value="Newfoundland and Labrador,GMT -3:30">Newfoundland and Labrador,GMT -3:30</option><option value="Nova Scotia,GMT -4:00">Nova Scotia,GMT -4:00</option><option value="Ontario,GMT -5:00">Ontario,GMT -5:00</option><option value="Quebec,GMT -5:00">Quebec,GMT -5:00</option><option value="Santiago,GMT -4:00">Santiago,GMT -4:00</option><option value="Shanghai,GMT +8:00">Shanghai,GMT +8:00</option><option value="Bogota,GMT -5:00">Bogota,GMT -5:00</option><option value="Zagreb,GMT +1:00">Zagreb,GMT +1:00</option><option value="Havana,GMT -5:00">Havana,GMT -5:00</option><option value="Prague,GMT +1:00">Prague,GMT +1:00</option><option value="Cambodia,GMT + 7:00">Cambodia,GMT + 7:00</option><option value="Cameroon,GMT + 1:00">Cameroon,GMT + 1:00</option><option value="Cape Verde,GMT - 1:00">Cape Verde,GMT - 1:00</option><option value="Cayman Islands,GMT - 5:00">Cayman Islands,GMT - 5:00</option><option value="Central African Republic,GMT + 1:00">Central African Republic,GMT + 1:00</option><option value="Chad,GMT + 1:00">Chad,GMT + 1:00</option><option value="Christmas Island,GMT + 7:00">Christmas Island,GMT + 7:00</option><option value="Cocos (Keeling) Islands,GMT + 7:00">Cocos (Keeling) Islands,GMT + 7:00</option><option value="Comoros,GMT + 3:00">Comoros,GMT + 3:00</option><option value="Congo, Democratic Republic of the,GMT + 1:00">Congo, Democratic Republic of the,GMT + 1:00</option><option value="Congo, Republic of the,GMT + 1:00">Congo, Republic of the,GMT + 1:00</option><option value="Cook Islands,GMT - 10:00">Cook Islands,GMT - 10:00</option><option value="Costa Rica,GMT - 6:00">Costa Rica,GMT - 6:00</option><option value="Cote d'Ivoire,GMT + 0:00">Cote d'Ivoire,GMT + 0:00</option><option value="Cyprus,GMT + 2:00">Cyprus,GMT + 2:00</option><option value="Copenhagen,GMT +1:00">Copenhagen,GMT +1:00</option><option value="Santo Domingo,GMT -4:00">Santo Domingo,GMT -4:00</option><option value="Djibouti,GMT + 3:00">Djibouti,GMT + 3:00</option><option value="Dominica,GMT - 4:00">Dominica,GMT - 4:00</option><option value="Cairo,GMT +2:00">Cairo,GMT +2:00</option><option value="San Salvador,GMT -6:00">San Salvador,GMT -6:00</option><option value="Tallinn,GMT +2:00">Tallinn,GMT +2:00</option><option value="Addis Ababa,GMT +3:00">Addis Ababa,GMT +3:00</option><option value="Ecuador,GMT - 5:00">Ecuador,GMT - 5:00</option><option value="Equatorial Guinea,GMT + 1:00">Equatorial Guinea,GMT + 1:00</option><option value="Eritrea,GMT + 3:00">Eritrea,GMT + 3:00</option><option value="Suva,GMT +12:00">Suva,GMT +12:00</option><option value="GMT +2:00">GMT +2:00</option><option value="Paris,GMT +1:00">Paris,GMT +1:00</option><option value="French Guiana,GMT - 3:00">French Guiana,GMT - 3:00</option><option value="Faroe Islands,GMT + 0:00">Faroe Islands,GMT + 0:00</option><option value="Falkland Islands (Islas Malvinas),GMT - 3:00">Falkland Islands (Islas Malvinas),GMT - 3:00</option><option value="French Polynesia,GMT + 13:00">French Polynesia,GMT + 13:00</option><option value="French Southern and Antarctic Lands,GMT + 13:00">French Southern and Antarctic Lands,GMT + 13:00</option><option value="Berlin,GMT +1:00">Berlin,GMT +1:00</option><option value="Athens,GMT +2:00">Athens,GMT +2:00</option><option value="Guatemala,GMT -6:00">Guatemala,GMT -6:00</option><option value="Gabon,GMT + 1:00">Gabon,GMT + 1:00</option><option value="Gambia,GMT + 0:00">Gambia,GMT + 0:00</option><option value="Georgia,GMT + 4:00">Georgia,GMT + 4:00</option><option value="Ghana,GMT + 0:00">Ghana,GMT + 0:00</option><option value="Gibraltar,GMT + 1:00">Gibraltar,GMT + 1:00</option><option value="Greenland,GMT - 3:00">Greenland,GMT - 3:00</option><option value="Grenada,GMT - 4:00">Grenada,GMT - 4:00</option><option value="Guadeloupe,GMT - 4:00">Guadeloupe,GMT - 4:00</option><option value="Guam,GMT + 10:00">Guam,GMT + 10:00</option><option value="Guinea,GMT + 0:00">Guinea,GMT + 0:00</option><option value="Guinea-Bissau,GMT + 0:00">Guinea-Bissau,GMT + 0:00</option><option value="Guyana,GMT - 4:00">Guyana,GMT - 4:00</option><option value="Tegucigalpa,GMT -6:00">Tegucigalpa,GMT -6:00</option><option value="Hong Kong,GMT +8:00">Hong Kong,GMT +8:00</option><option value="Budapest,GMT +1:00">Budapest,GMT +1:00</option><option value="Haiti,GMT - 5:00">Haiti,GMT - 5:00</option><option value="Holy See (Vatican City),GMT + 2:00">Holy See (Vatican City),GMT + 2:00</option><option value="Heard Island and McDonald Islands,GMT + 5:00">Heard Island and McDonald Islands,GMT + 5:00</option><option value="Reykjavik,GMT +0:00">Reykjavik,GMT +0:00</option><option value="New Delhi,GMT +5:30">New Delhi,GMT +5:30</option><option value="Mumbai,GMT +5:30">Mumbai,GMT +5:30</option><option value="Kolkata,GMT +5:30">Kolkata,GMT +5:30</option><option value="Jakarta,GMT +7:00">Jakarta,GMT +7:00</option><option value="Tehran,GMT +3:30">Tehran,GMT +3:30</option><option value="Baghdad,MT +3:00">Baghdad,MT +3:00</option><option value="Dublin,GMT +0:00">Dublin,GMT +0:00</option><option value="Jerusalem,GMT +2:00">Jerusalem,GMT +2:00</option><option value="Rome,GMT +1:00">Rome,GMT +1:00</option><option value="Rome,GMT +0:00">Rome,GMT +0:00</option><option value="Kingston,MT -5:00">Kingston,MT -5:00</option><option value="Tokyo,GMT +9:00">Tokyo,GMT +9:00</option><option value="Amman,GMT +2:00">Amman,GMT +2:00</option><option value="Almaty,GMT +6:00">Almaty,GMT +6:00</option><option value="Nairobi,GMT +3:00">Nairobi,GMT +3:00</option><option value="Christmas Island,GMT +14:00">Christmas Island,GMT +14:00</option><option value="Kuwait City,GMT +3:00">Kuwait City,GMT +3:00</option><option value="Korea, North,GMT + 9:00">Korea, North,GMT + 9:00</option><option value="Kyrgyzstan,GMT + 6:00">Kyrgyzstan,GMT + 6:00</option><option value="Beirut,GMT +2:00">Beirut,GMT +2:00</option><option value="Laos,GMT + 7:00">Laos,GMT + 7:00</option><option value="Latvia,GMT + 2:00">Latvia,GMT + 2:00</option><option value="Lesotho,GMT + 2:00">Lesotho,GMT + 2:00</option><option value="Liberia,GMT + 0:00">Liberia,GMT + 0:00</option><option value="Libya,GMT + 2:00">Libya,GMT + 2:00</option><option value="Liechtenstein,GMT + 1:00">Liechtenstein,GMT + 1:00</option><option value="Lithuania,GMT + 2:00">Lithuania,GMT + 2:00</option><option value="Luxembourg,GMT + 1:00">Luxembourg,GMT + 1:00</option><option value="Antananarivo,GMT +3:00">Antananarivo,GMT +3:00</option><option value="Kuala Lumpur,GMT +8:00">Kuala Lumpur,GMT +8:00</option><option value="Federal District,GMT -6:00">Federal District,GMT -6:00</option><option value="Casablanca,GMT +0:00">Casablanca,GMT +0:00</option><option value="Malawi,GMT + 2:00">Malawi,GMT + 2:00</option><option value="Maldives,GMT + 5:00">Maldives,GMT + 5:00</option><option value="Mali,GMT + 0:00">Mali,GMT + 0:00</option><option value="Malta,GMT + 1:00">Malta,GMT + 1:00</option><option value="Marshall Islands,GMT + 12:00">Marshall Islands,GMT + 12:00</option><option value="Martinique,GMT - 4:00">Martinique,GMT - 4:00</option><option value="Mauritania,GMT + 0:00">Mauritania,GMT + 0:00</option><option value="Mauritius,GMT + 4:00">Mauritius,GMT + 4:00</option><option value="Mayotte,GMT + 3:00">Mayotte,GMT + 3:00</option><option value="Chuuk (Truk) and Yap,GMT + 10:00">Chuuk (Truk) and Yap,GMT + 10:00</option><option value="Pohnpei and Kosrae,GMT + 11:00">Pohnpei and Kosrae,GMT + 11:00</option><option value="Moldova,GMT + 2:00">Moldova,GMT + 2:00</option><option value="Monaco,GMT + 1:00">Monaco,GMT + 1:00</option><option value="Mongolia,GMT + 8:00">Mongolia,GMT + 8:00</option><option value="Montserrat,GMT - 4:00">Montserrat,GMT - 4:00</option><option value="Mozambique,GMT + 2:00">Mozambique,GMT + 2:00</option><option value="Macau,GMT + 8:00">Macau,GMT + 8:00</option><option value="Kathmandu,GMT +5:45">Kathmandu,GMT +5:45</option><option value="Amsterdam,GMT +1:00">Amsterdam,GMT +1:00</option><option value="Auckland,GMT +12:00">Auckland,GMT +12:00</option><option value="Chatham Islands,GMT +12:45">Chatham Islands,GMT +12:45</option><option value="Managua,GMT -6:00">Managua,GMT -6:00</option><option value="Lagos,GMT +1:00">Lagos,GMT +1:00</option><option value="Oslo,GMT +1:00">Oslo,GMT +1:00</option><option value="Namibiae,GMT + 2:00">Namibiae,GMT + 2:00</option><option value="Nauru,GMT + 12:00">Nauru,GMT + 12:00</option><option value="Netherlands Antilles,GMT - 4:00">Netherlands Antilles,GMT - 4:00</option><option value="New Caledonia,GMT + 11:00">New Caledonia,GMT + 11:00</option><option value="Nigera,GMT + 1:00">Nigera,GMT + 1:00</option><option value="Niue,GMT - 11:00">Niue,GMT - 11:00</option><option value="Norfolk Island,GMT + 11:30">Norfolk Island,GMT + 11:30</option><option value="Northern Mariana Islands,GMT + 10:00">Northern Mariana Islands,GMT + 10:00</option><option value="Oman,GMT + 4:00">Oman,GMT + 4:00</option><option value="Karachi,GMT +5:00">Karachi,GMT +5:00</option><option value="Asuncion,GMT -4:00">Asuncion,GMT -4:00</option><option value="Lima,GMT -5:00">Lima,GMT -5:00</option><option value="Manila,GMT +8:00">Manila,GMT +8:00</option><option value="Warsaw,GMT +1:00">Warsaw,GMT +1:00</option><option value="Lisbon,GMT +0:00">Lisbon,GMT +0:00</option><option value="San Juan,GMT -3:00">San Juan,GMT -3:00</option><option value="Palau,GMT + 9:00">Palau,GMT + 9:00</option><option value="Panama,GMT - 5:00">Panama,GMT - 5:00</option><option value="Papua New Guinea,GMT + 10:00">Papua New Guinea,GMT + 10:00</option><option value="Pitcairn Islandsa,GMT - 8:00">Pitcairn Islandsa,GMT - 8:00</option><option value="Qatar,GMT + 3:00">Qatar,GMT + 3:00</option><option value="Reunion,GMT + 4:00">Reunion,GMT + 4:00</option><option value="Rwanda,GMT + 2:00">Rwanda,GMT + 2:00</option><option value="Bucharest,GMT +2:00">Bucharest,GMT +2:00</option><option value="Anadyr,GMT +12:00">Anadyr,GMT +12:00</option><option value="Kamchatka,GMT +12:00">Kamchatka,GMT +12:00</option><option value="Moscow,GMT +3:00">Moscow,GMT +3:00</option><option value="Vladivostok,GMT +10:00">Vladivostok,GMT +10:00</option><option value="Riyadh,GMT +3:00">Riyadh,GMT +3:00</option><option value="Belgrade,GMT +1:00">Belgrade,GMT +1:00</option><option value="Singapore,GMT +8:00">Singapore,GMT +8:00</option><option value="Johannesburg,GMT +2:00">Johannesburg,GMT +2:00</option><option value="Seoul,GMT +9:00">Seoul,GMT +9:00</option><option value="Madrid,GMT +1:00">Madrid,GMT +1:00</option><option value="Khartoum,GMT +3:00">Khartoum,GMT +3:00</option><option value="Stockholm,GMT +1:00">Stockholm,GMT +1:00</option><option value="Zurich,GMT +1:00">Zurich,GMT +1:00</option><option value="Saint Helena,GMT + 0:00">Saint Helena,GMT + 0:00</option><option value="Saint Kitts and Nevis,GMT - 4:00">Saint Kitts and Nevis,GMT - 4:00</option><option value="Saint Lucia,GMT - 4:00">Saint Lucia,GMT - 4:00</option><option value="Saint Pierre and Miquelon,GMT - 3:00">Saint Pierre and Miquelon,GMT - 3:00</option><option value="Saint Vincent and the Grenadines,GMT - 4:00">Saint Vincent and the Grenadines,GMT - 4:00</option><option value="Samoa,GMT - 10:00">Samoa,GMT - 10:00</option><option value="San Marino,GMT + 1:00">San Marino,GMT + 1:00</option><option value="Sao Tome and Principe,GMT + 0:00">Sao Tome and Principe,GMT + 0:00</option><option value="Senegal,GMT + 0:00">Senegal,GMT + 0:00</option><option value="Seychelles,GMT + 4:00">Seychelles,GMT + 4:00</option><option value="Sierra Leone,GMT + 0:00">Sierra Leone,GMT + 0:00</option><option value="Slovakia,GMT + 1:00">Slovakia,GMT + 1:00</option><option value="Slovenia,GMT + 1:00">Slovenia,GMT + 1:00</option><option value="Solomon Islands,GMT + 11:00">Solomon Islands,GMT + 11:00</option><option value="Somalia,GMT + 3:00">Somalia,GMT + 3:00</option><option value="South Georgia and the South Sandwich Islands,GMT - 2:00">South Georgia and the South Sandwich Islands,GMT - 2:00</option><option value="Sri Lanka,GMT + 5:30">Sri Lanka,GMT + 5:30</option><option value="Suriname,GMT - 3:00">Suriname,GMT - 3:00</option><option value="Svalbard,GMT + 1:00">Svalbard,GMT + 1:00</option><option value="Swaziland,GMT + 2:00">Swaziland,GMT + 2:00</option><option value="Syria,GMT + 2:00">Syria,GMT + 2:00</option><option value="Serbia and Montenegro,GMT + 1:00">Serbia and Montenegro,GMT + 1:00</option><option value="Taipei,GMT +8:00">Taipei,GMT +8:00</option><option value="Bangkok,GMT +7:00">Bangkok,GMT +7:00</option><option value="Ankara,GMT +2:00">Ankara,GMT +2:00</option><option value="Istanbul,GMT +2:00">Istanbul,GMT +2:00</option><option value="Tajikistan,GMT + 5:00">Tajikistan,GMT + 5:00</option><option value="Tanzania,GMT + 3:00">Tanzania,GMT + 3:00</option><option value="Togo,GMT + 0:00">Togo,GMT + 0:00</option><option value="Tokelau,GMT - 10:00">Tokelau,GMT - 10:00</option><option value="Tonga,GMT + 13:00">Tonga,GMT + 13:00</option><option value="Trinidad and Tobago,GMT - 4:00">Trinidad and Tobago,GMT - 4:00</option><option value="Tunisia,GMT + 1:00">Tunisia,GMT + 1:00</option><option value="Turkmenistan,GMT + 5:00">Turkmenistan,GMT + 5:00</option><option value="Turks and Caicos Islands,GMT - 5:00">Turks and Caicos Islands,GMT - 5:00</option><option value="Tuvalu,GMT + 12:00">Tuvalu,GMT + 12:00</option><option value="Dili,GMT + 9:00">Dili,GMT + 9:00</option><option value="Dubai,GMT +4:00">Dubai,GMT +4:00</option><option value="Montevideo,GMT -3:00">Montevideo,GMT -3:00</option><option value="Tashkent,GMT +5:00">Tashkent,GMT +5:00</option><option value="Uganda,GMT +3:00">Uganda,GMT +3:00</option><option value="Ukraine,GMT +2:00">Ukraine,GMT +2:00</option><option value="Caracas,GMT -4:30">Caracas,GMT -4:30</option><option value="Hanoi,GMT +7:00">Hanoi,GMT +7:00</option><option value="Vanuatu,GMT +11:00">Vanuatu,GMT +11:00</option><option value="Virgin Islands,GMT -4:00">Virgin Islands,GMT -4:00</option><option value="Aden,GMT +3:00">Aden,GMT +3:00</option><option value="Wallis and Futuna,GMT +12:00">Wallis and Futuna,GMT +12:00</option><option value="Western Sahara,GMT +0:00">Western Sahara,GMT +0:00</option><option value="Zambia,GMT +2:00">Zambia,GMT +2:00</option><option value="Harare,GMT +2:00">Harare,GMT +2:00</option>
			
			</select>
		</tr>
		<tr>
			<td></td>
			<td style="float: right;"><input type="submit" name="submit"
				value="Submit" style="" /></td>
		</tr>
	</table>
	</form>
	</div>
</c:if>
</body>
</html>
