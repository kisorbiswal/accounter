package com.vimukti.accounter.core;

import org.hibernate.Session;

import com.vimukti.accounter.utils.HibernateUtil;

/**
 * On Production Calculation Type is used to calculate the pay value based on
 * the Production/Work down. The production data can be entered in Attendance
 * voucher against the Production type.
 * 
 * @author Prasanna Kumar G
 * 
 */
public class ProductionPayHead extends PayHead {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	AttendanceOrProductionType productionType;

	public ProductionPayHead() {
		super(CALCULATION_TYPE_ON_PRODUCTION);
	}

	/**
	 * @return the productionType
	 */
	public AttendanceOrProductionType getProductionType() {
		return productionType;
	}

	/**
	 * @param productionType
	 *            the productionType to set
	 */
	public void setProductionType(AttendanceOrProductionType productionType) {
		this.productionType = productionType;
	}

	@Override
	public double calculatePayment(PayStructureItem payStructureItem,
			double deductions, double earnings) {
		Session currentSession = HibernateUtil.getCurrentSession();
		AttendanceManagementItem item = (AttendanceManagementItem) currentSession
				.getNamedQuery("getAttendanceMItem.by.employee")
				.setParameter("employee",
						payStructureItem.getPayStructure().getEmployee())
				.setParameter("attendanceType", this.getProductionType())
				.uniqueResult();
		long workingDays = item == null ? 0 : item.getNumber();
		double rate = payStructureItem.getRate();
		double earningSalary = 0.0;
		if (workingDays != 0) {
			double perDayAmount = rate / workingDays;
			earningSalary = perDayAmount * payStructureItem.getAttendance()[1];
		}
		return earningSalary;
	}

}
