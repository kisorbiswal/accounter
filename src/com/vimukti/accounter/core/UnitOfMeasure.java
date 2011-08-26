package com.vimukti.accounter.core;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class UnitOfMeasure implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = -952703729336678057L;
	int version;
	/**
	 * Type of the UnitOfMeasure
	 */
	int type;

	/**
	 * The name of the UnitOfMeasure
	 */
	String name;
	String abbreviation;
	private long id;

	/**
	 * @return the version
	 */
	public UnitOfMeasure() {

	}

	public int getVersion() {
		return version;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the abbreviation
	 */
	public String getAbbreviation() {
		return abbreviation;
	}

	@Override
	public long getID() {

		return this.id;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {

		return true;
	}

	@Override
	public void setVersion(int version) {
		this.version=version;
		
	}

}
