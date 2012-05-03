package com.vimukti.accounter.web.client.core;

public class ClientPayStructureItem implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * PayHead of this PayStructure Item
	 */
	private long payHead;

	/**
	 * Rate
	 */
	private double rate;

	private long payStructure;

	private long id;

	private long effectiveFrom;

	private ClientPayHead clientPayHead;

	/**
	 * @return the payHead
	 */
	public long getPayHead() {
		return payHead;
	}

	/**
	 * @param payHead
	 *            the payHead to set
	 */
	public void setPayHead(long payHead) {
		this.payHead = payHead;
	}

	/**
	 * @return the rate
	 */
	public double getRate() {
		return rate;
	}

	/**
	 * @param rate
	 *            the rate to set
	 */
	public void setRate(double rate) {
		this.rate = rate;
	}

	/**
	 * @return the payStructure
	 */
	public long getPayStructure() {
		return payStructure;
	}

	/**
	 * @param payStructure
	 *            the payStructure to set
	 */
	public void setPayStructure(long payStructure) {
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
		this.id = id;
	}

	@Override
	public long getID() {
		return this.id;
	}

	public boolean isEmpty() {
		if (this.getPayHead() == 0 && this.getRate() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * @return the effectiveFrom
	 */
	public long getEffectiveFrom() {
		return effectiveFrom;
	}

	/**
	 * @param effectiveFrom
	 *            the effectiveFrom to set
	 */
	public void setEffectiveFrom(long effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	public ClientPayHead getClientPayHead() {
		return clientPayHead;
	}

	public void setClientPayHead(ClientPayHead newValue) {
		this.clientPayHead = newValue;
	}
}
