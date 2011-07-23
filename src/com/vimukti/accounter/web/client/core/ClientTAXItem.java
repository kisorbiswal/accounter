package com.vimukti.accounter.web.client.core;

@SuppressWarnings("serial")
public class ClientTAXItem extends ClientTAXItemGroup {

	String taxAgency;

	double taxRate;

	String vatReturnBox;

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
	 * @return the vatAgency
	 */
	public String getTaxAgency() {
		return taxAgency;
	}

	/**
	 * @param taxAgency
	 *            the vatAgency to set
	 */
	public void setTaxAgency(String taxAgency) {
		this.taxAgency = taxAgency;
	}

	/**
	 * @return the vatRate
	 */
	public double getTaxRate() {
		return taxRate;
	}

	/**
	 * @param taxRate
	 *            the vatRate to set
	 */
	public void setTaxRate(double taxRate) {
		this.taxRate = taxRate;
	}

	/**
	 * @return the vatReturnBox
	 */
	public String getVatReturnBox() {
		return vatReturnBox;
	}

	/**
	 * @param vatReturnBox
	 *            the vatReturnBox to set
	 */
	public void setVatReturnBox(String vatReturnBox) {
		this.vatReturnBox = vatReturnBox;
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

		return AccounterCoreType.TAXITEM;
	}

}
