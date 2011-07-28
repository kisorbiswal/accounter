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

	public String getTocken() {
		return token;
	}

	public void setTocken(String tocken) {
		this.token = tocken;
	}

	public Date getSignUpDate() {
		return signUpDate;
	}

	public void setSignUpDate(Date sighnUpDate) {
		this.signUpDate = sighnUpDate;
	}

}
