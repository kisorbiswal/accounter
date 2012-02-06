package com.vimukti.accounter.core;

public class SubscriptionManagementData {

	private String userMailds;
	private String adminMailId;
	private int subscriptionType;
	private String subscriptionDate;

	// public List<String> getUserMailds() {
	// return userMailds;
	// }
	//
	// public void setUserMailds(List<String> userMailds) {
	// this.userMailds = userMailds;
	// }

	public String getUserMailds() {
		return userMailds;
	}

	public void setUserMailds(String userMailds) {
		this.userMailds = userMailds;
	}

	public String getAdminMailId() {
		return adminMailId;
	}

	public void setAdminMailId(String adminMailId) {
		this.adminMailId = adminMailId;
	}

	public String getSubscriptionDate() {
		return subscriptionDate;
	}

	public void setSubscriptionDate(String subscriptionDate) {
		this.subscriptionDate = subscriptionDate;
	}

	public int getSubscriptionType() {
		return subscriptionType;
	}

	public void setSubscriptionType(int subscriptionType) {
		this.subscriptionType = subscriptionType;
	}
}
