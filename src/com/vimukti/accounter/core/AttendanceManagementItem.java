package com.vimukti.accounter.core;

import org.json.JSONException;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class AttendanceManagementItem extends CreatableObject implements
		IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Employee employee;
	private AttendanceOrProductionType attendanceType;
	private long value;

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public AttendanceOrProductionType getAttendanceType() {
		return attendanceType;
	}

	public void setAttendanceType(AttendanceOrProductionType attendanceType) {
		this.attendanceType = attendanceType;
	}

	public long getNumber() {
		return value;
	}

	public void setNumber(long number) {
		this.value = number;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

	@Override
	public void selfValidate() throws AccounterException {
		// TODO Auto-generated method stub

	}

}
