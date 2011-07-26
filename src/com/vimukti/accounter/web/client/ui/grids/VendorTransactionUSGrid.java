package com.vimukti.accounter.web.client.ui.grids;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.ProductCombo;
import com.vimukti.accounter.web.client.ui.combo.PurchaseAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.ServiceCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.combo.VATItemCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;

public class VendorTransactionUSGrid extends
		AbstractTransactionGrid<ClientTransactionItem> {

	private PurchaseAccountsCombo accountsCombo;
	// ItemCombo itemCombo;
	ServiceCombo serviceItemCombo;
	ProductCombo productItemCombo;
	TAXCodeCombo taxCodeCombo;
	VATItemCombo vatItemCombo;

	private Double totallinetotal = 0.0;
	private Double totalVat = 0.0;
	private Double grandTotal;
	private ClientPriceLevel priceLevel;
	private double taxableTotal;
	private int accountingType;
	private boolean isBankingTransaction = false;
	protected boolean isPurchseOrderTransaction;
	private boolean isAddNewRequired = true;
	private long ztaxCodeid;
	protected int maxDecimalPoint;

	public VendorTransactionUSGrid() {
		super(false, true);
		this.accountingType = getCompany().getAccountingType();
	}

	private ClientCompany getCompany() {
		return Accounter.getCompany();
	}

	public VendorTransactionUSGrid(boolean isAddNewRequired) {
		super(false, true);
		this.isAddNewRequired = isAddNewRequired;
		this.accountingType = getCompany().getAccountingType();
	}

	@Override
	public void init() {
		super.isEnable = false;
		super.init();
		if (getCompany().getAccountingType() != ClientCompany.ACCOUNTING_TYPE_UK) {
			createControls();
			initTransactionData();

			ClientTransaction transactionObject = transactionView
					.getTransactionObject();

			if (transactionObject != null) {
				setAllTransactions(transactionObject.getTransactionItems());
				if (transactionObject.getID() != 0) {
					// ITS Edit Mode
					// setShowMenu(false);
					// isEdit = true;
					canDeleteRecord(false);
					// canAddRecord(false);
					// setEditDisableCells(new int[] { 0, 1, 2, 3, 4 });
				}
			}
		}
	}

	protected void initTransactionData() {

		List<ClientItem> items = getCompany().getActiveItems();
		List<ClientItem> vendorItems = new ArrayList<ClientItem>();
		for (ClientItem item : items) {
			if (item.isIBuyThisItem())
				vendorItems.add(item);
		}
		List<ClientItem> serviceitems = new ArrayList<ClientItem>();
		List<ClientItem> productitems = new ArrayList<ClientItem>();
		for (ClientItem item : vendorItems) {
			if (item.getType() == ClientItem.TYPE_SERVICE)
				serviceitems.add(item);
			else
				productitems.add(item);
		}
		serviceItemCombo.initCombo(serviceitems);

		productItemCombo.initCombo(productitems);
		// itemCombo.initCombo(vendorItems);
	}

	@Override
	public List<ClientTransactionItem> getallTransactions(
			ClientTransaction object) throws InvalidEntryException {
		return getRecords();
	}

	protected void createControls() {
		setSize("100%", "200px");

		// Passing 1 for Customer, 2 For Vendor For Item View- Raj Vimal
		serviceItemCombo = new ServiceCombo(Accounter.getCustomersMessages()
				.item(), 2, isAddNewRequired);
		serviceItemCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientItem>() {

					@Override
					public void selectedComboBoxItem(ClientItem selectItem) {
						if (selectItem != null) {
							selectedObject.setItem(selectItem.getID());
							selectedObject.setUnitPrice(selectItem
									.getPurchasePrice());
							if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK)
								selectedObject.setTaxable(selectItem
										.isTaxable());
							else
								selectedObject.setTaxable(false);
							setText(currentRow, currentCol,
									selectItem.getName());
							if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
								selectedObject.setTaxCode(selectItem
										.getTaxCode() != 0 ? selectItem
										.getTaxCode() : 0);
							}
							editComplete(selectedObject,
									selectItem.getPurchasePrice(), 4);

							// it should be here only for vat calculations(it
							// needs line total for this,linetotal calculated in
							// editcomplete()
							if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK
									&& !isPurchseOrderTransaction) {
								refreshVatValue(selectedObject);
							}
						}
					}
				});
		serviceItemCombo.setGrid(this);

		productItemCombo = new ProductCombo(Accounter.getVendorsMessages()
				.product(), 2, isAddNewRequired);
		productItemCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientItem>() {

					@Override
					public void selectedComboBoxItem(ClientItem selectItem) {
						if (selectItem != null) {
							selectedObject.setItem(selectItem.getID());
							selectedObject.setUnitPrice(selectItem
									.getPurchasePrice());
							if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK)
								selectedObject.setTaxable(selectItem
										.isTaxable());
							else
								selectedObject.setTaxable(false);
							setText(currentRow, currentCol,
									selectItem.getName());
							if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
								selectedObject.setTaxCode(selectItem
										.getTaxCode() != 0 ? selectItem
										.getTaxCode() : 0);
							}
							editComplete(selectedObject,
									selectItem.getPurchasePrice(), 4);

							// it should be here only for vat calculations(it
							// needs line total for this,linetotal calculated in
							// editcomplete()
							if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK
									&& !isPurchseOrderTransaction) {
								refreshVatValue(selectedObject);
							}
						}
					}
				});
		productItemCombo.setGrid(this);

		accountsCombo = new PurchaseAccountsCombo(Accounter
				.getVendorsMessages().Accounts(), isAddNewRequired);
		accountsCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectedObject.setAccount(selectItem.getID());
						if (getCompany().getAccountingType() == 1)
							selectedObject.setTaxable(true);
						setText(currentRow, currentCol, selectItem.getName());
						if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
							if (getCompany().getpreferences()
									.getDoYouPaySalesTax())
								setVendorTaxCode(selectedObject);

						}
					}
				});
		accountsCombo.setGrid(this);
		// accountsCombo.setWidth("600");
		// if (!isBankingTransaction && !isPurchseOrderTransaction)
		// this.addFooterValues(new String[] { "", "", "", "",
		// FinanceApplication.getVendorsMessages().total(),
		// DataUtils.getAmountAsString(0.00), });
		// else if (isPurchseOrderTransaction) {
		//
		// this.addFooterValues(new String[] { "", "", "", "", "", "",
		// FinanceApplication.getVATMessages().totalcolan(), "" });
		// } else {
		// this.addFooterValues(new String[] { "", "", "",
		// FinanceApplication.getVATMessages().totalcolan(), "" });
		// }
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			if (!isBankingTransaction && !isPurchseOrderTransaction) {
				// this.addFooterValue("VAT", 6);
				// this.addFooterValue(DataUtils.getAmountAsString(0.00), 7);
			}
		}

		addRecordClickHandler(new RecordClickHandler<ClientTransactionItem>() {

			@Override
			public boolean onRecordClick(ClientTransactionItem core, int column) {

				switch (column) {
				case 1:
					if (core.getType() == ClientTransactionItem.TYPE_ACCOUNT) {
						if (core.getAccount() != 0)
							accountsCombo.setComboItem(Accounter.getCompany()
									.getAccount(core.getAccount()));
						else
							accountsCombo.setValue("");
					} else if (core.getType() == ClientTransactionItem.TYPE_ITEM) {
						if (core.getItem() != 0)
							productItemCombo.setComboItem(Accounter
									.getCompany().getItem(core.getItem()));
						else
							productItemCombo.setValue("");
					} else if (core.getType() == ClientTransactionItem.TYPE_SERVICE) {
						if (core.getItem() != 0)
							serviceItemCombo.setComboItem(Accounter
									.getCompany().getItem(core.getItem()));
						else
							serviceItemCombo.setValue("");
					} else if (core.getType() == ClientTransactionItem.TYPE_SALESTAX) {
						if (core.getTaxCode() != 0)
							vatItemCombo.setComboItem(Accounter.getCompany()
									.getTaxItem(core.getVatItem()));
						else
							vatItemCombo.setValue("");

					}
					break;
				default:
					break;
				}
				// in US-version,column '6' is delete,so if user doubleclick on
				// it deleteshould be done,for avoiding columnindex collisions
				// of 6th column in uk,the code for VATCodeCombo
				// is written here
				if (column == getColumnsCount() - 1) {
					if (!transactionView.isEdit()) {
						deleteRecord(core);
						if (!isPurchseOrderTransaction)
							updateTotals();
					}
					return true;
				}

				if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK
						&& column == 6) {
					taxCodeCombo.setComboItem(getCompany().getTAXCode(
							core.getTaxCode()));
				}
				return true;
			}
		});
	}

	protected void setUnitPriceForSelectedItem(ClientItem selectedItem) {

		ClientTransactionItem record = (ClientTransactionItem) getSelection();

		if (record == null)
			return;

		Double calculatedUnitPrice = Utility
				.getCalculatedItemUnitPriceForPriceLevel(selectedItem,
						priceLevel, true);

		if (calculatedUnitPrice == null)
			return;

		int index = getSelectedRecordIndex();

		int col = 4;
		record.setItem(selectedItem.getID());

		record.setUnitPrice(calculatedUnitPrice);

		record.setDescription(selectedItem.getName());

		stopEditing(col);

		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK
				&& !isPurchseOrderTransaction && selectedItem.getTaxCode() != 0) {
			record.setTaxCode(getCompany()
					.getTAXCode(selectedItem.getTaxCode()).getID());
			refreshVatValue(record);
		} else {
			record.setTaxable(selectedItem.isTaxable());

			// if (selectedItem.isTaxable())
			// record.setItemTax("Taxable");
			// else
			// record.setItemTax("Non-Taxable");

		}

		startEditing(index);

	}

	@Override
	public void updateRecord(ClientTransactionItem obj, int row, int col) {
		super.updateRecord(obj, row, col);
	}

	public void setVendorTaxCode(ClientTransactionItem selectedObject) {
		List<ClientTAXCode> taxCodes = getCompany().getActiveTaxCodes();
		for (ClientTAXCode taxCode : taxCodes) {
			if (taxCode.getName().equals("S")) {
				ztaxCodeid = taxCode.getID();
			}
		}
		if (transactionView.getTransactionObject() == null
				&& transactionView.vendor != null)
			selectedObject
					.setTaxCode(selectedObject.getTaxCode() != 0 ? selectedObject
							.getTaxCode()
							: transactionView.vendor.getTAXCode() != 0 ? transactionView.vendor
									.getTAXCode() : ztaxCodeid);

		updateTotals();
		updateData(selectedObject);
	}

	@Override
	public void updateData(ClientTransactionItem obj) {
		super.updateData(obj);
	}

	public void updateTotals() {

		List<ClientTransactionItem> allrecords = getRecords();
		int totaldiscount = 0;
		totallinetotal = 0.0;
		taxableTotal = 0.0;
		totalVat = 0.0;
		for (ClientTransactionItem rec : allrecords) {

			int type = rec.getType();

			if (type == 0)
				continue;

			totaldiscount += rec.getDiscount();

			Double lineTotalAmt = rec.getLineTotal();
			totallinetotal += lineTotalAmt;

			ClientItem item = getCompany().getItem(rec.getItem());
			if (item != null && item.isTaxable()) {
				taxableTotal += lineTotalAmt;
			}
			totalVat += rec.getVATfraction();
		}
		// if (isPurchseOrderTransaction) {
		// this.addFooterValue(DataUtils.getAmountAsString(totallinetotal), 7);
		// } else {
		// this.addFooterValue(DataUtils.getAmountAsString(totallinetotal), 5);
		// }

		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			grandTotal = totalVat + totallinetotal;
		else {
			// if (transactionView.vatinclusiveCheck != null
			// && (Boolean) transactionView.vatinclusiveCheck.getValue()) {
			// grandTotal = totallinetotal - totalVat;
			//
			// } else {
			grandTotal = totallinetotal;
			totallinetotal = grandTotal + totalVat;
			// }
		}

		transactionView.updateNonEditableItems();

		// if (FinanceApplication.getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_UK
		// && !isPurchseOrderTransaction) {
		// this.addFooterValue(DataUtils.getAmountAsString(totalVat), 7);
		// }

	}

	// public List<ClientTransactionItem> getallTransactions(
	// ClientTransaction object) throws InvalidEntryException {
	//
	// return getRecords();
	// }

	@Override
	public void setAllTransactions(List<ClientTransactionItem> transactionItems) {
		// removeAllRecords();
		for (int i = 0; i < transactionItems.size(); i++) {
			CustomCombo<?> combo = null;
			combo = getCustomCombo(transactionItems.get(i), 1);
			/* If the record type is COMMENT,then combo value is null */
			if (combo != null) {
				String itemName = "";
				if (combo instanceof TAXCodeCombo) {
					itemName = getTAXCodeName(transactionItems.get(i)
							.getTaxCode());
				} else {
					itemName = getNameValue(transactionItems.get(i));
				}
				combo.setSelected(itemName);
				if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
					combo = getCustomCombo(transactionItems.get(i), 6);
					if (combo != null) {
						String taxCodeName = "";
						if (combo instanceof TAXCodeCombo) {
							taxCodeName = getTAXCodeName(transactionItems
									.get(i).getTaxCode());
						} else {
							taxCodeName = getNameValue(transactionItems.get(i));
						}
						combo.setSelected(taxCodeName);
					}
				}
			}
		}
		if (!(this instanceof PurchaseOrderUKGrid || this instanceof PurchaseOrderUSGrid)) {
			for (ClientTransactionItem item : transactionItems) {
				item.setID(0);
			}
		}
		addRecords(transactionItems);
		updateTotals();
		if (isItemRecieptView) {
			refreshVatValue();
		}

	}

	protected String getTAXCodeName(long taxCode) {
		ClientTAXCode t = null;
		if (taxCode != 0)
			t = getCompany().getTAXCode(taxCode);
		return t != null ? t.getName() : "";
	}

	@Override
	public Double getTotal() {
		return totallinetotal != null ? totallinetotal.doubleValue() : 0.0d;
	}

	public void priceLevelSelected(ClientPriceLevel priceLevel) {

		this.priceLevel = priceLevel;
		updateTotals();
	}

	public Double getTaxableLineTotal() {
		return this.taxableTotal;

	}

	public Double getVatTotal() {
		return this.totalVat;
	}

	protected void transactionItemRecordDeleted() {
		updateTotals();
	}

	public void resetGridEditEvent() {

	}

	@Override
	public Double getGrandTotal() {
		return grandTotal;
	}

	public void refreshVatValue() {
		List<ClientTransactionItem> allrecords = getRecords();
		for (ClientTransactionItem record : allrecords) {
			if (record.getTaxCode() != 0)
				refreshVatValue(record);
		}
		updateTotals();
	}

	@Override
	public void refreshVatValue(ClientTransactionItem record) {

		if (!(record instanceof ClientTransactionItem))
			return;

		if (record.getTaxCode() == 0)
			return;

		record.setVATfraction(getVATAmount(record.getTaxCode(), record));
		updateTotals();
		updateData(record);
	}

	public double getVATAmount(long TAXCodeID, ClientTransactionItem record) {

		double vatRate = 0.0;
		try {
			if (TAXCodeID != 0) {
				// Checking the selected object is VATItem or VATGroup.
				// If it is VATItem,the we should get 'VATRate',otherwise
				// 'GroupRate
				vatRate = UIUtils.getVATItemRate(Accounter.getCompany()
						.getTAXCode(TAXCodeID), false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Double vat = 0.0;
		if (transactionView.isShowPriceWithVat()) {
			vat = ((ClientTransactionItem) record).getLineTotal()
					- (100 * (((ClientTransactionItem) record).getLineTotal() / (100 + vatRate)));
		} else {
			vat = ((ClientTransactionItem) record).getLineTotal() * vatRate
					/ 100;
		}
		vat = UIUtils.getRoundValue(vat);
		return vat.doubleValue();
	}

	// @Override
	protected void onValueChange(ClientTransactionItem item, int index,
			Object value) {
		if (index == 1) {
			switch (item.getType()) {
			case TYPE_ITEM:
				setUnitPriceForSelectedItem((ClientItem) value);
				break;
			case TYPE_ACCOUNT:
				if (item != null) {
					item.setTaxable(false);
				}
			default:
				break;
			}
		}
		super.onValueChange(item, index, value);
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 6 || index == 0)
			if (UIUtils.isMSIEBrowser())
				return 25;
			else
				return 15;
		// else if (index == 2)
		// return 150;
		else if (index == 4 || index == 5)
			return 100;
		else if (index == 3)
			return 80;
		return -1;
	}

	@Override
	protected int getColumnType(int col) {
		// switch (accountingType) {
		// case 0:
		if (col == 0 || col == 6)
			return ListGrid.COLUMN_TYPE_IMAGE;
		else
			return getColumnTypeforUS(col);
		// case 1:
		// if (col == 8)
		// return ListGrid.COLUMN_TYPE_IMAGE;
		// else
		// return getColumnTypeforUK(col);
		//
		// }
		// return 0;
	}

	private int getColumnTypeforUS(int col) {
		switch (col) {
		case 1:
			return ListGrid.COLUMN_TYPE_SELECT;
		case 2:
			return ListGrid.COLUMN_TYPE_TEXTBOX;
		case 3:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		case 4:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		case 5:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXTBOX;
		default:
			return 0;

		}
	}

	@Override
	protected Object getColumnValue(ClientTransactionItem item, int col) {
		// implement for UK version

		if (item.getType() == ClientTransactionItem.TYPE_COMMENT) {

			if (!Arrays.asList(0, 2, 6).contains(col))
				return "..";
		}
		// if (item.getType() == ClientTransactionItem.TYPE_ACCOUNT) {
		//
		// if (Arrays.asList(3).contains(col))
		// return "";
		// }

		if (item.getType() == ClientTransactionItem.TYPE_SALESTAX) {
			if (!Arrays.asList(0, 1, 4, 6, 8).contains(col))
				return "";
		}
		// if (item.getType() == ClientTransactionItem.TYPE_SERVICE) {
		// if (!Arrays.asList(0, 1, 5, 6, 7, 8).contains(col))
		// return "";
		// }

		switch (col) {
		case 0:
			return getImageByType(item.getType());
		case 1:
			return getNameValue(item);
		case 2:
			return item.getDescription();
		case 3:
			if (item.getType() != ClientTransactionItem.TYPE_ACCOUNT)
				return item.getQuantity();
			else {
				return (item.getQuantity() != null || item.getLineTotal() == 0) ? item
						.getQuantity() : "";
			}
		case 4:
			if (item.getType() != ClientTransactionItem.TYPE_ACCOUNT)
				return DataUtils.getAmountAsString(item.getUnitPrice());
			else {
				return (item.getUnitPrice() != 0 || item.getLineTotal() == 0) ? DataUtils
						.getAmountAsString(item.getUnitPrice()) : "";
			}
		case 5:
			return DataUtils.getAmountAsString(item.getLineTotal());
		case 6:
			return Accounter.getFinanceMenuImages().delete();
			// return "/images/delete.png";
		default:
			return "";
		}
	}

	protected String getNameValue(ClientTransactionItem item) {
		switch (item.getType()) {
		// Without Fixing this, Software is working fine
		// FIXME--need to check for default selection values(eg:- a combo
		// should
		// have a default value selected.)
		case TYPE_ITEM:
			ClientItem itm = getCompany().getItem(item.getItem());
			return itm != null ? itm.getName() : "";
		case TYPE_SERVICE:
			ClientItem itm1 = getCompany().getItem(item.getItem());
			return itm1 != null ? itm1.getName() : "";
		case TYPE_ACCOUNT:
			ClientAccount account = getCompany().getAccount(item.getAccount());
			return account != null ? account.getDisplayName() : "";
		case TYPE_SALESTAX:
			ClientTAXItem vatItem = getCompany().getTaxItem(item.getVatItem());
			return vatItem != null ? vatItem.getDisplayName() : "";
		case TYPE_COMMENT:
			return item.getDescription() != null ? item.getDescription() : "";

		default:
			break;
		}
		return null;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { "", Accounter.getVendorsMessages().name(),
				Accounter.getVATMessages().description(),
				Accounter.getCustomersMessages().quantity(),
				Accounter.getVendorsMessages().unitPrice(),
				Accounter.getVendorsMessages().total(), " " };
	}

	/*
	 * This method implementation is for UK only
	 */
	@Override
	protected String[] getSelectValues(ClientTransactionItem obj, int index) {
		switch (index) {
		case 6:
			// deleteRecord(obj);

		default:
			break;
		}
		return super.getSelectValues(obj, index);
	}

	@Override
	public void editComplete(ClientTransactionItem item, Object value, int col) {
		// column index starts from '1'.
		try {
			boolean isItem = (item.getType() == ClientTransactionItem.TYPE_ITEM || item
					.getType() == ClientTransactionItem.TYPE_SERVICE) ? true
					: false;
			switch (col) {
			case 1:

			case 2:
				item.setDescription(value.toString() != null
						|| value.toString().length() != 0 ? value.toString()
						: "");
				if (item.getType() == ClientTransactionItem.TYPE_COMMENT)
					return;
				break;
			case 3:
				String qty = value.toString() != null
						&& (value.toString().length() != 0 && value.toString()
								.length() <= 8) ? value.toString()
						: isItem ? "1" : "0";

				if (qty.startsWith("" + UIUtils.getCurrencySymbol() + "")) {
					qty = qty.replaceAll("" + UIUtils.getCurrencySymbol() + "",
							"");
				}
				qty = qty.replaceAll(",", "");
				// int q = 0;
				// try {
				// q = Integer.parseInt(qty);
				// } catch (Exception e) {
				// Accounter.showError(AccounterErrorType.INVALIDENTRY);
				// }
				double total = DataUtils.getReformatedAmount(qty);
				Integer quantity = (int) total;
				if (quantity == 0) {
					quantity = 1;
					qty = "1";
				}
				try {
					ClientQuantity quant = new ClientQuantity();
					if (!AccounterValidator.validateGridQuantity(quantity)) {
						quant.setValue(Integer.parseInt(qty));
						item.setQuantity(quant);
						update_quantity_inAllRecords(item.getQuantity()
								.getValue());
					} else
						quant.setValue(isItem ? 1 : 0);
					item.setQuantity(quant);
				} catch (InvalidTransactionEntryException e) {
					e.printStackTrace();
				}

				// Double q = Double.parseDouble(DataUtils
				// .getReformatedAmount(qty)
				// + "");
				// if (q < 0.0)
				// Accounter.showError("Negative value not allowed");
				// item.setQuantity(q);
				refreshVatValue();
				break;
			case 4:
				String unitPriceString = value.toString() != null
						|| value.toString().length() != 0 ? value.toString()
						: "0";

				// if (unitPriceString.startsWith("" +
				// UIUtils.getCurrencySymbol()
				// + "")) {
				// unitPriceString = unitPriceString.replaceAll(""
				// + UIUtils.getCurrencySymbol() + "", "");
				// }
				// unitPriceString = unitPriceString.replaceAll(",", "");
				Double d = Double.parseDouble(DataUtils
						.getReformatedAmount(unitPriceString) + "");
				if (!AccounterValidator.validateGridUnitPrice(d)) {
					item.setUnitPrice(d);
				} else {
					d = 0.0D;
					item.setUnitPrice(d);
				}
				priceLevelSelected(priceLevel);
				refreshVatValue();

				break;
			case 5:
				String lineTotalAmtString = value.toString() != null
						|| value.toString().length() != 0 ? value.toString()
						: "0";
				// if (lineTotalAmtString.contains(""
				// + UIUtils.getCurrencySymbol() + "")) {
				// lineTotalAmtString = lineTotalAmtString.replaceAll(""
				// + UIUtils.getCurrencySymbol() + "", "");
				// }
				if (item.getType() == TYPE_SALESTAX
						|| item.getType() == TYPE_ACCOUNT
						|| item.getType() == TYPE_SERVICE) {

					// lineTotalAmtString =
					// lineTotalAmtString.replaceAll(",",
					// "");
					Double lineTotal = Double.parseDouble(DataUtils
							.getReformatedAmount(lineTotalAmtString) + "");
					try {
						if ((!AccounterValidator
								.validateGridLineTotal(lineTotal))
								&& (!AccounterValidator
										.isAmountTooLarge(lineTotal))) {
							item.setLineTotal(lineTotal);
							item.setUnitPrice(isItem ? lineTotal : 0);
							ClientQuantity quant = new ClientQuantity();
							quant.setValue(isItem ? 1 : 0);
							item.setQuantity(quant);
						}

					} catch (Exception e) {
						if (e instanceof InvalidEntryException) {
							item.setLineTotal(0.0D);
							item.setUnitPrice(0.0D);
							ClientQuantity quant = new ClientQuantity();
							quant.setValue(isItem ? 1 : 0);
							item.setQuantity(quant);
							// Accounter.showError(e.getMessage());
							// BaseView.errordata.setHTML("<li> " +
							// e.getMessage()
							// + ".");
							// BaseView.commentPanel.setVisible(true);
							MainFinanceWindow.getViewManager().showError(
									e.getMessage());
						}
					}
				}

				break;

			case 7:
				if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
					if (value.equals("Taxable")) {
						item.setTaxable(true);
					} else
						item.setTaxable(false);
				}
				break;
			case 8:

				break;

			default:
				break;
			}
		} catch (Exception e) {
			// Accounter.showError(AccounterErrorType.INVALIDENTRY);
		}

		if (accountingType == ClientCompany.ACCOUNTING_TYPE_UK
				&& !getCompany().getpreferences().getDoYouPaySalesTax()) {
			if (item.getType() == TYPE_SERVICE
					&& item.getType() == TYPE_ACCOUNT
					|| item.getType() == TYPE_ITEM) {
				if (ztaxCodeid != 0)
					item.setTaxCode(ztaxCodeid);
				else {
					List<ClientTAXCode> taxCodes = Accounter.getCompany()
							.getActiveTaxCodes();
					for (ClientTAXCode taxCode : taxCodes) {
						if (taxCode.getName().equals("Z")) {
							ztaxCodeid = taxCode.getID();
						}
					}
					if (ztaxCodeid != 0)
						item.setTaxCode(ztaxCodeid);
				}
			}

		}
		if (col != 5 && col != 6) {
			double lt = item.getQuantity().getValue() * item.getUnitPrice();
			double disc = item.getDiscount();
			if (col != 5)
				item.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt
						* disc / 100))
						: lt);
		}
		updateTotals();
		updateData(item);
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			if (Arrays.asList(3, 4, 5, 6).contains(col))
				refreshVatValue();
		}
	}

	@Override
	protected boolean isEditable(ClientTransactionItem obj, int row, int col) {
		if (obj == null)
			return false;
		if (!getCompany().getpreferences().getDoYouPaySalesTax()) {
			if (col == 6 || col == 7)
				return false;
		}
		switch (obj.getType()) {
		case ClientTransactionItem.TYPE_ACCOUNT:
			switch (col) {
			// case 3:
			// return false;
			// case 4:
			// return false;
			default:
				return true;
			}
		case ClientTransactionItem.TYPE_COMMENT:
			switch (col) {
			case 2:
				return true;
			default:
				return false;
			}
		case ClientTransactionItem.TYPE_ITEM:
			switch (col) {
			case 5:
				return false;
			default:
				return true;
			}
		case ClientTransactionItem.TYPE_SERVICE:
			switch (col) {
			// case 3:
			// return false;
			// case 4:
			// return false;

			default:
				return true;
			}

		}

		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <E> CustomCombo<E> getCustomCombo(ClientTransactionItem obj,
			int colIndex) {
		CustomCombo<E> combo = null;
		switch (colIndex) {
		case 1:
			if (obj.getType() == ClientTransactionItem.TYPE_ACCOUNT) {
				combo = (CustomCombo<E>) accountsCombo;
			} else if (obj.getType() == ClientTransactionItem.TYPE_ITEM)
				combo = (CustomCombo<E>) productItemCombo;
			else if (obj.getType() == ClientTransactionItem.TYPE_SERVICE) {
				combo = (CustomCombo<E>) serviceItemCombo;
			} else if (obj.getType() == ClientTransactionItem.TYPE_SALESTAX) {
				combo = (CustomCombo<E>) vatItemCombo;
			}

			if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
				combo.downarrowpanel.getElement().getStyle()
						.setMarginLeft(-10, Unit.PX);
			} else {
				if (this instanceof PurchaseOrderUSGrid)
					combo.downarrowpanel.getElement().getStyle()
							.setMarginLeft(-8, Unit.PX);
				else
					combo.downarrowpanel.getElement().getStyle()
							.setMarginLeft(-15, Unit.PX);
			}
			break;
		case 6:
			// for UK
			combo = (CustomCombo<E>) taxCodeCombo;
			if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
				combo.downarrowpanel.getElement().getStyle()
						.setMarginLeft(-7, Unit.PX);
			} else {

			}
			break;
		case 7:
			// for purchase Order
			combo = (CustomCombo<E>) taxCodeCombo;
			if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
				combo.downarrowpanel.getElement().getStyle()
						.setMarginLeft(-7, Unit.PX);
			} else {

			}
			break;
		default:
			break;
		}
		return combo;
	}

	protected String getImageByType(int type) {
		switch (type) {
		case TYPE_ITEM:
			// return
			// FinanceApplication.getFinanceMenuImages().items().getURL();
			return "/images/items.png";
		case TYPE_ACCOUNT:
			// return FinanceApplication.getFinanceMenuImages().Accounts()
			// .getURL();
			return "/images/Accounts.png";
		case TYPE_COMMENT:
			// return FinanceApplication.getFinanceMenuImages().comments()
			// .getURL();
			return "/images/comments.png";
		case TYPE_SALESTAX:
			// return FinanceApplication.getFinanceMenuImages().salestax()
			// .getURL();
			return "/images/salestax.png";
		case TYPE_SERVICE:
			// return FinanceApplication.getFinanceMenuImages().salestax()
			// .getURL();
			return "/images/salestax.png";
		default:
			break;
		}
		return "";
	}

	@Override
	public boolean validateGrid() throws InvalidTransactionEntryException {
		int validationcount = 1;
		for (ClientTransactionItem item : this.getRecords()) {
			if (item.getType() != ClientTransactionItem.TYPE_COMMENT) {
				switch (validationcount++) {
				case 1:
					AccounterValidator.validateGridItem(
							this.getColumnValue(item, 1),
							UIUtils.getTransactionTypeName(item.getType()));
				case 2:
					if (accountingType == ClientCompany.ACCOUNTING_TYPE_UK
							&& item.getType() != ClientTransactionItem.TYPE_SALESTAX) {
						AccounterValidator.validateGridItem(
								this.getColumnValue(item, 6), "Vat Code");
						validationcount = 1;
					} else
						validationcount = 1;
					break;
				default:
					break;
				}

			}
		}
		if (DecimalUtil.isLessThan(totallinetotal, 0.0)) {
			Accounter.showError(AccounterErrorType.InvalidTransactionAmount);
			return false;
		}
		return true;
	}

	private void update_quantity_inAllRecords(double quantity) {
		int decimalpoints_count = getMaxDecimals(quantity);
		if (maxDecimalPoint < decimalpoints_count) {
			maxDecimalPoint = decimalpoints_count;
			for (ClientTransactionItem item : this.getRecords()) {
				updateData(item);
			}
		}
	}

	public int getMaxDecimals(double quantity) {
		String qty = String.valueOf(quantity);
		String max = qty.substring(qty.indexOf(".") + 1);
		return max.length();
	}

	@Override
	public void setTaxCode(long taxCode) {

	}

}
