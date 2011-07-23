package com.vimukti.accounter.core;

import com.vimukti.accounter.web.client.InvalidOperationException;

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
	 * @return the id
	 */
	public long getId() {
		return id;
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
		// TODO Auto-generated method stub
		return this.id;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws InvalidOperationException {
		// TODO Auto-generated method stub
		return true;
	}

}
