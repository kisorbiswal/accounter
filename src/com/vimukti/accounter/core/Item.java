package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.CallbackException;
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
	private long onhandQuantity;
	private double itemTotalValue;

	private long balance;

	private Set<InventoryItemHistory> inventoryHistory = new HashSet<InventoryItemHistory>();

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
		if (type == TYPE_INVENTORY_PART) {
			doCreateEffectForInventoryItem();
		}
		return false;

	}

	public void doCreateEffectForInventoryItem() {
		if (warehouse == null) {
			return;
		}
		Session session = HibernateUtil.getCurrentSession();

		if (!DecimalUtil.isEquals(itemTotalValue, 0)) {
			JournalEntry journalEntry = createJournalEntry();
			session.save(journalEntry);
			for (int x = 0; x < balance; x++) {
				InventoryItemHistory history = new InventoryItemHistory(this,
						purchasePrice, journalEntry);
				session.save(history);
			}
		}

		Unit selectedUnit = this.getMeasurement().getDefaultUnit();
		Measurement defaultMeasurement = this.getMeasurement();
		Unit defaultUnit = defaultMeasurement.getDefaultUnit();
		Double value = (this.onhandQuantity * selectedUnit.getFactor())
				/ defaultUnit.getFactor();
		warehouse.updateItemStatus(this, value, false);
		warehouse.onUpdate(session);
		session.saveOrUpdate(warehouse);

		ChangeTracker.put(warehouse);
	}

	protected JournalEntry createJournalEntry() {

		String number = NumberUtils.getNextTransactionNumber(
				Transaction.TYPE_JOURNAL_ENTRY, getCompany());

		JournalEntry journalEntry = new JournalEntry();
		// journalEntry.setInvolvedPayee(this);
		journalEntry.setCompany(getCompany());
		journalEntry.number = number;
		journalEntry.transactionDate = asOfDate;
		journalEntry.memo = "Opening Balance";
		journalEntry.balanceDue = itemTotalValue;

		List<TransactionItem> items = new ArrayList<TransactionItem>();
		// Line 1
		TransactionItem item1 = new TransactionItem();
		item1.setAccount(getCompany().getOpeningBalancesAccount());
		item1.setType(TransactionItem.TYPE_ACCOUNT);
		item1.setDescription(getName());
		item1.setLineTotal(-1 * itemTotalValue);
		items.add(item1);

		TransactionItem item2 = new TransactionItem();
		item2.setAccount(assestsAccount);
		item2.setType(TransactionItem.TYPE_ACCOUNT);
		item2.setDescription(AccounterServerConstants.MEMO_OPENING_BALANCE);
		item2.setLineTotal(itemTotalValue);
		items.add(item2);

		journalEntry.setDebitTotal(items.get(1).getLineTotal());
		journalEntry.setCreditTotal(items.get(0).getLineTotal());

		journalEntry.setTransactionItems(items);

		journalEntry.setCurrency(getCompany().getPrimaryCurrency());
		journalEntry.setDate(new FinanceDate());

		return journalEntry;
	}

	public void doReverseEffectForInventoryItem() {

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

	public long getOnhandQuantity() {
		return onhandQuantity;
	}

	public void setOnhandQuantity(long onhandQuantity) {
		this.onhandQuantity = onhandQuantity;
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

	public Set<InventoryItemHistory> getInventoryHistory() {
		return inventoryHistory;
	}

	public void setInventoryHistory(Set<InventoryItemHistory> inventoryHistory) {
		this.inventoryHistory = inventoryHistory;
	}

	public void updateBalance(TransactionItem transactionItem) {
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
		Transaction transaction = transactionItem.getTransaction();
		if (transaction.isDebitTransaction()) {
			wareHouse.updateItemStatus(this, value, false);
		} else {
			wareHouse.updateItemStatus(this, value, true);
		}
		wareHouse.onUpdate(session);
		session.saveOrUpdate(wareHouse);
		ChangeTracker.put(wareHouse);

		updateAccounts(transactionItem);

		
		InventoryItemHistory similarHistory = getSimilarHistory(transactionItem
				.getUpdateAmount());
		if (similarHistory != null) {
			session.delete(similarHistory);
		} else {
			InventoryItemHistory history = new InventoryItemHistory(this,
					transactionItem.getUpdateAmount(), transaction);
			session.save(history);
		}
	}

	private InventoryItemHistory getSimilarHistory(double cost) {
		for (InventoryItemHistory history : getInventoryHistory()) {
			if (history.getCost() == -cost) {
				return history;
			}
		}
		return null;
	}

	private void updateAccounts(TransactionItem transactionItem) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction transaction = transactionItem.getTransaction();
		double currencyFactor = transaction.getCurrencyFactor();
		if (this.getType() == Item.TYPE_INVENTORY_PART) {
			if (transaction.getTransactionCategory() != Transaction.CATEGORY_VENDOR) {
				double costByInventoryScheme = getupdateAmount(transactionItem
						.getItem());
				if (expenseAccount != null) {
					expenseAccount.updateCurrentBalance(transaction,
							costByInventoryScheme, currencyFactor);
					session.update(expenseAccount);
				}
				if (assestsAccount != null) {
					assestsAccount.updateCurrentBalance(transaction,
							-costByInventoryScheme, currencyFactor);
					session.update(assestsAccount);
				}
			}
		}
	}

	private void doReverseEffectAccounts(TransactionItem transactionItem) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction transaction = transactionItem.getTransaction();
		double currencyFactor = transaction.getCurrencyFactor();
		if (this.getType() == Item.TYPE_INVENTORY_PART) {
			if (transaction.getTransactionCategory() != Transaction.CATEGORY_VENDOR) {
				double costByInventoryScheme = getupdateAmount(transactionItem
						.getItem());
				if (expenseAccount != null) {
					expenseAccount.updateCurrentBalance(transaction,
							costByInventoryScheme, currencyFactor);
					session.update(expenseAccount);
				}
				if (assestsAccount != null) {
					assestsAccount.updateCurrentBalance(transaction,
							-costByInventoryScheme, currencyFactor);
					session.update(assestsAccount);
				}
			}
		}

	}

	public void doReverseEffect(TransactionItem transactionItem) {
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
		if (transactionItem.getTransaction().isDebitTransaction()) {
			wareHouse.updateItemStatus(this, value, true);
		} else {
			wareHouse.updateItemStatus(this, value, false);
		}
		wareHouse.onUpdate(session);
		session.saveOrUpdate(wareHouse);
		ChangeTracker.put(wareHouse);

		doReverseEffectAccounts(transactionItem);
	}

	/**
	 * This will be used for Inventory Items Only.
	 * 
	 * @param item
	 * 
	 * @return
	 */
	public double getupdateAmount(Item item) {

		int type = getCompany().getPreferences().getInventoryScheme();
		List<InventoryItemHistory> list = new ArrayList<InventoryItemHistory>(
				item.getInventoryHistory());
		if (list.isEmpty()) {
			// TODO
		}
		switch (type) {
		case Item.INVENTORY_SCHME_FIFO: {

			return getCostByFIFO(list);
		}
		case Item.INVENTORY_SCHME_LIFO: {
			return getCostByLIFO(list);
		}
		case Item.INVENTORY_SCHME_AVERAGE: {

			return getCostByAverage(list);
		}
		default:
			return 0.0;
		}

	}

	private double getCostByAverage(List<InventoryItemHistory> list) {
		int total = 0;

		for (InventoryItemHistory history : list) {
			total += history.getCost();

		}
		double cost = total / list.size();
		return cost;
	}

	private double getCostByLIFO(List<InventoryItemHistory> list) {
		InventoryItemHistory inventoryItemHistory = list.get(list.size() - 1);
		double cost = inventoryItemHistory.getCost();
		return cost;
	}

	private double getCostByFIFO(List<InventoryItemHistory> list) {
		double cost = 0.0;
		InventoryItemHistory inventoryItemHistory = list.get(1);
		cost = inventoryItemHistory.getCost();
		return cost;
	}
}
