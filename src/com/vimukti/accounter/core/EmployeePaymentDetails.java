package com.vimukti.accounter.core;

import java.util.Set;

import org.json.JSONException;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class EmployeePaymentDetails extends CreatableObject implements
		IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmployeePaymentDetails() {
	}

	/**
	 * Pay Run
	 */
	private PayRun payRun;

	private Employee employee;

	private Set<EmployeePayHeadComponent> payHeadComponents;

	/**
	 * @return the payHeadComponents
	 */
	public Set<EmployeePayHeadComponent> getPayHeadComponents() {
		return payHeadComponents;
	}

	/**
	 * @param payHeadComponents
	 *            the payHeadComponents to set
	 */
	public void setPayHeadComponents(
			Set<EmployeePayHeadComponent> payHeadComponents) {
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

	/**
	 * @return the employee
	 */
	public Employee getEmployee() {
		return employee;
	}

	/**
	 * @param employee
	 *            the employee to set
	 */
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	/**
	 * Run the Payment of this Employee
	 */
	public void runPayment() {
		double earnings = getEarnings();
		double deductions = getDeductions();
	}

	double deduction = 0.00D;

	private double getDeductions() {
		for (EmployeePayHeadComponent component : payHeadComponents) {
			component.setStartEndDates(payRun.getPayPeriodStartDate(),
					payRun.getPayPeriodEndDate());
			component.setEmployeePaymentDetails(this);
			if (component.isDeduction()) {
				double calculatePayment = component
						.calculatePayment(payRun.getDeductionAmount(),
								payRun.getEarningsAmount());
				deduction += calculatePayment;
				payRun.addDeductions(calculatePayment);
			}

		}
		return deduction;
	}

	double earnings = 0.00D;

	private double getEarnings() {
		for (EmployeePayHeadComponent component : payHeadComponents) {
			if (component.isEarning()) {
				component.setEmployeePaymentDetails(this);
				component.setStartEndDates(payRun.getPayPeriodStartDate(),
						payRun.getPayPeriodEndDate());
				double calculatePayment = component
						.calculatePayment(payRun.getDeductionAmount(),
								payRun.getEarningsAmount());
				earnings += calculatePayment;
				payRun.addEarnings(calculatePayment);
			}

		}
		return earnings;
	}

	public double getErningAmount() {
		return earnings;
	}

	public double getDeductionAmount() {
		return deduction;
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

	public double getBasicSalary() {
		// TODO Auto-generated method stub
		return 0;
	}
}
