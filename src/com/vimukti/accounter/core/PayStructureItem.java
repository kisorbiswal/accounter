package com.vimukti.accounter.core;

import org.json.JSONException;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class PayStructureItem extends CreatableObject implements
		IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * PayHead of this PayStructure Item
	 */
	private PayHead payHead;

	/**
	 * Rate
	 */
	private double rate;

	private PayStructure payStructure;

	/**
	 * @return the payHead
	 */
	public PayHead getPayHead() {
		return payHead;
	}

	/**
	 * @param payHead
	 *            the payHead to set
	 */
	public void setPayHead(PayHead payHead) {
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
	public PayStructure getPayStructure() {
		return payStructure;
	}

	/**
	 * @param payStructure
	 *            the payStructure to set
	 */
	public void setPayStructure(PayStructure payStructure) {
		this.payStructure = payStructure;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

}
