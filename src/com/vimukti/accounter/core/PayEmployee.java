package com.vimukti.accounter.core;

import java.util.List;

public class PayEmployee {

	/**
	 * Pay Run
	 */
	private PayRun payRun;

	private List<EmployeePayHeadComponent> payHeadComponents;

	/**
	 * @return the payHeadComponents
	 */
	public List<EmployeePayHeadComponent> getPayHeadComponents() {
		return payHeadComponents;
	}

	/**
	 * @param payHeadComponents
	 *            the payHeadComponents to set
	 */
	public void setPayHeadComponents(
			List<EmployeePayHeadComponent> payHeadComponents) {
		this.payHeadComponents = payHeadComponents;
	}

	/**
	 * @return the payRun
	 */
	public PayRun getPayRun() {
		return payRun;
	}

	/**
	 * @param payRun
	 *            the payRun to set
	 */
	public void setPayRun(PayRun payRun) {
		this.payRun = payRun;
	}
}
