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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setVersion(int version) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
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

}
