package com.vimukti.accounter.web.client.ui;

/*	 Modified by Rajesh.A, Murali.A
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientItemGroup;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.ItemGroupCombo;
import com.vimukti.accounter.web.client.ui.combo.PurchaseItemCombo;
import com.vimukti.accounter.web.client.ui.combo.SalesItemCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.combo.VendorCombo;
import com.vimukti.accounter.web.client.ui.company.NewItemAction;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.FloatRangeValidator;
import com.vimukti.accounter.web.client.ui.core.IntegerField;
import com.vimukti.accounter.web.client.ui.core.IntegerRangeValidator;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class ItemView extends BaseView<ClientItem> {

	public static final int TYPE_SERVICE = 1;
	public static final int NON_INVENTORY_PART = 3;
	private int type;
	private TextItem nameText, skuText;
	private AmountField salesPriceText, stdCostText, purchasePriceTxt;
	private IntegerField vendItemNumText, weightText, minStock, maxStock,
			defaultSellPrice, defaultPurchasePrice, salesTaxRate,
			purcahseTaxRate;
	private TextAreaItem salesDescArea, purchaseDescArea;
	CheckboxItem isservice, isellCheck, comCheck, activeCheck, ibuyCheck,
			itemTaxCheck;

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
	private ClientItem takenItem;
	protected ClientAccount selectAccount, selectExpAccount,
			defaultIncomeAccount, defaultExpAccount;

	protected ClientItemGroup selectItemGroup;
	protected ClientVendor selectVendor;
	protected ClientTAXCode selectTaxCode;
	private ClientCompany company;
	private boolean isGeneratedFromCustomer;
	private ArrayList<DynamicForm> listforms;
	String name;

	private SelectCombo measurement, wareHouse;

	public ItemView(ClientItem item, int type, boolean isGeneratedFromCustomer) {

		super();
		this.takenItem = item;
		this.type = type;
		this.company = getCompany();
		this.isGeneratedFromCustomer = isGeneratedFromCustomer;
		this.validationCount = 9;

	}

	private void initTaxCodes() {
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			List<ClientTAXCode> result = getCompany().getActiveTaxCodes();
			if (result != null) {
				taxCode.initCombo(getCompany().getActiveTaxCodes());
				if (takenItem != null) {
					taxCode.setComboItem(getCompany().getTAXCode(
							takenItem.getTaxCode()));
				} else if (!getCompany().getPreferences().getDoYouPaySalesTax()) {
					// vatCode.setDisabled(true);
					List<ClientTAXCode> taxCodes = Accounter.getCompany()
							.getActiveTaxCodes();
					for (ClientTAXCode vatCod : taxCodes) {
						if (vatCod.getName().equals("Z")) {
							taxCode.setComboItem(vatCod);
							selectTaxCode = vatCod;
							break;
						}
					}

				}
			}
		}

	}

	private ClientAccount getDefaultAccount(String defaultAccount) {
		List<ClientAccount> accountList = getCompany().getActiveAccounts();
		for (ClientAccount account : accountList) {
			if (account.getName().equalsIgnoreCase(defaultAccount)) {
				return account;
			}
		}
		return null;
	}

	private void createControls() {

		// setTitle(UIUtils.title("NEW ITEM"));
		// setTitle(UIUtils.title(FinanceApplication.constants()
		// .. .newServiceItem()));

		listforms = new ArrayList<DynamicForm>();

		Label lab1 = new Label(Accounter.constants().newProduct());
		lab1.setStyleName(Accounter.constants().labelTitle());

		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.add(lab1);

		// nameText = new TextItem(FinanceApplication.constants()
		// .itemName());
		nameText = new TextItem(this.type == TYPE_SERVICE ? Accounter
				.constants().serviceName() : Accounter.constants()
				.productName());
		nameText.setHelpInformation(true);
		nameText.setWidth(100);
		nameText.setRequired(true);
		isservice = new CheckboxItem(Accounter.constants().isService());
		isservice.setValue(true);
		isservice.setDisabled(true);

		floatRangeValidator = new FloatRangeValidator();
		floatRangeValidator.setMin(0);

		integerRangeValidator = new IntegerRangeValidator();
		integerRangeValidator.setMin(0);

		skuText = new TextItem();// new FormFieldItem("UPC/SKU",
		skuText.setHelpInformation(true);
		skuText.setWidth(100);
		skuText.setTitle(Accounter.constants().upcsku());

		weightText = new IntegerField(Accounter.constants().weight());
		weightText.setHelpInformation(true);
		weightText.setWidth(100);
		weightText.setValidators(integerRangeValidator);
		commodityCode = new ItemGroupCombo(Accounter.constants()
				.commodityCode());
		commodityCode.setHelpInformation(true);
		itemForm = new DynamicForm();
		itemForm.setWidth("98%");
		itemForm.setStyleName("item-form-view");
		itemForm.setIsGroup(true);
		itemForm.setGroupTitle(Accounter.constants().item());
		if (isEdit)
			this.type = takenItem.getType();
		if (type == TYPE_SERVICE) {
			lab1.setText(Accounter.constants().newService());
			if (getCompany().getAccountingType() == 1)
				itemForm.setFields(nameText, isservice);
			else

				itemForm.setFields(nameText, isservice, skuText);
		} else {
			lab1.setText(Accounter.constants().newProduct());
			if (getCompany().getAccountingType() == 1)
				itemForm.setFields(nameText, weightText);
			else
				itemForm.setFields(nameText, skuText, weightText);
		}
		itemForm.getCellFormatter().setWidth(0, 0, "30%");
		salesDescArea = new TextAreaItem();
		salesDescArea.setHelpInformation(true);
		salesDescArea.setWidth(100);
		salesDescArea.setTitle(Accounter.constants().salesDescription());

		salesPriceText = new AmountField(Accounter.constants().salesPrice());
		salesPriceText.setHelpInformation(true);
		salesPriceText.setWidth(100);
		// FIXME--needto implement this feature
		// salesPriceText.setValidators(floatRangeValidator);
		// salesPriceText.setValidateOnChange(true);

		accountCombo = new SalesItemCombo(Accounter.constants().incomeAccount());
		accountCombo.setHelpInformation(true);
		// accountCombo.setWidth(100);
		accountCombo.setPopupWidth("500px");
		accountCombo.setRequired(true);
		accountCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {
					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectAccount = selectItem;
						if (selectAccount != null
								&& selectAccount != defaultIncomeAccount
								&& defaultIncomeAccount != null) {
							if (type == TYPE_SERVICE)
								AccounterValidator
										.defaultIncomeAccountServiceItem(
												selectAccount,
												defaultIncomeAccount);
							if (type == NON_INVENTORY_PART)
								AccounterValidator
										.defaultIncomeAccountNonInventory(
												selectAccount,
												defaultIncomeAccount);
						}
					}
				});
		itemTaxCheck = new CheckboxItem(Accounter.constants().taxable());
		itemTaxCheck.setValue(true);

		comCheck = new CheckboxItem(Accounter.constants().commissionItem());

		salesInfoForm = UIUtils.form(Accounter.constants().salesInformation());
		salesInfoForm.setWidth("98%");

		stdCostText = new AmountField(Accounter.constants().standardCost());
		stdCostText.setHelpInformation(true);
		stdCostText.setWidth(100);
		// FIXME--needto implement this feature
		// stdCostText.setValidators(floatRangeValidator);
		// stdCostText.setValidateOnChange(true);

		// stdCostForm = UIUtils.form(FinanceApplication.constants()
		// .standardcost());
		// stdCostForm.setFields(stdCostText);
		// stdCostForm.setWidth("95%");
		// stdCostForm.getCellFormatter().setWidth(0, 0, "165");
		// itemGroupCombo = new ItemGroupCombo(FinanceApplication
		// .constants().itemGroup());
		itemGroupCombo = new ItemGroupCombo(Accounter.constants().itemGroup());
		itemGroupCombo.setHelpInformation(true);
		// itemGroupCombo.setWidth(100);
		itemGroupCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientItemGroup>() {
					public void selectedComboBoxItem(ClientItemGroup selectItem) {
						selectItemGroup = selectItem;
					}
				});
		taxCode = new TAXCodeCombo(Accounter.constants().vatCode(),
				isGeneratedFromCustomer);
		taxCode.setHelpInformation(true);
		taxCode.setRequired(true);
		// if (!FinanceApplication.getCompany().getpreferences()
		// .getDoYouPaySalesTax()) {
		// vatCode.setDisabled(true);
		// }

		taxCode.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXCode>() {
			public void selectedComboBoxItem(ClientTAXCode selectItem) {
				selectTaxCode = selectItem;
			}
		});
		taxCode.setDefaultValue(Accounter.constants().ztozeroperc());
		activeCheck = new CheckboxItem(Accounter.constants().active());
		activeCheck.setValue(true);
		purchaseDescArea = new TextAreaItem();
		purchaseDescArea.setHelpInformation(true);
		purchaseDescArea.setWidth(100);
		purchaseDescArea.setTitle(Accounter.constants().purchaseDescription());

		purchasePriceTxt = new AmountField(Accounter.constants()
				.purchasePrice());
		purchasePriceTxt.setHelpInformation(true);
		purchasePriceTxt.setWidth(100);
		// FIXME--needto implement this feature
		// purchasePriceTxt.setValidators(floatRangeValidator);
		// purchasePriceTxt.setValidateOnChange(true);

		expAccCombo = new PurchaseItemCombo(Accounter.constants()
				.expenseAccount());
		expAccCombo.setHelpInformation(true);
		expAccCombo.setRequired(true);
		expAccCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {
					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectExpAccount = selectItem;
						if (selectExpAccount != null
								&& selectExpAccount != defaultExpAccount) {
							if (type == TYPE_SERVICE)
								AccounterValidator
										.defaultExpenseAccountServiceItem(
												selectExpAccount,
												defaultExpAccount);
							if (type == NON_INVENTORY_PART)
								AccounterValidator
										.defaultExpenseAccountNonInventory(
												selectExpAccount,
												defaultExpAccount);
						}
					}
				});
		expAccCombo.setPopupWidth("500px");
		prefVendorCombo = new VendorCombo(UIUtils.getVendorString(Accounter
				.constants().preferredSupplier(), Accounter.constants()
				.preferredVendor()));
		prefVendorCombo.setHelpInformation(true);
		prefVendorCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {
					public void selectedComboBoxItem(ClientVendor selectItem) {
						selectVendor = selectItem;
					}
				});
		vendItemNumText = new IntegerField(
				this.type != TYPE_SERVICE ? UIUtils.getVendorString(Accounter
						.constants().supplierProductNo(), Accounter.constants()
						.vendorProductNo()) : UIUtils.getVendorString(Accounter
						.constants().supplierServiceNo(), Accounter.constants()
						.vendorServiceNo()));
		vendItemNumText.setHelpInformation(true);
		vendItemNumText.setWidth(100);

		// isellCheck = new CheckboxItem(FinanceApplication
		// .constants().iSellThisItem());
		isellCheck = new CheckboxItem(this.type == TYPE_SERVICE ? Accounter
				.constants().isellthisservice() : Accounter.constants()
				.isellthisproduct());
		if (isGeneratedFromCustomer) {
			isellCheck.setValue(isGeneratedFromCustomer);
			isellCheck.setDisabled(!isGeneratedFromCustomer);
			disablePurchaseFormItems(isGeneratedFromCustomer);
		} else {
			isellCheck.setDisabled(isGeneratedFromCustomer);
			disableSalesFormItems(isGeneratedFromCustomer);
		}

		isellCheck.addChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				disableSalesFormItems(!event.getValue());
			}

		});

		// ibuyCheck = new
		// CheckboxItem(FinanceApplication.constants()
		// .iBuyThisItem());
		ibuyCheck = new CheckboxItem(this.type == TYPE_SERVICE ? Accounter
				.constants().ibuythisservice() : Accounter.constants()
				.ibuythisproduct());
		ibuyCheck.setValue(!isGeneratedFromCustomer);

		ibuyCheck.addChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				disablePurchaseFormItems(!event.getValue());

			}

		});

		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK)
			salesInfoForm.setFields(isellCheck, salesDescArea, salesPriceText,
					accountCombo, comCheck, stdCostText);
		else
			salesInfoForm.setFields(isservice, isellCheck, salesDescArea,
					salesPriceText, accountCombo, itemTaxCheck, comCheck,
					stdCostText);

		salesInfoForm.setStyleName("align-form");
		salesInfoForm.setStyleName("new_service_table");
		salesInfoForm.getCellFormatter().setWidth(0, 0, "25%");
		salesInfoForm.getCellFormatter().setWidth(3, 0, "25%");
		salesInfoForm.getCellFormatter().addStyleName(1, 0, "memoFormAlign");
		itemInfoForm = UIUtils.form(Accounter.constants().itemInformation());
		itemInfoForm.setWidth("97%");
		if (getCompany().getAccountingType() == 1)
			itemInfoForm.setFields(itemGroupCombo, taxCode, activeCheck);
		else
			itemInfoForm.setFields(itemGroupCombo, activeCheck);
		itemInfoForm.getCellFormatter().setWidth(0, 0, "30%");
		purchaseInfoForm = UIUtils.form(Accounter.constants()
				.purchaseInformation());
		purchaseInfoForm.setNumCols(2);
		purchaseInfoForm.setStyleName("purchase_info_form");
		purchaseInfoForm.setWidth("97%");
		purchaseInfoForm
				.setFields(ibuyCheck, purchaseDescArea, purchasePriceTxt,
						expAccCombo, prefVendorCombo, vendItemNumText);
		purchaseInfoForm.getCellFormatter().setWidth(0, 0, "30%");
		purchaseInfoForm.getCellFormatter().addStyleName(1, 0, "memoFormAlign");
		VerticalPanel salesVPanel = new VerticalPanel();
		salesVPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		salesVPanel.setWidth("100%");
		HorizontalPanel itemHPanel = new HorizontalPanel();
		itemHPanel.setWidth("90%");

		itemHPanel.setCellHorizontalAlignment(itemForm, ALIGN_RIGHT);
		salesVPanel.setCellHorizontalAlignment(itemHPanel, ALIGN_RIGHT);

		// itemHPanel.add(itemForm);

		salesVPanel.add(salesInfoForm);
		// salesVPanel.add(stdCostForm);

		VerticalPanel purchzVPanel = new VerticalPanel();

		purchzVPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		purchzVPanel.setWidth("100%");
		HorizontalPanel itemInfoPanel = new HorizontalPanel();

		itemInfoPanel.setCellHorizontalAlignment(itemInfoForm, ALIGN_RIGHT);
		// itemInfoPanel.add(itemInfoForm);

		purchzVPanel.add(purchaseInfoForm);

		HorizontalPanel topPanel1 = new HorizontalPanel();
		// topPanel1.setSpacing(15);
		// topPanel1.setStyleName("equal-columns");
		topPanel1.setHorizontalAlignment(ALIGN_RIGHT);
		topPanel1.setWidth("100%");
		topPanel1.add(itemForm);
		topPanel1.setStyleName("service-item-group");
		topPanel1.setCellHorizontalAlignment(itemInfoPanel, ALIGN_RIGHT);
		topPanel1.add(itemInfoForm);
		topPanel1.setCellWidth(itemForm, "50%");
		topPanel1.setCellWidth(itemInfoForm, "50%");
		VerticalPanel topHLay = new VerticalPanel();
		topHLay.setWidth("100%");

		HorizontalPanel topPanel2 = new HorizontalPanel();
		// topPanel2.setStyleName("equal-columns");
		VerticalPanel emptyPanel = new VerticalPanel();
		emptyPanel.setWidth("100%");

		topPanel2.setHorizontalAlignment(ALIGN_RIGHT);
		topPanel2.setWidth("100%");
		topPanel2.add(salesVPanel);
		// topPanel2.add(emptyPanel);
		topPanel2.setCellHorizontalAlignment(purchzVPanel, ALIGN_RIGHT);
		topPanel2.add(purchzVPanel);
		topPanel2.setCellWidth(salesVPanel, "50%");
		topPanel2.setCellWidth(purchzVPanel, "50%");
		topHLay.add(topPanel1);
		topHLay.add(topPanel2);
		// topHLay.setCellWidth(topPanel1, "50%");
		// topHLay.setCellWidth(topPanel2, "50%");

		VerticalPanel stockPanel = new VerticalPanel();
		DynamicForm stockForm = new DynamicForm();
		measurement = new SelectCombo(Accounter.constants().measurement());
		wareHouse = new SelectCombo(Accounter.constants().wareHouse());
		minStock = new IntegerField(Accounter.constants().minStockAlertLevel());
		maxStock = new IntegerField(Accounter.constants().maxStockAlertLevel());
		defaultSellPrice = new IntegerField(Accounter.constants()
				.defaultSellPrice());
		defaultPurchasePrice = new IntegerField(Accounter.constants()
				.defaultPurchasePrice());
		salesTaxRate = new IntegerField(Accounter.constants().salesTaxRate());
		purcahseTaxRate = new IntegerField(Accounter.constants()
				.purchaseTaxRate());
		stockForm.setFields(measurement, wareHouse, minStock, maxStock,
				defaultSellPrice, defaultPurchasePrice, salesTaxRate,
				purcahseTaxRate);

		stockPanel.add(stockForm);
		stockPanel.setWidth("100%");

		VerticalPanel mainVLay = new VerticalPanel();

		mainVLay.setSize("100%", "100%");
		mainVLay.getElement().getStyle().setMarginBottom(15, Unit.PX);
		mainVLay.add(hPanel);
		mainVLay.add(topHLay);
		mainVLay.add(stockPanel);

		canvas.add(mainVLay);

		if (takenItem != null) {
			nameText.setValue(takenItem.getName());
			name = takenItem.getName();
			System.out.println(name + Accounter.constants().beforesaving());
			stdCostText.setAmount(takenItem.getStandardCost());
			if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
				skuText.setValue(takenItem.getUPCorSKU() != null ? takenItem
						.getUPCorSKU() : "");

			weightText.setValue(UIUtils.toLong(takenItem.getWeight()));

			isellCheck.setValue(takenItem.isISellThisItem());
			if (takenItem.getSalesDescription() != null)
				salesDescArea.setValue(takenItem.getSalesDescription());
			salesPriceText.setAmount(takenItem.getSalesPrice());

			ClientCompany company = getCompany();
			selectAccount = company.getAccount(takenItem.getIncomeAccount());
			comCheck.setValue(takenItem.isCommissionItem());

			selectItemGroup = company.getItemGroup(takenItem.getItemGroup());
			activeCheck.setValue((takenItem.isActive()));

			ibuyCheck.setValue(takenItem.isIBuyThisItem());
			if (takenItem.getPurchaseDescription() != null)
				purchaseDescArea.setValue(takenItem.getPurchaseDescription());
			purchasePriceTxt.setAmount(takenItem.getPurchasePrice());

			selectVendor = company.getVendor(takenItem.getPreferredVendor());
			if (takenItem.getVendorItemNumber() != null)
				vendItemNumText.setValue(takenItem.getVendorItemNumber());
			if (company.getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK)
				selectTaxCode = company.getTAXCode(takenItem.getTaxCode());

			itemTaxCheck.setValue(takenItem.isTaxable());

		}

		/* Adding dynamic forms in list */
		listforms.add(itemForm);
		listforms.add(salesInfoForm);

		listforms.add(stdCostForm);
		listforms.add(itemInfoForm);
		listforms.add(purchaseInfoForm);

	}

	@Override
	public void saveAndUpdateView() throws Exception {
		ClientItem item = null;
		item = getItem(saveAndClose);

		if (takenItem == null) {

			if (Utility.isObjectExist(getCompany().getItems(), item.getName())) {
				throw new InvalidEntryException(AccounterErrorType.ALREADYEXIST);
			} else
				createObject(item);

		} else {
			alterObject(item);

		}

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
	protected ClientItem getItem(boolean isSaveClose) {

		final boolean isSaveOrReset = isSaveClose;
		ClientItem item;
		if (takenItem == null)
			item = new ClientItem();
		else {
			item = takenItem;
		}

		item.setActive(getBooleanValue(activeCheck));
		item.setType(type);
		if (nameText.getValue() != null)
			item.setName(nameText.getValue().toString());
		if (selectItemGroup != null)
			item.setItemGroup(selectItemGroup.getID());
		item.setStandardCost(stdCostText.getAmount());

		item.setUPCorSKU((String) skuText.getValue());

		if (type == NON_INVENTORY_PART && weightText.getNumber() != null)
			item.setWeight(UIUtils.toInt(weightText.getNumber()));

		item.setISellThisItem(getBooleanValue(isellCheck));
		item.setIBuyThisItem(getBooleanValue(ibuyCheck));

		if (getBooleanValue(isellCheck)) {
			if (salesDescArea.getValue() != null)
				item.setSalesDescription(salesDescArea.getValue().toString());
			item.setSalesPrice(salesPriceText.getAmount());
			if (selectAccount != null)
				item.setIncomeAccount(selectAccount.getID());
			item.setCommissionItem(getBooleanValue(comCheck));
		}

		if (getBooleanValue(ibuyCheck)) {

			item.setPurchaseDescription(getStringValue(purchaseDescArea));
			item.setPurchasePrice(purchasePriceTxt.getAmount());
			if (selectVendor != null)
				item.setPreferredVendor(selectVendor.getID());
			if (selectExpAccount != null)
				item.setExpenseAccount(selectExpAccount.getID());

			item.setVendorItemNumber(vendItemNumText.getValue().toString());
		}
		if (getCompany().getAccountingType() == 0)
			item.setTaxable(getBooleanValue(itemTaxCheck));
		else
			item.setTaxable(true);
		if (type == NON_INVENTORY_PART || type == TYPE_SERVICE)
			item.setTaxCode(selectTaxCode != null ? selectTaxCode.getID()
					: null);
		return item;
	}

	@Override
	public void saveFailed(Throwable exception) {
		super.saveFailed(exception);

		String exceptionMessage = exception.getMessage();
		// MainFinanceWindow.getViewManager().showError(exceptionMessage);

		// BaseView.errordata
		// .setHTML(this.type != TYPE_SERVICE ?
		// "Duplication of Product name are not allowed..."
		// : "Duplication of Service name are not allowed...");
		// BaseView.commentPanel.setVisible(true);
		// this.errorOccured = true;
		MainFinanceWindow.getViewManager().showError(
				this.type != TYPE_SERVICE ? Accounter.constants()
						.duplicationofProductnamearenotallowed3dots()
						: Accounter.constants()
								.duplicationofServicenamearenotallowed3dots());

		ClientItem item = getItem(false);
		if (exceptionMessage.contains(Accounter.constants().failed())) {
			item.setName(name);
			System.out.println(name + Accounter.constants().aftersaving());
		}

	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		if (result == null) {
			saveFailed(new Exception());
			return;
		} else {
			// if (takenItem == null)
			// Accounter.showInformation(FinanceApplication
			// .constants().itemCreatedSuccessfully());
			// else
			// Accounter.showInformation(FinanceApplication
			// .constants().itemupdatedSuccessfully());
			NewItemAction action = (NewItemAction) this.getAction();
			action.setType(type);
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

	public void init() {
		super.init();
		createControls();
		setSize("100%", "100%");
	}

	@Override
	public void initData() {
		initAccountList();
		initVendorsList();
		initItemGroups();
		if (company.getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK)
			initTaxCodes();
		super.initData();

	}

	private void initItemGroups() {
		List<ClientItemGroup> clientItemgroup = getCompany().getItemGroups();

		if (clientItemgroup != null) {
			itemGroupCombo.initCombo(clientItemgroup);
			if (takenItem != null) {
				itemGroupCombo.setComboItem(getCompany().getItemGroup(
						takenItem.getItemGroup()));
			}
		}
		if (takenItem != null) {
			if (takenItem.isISellThisItem()) {
				isellCheck.setDisabled(false);
				ibuyCheck.setDisabled(true);
				disablePurchaseFormItems(true);
			}

			if (takenItem.isIBuyThisItem()) {
				isellCheck.setDisabled(true);
				disableSalesFormItems(true);
			}

		} else {
			disablePurchaseFormItems(isGeneratedFromCustomer);
			disableSalesFormItems(!isGeneratedFromCustomer);
		}
	}

	private void initVendorsList() {
		List<ClientVendor> clientVendor = getCompany().getActiveVendors();
		if (clientVendor != null) {
			prefVendorCombo.initCombo(clientVendor);
			if (takenItem != null) {
				prefVendorCombo.setComboItem(getCompany().getVendor(
						takenItem.getPreferredVendor()));
				if (takenItem.isIBuyThisItem() == false)
					prefVendorCombo.setDisabled(true);
				else
					prefVendorCombo.setDisabled(false);

			} else
				prefVendorCombo.setDisabled(true);
		}

	}

	private void initAccountList() {
		List<ClientAccount> listAccount = accountCombo.getFilterdAccounts();
		List<ClientAccount> listExpAccount = expAccCombo.getFilterdAccounts();
		int accountType = company.getAccountingType();
		if (listAccount != null) {
			accountCombo.initCombo(listAccount);
			expAccCombo.initCombo(listExpAccount);
			if (type == TYPE_SERVICE) {
				if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
					defaultIncomeAccount = getDefaultAccount(company
							.getUkServiceItemDefaultIncomeAccount());
					defaultExpAccount = getDefaultAccount(company
							.getUkServiceItemDefaultExpenseAccount());

				} else if (accountType == ClientCompany.ACCOUNTING_TYPE_US) {
					defaultIncomeAccount = getDefaultAccount(Accounter
							.constants().incomeandDistribution());
					defaultExpAccount = getDefaultAccount(Accounter.constants()
							.cashDiscountTaken());

				}
				selectAccount = defaultIncomeAccount;
				accountCombo.setComboItem(defaultIncomeAccount);
				selectExpAccount = defaultExpAccount;
				expAccCombo.setComboItem(defaultExpAccount);
			}
			if (type == NON_INVENTORY_PART) {
				if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
					defaultIncomeAccount = getDefaultAccount(company
							.getUkNonInventoryItemDefaultIncomeAccount());
					defaultExpAccount = getDefaultAccount(company
							.getUkNonInventoryItemDefaultExpenseAccount());
				} else if (accountType == ClientCompany.ACCOUNTING_TYPE_US) {
					defaultIncomeAccount = getDefaultAccount(Accounter
							.constants().incomeandDistribution());
					defaultExpAccount = getDefaultAccount(Accounter.constants()
							.cashDiscountTaken());

				}
				selectAccount = defaultIncomeAccount;
				accountCombo.setComboItem(defaultIncomeAccount);
				selectExpAccount = defaultExpAccount;
				expAccCombo.setComboItem(defaultExpAccount);
			}
		}
		if (takenItem != null) {

			accountCombo.setComboItem(getCompany().getAccount(
					takenItem.getIncomeAccount()));
			if (takenItem.isISellThisItem() == false)
				accountCombo.setDisabled(true);
			selectExpAccount = getCompany().getAccount(
					takenItem.getExpenseAccount());
			expAccCombo.setComboItem(selectExpAccount);
			if (takenItem.isIBuyThisItem() == false)
				expAccCombo.setDisabled(true);
			else
				expAccCombo.setDisabled(false);

		} else
			expAccCombo.setDisabled(true);

	}

	@Override
	public void setData(ClientItem data) {
		super.setData(data);
		if (data != null)
			takenItem = (ClientItem) data;
		else
			takenItem = null;
	}

	public void onDraw() {

		this.purchaseDescArea.setDisabled(true);
		this.purchasePriceTxt.setDisabled(true);
		this.vendItemNumText.setDisabled(true);

	}

	@Override
	public boolean validate() throws InvalidEntryException {

		switch (this.validationCount) {
		case 9:
			String name = nameText.getValue().toString();
			if (takenItem == null) {
				if (Utility.isObjectExist(company.getItems(), name)) {
					// BaseView.errordata.setHTML(BaseView.errordata.getHTML()
					// + "<li> An Item already exists with this name.");
					// BaseView.commentPanel.setVisible(true);
					// AbstractBaseView.errorOccured = true;
					MainFinanceWindow.getViewManager().appendError(
							Accounter.constants()
									.aItemGroupAlreadyExistswiththisname());
					// Accounter
					// .showError("An Item already exists with this name");
					return false;
				} else
					return true;
			}/*
			 * else if (takenItem != null &&
			 * (!takenItem.getName().equalsIgnoreCase(name))) { if
			 * (Utility.isObjectExist(company.getItems(), name)) throw new
			 * InvalidEntryException( AccounterErrorType.ALREADYEXIST); else
			 * return true; }
			 */

		case 8:
			return AccounterValidator.validateForm(itemForm, false);
		case 7:
			return AccounterValidator.isSellorBuyCheck(isellCheck, ibuyCheck);
		case 6:
			if (AccounterValidator.isChecked(isellCheck))
				return AccounterValidator.validateForm(salesInfoForm, false);
			return true;
		case 5:
			boolean result = true;
			if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
				result = AccounterValidator.validateForm(itemInfoForm, false);
			}
			if (AccounterValidator.isChecked(ibuyCheck))
				result = AccounterValidator.validateForm(purchaseInfoForm,
						false);
			return result;

		case 4:
			if (AccounterValidator.isChecked(isellCheck))
				return AccounterValidator.validateAmount(salesPriceText
						.getAmount());
			else
				return true;

		case 3:
			if (AccounterValidator.isChecked(ibuyCheck))
				return AccounterValidator.validateAmount(purchasePriceTxt
						.getAmount());
			else
				return true;

		case 2:
			if (selectAccount != null) {
				if (AccounterValidator.isChecked(isellCheck))
					return AccounterValidator.validate_IncomeAccount(this,
							selectAccount);
				else
					return true;
			}
			return true;
		case 1:
			if (selectExpAccount != null) {
				if (AccounterValidator.isChecked(ibuyCheck))
					return AccounterValidator.validate_ExpenseAccount(this,
							selectExpAccount);
				else
					return true;
			}
		default:
			return false;
		}
	}

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
	public void deleteFailed(Throwable caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		switch (command) {
		case AccounterCommand.CREATION_SUCCESS:
			if (core.getObjectType() == AccounterCoreType.ITEM_GROUP)
				itemGroupCombo.addComboItem((ClientItemGroup) core);
			if (core.getObjectType() == AccounterCoreType.ITEM_GROUP)
				commodityCode.addComboItem((ClientItemGroup) core);
			if (core.getObjectType() == AccounterCoreType.VENDOR)
				this.prefVendorCombo.addComboItem((ClientVendor) core);
			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.accountCombo.addComboItem((ClientAccount) core);
			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.expAccCombo.addComboItem((ClientAccount) core);
			if (core.getObjectType() == AccounterCoreType.TAX_CODE
					&& getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK)
				this.taxCode.addComboItem((ClientTAXCode) core);
			break;
		case AccounterCommand.UPDATION_SUCCESS:
			if (core.getObjectType() == AccounterCoreType.ITEM_GROUP)
				itemGroupCombo.updateComboItem((ClientItemGroup) core);
			if (core.getObjectType() == AccounterCoreType.ITEM_GROUP)
				commodityCode.updateComboItem((ClientItemGroup) core);
			if (core.getObjectType() == AccounterCoreType.VENDOR)
				this.prefVendorCombo.updateComboItem((ClientVendor) core);
			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.accountCombo.updateComboItem((ClientAccount) core);
			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.expAccCombo.updateComboItem((ClientAccount) core);
			if (core.getObjectType() == AccounterCoreType.TAX_CODE
					&& getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK)
				this.taxCode.updateComboItem((ClientTAXCode) core);
			break;

		case AccounterCommand.DELETION_SUCCESS:
			if (core.getObjectType() == AccounterCoreType.ITEM_GROUP)
				itemGroupCombo.removeComboItem((ClientItemGroup) core);
			if (core.getObjectType() == AccounterCoreType.ITEM_GROUP)
				commodityCode.removeComboItem((ClientItemGroup) core);
			if (core.getObjectType() == AccounterCoreType.VENDOR)
				this.prefVendorCombo.removeComboItem((ClientVendor) core);
			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.accountCombo.removeComboItem((ClientAccount) core);
			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.expAccCombo.removeComboItem((ClientAccount) core);
			if (core.getObjectType() == AccounterCoreType.TAX_CODE
					&& getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK)
				this.taxCode.removeComboItem((ClientTAXCode) core);
			break;
		}
	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// NOTHING TO DO.
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().item();
	}
}
