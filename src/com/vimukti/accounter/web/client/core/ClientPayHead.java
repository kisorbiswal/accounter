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
	public static final int TYPE_GRATUITY = 7;
	public static final int TYPE_LOANS_AND_ADVANCES = 8;
	public static final int TYPE_REIMBURSEMENTS_TO_EMPLOYEES = 9;

	public static final int CALCULATION_TYPE_ON_ATTENDANCE = 1;
	public static final int CALCULATION_TYPE_AS_COMPUTED_VALUE = 2;
	public static final int CALCULATION_TYPE_FLAT_RATE = 3;
	public static final int CALCULATION_TYPE_ON_PRODUCTION = 4;
	public static final int CALCULATION_TYPE_AS_USER_DEFINED = 5;

	public static final int CALCULATION_TYPE_DAYS = 1;
	public static final int CALCULATION_TYPE_WEEKS = 2;
	public static final int CALCULATION_TYPE_MONTHS = 3;

	public static final int ROUNDING_METHOD_DOWNWORD = 1;
	public static final int ROUNDING_METHOD_NORMAL = 2;
	public static final int ROUNDING_METHOD_UPWORD = 3;

	private String name;

	private String alias;

	private int type;

	private String nameToAppearInPaySlip;

	private int calculationType;

	private int calculationPeriod;

	private int roundingMethod;

	private boolean isAffectNetSalary;

	private List<ClientPayHeadField> companyFields;

	private List<ClientPayHeadField> employeeFields;

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getNameToAppearInPaySlip() {
		return nameToAppearInPaySlip;
	}

	public void setNameToAppearInPaySlip(String nameToAppearInPaySlip) {
		this.nameToAppearInPaySlip = nameToAppearInPaySlip;
	}

	public int getCalculationType() {
		return calculationType;
	}

	public void setCalculationType(int calculationType) {
		this.calculationType = calculationType;
	}

	public int getCalculationPeriod() {
		return calculationPeriod;
	}

	public void setCalculationPeriod(int calculationPeriod) {
		this.calculationPeriod = calculationPeriod;
	}

	public int getRoundingMethod() {
		return roundingMethod;
	}

	public void setRoundingMethod(int roundingMethod) {
		this.roundingMethod = roundingMethod;
	}

	public boolean isAffectNetSalary() {
		return isAffectNetSalary;
	}

	public void setAffectNetSalary(boolean isAffectNetSalary) {
		this.isAffectNetSalary = isAffectNetSalary;
	}

	public List<ClientPayHeadField> getCompanyFields() {
		return companyFields;
	}

	public void setCompanyFields(List<ClientPayHeadField> companyFields) {
		this.companyFields = companyFields;
	}

	public List<ClientPayHeadField> getEmployeeFields() {
		return employeeFields;
	}

	public void setEmployeeFields(List<ClientPayHeadField> employeeFields) {
		this.employeeFields = employeeFields;
	}

	public void setName(String name) {
		this.name = name;
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
	public String getName() {
		return this.name;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setID(long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public long getID() {
		// TODO Auto-generated method stub
		return 0;
	}

}
