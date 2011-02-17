package com.vimukti.accounter.web.client.core;

@SuppressWarnings("serial")
public class ClientTAXCode implements IAccounterCore {

	String stringID;
	String name;
	String description;
	boolean isTaxable;
	boolean isActive;
	String TAXItemGrpForPurchases;
	String TAXItemGrpForSales;

	boolean isECSalesEntry;
	boolean isDefault;
	private String taxAgency;

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isTaxable() {
		return isTaxable;
	}

	public void setTaxable(boolean isTaxable) {
		this.isTaxable = isTaxable;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getTAXItemGrpForPurchases() {
		return TAXItemGrpForPurchases;
	}

	public void setTAXItemGrpForPurchases(String vATItemGrpForPurchases) {
		TAXItemGrpForPurchases = vATItemGrpForPurchases;
	}

	public String getTAXItemGrpForSales() {
		return TAXItemGrpForSales;
	}

	public void setTAXItemGrpForSales(String vATItemGrpForSales) {
		TAXItemGrpForSales = vATItemGrpForSales;
	}

	/**
	 * @return the isECSalesEntry
	 */
	public boolean isECSalesEntry() {
		return isECSalesEntry;
	}

	/**
	 * @param isECSalesEntry
	 *            the isECSalesEntry to set
	 */
	public void setECSalesEntry(boolean isECSalesEntry) {
		this.isECSalesEntry = isECSalesEntry;
	}

	@Override
	public String getClientClassSimpleName() {
		return "ClientTAXCode";
	}

	@Override
	public String getDisplayName() {
		return name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.TAX_CODE;
	}

	@Override
	public String getStringID() {
		return this.stringID;
	}

	@Override
	public void setStringID(String stringID) {
		this.stringID = stringID;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public void setTaxAgency(String taxAgency) {
		this.taxAgency = taxAgency;
	}

	public String getTaxAgency() {
		return taxAgency;
	}

}
