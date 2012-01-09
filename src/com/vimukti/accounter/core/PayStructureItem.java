package com.vimukti.accounter.core;

public class PayStructureItem extends CreatableObject {

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

}
