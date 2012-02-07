package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

/**
 * Class for any item. Basically we have divided items in two part SERVICE and
 * INVENTORY
 * 
 * @author Suresh
 * 
 */

public class Item extends CreatableObject implements IAccounterServerCore,
		INamedObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5861971680510342701L;

	/**
	 * Any item must belong these two
	 */
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

	public static final int INVENTORY_SCHME_FIFO = 1;
	public static final int INVENTORY_SCHME_LIFO = 2;
	public static final int INVENTORY_SCHME_AVERAGE = 3;

	/**
	 * Item Type
	 */
	int type;

	/**
	 * Item Name
	 */
	String name;

	String UPCorSKU;

	int weight;

	/**
	 * ItemGroup to which this Item Belongs
	 * 
	 * @see ItemGroup
	 */
	ItemGroup itemGroup;

	/**
	 * Is Item Active
	 */
	boolean isActive;

	boolean isISellThisItem;
	/**
	 * Sales Description
	 */
	String salesDescription;

	double salesPrice;

	/**
	 * Income Account
	 * 
	 * @see Account
	 */
	Account incomeAccount;

	boolean isTaxable;
	boolean isCommissionItem;

	boolean isIBuyThisItem;
	String purchaseDescription;

	double purchasePrice;
	TAXCode taxCode;

	double openingBalance;
	/**
	 * Expense Account set to this Item
	 * 
	 * @see Account
	 */
	Account expenseAccount;

	/**
	 * Preferred Vendor
	 * 
	 * @see Vendor
	 */
	Vendor preferredVendor;
	String vendorItemNumber;

	double standardCost;

	boolean isDefault;
	transient private boolean isOnSaveProccessed;

	private Quantity maxStockAlertLevel;
	private Quantity minStockAlertLevel;

	private Warehouse warehouse;
	private Measurement measurement;

	Set<ItemStatus> itemStatuses;

	private FinanceDate asOfDate;
	private Account assestsAccount;
	private int reorderPoint;
	private Quantity onHandQty;
	private double itemTotalValue;

	// TaxCode VATCode;

	public Item() {
	}

	/**
	 * @return the isDefault
	 */
	public boolean isDefault() {
		return isDefault;
	}

	public Quantity getMaxStockAlertLevel() {
		return maxStockAlertLevel;
	}

	public void setMaxStockAlertLevel(Quantity maxStockAlertLevel) {
		this.maxStockAlertLevel = maxStockAlertLevel;
	}

	public Quantity getMinStockAlertLevel() {
		return minStockAlertLevel;
	}

	public void setMinStockAlertLevel(Quantity minStockAlertLevel) {
		this.minStockAlertLevel = minStockAlertLevel;
	}

	/**
	 * @param isDefault
	 *            the isDefault to set
	 */
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
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
	 * @return the vatCode
	 */
	public TAXCode getTaxCode() {
		return taxCode;
	}

	/**
	 * @param vatCode
	 *            the vatCode to set
	 */
	public void setTaxCode(TAXCode taxCode) {
		this.taxCode = taxCode;
	}

	/**
	 * @return the uPCorSKU
	 */
	public String getUPCorSKU() {
		return UPCorSKU;
	}

	/**
	 * @return the weight
	 */
	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	/**
	 * @return the itemGroup
	 */
	public ItemGroup getItemGroup() {
		return itemGroup;
	}

	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * @return the isISellThisItem
	 */
	public boolean isISellThisItem() {
		return isISellThisItem;
	}

	/**
	 * @return the salesDescription
	 */
	public String getSalesDescription() {
		return salesDescription;
	}

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
	 * @return the incomeAccount
	 */
	public Account getIncomeAccount() {
		return incomeAccount;
	}

	/**
	 * @return the isCommissionItem
	 */
	public boolean isCommissionItem() {
		return isCommissionItem;
	}

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
	 * @return the purchaseDescription
	 */
	public String getPurchaseDescription() {
		return purchaseDescription;
	}

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
	 * @return the expenseAccount
	 */
	public Account getExpenseAccount() {
		return expenseAccount;
	}

	/**
	 * @return the preferredVendor
	 */
	public Vendor getPreferredVendor() {
		return preferredVendor;
	}

	public void setPreferredVendor(Vendor preferredVendor) {
		this.preferredVendor = preferredVendor;
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
	 * @return the standardCostisImported
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

	public void setItemGroup(ItemGroup itemGroup) {
		this.itemGroup = itemGroup;
	}

	public void setIncomeAccount(Account incomeAccount) {
		this.incomeAccount = incomeAccount;
	}

	public void setISellThisItem(boolean isISellThisItem) {
		this.isISellThisItem = isISellThisItem;
	}

	public void setActive(boolean iaActive) {

	}

	public void setIBuyThisItem(boolean isIBuyThisItem) {
		this.isIBuyThisItem = isIBuyThisItem;
	}

	public void setExpenseAccount(Account expanseAccount) {
		this.expenseAccount = expanseAccount;
	}

	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public boolean isTaxable() {
		return isTaxable;
	}

	public void setTaxable(boolean isTaxable) {
		this.isTaxable = isTaxable;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(this.getID());
		accounterCore.setObjectType(AccounterCoreType.ITEM);
		ChangeTracker.put(accounterCore);
		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {

	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		super.onSave(arg0);
		this.isOnSaveProccessed = true;
		if (type == TYPE_INVENTORY_PART || type == TYPE_INVENTORY_ASSEMBLY) {
			doCreateEffectForInventoryItem();
		}
		ChangeTracker.put(this);
		return false;

	}

	public void doCreateEffectForInventoryItem() {
		if (warehouse == null) {
			return;
		}
		if (!onHandQty.isEmpty() && !DecimalUtil.isEquals(itemTotalValue, 0)) {
			Session session = HibernateUtil.getCurrentSession();
			StockAdjustment adjustment = createStockAdjustment();
			session.save(adjustment);
		}
	}

	protected StockAdjustment createStockAdjustment() {

		String number = NumberUtils.getNextTransactionNumber(
				Transaction.TYPE_STOCK_ADJUSTMENT, getCompany());

		StockAdjustment adjustment = new StockAdjustment();
		adjustment.setCompany(getCompany());
		adjustment.setNumber(number);
		adjustment.setDate(asOfDate);
		adjustment.setMemo(AccounterServerConstants.MEMO_OPENING_BALANCE);
		adjustment.setAdjustmentAccount(getCompany()
				.getOpeningBalancesAccount());
		adjustment.setWareHouse(warehouse);

		TransactionItem item = new TransactionItem();
		item.setType(TransactionItem.TYPE_ITEM);
		item.setItem(this);
		item.setQuantity(onHandQty.copy());
		item.setUnitPrice(purchasePrice);
		item.setDescription(AccounterServerConstants.MEMO_OPENING_BALANCE);
		item.setTransaction(adjustment);
		item.setWareHouse(warehouse);
		item.setLineTotal(itemTotalValue);

		// Empty OnHandQty.Because StockAdjustment will update onHandQty
		onHandQty.setValue(0);
		item.doInventoryEffect();

		List<TransactionItem> adjustmentItems = new ArrayList<TransactionItem>();
		adjustmentItems.add(item);
		adjustment.setTransactionItems(adjustmentItems);

		return adjustment;
	}

	private void checkAccountsNull() throws AccounterException {
		if (isIBuyThisItem && expenseAccount == null) {
			throw new AccounterException(AccounterException.ERROR_ACCOUNT_NULL);
		}
		if (isISellThisItem && incomeAccount == null) {
			throw new AccounterException(AccounterException.ERROR_ACCOUNT_NULL);
		}
	}

	private void checkNameNull() throws AccounterException {
		if (name.trim().length() == 0) {
			throw new AccounterException(
					AccounterException.ERROR_ITEM_NAME_NULL);
		}
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		super.onUpdate(arg0);

		ChangeTracker.put(this);
		return false;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();

		if (!UserUtils.canDoThis(Item.class)) {
			throw new AccounterException(
					AccounterException.ERROR_DONT_HAVE_PERMISSION);
		}

		Item item = (Item) clientObject;
		Query query = session.getNamedQuery("getItem.by.Name")
				.setString("name", item.name)
				.setEntity("company", item.getCompany());
		List list = query.list();
		if (list != null && list.size() > 0) {
			Item newItem = (Item) list.get(0);
			if (item.getID() != newItem.getID()) {
				throw new AccounterException(
						AccounterException.ERROR_NAME_CONFLICT);
				// "An Item already exists with this Name");
			}
		}
		checkNameNull();
		checkAccountsNull();
		return true;

	}

	public void setMeasurement(Measurement measurement) {
		this.measurement = measurement;
	}

	public Measurement getMeasurement() {
		return measurement;
	}

	@Override
	public String toString() {

		return "Item Name:" + this.name + "  cost:" + this.standardCost;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Item) {
			return ((Item) obj).getID() == this.getID();
		}
		return false;
	}

	public double getOpeningBalance() {
		return openingBalance;
	}

	public void setOpeningBalance(double openingBalance) {
		this.openingBalance = openingBalance;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public FinanceDate getAsOfDate() {
		return asOfDate;
	}

	public void setAsOfDate(FinanceDate asOfDate) {
		this.asOfDate = asOfDate;
	}

	public Account getAssestsAccount() {
		return assestsAccount;
	}

	public void setAssestsAccount(Account assestsAccount) {
		this.assestsAccount = assestsAccount;
	}

	public int getReorderPoint() {
		return reorderPoint;
	}

	public void setReorderPoint(int reorderPoint) {
		this.reorderPoint = reorderPoint;
	}

	public Quantity getOnhandQty() {
		return onHandQty;
	}

	public void setOnhandQuantity(Quantity onhandQuantity) {
		this.onHandQty = onhandQuantity;
	}

	public double getItemTotalValue() {
		return itemTotalValue;
	}

	public void setItemTotalValue(double itemTotalValue) {
		this.itemTotalValue = itemTotalValue;
	}

	@Override
	public int getObjType() {
		return IAccounterCore.ITEM;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		AccounterMessages messages = Global.get().messages();

		w.put(messages.type(), messages.item()).gap();
		w.put(messages.name(), this.name);
	}

	public void updateBalance(TransactionItem transactionItem, boolean isSales) {
		if (getType() != Item.TYPE_INVENTORY_PART
				&& getType() != Item.TYPE_INVENTORY_ASSEMBLY) {
			return;
		}
		Warehouse wareHouse = transactionItem.getWareHouse();
		if (wareHouse == null) {
			return;
		}
		Quantity quantity = transactionItem.getQuantity();
		Session session = HibernateUtil.getCurrentSession();
		Unit selectedUnit = quantity.getUnit();
		Measurement defaultMeasurement = this.getMeasurement();
		Unit defaultUnit = defaultMeasurement.getDefaultUnit();
		Double value = (quantity.getValue() * selectedUnit.getFactor())
				/ defaultUnit.getFactor();
		wareHouse.updateItemStatus(this, value, isSales);
		wareHouse.onUpdate(session);
		session.saveOrUpdate(wareHouse);
		ChangeTracker.put(wareHouse);
		int activeInventoryScheme = getActiveInventoryScheme();
		if (isSales) {
			doSalesEffect(transactionItem, activeInventoryScheme, false);
		} else {
			doPurchaseEffect(transactionItem, activeInventoryScheme, false);
		}

		ChangeTracker.put(this);
	}

	public void doReverseEffect(TransactionItem transactionItem, boolean isSales) {
		if (this.getType() != Item.TYPE_INVENTORY_PART) {
			return;
		}
		Warehouse wareHouse = transactionItem.getWareHouse();
		if (wareHouse == null) {
			return;
		}
		Quantity quantity = transactionItem.getQuantity();
		Session session = HibernateUtil.getCurrentSession();
		Unit selectedUnit = quantity.getUnit();
		Measurement defaultMeasurement = this.getMeasurement();
		Unit defaultUnit = defaultMeasurement.getDefaultUnit();
		Double value = (quantity.getValue() * selectedUnit.getFactor())
				/ defaultUnit.getFactor();
		wareHouse.updateItemStatus(this, value, !isSales);
		wareHouse.onUpdate(session);
		session.saveOrUpdate(wareHouse);
		ChangeTracker.put(wareHouse);
		int activeInventoryScheme = getActiveInventoryScheme();
		if (isSales) {
			doSalesEffect(transactionItem, activeInventoryScheme, true);
		} else {
			doPurchaseEffect(transactionItem, activeInventoryScheme, true);
		}
		ChangeTracker.put(this);
	}

	private void doSalesEffect(TransactionItem transactionItem,
			int inventoryScheme, boolean isReverse) {
		Quantity quantity = transactionItem.getQuantity().copy();
		List<TransactionItem> sales = getSales();
		if (isReverse) {
			quantity.setValue(-quantity.getValue());
			transactionItem.modifyPurchases(null, false, null);
			sales.remove(transactionItem);
		}

		onHandQty = onHandQty.subtract(quantity);
		adjustSales(inventoryScheme, sales, getPurchases(inventoryScheme, -1));
	}

	private void doPurchaseEffect(TransactionItem transactionItem,
			int inventoryScheme, boolean isReverse) {
		Session session = HibernateUtil.getCurrentSession();
		Quantity quantity = transactionItem.getQuantity().copy();
		Double unitPrice = transactionItem.getUnitPrice();
		List<InventoryDetails> purchases = new ArrayList<Item.InventoryDetails>();
		if (isReverse) {
			quantity.setValue(-quantity.getValue());
			purchases = getPurchases(inventoryScheme, transactionItem.getID());
		} else {
			purchases = getPurchases(inventoryScheme, -1);
		}
		double amountToUpdate = (quantity.getValue() * unitPrice);
		Transaction transaction = transactionItem.getTransaction();
		assestsAccount.updateCurrentBalance(transaction, -amountToUpdate, 1);
		session.update(assestsAccount);
		onHandQty = onHandQty.add(quantity);

		adjustSales(inventoryScheme, getSales(), purchases);
	}

	private void adjustSales(int inventoryScheme, List<TransactionItem> sales,
			List<InventoryDetails> purchases) {
		if (sales.isEmpty()) {
			return;
		}
		Iterator<InventoryDetails> purchaseIterator = purchases.iterator();
		for (TransactionItem inventorySale : sales) {
			Quantity salesQty = inventorySale.getQuantity().copy();
			Map<Quantity, Double> purchaseForThisSale = new HashMap<Quantity, Double>();
			while (purchaseIterator.hasNext()) {
				InventoryDetails next = purchaseIterator.next();
				Quantity purchaseQty = next.quantity;
				int compareTo = purchaseQty.compareTo(salesQty);
				if (compareTo < 0) {
					purchaseForThisSale.put(purchaseQty, next.cost);
					purchaseIterator.remove();
					salesQty = salesQty.subtract(purchaseQty);
					continue;
				} else if (compareTo > 0) {
					purchaseQty = purchaseQty.subtract(salesQty);
					purchaseForThisSale.put(salesQty.copy(), next.cost);
					next.quantity = purchaseQty;
					salesQty.setValue(0.00D);
					break;
				} else {
					purchaseForThisSale.put(purchaseQty, next.cost);
					purchaseIterator.remove();
					salesQty.setValue(0.00D);
					break;
				}
			}
			Double averageCost = null;
			if (inventoryScheme == CompanyPreferences.INVENTORY_SCHME_AVERAGE) {
				averageCost = getAverageCost();
			}
			inventorySale
					.modifyPurchases(
							purchaseForThisSale,
							inventoryScheme == CompanyPreferences.INVENTORY_SCHME_AVERAGE,
							averageCost);
		}
	}

	private List<TransactionItem> getSales() {
		Session session = HibernateUtil.getCurrentSession();
		FlushMode flushMode = session.getFlushMode();
		try {
			session.setFlushMode(FlushMode.COMMIT);
			Query query = session.getNamedQuery("list.InventorySales")
					.setParameter("itemId", getID());

			return query.list();
		} finally {
			session.setFlushMode(flushMode);
		}
	}

	private List<InventoryDetails> getPurchases(int activeInventoryScheme,
			long exceptThis) {
		Session session = HibernateUtil.getCurrentSession();
		FlushMode flushMode = session.getFlushMode();
		List<InventoryDetails> details = new ArrayList<Item.InventoryDetails>();
		try {
			session.setFlushMode(FlushMode.COMMIT);
			Query query = null;
			if (activeInventoryScheme == CompanyPreferences.INVENTORY_SCHME_LIFO) {
				query = session.getNamedQuery("getPurchasesOfItem.for.LIFO")
						.setParameter("inventoryId", getID());
			} else {
				query = session.getNamedQuery("getPurchasesOfItem")
						.setParameter("inventoryId", getID());
			}

			Iterator<Object[]> result = query.list().iterator();
			while (result.hasNext()) {
				Object[] next = result.next();
				if (exceptThis > 0 && (Long) next[0] == exceptThis) {
					continue;
				}
				Quantity quantity = new Quantity();
				quantity.setValue((Double) next[1]);

				Unit unit = (Unit) session.get(Unit.class, (Long) next[2]);
				quantity.setUnit(unit);
				details.add(new InventoryDetails(quantity, (Double) next[3]));
			}
		} finally {
			session.setFlushMode(flushMode);
		}
		return details;
	}

	public Double getAverageCost() {
		Session session = HibernateUtil.getCurrentSession();
		FlushMode flushMode = session.getFlushMode();
		try {
			session.setFlushMode(FlushMode.COMMIT);
			Object result = session.getNamedQuery("getAverageCost.of.Item")
					.setParameter("inventoryId", getID()).uniqueResult();
			return (Double) result;
		} finally {
			session.setFlushMode(flushMode);
		}
	}

	private int getActiveInventoryScheme() {
		int schemeType = getCompany().getPreferences()
				.getActiveInventoryScheme();
		return schemeType;
	}

	public void adjustQuantityAndValue(StockAdjustment adjustment,
			Quantity currentQty, Quantity adjustedQty, double currentValue,
			double adjustedValue) {
		Session session = HibernateUtil.getCurrentSession();
		double amountToUpdate = adjustedValue - currentValue;
		Quantity quantityToUpdate = adjustedQty.subtract(currentQty);
		getAssestsAccount()
				.updateCurrentBalance(adjustment, -amountToUpdate, 1);
		session.saveOrUpdate(assestsAccount);
		setOnhandQuantity(getOnhandQty().add(quantityToUpdate));
		ChangeTracker.put(this);
	}

	class InventoryDetails {
		Quantity quantity;
		double cost;

		InventoryDetails(Quantity quantity, double cost) {
			this.quantity = quantity;
			this.cost = cost;
		}
	}

}
