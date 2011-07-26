/**
 * 
 */
package com.vimukti.accounter.web.client.core;

import java.io.Serializable;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.core.Unit;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ClientMeasurement implements Serializable, IsSerializable {

	private ClientUnit defaultUnit;

	private String desctiption;

	private long id;

	private String name;
	private Set<Unit> units;

	/**
	 * @return the defaultUnit
	 */
	public ClientUnit getDefaultUnit() {
		return defaultUnit;
	}

	/**
	 * @param defaultUnit
	 *            the defaultUnit to set
	 */
	public void setDefaultUnit(ClientUnit defaultUnit) {
		this.defaultUnit = defaultUnit;
	}

	/**
	 * @return the desctiption
	 */
	public String getDesctiption() {
		return desctiption;
	}

	/**
	 * @param desctiption
	 *            the desctiption to set
	 */
	public void setDesctiption(String desctiption) {
		this.desctiption = desctiption;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the units
	 */
	public Set<Unit> getUnits() {
		return units;
	}

	/**
	 * @param units
	 *            the units to set
	 */
	public void setUnits(Set<Unit> units) {
		this.units = units;
	}

}
