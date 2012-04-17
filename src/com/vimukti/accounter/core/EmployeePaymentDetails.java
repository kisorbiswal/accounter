package com.vimukti.accounter.core;

import java.util.List;
import java.util.Set;

import org.json.JSONException;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class EmployeePaymentDetails extends CreatableObject implements
		IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

	private double getDeductions() {
		double deduction = 0.00D;
		for (EmployeePayHeadComponent component : payHeadComponents) {
			if (component.isDeduction()) {
				deduction = component.calculatePayment();
			}

		}
		return deduction;
	}

	private double getEarnings() {
		double earnings = 0.00D;
		for (EmployeePayHeadComponent component : payHeadComponents) {
			if (component.isEarning()) {
				earnings = component.calculatePayment();
			}

		}
		return earnings;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

}
