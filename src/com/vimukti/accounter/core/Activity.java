package com.vimukti.accounter.core;

import java.sql.Timestamp;

public class Activity extends CreatableObject {

	private User user;

	private int activityType;

	private int objType;

	private Timestamp time;

	private String dataType;

	private long objectID;

	private String name;

	private String userName;

	private FinanceDate transactionDate;

	private double amount;

	private String description;

	public Activity() {
	}

	public Activity(Company company, User user, ActivityType type) {
		this.user = user;
		this.userName = user.getClient().getFullName();
		this.time = new Timestamp(System.currentTimeMillis());
		setActivityType(type.getValue());
		setCompany(company);
	}

	public Activity(Company company, User user, ActivityType type,
			IAccounterServerCore obj) {
		this(company, user, type);
		setObject(obj);
	}

	private void setObject(IAccounterServerCore obj) {
		if (obj instanceof Transaction) {
			Transaction tr = (Transaction) obj;
			this.amount = tr.getTotal();
			this.transactionDate = tr.getDate();
			Payee payee = tr.getInvolvedPayee();
			if (payee != null) {
				this.name = payee.getName();
			}
			this.setDataType(Utility.getTransactionName(tr.getType()));
			this.setObjType(tr.getType());
		} else {
			if (obj instanceof INamedObject) {
				this.name = ((INamedObject) obj).getName();
				this.objType = ((INamedObject) obj).getObjType();
			}
			this.setDataType(obj.getClass().getSimpleName());
		}
		this.objectID = obj.getID();
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
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
		return transactionDate;
	}

	public void setDate(FinanceDate date) {
		this.transactionDate = date;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@Override
	public int getVersion() {
		return 0;
	}

	@Override
	public void setVersion(int version) {

	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getActivityType() {
		return activityType;
	}

	public void setActivityType(int activityType) {
		this.activityType = activityType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	/**
	 * @return the details
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param details
	 *            the details to set
	 */
	public void setDescription(String details) {
		this.description = details;
	}

	public int getObjType() {
		return objType;
	}

	public void setObjType(int objType) {
		this.objType = objType;
	}

}
