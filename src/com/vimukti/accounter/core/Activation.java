package com.vimukti.accounter.core;

import java.util.Date;

public class Activation {
	private String emailId;
	private String tocken;
	private Date signUpDate;

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getTocken() {
		return tocken;
	}

	public void setTocken(String tocken) {
		this.tocken = tocken;
	}

	public Date getSignUpDate() {
		return signUpDate;
	}

	public void setSignUpDate(Date sighnUpDate) {
		this.signUpDate = sighnUpDate;
	}

}
