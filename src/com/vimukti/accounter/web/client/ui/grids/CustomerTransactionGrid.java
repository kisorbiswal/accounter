package com.vimukti.accounter.web.client.ui.grids;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTAXGroup;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTAXItemGroup;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.banking.WriteChequeView;
import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.ProductCombo;
import com.vimukti.accounter.web.client.ui.combo.SalesAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.ServiceCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.combo.VATItemCombo;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;

public abstract class CustomerTransactionGrid extends
		AbstractTransactionGrid<ClientTransactionItem> {

	private SalesAccountsCombo accountsCombo;
	// TaxCodeCombo salesTaxCombo;
	// ItemCombo itemCombo;
	ServiceCombo serviceItemCombo;
	ProductCombo productItemCombo;
	TAXCodeCombo taxCodeCombo;
	VATItemCombo vatItemCombo;
	protected boolean isSalesOrderTransaction;
	private Double totallinetotal = 0.0D;
	private Double totalVat;
	private Double grandTotal;

	private ClientPriceLevel priceLevel;
	private ClientTransactionItem selectedRecord;

	@SuppressWarnings("unused")
	private List<ClientAccount> gridAccounts;
	private double taxableTotal;

	int transactionDomain;
	boolean isBankingTransaction = false;

	private int accountingType;
	private Double totalValue;

	private boolean isAddNewRequired = true;
	private String ztaxCodeStringId = null;
	protected int maxDecimalPoint;
	protected String taxCode;

	public CustomerTransactionGrid() {
		super(false, true);
		this.accountingType = FinanceApplication.getCompany()
				.getAccountingType();

	}

	public CustomerTransactionGrid(boolean isAddNewRequired) {
		super(false, true);
		this.isAddNewRequired = isAddNewRequired;
		this.accountingType = FinanceApplication.getCompany()
				.getAccountingType();

	}

	@Override
	public String[] getColumnNamesForPrinting() {
		return null;
	}

	@Override
	public String getColumnValueForPrinting(ClientTransactionItem item,
			int index) {
		return null;

	}

	@Override
	protected int getColumnType(int col) {
		return 0;

	}

	@Override
	protected int getCellWidth(int index) {
		return -1;

	}

	@Override
	public void init() {
		super.init();
		// if (FinanceApplication.getCompany().getAccountingType() !=
		// ClientCompany.ACCOUNTING_TYPE_UK) {
		createControls();

		initTransactionData();

		ClientTransaction transactionObject = transactionView
				.getTransactionObject();

		if (transactionObject != null) {
			setAllTransactions(transactionObject.getTransactionItems());
			if (transactionObject.getStringID() != null) {
				// ITS Edit Mode
				// setShowMenu(false);
				// isEdit = true;
				canDeleteRecord(false);
				// canAddRecord(false);
				// setEditDisableCells(new int[] { 0, 1, 2, 3, 4 });
			}
			// }
		}
	}

	@Override
	protected String[] getSelectValues(ClientTransactionItem obj, int index) {
		switch (index) {
		case 7:
			return new String[] {
					FinanceApplication.getCustomersMessages().taxable(),
					FinanceApplication.getCustomersMessages().nonTaxable() };

		default:
			break;
		}
		return super.getSelectValues(obj, index);
	}

	protected void initTransactionData() {

		List<ClientItem> result = FinanceApplication.getCompany()
				.getActiveItems();
		// if (isCustomerTransaction || isBankingTransaction) {
		List<ClientItem> customerItems = new ArrayList<ClientItem>();
		for (ClientItem item : result) {
			if (item.isISellThisItem())
				customerItems.add(item);
		}
		List<ClientItem> serviceitems = new ArrayList<ClientItem>();
		List<ClientItem> productitems = new ArrayList<ClientItem>();
		for (ClientItem item : customerItems) {
			if (item.getType() == ClientItem.TYPE_SERVICE)
				serviceitems.add(item);
			else
				productitems.add(item);
		}
		serviceItemCombo.initCombo(serviceitems);

		productItemCombo.initCombo(productitems);
		// itemCombo.initCombo(customerItems);
	}

	protected void createControls() {

		setSize("100%", "250px");

		// Passing 1 for Customer, 2 For Vendor For Item View- Raj Vimal
		serviceItemCombo = new ServiceCombo("Item", 1, isAddNewRequired);
		serviceItemCombo.setGrid(this);
		serviceItemCombo.setRequired(true);
		serviceItemCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientItem>() {

					@Override
					public void selectedComboBoxItem(ClientItem selectItem) {
						if (selectItem != null) {
							selectedObject.setItem(selectItem.getStringID());
							selectedObject.setUnitPrice(selectItem
									.getSalesPrice());
							selectedObject.setTaxable(selectItem.isTaxable());
							if (FinanceApplication.getCompany()
									.getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
								selectedObject.setTaxCode(selectItem
										.getTaxCode() != null ? selectItem
										.getTaxCode() : "");
							}
							// it should be here only for vat
							// calculations(it
							// needs line total for this,linetotal
							// calculated in
							// editcomplete()
							editComplete(selectedObject, selectItem
									.getSalesPrice(), 4);
							applyPriceLevel(selectedObject);

						}
					}
				});

		productItemCombo = new ProductCombo(FinanceApplication
				.getCustomersMessages().PRoduct(), 1, isAddNewRequired);
		productItemCombo.setGrid(this);
		productItemCombo.setRequired(true);
		productItemCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientItem>() {

					@Override
					public void selectedComboBoxItem(ClientItem selectItem) {
						if (selectItem != null) {
							selectedObject.setItem(selectItem.getStringID());
							selectedObject.setUnitPrice(selectItem
									.getSalesPrice());
							if (transactionView instanceof WriteChequeView)
								selectedObject.setTaxable(false);
							else
								selectedObject.setTaxable(selectItem
										.isTaxable());
							if (FinanceApplication.getCompany()
									.getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
								selectedObject.setTaxCode(selectItem
										.getTaxCode() != null ? selectItem
										.getTaxCode() : "");
							}
							// it should be here only for vat
							// calculations(it
							// needs line total for this,linetotal
							// calculated in
							// editcomplete()
							editComplete(selectedObject, selectItem
									.getSalesPrice(), 4);
							applyPriceLevel(selectedObject);

						}
					}
				});

		// itemCombo.addFilter(new Filter() {
		//
		// @Override
		// public boolean canAdd(IAccounterCore core) {
		// ClientItem item = (ClientItem) core;
		//
		// if (item == null)
		// return false;
		// if (!item.isISellThisItem()) {
		// Accounter
		// .showInformation(AccounterErrorType.cannotUsePurchaseItem);
		// return false;
		// }
		//
		// // else if (isVendorTransaction && !item.isIBuyThisItem()) {
		// // Accounter
		// // .showInformation(AccounterErrorType.cannotUseSalesItem);
		// // return false;
		// // }
		// return true;
		//
		// // return isCustomerTransaction ? item.isISellThisItem() : item
		// // .isIBuyThisItem();
		// }
		//
		// });

		accountsCombo = new SalesAccountsCombo(FinanceApplication
				.getCustomersMessages().nominalCodeItem(), isAddNewRequired);
		accountsCombo.setGrid(this);
		accountsCombo.setRequired(true);
		accountsCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectedObject.setAccount(selectItem.getStringID());
						if (FinanceApplication.getCompany().getAccountingType() == 1)
							selectedObject.setTaxable(true);
						setText(currentRow, currentCol, selectItem.getName());
						updateData(selectedObject);
						if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
							if (FinanceApplication.getCompany()
									.getpreferences().getDoYouPaySalesTax())
								setCustomerTaxCode(selectedObject);
						}
					}
				});
		// accountsCombo.setWidth("530");

		// salesTaxCombo = new TaxCodeCombo(FinanceApplication
		// .getCustomersMessages().salesTax(), isAddNewRequired);
		// salesTaxCombo.setGrid(this);
		// salesTaxCombo.setRequired(true);
		// salesTaxCombo
		// .addSelectionChangeHandler(new
		// IAccounterComboSelectionChangeHandler<ClientTaxCode>() {
		//
		// @Override
		// public void selectedComboBoxItem(ClientTaxCode selectItem) {
		// selectedObject.setTaxCode(selectItem.getStringID());
		// setText(currentRow, currentCol, selectItem.getName());
		// }
		// });

		// column count from '1'
		// if (!isBankingTransaction)
		// this.addFooterValues(new String[] { "", "", "Sub Total",
		// DataUtils.getAmountAsString(0.00), "", "Discount: 0%",
		// "Line Total: " + DataUtils.getAmountAsString(0.00), });
		// else
		// this.addFooterValues(new String[] { "", "", "Sub Total",
		// DataUtils.getAmountAsString(0.00),
		// "Line Total: " + DataUtils.getAmountAsString(0.00), });
		// FXIME check it for VAT implementation
		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK)
			createVATItemAndTaxCodeCombo();

		if (!isBankingTransaction && !isSalesOrderTransaction){
//			this.addFooterValues(new String[] { "", "", "", "", "",
//					FinanceApplication.getCustomersMessages().total(),
//					DataUtils.getAmountAsString(0.00), });
		}
		else if (isSalesOrderTransaction) {

//			this.addFooterValues(new String[] { "", "", "", "", "", "",
//					DataUtils.getAmountAsString(0.00), "" });
		
		} else {
//			this.addFooterValues(new String[] { "", "", "",
//					DataUtils.getAmountAsString(0.00), "" });
		}
		if (FinanceApplication.getCompany().getAccountingType() == 1) {
			if (!isBankingTransaction)
				if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
					// this.addFooterValue(FinanceApplication.getVATMessages()
					// .VAT(), 7);
					// this.addFooterValue(DataUtils.getAmountAsString(0.00),
					// 8);
				}
			// setBottomLabelTitle("VAT: "
			// + DataUtils.getAmountAsString(totalVat), 8);
		}

		addRecordClickHandler(new RecordClickHandler<ClientTransactionItem>() {

			@Override
			public boolean onRecordClick(ClientTransactionItem core, int column) {
				switch (column) {
				case 1:
					if (core.getType() == ClientTransactionItem.TYPE_ACCOUNT) {
						if (core.getAccount() != null)
							accountsCombo
									.setComboItem(FinanceApplication
											.getCompany().getAccount(
													core.getAccount()));
						else
							accountsCombo.setValue("");
					} else if (core.getType() == ClientTransactionItem.TYPE_SERVICE) {
						if (core.getItem() != null)
							serviceItemCombo.setComboItem(FinanceApplication
									.getCompany().getItem(core.getItem()));
						else
							serviceItemCombo.setValue("");
					} else if (core.getType() == ClientTransactionItem.TYPE_ITEM) {
						if (core.getItem() != null)
							productItemCombo.setComboItem(FinanceApplication
									.getCompany().getItem(core.getItem()));
						else
							productItemCombo.setValue("");
					} else if (core.getType() == ClientTransactionItem.TYPE_SALESTAX) {
						// if
						// (FinanceApplication.getCompany().getAccountingType()
						// == ClientCompany.ACCOUNTING_TYPE_US) {
						// if (core.getTaxCode() != null)
						// salesTaxCombo.setComboItem(FinanceApplication
						// .getCompany().getTaxCode(
						// core.getTaxCode()));
						// else
						// salesTaxCombo.setValue("");
						// } else {
						// if (core.getTaxCode() != null)
						// salesTaxCombo.setComboItem(FinanceApplication
						// .getCompany().getTaxCode(
						// core.getTaxCode()));
						// else
						// salesTaxCombo.setValue("");
						// }
					}
					break;
				default:
					break;

				}
				// in US-version,column '7' is Taxable,so if user doubleclick on
				// it thatcombo should be enbled,for avoiding columnindex
				// collisions of 7th column in uk,the code for VATCodeCombo
				// is written here
				// if (FinanceApplication.getCompany().getAccountingType() ==
				// ClientCompany.ACCOUNTING_TYPE_UK
				// && column == 7) {
				// vatCodeCombo.setComboItem(FinanceApplication.getCompany()
				// .getVATCode(core.getVatCode()));
				// }
				if (column == getColumnsCount() - 1) {
					if (!transactionView.isEdit()) {
						deleteRecord(core);
						updateTotals();
					}
					return true;
				}
				if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK
						&& column == 7) {
					taxCodeCombo.setComboItem(FinanceApplication.getCompany()
							.getTAXCode(core.getTaxCode()));
					return true;
				}
				return true;
			}
		});
	}

	protected void applyPriceLevel(ClientTransactionItem item) {
		priceLevelSelected(priceLevel);
		selectedRecord = item;
		if (priceLevel != null)
			setUnitPriceForSelectedItem(FinanceApplication.getCompany()
					.getItem(item.getItem()));
	}

	@Override
	public void setCustomerTaxCode(ClientTransactionItem selectedObject) {
		List<ClientTAXCode> taxCodes = FinanceApplication.getCompany()
				.getActiveTaxCodes();
		for (ClientTAXCode taxCode : taxCodes) {
			if (taxCode.getName().equals("S")) {
				ztaxCodeStringId = taxCode.getStringID();
			}
		}
		if (transactionView.getTransactionObject() == null) {
			if (transactionView.customer != null)
				selectedObject
						.setTaxCode(selectedObject.getTaxCode() != null ? selectedObject
								.getTaxCode()
								: transactionView.vendor.getTAXCode() != null ? transactionView.vendor
										.getTAXCode()
										: ztaxCodeStringId);
		}
		updateTotals();
		updateData(selectedObject);
	}

	@Override
	public void updateData(ClientTransactionItem obj) {
		super.updateData(obj);
	}

	@Override
	public void updateRecord(ClientTransactionItem obj, int row, int col) {
		super.updateRecord(obj, row, col);
	}

	@Override
	protected Object getColumnValue(ClientTransactionItem item, int index) {
		return null;

		// if (item.getType() == ClientTransactionItem.TYPE_COMMENT) {
		// if (!Arrays.asList(0, 2, 8).contains(index))
		// return "";
		// }
		// // if (item.getType() == ClientTransactionItem.TYPE_ACCOUNT) {
		// // if (Arrays.asList(3).contains(index))
		// // return "";
		// // }
		//
		// if (item.getType() == ClientTransactionItem.TYPE_SALESTAX) {
		// if (!Arrays.asList(0, 1, 2, 6, 8).contains(index))
		// return "";
		// }
		// switch (index) {
		// case 0:
		// return getImageByType(item.getType());
		// case 1:
		// return getNameValue(item);
		// case 2:
		// return item.getDescription();
		// case 3:
		// return item.getQuantity();
		// case 4:
		// return DataUtils.getAmountAsString(item.getUnitPrice());
		// case 5:
		// return DataUtils.getNumberAsPercentString(item.getDiscount() + "");
		// case 6:
		// return DataUtils.getAmountAsString(item.getLineTotal());
		// case 7:
		// return item.isTaxable() ? FinanceApplication.getCustomersMessages()
		// .taxable() : FinanceApplication.getCustomersMessages()
		// .nonTaxable();
		// case 8:
		// return FinanceApplication.getFinanceMenuImages().delete();
		// // return "/images/delete.png";
		// default:
		// return "";
		// }
	}

	protected String getImageByType(int type) {
		switch (type) {
		case TYPE_ITEM:
			// return
			// FinanceApplication.getFinanceMenuImages().items().getURL();
			return "/images/items.png";
		case TYPE_SERVICE:
			// return FinanceApplication.getFinanceMenuImages().salestax()
			// .getURL();
			return "/images/salestax.png";
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
		default:
			break;
		}
		return "";
	}

	protected String getNameValue(ClientTransactionItem item) {
		switch (item.getType()) {
		// FIXME--need to check for default selection values(eg:- a combo
		// should
		// have a default value selected.)
		case TYPE_ITEM:
			ClientItem itm = FinanceApplication.getCompany().getItem(
					item.getItem());
			return itm != null ? itm.getName() : "";
		case TYPE_SERVICE:
			ClientItem itm1 = FinanceApplication.getCompany().getItem(
					item.getItem());
			return itm1 != null ? itm1.getName() : "";
		case TYPE_ACCOUNT:

			ClientAccount account = FinanceApplication.getCompany().getAccount(
					item.getAccount());
			return account != null ? account.getDisplayName() : "";
		case TYPE_COMMENT:
			return item.getDescription() != null ? item.getDescription() : "";
		case TYPE_SALESTAX:
			if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
				// ClientTaxCode taxCode = FinanceApplication.getCompany()
				// .getTaxCode(item.getTaxCode());
				// return taxCode != null ? taxCode.getName() : "";
			} else {
				ClientTAXItem vatItem = FinanceApplication.getCompany()
						.getTaxItem(item.getVatItem());
				return vatItem != null ? vatItem.getName() : "";
			}
		default:
			break;
		}
		return "";
	}

	protected void setUnitPriceForSelectedItem(ClientItem selectedItem) {

		ClientTransactionItem record = selectedRecord;

		if (record == null)
			return;
		Double calculatedUnitPrice = Utility
				.getCalculatedItemUnitPriceForPriceLevel(selectedItem,
						priceLevel, false);

		if (calculatedUnitPrice == null)
			return;

		@SuppressWarnings("unused")
		int index = getSelectedRecordIndex();

		int col = 4;
		record.setItem(selectedItem.getStringID());

		record.setUnitPrice(calculatedUnitPrice);

		record.setDescription(selectedItem.getName());

		stopEditing(col);

		if (FinanceApplication.getCompany().getAccountingType() == 1) {
			// if (selectedItem.getVatCode() != null) {
			// record.setTaxCode(selectedItem.getVatCode());
			// refreshVatValue(record);
			// }
		} else {
			record.setTaxable(selectedItem.isTaxable());

		}
		double lt = record.getQuantity() * record.getUnitPrice();
		double disc = record.getDiscount();
		record.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt
				* disc / 100)) : lt);
		updateData(record);
		// startEditing(index);

	}

	@SuppressWarnings("unchecked")
	public void updateTotals() {

		List<ClientTransactionItem> allrecords = (List<ClientTransactionItem>) (ArrayList) getRecords();
		int totaldiscount = 0;
		totallinetotal = 0.0;
		taxableTotal = 0.0;
		totalVat = 0.0;
		int accountType = FinanceApplication.getCompany().getAccountingType();
		for (ClientTransactionItem citem : allrecords) {

			totaldiscount += citem.getDiscount();

			Double lineTotalAmt = citem.getLineTotal();
			totallinetotal += lineTotalAmt;
			if (accountType == ClientCompany.ACCOUNTING_TYPE_US) {
				if (citem != null && citem.isTaxable())
					taxableTotal += lineTotalAmt;
			}
			totalVat += citem.getVATfraction();
		}

		if (accountType == ClientCompany.ACCOUNTING_TYPE_US)
			grandTotal = totalVat + totallinetotal;
		else {
			// if (transactionView.vatinclusiveCheck != null
			// && (Boolean) transactionView.vatinclusiveCheck.getValue()) {
			// grandTotal = totallinetotal - totalVat;
			// setTotalValue(totallinetotal);
			//
			// } else {
			grandTotal = totallinetotal;
			totalValue = grandTotal + totalVat;
			// }
		}

		transactionView.updateNonEditableItems();

		// if (!isBankingTransaction)
		// this.updateFooterValues("Discount: " + totaldiscount + "%", 5);
		// this.updateFooterValues(DataUtils.getAmountAsString(totallinetotal),
		// 6);
		//
		// if (FinanceApplication.getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_UK) {
		// if (!isBankingTransaction) {
		// this.updateFooterValues(DataUtils.getAmountAsString(totalVat),
		// 8);
		// }
		// }
	}

	public Map<Integer, Map<String, String>> getVATDetailsMapForPrinting() {
		Map<Integer, Map<String, String>> vatMap = new HashMap<Integer, Map<String, String>>();
		Map<String, String> vat = new HashMap<String, String>();
		;
		double totalVATAmount = 0.0;
		int r = 0;
		for (ClientTransactionItem rec : getRecords()) {
			if (rec.getTaxCode() != null) {
				vat = new HashMap<String, String>();
				String taxCodeWidRate = getTAXCodeName(rec.getTaxCode())
						+ "@"
						+ DataUtils.getNumberAsPercentString(getVATRate(rec)
								+ "");
				double vatAmount = getVATAmount(rec.getTaxCode(), rec);
				totalVATAmount += vatAmount;
				vat.put(taxCodeWidRate, DataUtils.getAmountAsString(vatAmount));
				vatMap.put(r++, vat);
			}
		}
		vat = new HashMap<String, String>();
		vat.put("Total VAT:", DataUtils.getAmountAsString(totalVATAmount));
		vatMap.put(r++, vat);
		return vatMap;
	}

	public double getTotalVATAmountForPrinting() {
		double vatAmount = 0.0;
		for (ClientTransactionItem rec : getRecords()) {
			if (rec.getTaxCode() != null) {
				vatAmount += getVATAmount(rec.getTaxCode(), rec);
			}
		}
		return vatAmount;
	}

	public double getVATRate(ClientTransactionItem rec) {
		double vatRate = 0.0;

		String TAXCodeID = rec.getTaxCode();
		if (TAXCodeID != null && TAXCodeID.length() != 0) {
			// Checking the selected object is VATItem or VATGroup.
			// If it is VATItem,the we should get 'VATRate',otherwise
			// 'GroupRate
			try {
				if (FinanceApplication.getCompany().getTAXItemGroup(
						FinanceApplication.getCompany().getTAXCode(TAXCodeID)
								.getTAXItemGrpForSales()) instanceof ClientTAXItem) {
					// The selected one is VATItem,so get 'VATRate' from
					// 'VATItem'
					vatRate = ((ClientTAXItem) FinanceApplication.getCompany()
							.getTAXItemGroup(
									FinanceApplication.getCompany().getTAXCode(
											TAXCodeID).getTAXItemGrpForSales()))
							.getTaxRate();
				} else {
					// The selected one is VATGroup,so get 'GroupRate' from
					// 'VATGroup'
					vatRate = ((ClientTAXGroup) FinanceApplication.getCompany()
							.getTAXItemGroup(
									FinanceApplication.getCompany().getTAXCode(
											TAXCodeID).getTAXItemGrpForSales()))
							.getGroupRate();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return vatRate;
	}

	public List<ClientTransactionItem> getallTransactions(
			ClientTransaction object) throws InvalidEntryException {

		return getRecords();
	}

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
				if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
					combo = getCustomCombo(transactionItems.get(i), 7);
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
		if (!(this instanceof SalesOrderUKGrid || this instanceof SalesOrderUSGrid)) {
			for (ClientTransactionItem item : transactionItems) {
				item.setStringID("");
			}
		}
		addRecords(transactionItems);
		updateTotals();

	}

	protected String getTAXCodeName(String taxCode) {
		ClientTAXCode t = null;
		if (taxCode != null)
			t = FinanceApplication.getCompany().getTAXCode(taxCode);
		return t != null ? t.getName() : "";
	}

	public Double getTotal() {
		return totallinetotal != null ? totallinetotal.doubleValue() : 0.0d;
	}

	public void priceLevelSelected(ClientPriceLevel priceLevel) {
		this.priceLevel = priceLevel;

	}

	public void updatePriceLevel() {
		for (ClientTransactionItem item : getRecords()) {
			if (item.getType() == ClientTransactionItem.TYPE_ITEM) {
				selectedRecord = item;
				if (priceLevel != null)
					setUnitPriceForSelectedItem(FinanceApplication.getCompany()
							.getItem(item.getItem()));

			}
		}

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

	public Double getGrandTotal() {
		return grandTotal;
	}

	public void refreshVatValue(ClientTransactionItem obj) {
		ClientTransactionItem record = (ClientTransactionItem) obj;

		String taxCode = getTaxCode(obj);
		if (taxCode == null)
			return;
		record.setVATfraction(getVATAmount(taxCode, record));
		updateTotals();
		updateData(record);
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
		refreshVatValue();
	}

	public double getVATAmount(String TAXCodeID, ClientTransactionItem record) {

		double vatRate = 0.0;
		if (TAXCodeID != null && TAXCodeID.length() != 0) {
			// Checking the selected object is VATItem or VATGroup.
			// If it is VATItem,the we should get 'VATRate',otherwise 'GroupRate
			try {
				ClientTAXItemGroup item = FinanceApplication.getCompany()
						.getTAXItemGroup(
								FinanceApplication.getCompany().getTAXCode(
										TAXCodeID).getTAXItemGrpForSales());
				if (item == null) {
					vatRate = 0.0;
				} else if (item instanceof ClientTAXItem) {
					// The selected one is VATItem,so get 'VATRate' from
					// 'VATItem'
					vatRate = ((ClientTAXItem) item).getTaxRate();
				} else {
					// The selected one is VATGroup,so get 'GroupRate' from
					// 'VATGroup'
					vatRate = ((ClientTAXGroup) item).getGroupRate();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
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

	public void refreshVatValue() {
		List<ClientTransactionItem> allrecords = getRecords();
		for (ClientTransactionItem record : allrecords) {
			// if (record.getTaxCode() != null)
			refreshVatValue(record);
		}
		updateTotals();

	}

	@Override
	public void editComplete(ClientTransactionItem item, Object value, int col) {
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
				Double quantity = Double.parseDouble(DataUtils
						.getReformatedAmount(qty)
						+ "");
				if (quantity == 0) {
					quantity = 1D;
					qty = "1";
				}
				try {
					if (!AccounterValidator.validateGridQuantity(quantity)) {
						item.setQuantity(Double.parseDouble(qty));
						update_quantity_inAllRecords(item.getQuantity());
					} else
						item.setQuantity(isItem ? 1D : 0);
				} catch (InvalidTransactionEntryException e) {
					e.printStackTrace();
				}

				// FIXME need to implement warnings
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
				Double d = DataUtils.getReformatedAmount(unitPriceString);// Double.parseDouble(unitPriceString);

				if (!AccounterValidator.validateGridUnitPrice(d)) {
					item.setUnitPrice(d);
				} else {
					d = 0.0D;
					item.setUnitPrice(d);
				}

				break;
			case 5:
				String discount = value.toString();

				// if (discount.contains("%")) {
				// discount = discount.replaceAll("%", "");
				// }
				// discount = discount.replaceAll(",", "");
				Double discountRate = Double.parseDouble(DataUtils
						.getReformatedAmount(discount)
						+ "");
				try {
					if (!AccounterValidator.validateNagtiveAmount(discountRate)) {
						item.setUnitPrice(0.0D);
						discountRate = 0.0D;
					}
				} catch (InvalidEntryException e) {
					e.printStackTrace();
				}
				if (!DecimalUtil.isGreaterThan(discountRate, 100))
					item.setDiscount(discountRate);
				break;
			case 6:
				String lineTotalAmtString = value.toString();
				// if (lineTotalAmtString.contains(""
				// + UIUtils.getCurrencySymbol() + "")) {
				// lineTotalAmtString = lineTotalAmtString.replaceAll(""
				// + UIUtils.getCurrencySymbol() + "", "");
				// }
				if (item.getType() == TYPE_SALESTAX
						|| item.getType() == TYPE_SERVICE
						|| item.getType() == TYPE_ACCOUNT) {
					// lineTotalAmtString =
					// lineTotalAmtString.replaceAll(",",
					// "");
					Double lineTotal = Double.parseDouble(DataUtils
							.getReformatedAmount(lineTotalAmtString)
							+ "");
					try {
						if ((!AccounterValidator
								.validateGridLineTotal(lineTotal))
								&& (!AccounterValidator
										.isAmountTooLarge(lineTotal))) {
							item.setLineTotal(lineTotal);
							item.setUnitPrice(isItem ? lineTotal : 0.0D);
							item.setQuantity(isItem ? 1D : 0.0D);
						}
					} catch (Exception e) {

						if (e instanceof InvalidEntryException) {
							item.setLineTotal(0.0D);
							item.setUnitPrice(0.0D);
							item.setQuantity(isItem ? 1D : 0.0D);
							Accounter.showError(e.getMessage());

						}
					}
				}

				break;
			case 7:
				if (value.equals("Taxable")) {
					item.setTaxable(true);
				} else
					item.setTaxable(false);
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
				&& !FinanceApplication.getCompany().getpreferences()
						.getDoYouPaySalesTax()) {
			if (item.getType() == TYPE_SERVICE
					|| item.getType() == TYPE_ACCOUNT
					|| item.getType() == TYPE_ITEM) {
				if (ztaxCodeStringId != null)
					item.setTaxCode(ztaxCodeStringId);
				else {
					List<ClientTAXCode> taxCodes = FinanceApplication
							.getCompany().getActiveTaxCodes();
					for (ClientTAXCode taxCode : taxCodes) {
						if (taxCode.getName().equals("Z")) {
							ztaxCodeStringId = taxCode.getStringID();
						}
					}
					if (ztaxCodeStringId != null)
						item.setTaxCode(ztaxCodeStringId);
				}

			}
		}
		if (item.getType() != TYPE_SALESTAX && col != 6) {
			double lt = item.getQuantity() * item.getUnitPrice();
			double disc = item.getDiscount();
			item.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt
					* disc / 100)) : lt);
		}
		updateTotals();
		updateData(item);
		// if (FinanceApplication.getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_UK) {
		if (Arrays.asList(3, 4, 5, 6).contains(col))
			refreshVatValue(item);
		// }
	}

	@Override
	protected void onValueChange(ClientTransactionItem item, int index,
			Object value) {
		if (index == 1) {
			int type = item.getType();
			switch (type) {
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
	protected boolean isEditable(ClientTransactionItem obj, int row, int col) {
		if (obj == null)
			return false;
		if (obj.getType() == TYPE_SERVICE
				&& !FinanceApplication.getCompany().getpreferences()
						.getDoYouPaySalesTax()) {
			if (col == 7 || col == 8)
				return false;
		}
		switch (obj.getType()) {
		case ClientTransactionItem.TYPE_ACCOUNT:
			switch (col) {
			// case 3:
			// return false;
			// case 4:
			// return false;
			// case 5:
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
			case 6:
				return false;
			default:
				return true;
			}
		case ClientTransactionItem.TYPE_SALESTAX:
			switch (col) {
			case 1:
				return true;
			case 2:
				return true;
			case 6:
				return true;
			default:
				return false;
			}
		case ClientTransactionItem.TYPE_SERVICE:

			switch (col) {
			case 1:
				return true;
			case 2:
				return true;
			case 3:
				return true;
			case 4:
				return true;
			case 5:
				return true;
			case 6:
				return true;
			case 7:
				return true;
			default:
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <E> CustomCombo<E> getCustomCombo(ClientTransactionItem obj,
			int colIndex) {
		switch (colIndex) {
		case 1:
			if (obj.getType() == ClientTransactionItem.TYPE_ACCOUNT) {
				return (CustomCombo<E>) accountsCombo;

			} else if (obj.getType() == ClientTransactionItem.TYPE_ITEM) {
				return (CustomCombo<E>) productItemCombo;
			} else if (obj.getType() == ClientTransactionItem.TYPE_SERVICE) {
				return (CustomCombo<E>) serviceItemCombo;
			} else if (obj.getType() == ClientTransactionItem.TYPE_SALESTAX) {
				if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
					// return (CustomCombo<E>) salesTaxCombo;
				} else
					return (CustomCombo<E>) vatItemCombo;
			}
			break;
		case 7:
			return (CustomCombo<E>) taxCodeCombo;
		default:
			break;
		}

		return null;
	}

	public void setTotalValue(Double totalValue) {
		this.totalValue = totalValue;
	}

	public Double getTotalValue() {
		return totalValue;
	}

	@Override
	public boolean validateGrid() throws InvalidTransactionEntryException {
		int validationcount = 1;
		for (ClientTransactionItem item : this.getRecords()) {
			if (item.getType() != ClientTransactionItem.TYPE_COMMENT) {
				switch (validationcount++) {
				case 1:
					AccounterValidator.validateGridItem(this.getColumnValue(
							item, 1), UIUtils.getTransactionTypeName(item
							.getType()));
				case 2:
					if (accountingType == ClientCompany.ACCOUNTING_TYPE_UK
							&& item.getType() != ClientTransactionItem.TYPE_SALESTAX) {
						AccounterValidator.validateGridItem(this
								.getColumnValue(item, 7), "Vat Code");
						validationcount = 1;
					} else
						validationcount = 1;
					break;
				default:
					break;
				}

			}
		}
		// if (DecimalUtil.isLessThan(totallinetotal, 0.0)) {
		// Accounter.showError(AccounterErrorType.InvalidTransactionAmount);
		// return false;
		// }
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
	protected String[] getColumns() {
		// TODO Auto-generated method stub
		return null;
	}

	protected void createVATItemAndTaxCodeCombo() {

		vatItemCombo = new VATItemCombo(FinanceApplication.getVATMessages()
				.VATItem(), isAddNewRequired);
		vatItemCombo.initCombo(getVatItems());
		vatItemCombo.setGrid(this);
		vatItemCombo.setRequired(true);
		vatItemCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXItem>() {

					@Override
					public void selectedComboBoxItem(ClientTAXItem selectItem) {
						if (selectItem != null) {
							if (!isPreviuslyUsed(selectItem)) {
								Accounter
										.showError("The VATItem selected is already used in VAT column.Please select a different VATItem");
							}
							selectedObject.setVatItem(selectItem.getStringID());
							setText(currentRow, currentCol, selectItem
									.getName());
						}
					}
				});

		taxCodeCombo = new TAXCodeCombo(FinanceApplication.getVATMessages()
				.vatCode(), isAddNewRequired, true);
		taxCodeCombo.setGrid(this);
		taxCodeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXCode>() {

					@Override
					public void selectedComboBoxItem(ClientTAXCode selectItem) {
						if (selectItem != null) {

							selectedObject.setTaxCode(selectItem.getStringID());
							if (selectedObject.getType() == TYPE_ACCOUNT)
								editComplete(selectedObject, selectedObject
										.getLineTotal(), 6);
							else

								editComplete(selectedObject, selectedObject
										.getUnitPrice(), 4);
						} else
							selectedObject.setTaxCode(null);
					}
				});

	}

	private List<ClientTAXItem> getVatItems() {
		List<ClientTAXItem> customerVATItems = new ArrayList<ClientTAXItem>();
		for (ClientTAXItem vatItem : FinanceApplication.getCompany()
				.getActiveTaxItems()) {
			if (vatItem.isSalesType())
				customerVATItems.add(vatItem);
		}
		return customerVATItems;
	}

	public boolean isPreviuslyUsed(ClientTAXItem selectedVATItem) {
		for (ClientTransactionItem rec : getRecords()) {
			if (rec.getTaxCode() != null && rec.getTaxCode().length() != 0) {
				String vatItem = FinanceApplication.getCompany().getTAXCode(
						rec.getTaxCode()).getTAXItemGrpForSales();
				if (selectedVATItem.getStringID().equals(vatItem)) {
					return false;
				}
			}
		}
		return true;
	}

	public abstract String getTaxCode(ClientTransactionItem item);
}
