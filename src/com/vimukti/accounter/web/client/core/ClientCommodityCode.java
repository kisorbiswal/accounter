package com.vimukti.accounter.web.client.core;


public class ClientCommodityCode implements IAccounterCore{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String stringID;
	String name;
	String company;
	
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getStringID() {
			return this.stringID;
	}
	@Override
	public void setStringID(String stringID) {
		this.stringID = stringID;
		
	}
		@Override
	public String getClientClassSimpleName() {
		
		return "ClientCommodityCode";
	}
	
	
}
