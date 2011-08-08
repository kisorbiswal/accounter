//package com.vimukti.accounter.web.client.ui.customers;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.google.gwt.event.dom.client.ChangeEvent;
//import com.google.gwt.event.dom.client.ChangeHandler;
//import com.google.gwt.event.dom.client.ClickHandler;
//import com.google.gwt.user.client.rpc.IsSerializable;
//import com.vimukti.accounter.web.client.core.ClientAccount;
//import com.vimukti.accounter.web.client.core.ClientCompany;
//import com.vimukti.accounter.web.client.core.ClientItem;
//import com.vimukti.accounter.web.client.core.ClientPriceLevel;
//import com.vimukti.accounter.web.client.core.ClientTaxCode;
//import com.vimukti.accounter.web.client.core.ClientTransaction;
//import com.vimukti.accounter.web.client.core.ClientTransactionItem;
//import com.vimukti.accounter.web.client.core.RecordDoubleClickHandler;
//import com.vimukti.accounter.web.client.core.Utility;
//import com.vimukti.accounter.web.client.ui.FinanceApplication;
//import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
//import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
//import com.vimukti.accounter.web.client.ui.combo.SelectItemType;
//import com.vimukti.accounter.web.client.ui.core.Accounter;
//import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
//import com.vimukti.accounter.web.client.ui.core.ListGridView;
//import com.vimukti.accounter.web.client.ui.core.TransactionItemRecord;
//import com.vimukti.accounter.web.client.ui.forms.ComboBoxItem;
//import com.vimukti.accounter.web.client.ui.forms.TextItem;
//import com.vimukti.accounter.web.client.ui.grids.EditCompleteHandler;
//
///**
// * 
// * @author Fernandez
// * @implementation Fernandez
// * 
// */
//public class ProductsAndServicesListGrid extends ListGridView {
//
//	private ListGridField nameField;
//	// private ItemCombo itemCombo;
//	private List<ClientItem> items = new ArrayList<ClientItem>();
//	private List<ClientAccount> accounts = new ArrayList<ClientAccount>();
//	private List<ClientTaxCode> taxcodes = new ArrayList<ClientTaxCode>();
//	// private AccountCombo accountsCombo;
//	// private TaxCodeCombo salesTaxCombo;
//	private CustomCombo accountsCombo, salesTaxCombo, itemCombo;
//	private ComboBoxItem taxSelectItem;
//	private ClientCompany company;
//	private TextItem descrptextItem;
//	private ListGridField descpField;
//	private ListGridField qtyField;
//	private TextItem unitpriceTextItem;
//	private ListGridField unitPriceField;
//	private ListGridField discuntField;
//	private ListGridField linetotalField;
//	private ListGridField taxField;
//	private TextItem discountTextItem;
//	private TextItem linetotalTextItem;
//	// private ITemTaxCombo taxSelectItem;
//	protected ListGridRecord currentRecord;
//	private Double totallinetotal;
//	// private ItemTax selectedItemTax;
//	private ClientPriceLevel priceLevel;
//	private double taxableTotal;
//	private AbstractCustomerTransactionView customerTransactionView;
//
//	public ProductsAndServicesListGrid(
//			AbstractCustomerTransactionView transactionView,
//			ClientTransaction transaction) {
//		super(ListGridView.CUSTOMER_TRANSACTION);
//
//		if (transactionView == null)
//			return;
//
//		// if (transaction!=null && transaction.getID() != null)
//		// showImageForRecord(false);
//
//		this.customerTransactionView = transactionView;
//
//		company = FinanceApplication.getCompany();
//		getData();
//		createControls();
//
//		if (transaction != null) {
//			setAllTransactions(transaction.getTransactionItems());
//			if (transaction.getID() != 0) {
//				setEnableMenu(false);
//				setIsDeleteEnable(false);
//			}
//		}
//	}
//
//	private void getData() {
//		accounts = FinanceApplication.getCompany().getAccounts();
//		accountsCombo.initCombo(accounts);
//		nameField.setEditorType(accountsCombo);
//
//		items = FinanceApplication.getCompany().getItems();
//		itemCombo.initCombo(items);
//		nameField.setEditorType(itemCombo);
//
//		taxcodes = FinanceApplication.getCompany().getTaxcodes();
//		salesTaxCombo.initCombo(taxcodes);
//		nameField.setEditorType(salesTaxCombo);
//
//		// itemtaxList = FinanceApplication.getCompany().getItemTaxs();
//		// taxSelectItem.initCombo(itemtaxList);
//		taxField.setEditorType(taxSelectItem);
//		accountsCombo.initCombo(FinanceApplication.getCompany().getAccounts());
//		nameField.setEditorType(accountsCombo);
//
//		List<ClientItem> result = FinanceApplication.getCompany().getItems();
//		items = result;
//		itemCombo.initCombo(result);
//		nameField.setEditorType(itemCombo);
//
//		// AccounterAsyncCallback<ArrayList<ClientAccount>> accountsCallback = new
//		// AccounterAsyncCallback<ArrayList<ClientAccount>>() {
//		//
//		// public void onException(AccounterException caught) {
//		// Accounter.showError("Result: null");
//		// }
//		//
//		// public void onSuccess(ArrayList<ClientAccount> result) {
//		// accountsCombo.initCombo(result);
//		// nameField.setEditorType(accountsCombo);
//		// }
//		//
//		// };
//		// AccounterAsyncCallback<ArrayList<ClientItem>> itemsCallback = new
//		// AccounterAsyncCallback<ArrayList<ClientItem>>() {
//		//
//		// public void onException(AccounterException caught) {
//		// Accounter.showError("Result: null");
//		// }
//		//
//		// public void onSuccess(ArrayList<ClientItem> result) {
//		// items = result;
//		// itemCombo.initCombo(result);
//		// nameField.setEditorType(itemCombo);
//		// }
//		//
//		// };
//		List<ClientTaxCode> listTaxCodes = FinanceApplication.getCompany()
//				.getTaxcodes();
//		taxcodes = listTaxCodes;
//		salesTaxCombo.initCombo(listTaxCodes);
//		nameField.setEditorType(salesTaxCombo);
//
//		// AccounterAsyncCallback<ArrayList<ClientTaxCode>> taxcodesCallBack = new
//		// AccounterAsyncCallback<ArrayList<ClientTaxCode>>() {
//		//
//		// public void onException(AccounterException caught) {
//		// Accounter.showError("Result: null");
//		// }
//		//
//		// public void onSuccess(ArrayList<ClientTaxCode> result) {
//		// taxcodes = result;
//		// salesTaxCombo.initCombo(result);
//		// nameField.setEditorType(salesTaxCombo);
//		// }
//		//
//		// };
//		// List<ClientItemTax> listItemTax =
//		// FinanceApplication.getCompany().getItemTaxs();
//		// itemtaxList = listItemTax;
//		taxSelectItem.setValueMap("Taxable", "Non-Taxable");
//		taxField.setEditorType(taxSelectItem);
//		// AccounterAsyncCallback<ArrayList<ClientItemTax>> taxsCallback = new
//		// AccounterAsyncCallback<ArrayList<ClientItemTax>>() {
//		//
//		// public void onException(AccounterException caught) {
//		// Accounter.showError("Result: null");
//		// }
//		//
//		// public void onSuccess(ArrayList<ClientItemTax> result) {
//		// itemtaxList = result;
//		// taxSelectItem.initCombo(result);
//		// taxField.setEditorType(taxSelectItem);
//		// }
//		//
//		// };
//
//		// FinanceApplication.createGETService().getAccounts(accountsCallback);
//		// FinanceApplication.createGETService().getItems(itemsCallback);
//		// FinanceApplication.createGETService().getTaxCodes(taxcodesCallBack);
//		// FinanceApplication.createGETService().getItemTaxes(taxsCallback);
//	}
//
//	// AccounterAsyncCallback<ArrayList<ClientAccount>> accountsCallback = new
//	// AccounterAsyncCallback<ArrayList<ClientAccount>>() {
//	//
//	// public void onException(AccounterException caught) {
//	// Accounter.showError("Result: null");
//	// }
//	//
//	// public void onSuccess(ArrayList<ClientAccount> result) {
//	//			
//	// }
//	//
//	// };
//
//	// AccounterAsyncCallback<ArrayList<ClientItem>> itemsCallback = new
//	// AccounterAsyncCallback<ArrayList<ClientItem>>() {
//	//
//	// public void onException(AccounterException caught) {
//	// Accounter.showError("Result: null");
//	// }
//	//
//	// public void onSuccess(ArrayList<ClientItem> result) {
//	//				
//	// }
//	//
//	// };
//
//	// AccounterAsyncCallback<ArrayList<ClientTaxCode>> taxcodesCallBack = new
//	// AccounterAsyncCallback<ArrayList<ClientTaxCode>>() {
//	//
//	// public void onException(AccounterException caught) {
//	// Accounter.showError("Result: null");
//	// }
//	//
//	// public void onSuccess(ArrayList<ClientTaxCode> result) {
//	//				
//	// }
//	//
//	// };
//	// AccounterAsyncCallback<ArrayList<ClientItemTax>> taxsCallback = new
//	// AccounterAsyncCallback<ArrayList<ClientItemTax>>() {
//	//
//	// public void onException(AccounterException caught) {
//	// Accounter.showError("Result: null");
//	// }
//	//
//	// public void onSuccess(ArrayList<ClientItemTax> result) {
//	//				
//	// }
//	//
//	// };
//
//	// FinanceApplication.createGETService().getAccounts(accountsCallback);
//	//
//	// FinanceApplication.createGETService().getItems(itemsCallback);
//	//
//	// FinanceApplication.createGETService().getTaxCodes(taxcodesCallBack);
//	//
//	// FinanceApplication.createGETService().getItemTaxes(taxsCallback);
//	// }
//
//	private void createControls() {
//		createMenu();
//		nameField = new ListGridField(TransactionItemRecord.ATTR_NAME, "Name",
//				100);
//		// nameField.addEditorEnterHandler(new EditorEnterHandler(){
//		//
//		// public void onEditorEnter(EditorEnterEvent event) {
//		// Accounter.showError("Camee.."+event.getRecord().getAttribute("type"));
//		// accountsCombo.initCombo(accounts);
//		// nameField.setEditorType(accountsCombo);
//		//				
//		// }
//		//			
//		// });
//		itemCombo = new CustomCombo("Item", SelectItemType.ITEM, true);
//		// itemCombo.initCombo(items);
//		// itemCombo.setName("name");
//	//	itemCombo.setPickListWidth(200);
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
//		accountsCombo = new CustomCombo("Accounts", SelectItemType.ACCOUNT,
//				true);
//		accountsCombo.initCombo(accounts);
//		// accountsCombo.setName("name");
//	//	accountsCombo.setPickListWidth(200);
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
//					}
//				});
//
//		salesTaxCombo = new CustomCombo("Sales Tax", SelectItemType.TAX_CODE,
//				true);
//		salesTaxCombo.initCombo(taxcodes);
//		// salesTaxCombo.setName("name");
//	//	salesTaxCombo.setPickListWidth(200);
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
//
//						TransactionItemRecord record = (TransactionItemRecord) event
//								.getRecord();
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
//								TransactionItemRecord.TYPE_COMMENT)) {
//
//							Double lineTotal = Double
//									.parseDouble(lineTotalAmtString);
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
//		taxSelectItem.setName("tax");
//		taxSelectItem.addChangeHandler(new ChangeHandler() {
//
//			@Override
//			public void onChange(ChangeEvent event) {
//				//  Auto-generated method stub
//				TransactionItemRecord record = (TransactionItemRecord) getGrid()
//						.getSelectedRecord();
//
//				record.setItemTax((String) taxSelectItem.getValue());
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
//
//				updateTotals();
//			}
//		});
//
//		addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
//
//			@Override
//			public void OnCellDoubleClick(ListGridRecord record) {
//				changeRecordType(record.getAttribute("type").toString());
//			}
//		});
//		addToIntegerValidator(qtyField, unitPriceField, discuntField);
//
//		addFields(nameField, descpField, qtyField, unitPriceField,
//				discuntField, linetotalField, taxField);
//		// set
//
//		setDefaultValue("qty", "1");
//		setDefaultValue("unitPrice", ""+UIUtils.getCurrencySymbol() +"0.00");
//		setDefaultValue("lineTotal", ""+UIUtils.getCurrencySymbol() +"0.00");
//		setDefaultValue("discount", "0%");
//
//		setBottomLabelTitle("Sub Total:", 1);
//		setBottomLabelTitle("Discount:", 5);
//		setBottomLabelTitle("LineTotal:", 6);
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
//						priceLevel, false);
//
//		if (calculatedUnitPrice == null)
//			return;
//
//		int index = getGrid().getRecordIndex(getSelectedRecord());
//
//		int col = 4;
//
//		// getGrid().endEditing();
//
//		getGrid().discardEdits(index, col, false);
//
//		// getSelectedRecord().setAttribute("name", selectedItem.getName());
//		record.setItem(selectedItem);
//
//		record.setUnitPrice(calculatedUnitPrice);
//
//		record.refresh();
//
//		// getSelectedRecord().setAttribute("unitPrice",
//		// ""+UIUtils.getCurrencySymbol() +" " + String.valueOf(calculatedUnitPrice));
//
//		getGrid().startEditing(index, col, true);
//
//	}
//
//	protected void updateTotals() {
//
//		ListGridRecord allrecords[] = getRecords();
//		int totaldiscount = 0;
//		totallinetotal = 0.0;
//		taxableTotal = 0.0;
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
//			if (rec.getItem().isTaxable()) {
//				taxableTotal += lineTotalAmt;
//			}
//		}
//		customerTransactionView.updateNonEditableItems();
//		setBottomLabelTitle("Discount:" + totaldiscount + "%", 5);
//		setBottomLabelTitle("Total:" + ""+UIUtils.getCurrencySymbol() +"" + totallinetotal, 6);
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
//			setSelectedRecordImagePath("account");
//			nameField.setEditorType(accountsCombo);
//			setEditDiableCellsExecpt(nameField, descpField, qtyField,
//					unitPriceField, taxField);
//		} else if (type.equals(TransactionItemRecord.TYPE_ITEM)) {
//			setSelectedRecordImagePath("customers/new_item");
//			nameField.setEditorType(itemCombo);
//		} else if (type.equals(TransactionItemRecord.TYPE_SALESTAX)) {
//			setSelectedRecordImagePath("salestax");
//			nameField.setEditorType(salesTaxCombo);
//			setEditDiableCellsExecpt(nameField, descpField);
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
//		ClickHandler AccountsclickHandler = new ClickHandler() {
//
//			public void onClick(MenuItemClickEvent event) {
//				onMenuClick("4");
//			}
//		};
//		addMenuItem("Accounts", "/images/icons/account.png",
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
//		addMenuItem("Sales Tax", "/images/icons/salestax.png",
//				taxcodesclickHandler1);
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
//			setImagePath("account");
//		}
//		changeRecordType(type);
//		addEmptyRecord(type);
//	}
//
//	public List<ClientTransactionItem> getallTransactions(
//			ClientTransaction object) {
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
//
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
//		TransactionItemRecord allRecords[] = new TransactionItemRecord[transactionItems
//				.size()];
//
//		int i = 0;
//		for (ClientTransactionItem item : transactionItems) {
//			// allRecords[i] = new TransactionItemRecord();
//			// Setting type
//			allRecords[i].setAttribute("type", item.getType() + "");
//
//			// Setting Name
//			if (item.getType() == ClientTransactionItem.TYPE_ACCOUNT)
//				allRecords[i].setAttribute("name", FinanceApplication
//						.getCompany().getAccount(item.getAccount()).getName());
//			else if (item.getType() == ClientTransactionItem.TYPE_ITEM)
//				allRecords[i].setAttribute("name", FinanceApplication
//						.getCompany().getItem(item.getItem()).getName());
//			else if (item.getType() == ClientTransactionItem.TYPE_SALESTAX)
//				allRecords[i].setAttribute("name", FinanceApplication
//						.getCompany().getTaxCode(item.getTaxCode()).getName());
//			// Setting Description
//			allRecords[i].setAttribute("description", item.getDescription());
//			// Setting Qty
//			allRecords[i].setAttribute("qty", "" + item.getQuantity());
//			// Setting Unitprice
//			allRecords[i].setAttribute("unitPrice", ""+UIUtils.getCurrencySymbol() +"" + item.getUnitPrice());
//
//			allRecords[i].setAttribute("discount",
//					item.getDiscount() != 0 ? item.getDiscount() : 0.00D + "%");
//
//			allRecords[i].setAttribute("lineTotal", ""+UIUtils.getCurrencySymbol() +"" + item.getLineTotal());
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
//		for (ListGridRecord record : getGrid().getRecords()) {
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
//						item, priceLevel, false);
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
// }
