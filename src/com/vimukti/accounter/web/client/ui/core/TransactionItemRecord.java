///**
// * 
// */
//package com.vimukti.accounter.web.client.ui.core;
//
//import com.vimukti.accounter.web.client.core.ClientAccount;
//import com.vimukti.accounter.web.client.core.ClientItem;
//import com.vimukti.accounter.web.client.core.ClientTaxAgency;
//import com.vimukti.accounter.web.client.core.ClientTaxCode;
//import com.vimukti.accounter.web.client.core.ClientTaxGroup;
//import com.vimukti.accounter.web.client.core.ClientTransaction;
//import com.vimukti.accounter.web.client.core.ClientTransactionItem;
//import com.vimukti.accounter.web.client.ui.DataUtils;
//import com.vimukti.accounter.web.client.ui.FinanceApplication;
//import com.vimukti.accounter.web.client.ui.widgets.ListGrid;
//
///**
// * @author Fernandez
// * 
// */
//public class TransactionItemRecord extends ListGridRecord {
//
//	static final int CUSTOMER_TRANSACTION = 1;
//
//	static final int VENDOR_TRANSACTION = 2;
//
//	static final int BANKING_TRANSACTION = 3;
//
//	public static final String ATTR_NAME = "name";
//
//	public static final String ATTR_DISCRIPTION = "description";
//
//	public static final String ATTR_QTY = "qty";
//
//	public static final String ATTR_UNITPRICE = "unitPrice";
//
//	public static final String ATTR_DISCOUNT = "discount";
//
//	public static final String ATTR_LINETOTAL = "lineTotal";
//
//	public static final String ATTR_ITEMTAX = "itemTax";
//
//	public static final String ATTR_VATCODE = "vatCode";
//
//	public static final String ATTR_VAT = "vat";
//
//	public static final String TYPE_ACCOUNT = "4";
//
//	public static final String TYPE_ITEM = "1";
//
//	public static final String TYPE_SALESTAX = "3";
//
//	public static final String TYPE_COMMENT = "2";
//
//	private ClientTransaction transaction;
//
//	int transactionItemType;
//
//	int transactionDomain;
//
//	String description;
//
//	Double vat;
//
//	Double quantity = 1D;
//
//	Double unitPrice = 0.0D;
//
//	Double discount = 0.0D;
//
//	Double lineTotal = 0.0D;
//
//	ClientAccount account;
//
//	ClientItem item;
//
//	ClientTaxCode taxCode;
//
//	String itemTax;
//
//	ClientTaxGroup taxGroup;
//
//	ClientTransactionItem transactionItem;
//
//	ListGrid grid;
//
//	ClientTaxAgency agency;
//
//	public void setType(String type) {
//
//		if (type == null)
//			type = "none";
//
//		if (type.equals(TransactionItemRecord.TYPE_ACCOUNT)) {
//			setAttribute("type", TYPE_ACCOUNT);
//			transactionItemType = 4;
//		} else if (type.equals(TransactionItemRecord.TYPE_COMMENT)) {
//			setAttribute("type", TYPE_COMMENT);
//			transactionItemType = 2;
//		} else if (type.equals(TransactionItemRecord.TYPE_ITEM)) {
//			setAttribute("type", TYPE_ITEM);
//			transactionItemType = 1;
//		} else if (type.equals(TransactionItemRecord.TYPE_SALESTAX)) {
//			setAttribute("type", TYPE_SALESTAX);
//			transactionItemType = 3;
//		} else {
//			transactionItemType = 0;
//			setAttribute("type", "none");
//		}
//
//	}
//
//	public void setDescription(String description) {
//		if (description == null)
//			description = "Un-Available!";
//
//		this.description = description;
//		setAttribute(ATTR_DISCRIPTION, description);
//
//	}
//
//	public String getDescription() {
//		return description;
//	}
//
//	public void setQuantity(Double quantity) {
//		if (quantity == null)
//			quantity = 1D;
//		this.quantity = quantity;
//		this.lineTotal = getCalculatedLineTotal();
//		setAttribute(ATTR_QTY, getCalculatedLineTotal());
//	}
//
//	private Double getCalculatedLineTotal() {
//
//		Double lineTotal = 0.0D;
//
//		if (unitPrice == null)
//			unitPrice = 0.0D;
//
//		if (quantity == null)
//			quantity = 1D;
//
//		lineTotal = unitPrice * quantity;
//
//		if (discount != null) {
//			double discountAmt = lineTotal * (discount.doubleValue() / 100);
//			lineTotal = lineTotal - discountAmt;
//		}
//
//		return lineTotal;
//	}
//
//	public Double getQuantity() {
//		return quantity;
//	}
//
//	public void setDiscount(Double discount) {
//		if (discount == null)
//			discount = 0.0D;
//		this.discount = discount;
//		this.lineTotal = getCalculatedLineTotal();
//		setAttribute(ATTR_DISCOUNT, DataUtils.getDiscountString(discount));
//	}
//
//	public Double getDiscount() {
//		return discount;
//	}
//
//	public void setLineTotal(Double lineTotal) throws InvalidEntryException {
//		if (lineTotal == null)
//			lineTotal = 0.0D;
//		this.lineTotal = lineTotal;
//		setAttribute(ATTR_LINETOTAL, DataUtils.getAmountAsString(lineTotal));
//	}
//
//	public Double getLineTotal() {
//		return lineTotal;
//	}
//
//	public void setUnitPrice(Double unitPrice) {
//		if (unitPrice == null)
//			unitPrice = 0.0D;
//		this.unitPrice = unitPrice;
//		this.lineTotal = getCalculatedLineTotal();
//		setAttribute(ATTR_UNITPRICE, DataUtils.getAmountAsString(unitPrice));
//	}
//
//	public Double getUnitPrice() {
//		return unitPrice;
//	}
//
//	public void setTransactionItemType(int transactionItemType) {
//		this.transactionItemType = transactionItemType;
//	}
//
//	public int getTransactionItemType() {
//
//		String type = getAttribute("type");
//
//		if (type.equals("none"))
//			return 0;
//		else
//			return Integer.parseInt(type.equals("") ? "0" : type);
//
//	}
//
//	public void setGrid(ListGrid grid) {
//		this.grid = grid;
//	}
//
//	public ListGrid getGrid() {
//		return grid;
//	}
//
//	public String getItemTax() {
//		return itemTax;
//	}
//
//	public void setTransactionItem(ClientTransactionItem transactionItem) {
//
//		if (transactionItem == null)
//			return;
//
//		try {
//
//			this.transactionItem = transactionItem;
//
//			setType(String.valueOf(transactionItem.getType()));
//
//			switch (transactionItem.getType()) {
//
//			case ClientTransactionItem.TYPE_ACCOUNT:
//
//				if (transactionItem.getAccount() != 0)
//					setAccount(FinanceApplication.getCompany().getAccount(
//							transactionItem.getAccount()));
//
//				break;
//
//			case ClientTransactionItem.TYPE_ITEM:
//
//				if (transactionItem.getItem() != 0)
//					setItem(FinanceApplication.getCompany().getItem(
//							transactionItem.getItem()));
//				if (FinanceApplication.getCompany().getAccountingType() == 0)
//					setDiscount(transactionItem.getDiscount());
//				else {
//					setVat(transactionItem.getVATfraction());
//					setTaxCode(FinanceApplication.getCompany().getTaxCode(
//							transactionItem.getTaxCode()));
//				}
//
//				break;
//
//			case ClientTransactionItem.TYPE_SALESTAX:
//
//				if (transactionItem.getTaxCode() != 0)
//					setTaxCode(FinanceApplication.getCompany().getTaxCode(
//							transactionItem.getTaxCode()));
//
//				break;
//
//			case ClientTransactionItem.TYPE_COMMENT:
//
//				setAttribute("image", "comment");
//
//			default:
//				break;
//
//			}
//
//			setDescription(transactionItem.getDescription());
//
//			setQuantity(transactionItem.getQuantity());
//
//			setUnitPrice(transactionItem.getUnitPrice());
//
//			setLineTotal(transactionItem.getLineTotal());
//
//			if (transactionItem.isTaxable())
//				setItemTax("Taxable");
//			else
//				setItemTax("Non-Taxable");
//
//		} catch (Exception e) {
//			if (e instanceof InvalidEntryException)
//				Accounter.showError(e.getMessage());
//		}
//	}
//
//	public ClientTransactionItem getTransactionItem()
//			throws InvalidEntryException {
//		if (this.transactionItem == null)
//			this.transactionItem = new ClientTransactionItem();
//
//		switch (getTransactionItemType()) {
//
//		case ClientTransactionItem.TYPE_ACCOUNT:
//			if (account != null)
//				transactionItem.setAccount(account.getID());
//			else
//				throw new InvalidEntryException(this, "Account Missing!");
//
//			transactionItem.setQuantity(quantity);
//			transactionItem.setUnitPrice(unitPrice);
//			transactionItem.setLineTotal(lineTotal);
//			transactionItem.setType(ClientTransactionItem.TYPE_ACCOUNT);
//			if (FinanceApplication.getCompany().getAccountingType() == 0) {
//				if (getAttribute(ATTR_ITEMTAX).equals("Taxable"))
//					transactionItem.setTaxable(true);
//				else
//					transactionItem.setTaxable(false);
//			} else {
//				transactionItem.setTaxCode(taxCode.getID());
//				transactionItem.setVATfraction(vat);
//			}
//
//			break;
//
//		case ClientTransactionItem.TYPE_ITEM:
//			if (item != null)
//				transactionItem.setItem(item.getID());
//
//			else
//				throw new InvalidEntryException(this, "Missing Item");
//			transactionItem.setType(ClientTransactionItem.TYPE_ITEM);
//			transactionItem.setQuantity(quantity);
//			transactionItem.setUnitPrice(unitPrice);
//			transactionItem.setLineTotal(lineTotal);
//
//			if (FinanceApplication.getCompany().getAccountingType() == 0) {
//				if (getAttribute(ATTR_ITEMTAX).equals("Taxable"))
//					transactionItem.setTaxable(true);
//				else
//					transactionItem.setTaxable(false);
//
//				if (transactionDomain == CUSTOMER_TRANSACTION)
//					transactionItem.setDiscount(discount);
//			} else {
//				if (transactionDomain == CUSTOMER_TRANSACTION)
//					transactionItem.setDiscount(discount);
//
//				if (transactionDomain == CUSTOMER_TRANSACTION
//						|| transactionDomain == VENDOR_TRANSACTION) {
//					transactionItem.setTaxCode(taxCode.getID());
//					transactionItem.setVATfraction(vat);
//
//				}
//			}
//
//			break;
//
//		case ClientTransactionItem.TYPE_COMMENT:
//			transactionItem.setDescription(description);
//			transactionItem.setType(ClientTransactionItem.TYPE_COMMENT);
//			break;
//
//		case ClientTransactionItem.TYPE_SALESTAX:
//			if (taxCode != null)
//				transactionItem.setTaxCode(taxCode.getID());
//			else
//				throw new InvalidEntryException(this,
//						"Missing tax Code in one of the transaction Items!!");
//
//			transactionItem.setLineTotal(lineTotal);
//
//			transactionItem.setType(ClientTransactionItem.TYPE_SALESTAX);
//
//			break;
//
//		default:
//			break;
//		}
//
//		transactionItem.setDescription(description);
//
//		return transactionItem;
//	}
//
//	public void setAccount(ClientAccount account) {
//		this.account = account;
//		setAttribute(ATTR_NAME, account.getName());
//	}
//
//	public ClientAccount getAccount() {
//		return account;
//	}
//
//	public void setItem(ClientItem item) {
//		this.item = item;
//		setAttribute(ATTR_NAME, item.getName());
//	}
//
//	public ClientItem getItem() {
//		return item;
//	}
//
//	public void setTaxCode(ClientTaxCode taxCode) {
//		this.taxCode = taxCode;
//		setAttribute(ATTR_VATCODE, taxCode.getDisplayName());
//	}
//
//	public ClientTaxCode getTaxCode() {
//		return taxCode;
//	}
//
//	public TransactionItemRecord(int transactionDomain) {
//		super();
//		this.transactionDomain = transactionDomain;
//	}
//
//	public void setTransaction(ClientTransaction transaction) {
//		this.transaction = transaction;
//	}
//
//	public ClientTransaction getTransaction() {
//		return transaction;
//	}
//
//	public void resetValues() {
//		account = null;
//		itemTax = null;
//		item = null;
//		taxCode = null;
//		description = null;
//		quantity = 1D;
//		unitPrice = 0.0D;
//		discount = 0.0D;
//		lineTotal = 0.0D;
//
//		refresh();
//	}
//
//	public void refresh() {
//
//		int type = getTransactionItemType();
//
//		switch (type) {
//
//		case ClientTransactionItem.TYPE_ACCOUNT:
//			setAttribute(ATTR_NAME, account != null ? account.getName() : "");
//			setAttribute("image", "account");
//			setAttribute(ATTR_QTY, String.valueOf(quantity != null ? quantity
//					.intValue() : "1"));
//			setAttribute(ATTR_UNITPRICE, DataUtils
//					.getAmountAsString(getUnitPrice()));
//			setAttribute(ATTR_LINETOTAL, DataUtils
//					.getAmountAsString(getLineTotal()));
//			if (transactionDomain == CUSTOMER_TRANSACTION) {
//				setAttribute(ATTR_DISCOUNT, "");
//				setAttribute(ATTR_ITEMTAX, itemTax != null ? itemTax : "");
//			}
//			if (FinanceApplication.getCompany().getAccountingType() == 1) {
//				if (transactionDomain == CUSTOMER_TRANSACTION
//						|| transactionDomain == VENDOR_TRANSACTION) {
//					setAttribute(ATTR_VATCODE, taxCode != null ? taxCode
//							.getDisplayName() : "");
//				}
//			}
//			break;
//
//		case ClientTransactionItem.TYPE_ITEM:
//			setAttribute(ATTR_NAME, item != null ? item.getName() : "");
//			setAttribute("image", "customers/new_item");
//			setAttribute(ATTR_QTY, String.valueOf(quantity != null ? quantity
//					.intValue() : "1"));
//			setAttribute(ATTR_UNITPRICE, DataUtils
//					.getAmountAsString(getUnitPrice()));
//			setAttribute(ATTR_LINETOTAL, DataUtils
//					.getAmountAsString(getLineTotal()));
//
//			if (FinanceApplication.getCompany().getAccountingType() == 1) {
//				if (transactionDomain == CUSTOMER_TRANSACTION
//						|| transactionDomain == VENDOR_TRANSACTION) {
//					setAttribute(ATTR_VATCODE, taxCode != null ? taxCode
//							.getDisplayName() : "");
//				}
//			} else {
//				if (transactionDomain == CUSTOMER_TRANSACTION) {
//					setAttribute(ATTR_DISCOUNT, DataUtils
//							.getDiscountString(discount));
//					if (item != null)
//						setAttribute(ATTR_ITEMTAX, itemTax != null ? itemTax
//								: "Non-Taxable");
//				}
//			}
//
//			break;
//
//		case ClientTransactionItem.TYPE_SALESTAX:
//			setAttribute(ATTR_NAME, taxCode != null ? taxCode.getName() : "");
//			setAttribute("image", "salestax");
//			setAttribute(ATTR_QTY, "");
//			setAttribute(ATTR_UNITPRICE, "");
//			setAttribute(ATTR_LINETOTAL, DataUtils
//					.getAmountAsString(getLineTotal()));
//			if (transactionDomain == CUSTOMER_TRANSACTION) {
//				setAttribute(ATTR_DISCOUNT, "");
//			}
//
//			break;
//
//		case ClientTransactionItem.TYPE_COMMENT:
//			setAttribute(ATTR_NAME, "COMMENT");
//			setAttribute("image", "comment");
//			setAttribute(ATTR_QTY, "");
//			setAttribute(ATTR_UNITPRICE, "");
//			setAttribute(ATTR_LINETOTAL, "");
//
//			break;
//
//		default:
//			return;
//
//		}
//
//		setAttribute(ATTR_DISCRIPTION,
//				getDescription() != null ? getDescription() : "");
//
//		if (grid != null) {
//
//			grid.refreshRow(grid.getRecordIndex(this));
//		}
//
//	}
//
//	public boolean isValid() {
//
//		String type = getAttribute("type");
//
//		if (type.equals(TYPE_ACCOUNT))
//			return account != null;
//
//		if (type.equals(TYPE_ITEM)) 
//			return item != null;
//
//		if (type.equals(TYPE_SALESTAX))
//			return taxCode != null;
//
//		return true;
//	}
//
//	public void setItemTax(String selectItem) {
//
//		if (selectItem == null)
//			selectItem = "Taxable";
//
//		this.itemTax = selectItem;
//		setAttribute(ATTR_ITEMTAX, selectItem);
//
//	}
//
//	public ClientTaxGroup getTaxGroup() {
//		return taxGroup;
//	}
//
//	public void setTaxGroup(ClientTaxGroup taxGroup) {
//		this.taxGroup = taxGroup;
//	}
//
//	public Double getVat() {
//		return vat;
//	}
//
//	public void setVat(Double vat) {
//		this.vat = vat;
//		setAttribute(ATTR_VAT, DataUtils.getAmountAsString(vat));
//	}
//}
///**
// * 
// */
//package com.vimukti.accounter.web.client.ui.core;
//
//import com.vimukti.accounter.web.client.core.ClientAccount;
//import com.vimukti.accounter.web.client.core.ClientItem;
//import com.vimukti.accounter.web.client.core.ClientTaxAgency;
//import com.vimukti.accounter.web.client.core.ClientTaxCode;
//import com.vimukti.accounter.web.client.core.ClientTaxGroup;
//import com.vimukti.accounter.web.client.core.ClientTransaction;
//import com.vimukti.accounter.web.client.core.ClientTransactionItem;
//import com.vimukti.accounter.web.client.ui.DataUtils;
//import com.vimukti.accounter.web.client.ui.FinanceApplication;
//import com.vimukti.accounter.web.client.ui.widgets.ListGrid;
//
///**
// * @author Fernandez
// * 
// */
//public class TransactionItemRecord  {
//
//	static final int CUSTOMER_TRANSACTION = 1;
//
//	static final int VENDOR_TRANSACTION = 2;
//
//	static final int BANKING_TRANSACTION = 3;
//
//	public static final String ATTR_NAME = "name";
//
//	public static final String ATTR_DISCRIPTION = "description";
//
//	public static final String ATTR_QTY = "qty";
//
//	public static final String ATTR_UNITPRICE = "unitPrice";
//
//	public static final String ATTR_DISCOUNT = "discount";
//
//	public static final String ATTR_LINETOTAL = "lineTotal";
//
//	public static final String ATTR_ITEMTAX = "itemTax";
//
//	public static final String ATTR_VATCODE = "vatCode";
//
//	public static final String ATTR_VAT = "vat";
//
//	public static final String TYPE_ACCOUNT = "4";
//
//	public static final String TYPE_ITEM = "1";
//
//	public static final String TYPE_SALESTAX = "3";
//
//	public static final String TYPE_COMMENT = "2";
//
//	private ClientTransaction transaction;
//
//	int transactionItemType;
//
//	int transactionDomain;
//
//	String description;
//
//	Double vat;
//
//	Double quantity = 1D;
//
//	Double unitPrice = 0.0D;
//
//	Double discount = 0.0D;
//
//	Double lineTotal = 0.0D;
//
//	ClientAccount account;
//
//	ClientItem item;
//
//	ClientTaxCode taxCode;
//
//	String itemTax;
//
//	ClientTaxGroup taxGroup;
//
//	ClientTransactionItem transactionItem;
//
//	ListGrid grid;
//
//	ClientTaxAgency agency;
//
//	public void setType(String type) {
//
//		if (type == null)
//			type = "none";
//
//		if (type.equals(TransactionItemRecord.TYPE_ACCOUNT)) {
//			setAttribute("type", TYPE_ACCOUNT);
//			transactionItemType = 4;
//		} else if (type.equals(TransactionItemRecord.TYPE_COMMENT)) {
//			setAttribute("type", TYPE_COMMENT);
//			transactionItemType = 2;
//		} else if (type.equals(TransactionItemRecord.TYPE_ITEM)) {
//			setAttribute("type", TYPE_ITEM);
//			transactionItemType = 1;
//		} else if (type.equals(TransactionItemRecord.TYPE_SALESTAX)) {
//			setAttribute("type", TYPE_SALESTAX);
//			transactionItemType = 3;
//		} else {
//			transactionItemType = 0;
//			setAttribute("type", "none");
//		}
//
//	}
//
//	public void setDescription(String description) {
//		if (description == null)
//			description = "Un-Available!";
//
//		this.description = description;
//		setAttribute(ATTR_DISCRIPTION, description);
//
//	}
//
//	public String getDescription() {
//		return description;
//	}
//
//	public void setQuantity(Double quantity) {
//		if (quantity == null)
//			quantity = 1D;
//		this.quantity = quantity;
//		this.lineTotal = getCalculatedLineTotal();
//		setAttribute(ATTR_QTY, getCalculatedLineTotal());
//	}
//
//	private Double getCalculatedLineTotal() {
//
//		Double lineTotal = 0.0D;
//
//		if (unitPrice == null)
//			unitPrice = 0.0D;
//
//		if (quantity == null)
//			quantity = 1D;
//
//		lineTotal = unitPrice * quantity;
//
//		if (discount != null) {
//			double discountAmt = lineTotal * (discount.doubleValue() / 100);
//			lineTotal = lineTotal - discountAmt;
//		}
//
//		return lineTotal;
//	}
//
//	public Double getQuantity() {
//		return quantity;
//	}
//
//	public void setDiscount(Double discount) {
//		if (discount == null)
//			discount = 0.0D;
//		this.discount = discount;
//		this.lineTotal = getCalculatedLineTotal();
//		setAttribute(ATTR_DISCOUNT, DataUtils.getDiscountString(discount));
//	}
//
//	public Double getDiscount() {
//		return discount;
//	}
//
//	public void setLineTotal(Double lineTotal) throws InvalidEntryException {
//		if (lineTotal == null)
//			lineTotal = 0.0D;
//		this.lineTotal = lineTotal;
//		setAttribute(ATTR_LINETOTAL, DataUtils.getAmountAsString(lineTotal));
//	}
//
//	public Double getLineTotal() {
//		return lineTotal;
//	}
//
//	public void setUnitPrice(Double unitPrice) {
//		if (unitPrice == null)
//			unitPrice = 0.0D;
//		this.unitPrice = unitPrice;
//		this.lineTotal = getCalculatedLineTotal();
//		setAttribute(ATTR_UNITPRICE, DataUtils.getAmountAsString(unitPrice));
//	}
//
//	public Double getUnitPrice() {
//		return unitPrice;
//	}
//
//	public void setTransactionItemType(int transactionItemType) {
//		this.transactionItemType = transactionItemType;
//	}
//
//	public int getTransactionItemType() {
//
//		String type = getAttribute("type");
//
//		if (type.equals("none"))
//			return 0;
//		else
//			return Integer.parseInt(type.equals("") ? "0" : type);
//
//	}
//
//	public void setGrid(ListGrid grid) {
//		this.grid = grid;
//	}
//
//	public ListGrid getGrid() {
//		return grid;
//	}
//
//	public String getItemTax() {
//		return itemTax;
//	}
//
//	public void setTransactionItem(ClientTransactionItem transactionItem) {
//
//		if (transactionItem == null)
//			return;
//
//		try {
//
//			this.transactionItem = transactionItem;
//
//			setType(String.valueOf(transactionItem.getType()));
//
//			switch (transactionItem.getType()) {
//
//			case ClientTransactionItem.TYPE_ACCOUNT:
//
//				if (transactionItem.getAccount() != 0)
//					setAccount(FinanceApplication.getCompany().getAccount(
//							transactionItem.getAccount()));
//
//				break;
//
//			case ClientTransactionItem.TYPE_ITEM:
//
//				if (transactionItem.getItem() != 0)
//					setItem(FinanceApplication.getCompany().getItem(
//							transactionItem.getItem()));
//				if (FinanceApplication.getCompany().getAccountingType() == 0)
//					setDiscount(transactionItem.getDiscount());
//				else {
//					setVat(transactionItem.getVATfraction());
//					setTaxCode(FinanceApplication.getCompany().getTaxCode(
//							transactionItem.getTaxCode()));
//				}
//
//				break;
//
//			case ClientTransactionItem.TYPE_SALESTAX:
//
//				if (transactionItem.getTaxCode() != 0)
//					setTaxCode(FinanceApplication.getCompany().getTaxCode(
//							transactionItem.getTaxCode()));
//
//				break;
//
//			case ClientTransactionItem.TYPE_COMMENT:
//
//				setAttribute("image", "comment");
//
//			default:
//				break;
//
//			}
//
//			setDescription(transactionItem.getDescription());
//
//			setQuantity(transactionItem.getQuantity());
//
//			setUnitPrice(transactionItem.getUnitPrice());
//
//			setLineTotal(transactionItem.getLineTotal());
//
//			if (transactionItem.isTaxable())
//				setItemTax("Taxable");
//			else
//				setItemTax("Non-Taxable");
//
//		} catch (Exception e) {
//			if (e instanceof InvalidEntryException)
//				Accounter.showError(e.getMessage());
//		}
//	}
//
//	public ClientTransactionItem getTransactionItem()
//			throws InvalidEntryException {
//		if (this.transactionItem == null)
//			this.transactionItem = new ClientTransactionItem();
//
//		switch (getTransactionItemType()) {
//
//		case ClientTransactionItem.TYPE_ACCOUNT:
//			if (account != null)
//				transactionItem.setAccount(account.getID());
//			else
//				throw new InvalidEntryException(this, "Account Missing!");
//
//			transactionItem.setQuantity(quantity);
//			transactionItem.setUnitPrice(unitPrice);
//			transactionItem.setLineTotal(lineTotal);
//			transactionItem.setType(ClientTransactionItem.TYPE_ACCOUNT);
//			if (FinanceApplication.getCompany().getAccountingType() == 0) {
//				if (getAttribute(ATTR_ITEMTAX).equals("Taxable"))
//					transactionItem.setTaxable(true);
//				else
//					transactionItem.setTaxable(false);
//			} else {
//				transactionItem.setTaxCode(taxCode.getID());
//				transactionItem.setVATfraction(vat);
//			}
//
//			break;
//
//		case ClientTransactionItem.TYPE_ITEM:
//			if (item != null)
//				transactionItem.setItem(item.getID());
//
//			else
//				throw new InvalidEntryException(this, "Missing Item");
//			transactionItem.setType(ClientTransactionItem.TYPE_ITEM);
//			transactionItem.setQuantity(quantity);
//			transactionItem.setUnitPrice(unitPrice);
//			transactionItem.setLineTotal(lineTotal);
//
//			if (FinanceApplication.getCompany().getAccountingType() == 0) {
//				if (getAttribute(ATTR_ITEMTAX).equals("Taxable"))
//					transactionItem.setTaxable(true);
//				else
//					transactionItem.setTaxable(false);
//
//				if (transactionDomain == CUSTOMER_TRANSACTION)
//					transactionItem.setDiscount(discount);
//			} else {
//				if (transactionDomain == CUSTOMER_TRANSACTION)
//					transactionItem.setDiscount(discount);
//
//				if (transactionDomain == CUSTOMER_TRANSACTION
//						|| transactionDomain == VENDOR_TRANSACTION) {
//					transactionItem.setTaxCode(taxCode.getID());
//					transactionItem.setVATfraction(vat);
//
//				}
//			}
//
//			break;
//
//		case ClientTransactionItem.TYPE_COMMENT:
//			transactionItem.setDescription(description);
//			transactionItem.setType(ClientTransactionItem.TYPE_COMMENT);
//			break;
//
//		case ClientTransactionItem.TYPE_SALESTAX:
//			if (taxCode != null)
//				transactionItem.setTaxCode(taxCode.getID());
//			else
//				throw new InvalidEntryException(this,
//						"Missing tax Code in one of the transaction Items!!");
//
//			transactionItem.setLineTotal(lineTotal);
//
//			transactionItem.setType(ClientTransactionItem.TYPE_SALESTAX);
//
//			break;
//
//		default:
//			break;
//		}
//
//		transactionItem.setDescription(description);
//
//		return transactionItem;
//	}
//
//	public void setAccount(ClientAccount account) {
//		this.account = account;
//		setAttribute(ATTR_NAME, account.getName());
//	}
//
//	public ClientAccount getAccount() {
//		return account;
//	}
//
//	public void setItem(ClientItem item) {
//		this.item = item;
//		setAttribute(ATTR_NAME, item.getName());
//	}
//
//	public ClientItem getItem() {
//		return item;
//	}
//
//	public void setTaxCode(ClientTaxCode taxCode) {
//		this.taxCode = taxCode;
//		setAttribute(ATTR_VATCODE, taxCode.getDisplayName());
//	}
//
//	public ClientTaxCode getTaxCode() {
//		return taxCode;
//	}
//
//	public TransactionItemRecord(int transactionDomain) {
//		super();
//		this.transactionDomain = transactionDomain;
//	}
//
//	public void setTransaction(ClientTransaction transaction) {
//		this.transaction = transaction;
//	}
//
//	public ClientTransaction getTransaction() {
//		return transaction;
//	}
//
//	public void resetValues() {
//		account = null;
//		itemTax = null;
//		item = null;
//		taxCode = null;
//		description = null;
//		quantity = 1D;
//		unitPrice = 0.0D;
//		discount = 0.0D;
//		lineTotal = 0.0D;
//
//		refresh();
//	}
//
//	public void refresh() {
//
//		int type = getTransactionItemType();
//
//		switch (type) {
//
//		case ClientTransactionItem.TYPE_ACCOUNT:
//			setAttribute(ATTR_NAME, account != null ? account.getName() : "");
//			setAttribute("image", "account");
//			setAttribute(ATTR_QTY, String.valueOf(quantity != null ? quantity
//					.intValue() : "1"));
//			setAttribute(ATTR_UNITPRICE, DataUtils
//					.getAmountAsString(getUnitPrice()));
//			setAttribute(ATTR_LINETOTAL, DataUtils
//					.getAmountAsString(getLineTotal()));
//			if (transactionDomain == CUSTOMER_TRANSACTION) {
//				setAttribute(ATTR_DISCOUNT, "");
//				setAttribute(ATTR_ITEMTAX, itemTax != null ? itemTax : "");
//			}
//			if (FinanceApplication.getCompany().getAccountingType() == 1) {
//				if (transactionDomain == CUSTOMER_TRANSACTION
//						|| transactionDomain == VENDOR_TRANSACTION) {
//					setAttribute(ATTR_VATCODE, taxCode != null ? taxCode
//							.getDisplayName() : "");
//				}
//			}
//			break;
//
//		case ClientTransactionItem.TYPE_ITEM:
//			setAttribute(ATTR_NAME, item != null ? item.getName() : "");
//			setAttribute("image", "customers/new_item");
//			setAttribute(ATTR_QTY, String.valueOf(quantity != null ? quantity
//					.intValue() : "1"));
//			setAttribute(ATTR_UNITPRICE, DataUtils
//					.getAmountAsString(getUnitPrice()));
//			setAttribute(ATTR_LINETOTAL, DataUtils
//					.getAmountAsString(getLineTotal()));
//
//			if (FinanceApplication.getCompany().getAccountingType() == 1) {
//				if (transactionDomain == CUSTOMER_TRANSACTION
//						|| transactionDomain == VENDOR_TRANSACTION) {
//					setAttribute(ATTR_VATCODE, taxCode != null ? taxCode
//							.getDisplayName() : "");
//				}
//			} else {
//				if (transactionDomain == CUSTOMER_TRANSACTION) {
//					setAttribute(ATTR_DISCOUNT, DataUtils
//							.getDiscountString(discount));
//					if (item != null)
//						setAttribute(ATTR_ITEMTAX, itemTax != null ? itemTax
//								: "Non-Taxable");
//				}
//			}
//
//			break;
//
//		case ClientTransactionItem.TYPE_SALESTAX:
//			setAttribute(ATTR_NAME, taxCode != null ? taxCode.getName() : "");
//			setAttribute("image", "salestax");
//			setAttribute(ATTR_QTY, "");
//			setAttribute(ATTR_UNITPRICE, "");
//			setAttribute(ATTR_LINETOTAL, DataUtils
//					.getAmountAsString(getLineTotal()));
//			if (transactionDomain == CUSTOMER_TRANSACTION) {
//				setAttribute(ATTR_DISCOUNT, "");
//			}
//
//			break;
//
//		case ClientTransactionItem.TYPE_COMMENT:
//			setAttribute(ATTR_NAME, "COMMENT");
//			setAttribute("image", "comment");
//			setAttribute(ATTR_QTY, "");
//			setAttribute(ATTR_UNITPRICE, "");
//			setAttribute(ATTR_LINETOTAL, "");
//
//			break;
//
//		default:
//			return;
//
//		}
//
//		setAttribute(ATTR_DISCRIPTION,
//				getDescription() != null ? getDescription() : "");
//
//		if (grid != null) {
//
//			grid.refreshRow(grid.getRecordIndex(this));
//		}
//
//	}
//
//	public boolean isValid() {
//
//		String type = getAttribute("type");
//
//		if (type.equals(TYPE_ACCOUNT))
//			return account != null;
//
//		if (type.equals(TYPE_ITEM)) 
//			return item != null;
//
//		if (type.equals(TYPE_SALESTAX))
//			return taxCode != null;
//
//		return true;
//	}
//
//	public void setItemTax(String selectItem) {
//
//		if (selectItem == null)
//			selectItem = "Taxable";
//
//		this.itemTax = selectItem;
//		setAttribute(ATTR_ITEMTAX, selectItem);
//
//	}
//
//	public ClientTaxGroup getTaxGroup() {
//		return taxGroup;
//	}
//
//	public void setTaxGroup(ClientTaxGroup taxGroup) {
//		this.taxGroup = taxGroup;
//	}
//
//	public Double getVat() {
//		return vat;
//	}
//
//	public void setVat(Double vat) {
//		this.vat = vat;
//		setAttribute(ATTR_VAT, DataUtils.getAmountAsString(vat));
//	}
// }
