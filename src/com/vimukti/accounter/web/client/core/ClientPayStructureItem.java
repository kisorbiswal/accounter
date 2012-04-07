package com.vimukti.accounter.web.client.core;

public class ClientPayStructureItem implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * PayHead of this PayStructure Item
	 */
	private ClientPayHead payHead;

	/**
	 * Rate
	 */
	private double rate;

	private ClientPayStructure payStructure;

	public ClientPayHead getPayHead() {
		return payHead;
	}

	public void setPayHead(ClientPayHead payHead) {
		this.payHead = payHead;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public ClientPayStructure getPayStructure() {
		return payStructure;
	}

	public void setPayStructure(ClientPayStructure payStructure) {
		this.payStructure = payStructure;
	}

	@Override
	public int getVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setVersion(int version) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setID(long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public long getID() {
		// TODO Auto-generated method stub
		return 0;
	}

}
