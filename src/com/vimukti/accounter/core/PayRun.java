package com.vimukti.accounter.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.web.client.Global;
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

	private Employee employee;

	private EmployeeGroup employeeGroup;

	private String noOfWorkingDays;

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
		doPayRunCreateEffect();
		return super.onSave(session);
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		PayRun payRun = (PayRun) clientObject;
		if (payRun.getSaveStatus() == STATUS_VOID) {
			throw new AccounterException(AccounterException.ERROR_CANT_EDIT);
		}

		if (payRun.getSaveStatus() == STATUS_DRAFT
				|| payRun.getSaveStatus() == STATUS_TEMPLATE) {
			throw new AccounterException(
					AccounterException.ERROR_CANT_CREATE_PAYRUN_DRAFT_OR_TEMPLATE);
		}
		if (!goingToBeEdit) {
			if (payRun.getPayEmployee() == null
					|| payRun.getPayEmployee().isEmpty()) {
				throw new AccounterException(
						AccounterException.ERROR_TRANSACTION_ITEM_NULL, Global
								.get().messages().transactionItem());
			}

			for (EmployeePaymentDetails detail : payRun.getPayEmployee()) {
				detail.canEdit(detail, goingToBeEdit);
			}
		}
		return true;
	}

	@Override
	public void onEdit(Transaction clonedObject) throws AccounterException {
		PayRun payRun = (PayRun) clonedObject;

		doPayRunDeleteEffect(payRun);

		if (!this.isVoid() && !payRun.isVoid()) {
			doPayRunCreateEffect();
		}

		super.onEdit(clonedObject);
	}

	private void doPayRunCreateEffect() {
		for (EmployeePaymentDetails detail : this.getPayEmployee()) {
			detail.setPayRun(this);
			detail.runPayment();
		}

		double totalAmount = earningsAmount - deductionAmount;
		setTotal(totalAmount);
		for (EmployeePaymentDetails detail : this.getPayEmployee()) {
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

	public List<AttendanceManagementItem> getAttendanceItems() {
		return attendanceItems;
	}

	public void setAttendanceItems(
			List<AttendanceManagementItem> attendanceItems) {
		this.attendanceItems = attendanceItems;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public EmployeeGroup getEmployeeGroup() {
		return employeeGroup;
	}

	public void setEmployeeGroup(EmployeeGroup employeeGroup) {
		this.employeeGroup = employeeGroup;
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		doPayRunDeleteEffect(this);
		this.payEmployee.clear();
		return super.onDelete(session);
	}

	private void doPayRunDeleteEffect(PayRun payRun) {
		for (EmployeePaymentDetails detail : payRun.getPayEmployee()) {
			for (EmployeePayHeadComponent component : detail
					.getPayHeadComponents()) {
				double rate = component.getRate();
				Account account = component.getPayHead().getAccount();
				if (component.isDeduction()) {
					account.updateCurrentBalance(payRun, -rate, 1);
				} else if (component.isEarning()) {
					account.updateCurrentBalance(payRun, rate, 1);
				}
			}
		}

		Account salariesPayableAccount = getCompany()
				.getSalariesPayableAccount();
		salariesPayableAccount.updateCurrentBalance(payRun, -payRun.getTotal(),
				1);
	}

	@Override
	public void getEffects(ITransactionEffects e) {
		// TODO Auto-generated method stub

	}

	public void setNoOfWorkingDays(String noOfWorkingDays) {
		this.noOfWorkingDays = noOfWorkingDays;
	}

	public String getNoOfWorkingDays() {
		return noOfWorkingDays;
	}
}