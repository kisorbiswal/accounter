//package com.vimukti.accounter.web.client.ui.core;
//
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//
//import com.google.gwt.event.dom.client.ChangeEvent;
//import com.google.gwt.event.dom.client.ChangeHandler;
//import com.google.gwt.layout.client.Layout.Alignment;
//import com.google.gwt.user.client.rpc.AccounterAsyncCallback;
//import com.google.gwt.user.client.rpc.IsSerializable;
//import com.vimukti.accounter.web.client.core.ClientAccount;
//import com.vimukti.accounter.web.client.core.ClientItem;
//import com.vimukti.accounter.web.client.core.ClientPriceLevel;
//import com.vimukti.accounter.web.client.core.ClientTaxCode;
//import com.vimukti.accounter.web.client.core.ClientTransaction;
//import com.vimukti.accounter.web.client.core.ClientTransactionItem;
//import com.vimukti.accounter.web.client.core.IAccounterCore;
//import com.vimukti.accounter.web.client.core.RecordDoubleClickHandler;
//import com.vimukti.accounter.web.client.core.Utility;
//import com.vimukti.accounter.web.client.ui.DataUtils;
//import com.vimukti.accounter.web.client.ui.FinanceApplication;
//import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
//import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
//import com.vimukti.accounter.web.client.ui.combo.SelectItemType;
//import com.vimukti.accounter.web.client.ui.combo.CustomCombo.Filter;
//import com.vimukti.accounter.web.client.ui.forms.ComboBoxItem;
//import com.vimukti.accounter.web.client.ui.forms.TextItem;
//
///**
// * 
// * This view represents the Transaction Grid available in Select Customers,
// * Vendors, and Banking Transactions i.e All Transactions Related to Products
// * And Services(Customers), Items And Expenses of Vendors, Banking.
// * 
// * @author Fernandez
// * @implementation Fernandez
// * 
// */
//public class TransactionListGrid extends ListGridView {
//
//	private ListGridField nameField;
//
//	private CustomCombo accountsCombo, salesTaxCombo, itemCombo, vatCodeCombo;
//	private ComboBoxItem taxSelectItem;
//	private TextItem descrptextItem, vatTextItem;
//	private ListGridField descpField;
//	private ListGridField qtyField;
//	private TextItem unitpriceTextItem;
//	private ListGridField unitPriceField;
//	private ListGridField discuntField;
//	private ListGridField linetotalField;
//	private ListGridField taxField;
//	private ListGridField vatCodeField, vatField;
//	private TextItem discountTextItem;
//	private TextItem linetotalTextItem;
//	protected ListGridRecord currentRecord;
//	private Double totallinetotal;
//	private Double totalVat;
//	private Double grandTotal;
//	// private ItemTax selectedItemTax;
//	private ClientPriceLevel priceLevel;
//	// protected List<ClientItemTax> itemtaxList;
//	private List<ClientAccount> gridAccounts;
//	private double taxableTotal;
//	private AbstractTransactionBaseView transactionView;
//	private boolean isCustomerTransaction;
//	private boolean isBankingTransaction;
//	private boolean isVendorTransaction;
//
//	public TransactionListGrid(AbstractTransactionBaseView transactionView,
//			int transactionDomain) {
//		super(transactionDomain);
//
//		if (transactionView == null)
//			return;
//
//		isCustomerTransaction = transactionDomain == CUSTOMER_TRANSACTION;
//
//		isBankingTransaction = transactionDomain == BANKING_TRANSACTION;
//
//		isVendorTransaction = transactionDomain == VENDOR_TRANSACTION;
//
//		this.transactionView = transactionView;
//
//		createControls();
//
//		initTransactionData();
//
//		ClientTransaction transactionObject = transactionView
//				.getTransactionObject();
//
//		if (transactionObject != null) {
//			setAllTransactions(transactionObject.getTransactionItems());
//			if (transactionObject.getID() != 0) {
//				// ITS Edit Mode
//				setEnableMenu(false);
//				isEdit = true;
//				setIsDeleteEnable(false);
//				getGrid().setEditEvent(ListGridEditEvent.NONE);
//				setEditDisableCells(descpField, qtyField, unitPriceField,
//						discuntField, linetotalField, taxField);
//
//			}
//		}
//	}
//
//	private void getPayFromAccounts() {
//		gridAccounts = new ArrayList<ClientAccount>();
//		for (ClientAccount account : FinanceApplication.getCompany()
//				.getAccounts()) {
//			if (account.getType() != ClientAccount.TYPE_CASH
//					&& account.getType() != ClientAccount.TYPE_BANK
//					&& account.getType() != ClientAccount.TYPE_INVENTORY_ASSET
//					&& account.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
//					&& account.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE) {
//				gridAccounts.add(account);
//			}
//		}
//		accountsCombo.initCombo(gridAccounts);
//	}
//
//	private void initTransactionData() {
//
//		List<ClientItem> result = FinanceApplication.getCompany().getItems();
//		if (isCustomerTransaction || isBankingTransaction) {
//			List<ClientItem> customerItems = new ArrayList<ClientItem>();
//			for (ClientItem item : result) {
//				if (item.isISellThisItem())
//					customerItems.add(item);
//			}
//			itemCombo.initCombo(customerItems);
//		} else if (isVendorTransaction) {
//			List<ClientItem> vendorItems = new ArrayList<ClientItem>();
//			for (ClientItem item : result) {
//				if (item.isIBuyThisItem())
//					vendorItems.add(item);
//			}
//			itemCombo.initCombo(vendorItems);
//		}
//
//		nameField.setEditorType(itemCombo);
//
//		List<ClientTaxCode> listTaxCodes = FinanceApplication.getCompany()
//				.getTaxcodes();
//		salesTaxCombo.initCombo(listTaxCodes);
//		nameField.setEditorType(salesTaxCombo);
//
//		AccounterAsyncCallback<List<ClientTaxCode>> taxcodesCallBack = new AccounterAsyncCallback<List<ClientTaxCode>>() {
//
//			public void onException(AccounterException caught) {
//				Accounter.showError("Failed to Get TaxCode List...");
//			}
//
//			public void onSuccess(List<ClientTaxCode> result) {
//				if (FinanceApplication.getCompany().getAccountingType() == 1) {
//					vatCodeCombo.initCombo(result);
//					vatCodeField.setEditorType(vatCodeCombo);
//				} else {
//					salesTaxCombo.initCombo(result);
//					nameField.setEditorType(salesTaxCombo);
//				}
//
//			}
//
//		};
//
//		FinanceApplication.createGETService().getTaxCodes(taxcodesCallBack);
//		getPayFromAccounts();
//	}
//
//	private void createControls() {
//		createMenu();
//		nameField = new ListGridField(TransactionItemRecord.ATTR_NAME, "Name",
//				100);
//
//		itemCombo = new CustomCombo("Item", SelectItemType.ITEM, true);
//		// itemCombo.setName("name");
//		itemCombo.setPickListWidth(200);
//
//		itemCombo
//				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler() {
//
//					public void selectedComboBoxItem(IsSerializable selectItem) {
//						if (selectItem != null)
//							setUnitPriceForSelectedItem((ClientItem) selectItem);
//
//					}
//				});
//
//		itemCombo.addFilter(new Filter() {
//
//			@Override
//			public boolean canAdd(IAccounterCore core) {
//				ClientItem item = (ClientItem) core;
//
//				if (item == null)
//					return false;
//				if ((isCustomerTransaction || isBankingTransaction)
//						&& !item.isISellThisItem()) {
//
//					Accounter
//							.showInformation(AccounterErrorType.cannotUsePurchaseItem);
//					return false;
//
//				} else if (isVendorTransaction && !item.isIBuyThisItem()) {
//					Accounter
//							.showInformation(AccounterErrorType.cannotUseSalesItem);
//					return false;
//				}
//				return true;
//
//				// return isCustomerTransaction ? item.isISellThisItem() : item
//				// .isIBuyThisItem();
//			}
//
//		});
//
//		accountsCombo = new CustomCombo("Accounts", SelectItemType.ACCOUNT,
//				true);
//
//		// accountsCombo.initCombo(gridAccounts);
//
//		// accountsCombo.setName("name");
//		accountsCombo.setPickListWidth(200);
//		// accountsCombo.setAddUnknownValues(false);
//		accountsCombo
//				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler() {
//
//					public void selectedComboBoxItem(IsSerializable selectItem) {
//						if (selectItem == null)
//							return;
//
//						TransactionItemRecord record = (TransactionItemRecord) getGrid()
//								.getSelectedRecord();
//
//						record.setAccount((ClientAccount) selectItem);
//
//						record.setItemTax("Non-Taxable");
//
//						record.refresh();
//
//					}
//				});
//		nameField.setEditorType(accountsCombo);
//
//		salesTaxCombo = new CustomCombo("Sales Tax", SelectItemType.TAX_CODE,
//				true);
//		salesTaxCombo.setPickListWidth(200);
//		salesTaxCombo
//				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler() {
//
//					public void selectedComboBoxItem(IsSerializable selectItem) {
//						if (selectItem == null)
//							return;
//
//						TransactionItemRecord record = (TransactionItemRecord) getGrid()
//								.getSelectedRecord();
//
//						record.setTaxCode((ClientTaxCode) selectItem);
//
//					}
//				});
//
//		nameField.setEditorType(itemCombo);
//		nameField.setRequired(true);
//
//		descrptextItem = new TextItem();
//		descrptextItem.setWidth("20%");
//		descpField = new ListGridField(TransactionItemRecord.ATTR_DISCRIPTION,
//				"Description", 175);
//		descpField.addEditorExitHandler(new EditorExitHandler() {
//
//			public void onEditorExit(EditorExitEvent event) {
//
//				if (event.getNewValue() != null) {
//
//					TransactionItemRecord record = (TransactionItemRecord) event
//							.getRecord();
//
//					record.setDescription(event.getNewValue().toString());
//
//				}
//
//			}
//
//		});
//
//		descpField.setEditorType(descrptextItem);
//
//		qtyField = new ListGridField(TransactionItemRecord.ATTR_QTY, "Qty", 100);
//		qtyField.setEditorType(new SpinnerItem());
//		qtyField.setType(ListGridFieldType.INTEGER);
//		qtyField.setRequired(true);
//
//		qtyField.setValidateOnChange(true);
//
//		qtyField.addEditorExitHandler(new EditorExitHandler() {
//
//			public void onEditorExit(EditorExitEvent event) {
//
//				if (event.getNewValue() != null) {
//
//					try {
//
//						Double d = Double.parseDouble(event.getNewValue()
//								.toString());
//
//						TransactionItemRecord record = (TransactionItemRecord) event
//								.getRecord();
//
//						record.setQuantity(d);
//						refreshVatValue(record);
//
//					} catch (Exception e) {
//
//						Accounter
//								.showError(AccounterErrorType.INCORRECTINFORMATION
//										+ "At" + qtyField.getName());
//						event.cancel();
//					}
//
//				}
//			}
//
//		});
//
//		unitpriceTextItem = new TextItem();
//		unitpriceTextItem.setName("unitPrice");
//
//		unitPriceField = new ListGridField(
//				TransactionItemRecord.ATTR_UNITPRICE, "Unit Price", 225);
//		unitPriceField.setEditorType(unitpriceTextItem);
//		unitPriceField.setAttribute("unitPrice", "0");
//		unitPriceField.setValidateOnChange(true);
//		unitPriceField.setRequired(true);
//		unitPriceField.setType(ListGridFieldType.FLOAT);
//		unitPriceField.addEditorExitHandler(new EditorExitHandler() {
//
//			public void onEditorExit(EditorExitEvent event) {
//
//				if (event.getNewValue() != null) {
//
//					try {
//
//						String unitPriceString = event.getNewValue().toString();
//
//						if (unitPriceString.startsWith(""+UIUtils.getCurrencySymbol() +"")) {
//							unitPriceString = unitPriceString.replaceAll(""+UIUtils.getCurrencySymbol() +"",
//									"");
//						}
//
//						Double d = Double.parseDouble(unitPriceString);
//						if (!AccounterValidator.validateNagtiveAmount(d)) {
//							unitpriceTextItem.setValue(""+UIUtils.getCurrencySymbol() +"0.0");
//							d = 0.0D;
//						}
//
//						TransactionItemRecord record = (TransactionItemRecord) event
//								.getRecord();
//
//						priceLevelSelected(priceLevel);
//
//						record.setUnitPrice(d);
//
//					} catch (Exception e) {
//
//						Accounter.showError("Incorrect Information Entered!");
//						event.cancel();
//					}
//
//				}
//			}
//
//		});
//
//		discountTextItem = new TextItem();
//		discountTextItem.setName("discount");
//
//		discuntField = new ListGridField(TransactionItemRecord.ATTR_DISCOUNT,
//				"Discount", 225);
//		discuntField.setAttribute("discount", "0");
//		discuntField.setEditorType(discountTextItem);
//		discuntField.setType(ListGridFieldType.FLOAT);
//		discuntField.setValidateOnChange(true);
//		discuntField.setRequired(true);
//		discuntField.addEditorExitHandler(new EditorExitHandler() {
//
//			public void onEditorExit(EditorExitEvent event) {
//
//				if (event.getNewValue() != null) {
//
//					try {
//						String discount = event.getNewValue().toString();
//
//						if (discount.contains("%")) {
//							discount = discount.replaceAll("%", "");
//						}
//
//						Double discountRate = Double.parseDouble(discount);
//						if (!AccounterValidator
//								.validateNagtiveAmount(discountRate)) {
//							unitpriceTextItem.setValue(""+UIUtils.getCurrencySymbol() +"0.0");
//							discountRate = 0.0D;
//						}
//
//						TransactionItemRecord record = (TransactionItemRecord) event
//								.getRecord();
//
//						record.setDiscount(discountRate);
//
//					} catch (Exception e) {
//						Accounter.showError("Incorrect Information Entered!");
//						e.printStackTrace();
//						event.cancel();
//					}
//				}
//
//			}
//
//		});
//
//		linetotalTextItem = new TextItem();
//		linetotalTextItem.setName("lineTotal");
//
//		linetotalField = new ListGridField(
//				TransactionItemRecord.ATTR_LINETOTAL, "Line Total", 225);
//		linetotalField.setEditorType(linetotalTextItem);
//		linetotalField.setCanEdit(false);
//		linetotalField.setValidateOnChange(true);
////		linetotalField.setAlign(Alignment.RIGHT);
//		linetotalField.addEditorExitHandler(new EditorExitHandler() {
//
//			public void onEditorExit(EditorExitEvent event) {
//
//				if (event.getNewValue() != null) {
//
//					try {
//						String lineTotalAmtString = event.getNewValue()
//								.toString();
//
//						if (lineTotalAmtString.contains(""+UIUtils.getCurrencySymbol() +"")) {
//							lineTotalAmtString = lineTotalAmtString.replaceAll(
//									""+UIUtils.getCurrencySymbol() +"", "");
//						}
//
//						TransactionItemRecord record = (TransactionItemRecord) event
//								.getRecord();
//
//						if (record.getAttribute("type").equals(
//								TransactionItemRecord.TYPE_SALESTAX)) {
//
//							Double lineTotal = Double
//									.parseDouble(lineTotalAmtString);
//							AccounterValidator.validateGridLineTotal(lineTotal);
//
//							record.setLineTotal(lineTotal);
//
//						}
//
//					} catch (Exception e) {
//						e.printStackTrace();
//						Accounter.showError("Invalid Entry!");
//						event.cancel();
//					}
//
//				}
//
//			}
//
//		});
//
//		taxSelectItem = new ComboBoxItem();
//		taxSelectItem.setName(TransactionItemRecord.ATTR_ITEMTAX);
//		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
//		map.put("Taxable", "Taxable");
//		map.put("Non-Taxable", "Non-Taxable");
//		taxSelectItem.setValueMap(map);
//		taxSelectItem.addChangeHandler(new ChangeHandler() {
//
//			@Override
//			public void onChange(ChangeEvent event) {
//
//				TransactionItemRecord record = (TransactionItemRecord) getGrid()
//						.getSelectedRecord();
//
//				record.setItemTax((String) event.getValue());
//			}
//
//		});
//
//		taxField = new ListGridField(TransactionItemRecord.ATTR_ITEMTAX, "Tax",
//				225);
//		taxField.setEditorType(taxSelectItem);
//
//		addEditCompleteHandler(new EditCompleteHandler() {
//
//			public void onEditComplete(EditCompleteEvent event) {
//
//				TransactionItemRecord record = (TransactionItemRecord) getGrid()
//						.getRecord(event.getRowNum());
//
//				// record.refresh();
//
//				if (!record.isValid()) {
//					record.resetValues();
//				} else {
//					record.refresh();
//				}
//				// SC.say(String.valueOf(record.getAttribute("image")));
//				updateTotals();
//			}
//		});
//
//		vatCodeCombo = new CustomCombo("vatCode", SelectItemType.VAT_CODE, true);
//		// itemCombo.setName("name");
//		vatCodeCombo.setPickListWidth(200);
//
//		vatCodeCombo
//				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler() {
//
//					public void selectedComboBoxItem(IsSerializable selectItem) {
//						TransactionItemRecord record = (TransactionItemRecord) getGrid()
//								.getSelectedRecord();
//						record.setTaxCode((ClientTaxCode) selectItem);
//						refreshVatValue(record);
//
//					}
//				});
//		vatCodeField = new ListGridField(TransactionItemRecord.ATTR_VATCODE,
//				"VAT CODE", 225);
//		vatCodeField.setEditorType(vatCodeCombo);
//
//		addEditCompleteHandler(new EditCompleteHandler() {
//
//			public void onEditComplete(EditCompleteEvent event) {
//
//				TransactionItemRecord record = (TransactionItemRecord) getGrid()
//						.getRecord(event.getRowNum());
//
//				// record.refresh();
//
//				if (!record.isValid()) {
//					record.resetValues();
//				} else {
//					record.refresh();
//				}
//			}
//		});
//
//		vatTextItem = new TextItem();
//		vatTextItem.setName("VAT");
//		vatTextItem.setDisabled(true);
//		// vatTextItem.setShowDisabled(true);
//
//		vatField = new ListGridField(TransactionItemRecord.ATTR_VAT, "VAT", 225);
//		vatField.setEditorType(vatTextItem);
//		vatField.setCanEdit(false);
//		vatField.setValidateOnChange(true);
//		vatField.setAlign(Alignment.RIGHT);
//
//		getGrid().addEditorExitHandler(new EditorExitHandler() {
//
//			public void onEditorExit(EditorExitEvent event) {
//
//				TransactionItemRecord record = (TransactionItemRecord) getGrid()
//						.getRecord(event.getRowNum());
//
//				// record.refresh();
//
//				if (!record.isValid()) {
//					record.resetValues();
//				} else {
//					record.refresh();
//				}
//				// SC.say(String.valueOf(record.getAttribute("image")));
//				updateTotals();
//
//			}
//
//		});
//
//		addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
//
//			@Override
//			public void OnCellDoubleClick(ListGridRecord record) {
//				changeRecordType(record.getAttribute("type").toString());
//			}
//		});
//
//		if (isCustomerTransaction) {
//			if (FinanceApplication.getCompany().getAccountingType() == 1) {
//				addFields(nameField, descpField, qtyField, unitPriceField,
//						discuntField, linetotalField, vatCodeField, vatField);
//
//			} else {
//				addFields(nameField, descpField, qtyField, unitPriceField,
//						discuntField, linetotalField, taxField);
//			}
//		} else if (isBankingTransaction) {
//			addFields(nameField, descpField, qtyField, unitPriceField,
//					linetotalField);
//		} else {
//			if (FinanceApplication.getCompany().getAccountingType() == 1) {
//				addFields(nameField, descpField, qtyField, unitPriceField,
//						linetotalField, vatCodeField, vatField);
//			} else {
//				addFields(nameField, descpField, qtyField, unitPriceField,
//						linetotalField);
//			}
//		}
//		setDefaultValue("qty", "1");
//		setDefaultValue("unitPrice", ""+UIUtils.getCurrencySymbol() +"0.00");
//		setDefaultValue("lineTotal", ""+UIUtils.getCurrencySymbol() +"0.00");
//		setDefaultValue("discount", "0%");
//
//		setBottomLabelTitle("Sub Total:", 1);
//		if (isCustomerTransaction)
//			setBottomLabelTitle("Discount:", 5);
//		setBottomLabelTitle("LineTotal:", 6);
//
//		if (FinanceApplication.getCompany().getAccountingType() == 1) {
//			if (isCustomerTransaction || isVendorTransaction) {
//				setBottomLabelTitle("VAT: "
//						+ DataUtils.getAmountAsString(totalVat), 8);
//			}
//		}
//	}
//
//	protected void setUnitPriceForSelectedItem(ClientItem selectedItem) {
//
//		TransactionItemRecord record = (TransactionItemRecord) getSelectedRecord();
//
//		if (record == null)
//			return;
//
//		Double calculatedUnitPrice = Utility
//				.getCalculatedItemUnitPriceForPriceLevel(selectedItem,
//						priceLevel, isVendorTransaction);
//
//		if (calculatedUnitPrice == null)
//			return;
//
//		int index = getGrid().getRecordIndex(getSelectedRecord());
//
//		int col = 4;
//		record.setItem(selectedItem);
//
//		record.setUnitPrice(calculatedUnitPrice);
//
//		record.setDescription(selectedItem.getName());
//
//		getGrid().discardEdits(index, col, false);
//
//		if (FinanceApplication.getCompany().getAccountingType() == 1) {
//			if (selectedItem.getVatCode() != 0) {
//				record.setTaxCode(FinanceApplication.getCompany().getTaxCode(
//						selectedItem.getVatCode()));
//				refreshVatValue(record);
//			}
//		} else {
//
//			if (selectedItem.isTaxable())
//				record.setItemTax("Taxable");
//			else
//				record.setItemTax("Non-Taxable");
//
//		}
//
//		record.refresh();
//
//		// getGrid().startEditing(index, col, true);
//
//	}
//
//	public void updateTotals() {
//
//		ListGridRecord allrecords[] = getRecords();
//		int totaldiscount = 0;
//		totallinetotal = 0.0;
//		taxableTotal = 0.0;
//		totalVat = 0.0;
//		for (int i = 0; i < allrecords.length; i++) {
//			TransactionItemRecord rec = (TransactionItemRecord) allrecords[i];
//
//			String type = rec.getAttribute("type");
//
//			if (type == null || type.equals("none"))
//				continue;
//
//			totaldiscount += rec.getDiscount();
//
//			Double lineTotalAmt = rec.getLineTotal();
//			totallinetotal += lineTotalAmt;
//
//			ClientItem item = rec.getItem();
//			if (item != null && item.isTaxable()) {
//				taxableTotal += lineTotalAmt;
//			}
//			if (rec.getVat() != null)
//				totalVat += rec.getVat();
//		}
//
//		grandTotal = totalVat + totallinetotal;
//		transactionView.updateNonEditableItems();
//
//		if (isCustomerTransaction)
//			setBottomLabelTitle("Discount: " + totaldiscount + "%", 5);
//		setBottomLabelTitle("Total:"
//				+ DataUtils.getAmountAsString(totallinetotal), 6);
//		if (FinanceApplication.getCompany().getAccountingType() == 1) {
//			if (isCustomerTransaction || isVendorTransaction) {
//				setBottomLabelTitle("VAT: "
//						+ DataUtils.getAmountAsString(totalVat), 8);
//			}
//		}
//
//	}
//
//	/**
//	 * This method is for change type of current record
//	 * 
//	 * @param type
//	 */
//	protected void changeRecordType(String type) {
//		editLastRecord(true);
//		linetotalField.setCanEdit(false);
//		if (type.equals(TransactionItemRecord.TYPE_ACCOUNT)) {
//			setSelectedRecordImagePath("company/new_account");
//			accountsCombo.setComboItem(null);
//			nameField.setEditorType(accountsCombo);
//			setEditDiableCellsExecpt(nameField, descpField, qtyField,
//					unitPriceField, taxField, vatCodeField);
//		} else if (type.equals(TransactionItemRecord.TYPE_ITEM)) {
//			setSelectedRecordImagePath("customers/new_item");
//			itemCombo.setComboItem(null);
//			nameField.setEditorType(itemCombo);
//		} else if (type.equals(TransactionItemRecord.TYPE_SALESTAX)) {
//			setSelectedRecordImagePath("salestax");
//			salesTaxCombo.setComboItem(null);
//			nameField.setEditorType(salesTaxCombo);
//			setEditDiableCellsExecpt(nameField, descpField, linetotalField);
//		} else if (type.equals(TransactionItemRecord.TYPE_COMMENT)) {
//			setSelectedRecordImagePath("comment");
//			setEditDiableCellsExecpt(descpField);
//		} else {
//			editLastRecord(false);
//		}
//
//	}
//
//	public void createMenu() {
//
//		com.smartgwt.client.widgets.menu.events.ClickHandler AccountsclickHandler = new com.smartgwt.client.widgets.menu.events.ClickHandler() {
//
//			public void onClick(MenuItemClickEvent event) {
//				onMenuClick("4");
//			}
//		};
//		addMenuItem(
//				(isCustomerTransaction || isBankingTransaction) ? "Accounts"
//						: "Expenses", "/images/icons/company/new_account.png",
//				AccountsclickHandler);
//
//		com.smartgwt.client.widgets.menu.events.ClickHandler itemsclickHandler = new com.smartgwt.client.widgets.menu.events.ClickHandler() {
//
//			public void onClick(MenuItemClickEvent event) {
//				onMenuClick("1");
//			}
//
//		};
//		addMenuItem("Items", "/images/icons/customers/new_item.png",
//				itemsclickHandler);
//
//		com.smartgwt.client.widgets.menu.events.ClickHandler taxcodesclickHandler1 = new com.smartgwt.client.widgets.menu.events.ClickHandler() {
//
//			public void onClick(MenuItemClickEvent event) {
//				onMenuClick("3");
//			}
//		};
//		if (isCustomerTransaction || isBankingTransaction)
//			addMenuItem("Sales Tax", "/images/icons/salestax.png",
//					taxcodesclickHandler1);
//		com.smartgwt.client.widgets.menu.events.ClickHandler commentclickHandler1 = new com.smartgwt.client.widgets.menu.events.ClickHandler() {
//
//			public void onClick(MenuItemClickEvent event) {
//				onMenuClick("2");
//			}
//		};
//		addMenuItem("Comment", "/images/icons/comment.png",
//				commentclickHandler1);
//	}
//
//	public void onMenuClick(String itemType) {
//		currentRecord = getSelectedRecord();
//		if (currentRecord != null) {
//			String type = currentRecord.getAttribute("type");
//			if (type.equals("none")) {
//				addNewRecord(itemType);
//			} else {
//				if (currentRecord instanceof TransactionItemRecord) {
//					((TransactionItemRecord) currentRecord).setType(itemType);
//					((TransactionItemRecord) currentRecord).resetValues();
//
//				}
//				changeRecordType(itemType);
//				makeCurrentRecordEditable();
//			}
//			currentRecord.setAttribute("type", itemType);
//		}
//	}
//
//	public void addNewRecord(String type) {
//		if (type.equals("1")) {
//			setImagePath("customers/new_item");
//			isShowDefaultValues(true);
//		} else if (type.equals("2")) {
//			setImagePath("comment");
//			isShowDefaultValues(false);
//		} else if (type.equals("3")) {
//			setImagePath("salestax");
//			isShowDefaultValues(false);
//		} else if (type.equals("4")) {
//			setImagePath("company/new_account");
//			isShowDefaultValues(true);
//		}
//		changeRecordType(type);
//		addEmptyRecord(type);
//	}
//
//	public List<ClientTransactionItem> getallTransactions(
//			ClientTransaction object) throws InvalidEntryException {
//
//		ListGridRecord[] records = getRecords();
//		final List<ClientTransactionItem> transactionItems = new ArrayList<ClientTransactionItem>();
//		if (records != null) {
//			for (ListGridRecord record : records) {
//
//				if (record instanceof TransactionItemRecord) {
//					TransactionItemRecord txRecord = (TransactionItemRecord) record;
//					txRecord.setTransaction(object);
//
//					try {
//
//						transactionItems.add(txRecord.getTransactionItem());
//
//					} catch (InvalidEntryException exception) {
//						getGrid().deselectRecord(txRecord);
//						Accounter.showError(exception.getMessage());
//						throw exception;
//					}
//
//				}
//
//			}
//		}
//		return transactionItems;
//	}
//
//	public void setAllTransactions(List<ClientTransactionItem> transactionItems) {
//
//		deleteAllRecords();
//
//		// initGrid();
//
//		TransactionItemRecord allRecords[] = new TransactionItemRecord[transactionItems
//				.size()];
//
//		int i = 0;
//		for (ClientTransactionItem item : transactionItems) {
//
//			allRecords[i] = new TransactionItemRecord(transactionDomain);
//			allRecords[i].setTransactionItem(item);
//			allRecords[i].refresh();
//			i++;
//		}
//		addRecords(allRecords);
//		updateTotals();
//
//	}
//
//	public Double getTotal() {
//		return totallinetotal != null ? totallinetotal.doubleValue() : 0.0d;
//	}
//
//	public void priceLevelSelected(ClientPriceLevel priceLevel) {
//
//		this.priceLevel = priceLevel;
//
//		ListGridRecord[] records = getGrid().getRecords();
//
//		if (records.length == 0)
//			return;
//
//		for (ListGridRecord record : records) {
//			if (!record.getAttribute("type").equals(
//					TransactionItemRecord.TYPE_ITEM))
//				continue;
//
//			TransactionItemRecord transactionRecord = (TransactionItemRecord) record;
//
//			double unitPrice = 0.0;
//
//			ClientItem item = transactionRecord.getItem();
//
//			if (item != null) {
//
//				unitPrice = Utility.getCalculatedItemUnitPriceForPriceLevel(
//						item, priceLevel, isVendorTransaction);
//
//				transactionRecord.setUnitPrice(unitPrice);
//
//			}
//
//			transactionRecord.refresh();
//		}
//		updateTotals();
//	}
//
//	public Double getTaxableLineTotal() {
//		return this.taxableTotal;
//
//	}
//
//	public Double getVatTotal() {
//		return this.totalVat;
//	}
//
//	@Override
//	protected void transactionItemRecordDeleted() {
//		super.transactionItemRecordDeleted();
//		updateTotals();
//	}
//
//	public void resetGridEditEvent() {
//		if (getGrid() != null)
//			getGrid().setEditEvent(ListGridEditEvent.DOUBLECLICK);
//	}
//
//	public Double getGrandTotal() {
//		return grandTotal;
//	}
//
//	public void refreshVatValue(ListGridRecord record) {
//		if (!(record instanceof TransactionItemRecord))
//			return;
//
//		if (((TransactionItemRecord) record).getTaxCode() == null)
//			return;
//		double nearestTaxRate = ((TransactionItemRecord) record).getTaxCode()
//				.getNearestTaxRate(
//						transactionView.getTransactionDate().getTime());
//		double vat = 0.0;
//		if (transactionView.isEUVatExempt()) {
//			vat = 0.0;
//		} else {
//			if (transactionView.isShowPriceWithVat()) {
//				vat = ((TransactionItemRecord) record).getLineTotal()
//						- (100 * (((TransactionItemRecord) record)
//								.getLineTotal() / (100 + nearestTaxRate)));
//			} else {
//				vat = ((TransactionItemRecord) record).getLineTotal()
//						* nearestTaxRate / 100;
//			}
//		}
//
//		((TransactionItemRecord) record).setVat(vat);
//		updateTotals();
//	}
//
//	public void refreshVatValue() {
//		for (ListGridRecord record : getGrid().getRecords()) {
//			if (((TransactionItemRecord) record).getTaxCode() != null)
//				refreshVatValue((TransactionItemRecord) record);
//			this.redraw();
//		}
//		updateTotals();
//
//	}
// }
