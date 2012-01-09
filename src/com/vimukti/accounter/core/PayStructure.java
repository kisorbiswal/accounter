package com.vimukti.accounter.core;

import java.util.List;


/**
 * Salary Details is used to define the Pay Structure for an Employee or an
 * Employee Group. To speed up the entry of individual Employee's Pay Structure
 * you can define the Pay Structure for the Employee Group with those Pay Heads
 * which applies for most of the employees and copy the same to individual
 * employees.
 * 
 * If required, a Pay Head component or its value may be added, deleted or
 * altered at Individual Employee level. To define Salary Details for Employees
 * refer Creating Salary Details for an Employee.
 * 
 * @author Prasanna Kumar G
 * 
 */
public class PayStructure extends CreatableObject {

	private Employee employee;

	private EmployeeGroup employeeGroup;

	private FinanceDate effectiveFrom;

	private List<PayStructureItem> items;

	/**
	 * @return the effectiveFrom
	 */
	public FinanceDate getEffectiveFrom() {
		return effectiveFrom;
	}

	/**
	 * @param effectiveFrom
	 *            the effectiveFrom to set
	 */
	public void setEffectiveFrom(FinanceDate effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	/**
	 * @return the items
	 */
	public List<PayStructureItem> getItems() {
		return items;
	}

	/**
	 * @param items
	 *            the items to set
	 */
	public void setItems(List<PayStructureItem> items) {
		this.items = items;
	}

	/**
	 * @return the employeeGroup
	 */
	public EmployeeGroup getEmployeeGroup() {
		return employeeGroup;
	}

	/**
	 * @param employeeGroup
	 *            the employeeGroup to set
	 */
	public void setEmployeeGroup(EmployeeGroup employeeGroup) {
		this.employeeGroup = employeeGroup;
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

}
