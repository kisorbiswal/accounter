package com.vimukti.accounter.web.client.core;

import java.sql.Timestamp;

public class ClientActivity implements IAccounterCore {

	private long id;

	private ClientUser clientUser;

	// private ActivityType type;

	private Timestamp time;

	private int objectType;

	private long objectID;

	private String name;

	private ClientFinanceDate date;

	private Double amount;

	private String activity;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	// public ActivityType getType() {
	// return type;
	// }
	//
	// public void setType(ActivityType type) {
	// this.type = type;
	// }

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
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

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public ClientUser getClientUser() {
		return clientUser;
	}

	public void setClientUser(ClientUser clientUser) {
		this.clientUser = clientUser;
	}

	public ClientFinanceDate getDate() {
		return date;
	}

	public void setDate(ClientFinanceDate date) {
		this.date = date;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	@Override
	public void setVersion(int version) {

	}

	@Override
	public int getVersion() {
		return 0;
	}

	@Override
	public String getDisplayName() {
		return "ClientActivity";
	}

	@Override
	public void setID(long id) {

	}

	@Override
	public long getID() {
		return 0;
	}

	@Override
	public String getClientClassSimpleName() {
		return "ClientAvtivity";
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.ACTIVITY;
	}

}
