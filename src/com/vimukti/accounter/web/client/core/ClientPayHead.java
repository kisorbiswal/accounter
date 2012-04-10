package com.vimukti.accounter.web.client.core;

import java.util.List;

public class ClientPayHead implements IAccounterCore {

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

	private String alias;

	private int type;

	private String nameToAppearInPaySlip;

	private int calculationType;

	private int roundingMethod;

	private boolean isAffectNetSalary;

	private List<ClientPayHeadField> companyFields;

	private List<ClientPayHeadField> employeeFields;

	private long id;

	private ClientAccount expenseAccount;

	/**
	 * @return the companyFields
	 */
	public List<ClientPayHeadField> getCompanyFields() {
		return companyFields;
	}

	/**
	 * @param companyFields
	 *            the companyFields to set
	 */
	public void setCompanyFields(List<ClientPayHeadField> companyFields) {
		this.companyFields = companyFields;
	}

	/**
	 * @return the employeeFields
	 */
	public List<ClientPayHeadField> getEmployeeFields() {
		return employeeFields;
	}

	/**
	 * @param employeeFields
	 *            the employeeFields to set
	 */
	public void setEmployeeFields(List<ClientPayHeadField> employeeFields) {
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
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias
	 *            the alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Override
	public int getVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setVersion(int version) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getDisplayName() {
		return this.name;
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return this.id;
	}

	public ClientAccount getExpenseAccount() {
		return expenseAccount;
	}

	public void setExpenseAccount(ClientAccount expenseAccount) {
		this.expenseAccount = expenseAccount;
	}

}
