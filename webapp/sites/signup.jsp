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
<title> Sign up & Trial free | Accounter
</title>
<meta content="IE=100" http-equiv="X-UA-Compatible">
<% String version = application.getInitParameter("version"); %>
<link rel="shortcut icon" href="../images/favicon.ico" />
<link type="text/css" href="ss.css?version=<%= version%>" rel="stylesheet">
<link type="text/css" href="cmxform.css?version=<%= version%>" rel="stylesheet">\

<script type="text/javascript" src="/jscripts/jquery-1.6.2.js"></script>
<script src="/jscripts/jquery.validate.js" type="text/javascript"></script>
<script type="text/javascript" charset="utf-8">
	var is_ssl = ("https:" == document.location.protocol);
	var asset_host = is_ssl ? "https://s3.amazonaws.com/getsatisfaction.com/" : "http://s3.amazonaws.com/getsatisfaction.com/";
	document.write(unescape("%3Cscript src='" + asset_host + "javascripts/feedback-v2.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript" charset="utf-8">
	var feedback_widget_options = {};

	feedback_widget_options.display = "overlay";  
  	feedback_widget_options.company = "vimukti";
	feedback_widget_options.placement = "left";
	feedback_widget_options.color = "#222";
	feedback_widget_options.style = "idea";
	
	var feedback_widget = new GSFN.feedback_widget(feedback_widget_options);
</script>
<script  type="text/javascript" >
$.validator.setDefaults({
	submitHandler: function() {
		$.blockUI({ message: $("#hiddenLoaderDiv"), css: {height: '80px', width: '300px', display: 'block'} });
		$('#accounterForm').submit();
	}
});
$.validator.addMethod("no_special_characters", function(value, element) {
    return this.optional(element) ||
        value.match(/^[a-zA-Z0-9_]*[a-zA-Z][a-zA-Z0-9_]*$/);
}, "Company ID shouldn't contain special characters");

	$(document).ready(function() {
	    var is_opera = navigator.userAgent.toLowerCase().indexOf('opera') > -1;
	    $('#select-box').keydown(function(event){
		    if(event.which == 9){
		       $('#checkbox').focus();
		    } 
	    });
		$('#submitButton').click(function() {
			$("#accounterForm").validate({
		rules: {
			firstName: "required",
			lastName: "required",
			companyName: {
				required: true,
				no_special_characters: true,
				maxlength: 64
			},
			companyFullName: "required",
			password: {
				required: true,
				minlength: 6
			},
			confirmPassword: {
				required: true,
				minlength: 6,
				equalTo: "#mid-box4"
			},
			emailId: {
				required: true,
				email: true
			},
			agree: "required"
		},
		messages: {
			firstName: "Please enter your first name",
			lastName: "Please enter your last name",
			companyName: "Please enter company ID",
			companyName: {
				required: "Please enter company ID",
				no_special_characters: "Company ID should contain atleast one letter and no special characters",
				maxlength: "Company ID exceeded the maximum length"
			},
			companyFullName: "Please enter company name",
			password: {
				required: "Please provide a password",
				minlength: "Your password must be at least 6 characters long"
			},
			confirmPassword: {
				required: "Please provide a password",
				minlength: "Your password must be at least 6 characters long",
				equalTo: "Please enter the same password as above"
			},
			emailId: "Please enter a valid email address",
			agree: "Please accept Terms of use"
		}
	});
			 <!--$.blockUI({ message: $('#hiddenLoaderDiv'), css: {height: '80px', width: '300px'} });--> 
			<!--document.getElementById("hiddenLoaderDiv").style.display="block";-->
    		<!--document.getElementById("signup").style.display="none";-->
		});
	});
</script>

<%
   String app = request.getHeader( "Nativeapp" );
   boolean isNative = ( app != null && !app.equals(""));
   if( isNative ){ %>
   <link type="text/css" rel="stylesheet" href="/nativeLogin.css?version=<%= version%>">
   <% } %>
   
   

</head>
<body>
	<div class ="body-container">
	<%@ include file="./header.jsp" %>
		<div class="middle-part" id="cen">
			<div class="middle-signup-box" id="mid">
				<c:if test="${successmessage==null}">
				<h2>Create Company</h2>
				</c:if>
					
							<c:if test="${successmessage!=null}">
							<table id="signup-fields" class="signup-fields-box success-box">
							<tr>
									<td ><div class="signup-box-left-top"></div></td>
									<td ><div  class="signup-box-top-middle"></div></td>
									<td ><div class="signup-box-right-top"></div></td>
								</tr>
								<tr>
									<td class="signup-box-left-middle" ></td>
									<td class="signup-box">
								<div id="successDiv" class="successDiv"><b style="color: #3299A4; font-size: 17px;">${successmessage}</b></div>
								</td>
									<td class="signup-box-right-middle"></td>
									</tr>
								<tr>
									<td ><div class="signup-box-left-bottom"></div></td>
									<td ><div class="signup-box-bottom-middle"></div></td>
									<td ><div class="signup-box-right-bottom"></div></td>
								</tr>
			                    <tr>
			                        <td></td>
			                        <td><div class="remaining-header-part-login success_login">
										<ul>
										   <li><a href="/site/login" id="login-id" >Login</a></li>
										</ul>
									</div></td>
			                        <td></td>
			                    </tr>
							</table>
							</c:if>
							<div id="hiddenLoaderDiv" class="hiddenDiv">
								<img src="/images/icons/loading-indicator.gif" height="50" width="50">
								<span style="position: absolute; margin: 10px;">Please Wait...</span>
							</div>
							<c:if test="${successmessage==null}">
							<div id="signup" class="left-side-box">
								<form id="accounterForm" method="post" action="/site/signup"">
								<fieldset class="fieldset"">
								<table id="signup-fields" class="signup-fields-box">
								<c:if test="${errormessage != null}">
								<span style="color: red; line-height: 1.5;">
								${errormessage} </span>
								<br><br>
								</c:if>
								<tr>
									<td ><div class="signup-box-left-top"></div></td>
									<td ><div  class="signup-box-top-middle"></div></td>
									<td ><div class="signup-box-right-top"></div></td>
								</tr>
								<tr>
									<td class="signup-box-left-middle" ></td>
									<td class="signup-box">
									<h3 style="color:#3299A4">Company Details</h3>

									<table>
										<tr>
											 <td style="width : 155px">Company Full Name <span id=red>*</span> </td>
											 <td class="company-full-name">
												 <input id="mid-box3"  type="text" tabindex="1" name="companyFullName">
											 </td>
										</tr>
										<tr style="height:10px">
											<td></td>
									 		<td><h6 class="text-style" style="font-size: 10px;color:#A0A0A0; margin-bottom: 2px;margin-top:-4px\0">ex: Vimukti Technologies Pvt Ltd</h6></td>
										</tr>
										<tr>
											 <td style="width : 155px">Company ID <span id=red>*</span> </td>
											 <td class="company-id-class">
												 <input id="mid-box7"  type="text" tabindex="2" name="companyName" >
											 </td>
										</tr>
										<tr style="height:10px">
											<td></td>
									 		<td><h6 class="text-style" style="font-size: 10px; margin-bottom: 2px;color:#A0A0A0;margin-top:-4px\0">ex: vimukti</h6></td>
										</tr>
								  		<tr>
									 		<td style="width : 155px">Company Type <span id=red>*</span> </td>
									 		<td>
										 		<select id="select-box" tabindex="3" name="companyType">
										 			<option value="1">UK</option>
													<option value="0">US</option>
												</select>
									 		</td>
								  		</tr>
								  	</table>
									</td>
									<td class="signup-box-right-middle"></td>
								</tr>
								<tr>
									<td ><div class="signup-box-left-bottom"></div></td>
									<td ><div class="signup-box-bottom-middle"></div></td>
									<td ><div class="signup-box-right-bottom"></div></td>
								</tr>
                           </table>
                           <table id="signup-fields" class="signup-fields-box">
								<tr>
									<td ><div class="signup-box-left-top"></div></td>
									<td ><div  class="signup-box-top-middle"></div></td>
									<td ><div class="signup-box-right-top"></div></td>
								</tr>
								<tr>
									<td class="signup-box-left-middle" ></td>
									<td class="signup-box">
									<h3 style="color:#3299A4">Admin Details</h3>
									<table>
										<tr>
									 <td style="width : 155px">First Name <span id=red>*</span> </td>
									 <td>
										<input id="mid-box"  type="text" tabindex="4" name="firstName">
					
									 </td>
								  </tr>
								  <tr>
									<td style="width : 155px">Last Name <span id=red>*</span> </td>
									<td>
										 <input id="mid-box1"  type="text" tabindex="5" name="lastName">
										</td>
								  </tr>
								  <tr>
									 <td style="width : 155px">Email ID <span id=red>*</span> </td>
									 <td>
										<input id="mid-box2"  type="text" tabindex="6" name="emailId">
										</td>
								  </tr>
								  <tr>
									 <td style="width : 155px">Password <span id=red>*</span> </td>
									 <td>
										 <input id="mid-box4"  type="password" tabindex="7" name="password">
									 </td>
								  </tr>
								  <tr>
									 <td style="width : 155px">Confirm Password <span id=red>*</span> </td>
									 <td>
										 <input id="mid-box5"  type="password" tabindex="8" name="confirmPassword">
									 </td>
								  </tr>
								  <tr>
									 <td style="width : 155px">Phone Number </td>
									 <td>
										 <input id="mid-box6"  type="text" tabindex="9" name="phoneNumber">
										</td>
								  </tr>
								  <tr>
									 <td style="width : 155px">Country </td>
									 <td>
										 <select id="select-box" tabindex="10" name="country">
										 	<option value="United Kingdom">United Kingdom</option>
											<option value="United States">United States</option>
											<option value="">---------------------</option>
											<option value="Afghanistan">Afghanistan</option>
											<option value="Albania">Albania</option>
											<option value="Algeria">Algeria</option>
											<option value="American Samoa">American Samoa</option>
											<option value="Andorra">Andorra</option>
											<option value="Angola">Angola</option>
											<option value="Anguilla">Anguilla</option>
											<option value="Antarctica">Antarctica</option>
											<option value="Antigua and Barbuda">Antigua and Barbuda</option>
											<option value="Argentina">Argentina</option>
											<option value="Armenia">Armenia</option>
											<option value="Aruba">Aruba</option>
											<option value="Austria">Austria</option>
											<option value="Australia">Australia</option>
											<option value="Azerbaijan">Azerbaijan</option>
											<option value="Bahamas, The">Bahamas, The</option>
											<option value="Bahrain">Bahrain</option>
											<option value="Bangladesh">Bangladesh</option>
											<option value="Barbados">Barbados</option>
											<option value="Belarus">Belarus</option>
											<option value="Belgium">Belgium</option>
											<option value="Belize">Belize</option>
											<option value="Benin">Benin</option>
											<option value="Bermuda">Bermuda</option>
											<option value="Bhutan">Bhutan</option>
											<option value="Bolivia">Bolivia</option>
											<option value="Bosnia and Herzegovina">Bosnia and Herzegovina</option>
											<option value="Botswana">Botswana</option>
											<option value="Bouvet Island">Bouvet Island</option>
											<option value="Brazil">Brazil</option>
											<option value="British Indian Ocean Territory">British Indian Ocean Territory</option>
											<option value="Brunei">Brunei</option>
											<option value="Bulgaria">Bulgaria</option>
											<option value="Burkina Faso">Burkina Faso</option>
											<option value="Burundi">Burundi</option>
											<option value="Cambodia">Cambodia</option>
											<option value="Cameroon">Cameroon</option>
											<option value="Canada">Canada</option>
											<option value="Cape Verde">Cape Verde</option>
											<option value="Cayman Islands">Cayman Islands</option>
											<option value="Central African Republic">Central African Republic</option>
											<option value="Chad">Chad</option>
											<option value="Chile">Chile</option>
											<option value="China">China</option>
											<option value="Christmas Island">Christmas Island</option>
											<option value="Cocos (Keeling) Islands">Cocos (Keeling) Islands</option>
											<option value="Colombia">Colombia</option>
											<option value="Comoros">Comoros</option>
											<option value="Congo, Democratic Republic of the">Congo, Democratic Republic of the</option>
											<option value="Congo, Republic of the">Congo, Republic of the</option>
											<option value="Cook Islands">Cook Islands</option>
											<option value="Costa Rica">Costa Rica</option>
											<option value="Cote d'Ivoire">Cote d'Ivoire</option>
											<option value="Croatia">Croatia</option>
											<option value="Cuba">Cuba</option>
											<option value="Cyprus">Cyprus</option>
											<option value="Czech Republic">Czech Republic</option>
											<option value="Denmark">Denmark</option>
											<option value="Djibouti">Djibouti</option>
											<option value="Dominica">Dominica</option>
											<option value="Dominican Republic">Dominican Republic</option>
											<option value="Ecuador">Ecuador</option>
											<option value="Egypt">Egypt</option>
											<option value="El Salvador">El Salvador</option>
											<option value="Equatorial Guinea">Equatorial Guinea</option>
											<option value="Eritrea">Eritrea</option>
											<option value="Estonia">Estonia</option>
											<option value="Ethiopia">Ethiopia</option>
											<option value="Falkland Islands (Islas Malvinas)">Falkland Islands (Islas Malvinas)</option>
											<option value="Faroe Islands">Faroe Islands</option>
											<option value="Fiji">Fiji</option>
											<option value="Finland">Finland</option>
											<option value="France">France</option>
											<option value="French Guiana">French Guiana</option>
											<option value="French Polynesia">French Polynesia</option>
											<option value="French Southern and Antarctic Lands">French Southern and Antarctic Lands</option>
											<option value="Gabon">Gabon</option>
											<option value="Gambia, The">Gambia, The</option>
											<option value="Georgia">Georgia</option>
											<option value="Germany">Germany</option>
											<option value="Ghana">Ghana</option>
											<option value="Gibraltar">Gibraltar</option>
											<option value="Greece">Greece</option>
											<option value="Greenland">Greenland</option>
											<option value="Grenada">Grenada</option>
											<option value="Guadeloupe">Guadeloupe</option>
											<option value="Guam">Guam</option>
											<option value="Guatemala">Guatemala</option>
											<option value="Guinea">Guinea</option>
											<option value="Guinea-Bissau">Guinea-Bissau</option>
											<option value="Guyana">Guyana</option>
											<option value="Haiti">Haiti</option>
											<option value="Heard Island and McDonald Islands">Heard Island and McDonald Islands</option>
											<option value="Holy See (Vatican City)">Holy See (Vatican City)</option>
											<option value="Honduras">Honduras</option>
											<option value="Hong Kong">Hong Kong</option>
											<option value="Hungary">Hungary</option>
											<option value="Iceland">Iceland</option>
											<option value="India">India</option>
											<option value="Indonesia">Indonesia</option>
											<option value="Iran">Iran</option>
											<option value="Iraq">Iraq</option>
											<option value="Ireland">Ireland</option>
											<option value="Isle of Man">Isle of Man</option>
											<option value="Israel">Israel</option>
											<option value="Italy">Italy</option>
											<option value="Jamaica">Jamaica</option>
											<option value="Jan Mayen">Jan Mayen</option>
											<option value="Japan">Japan</option>
											<option value="Jordan">Jordan</option>
											<option value="Kazakhstan">Kazakhstan</option>
											<option value="Kenya">Kenya</option>
											<option value="Kiribati">Kiribati</option>
											<option value="Korea, North">Korea, North</option>
											<option value="Korea, South">Korea, South</option>
											<option value="Kuwait">Kuwait</option>
											<option value="Kyrgyzstan">Kyrgyzstan</option>
											<option value="Laos">Laos</option>
											<option value="Latvia">Latvia</option>
											<option value="Lebanon">Lebanon</option>
											<option value="Lesotho">Lesotho</option>
											<option value="Liberia">Liberia</option>
											<option value="Libya">Libya</option>
											<option value="Liechtenstein">Liechtenstein</option>
											<option value="Lithuania">Lithuania</option>
											<option value="Luxembourg">Luxembourg</option>
											<option value="Macau">Macau</option>
											<option value="Macedonia">Macedonia</option>
											<option value="Madagascar">Madagascar</option>
											<option value="Malawi">Malawi</option>
											<option value="Malaysia">Malaysia</option>
											<option value="Maldives">Maldives</option>
											<option value="Mali">Mali</option>
											<option value="Malta">Malta</option>
											<option value="Marshall Islands">Marshall Islands</option>
											<option value="Martinique">Martinique</option>
											<option value="Mauritania">Mauritania</option>
											<option value="Mauritius">Mauritius</option>
											<option value="Mayotte">Mayotte</option>
											<option value="Mexico">Mexico</option>
											<option value="Micronesia, Federated States of">Micronesia, Federated States of</option>
											<option value="Moldova">Moldova</option>
											<option value="Monaco">Monaco</option>
											<option value="Mongolia">Mongolia</option>
											<option value="Montserrat">Montserrat</option>
											<option value="Morocco">Morocco</option>
											<option value="Mozambique">Mozambique</option>
											<option value="Namibia">Namibia</option>
											<option value="Nauru">Nauru</option>
											<option value="Nepal">Nepal</option>
											<option value="Netherlands">Netherlands</option>
											<option value="Netherlands Antilles">Netherlands Antilles</option>
											<option value="New Caledonia">New Caledonia</option>
											<option value="New Zealand">New Zealand</option>
											<option value="Nicaragua">Nicaragua</option>
											<option value="Niger">Niger</option>
											<option value="Nigeria">Nigeria</option>
											<option value="Niue">Niue</option>
											<option value="Norfolk Island">Norfolk Island</option>
											<option value="Northern Mariana Islands">Northern Mariana Islands</option>
											<option value="Norway">Norway</option>
											<option value="Oman">Oman</option>
											<option value="Pakistan">Pakistan</option>
											<option value="Palau">Palau</option>
											<option value="Panama">Panama</option>
											<option value="Papua New Guinea">Papua New Guinea</option>
											<option value="Paraguay">Paraguay</option>
											<option value="Peru">Peru</option>
											<option value="Philippines">Philippines</option>
											<option value="Pitcairn Islands">Pitcairn Islands</option>
											<option value="Poland">Poland</option>
											<option value="Portugal">Portugal</option>
											<option value="Puerto Rico">Puerto Rico</option>
											<option value="Qatar">Qatar</option>
											<option value="Reunion">Reunion</option>
											<option value="Romania">Romania</option>
											<option value="Russia">Russia</option>
											<option value="Rwanda">Rwanda</option>
											<option value="Saint Helena">Saint Helena</option>
											<option value="Saint Kitts and Nevis">Saint Kitts and Nevis</option>
											<option value="Saint Lucia">Saint Lucia</option>
											<option value="Saint Pierre and Miquelon">Saint Pierre and Miquelon</option>
											<option value="Saint Vincent and the Grenadines">Saint Vincent and the Grenadines</option>
											<option value="Samoa">Samoa</option>
											<option value="San Marino">San Marino</option>
											<option value="Sao Tome and Principe">Sao Tome and Principe</option>
											<option value="Saudi Arabia">Saudi Arabia</option>
											<option value="Senegal">Senegal</option>
											<option value="Serbia and Montenegro">Serbia and Montenegro</option>
											<option value="Seychelles">Seychelles</option>
											<option value="Sierra Leone">Sierra Leone</option>
											<option value="Singapore">Singapore</option>
											<option value="Slovakia">Slovakia</option>
											<option value="Slovenia">Slovenia</option>
											<option value="Solomon Islands">Solomon Islands</option>
											<option value="Somalia">Somalia</option>
											<option value="South Africa">South Africa</option>
											<option value="South Georgia and the South Sandwich Islands">South Georgia and the South Sandwich Islands</option>
											<option value="Spain">Spain</option>
											<option value="Sri Lanka">Sri Lanka</option>
											<option value="Sudan">Sudan</option>
											<option value="Suriname">Suriname</option>
											<option value="Svalbard">Svalbard</option>
											<option value="Swaziland">Swaziland</option>
											<option value="Sweden">Sweden</option>
											<option value="Switzerland">Switzerland</option>
											<option value="Syria">Syria</option>
											<option value="Taiwan">Taiwan</option>
											<option value="Tajikistan">Tajikistan</option>
											<option value="Tanzania">Tanzania</option>
											<option value="Thailand">Thailand</option>
											<option value="Timor-Leste">Timor-Leste</option>
											<option value="Togo">Togo</option>
											<option value="Tokelau">Tokelau</option>
											<option value="Tonga">Tonga</option>
											<option value="Trinidad and Tobago">Trinidad and Tobago</option>
											<option value="Tunisia">Tunisia</option>
											<option value="Turkey">Turkey</option>
											<option value="Turkmenistan">Turkmenistan</option>
											<option value="Turks and Caicos Islands">Turks and Caicos Islands</option>
											<option value="Tuvalu">Tuvalu</option>
											<option value="Uganda">Uganda</option>
											<option value="Ukraine">Ukraine</option>
											<option value="United Arab Emirates">United Arab Emirates</option>
											<option value="Uruguay">Uruguay</option>
											<option value="Uzbekistan">Uzbekistan</option>
											<option value="Vanuatu">Vanuatu</option>
											<option value="Venezuela">Venezuela</option>
											<option value="Vietnam">Vietnam</option>
											<option value="Virgin Islands">Virgin Islands</option>
											<option value="Wallis and Futuna">Wallis and Futuna</option>
											<option value="Western Sahara">Western Sahara</option>
											<option value="Yemen">Yemen</option>
											<option value="Zambia">Zambia</option>
											<option value="Zimbabwe">Zimbabwe</option>
										</select>
									 </td>
								  </tr>
								  	</table>
									</td>
									<td class="signup-box-right-middle"></td>
								</tr>
								<tr>
									<td ><div class="signup-box-left-bottom"></div></td>
									<td ><div class="signup-box-bottom-middle"></div></td>
									<td ><div class="signup-box-right-bottom"></div></td>
								</tr>
								</table>
							   <!--<h5>Got a promo code? <a href="test.htm" tabindex="10">Enter it now</a></h5><br>-->
							   <div class="terms-error" style="margin-left: 30px;height:35px;">
							   <!--<div class="sign-up-divider"></div>-->
								<span style="margin-left:13px;color:#000">I have read and I accept the</span> <a href="/site/termsandconditions" tabindex="12">Terms of Use</a><input id="checkbox" type="checkbox" name="agree" tabindex="11" ></input></div>
								
								<ul class="getstarted-submit-button">
								     <li><span class="signup-submit-left"></span></li>
								     <li><input id="submitButton" type="submit" class="signup-submit-mid" name="getstarted" value="Get Started" tabindex="13" ></input></li>
								     <li><span class="signup-submit-right"></span></li>
								 </ul>
								
								
								<!--<input id="submitButton" type="submit" class="signup-but" name="getstarted" value="Get Started" tabindex="13" ></input>-->
								<div style="margin-top: 70px; margin-left: 35px;"><span style="font-size: 13px;color:#000">Note : You can invite more users after you login</span></div>
								</table>
								</fieldset>
								</form>
								
							</div>
							</c:if>
					
				<div class="signup-box-left-bot"></div>
				<div id="vertical-line" class="vertical-line" ></div>
				<div id="right-side-options" class="right-side-options">
						<div class="first-list">
							<div class="try">
									<h4>Signup Free</h4>
							</div>
							<div class="try-list">
								  <ul>
									<li>No setup fees</li></br>
									<li>No contracts</li></br>
									<li>No credit card is required</li></br>
									<li>Pay only when ready</li>
								  </ul>
							</div>
						</div>
						<div class ="second-list" >
							<div class="ben">
								<h4>Benefits</h4>
							</div>
							<div class="ben-list">
								  <ul>
									<li>Automatic bank feeds</li></br>
									<li>Powerful invoicing solution</li></br>
									<li>Interactive real-time reporting</li></br>
									<li>Automatic currency conversions</li></br>
									<li>Works on PC,Mac,and mobile devices</li>
								  </ul>
							</div>
						</div>
						<div class ="third-lists">
							<div class="price">
								<h4>Pricing Plans</h4>
							</div>
							<div class="price-list">
								  <ul>
									<li>Unlimited support 24/7</li></br>
									<li>Unlimited users</li></br>
									<li>Automatic back-ups</li></br>
									<li>Free updates and new features</li>									
								  </ul>
							</div>
						</div>
				
			</div>	
			</div><div class="box-shadow"></div>
		</div>
		
		<div class="down-test" id="down"></div>	
		<%@ include file="./footer.jsp" %>
		</div>
		<script  type="text/javascript" >
			if(${successmessage!=null}) {
				document.getElementById("right-side-options").style.marginTop = "15px";
				document.getElementById("vertical-line").style.marginTop = "30px";
			}
		</script>
		
<script type="text/javascript">

var _gaq = _gaq || [];
_gaq.push(['_setAccount', 'UA-24502570-1']);
_gaq.push(['_trackPageview']);

(function() {
var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
})();

</script>

</body>
</html>