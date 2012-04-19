package com.vimukti.accounter.core;

import org.json.JSONException;

import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * @author Prasanna Kumar g
 * 
 */
public class ComputaionFormulaFunction implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int FUNCTION_ADD_PAY_HEAD = 1;

	public static final int FUNCTION_SUBSTRACT_PAY_HEAD = 2;

	public static final int FUNCTION_DIVIDE_ATTENDANCE = 3;

	public static final int FUNCTION_MULTIPLY_ATTENDANCE = 4;

	private int functionType;

	private PayHead payHead;

	private AttendanceOrProductionType attendanceType;

	/**
	 * @return the functionType
	 */
	public int getFunctionType() {
		return functionType;
	}

	/**
	 * @param functionType
	 *            the functionType to set
	 */
	public void setFunctionType(int functionType) {
		this.functionType = functionType;
	}

	/**
	 * @return the payHead
	 */
	public PayHead getPayHead() {
		return payHead;
	}

	/**
	 * @param payHead
	 *            the payHead to set
	 */
	public void setPayHead(PayHead payHead) {
		this.payHead = payHead;
	}

	public AttendanceOrProductionType getAttendanceType() {
		return attendanceType;
	}

	public void setAttendanceType(AttendanceOrProductionType attendanceType) {
		this.attendanceType = attendanceType;
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
	public long getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

	public double calculatePayment(EmployeePayHeadComponent payHeadComponent,
			double deductions, double earnings) {
		return payHead.calculatePayment(payHeadComponent, deductions, earnings);
	}

}
