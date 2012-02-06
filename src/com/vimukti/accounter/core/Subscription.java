package com.vimukti.accounter.core;

import java.util.Date;

public class Subscription {

	public static final int ONE_USER_MONTHLY_SUBSCRIPTION = 0;
	public static final int ONE_USER_YEARLY_SUBSCRIPTION = 1;
	public static final int TWO_USERS_MONTHLY_SUBSCRIPTION = 2;
	public static final int TWO_USERS_YEARLY_SUBSCRIPTION = 3;
	public static final int FIVE_USERS_MONTHLY_SUBSCRIPTION = 4;
	public static final int FIVE_USERS_YEARLY_SUBSCRIPTION = 5;
	public static final int UNLIMITED_USERS_MONTHLY_SUBSCRIPTION = 6;
	public static final int UNLIMITED_USERS_YEARLY_SUBSCRIPTION = 7;

	private Date date;
	private int type;
	private long id;
	private Client client;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public static String getTypeToString(int type) {
		switch (type) {
		case 0:
			return "One User Monthly Subscription";
		case 1:
			return "One User Monthly Subscription";
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

	public static int getStringToType(String type) {
		if (type.equals("One User Monthly Subscription")) {
			return 0;
		} else if (type.equals("One User Monthly Subscription")) {
			return 1;
		} else if (type.equals("Two Users Monthly Subscription")) {
			return 2;
		} else if (type.equals("Two Users Yearly Subscription")) {
			return 3;
		} else if (type.equals("Five Users Monthly Subscription")) {
			return 4;
		} else if (type.equals("Five Users Yearly Subscription")) {
			return 5;
		} else if (type.equals("Unlimited Users Monthly Subscription")) {
			return 6;
		} else if (type.equals("Unlimited Users Yearly Subscription")) {
			return 7;
		}
		return 1;
	}
}
