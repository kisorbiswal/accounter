package com.vimukti.accounter.core;

/**
 * @author Prasanna Kumar G
 * 
 */
public class EmployeePayHeadComponent extends CreatableObject {

	private PayHead payHead;

	private double rate;

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

}
