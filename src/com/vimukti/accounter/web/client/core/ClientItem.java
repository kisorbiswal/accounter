package com.vimukti.accounter.web.client.core;

import java.util.Set;

import com.vimukti.accounter.web.client.core.reports.BaseReport;

public class ClientItem extends BaseReport implements IAccounterCore,
		IAccountable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int TYPE_SERVICE = 1;
	public static final int TYPE_INVENTORY_PART = 2;

	public static final int TYPE_NON_INVENTORY_PART = 3;
	public static final int TYPE_INVENTORY_ASSEMBLY = 4;
	public static final int TYPE_OTHER_CHARGE = 5;
	public static final int TYPE_SUBTOTAL = 6;
	public static final int TYPE_GROUP = 7;
	public static final int TYPE_DISCOUNT = 8;
	public static final int TYPE_PAYMENT = 9;
	public static final int TYPE_SALES_TAX_ITEM = 10;
	public static final int TYPE_SALES_TAX_GROUP = 11;

	long id;

	int type;
	String name;
	String UPCorSKU;
	int weight;
	long itemGroup;
	boolean isActive;

	public boolean isISellThisItem;
	String salesDescription;

	double salesPrice;
	long incomeAccount;
	boolean isTaxable;
	boolean isCommissionItem;

	public boolean isIBuyThisItem;
	String purchaseDescription;

	double purchasePrice;
	long expenseAccount;
	long preferredVendor;
	String vendorItemNumber;
	long taxCode;
	double standardCost;
	int version;
	double openingBalance;

	private ClientQuantity maxStockAlertLevel;
	private ClientQuantity minStockAlertLevel;

	private long warehouse;
	private long measurement;

	boolean isDefault;

	Set<ClientItemStatus> itemStatuses;

	private long assestsAccount;
	private int reorderPoint;
	private ClientQuantity onHandQty;
	private double itemTotalValue;
	private ClientFinanceDate asOfDate;

	private Double assetValue;

	public ClientItem() {
	}

	/**
	 * @return the vatCode
	 */
	public long getTaxCode() {
		return taxCode;
	}

	/**
	 * @param taxCode
	 *            the vatCode to set
	 */
	public void setTaxCode(long taxCode) {
		this.taxCode = taxCode;
	}

	/**
	 * @return the isDefault
	 */
	public boolean isDefault() {
		return isDefault;
	}

	/**
	 * @param isDefault
	 *            the isDefault to set
	 */
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public long getIncomeAccount() {
		return incomeAccount;
	}

	public void setIncomeAccount(long incomeAccount) {
		this.incomeAccount = incomeAccount;
	}

	public long getExpenseAccount() {
		return expenseAccount;
	}

	public void setExpenseAccount(long expenseAccount) {
		this.expenseAccount = expenseAccount;
	}

	public long getPreferredVendor() {
		return preferredVendor;
	}

	public void setPreferredVendor(long preferredVendor) {
		this.preferredVendor = preferredVendor;
	}

	public void setItemGroup(long itemGroup) {
		this.itemGroup = itemGroup;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the uPCorSKU
	 */
	public String getUPCorSKU() {
		return UPCorSKU;
	}

	/**
	 * @param corSKU
	 *            the uPCorSKU to set
	 */
	public void setUPCorSKU(String corSKU) {
		UPCorSKU = corSKU;
	}

	/**
	 * @return the weight
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * @param weight
	 *            the weight to set
	 */
	public void setWeight(int weight) {
		this.weight = weight;
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

	/**
	 * @return the isISellThisItem
	 */
	public boolean isISellThisItem() {
		return isISellThisItem;
	}

	/**
	 * @param isISellThisItem
	 *            the isISellThisItem to set
	 */
	public void setISellThisItem(boolean isISellThisItem) {
		this.isISellThisItem = isISellThisItem;
	}

	/**
	 * @return the salesDescription
	 */
	public String getSalesDescription() {
		return salesDescription;
	}

	/**
	 * @param salesDescription
	 *            the salesDescription to set
	 */
	public void setSalesDescription(String salesDescription) {
		this.salesDescription = salesDescription;
	}

	/**
	 * @return the salesPrice
	 */
	public double getSalesPrice() {
		return salesPrice;
	}

	/**
	 * @param salesPrice
	 *            the salesPrice to set
	 */
	public void setSalesPrice(double salesPrice) {
		this.salesPrice = salesPrice;
	}

	/**
	 * @return the isCommissionItem
	 */
	public boolean isCommissionItem() {
		return isCommissionItem;
	}

	/**
	 * @param isCommissionItem
	 *            the isCommissionItem to set
	 */
	public void setCommissionItem(boolean isCommissionItem) {
		this.isCommissionItem = isCommissionItem;
	}

	/**
	 * @return the isIBuyThisItem
	 */
	public boolean isIBuyThisItem() {
		return isIBuyThisItem;
	}

	/**
	 * @param isIBuyThisItem
	 *            the isIBuyThisItem to set
	 */
	public void setIBuyThisItem(boolean isIBuyThisItem) {
		this.isIBuyThisItem = isIBuyThisItem;
	}

	/**
	 * @return the purchaseDescription
	 */
	public String getPurchaseDescription() {
		return purchaseDescription;
	}

	/**
	 * @param purchaseDescription
	 *            the purchaseDescription to set
	 */
	public void setPurchaseDescription(String purchaseDescription) {
		this.purchaseDescription = purchaseDescription;
	}

	/**
	 * @return the purchasePrice
	 */
	public double getPurchasePrice() {
		return purchasePrice;
	}

	/**
	 * @param purchasePrice
	 *            the purchasePrice to set
	 */
	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	/**
	 * @return the vendorItemNumber
	 */
	public String getVendorItemNumber() {
		return vendorItemNumber;
	}

	/**
	 * @param vendorItemNumber
	 *            the vendorItemNumber to set
	 */
	public void setVendorItemNumber(String vendorItemNumber) {
		this.vendorItemNumber = vendorItemNumber;
	}

	/**
	 * @return the standardCost
	 */
	public double getStandardCost() {
		return standardCost;
	}

	/**
	 * @param standardCost
	 *            the standardCost to set
	 */
	public void setStandardCost(double standardCost) {
		this.standardCost = standardCost;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @param id
	 *            the id to set
	 */

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDisplayName() {

		return name;
	}

	@Override
	public String getName() {
		return name;
	}

	public boolean isTaxable() {
		return isTaxable;
	}

	public void setTaxable(boolean isTaxable) {
		this.isTaxable = isTaxable;
	}

	public long getItemGroup() {
		return this.itemGroup;
	}

	@Override
	public AccounterCoreType getObjectType() {

		return AccounterCoreType.ITEM;
	}

	// public void setVatCode(String vatcode) {
	// this.VATCode = vatcode;
	// }
	//
	// public String getVatCode() {
	// return this.VATCode;
	// }

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}

	public ClientItem clone() {
		ClientItem item = this.clone();
		return item;

	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ClientItem) {
			ClientItem item = (ClientItem) obj;
			if (this.getID() == item.getID())
				return true;
		}
		return false;
	}

	public double getOpeningBalance() {
		return openingBalance;
	}

	public void setOpeningBalance(double openingBalance) {
		this.openingBalance = openingBalance;
	}

	public ClientQuantity getMaxStockAlertLevel() {
		return maxStockAlertLevel;
	}

	public void setMaxStockAlertLevel(ClientQuantity maxStockAlertLevel) {
		this.maxStockAlertLevel = maxStockAlertLevel;
	}

	public ClientQuantity getMinStockAlertLevel() {
		return minStockAlertLevel;
	}

	public void setMinStockAlertLevel(ClientQuantity minStockAlertLevel) {
		this.minStockAlertLevel = minStockAlertLevel;
	}

	public long getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(long warehouse) {
		this.warehouse = warehouse;
	}

	public long getMeasurement() {
		return measurement;
	}

	public void setMeasurement(long measurement) {
		this.measurement = measurement;
	}

	public long getAssestsAccount() {
		return assestsAccount;
	}

	public void setAssestsAccount(long assestsAccount) {
		this.assestsAccount = assestsAccount;
	}

	public int getReorderPoint() {
		return reorderPoint;
	}

	public void setReorderPoint(int reorderPoint) {
		this.reorderPoint = reorderPoint;
	}

	public ClientQuantity getOnhandQty() {
		return onHandQty;
	}

	public void setOnhandQty(ClientQuantity onhandQuantity) {
		this.onHandQty = onhandQuantity;
	}

	public ClientFinanceDate getAsOfDate() {
		return asOfDate;
	}

	public void setAsOfDate(ClientFinanceDate asOfDate) {
		this.asOfDate = asOfDate;
	}

	public double getItemTotalValue() {
		return itemTotalValue;
	}

	public void setItemTotalValue(double itemTotalValue) {
		this.itemTotalValue = itemTotalValue;
	}

	public double getAvarageCost() {
		return purchasePrice;
	}

	public void setAssetValue(Double value) {
		this.assetValue = value;
	}

	/**
	 * @return the assetValue
	 */
	public Double getAssetValue() {
		return assetValue;
	}

}
