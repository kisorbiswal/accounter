package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AddNewButton;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientInventoryAssembly;
import com.vimukti.accounter.web.client.core.ClientInventoryAssemblyItem;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientItemGroup;
import com.vimukti.accounter.web.client.core.ClientMeasurement;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientWarehouse;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.ItemGroupCombo;
import com.vimukti.accounter.web.client.ui.combo.MeasurementCombo;
import com.vimukti.accounter.web.client.ui.combo.PurchaseItemCombo;
import com.vimukti.accounter.web.client.ui.combo.SalesItemCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.combo.VendorCombo;
import com.vimukti.accounter.web.client.ui.combo.WarehouseCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.FloatRangeValidator;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;
import com.vimukti.accounter.web.client.ui.core.IntegerField;
import com.vimukti.accounter.web.client.ui.core.IntegerRangeValidator;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class InventoryAssemblyView extends BaseView<ClientInventoryAssembly> {
	private TextItem nameText, skuText;
	private AmountField salesPriceText, stdCostText, purchasePriceTxt,
			itemTotalValue;
	private IntegerField vendItemNumText, reorderPoint, onHandQuantity;
	private TextAreaItem salesDescArea, purchaseDescArea;
	CheckboxItem comCheck, activeCheck, itemTaxCheck;

	private ItemGroupCombo itemGroupCombo, commodityCode;
	private VendorCombo prefVendorCombo;
	private SalesItemCombo accountCombo;
	private PurchaseItemCombo expAccCombo;
	private TAXCodeCombo taxCode;
	List<ClientAccount> incomeAccounts;
	HashMap<String, ClientVendor> allvendors;
	HashMap<String, ClientAccount> allaccounts;
	HashMap<String, ClientItemGroup> allitemgroups;
	private DynamicForm itemForm;
	private DynamicForm stdCostForm;
	private DynamicForm itemInfoForm;
	private DynamicForm purchaseInfoForm;
	private DynamicForm salesInfoForm;
	private FloatRangeValidator floatRangeValidator;
	private IntegerRangeValidator integerRangeValidator;

	protected ClientItemGroup selectItemGroup;
	protected ClientVendor selectVendor;
	protected ClientTAXCode selectTaxCode;
	private ArrayList<DynamicForm> listforms;
	private String name;
	private String itemName;
	private InventoryAssemblyItemTable table;

	private MeasurementCombo measurement;
	private WarehouseCombo wareHouse;
	private AccountCombo assetsAccount;
	private DateField asOfDate;
	private AddNewButton itemTableButton;
	private AmountLabel totalLabel;

	public InventoryAssemblyView() {
		super();
	}

	private void initTaxCodes() {
		if (getPreferences().isTrackTax()
				&& getPreferences().isTaxPerDetailLine()) {
			List<ClientTAXCode> result = getCompany().getActiveTaxCodes();
			if (result != null) {
				taxCode.initCombo(result);
				ClientTAXCode code = null;
				if (isInViewMode()) {
					code = getCompany().getTAXCode(data.getTaxCode());
				}
				if (code == null) {
					code = getCompany().getTAXCode(
							getPreferences().getDefaultTaxCode());
				}
				taxCode.setComboItem(code);
			}
		}

	}

	private void createControls() {

		listforms = new ArrayList<DynamicForm>();

		Label lab1 = new Label(messages.newProduct());
		lab1.setStyleName("label-title");

		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.add(lab1);

		nameText = new TextItem(messages.inventoryAssembly());
		nameText.setValue(itemName);
		nameText.setHelpInformation(true);
		nameText.setWidth(100);
		nameText.setRequired(true);
		nameText.setDisabled(isInViewMode());

		floatRangeValidator = new FloatRangeValidator();
		floatRangeValidator.setMin(0);

		integerRangeValidator = new IntegerRangeValidator();
		integerRangeValidator.setMin(0);

		skuText = new TextItem();
		skuText.setHelpInformation(true);
		skuText.setWidth(100);
		skuText.setTitle(messages.upcsku());
		skuText.setDisabled(isInViewMode());

		commodityCode = new ItemGroupCombo(messages.commodityCode());
		commodityCode.setHelpInformation(true);
		itemForm = new DynamicForm();
		itemForm.setStyleName("item-form-view");
		itemForm.setIsGroup(true);
		itemForm.setGroupTitle(messages.item());
		itemForm.setFields(nameText);
		salesDescArea = new TextAreaItem();
		salesDescArea.setHelpInformation(true);
		salesDescArea.setWidth(100);
		salesDescArea.setTitle(messages.salesDescription());

		salesDescArea.setDisabled(isInViewMode());

		salesDescArea.setToolTip(messages.writeCommentsForThis(
				this.getAction().getViewName()).replace(messages.comments(),
				messages.salesDescription()));
		salesPriceText = new AmountField(messages.salesPrice(), this,
				getBaseCurrency());
		salesPriceText.setHelpInformation(true);
		salesPriceText.setWidth(100);
		salesPriceText.setDisabled(isInViewMode());

		accountCombo = new SalesItemCombo(messages.incomeAccount());
		accountCombo.setHelpInformation(true);
		accountCombo.setDisabled(isInViewMode());
		accountCombo.setPopupWidth("500px");
		accountCombo.setRequired(true);

		/**
		 * adding the inventory information controls
		 */

		assetsAccount = new AccountCombo(messages.assetsAccount()) {

			@Override
			protected List<ClientAccount> getAccounts() {
				return getCompany().getAccounts();
			}
		};

		ArrayList<ClientAccount> accounts = getCompany().getAccounts();
		for (ClientAccount clientAccount : accounts) {
			if ((Integer.parseInt(clientAccount.getNumber()) == 1001)) {
				assetsAccount.setSelectedItem(accounts.indexOf(clientAccount));
				break;
			}
		}
		assetsAccount.setHelpInformation(true);
		assetsAccount.setDisabled(isInViewMode());
		assetsAccount.setPopupWidth("500px");
		assetsAccount.setRequired(true);

		reorderPoint = new IntegerField(this, "Reorder Point");
		reorderPoint.setHelpInformation(true);
		reorderPoint.setWidth(100);
		reorderPoint.setDisabled(isInViewMode());
		reorderPoint.setValidators(integerRangeValidator);

		itemTotalValue = new AmountField(messages.total(), this,
				getBaseCurrency());
		itemTotalValue.setHelpInformation(true);
		itemTotalValue.setWidth(100);
		itemTotalValue.setAmount(0.00D);
		itemTotalValue.setDisabled(true);

		onHandQuantity = new IntegerField(this, "On Hand Quantity");
		onHandQuantity.setNumber(0l);
		onHandQuantity.setHelpInformation(true);
		onHandQuantity.setWidth(100);
		onHandQuantity.setDisabled(isInViewMode());
		onHandQuantity.setValidators(integerRangeValidator);
		onHandQuantity.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				Double amount = purchasePriceTxt.getAmount();
				if (onHandQuantity.getValue().length() > 0) {
					Double amount2 = Double.valueOf(onHandQuantity.getValue());
					itemTotalValue.setAmount(amount * amount2);
				}

			}
		});

		asOfDate = new DateField(messages.asOf());
		asOfDate.setHelpInformation(true);
		asOfDate.setDisabled(isInViewMode());
		asOfDate.setEnteredDate(new ClientFinanceDate());

		DynamicForm inventoryInfoForm = new DynamicForm();
		inventoryInfoForm.setFields(assetsAccount, reorderPoint,
				onHandQuantity, itemTotalValue, asOfDate);

		/**
		 * over
		 */

		itemTaxCheck = new CheckboxItem(messages.taxable());
		itemTaxCheck.setValue(true);
		itemTaxCheck.setDisabled(true);

		comCheck = new CheckboxItem(messages.commissionItem());
		comCheck.setDisabled(isInViewMode());

		salesInfoForm = UIUtils.form(messages.salesInformation());

		stdCostText = new AmountField(messages.standardCost(), this,
				getBaseCurrency());
		stdCostText.setHelpInformation(true);
		stdCostText.setWidth(100);
		stdCostText.setDisabled(isInViewMode());

		itemGroupCombo = new ItemGroupCombo(messages.itemGroup());
		itemGroupCombo.setHelpInformation(true);
		itemGroupCombo.setDisabled(isInViewMode());
		itemGroupCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientItemGroup>() {
					@Override
					public void selectedComboBoxItem(ClientItemGroup selectItem) {
						selectItemGroup = selectItem;
					}
				});

		taxCode = new TAXCodeCombo(messages.taxCode(), false);
		taxCode.setHelpInformation(true);
		taxCode.setRequired(false);
		taxCode.setDisabled(isInViewMode());

		taxCode.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXCode>() {
			@Override
			public void selectedComboBoxItem(ClientTAXCode selectItem) {
				selectTaxCode = selectItem;
			}
		});
		taxCode.setDefaultValue(messages.ztozeroperc());
		activeCheck = new CheckboxItem(messages.active());
		activeCheck.setValue(true);
		activeCheck.setDisabled(isInViewMode());
		purchaseDescArea = new TextAreaItem();
		purchaseDescArea.setHelpInformation(true);
		purchaseDescArea.setWidth(100);
		purchaseDescArea.setTitle(messages.purchaseDescription());
		purchaseDescArea.setDisabled(isInViewMode());

		purchasePriceTxt = new AmountField(messages.standardCost(), this,
				getBaseCurrency());
		purchasePriceTxt.setHelpInformation(true);
		purchasePriceTxt.setWidth(100);
		purchasePriceTxt.setDisabled(isInViewMode());
		purchasePriceTxt.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				itemTotalValue.setAmount(purchasePriceTxt.getAmount()
						* onHandQuantity.getNumber());
			}
		});

		expAccCombo = new PurchaseItemCombo(messages.costOfGoodSold());
		expAccCombo.setHelpInformation(true);
		expAccCombo.setRequired(true);
		expAccCombo.setDisabled(isInViewMode());
		expAccCombo.setPopupWidth("500px");
		prefVendorCombo = new VendorCombo(messages.preferredVendor(Global.get()
				.Vendor()));
		prefVendorCombo.setHelpInformation(true);
		prefVendorCombo.setDisabled(isInViewMode());
		prefVendorCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {
					@Override
					public void selectedComboBoxItem(ClientVendor selectItem) {
						selectVendor = selectItem;
					}
				});

		vendItemNumText = new IntegerField(this,
				messages.vendorProductNo(Global.get().Vendor()));
		vendItemNumText.setHelpInformation(true);
		vendItemNumText.setWidth(100);
		vendItemNumText.setDisabled(isInViewMode());

		disableSalesFormItems(isInViewMode());
		disablePurchaseFormItems(isInViewMode());

		salesInfoForm.setFields(salesDescArea, salesPriceText, accountCombo,
				itemTaxCheck, comCheck, stdCostText);

		if (!getPreferences().isTrackTax()
				&& getPreferences().isTaxPerDetailLine())
			salesInfoForm.setFields(itemTaxCheck);

		salesInfoForm.setStyleName("align-form");
		salesInfoForm.setStyleName("new_service_table");
		salesInfoForm.getCellFormatter().addStyleName(1, 0, "memoFormAlign");
		itemInfoForm = UIUtils.form(messages.itemInformation());

		itemInfoForm.setFields(itemGroupCombo, activeCheck);
		if (!getPreferences().isTrackTax()
				&& getPreferences().isTaxPerDetailLine())
			itemInfoForm.setFields(activeCheck);

		purchaseInfoForm = UIUtils.form(messages.purchaseInformation());
		purchaseInfoForm.setNumCols(2);
		purchaseInfoForm.setStyleName("purchase_info_form");
		purchaseInfoForm.setFields(purchaseDescArea, purchasePriceTxt,
				expAccCombo, prefVendorCombo, vendItemNumText);
		purchaseInfoForm.getCellFormatter().addStyleName(1, 0, "memoFormAlign");

		VerticalPanel salesVPanel = new VerticalPanel();
		salesVPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		salesVPanel.setWidth("100%");
		HorizontalPanel itemHPanel = new HorizontalPanel();

		itemHPanel.setCellHorizontalAlignment(itemForm, ALIGN_LEFT);

		salesVPanel.setCellHorizontalAlignment(itemHPanel, ALIGN_LEFT);
		salesVPanel.add(salesInfoForm);
		salesVPanel.add(inventoryInfoForm);
		VerticalPanel purchzVPanel = new VerticalPanel();

		purchzVPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		purchzVPanel.setWidth("100%");
		HorizontalPanel itemInfoPanel = new HorizontalPanel();

		itemInfoPanel.setCellHorizontalAlignment(itemInfoForm, ALIGN_LEFT);

		purchzVPanel.add(purchaseInfoForm);

		HorizontalPanel topPanel1 = new HorizontalPanel();
		topPanel1.setHorizontalAlignment(ALIGN_RIGHT);
		topPanel1.setWidth("100%");
		topPanel1.add(itemForm);
		topPanel1.setCellHorizontalAlignment(itemForm, ALIGN_LEFT);
		topPanel1.setStyleName("service-item-group");
		topPanel1.setCellHorizontalAlignment(itemInfoPanel, ALIGN_LEFT);
		topPanel1.add(itemInfoForm);
		topPanel1.setCellHorizontalAlignment(itemInfoForm, ALIGN_LEFT);
		topPanel1.setCellWidth(itemForm, "50%");
		topPanel1.setCellWidth(itemInfoForm, "50%");
		VerticalPanel topHLay = new VerticalPanel();
		topHLay.addStyleName("fields-panel");
		topHLay.setWidth("100%");

		HorizontalPanel topPanel2 = new HorizontalPanel();
		VerticalPanel emptyPanel = new VerticalPanel();
		emptyPanel.setWidth("100%");

		topPanel2.setHorizontalAlignment(ALIGN_RIGHT);
		topPanel2.setWidth("100%");
		topPanel2.add(salesVPanel);
		topPanel2.setCellHorizontalAlignment(purchzVPanel, ALIGN_LEFT);
		topPanel2.add(purchzVPanel);
		topPanel2.setCellWidth(salesVPanel, "50%");
		topPanel2.setCellWidth(purchzVPanel, "50%");
		topHLay.add(topPanel1);
		topHLay.add(topPanel2);

		VerticalPanel mainVLay = new VerticalPanel();

		mainVLay.setSize("100%", "100%");
		// mainVLay.getElement().getStyle().setMarginBottom(15, Unit.PX);
		mainVLay.add(hPanel);
		mainVLay.add(topHLay);

		table = new InventoryAssemblyItemTable(
				InventoryAssemblyItemTable.INVENTORY_ASSEMBLY_ITEM,
				new ICurrencyProvider() {

					// private ClientCurrency currency;
					// private double currencyFactor;

					@Override
					public Double getAmountInBaseCurrency(Double amount) {
						// if (currency != null && amount != null) {
						// if (currencyFactor < 0.0) {
						// currencyFactor = 1.0;
						// }
						// return amount * currencyFactor;
						// } else {
						return amount;
						// }
					}

					@Override
					public ClientCurrency getTransactionCurrency() {
						return getCompany().getPrimaryCurrency();
					}

					@Override
					public Double getCurrencyFactor() {
						// return currencyFactor;
						return 1.0;
					}

				}) {

			@Override
			public ClientCurrency getTransactionCurrency() {
				return getCompany().getPrimaryCurrency();
			}

			@Override
			public Double getCurrencyFactor() {
				return 1.0;
			}

			@Override
			public Double getAmountInBaseCurrency(Double amount) {
				return amount;
			}

			@Override
			protected void updateNonEditableItems() {
				InventoryAssemblyView.this.updateNonEditableItems();
			}

			@Override
			protected boolean isInViewMode() {
				return InventoryAssemblyView.this.isInViewMode();
			}

			@Override
			protected void addEmptyRecords() {
				for (int i = 0; i < 4; i++) {
					add(new ClientInventoryAssemblyItem());
				}
			}
		};
		mainVLay.add(table);
		table.setDisabled(isInViewMode());
		itemTableButton = new AddNewButton();
		itemTableButton.setEnabled(!isInViewMode());
		itemTableButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addItem();
			}
		});
		mainVLay.add(itemTableButton);
		totalLabel = new AmountLabel(messages.totalValueOfMaterialsCost());
		DynamicForm amountLabelsForm = new DynamicForm();
		amountLabelsForm.setFields(totalLabel);
		mainVLay.add(amountLabelsForm);
		mainVLay.setCellHorizontalAlignment(amountLabelsForm,
				HasHorizontalAlignment.ALIGN_RIGHT);

		this.add(mainVLay);

		VerticalPanel stockPanel_1 = getStockPanel_1();
		VerticalPanel stockPanel_2 = getStockPanel_2();

		purchzVPanel.add(stockPanel_1);
		purchzVPanel.add(stockPanel_2);

		/* Adding dynamic forms in list */
		listforms.add(itemForm);
		listforms.add(salesInfoForm);

		listforms.add(stdCostForm);
		listforms.add(itemInfoForm);
		listforms.add(purchaseInfoForm);
		settabIndexes();

	}

	private VerticalPanel getStockPanel_2() {
		VerticalPanel measurementPanel = new VerticalPanel();
		measurement = new MeasurementCombo(messages.measurement());
		measurement.setDisabled(isInViewMode());

		DynamicForm dynamicForm = new DynamicForm();
		dynamicForm.setFields(measurement);

		measurementPanel.add(dynamicForm);

		return measurementPanel;
	}

	private VerticalPanel getStockPanel_1() {

		VerticalPanel stockPanel = new VerticalPanel();
		DynamicForm stockForm = new DynamicForm();

		wareHouse = new WarehouseCombo(messages.wareHouse());

		wareHouse.setDisabled(isInViewMode());
		stockForm.setFields(wareHouse);
		if (getPreferences().iswareHouseEnabled()) {
			stockPanel.add(stockForm);
		}
		listforms.add(stockForm);

		stockPanel.setWidth("100%");
		return stockPanel;

	}

	public void updateNonEditableItems() {
		if (table == null)
			return;
		totalLabel.setAmount(table.getLineTotal());
	}

	private void addItem() {
		ClientInventoryAssemblyItem transactionItem = new ClientInventoryAssemblyItem();
		table.add(transactionItem);
	}

	@Override
	public ClientInventoryAssembly saveView() {
		ClientInventoryAssembly saveView = super.saveView();
		if (saveView != null) {
			updateItem();
		}
		return saveView;
	}

	@Override
	public void saveAndUpdateView() {
		updateItem();
		saveOrUpdate(data);

	}

	protected void disableSalesFormItems(Boolean isEdit) {
		salesDescArea.setDisabled(isEdit);
		salesPriceText.setDisabled(isEdit);
		accountCombo.setDisabled(isEdit);
		itemTaxCheck.setDisabled(isEdit);
		comCheck.setDisabled(isEdit);
	}

	/**
	 * call to save Entered data on this View
	 * 
	 * @param isSaveClose
	 *            if it is true, than it means it will close View, if false,than
	 *            reset values to new View
	 */
	protected void updateItem() {

		data.setActive(getBooleanValue(activeCheck));
		data.setType(ClientItem.TYPE_INVENTORY_ASSEMBLY);
		if (nameText.getValue() != null)
			data.setName(nameText.getValue().toString());
		if (selectItemGroup != null)
			data.setItemGroup(selectItemGroup.getID());
		data.setStandardCost(stdCostText.getAmount());

		data.setUPCorSKU(skuText.getValue());

		data.setISellThisItem(true);
		data.setIBuyThisItem(true);

		if (salesDescArea.getValue() != null)
			data.setSalesDescription(salesDescArea.getValue().toString());
		data.setSalesPrice(salesPriceText.getAmount());
		if (accountCombo != null && accountCombo.getSelectedValue() != null) {
			data.setIncomeAccount(accountCombo.getSelectedValue().getID());
		}

		data.setCommissionItem(getBooleanValue(comCheck));

		data.setPurchaseDescription(getStringValue(purchaseDescArea));
		data.setPurchasePrice(purchasePriceTxt.getAmount());
		if (selectVendor != null)
			data.setPreferredVendor(selectVendor.getID());
		if (expAccCombo != null && expAccCombo.getSelectedValue() != null) {
			data.setExpenseAccount(expAccCombo.getSelectedValue().getID());
		}
		data.setVendorItemNumber(vendItemNumText.getValue().toString());

		if (table.getAllRows() != null) {
			List<ClientInventoryAssemblyItem> list = table.getAllRows();
			Set<ClientInventoryAssemblyItem> set = new HashSet<ClientInventoryAssemblyItem>();
			for (ClientInventoryAssemblyItem item : list) {
				if (!item.isEmpty()) {
					set.add(item);
				}
			}
			data.setComponents(set);
		}
		data.setAssestsAccount(assetsAccount.getSelectedValue().getID());

		if (reorderPoint.getValue().length() > 0)
			data.setReorderPoint(Integer.parseInt(reorderPoint.getValue()));
		else
			data.setReorderPoint(0);

		data.setItemTotalValue(itemTotalValue.getAmount());

		if (wareHouse.getSelectedValue() != null)
			data.setWarehouse(wareHouse.getSelectedValue().getID());

		Long qtyValue = null;
		if (onHandQuantity.getValue().length() > 0) {
			qtyValue = onHandQuantity.getNumber();
		} else {
			qtyValue = 0l;
		}
		ClientMeasurement measurement = null;

		if (this.measurement.getSelectedValue() != null) {
			measurement = this.measurement.getSelectedValue();
		} else {
			measurement = getCompany().getMeasurement(
					getCompany().getDefaultMeasurement());
		}
		data.setMeasurement(measurement.getId());
		ClientQuantity quantity = new ClientQuantity();
		quantity.setValue(qtyValue);
		quantity.setUnit(measurement.getDefaultUnit().getId());
		data.setOnhandQty(quantity);

		data.setAsOfDate(asOfDate.getValue());
		data.setTaxable(getBooleanValue(itemTaxCheck));

	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);

		String exceptionMessage = exception.getMessage();

		AccounterException accounterException = exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);

		updateItem();
		if (exceptionMessage.contains(messages.failed())) {
			data.setName(name);
			System.out.println(name + messages.aftersaving());
		}

	}

	private void setStockPanelData() {
		ClientMeasurement measurement;
		if (data.getMeasurement() != 0) {
			measurement = getCompany().getMeasurement(data.getMeasurement());
		} else {
			measurement = getCompany().getMeasurement(
					getCompany().getDefaultMeasurement());
		}
		this.measurement.setComboItem(measurement);
		ClientWarehouse wareHouse;
		if (data.getWarehouse() != 0) {
			wareHouse = getCompany().getWarehouse(data.getWarehouse());
		} else {
			wareHouse = getCompany().getWarehouse(
					getCompany().getDefaultWarehouse());
		}
		this.wareHouse.setComboItem(wareHouse);

	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		if (result == null) {
			saveFailed(new AccounterException());
			return;
		} else {
			super.saveSuccess(result);

		}

	}

	private boolean getBooleanValue(FormItem item) {
		return item.getValue() != null ? (Boolean) item.getValue() : false;
	}

	private String getStringValue(FormItem item) {
		return item.getValue() != null ? item.getValue().toString() : "";
	}

	protected void disablePurchaseFormItems(Boolean isDisable) {

		purchasePriceTxt.setDisabled(isDisable);
		purchaseDescArea.setDisabled(isDisable);
		vendItemNumText.setDisabled(isDisable);
		expAccCombo.setDisabled(isDisable);
		prefVendorCombo.setDisabled(isDisable);

	}

	@Override
	public void init() {
		super.init();
		createControls();
		setSize("100%", "100%");
	}

	@Override
	public void initData() {
		if (data != null) {

			nameText.setValue(data.getName());
			name = data.getName();
			stdCostText.setAmount(data.getStandardCost());

			if (data.getSalesDescription() != null)
				salesDescArea.setValue(data.getSalesDescription());
			salesPriceText.setAmount(data.getSalesPrice());

			ClientCompany company = getCompany();
			comCheck.setValue(data.isCommissionItem());

			selectItemGroup = company.getItemGroup(data.getItemGroup());
			itemGroupCombo.setComboItem(selectItemGroup);

			activeCheck.setValue((data.isActive()));

			if (data.getPurchaseDescription() != null)
				purchaseDescArea.setValue(data.getPurchaseDescription());
			purchasePriceTxt.setAmount(data.getPurchasePrice());

			selectVendor = company.getVendor(data.getPreferredVendor());
			if (data.getVendorItemNumber() != null) {
				vendItemNumText.setValue(data.getVendorItemNumber());
			}
			if (getPreferences().isTrackTax()
					&& getPreferences().isTaxPerDetailLine()) {
				selectTaxCode = company.getTAXCode(data.getTaxCode());
			}
			itemTaxCheck.setValue(data.isTaxable());
			if (data.getAssestsAccount() != 0) {
				assetsAccount.setSelected(getCompany().getAccount(
						data.getAssestsAccount()).getName());
			}

			reorderPoint.setValue(Integer.toString(data.getReorderPoint()));
			onHandQuantity.setValue(String.valueOf(data.getOnhandQty()
					.getValue()));
			// onHandQuantity.setValue("0");
			itemTotalValue.setValue(Double.toString(data.getItemTotalValue()));
			asOfDate.setValue(data.getAsOfDate());

			if (data.getComponents() != null) {
				List<ClientInventoryAssemblyItem> list = new ArrayList<ClientInventoryAssemblyItem>();
				Set<ClientInventoryAssemblyItem> set = data.getComponents();
				for (ClientInventoryAssemblyItem item : set) {
					list.add(item);
				}
				table.setRecords(list);
			}

		} else {
			setData(new ClientInventoryAssembly());
			table.setRecords(new ArrayList<ClientInventoryAssemblyItem>());
		}
		initAccountList();
		initVendorsList();
		initItemGroups();
		setStockPanelData();
		if (getPreferences().isTrackTax()
				&& getPreferences().isTaxPerDetailLine()) {
			if (data != null) {
				if (getPreferences().isTaxPerDetailLine())
					initTaxCodes();
			}
		}
		super.initData();
	}

	private void initItemGroups() {
		List<ClientItemGroup> clientItemgroup = getCompany().getItemGroups();

		if (clientItemgroup != null) {
			itemGroupCombo.initCombo(clientItemgroup);
			if (isInViewMode()) {
				itemGroupCombo.setComboItem(getCompany().getItemGroup(
						data.getItemGroup()));
			}
		}
	}

	private void initVendorsList() {
		List<ClientVendor> clientVendor = getCompany().getActiveVendors();
		if (clientVendor != null) {
			prefVendorCombo.initCombo(clientVendor);
			if (data != null) {
				prefVendorCombo.setComboItem(getCompany().getVendor(
						data.getPreferredVendor()));
			}
			prefVendorCombo.setDisabled(isInViewMode());
		}

	}

	private void initAccountList() {
		List<ClientAccount> listAccount = accountCombo.getFilterdAccounts();
		List<ClientAccount> listExpAccount = expAccCombo.getFilterdAccounts();
		if (listAccount != null) {
			accountCombo.initCombo(listAccount);
			expAccCombo.initCombo(listExpAccount);
		}
		if (!isInViewMode()) {
			if (data != null && data.getIncomeAccount() != 0) {
				accountCombo.setComboItem(getCompany().getAccount(
						data.getIncomeAccount()));
			}
			ClientAccount selectExpAccount = getCompany().getAccount(
					data.getExpenseAccount());
			if (selectExpAccount != null) {
				expAccCombo.setComboItem(selectExpAccount);
			}
		} else {
			expAccCombo.setDisabled(true);
			ClientAccount incomeAccount = getCompany().getAccount(
					data.getIncomeAccount());
			accountCombo.setComboItem(incomeAccount);

			ClientAccount expenseAccount = getCompany().getAccount(
					data.getExpenseAccount());
			expAccCombo.setComboItem(expenseAccount);

		}
	}

	@SuppressWarnings("unused")
	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();

		String name = nameText.getValue().toString();

		ClientInventoryAssembly clientItem = null;
		if (clientItem != null
				&& !(this.getData().getID() == clientItem.getID())) {
			result.addError(nameText,
					messages.anItemAlreadyExistswiththisname());
			return result;
		}

		result.add(itemForm.validate());

		result.add(salesInfoForm.validate());
		if (getPreferences().isTrackTax()
				&& getPreferences().isTaxPerDetailLine()) {
			result.add(itemInfoForm.validate());
		}
		result.add(purchaseInfoForm.validate());
		if (AccounterValidator.isNegativeAmount(salesPriceText.getAmount())) {
			result.addError(salesPriceText, messages.enterValidAmount());
		}
		if (AccounterValidator.isNegativeAmount(purchasePriceTxt.getAmount())) {
			result.addError(purchasePriceTxt, messages.enterValidAmount());
		}
		table.validateGrid();
		return result;
	}

	@Override
	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.nameText.setFocus();
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void onEdit() {

		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showError(caught.getMessage());
			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		this.rpcDoSerivce.canEdit(AccounterCoreType.ITEM, data.getID(),
				editCallBack);
	}

	protected void enableFormItems() {
		setMode(EditMode.EDIT);
		nameText.setDisabled(isInViewMode());
		skuText.setDisabled(isInViewMode());
		salesDescArea.setDisabled(isInViewMode());
		salesPriceText.setDisabled(isInViewMode());
		accountCombo.setDisabled(isInViewMode());
		stdCostText.setDisabled(isInViewMode());
		itemGroupCombo.setDisabled(isInViewMode());
		taxCode.setDisabled(isInViewMode());
		purchaseDescArea.setDisabled(isInViewMode());
		expAccCombo.setDisabled(isInViewMode());
		prefVendorCombo.setDisabled(isInViewMode());
		purchasePriceTxt.setDisabled(isInViewMode());
		vendItemNumText.setDisabled(isInViewMode());
		salesDescArea.setDisabled(isInViewMode());
		accountCombo.setDisabled(isInViewMode());
		salesPriceText.setDisabled(isInViewMode());
		assetsAccount.setDisabled(isInViewMode());
		reorderPoint.setDisabled(isInViewMode());
		onHandQuantity.setDisabled(isInViewMode());
		totalLabel.setDisabled(isInViewMode());
		asOfDate.setDisabled(isInViewMode());

		table.setDisabled(isInViewMode());
		measurement.setDisabled(isInViewMode());
		wareHouse.setDisabled(isInViewMode());
		activeCheck.setDisabled(isInViewMode());

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {
	}

	@Override
	protected String getViewTitle() {
		return messages.item();
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	private void settabIndexes() {
		nameText.setTabIndex(1);
		// isservice.setTabIndex(2);
		salesDescArea.setTabIndex(4);
		salesPriceText.setTabIndex(5);
		accountCombo.setTabIndex(6);
		comCheck.setTabIndex(7);
		stdCostText.setTabIndex(8);
		itemGroupCombo.setTabIndex(9);
		taxCode.setTabIndex(10);
		activeCheck.setTabIndex(11);
		purchaseDescArea.setTabIndex(13);
		purchasePriceTxt.setTabIndex(14);
		expAccCombo.setTabIndex(15);
		prefVendorCombo.setTabIndex(16);
		vendItemNumText.setTabIndex(17);
		saveAndCloseButton.setTabIndex(18);
		saveAndNewButton.setTabIndex(19);
		cancelButton.setTabIndex(20);
	}

	@Override
	protected boolean canVoid() {
		return false;
	}

}
