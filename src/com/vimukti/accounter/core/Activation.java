package com.vimukti.accounter.core;

import java.util.Date;

public class Activation {
	private String emailId;
	private String token;
	private Date signUpDate;

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getSignUpDate() {
		return signUpDate;
	}

	public void setSignUpDate(Date sighnUpDate) {
		this.signUpDate = sighnUpDate;
	}

}
