package com.vimukti.accounter.web.client.core;


public class ClientAttendancePayhead extends ClientPayHead {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * As per Calendar Period refers to the perpetual calendar month, i.e. if
	 * the payroll process is carried out for the month of March, then the
	 * attendance records will be entered for 31 days.
	 */
	public static final int PER_DAY_CALCULATION_AS_PER_CALANDAR_PERIOD = 1;

	/**
	 * User Defined refers to the consistent payroll period irrespective of
	 * calendar month, i.e. If an employee�s salary is based on a standard
	 * month of 30 days irrespective of the calendar month, then you can select
	 * User Defined as the Calculation Basis and define the periodicity of the
	 * specified period or month.
	 */
	public static final int PER_DAY_CALCULATION_USER_DEFINED = 2;

	/**
	 * User Defined Calendar Type option will provide the flexibility to the
	 * user to select user defined calendar days every Calculation Period. Once
	 * this option is selected the user can have different number of working
	 * days for every Calculation Period.
	 */
	public static final int PER_DAY_CALCULATION_USER_DEFINED_CALANDAR = 3;

	private int calculationPeriod;

	private int perDayCalculationBasis;

	private ClientAttendanceOrProductionType leaveWithPay;

	/**
	 * Will be used if leaveWithPay does not Exist
	 */
	private ClientAttendanceOrProductionType leaveWithoutPay;

	public int getCalculationPeriod() {
		return calculationPeriod;
	}

	public void setCalculationPeriod(int calculationPeriod) {
		this.calculationPeriod = calculationPeriod;
	}

	public int getPerDayCalculationBasis() {
		return perDayCalculationBasis;
	}

	public void setPerDayCalculationBasis(int perDayCalculationBasis) {
		this.perDayCalculationBasis = perDayCalculationBasis;
	}

	public ClientAttendanceOrProductionType getLeaveWithPay() {
		return leaveWithPay;
	}

	public void setLeaveWithPay(ClientAttendanceOrProductionType leaveWithPay) {
		this.leaveWithPay = leaveWithPay;
	}

	public ClientAttendanceOrProductionType getLeaveWithoutPay() {
		return leaveWithoutPay;
	}

	public void setLeaveWithoutPay(
			ClientAttendanceOrProductionType leaveWithoutPay) {
		this.leaveWithoutPay = leaveWithoutPay;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.ATTENDANCE_PAY_HEAD;
	}
}
