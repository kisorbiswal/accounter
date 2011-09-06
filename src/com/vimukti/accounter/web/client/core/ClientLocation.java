package com.vimukti.accounter.web.client.core;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class ClientLocation implements IAccounterCore {

	public static final int LOCATION = 1;

	public static final int BUSINESS = 2;

	public static final int DEPARTMENT = 3;

	public static final int DIVISION = 4;

	public static final int PROPERTY = 5;

	public static final int STORE = 6;

	public static final int TERRITORY = 7;

	private static final long serialVersionUID = 1L;

	private String locationName;

	private long id;

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	@Override
	public String getName() {
		return locationName;
	}

	@Override
	public String getDisplayName() {
		return locationName;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.LOCATION;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public String getClientClassSimpleName() {
		return null;
	}

	@Override
	public int getVersion() {
		return 0;
	}

	@Override
	public void setVersion(int version) {
	}
}
