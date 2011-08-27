package com.vimukti.accounter.core;

import java.sql.Timestamp;

public class Activity {

	public Activity(User user, ActivityType type) {
		this.user = user;
		this.type = type;
		this.time = new Timestamp(System.currentTimeMillis());
	}

	public Activity(User user, ActivityType type, IAccounterServerCore obj) {
		this(user, type);
		setObject(obj);
	}

	private void setObject(IAccounterServerCore obj) {
		if (obj instanceof Transaction) {
			Transaction tr = (Transaction) obj;
			this.amount = tr.getTotal();
			this.date = tr.getDate();
			Payee payee = tr.getPayee();
			if (payee != null) {
				this.name = payee.getName();
			}
			this.objectType = tr.getType();
		} else {
			if (obj instanceof INamedObject) {
				this.name = ((INamedObject) obj).getName();
			}
		}
		this.objectID = obj.getID();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ActivityType getType() {
		return type;
	}

	public void setType(ActivityType type) {
		this.type = type;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public int getObjectType() {
		return objectType;
	}

	public void setObjectType(int objectType) {
		this.objectType = objectType;
	}

	public long getObjectID() {
		return objectID;
	}

	public void setObjectID(long objectID) {
		this.objectID = objectID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FinanceDate getDate() {
		return date;
	}

	public void setDate(FinanceDate date) {
		this.date = date;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public long getId() {
		return id;
	}

	private long id;

	private User user;

	private ActivityType type;

	private Timestamp time;

	private int objectType;

	private long objectID;

	private String name;

	private FinanceDate date;

	private Double amount;

}
