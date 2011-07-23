package com.vimukti.accounter.web.client.core;

import java.util.List;

@SuppressWarnings("serial")
public class ClientTAXGroup extends ClientTAXItemGroup {

	List<ClientTAXItem> taxItems;
	double groupRate;

	/**
	 * @return the groupRate
	 */
	public double getGroupRate() {
		return groupRate;
	}

	/**
	 * @param groupRate
	 *            the groupRate to set
	 */
	public void setGroupRate(double groupRate) {
		this.groupRate = groupRate;
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
	public void setID(long id){
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
	 * @return the vatItems
	 */
	public List<ClientTAXItem> getTaxItems() {
		return taxItems;
	}

	/**
	 * @param vatItems
	 *            the vatItems to set
	 */
	public void setTaxItems(List<ClientTAXItem> taxItems) {
		this.taxItems = taxItems;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * @param isActive
	 *            the isActive to set
	 */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public AccounterCoreType getObjectType() {

		return AccounterCoreType.TAX_GROUP;
	}

}
