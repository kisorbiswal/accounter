package com.vimukti.accounter.web.client.core;

public class ClientAttendanceManagementItem implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ClientEmployee employee;
	private ClientAttendanceOrProductionType attendanceType;
	private long value;
	private long id;
	private long currBal;

	private int version;

	public ClientEmployee getEmployee() {
		return employee;
	}

	public void setEmployee(ClientEmployee employee) {
		this.employee = employee;
	}

	public ClientAttendanceOrProductionType getAttendanceType() {
		return attendanceType;
	}

	public void setAttendanceType(
			ClientAttendanceOrProductionType attendanceType) {
		this.attendanceType = attendanceType;
	}

	public long getNumber() {
		return value;
	}

	public void setNumber(long number) {
		this.value = number;
	}

	@Override
	public int getVersion() {
		return this.version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String getName() {
		return "Attendance Management Item";
	}

	@Override
	public String getDisplayName() {
		return getName();
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.ATTENDANCE_MANAGEMENT;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return this.id;
	}

	public long getCurrBal() {
		return currBal;
	}

	public void setCurrBal(long currBal) {
		this.currBal = currBal;
	}

	public boolean isAllowed() {
		return (employee != null && value != 0 && attendanceType != null);
	}

}
