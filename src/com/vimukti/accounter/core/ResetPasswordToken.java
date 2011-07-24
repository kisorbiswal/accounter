package com.vimukti.accounter.core;

public class ResetPasswordToken {

	long id;
	String token;
	String userId;
	
	public ResetPasswordToken() {
		
	}
	
	public ResetPasswordToken(String token, String userId) {
		this.token = token;
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public long getID() {
		return id;
	}

	public void setID(long id){
		this.id = id;
	}

}
