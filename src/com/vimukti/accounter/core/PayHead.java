package com.vimukti.accounter.core;

import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Session;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;

/**
 * The salary components constituting Pay Structures are called Pay Heads. A Pay
 * Head may be an earning, which is paid to an employee, or a deduction, which
 * is recovered from his/her salary. The value of these Pay Heads could be
 * either fixed or variable, for each Payroll period.
 * 
 * @author Prasanna Kumar G
 * 
 */
public abstract class PayHead extends CreatableObject implements
		IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int TYPE_EARNINGS_FOR_EMPLOYEES = 1;
	public static final int TYPE_DEDUCTIONS_FOR_EMPLOYEES = 2;
	public static final int TYPE_EMPLOYEES_STATUTORY_DEDUCTIONS = 3;
	public static final int TYPE_EMPLOYEES_STATUTORY_CONTRIBUTIONS = 4;
	public static final int TYPE_EMPLOYEES_OTHER_CHARGES = 5;
	public static final int TYPE_BONUS = 6;
	public static final int TYPE_LOANS_AND_ADVANCES = 7;
	public static final int TYPE_REIMBURSEMENTS_TO_EMPLOYEES = 8;

	public static final int CALCULATION_TYPE_ON_ATTENDANCE = 1;
	public static final int CALCULATION_TYPE_AS_COMPUTED_VALUE = 2;
	public static final int CALCULATION_TYPE_FLAT_RATE = 3;
	public static final int CALCULATION_TYPE_ON_PRODUCTION = 4;
	public static final int CALCULATION_TYPE_AS_USER_DEFINED = 5;

	public static final int CALCULATION_PERIOD_DAYS = 1;
	public static final int CALCULATION_PERIOD_FOR_NIGHTS = 2;
	public static final int CALCULATION_PERIOD_WEEKS = 3;
	public static final int CALCULATION_PERIOD_MONTHS = 4;

	public static final int ROUNDING_METHOD_DOWNWORD = 1;
	public static final int ROUNDING_METHOD_NORMAL = 2;
	public static final int ROUNDING_METHOD_UPWORD = 3;

	private String name;

	private int type;

	private String nameToAppearInPaySlip;

	private int calculationType;

	private int roundingMethod;

	private boolean isAffectNetSalary;

	private List<PayHeadField> companyFields;

	private List<PayHeadField> employeeFields;

	/**
	 * Expense Account of this PayHead
	 */
	private Account account;

	public PayHead() {
		// TODO Auto-generated constructor stub
	}
	
	public PayHead(int calculationType) {
		this.calculationType = calculationType;
	}

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

	/**
	 * @return the nameToAppearInPaySlip
	 */
	public String getNameToAppearInPaySlip() {
		return nameToAppearInPaySlip;
	}

	/**
	 * @param nameToAppearInPaySlip
	 *            the nameToAppearInPaySlip to set
	 */
	public void setNameToAppearInPaySlip(String nameToAppearInPaySlip) {
		this.nameToAppearInPaySlip = nameToAppearInPaySlip;
	}

	/**
	 * @return the calculationType
	 */
	public int getCalculationType() {
		return calculationType;
	}

	/**
	 * @param calculationType
	 *            the calculationType to set
	 */
	public void setCalculationType(int calculationType) {
		this.calculationType = calculationType;
	}

	/**
	 * @return the roundingMethod
	 */
	public int getRoundingMethod() {
		return roundingMethod;
	}

	/**
	 * @param roundingMethod
	 *            the roundingMethod to set
	 */
	public void setRoundingMethod(int roundingMethod) {
		this.roundingMethod = roundingMethod;
	}

	/**
	 * @return the isAffectNetSalary
	 */
	public boolean isAffectNetSalary() {
		return isAffectNetSalary;
	}

	/**
	 * @param isAffectNetSalary
	 *            the isAffectNetSalary to set
	 */
	public void setAffectNetSalary(boolean isAffectNetSalary) {
		this.isAffectNetSalary = isAffectNetSalary;
	}

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
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @param account
	 *            the account to set
	 */
	public void setAccount(Account account) {
		this.account = account;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(getID());
		accounterCore.setObjectType(AccounterCoreType.PAY_HEAD);
		ChangeTracker.put(accounterCore);
		return super.onDelete(arg0);
	}
}
