package com.vimukti.accounter.core;

import java.util.Calendar;

/**
 * On Attendance type of Calculation Type is based on the attendance data where
 * the component will get pro-rated based on the actual days the Employee is
 * present in the office for the selected period. In Tally.ERP 9 the attendance
 * can be recorded as positive days (Present) or loss of pay (Absent) type.
 * 
 * @author Prasanna Kumar G
 * 
 */
public class AttendancePayHead extends PayHead {

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

	private AttendanceOrProductionType leaveWithPay;

	/**
	 * Will be used if leaveWithPay does not Exist
	 */
	private AttendanceOrProductionType leaveWithoutPay;

	public AttendancePayHead() {
		super(CALCULATION_TYPE_ON_ATTENDANCE);
	}

	/**
	 * @return the calculationPeriod
	 */
	public int getCalculationPeriod() {
		return calculationPeriod;
	}

	/**
	 * @param calculationPeriod
	 *            the calculationPeriod to set
	 */
	public void setCalculationPeriod(int calculationPeriod) {
		this.calculationPeriod = calculationPeriod;
	}

	/**
	 * @return the perDayCalculationBasis
	 */
	public int getPerDayCalculationBasis() {
		return perDayCalculationBasis;
	}

	/**
	 * @param perDayCalculationBasis
	 *            the perDayCalculationBasis to set
	 */
	public void setPerDayCalculationBasis(int perDayCalculationBasis) {
		this.perDayCalculationBasis = perDayCalculationBasis;
	}

	/**
	 * @return the leaveWithPay
	 */
	public AttendanceOrProductionType getLeaveWithPay() {
		return leaveWithPay;
	}

	/**
	 * @param leaveWithPay
	 *            the leaveWithPay to set
	 */
	public void setLeaveWithPay(AttendanceOrProductionType leaveWithPay) {
		this.leaveWithPay = leaveWithPay;
	}

	/**
	 * @return the leaveWithoutPay
	 */
	public AttendanceOrProductionType getLeaveWithoutPay() {
		return leaveWithoutPay;
	}

	/**
	 * @param leaveWithoutPay
	 *            the leaveWithoutPay to set
	 */
	public void setLeaveWithoutPay(AttendanceOrProductionType leaveWithoutPay) {
		this.leaveWithoutPay = leaveWithoutPay;
	}

	@Override
	public double calculatePayment(EmployeePayHeadComponent payHeadComponent,
			double deductions, double earnings) {
		double rate = payHeadComponent.getRate();
		double deductableSalary = rate;
		double perDayAmount = rate / getWorkingDays(payHeadComponent);
		deductableSalary = perDayAmount * payHeadComponent.getNoOfLeaves();
		return deductableSalary;
	}

	private long getWorkingDays(
			EmployeePayHeadComponent employeePayHeadComponent) {
		long workingDays = 0;

		if (getPerDayCalculationBasis() == PER_DAY_CALCULATION_AS_PER_CALANDAR_PERIOD) {
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(employeePayHeadComponent.getStartDate()
					.getAsDateObject());
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(employeePayHeadComponent.getEndDate()
					.getAsDateObject());
			long diff = calendar2.getTimeInMillis()
					- calendar1.getTimeInMillis();
			long diffDays = diff / (24 * 60 * 60 * 1000);
			workingDays = diffDays;
		}

		if (getPerDayCalculationBasis() == PER_DAY_CALCULATION_USER_DEFINED) {
			workingDays = 30;
		}

		if (getPerDayCalculationBasis() == PER_DAY_CALCULATION_USER_DEFINED_CALANDAR) {
			PayRollDetails companyHolidays = getCompanyHolidaysWithGivenPeriod(employeePayHeadComponent);
			workingDays = companyHolidays.getWorkingDays()
					- companyHolidays.getHoliDays();
		}

		return workingDays;
	}
}
