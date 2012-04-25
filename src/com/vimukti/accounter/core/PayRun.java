package com.vimukti.accounter.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class PayRun extends Transaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Set<EmployeePaymentDetails> payEmployee = new HashSet<EmployeePaymentDetails>();

	private FinanceDate payPeriodStartDate;

	private FinanceDate payPeriodEndDate;

	private double deductionAmount = 0.0;

	private double earningsAmount = 0.0;

	private List<AttendanceManagementItem> attendanceItems = new ArrayList<AttendanceManagementItem>();

	public PayRun() {
		super();
		setType(Transaction.TYPE_PAY_RUN);
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
		setType(Transaction.TYPE_PAY_RUN);
		for (EmployeePaymentDetails detail : payEmployee) {
			detail.setPayRun(this);
			detail.runPayment();
		}

		double totalAmount = earningsAmount - deductionAmount;
		setTotal(totalAmount);
		for (EmployeePaymentDetails detail : payEmployee) {
			for (EmployeePayHeadComponent component : detail
					.getPayHeadComponents()) {
				double rate = component.getRate();
				Account account = component.getPayHead().getAccount();
				if (component.isDeduction()) {
					account.updateCurrentBalance(this, rate, 1);
				} else if (component.isEarning()) {
					account.updateCurrentBalance(this, -rate, 1);
				}
			}
		}

		Account salariesPayableAccount = getCompany()
				.getSalariesPayableAccount();
		salariesPayableAccount.updateCurrentBalance(this, totalAmount, 1);
		return super.onSave(session);
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {

		if (this.getSaveStatus() == STATUS_VOID) {
			throw new AccounterException(AccounterException.ERROR_CANT_EDIT);
		}

		return true;
	}

	@Override
	public void onEdit(Transaction clonedObject) {
		super.onEdit(clonedObject);
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {

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

	@Override
	public boolean isPositiveTransaction() {
		return true;
	}

	@Override
	public boolean isDebitTransaction() {
		return false;
	}

	@Override
	public Account getEffectingAccount() {
		return null;
	}

	@Override
	public Payee getPayee() {
		return null;
	}

	@Override
	public int getTransactionCategory() {
		return Transaction.CATEGORY_EMPLOYEE;
	}

	@Override
	public String toString() {
		return AccounterServerConstants.TYPE_PAY_RUN;
	}

	@Override
	public Payee getInvolvedPayee() {
		return null;
	}

	@Override
	protected void updatePayee(boolean onCreate) {
	}

	public List<AttendanceManagementItem> getAttendanceItems() {
		return attendanceItems;
	}

	public void setAttendanceItems(
			List<AttendanceManagementItem> attendanceItems) {
		this.attendanceItems = attendanceItems;
	}
}
