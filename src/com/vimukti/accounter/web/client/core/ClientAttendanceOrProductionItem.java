package com.vimukti.accounter.web.client.core;


public class ClientAttendanceOrProductionItem implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ClientAttendanceOrProductionType attendanceOrProductionType;

	private double value;

	private int version;

	private long id;

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
		return "Attendance Or Production Item";
	}

	@Override
	public String getDisplayName() {
		return getName();
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.ATTENDANCE_PRODUCTION_ITEM;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return this.id;
	}

	public ClientAttendanceOrProductionType getAttendanceOrProductionType() {
		return attendanceOrProductionType;
	}

	public void setAttendanceOrProductionType(
			ClientAttendanceOrProductionType attendanceOrProductionType) {
		this.attendanceOrProductionType = attendanceOrProductionType;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

}
