package com.vimukti.accounter.core;

import java.util.List;


/**
 * An Employee Group allows you to group/classify the employees in a logical
 * manner as required.
 * 
 * The Salary structure can be defined at the Employee Group level. For example,
 * you can create the Salary structure based on the department or function such
 * as Production, Sales, Administration and so on, or by designation such as
 * Managers, Supervisors, Workers and so on.
 * 
 * @author Prasanna Kumar G
 * 
 */
public class EmployeeGroup extends CreatableObject {

	private String name;

	private List<Employee> employees;

	private PayStructure payStructure;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the employees
	 */
	public List<Employee> getEmployees() {
		return employees;
	}

	/**
	 * @param employees
	 *            the employees to set
	 */
	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	/**
	 * @return the payStructure
	 */
	public PayStructure getPayStructure() {
		return payStructure;
	}

	/**
	 * @param payStructure
	 *            the payStructure to set
	 */
	public void setPayStructure(PayStructure payStructure) {
		this.payStructure = payStructure;
	}
}
