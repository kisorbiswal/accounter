package com.vimukti.accounter.core;

import java.util.List;


/**
 * The salary components constituting Pay Structures are called Pay Heads. A Pay
 * Head may be an earning, which is paid to an employee, or a deduction, which
 * is recovered from his/her salary. The value of these Pay Heads could be
 * either fixed or variable, for each Payroll period.
 * 
 * @author Prasanna Kumar G
 * 
 */
public class PayHead extends CreatableObject {

	public static final int TYPE_EARNINGS_FOR_EMPLOYEES = 1;
	public static final int TYPE_DEDUCTIONS_FOR_EMPLOYEES = 2;
	public static final int TYPE_EMPLOYEES_STATUTORY_DEDUCTIONS = 3;
	public static final int TYPE_EMPLOYEES_STATUTORY_CONTRIBUTIONS = 4;
	public static final int TYPE_EMPLOYEES_OTHER_CHARGES = 5;
	public static final int TYPE_BONUS = 6;
	public static final int TYPE_GRATUITY = 7;
	public static final int TYPE_LOANS_AND_ADVANCES = 8;
	public static final int TYPE_REIMBURSEMENTS_TO_EMPLOYEES = 9;

	private int type;

	private List<PayHeadField> companyFields;

	private List<PayHeadField> employeeFields;

	/**
	 * @return the companyFields
	 */
	public List<PayHeadField> getCompanyFields() {
		return companyFields;
	}

	/**
	 * @param companyFields
	 *            the companyFields to set
	 */
	public void setCompanyFields(List<PayHeadField> companyFields) {
		this.companyFields = companyFields;
	}

	/**
	 * @return the employeeFields
	 */
	public List<PayHeadField> getEmployeeFields() {
		return employeeFields;
	}

	/**
	 * @param employeeFields
	 *            the employeeFields to set
	 */
	public void setEmployeeFields(List<PayHeadField> employeeFields) {
		this.employeeFields = employeeFields;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

}
