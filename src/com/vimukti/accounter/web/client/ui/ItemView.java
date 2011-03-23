package com.vimukti.accounter.web.client.ui;

/*	 Modified by Rajesh.A, Murali.A
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.combo.VendorCombo;
import com.vimukti.accounter.web.client.ui.company.NewItemAction;
import com.vimukti.accounter.web.client.ui.core.Accounter;
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
	private IntegerField vendItemNumText, weightText;
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

	public ItemView(ClientItem item, int type, boolean isGeneratedFromCustomer) {

		super();
		this.takenItem = item;
		this.type = type;
		this.company = FinanceApplication.getCompany();
		this.isGeneratedFromCustomer = isGeneratedFromCustomer;
		this.validationCount = 9;

	}

	private void initTaxCodes() {
		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			List<ClientTAXCode> result = FinanceApplication.getCompany()
					.getActiveTaxCodes();
			if (result != null) {
				taxCode.initCombo(FinanceApplication.getCompany()
						.getActiveTaxCodes());
				if (takenItem != null) {
					taxCode.setComboItem(FinanceApplication.getCompany()
							.getTAXCode(takenItem.getTaxCode()));
				} else if (!FinanceApplication.getCompany().getpreferences()
						.getDoYouPaySalesTax()) {
					// vatCode.setDisabled(true);
					List<ClientTAXCode> taxCodes = FinanceApplication
							.getCompany().getActiveTaxCodes();
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
		List<ClientAccount> accountList = FinanceApplication.getCompany()
				.getActiveAccounts();
		for (ClientAccount account : accountList) {
			if (account.getName().equalsIgnoreCase(defaultAccount)) {
				return account;
			}
		}
		return null;
	}

	private void createControls() {

		// setTitle(UIUtils.title("NEW ITEM"));
		// setTitle(UIUtils.title(FinanceApplication.getFinanceUIConstants()
		// .. .newServiceItem()));

		listforms = new ArrayList<DynamicForm>();

		Label lab1 = new Label(FinanceApplication.getCustomersMessages()
				.newProduct());
		lab1.setStyleName(FinanceApplication.getCustomersMessages()
				.lableTitle());

		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.add(lab1);

		// nameText = new TextItem(FinanceApplication.getFinanceUIConstants()
		// .itemName());
		nameText = new TextItem(this.type == TYPE_SERVICE ? FinanceApplication
				.getCustomersMessages().serviceName() : FinanceApplication
				.getCustomersMessages().productName());
		nameText.setHelpInformation(true);
		nameText.setWidth(100);
		nameText.setRequired(true);
		isservice = new CheckboxItem(FinanceApplication.getFinanceUIConstants()
				.isService());
		isservice.setValue(true);
		isservice.setDisabled(true);

		floatRangeValidator = new FloatRangeValidator();
		floatRangeValidator.setMin(0);

		integerRangeValidator = new IntegerRangeValidator();
		integerRangeValidator.setMin(0);

		skuText = new TextItem();// new FormFieldItem("UPC/SKU",
		skuText.setHelpInformation(true);
		skuText.setWidth(100);
		skuText.setTitle("UPC/SKU");

		weightText = new IntegerField(FinanceApplication
				.getFinanceUIConstants().weight());
		weightText.setHelpInformation(true);
		weightText.setWidth(100);
		weightText.setValidators(integerRangeValidator);
		commodityCode = new ItemGroupCombo(FinanceApplication
				.getFinanceUIConstants().commmodityCode());
		commodityCode.setHelpInformation(true);
		itemForm = new DynamicForm();
		itemForm.setWidth("98%");
		itemForm.setIsGroup(true);
		itemForm.setGroupTitle(FinanceApplication.getFinanceUIConstants()
				.item());
		if (isEdit)
			this.type = takenItem.getType();
		if (type == TYPE_SERVICE) {
			lab1
					.setText(FinanceApplication.getCustomersMessages()
							.newService());
			if (FinanceApplication.getCompany().getAccountingType() == 1)
				itemForm.setFields(nameText, isservice);
			else

				itemForm.setFields(nameText, isservice, skuText);
		} else {
			lab1
					.setText(FinanceApplication.getCustomersMessages()
							.newProduct());
			if (FinanceApplication.getCompany().getAccountingType() == 1)
				itemForm.setFields(nameText, weightText);
			else
				itemForm.setFields(nameText, skuText, weightText);
		}
		itemForm.getCellFormatter().setWidth(0, 0, "106");
		salesDescArea = new TextAreaItem();
		salesDescArea.setHelpInformation(true);
		salesDescArea.setWidth(100);
		salesDescArea.setTitle(FinanceApplication.getFinanceUIConstants()
				.salesDescription());

		salesPriceText = new AmountField(FinanceApplication
				.getFinanceUIConstants().salesPrice());
		salesPriceText.setHelpInformation(true);
		salesPriceText.setWidth(100);
		// FIXME--needto implement this feature
		// salesPriceText.setValidators(floatRangeValidator);
		// salesPriceText.setValidateOnChange(true);

		accountCombo = new SalesItemCombo(FinanceApplication
				.getFinanceUIConstants().incomeAccount());
		accountCombo.setHelpInformation(true);
		accountCombo.setWidth(100);
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
		itemTaxCheck = new CheckboxItem(FinanceApplication
				.getFinanceUIConstants().taxable());
		itemTaxCheck.setValue(true);

		comCheck = new CheckboxItem(FinanceApplication.getFinanceUIConstants()
				.commissionItem());

		salesInfoForm = UIUtils.form(FinanceApplication.getFinanceUIConstants()
				.salesInformation());
		salesInfoForm.setWidth("95%");

		stdCostText = new AmountField(FinanceApplication
				.getFinanceUIConstants().standardCost());
		stdCostText.setHelpInformation(true);
		stdCostText.setWidth(100);
		// FIXME--needto implement this feature
		// stdCostText.setValidators(floatRangeValidator);
		// stdCostText.setValidateOnChange(true);

//		stdCostForm = UIUtils.form(FinanceApplication.getCustomersMessages()
//				.standardcost());
//		stdCostForm.setFields(stdCostText);
//		stdCostForm.setWidth("95%");
//		stdCostForm.getCellFormatter().setWidth(0, 0, "165");
		// itemGroupCombo = new ItemGroupCombo(FinanceApplication
		// .getFinanceUIConstants().itemGroup());
		itemGroupCombo = new ItemGroupCombo(
				this.type == TYPE_SERVICE ? FinanceApplication
						.getCustomersMessages().serviceGroup()
						: FinanceApplication.getCustomersMessages()
								.productGroup());
		itemGroupCombo.setHelpInformation(true);
		itemGroupCombo.setWidth(100);
		itemGroupCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientItemGroup>() {
					public void selectedComboBoxItem(ClientItemGroup selectItem) {
						selectItemGroup = selectItem;
					}
				});
		taxCode = new TAXCodeCombo(FinanceApplication.getFinanceUIConstants()
				.vatCode(), isGeneratedFromCustomer);
		taxCode.setHelpInformation(true);
		taxCode.setRequired(true);
		// if (!FinanceApplication.getCompany().getpreferences()
		// .getDoYouPaySalesTax()) {
		// vatCode.setDisabled(true);
		// }

		taxCode
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXCode>() {
					public void selectedComboBoxItem(ClientTAXCode selectItem) {
						selectTaxCode = selectItem;
					}
				});
		taxCode.setDefaultValue("Z-0.0%");
		activeCheck = new CheckboxItem(FinanceApplication
				.getFinanceUIConstants().active());
		activeCheck.setValue(true);
		purchaseDescArea = new TextAreaItem();
		purchaseDescArea.setHelpInformation(true);
		purchaseDescArea.setWidth(100);
		purchaseDescArea.setTitle(FinanceApplication.getFinanceUIConstants()
				.purchaseDescription());

		purchasePriceTxt = new AmountField(FinanceApplication
				.getFinanceUIConstants().purchasePrice());
		purchasePriceTxt.setHelpInformation(true);
		purchasePriceTxt.setWidth(100);
		// FIXME--needto implement this feature
		// purchasePriceTxt.setValidators(floatRangeValidator);
		// purchasePriceTxt.setValidateOnChange(true);

		expAccCombo = new PurchaseItemCombo(FinanceApplication
				.getFinanceUIConstants().expenseAccount());
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
		prefVendorCombo = new VendorCombo(UIUtils.getVendorString(
				FinanceApplication.getCustomersMessages().preferredSupplier(),
				FinanceApplication.getCustomersMessages().preferredVendor()));
		prefVendorCombo.setHelpInformation(true);
		prefVendorCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {
					public void selectedComboBoxItem(ClientVendor selectItem) {
						selectVendor = selectItem;
					}
				});
		vendItemNumText = new IntegerField(this.type != TYPE_SERVICE ? UIUtils
				.getVendorString(FinanceApplication.getCustomersMessages()
						.supplierProductNo(), FinanceApplication
						.getCustomersMessages().vendorProductNo()) : UIUtils
				.getVendorString(FinanceApplication.getCustomersMessages()
						.supplierServiceNo(), FinanceApplication
						.getCustomersMessages().vendorServiceNo()));
		vendItemNumText.setHelpInformation(true);
		vendItemNumText.setWidth(100);

		// isellCheck = new CheckboxItem(FinanceApplication
		// .getFinanceUIConstants().iSellThisItem());
		isellCheck = new CheckboxItem(
				this.type == TYPE_SERVICE ? FinanceApplication
						.getCustomersMessages().isellthisservice()
						: FinanceApplication.getCustomersMessages()
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
		// CheckboxItem(FinanceApplication.getFinanceUIConstants()
		// .iBuyThisItem());
		ibuyCheck = new CheckboxItem(
				this.type == TYPE_SERVICE ? FinanceApplication
						.getCustomersMessages().ibuythisservice()
						: FinanceApplication.getCustomersMessages()
								.ibuythisproduct());
		ibuyCheck.setValue(!isGeneratedFromCustomer);

		ibuyCheck.addChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				disablePurchaseFormItems(!event.getValue());

			}

		});

		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK)
			salesInfoForm.setFields(isellCheck, salesDescArea, salesPriceText,
					accountCombo, comCheck,stdCostText);
		else
			salesInfoForm.setFields(isservice, isellCheck, salesDescArea,
					salesPriceText, accountCombo, itemTaxCheck, comCheck,stdCostText);
		salesInfoForm.setStyleName("align-form");
		salesInfoForm.getCellFormatter().addStyleName(1, 0, "memoFormAlign");
		itemInfoForm = UIUtils.form(FinanceApplication.getFinanceUIConstants()
				.itemInformation());
		itemInfoForm.setWidth("96%");
		if (FinanceApplication.getCompany().getAccountingType() == 1)
			itemInfoForm.setFields(itemGroupCombo, taxCode, activeCheck);
		else
			itemInfoForm.setFields(itemGroupCombo, activeCheck);
		itemInfoForm.getCellFormatter().setWidth(0, 0, "124");
		purchaseInfoForm = UIUtils.form(FinanceApplication
				.getFinanceUIConstants().purchaseInformation());
		purchaseInfoForm.setNumCols(2);
		purchaseInfoForm.setWidth("92%");
		purchaseInfoForm
				.setFields(ibuyCheck, purchaseDescArea, purchasePriceTxt,
						expAccCombo, prefVendorCombo, vendItemNumText);
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
//		salesVPanel.add(stdCostForm);

		VerticalPanel purchzVPanel = new VerticalPanel();

		purchzVPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		purchzVPanel.setWidth("100%");
		HorizontalPanel itemInfoPanel = new HorizontalPanel();

		itemInfoPanel.setCellHorizontalAlignment(itemInfoForm, ALIGN_RIGHT);
		// itemInfoPanel.add(itemInfoForm);

		purchzVPanel.add(purchaseInfoForm);

		HorizontalPanel topPanel1 = new HorizontalPanel();
		topPanel1.setSpacing(15);
		// topPanel1.setStyleName("equal-columns");
		topPanel1.setHorizontalAlignment(ALIGN_RIGHT);
		topPanel1.setWidth("100%");
		topPanel1.add(itemForm);
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

		topHLay.add(topPanel1);
		topHLay.add(topPanel2);
		topHLay.setCellWidth(topPanel1, "50%");
		topHLay.setCellWidth(topPanel2, "50%");
		VerticalPanel mainVLay = new VerticalPanel();

		mainVLay.setSize("100%", "100%");
		mainVLay.add(hPanel);
		mainVLay.add(topHLay);

		canvas.add(mainVLay);

		if (takenItem != null) {
			nameText.setValue(takenItem.getName());
			stdCostText.setAmount(takenItem.getStandardCost());
			if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
				skuText.setValue(takenItem.getUPCorSKU() != null ? takenItem
						.getUPCorSKU() : "");

			weightText.setValue(UIUtils.toLong(takenItem.getWeight()));

			isellCheck.setValue(takenItem.isISellThisItem());
			if (takenItem.getSalesDescription() != null)
				salesDescArea.setValue(takenItem.getSalesDescription());
			salesPriceText.setAmount(takenItem.getSalesPrice());

			ClientCompany company = FinanceApplication.getCompany();
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

			if (Utility.isObjectExist(FinanceApplication.getCompany()
					.getItems(), item.getName())) {
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
		@SuppressWarnings("unused")
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
			item.setItemGroup(selectItemGroup.getStringID());
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
				item.setIncomeAccount(selectAccount.getStringID());
			item.setCommissionItem(getBooleanValue(comCheck));
		}

		if (getBooleanValue(ibuyCheck)) {

			item.setPurchaseDescription(getStringValue(purchaseDescArea));
			item.setPurchasePrice(purchasePriceTxt.getAmount());
			if (selectVendor != null)
				item.setPreferredVendor(selectVendor.getStringID());
			if (selectExpAccount != null)
				item.setExpenseAccount(selectExpAccount.getStringID());

			item.setVendorItemNumber(vendItemNumText.getValue().toString());
		}
		if (FinanceApplication.getCompany().getAccountingType() == 0)
			item.setTaxable(getBooleanValue(itemTaxCheck));
		else
			item.setTaxable(true);
		if (type == NON_INVENTORY_PART || type == TYPE_SERVICE)
			item.setTaxCode(selectTaxCode != null ? selectTaxCode.getStringID()
					: null);
		return item;
	}

	@Override
	public void saveFailed(Throwable exception) {
		super.saveFailed(exception);
		Accounter
				.showError(this.type != TYPE_SERVICE ? "Duplication of Product name are not allowed..."
						: "Duplication of Service name are not allowed...");
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		if (result == null) {
			saveFailed(new Exception());
			return;
		} else {
			// if (takenItem == null)
			// Accounter.showInformation(FinanceApplication
			// .getFinanceUIConstants().itemCreatedSuccessfully());
			// else
			// Accounter.showInformation(FinanceApplication
			// .getFinanceUIConstants().itemupdatedSuccessfully());
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
		List<ClientItemGroup> clientItemgroup = FinanceApplication.getCompany()
				.getItemGroups();

		if (clientItemgroup != null) {
			itemGroupCombo.initCombo(clientItemgroup);
			if (takenItem != null) {
				itemGroupCombo.setComboItem(FinanceApplication.getCompany()
						.getItemGroup(takenItem.getItemGroup()));
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
		List<ClientVendor> clientVendor = FinanceApplication.getCompany()
				.getActiveVendors();
		if (clientVendor != null) {
			prefVendorCombo.initCombo(clientVendor);
			if (takenItem != null) {
				prefVendorCombo.setComboItem(FinanceApplication.getCompany()
						.getVendor(takenItem.getPreferredVendor()));
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
					defaultIncomeAccount = getDefaultAccount(company
							.getServiceItemDefaultIncomeAccount());
					defaultExpAccount = getDefaultAccount(company
							.getServiceItemDefaultExpenseAccount());

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
					defaultIncomeAccount = getDefaultAccount(company
							.getNonInventoryItemDefaultIncomeAccount());
					defaultExpAccount = getDefaultAccount(company
							.getNonInventoryItemDefaultExpenseAccount());

				}
				selectAccount = defaultIncomeAccount;
				accountCombo.setComboItem(defaultIncomeAccount);
				selectExpAccount = defaultExpAccount;
				expAccCombo.setComboItem(defaultExpAccount);
			}
		}
		if (takenItem != null) {

			accountCombo.setComboItem(FinanceApplication.getCompany()
					.getAccount(takenItem.getIncomeAccount()));
			if (takenItem.isISellThisItem() == false)
				accountCombo.setDisabled(true);
			selectExpAccount = FinanceApplication.getCompany().getAccount(
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
					Accounter
							.showError("An Item already exists with this name");
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
			return AccounterValidator.validateForm(itemForm);
		case 7:
			return AccounterValidator.isSellorBuyCheck(isellCheck, ibuyCheck);
		case 6:
			if (AccounterValidator.isChecked(isellCheck))
				return AccounterValidator.validateForm(salesInfoForm);
			return true;
		case 5:
			boolean result = true;
			if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
				result = AccounterValidator.validateForm(itemInfoForm);
			}
			if (AccounterValidator.isChecked(ibuyCheck))
				result = AccounterValidator.validateForm(purchaseInfoForm);
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
			if (selectAccount != null)
				if (AccounterValidator.isChecked(isellCheck))
					return AccounterValidator.validate_IncomeAccount(this,
							selectAccount);
				else
					return true;

		case 1:
			if (AccounterValidator.isChecked(ibuyCheck))
				return AccounterValidator.validate_ExpenseAccount(this,
						selectExpAccount);
			else
				return true;
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
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(Boolean result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fitToSize(int height, int width) {
		// TODO Auto-generated method stub

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
					&& FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK)
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
					&& FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK)
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
					&& FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK)
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
		// TODO Auto-generated method stub

	}
}
