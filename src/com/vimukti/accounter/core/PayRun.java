package com.vimukti.accounter.core;

import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class PayRun extends CreatableObject implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Account payableAccount;

	private Set<EmployeePaymentDetails> payEmployee;

	private FinanceDate payPeriodStartDate;

	private FinanceDate payPeriodEndDate;

	private double deductionAmount = 0.0;

	private double earningsAmount = 0.0;

	public PayRun() {
	}

	/**
	 * @return the payableAccount
	 */
	public Account getPayableAccount() {
		return payableAccount;
	}

	/**
	 * @param payableAccount
	 *            the payableAccount to set
	 */
	public void setPayableAccount(Account payableAccount) {
		this.payableAccount = payableAccount;
	}

	/**
	 * @return the payEmployee
	 */
	public Set<EmployeePaymentDetails> getPayEmployee() {
		return payEmployee;
	}

	/**
	 * @param payEmployee
	 *            the payEmployee to set
	 */
	public void setPayEmployee(Set<EmployeePaymentDetails> payEmployee) {
		this.payEmployee = payEmployee;
	}

	/**
	 * @return the payPeriodStartDate
	 */
	public FinanceDate getPayPeriodStartDate() {
		return payPeriodStartDate;
	}

	/**
	 * @param payPeriodStartDate
	 *            the payPeriodStartDate to set
	 */
	public void setPayPeriodStartDate(FinanceDate payPeriodStartDate) {
		this.payPeriodStartDate = payPeriodStartDate;
	}

	/**
	 * @return the payPeriodEndDate
	 */
	public FinanceDate getPayPeriodEndDate() {
		return payPeriodEndDate;
	}

	/**
	 * @param payPeriodEndDate
	 *            the payPeriodEndDate to set
	 */
	public void setPayPeriodEndDate(FinanceDate payPeriodEndDate) {
		this.payPeriodEndDate = payPeriodEndDate;
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		// Running Payment of Each Employee
		for (EmployeePaymentDetails detail : payEmployee) {
			detail.setPayRun(this);
			detail.runPayment();
		}
		return super.onSave(session);
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

	public void addDeductions(double deduction) {
		this.deductionAmount += deduction;
	}

	public double getDeductionAmount() {
		return deductionAmount;
	}

	public void addEarnings(double earnings) {
		this.earningsAmount += earnings;
	}

	public double getEarningsAmount() {
		return earningsAmount;
	}
}
