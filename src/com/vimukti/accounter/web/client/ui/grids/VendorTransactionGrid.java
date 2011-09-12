package com.vimukti.accounter.web.client.ui.grids;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.ProductCombo;
import com.vimukti.accounter.web.client.ui.combo.PurchaseAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.ServiceCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.combo.VATItemCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.vendors.AbstractVendorTransactionView;
import com.vimukti.accounter.web.client.ui.vendors.CreditCardExpenseView;

public class VendorTransactionGrid extends
		AbstractTransactionGrid<ClientTransactionItem> {

	private PurchaseAccountsCombo accountsCombo;
	// ItemCombo itemCombo;
	ServiceCombo serviceItemCombo;
	ProductCombo productItemCombo;
	TAXCodeCombo taxCodeCombo;
	VATItemCombo vatItemCombo;
	AccounterConstants accounterConstants = Accounter.constants();
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

	public VendorTransactionGrid() {
		super(false, true);
		this.accountingType = getCompany().getAccountingType();
	}

	public VendorTransactionGrid(boolean isAddNewRequired) {
		super(false, true);
		this.isAddNewRequired = isAddNewRequired;
		this.accountingType = getCompany().getAccountingType();
	}

	@Override
	public void init() {
		super.isEnable = false;
		super.init();
		createControls();
		initTransactionData();

		ClientTransaction transactionObject = transactionView
				.getTransactionObject();

		if (transactionObject != null) {
			setAllTransactionItems(transactionObject.getTransactionItems());
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
	public List<ClientTransactionItem> getallTransactionItems(
			ClientTransaction object) {
		return getRecords();
	}

	protected void createControls() {
		setWidth("100%");
		// setSize("100%", "200px");

		// Passing 1 for Customer, 2 For Vendor For Item View- Raj Vimal
		serviceItemCombo = new ServiceCombo(Accounter.constants().item(), 2,
				isAddNewRequired);
		serviceItemCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientItem>() {

					@Override
					public void selectedComboBoxItem(ClientItem selectItem) {
						if (selectItem != null) {
							selectedObject.setItem(selectItem.getID());
							selectedObject.setUnitPrice(selectItem
									.getPurchasePrice());
							if (getCompany().getPreferences()
									.isRegisteredForVAT()) {
								selectedObject.setTaxable(selectItem
										.isTaxable());
							} else {
								selectedObject.setTaxable(false);
							}
							setText(currentRow, currentCol,
									selectItem.getName());
							if (getCompany().getPreferences()
									.isRegisteredForVAT()) {
								selectedObject.setTaxCode(selectItem
										.getTaxCode() != 0 ? selectItem
										.getTaxCode() : 0);
							}
							editComplete(selectedObject,
									selectItem.getPurchasePrice(), 4);

							// it should be here only for vat calculations(it
							// needs line total for this,linetotal calculated in
							// editcomplete()
							if (getCompany().getPreferences()
									.isRegisteredForVAT()
									&& !isPurchseOrderTransaction) {
								refreshVatValue(selectedObject);
							}
						}
					}
				});
		serviceItemCombo.setGrid(this);

		productItemCombo = new ProductCombo(
				Accounter.constants().productItem(), 2, isAddNewRequired);
		productItemCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientItem>() {

					@Override
					public void selectedComboBoxItem(ClientItem selectItem) {
						if (selectItem != null) {
							selectedObject.setItem(selectItem.getID());
							selectedObject.setUnitPrice(selectItem
									.getPurchasePrice());
							if (getCompany().getPreferences()
									.isRegisteredForVAT()) {
								selectedObject.setTaxable(selectItem
										.isTaxable());
							} else {
								selectedObject.setTaxable(false);
							}
							setText(currentRow, currentCol,
									selectItem.getName());
							if (getCompany().getPreferences()
									.isRegisteredForVAT()) {
								selectedObject.setTaxCode(selectItem
										.getTaxCode() != 0 ? selectItem
										.getTaxCode() : 0);
							}
							editComplete(selectedObject,
									selectItem.getPurchasePrice(), 4);

							// it should be here only for vat calculations(it
							// needs line total for this,linetotal calculated in
							// editcomplete()
							if (getCompany().getPreferences()
									.isRegisteredForVAT()
									&& !isPurchseOrderTransaction) {
								refreshVatValue(selectedObject);
							}
						}
					}
				});
		productItemCombo.setGrid(this);

		accountsCombo = new PurchaseAccountsCombo(Accounter.messages()
				.accounts(Global.get().Account()), isAddNewRequired);
		accountsCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectedObject.setAccount(selectItem.getID());
						if (getCompany().getAccountingType() == 1)
							selectedObject.setTaxable(true);
						setText(currentRow, currentCol, selectItem.getName());
						if (getCompany().getPreferences().isRegisteredForVAT()) {
							setVendorTaxCode(selectedObject);
						}
					}
				});
		accountsCombo.setGrid(this);
		// accountsCombo.setWidth("600");
		// if (!isBankingTransaction && !isPurchseOrderTransaction)
		// this.addFooterValues(new String[] { "", "", "", "",
		// FinanceApplication.constants().total(),
		// amountAsString(0.00), });
		// else if (isPurchseOrderTransaction) {
		//
		// this.addFooterValues(new String[] { "", "", "", "", "", "",
		// FinanceApplication.constants().totalcolan(), "" });
		// } else {
		// this.addFooterValues(new String[] { "", "", "",
		// FinanceApplication.constants().totalcolan(), "" });
		// }
		if (getCompany().getPreferences().isRegisteredForVAT()) {
			createVatItemAndTaxCodeCombo();
			if (!isBankingTransaction && !isPurchseOrderTransaction) {
				// this.addFooterValue("VAT", 6);
				// this.addFooterValue(amountAsString(0.00), 7);
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
						// if (!isPurchseOrderTransaction)
						updateTotals();
					}
					return true;
				}

				if (getCompany().getPreferences().isRegisteredForVAT()
						&& column == 6) {
					taxCodeCombo.setComboItem(getCompany().getTAXCode(
							core.getTaxCode()));
				}
				return true;
			}
		});
	}

	private void createVatItemAndTaxCodeCombo() {
		vatItemCombo = new VATItemCombo(Accounter.constants().vatItem(),
				isAddNewRequired);
		List<ClientTAXItem> vendorVATItems = new ArrayList<ClientTAXItem>();
		for (ClientTAXItem vatItem : getCompany().getActiveTaxItems()) {
			if (!vatItem.isSalesType())
				vendorVATItems.add(vatItem);

		}
		vatItemCombo.initCombo(vendorVATItems);
		vatItemCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXItem>() {

					@Override
					public void selectedComboBoxItem(ClientTAXItem selectItem) {
						if (selectItem != null) {
							if (!isPreviuslyUsed(selectItem)) {
								Accounter
										.showError("The VATItem selected is already used in VAT column.Please select a different VATItem");
							}
							selectedObject.setVatItem(selectItem.getID());
							setText(currentRow, currentCol,
									selectItem.getName());
						}
					}
				});
		// taxCodeCombo.setGrid(this);

		taxCodeCombo = new TAXCodeCombo(Accounter.constants().vatCode(),
				isAddNewRequired, false);
		taxCodeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXCode>() {

					@Override
					public void selectedComboBoxItem(ClientTAXCode selectItem) {
						if (selectItem != null) {
							selectedObject.setTaxCode(selectItem.getID());
							if (selectedObject.getType() == TYPE_SERVICE
									|| selectedObject.getType() == TYPE_ACCOUNT)
								editComplete(selectedObject,
										selectedObject.getLineTotal(), 6);
							else
								editComplete(selectedObject,
										selectedObject.getUnitPrice(), 4);
						}
					}
				});
		taxCodeCombo.setGrid(this);
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

		if (getCompany().getPreferences().isRegisteredForVAT()
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
				break;
			}
		}
		if (transactionView instanceof CreditCardExpenseView) {
			CreditCardExpenseView cardExpenseView = (CreditCardExpenseView) transactionView;
			ClientVendor selectedVendor = cardExpenseView.getSelectedVendor();
			if (transactionView.getTransactionObject() == null
					&& selectedVendor != null)
				selectedObject
						.setTaxCode(selectedObject.getTaxCode() != 0 ? selectedObject
								.getTaxCode()
								: selectedVendor.getTAXCode() > 0 ? selectedVendor
										.getTAXCode() : ztaxCodeid);
		} else {
			AbstractVendorTransactionView<?> view = (AbstractVendorTransactionView<?>) transactionView;
			ClientVendor selectedVendor = view.getVendor();
			if (transactionView.getTransactionObject() == null
					&& selectedVendor != null)
				selectedObject
						.setTaxCode(selectedObject.getTaxCode() != 0 ? selectedObject
								.getTaxCode()
								: selectedVendor.getTAXCode() > 0 ? selectedVendor
										.getTAXCode() : ztaxCodeid);
			else
				selectedObject.setTaxCode(ztaxCodeid);
		}

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
		// this.addFooterValue(amountAsString(totallinetotal), 7);
		// } else {
		// this.addFooterValue(amountAsString(totallinetotal), 5);
		// }

		if (getCompany().getPreferences().isChargeSalesTax()) {
			grandTotal = totalVat + totallinetotal;
		} else {
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
		// this.addFooterValue(amountAsString(totalVat), 7);
		// }

	}

	// public List<ClientTransactionItem> getallTransactions(
	// ClientTransaction object) throws InvalidEntryException {
	//
	// return getRecords();
	// }

	@Override
	public void setAllTransactionItems(
			List<ClientTransactionItem> transactionItems) {
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
				if (getCompany().getPreferences().isRegisteredForVAT()) {
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
		if (!(this instanceof PurchaseOrderGrid)) {
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
	public double getGrandTotal() {
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
			// TODO raj
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
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			return getUKGridCellWidth(index);
		} else {
			return getUSGridCellWidth(index);
		}

	}

	private int getUSGridCellWidth(int index) {
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

	private int getUKGridCellWidth(int index) {
		if (index == 0 || index == 8)
			if (UIUtils.isMSIEBrowser())
				return 25;
			else
				return 15;
		else if (index == 3 || index == 4)
			return 90;
		if (index == 1)
			return 150;
		if (index == 6)
			if (getCompany().getPreferences().isChargeSalesTax()) {
				return 70;
			} else {
				if (UIUtils.isMSIEBrowser())
					return 25;
				else
					return 15;
			}
		if (index == 7)
			return 60;
		if (index == 2) {
			if (Accounter.isMacApp()) {
				int var = Window.getClientWidth();
				return var - 830;
			} else {
				if (UIUtils.isMSIEBrowser()) {
					return 110;
				} else {
					return 130;
				}
			}
		}
		if (index == 5)
			return 100;
		return -1;
	}

	@Override
	protected int getColumnType(int col) {
		switch (col) {
		case 0:
			return ListGrid.COLUMN_TYPE_IMAGE;
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
		case 6:
			if (this.accountingType == ClientCompany.ACCOUNTING_TYPE_UK
					&& getCompany().getPreferences().isChargeSalesTax())
				return ListGrid.COLUMN_TYPE_SELECT;
			else
				return ListGrid.COLUMN_TYPE_IMAGE;
		case 7:
			if (getCompany().getPreferences().isRegisteredForVAT())
				return ListGrid.COLUMN_TYPE_DECIMAL_TEXT;
			else
				return ListGrid.COLUMN_TYPE_IMAGE;
		case 8:
			return ListGrid.COLUMN_TYPE_IMAGE;
		default:
			return 0;

		}
	}

	@Override
	protected Object getColumnValue(ClientTransactionItem item, int col) {

		if (item.getType() == ClientTransactionItem.TYPE_COMMENT) {

			if (!Arrays.asList(0, 2, 6).contains(col))
				return "..";
		}

		if (item.getType() == ClientTransactionItem.TYPE_SALESTAX) {
			if (!Arrays.asList(0, 1, 4, 6, 8).contains(col))
				return "";
		}

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
				return amountAsString(getAmountInForeignCurrency(item
						.getUnitPrice()));
			else {
				return (item.getUnitPrice() != 0 || item.getLineTotal() == 0) ? amountAsString(getAmountInForeignCurrency(item
						.getUnitPrice())) : "";
			}
		case 5:
			return amountAsString(getAmountInForeignCurrency(item
					.getLineTotal()));
		case 6:
			if (this.accountingType == ClientCompany.ACCOUNTING_TYPE_UK
					&& getCompany().getPreferences().isChargeSalesTax()) {
				return getTAXCodeName(item.getTaxCode());
			} else {
				return Accounter.getFinanceMenuImages().delete();
			}
			// return "/images/delete.png";
		case 7:
			if (getCompany().getPreferences().isRegisteredForVAT()) {
				return amountAsString(getAmountInForeignCurrency(item
						.getVATfraction()));
			} else {
				return Accounter.getFinanceMenuImages().delete();
			}
		case 8:
			return Accounter.getFinanceMenuImages().delete();
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
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK
				&& getCompany().getPreferences().isChargeSalesTax()) {
			return new String[] { "", Accounter.constants().name(),
					Accounter.constants().description(),
					Accounter.constants().quantity(),
					Accounter.constants().unitPrice(),
					Accounter.constants().total(),
					Accounter.constants().newVATCode(),
					Accounter.constants().vat(), " " };

		} else {
			return new String[] { "", Accounter.constants().name(),
					Accounter.constants().description(),
					Accounter.constants().quantity(),
					Accounter.constants().unitPrice(),
					Accounter.constants().total(), " " };
		}
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
					if (AccounterValidator.isValidGridQuantity(quantity)) {
						quant.setValue(Double.parseDouble(qty));
						item.setQuantity(quant);
						update_quantity_inAllRecords(item.getQuantity()
								.getValue());
					} else {
						quant.setValue(isItem ? 1 : 0);
						item.setQuantity(quant);
						transactionView.addError(this, Accounter.constants()
								.quantity());
					}
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

				if (AccounterValidator.isValidGridUnitPrice(d)) {
					item.setUnitPrice(getAmountInBaseCurrency(d.doubleValue()));
				} else {
					d = 0.0D;
					item.setUnitPrice(d);
					transactionView.addError(this,
							accounterConstants.unitPrice());
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
								.isValidGridLineTotal(lineTotal))
								&& (!AccounterValidator
										.isAmountTooLarge(lineTotal))) {

							item.setLineTotal(getAmountInBaseCurrency(lineTotal
									.doubleValue()));
							// TODO
							item.setUnitPrice(isItem ? lineTotal : 0);
							ClientQuantity quant = new ClientQuantity();
							quant.setValue(1);
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
							transactionView.addError(this, e.getMessage());
						}
					}
				}

				break;

			case 7:
				if (getCompany().getPreferences().isChargeSalesTax()) {
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

		if (getCompany().getPreferences().isRegisteredForVAT()
				&& !getCompany().getPreferences().isChargeSalesTax()) {
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
		if (!getCompany().getPreferences().isChargeSalesTax()) {
			if (col == 6 || col == 7)
				return false;
		}
		switch (obj.getType()) {
		case ClientTransactionItem.TYPE_ACCOUNT:
			switch (col) {
			case 3:
				return false;
			case 4:
				return false;
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

			// if (getCompany().getAccountingType() ==
			// ClientCompany.ACCOUNTING_TYPE_UK) {
			// combo.downarrowpanel.getElement().getStyle()
			// .setMarginLeft(-10, Unit.PX);
			// } else {
			// if (this instanceof PurchaseOrderGrid)
			// combo.downarrowpanel.getElement().getStyle()
			// .setMarginLeft(-8, Unit.PX);
			// else
			// combo.downarrowpanel.getElement().getStyle()
			// .setMarginLeft(-15, Unit.PX);
			// }
			break;
		case 6:
			// for UK
			combo = (CustomCombo<E>) taxCodeCombo;
			// if (getCompany().getAccountingType() ==
			// ClientCompany.ACCOUNTING_TYPE_UK) {
			// combo.downarrowpanel.getElement().getStyle()
			// .setMarginLeft(-7, Unit.PX);
			// } else {
			//
			// }
			break;
		case 7:
			// for purchase Order
			combo = (CustomCombo<E>) taxCodeCombo;
			// if (getCompany().getAccountingType() ==
			// ClientCompany.ACCOUNTING_TYPE_UK) {
			// combo.downarrowpanel.getElement().getStyle()
			// .setMarginLeft(-7, Unit.PX);
			// } else {
			//
			// }
			break;
		default:
			break;
		}
		return combo;
	}

	protected ImageResource getImageByType(int type) {
		switch (type) {
		case TYPE_ITEM:
			// return
			// FinanceApplication.getFinanceMenuImages().items().getURL();
			return Accounter.getFinanceImages().itemsIcon();
		case TYPE_ACCOUNT:
			// return FinanceApplication.getFinanceMenuImages().Accounts()
			// .getURL();
			return Accounter.getFinanceImages().AccountsIcon();
		case TYPE_COMMENT:
			// return FinanceApplication.getFinanceMenuImages().comments()
			// .getURL();
			return Accounter.getFinanceImages().CommentsIcon();
		case TYPE_SALESTAX:
			// return FinanceApplication.getFinanceMenuImages().salestax()
			// .getURL();
			return Accounter.getFinanceImages().salesTaxIcon();
		case TYPE_SERVICE:
			// return FinanceApplication.getFinanceMenuImages().salestax()
			// .getURL();
			return Accounter.getFinanceImages().salesTaxIcon();
		default:
			break;
		}
		return Accounter.getFinanceImages().errorImage();
	}

	@Override
	public ValidationResult validateGrid() {
		ValidationResult result = new ValidationResult();
		int validationcount = 1;
		for (ClientTransactionItem item : this.getRecords()) {
			int row = this.objects.indexOf(item);
			if (item.getType() != ClientTransactionItem.TYPE_COMMENT) {
				switch (validationcount++) {
				case 1:
					if (AccounterValidator
							.isEmpty(this.getColumnValue(item, 1))) {
						result.addError(
								row + "," + 1,
								Accounter.messages().pleaseEnter(
										UIUtils.getTransactionTypeName(item
												.getType())));
					}
					// ,
					// UIUtils.getTransactionTypeName(item.getType()));
				case 2:
					if (getCompany().getPreferences().isRegisteredForVAT()
							&& item.getType() != ClientTransactionItem.TYPE_SALESTAX) {
						if (AccounterValidator.isEmpty(this
								.getColumnValue(item,
										this instanceof PurchaseOrderGrid ? 7
												: 6))) {
							result.addError(
									row + "," + 6,
									Accounter.messages().pleaseEnter(
											Accounter.constants().vatCode()));
						}
						// .vatCode());
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
			result.addError(this, Accounter.constants()
					.invalidTransactionAmount());
			// Accounter.showError(AccounterErrorType.InvalidTransactionAmount);
			// return false;
		}
		return result;
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

	public boolean isPreviuslyUsed(ClientTAXItem selectedVATItem) {
		for (ClientTransactionItem rec : getRecords()) {
			if (rec.getTaxCode() != 0) {
				long vatItem = getCompany().getTAXCode(rec.getTaxCode())
						.getTAXItemGrpForSales();
				if (selectedVATItem.getID() == vatItem) {
					return false;
				}
			}
		}
		return true;
	}
}
