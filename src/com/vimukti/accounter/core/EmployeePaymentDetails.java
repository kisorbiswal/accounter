package com.vimukti.accounter.core;

import java.util.ArrayList;
import java.util.List;

public class EmployeePaymentDetails extends CreatableObject {

	/**
	 * Pay Run
	 */
	private PayRun payRun;

	private Employee employee;

	private List<EmployeePayHeadComponent> payHeadComponents = new ArrayList<EmployeePayHeadComponent>();

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

		List<ComputationPayHead> payHeads = haveComputationPayHeads();
		for (ComputationPayHead payHead : payHeads) {
			payHead.compute(this);
		}
	}

	private List<ComputationPayHead> haveComputationPayHeads() {
		// TODO Auto-generated method stub
		return null;
	}

	double getDeductions() {
		double deduction = 0.00D;
		for (EmployeePayHeadComponent component : payHeadComponents) {
			if (component.isDeduction()) {
				deduction = component.calculatePayment();
			}

		}
		return deduction;
	}

	double getEarnings() {
		double earnings = 0.00D;
		for (EmployeePayHeadComponent component : payHeadComponents) {
			if (component.isEarning()) {
				earnings = component.calculatePayment();
			}

		}
		return earnings;
	}

	public double getSubTotal() {
		// TODO Auto-generated method stub
		return 0;
	}

}
