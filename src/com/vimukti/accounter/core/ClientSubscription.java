package com.vimukti.accounter.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.Global;

public class ClientSubscription implements IsSerializable {
	public static final int ONE_USER = 1;
	public static final int TWO_USERS = 2;
	public static final int FIVE_USERS = 3;
	public static final int UNLIMITED_USERS = 4;

	private long id;
	private Subscription subscription;
	private Date createdDate;
	private Date lastModified;
	private Date expiredDate;
	private Set<String> members = new HashSet<String>();
	private int premiumType;

	public ClientSubscription() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Subscription getSubscription() {
		return subscription;
	}

	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public Set<String> getMembers() {
		return members;
	}

	public void setMembers(Set<String> members) {
		this.members = members;
	}

	public Date getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}

	public int getPremiumType() {
		return premiumType;
	}

	public void setPremiumType(int premiumType) {
		this.premiumType = premiumType;
	}

	public String getTypeToString() {
		switch (premiumType) {
		case 0:
			return "One User Monthly Subscription";
		case 1:
			return "One User Yearly Subscription";
		case 2:
			return "Two Users Monthly Subscription";
		case 3:
			return "Two Users Yearly Subscription";
		case 4:
			return "Five Users Monthly Subscription";
		case 5:
			return "Five Users Yearly Subscription";
		case 6:
			return "Unlimited Users Monthly Subscription";
		case 7:
			return "Unlimited Users Yearly Subscription";
		default:
			break;
		}
		return "";
	}

	public String getExpiredDateAsString() {
		if (expiredDate == null) {
			return "Unlimited";
		}
		if (Global.get().preferences() != null) {
			String dateInSelectedFormat = Utility
					.getDateInSelectedFormat(new FinanceDate(expiredDate));
			return dateInSelectedFormat;
		}
		SimpleDateFormat dateformatMMDDYYYY = new SimpleDateFormat(
				"MMM/dd/yyyy");
		return dateformatMMDDYYYY.format(expiredDate);

	}
}