package com.vimukti.accounter.web.client.core;

public class ClientAttendanceOrProductionType implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int TYPE_LEAVE_WITH_PAY = 1;
	public static final int TYPE_LEAVE_WITHOUT_PAY = 2;
	public static final int TYPE_PRODUCTION = 3;
	public static final int TYPE_USER_DEFINED_CALENDAR = 4;

	private int type;

	/**
	 * Name of the AttendanceOrProductionType
	 */
	private String name;

	/** Will use for Type Production */
	private ClientPayrollUnit unit;

	/** Use for all AttendanceOrProductionType except Production */
	private int periodType;
	private long id;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public ClientPayrollUnit getUnit() {
		return unit;
	}

	public void setUnit(ClientPayrollUnit unit) {
		this.unit = unit;
	}

	public int getPeriodType() {
		return periodType;
	}

	public void setPeriodType(int periodType) {
		this.periodType = periodType;
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
		return this.name;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.ATTENDANCE_PRODUCTION_TYPE;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return this.id;
	}

}
