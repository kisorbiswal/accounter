package com.vimukti.accounter.core;

import org.json.JSONException;

import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * @author Prasanna Kumar G
 * 
 */
public class EmployeePayHeadComponent extends CreatableObject implements
		IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmployeePayHeadComponent() {
	}

	private PayHead payHead;

	private double rate;

	private FinanceDate periodEndDate;

	private FinanceDate periodStartDate;

	private EmployeePaymentDetails employeePaymentDetails;

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

	public boolean isEarning() {
		return payHead.isEarning();
	}

	public boolean isDeduction() {
		return payHead.isDeduction();
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

	public FinanceDate getStartDate() {
		return periodStartDate;
	}

	public FinanceDate getEndDate() {
		return periodEndDate;
	}

	public void setStartEndDates(FinanceDate payPeriodStartDate,
			FinanceDate payPeriodEndDate) {
		this.periodStartDate = payPeriodStartDate;
		this.periodEndDate = payPeriodEndDate;
	}

	public double getBasicSalary() {
		return employeePaymentDetails.getBasicSalary();
	}

	public EmployeePaymentDetails getEmployeePaymentDetails() {
		return employeePaymentDetails;
	}

	public void setEmployeePaymentDetails(
			EmployeePaymentDetails employeePaymentDetails) {
		this.employeePaymentDetails = employeePaymentDetails;
	}
}
