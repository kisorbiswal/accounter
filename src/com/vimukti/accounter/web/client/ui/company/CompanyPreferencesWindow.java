//package com.vimukti.accounter.web.client.ui.company;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.LinkedHashMap;
//import java.util.List;
//
//import com.google.gwt.core.client.GWT;
//import com.google.gwt.event.dom.client.ChangeEvent;
//import com.google.gwt.event.dom.client.ChangeHandler;
//import com.google.gwt.event.dom.client.ClickEvent;
//import com.google.gwt.event.dom.client.ClickHandler;
//import com.google.gwt.user.client.rpc.IsSerializable;
//import com.google.gwt.user.client.ui.Button;
//import com.google.gwt.user.client.ui.DecoratedTabPanel;
//import com.google.gwt.user.client.ui.HorizontalPanel;
//import com.google.gwt.user.client.ui.VerticalPanel;
//import com.google.gwt.user.client.ui.Widget;
//import com.vimukti.accounter.web.client.IAccounterCRUDServiceAsync;
//import com.vimukti.accounter.web.client.IAccounterGETServiceAsync;
//import com.vimukti.accounter.web.client.core.AccounterConstants;
//import com.vimukti.accounter.web.client.core.ClientAccount;
//import com.vimukti.accounter.web.client.core.ClientCompany;
//import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
//import com.vimukti.accounter.web.client.ui.FinanceApplication;
//import com.vimukti.accounter.web.client.ui.UIUtils;
//import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
//import com.vimukti.accounter.web.client.ui.combo.SelectItemType;
//import com.vimukti.accounter.web.client.ui.core.Accounter;
//import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
//import com.vimukti.accounter.web.client.ui.core.BaseView;
//import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
//import com.vimukti.accounter.web.client.ui.forms.DateItem;
//import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
//import com.vimukti.accounter.web.client.ui.forms.FormItem;
//import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;
//import com.vimukti.accounter.web.client.ui.forms.SelectItem;
//
//public class CompanyPreferencesWindow extends BaseView {
//	private DecoratedTabPanel tabSet;
//	private VerticalPanel companyLayOut, sysAccountsLayOut, generalLayOut;
//	private CompanyMessages companyMessges;
//	private ClientCompany company;
//	private List<ClientAccount> accounts = new ArrayList<ClientAccount>();
//	private HorizontalPanel hlLayout;
//	private Button helpButt;
//	private Button ok;
//	private Button cancel;
//	private IAccounterGETServiceAsync getService;
//	private IAccounterCRUDServiceAsync crudService;
//	private CheckboxItem useAccountscheckbox, useCustomertID, useVendorId,
//			allowDocumentNos, doupaySalesChecBox, playsounds;
//	private DateItem dateItem;
//	private RadioGroupItem whowillrunAccTransfer, paysalesTaxgroupItem;
//	private Button buttonItem, taxgroupBtn, clearlogBtn,
//			mangeServiceMappingsBtn;
//	private CustomCombo openinBalcombo, accountsreceivablecombo,
//			accountsPayablecombo, salesTaxPayablecombo, cashDiscountGivencombo,
//			cashDiscountTakencombo, payrollLiabilitycombo,
//			undepositedFoundscombo, bankChargecombo, retainedEarningsCombo,
//			pendingItemrecicptsCombo, jobresellAccountCombo,
//			writeOffAccountCombo, defaultCashCombo;
//	// XXX NOT USED --Previously 'logspaceTxt' was FloatItem
//	private FormItem logspaceTxt;
//	private SelectItem logContentSelect, defautFileAsSettings;
//	private boolean isCancel = false;
//
//	public CompanyPreferencesWindow() {
//		this.company = FinanceApplication.getCompany();
//		createControls();
//		this.getService = FinanceApplication.createGETService();
//		this.crudService = FinanceApplication.createCRUDService();
//
//		getAccounts();
//
//		initCompany();
//	}
//
//	protected void initCompany() {
//		ClientCompanyPreferences companyPreferences = company.getPreferences();
//		// XXX NOT USED Call Back to get ClientCompanyPreferences Object of Company
//		if (companyPreferences != null) {
//
//			dateItem.setValue(new Date(companyPreferences
//					.getStartOfFiscalYear()));
//			if (companyPreferences.getIsMyAccountantWillrunPayroll() != false)
//				whowillrunAccTransfer.setValue(companyPreferences
//						.getIsMyAccountantWillrunPayroll());
//
//			useAccountscheckbox.setValue(companyPreferences
//					.getUseAccountNumbers());
//			useCustomertID.setValue(companyPreferences.getUseCustomerId());
//			useVendorId.setValue(companyPreferences.getUseVendorId());
//			allowDocumentNos.setValue(companyPreferences
//					.getAllowDuplicateDocumentNumbers());
//
//			doupaySalesChecBox.setValue(companyPreferences
//					.getDoYouPaySalesTax());
//
//			if ((Double) companyPreferences.getLogSpaceUsed() != null)
//				logspaceTxt.setValue(companyPreferences.getLogSpaceUsed());
//
//			if (companyPreferences.getIsAccuralBasis())
//				paysalesTaxgroupItem.setValue(companyPreferences
//						.getIsAccuralBasis());
//
//		}
//	}
//
//	@Override
//	public void init() {
//		super.init();
//		createControls();
//		setSize("100%", "100%");
//		// setOverflow(Overflow.AUTO);
//
//	}
//
//	@Override
//	public void initData() {
//		// TODO Auto-generated method stub
//		super.initData();
//	}
//
//	private void createControls() {
//		this.companyMessges = (CompanyMessages) GWT
//				.create(CompanyMessages.class);
//		setTitle(this.companyMessges.companyPrefeTitle());
//		this.tabSet = new DecoratedTabPanel();
//		this.tabSet.add(getCompanyTab(), this.companyMessges.company());
//		this.tabSet.add(getSystemAccountsTab(), this.companyMessges
//				.systemAccounts());
//		this.tabSet.add(getGerneralTab(), this.companyMessges.general());
//
//		hlLayout = new HorizontalPanel();
//		hlLayout.setHeight("30");
//		// hlLayout.setAlign(Alignment.RIGHT);
//		// hlLayout.setMembersMargin(20);
//		// hlLayout.setAutoHeight();
//		// hlLayout.setLayoutLeftMargin(5);
//		// hlLayout.setLayoutRightMargin(5);
//
//		helpButt = new Button(this.companyMessges.help());
//		// helpButt.setHeight("30");
//		// helpButt.setAutoFit(true);
//		helpButt.addClickHandler(new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//				Accounter
//						.showInformation(AccounterWarningType.NOT_YET_IMPLEMENTED);
//			}
//
//		});
//
//		HorizontalPanel helpLayout = new HorizontalPanel();
//		helpLayout.setWidth("50%");
//		helpLayout.add(helpButt);
//
//		HorizontalPanel okCancelayout = new HorizontalPanel();
//		okCancelayout.setWidth("100%");
//		// okCancelayout.setMembersMargin(10);
//		// okCancelayout.setAlign(Alignment.RIGHT);
//
//		ok = new Button(this.companyMessges.ok());
//		// ok.setAutoFit(true);
//		ok.addClickHandler(new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//				savePreference();
//
//			}
//		});
//
//		cancel = new Button(this.companyMessges.cancel());
//		// cancel.setAutoFit(true);
//		cancel.addClickHandler(new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//				isCancel = true;
//				saveAndClose = true;
//				saveSuccess(null);
//			}
//		});
//
//		okCancelayout.add(ok);
//		okCancelayout.add(cancel);
//		hlLayout.add(helpLayout);
//		hlLayout.add(okCancelayout);
//		// hlLayout.setLayoutTopMargin(10);
//		// hlLayout.setLayoutBottomMargin(10);
//
//		VerticalPanel mainLayout = new VerticalPanel();
//		mainLayout.setSize("100%", "100%");
//		mainLayout.add(this.tabSet);
//		mainLayout.add(hlLayout);
//
//		add(mainLayout);
//	}
//
//	protected void savePreference() {
//		ClientCompanyPreferences companyPreferences = company.getPreferences();
//		if (companyPreferences == null) {
//			companyPreferences = new ClientCompanyPreferences();
//		}
//		companyPreferences
//				.setAllowDuplicateDocumentNumbers(getBooleanValue(allowDocumentNos));
//		companyPreferences
//				.setDoYouPaySalesTax(getBooleanValue(doupaySalesChecBox));
//		companyPreferences.setIsAccuralBasis(paysalesTaxgroupItem.getValue()
//				.equals("2"));
//		System.out.println("radio value:" + whowillrunAccTransfer.getValue());
//
//		companyPreferences
//				.setUseAccountNumbers(getBooleanValue(useAccountscheckbox));
//		companyPreferences.setUseCustomerId(getBooleanValue(useCustomertID));
//		companyPreferences.setUseVendorId(getBooleanValue(useVendorId));
//		if (dateItem.getValue() != null)
//			companyPreferences.setEndOfFiscalYear((Date) dateItem.getValue());
//		companyPreferences
//				.setIsMyAccountantWillrunPayroll(whowillrunAccTransfer
//						.getValue().equals("2"));
//		companyPreferences.setLogSpaceUsed(getDoubleValue(logspaceTxt));
//
//		saveAndClose = true;
//		FinanceApplication.createCRUDService().alterCompany(company,
//				UIUtils.getGeneralizedSaveCallBack(this));
//	}
//
//	@Override
//	protected void saveSuccess(IsSerializable result) {
//		if (result != null || isCancel == true) {
//			if (isCancel != true)
//				Accounter.showInformation("Updated Successfully!");
//			super.saveSuccess(result);
//		} else
//			saveFailed(new Exception("Failed!"));
//
//	}
//
//	@Override
//	protected void saveFailed(Throwable exception) {
//		super.saveFailed(exception);
//		Accounter.showError("Failed to update!!");
//	}
//
//	private Double getDoubleValue(FormItem item) {
//		return Double.parseDouble(item.getValue() != null
//				&& !item.getValue().equals("") ? item.getValue().toString()
//				.replace(""+UIUtils.getCurrencySymbol() +"", "") : "0.0");
//	}
//
//	public String getStringValue(FormItem item) {
//		return item.getValue() != null ? item.getValue().toString() : "";
//
//	}
//
//	public boolean getBooleanValue(FormItem item) {
//		return item.getValue() != null ? (Boolean) item.getValue() : false;
//
//	}
//
//	private void getAccounts() {
//		this.accounts = FinanceApplication.getCompany().getAccounts();
//		initAllCombos(accounts);
//
//		// FinanceApplication.createGETService().getAccounts(
//		// new AsyncCallback<List<ClientAccount>>() {
//		//
//		// public void onFailure(Throwable caught) {
//		//
//		// }
//		//
//		// public void onSuccess(List<ClientAccount> result) {
//		// initAllCombos(result);
//		// }
//		// });
//	}
//
//	public VerticalPanel getCompanyTab() {
//		this.companyLayOut = new VerticalPanel();
//		/**
//		 * Numbers and IDs Group
//		 */
//		DynamicForm numbersIdsvForm = UIUtils.form(this.companyMessges
//				.getNumbersandIdsTitle());
//
//		useAccountscheckbox = new CheckboxItem();
//		useAccountscheckbox.setTitle(this.companyMessges.useAccountNos());
//
//		useCustomertID = new CheckboxItem();
//		useCustomertID.setTitle(this.companyMessges.useCustomerId());
//
//		useVendorId = new CheckboxItem();
//		useVendorId.setTitle(this.companyMessges.useVendorId());
//
//		allowDocumentNos = new CheckboxItem();
//		allowDocumentNos.setTitle(this.companyMessges.allowDocNos());
//
//		numbersIdsvForm.setItems(useAccountscheckbox, useCustomertID,
//				useVendorId, allowDocumentNos);
//		// numbersIdsvForm.setPadding(10);
//
//		/**
//		 * Fiscal YearGroup
//		 * */
//		DynamicForm fiscalYrForm = UIUtils.form(this.companyMessges
//				.fiscalYear());
//		// fiscalYrForm.setPadding(10);
//
//		dateItem = new DateItem();
//		dateItem.setTitle(this.companyMessges.preventPostBefore());
//		dateItem.setUseTextField(true);
//
//		fiscalYrForm.setItems(dateItem);
//
//		/**
//		 * Account Transfer Group
//		 */
//		DynamicForm accountantTransferForm = UIUtils.form((this.companyMessges
//				.accountantTransfer()));
//		// accountantTransferForm.setWidth100();
//		// accountantTransferForm.setTitleOrientation(TitleOrientation.TOP);
//		// accountantTransferForm.setPadding(10);
//
//		whowillrunAccTransfer = new RadioGroupItem();
//		whowillrunAccTransfer.setTitle(this.companyMessges
//				.whowillrunwhenaccoutantTransfer());
//		// whowillrunAccTransfer.setColSpan("*");
//		whowillrunAccTransfer.setVertical(false);
//		LinkedHashMap<String, String> hashMap = new LinkedHashMap<String, String>();
//		hashMap.put("1", this.companyMessges.myAccountantwillrun());
//		hashMap.put("2", this.companyMessges.iwouldliketoberunPayroll());
//		whowillrunAccTransfer.setValueMap(hashMap);
//		Boolean value = company.getPreferences() != null ? company
//				.getPreferences().getIsMyAccountantWillrunPayroll() : false;
//		if (value == null)
//			value = Boolean.TRUE;
//		whowillrunAccTransfer.setDefaultValue(value ? "1" : "2");
//
//		accountantTransferForm.setItems(whowillrunAccTransfer);
//
//		/**
//		 * Taxes Group
//		 */
//
//		DynamicForm taxesForm = new DynamicForm();
//		// taxesForm.setWidth100();
////		taxesForm.setHeight("*");
//		taxesForm.setIsGroup(true);
//		taxesForm.setGroupTitle(this.companyMessges.taxes());
//		// taxesForm.setTitleOrientation(TitleOrientation.TOP);
//		// taxesForm.setPadding(10);
//
//		doupaySalesChecBox = new CheckboxItem();
//		// doupaySalesChecBox.setColSpan("2");
//		doupaySalesChecBox.setAttribute("vertical", false);
//		doupaySalesChecBox.setTitle(this.companyMessges.doYoupaySalesTaxes());
//		doupaySalesChecBox.addChangeHandler(new ChangeHandler() {
//
//			public void onChange(ChangeEvent event) {
//				if ((Boolean) ((CheckboxItem) event.getSource()).getValue())
//					taxgroupBtn.setEnabled(false);
//				else
//					taxgroupBtn.setEnabled(true);
//			}
//		});
//
//		taxgroupBtn = new Button();
//		// taxgroupBtn.setColSpan("*");
//		taxgroupBtn.setTitle(this.companyMessges.taxgroups());
//		taxgroupBtn.addClickHandler(new ClickHandler() {
//
//			public void onClick(ClickEvent event) {
//				taxGroupButtonClick();
//			}
//		});
//		paysalesTaxgroupItem = new RadioGroupItem();
//		paysalesTaxgroupItem.setTitle(this.companyMessges
//				.onWhatbasisdoUpaySalesTaxes());
//		// paysalesTaxgroupItem.setColSpan("*");
//		paysalesTaxgroupItem.setVertical(false);
//		paysalesTaxgroupItem
//				.setValue(company.getPreferences() != null ? company
//						.getPreferences().getIsAccuralBasis() ? "1" : "2" : "1");
//
//		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
//		map.put("1", this.companyMessges.accrualBasis());
//		map.put("2", this.companyMessges.cashBasis());
//		paysalesTaxgroupItem.setValueMap(map);
//
//		taxesForm.setItems(doupaySalesChecBox, paysalesTaxgroupItem);
//
//		/**
//		 * add all forms to company LayOut
//		 */
//		this.companyLayOut.add(numbersIdsvForm);
//		this.companyLayOut.add(fiscalYrForm);
//		this.companyLayOut.add(accountantTransferForm);
//		this.companyLayOut.add(taxesForm);
//		this.companyLayOut.add(taxgroupBtn);
//
//		VerticalPanel tab = new VerticalPanel();
//		tab.add(this.companyLayOut);
//		return tab;
//	}
//
//	protected void taxGroupButtonClick() {
//		new ManageSalesTaxGroupsAction("").run(null, false);
//
//	}
//
//	public VerticalPanel getSystemAccountsTab() {
//		/**
//		 * System Account Group
//		 */
//		this.sysAccountsLayOut = new VerticalPanel();
//
//		DynamicForm systemAccountForm = new DynamicForm();
//		// systemAccountForm.setWidth100();
//		// systemAccountForm.setHeight100();
//		systemAccountForm.setIsGroup(true);
//		systemAccountForm.setGroupTitle(this.companyMessges.systemAccounts());
//		// systemAccountForm.setPadding(30);
//
//		openinBalcombo = new CustomCombo(this.companyMessges.openingBalances(),
//				SelectItemType.ACCOUNT, true);
//
//		openinBalcombo.initCombo(accounts);
//
//		openinBalcombo.setWidth(250);
//		// openinBalcombo.setWrapTitle(false);
//		accountsreceivablecombo = new CustomCombo(this.companyMessges
//				.accountsReceivable(), SelectItemType.ACCOUNT, true);
//		accountsreceivablecombo.initCombo(accounts);
//
//		accountsreceivablecombo.setWidth(250);
//		// accountsreceivablecombo.setWrapTitle(false);
//		accountsPayablecombo = new CustomCombo(this.companyMessges
//				.accountsPayable(), SelectItemType.ACCOUNT, true);
//		accountsPayablecombo.initCombo(accounts);
//		accountsPayablecombo.setWidth(250);
//		// accountsPayablecombo.setWrapTitle(false);
//		salesTaxPayablecombo = new CustomCombo(this.companyMessges
//				.salesTaxPayable(), SelectItemType.ACCOUNT, true);
//		salesTaxPayablecombo.initCombo(accounts);
//
//		salesTaxPayablecombo.setWidth(250);
//		// salesTaxPayablecombo.setWrapTitle(false);
//		cashDiscountGivencombo = new CustomCombo(this.companyMessges
//				.cashDiscountGiven(), SelectItemType.ACCOUNT, true);
//		cashDiscountGivencombo.initCombo(accounts);
//
//		cashDiscountGivencombo.setWidth(250);
//		// cashDiscountGivencombo.setWrapTitle(false);
//		cashDiscountTakencombo = new CustomCombo(this.companyMessges
//				.cashDiscountTaken(), SelectItemType.ACCOUNT, true);
//
//		cashDiscountTakencombo.initCombo(accounts);
//		cashDiscountTakencombo.setWidth(250);
//		// cashDiscountTakencombo.setWrapTitle(false);
//
//		payrollLiabilitycombo = new CustomCombo(this.companyMessges
//				.payrollLiability(), SelectItemType.ACCOUNT, true);
//		payrollLiabilitycombo.initCombo(accounts);
//		payrollLiabilitycombo.setWidth(250);
//		undepositedFoundscombo = new CustomCombo(this.companyMessges
//				.undepositedFounds(), SelectItemType.ACCOUNT, true);
//
//		undepositedFoundscombo.initCombo(accounts);
//		undepositedFoundscombo.setWidth(250);
//		bankChargecombo = new CustomCombo(this.companyMessges.bankCharge(),
//				SelectItemType.ACCOUNT, true);
//
//		bankChargecombo.initCombo(accounts);
//		bankChargecombo.setWidth(250);
//		retainedEarningsCombo = new CustomCombo(this.companyMessges
//				.retainedEarnings(), SelectItemType.ACCOUNT, true);
//		retainedEarningsCombo.initCombo(accounts);
//
//		retainedEarningsCombo.setWidth(250);
//		pendingItemrecicptsCombo = new CustomCombo(this.companyMessges
//				.pendingItemreceipts(), SelectItemType.ACCOUNT, true);
//		pendingItemrecicptsCombo.initCombo(accounts);
//		pendingItemrecicptsCombo.setWidth(250);
//		// pendingItemrecicptsCombo.setWrapTitle(false);
//		jobresellAccountCombo = new CustomCombo(this.companyMessges
//				.jobresellAccount(), SelectItemType.ACCOUNT, true);
//		jobresellAccountCombo.initCombo(accounts);
//
//		jobresellAccountCombo.setWidth(250);
//		writeOffAccountCombo = new CustomCombo(this.companyMessges
//				.writeOffAccount(), SelectItemType.ACCOUNT, true);
//		writeOffAccountCombo.initCombo(accounts);
//		writeOffAccountCombo.setWidth(250);
//		defaultCashCombo = new CustomCombo(this.companyMessges
//				.defaultAccounts(), SelectItemType.ACCOUNT, true);
//
//		defaultCashCombo.initCombo(accounts);
//		defaultCashCombo.setWidth(250);
//
//		systemAccountForm.setItems(openinBalcombo, accountsreceivablecombo,
//				accountsPayablecombo, salesTaxPayablecombo,
//				cashDiscountGivencombo, cashDiscountTakencombo,
//				payrollLiabilitycombo, undepositedFoundscombo, bankChargecombo,
//				retainedEarningsCombo, pendingItemrecicptsCombo,
//				jobresellAccountCombo, writeOffAccountCombo, defaultCashCombo);
//		this.sysAccountsLayOut.add(systemAccountForm);
//
//		VerticalPanel tab = new VerticalPanel();
//		tab.add(this.sysAccountsLayOut);
//
//		return tab;
//	}
//
//	public VerticalPanel getGerneralTab() {
//		/**
//		 * User Preferences Group
//		 */
//		DynamicForm userPreferenceForm = new DynamicForm();
//		// userPreferenceForm.setWidth100();
//		userPreferenceForm.setIsGroup(true);
//		// userPreferenceForm.setTitleOrientation(TitleOrientation.TOP);
//		userPreferenceForm.setGroupTitle(this.companyMessges.userPreference());
//
//		playsounds = new CheckboxItem();
//		// playsounds.setTitleOrientation(TitleOrientation.TOP);
//		playsounds.setTitle(this.companyMessges.playsoundswithActions());
//
//		buttonItem = new Button(this.companyMessges.restoreDefault());
//		// buttonItem.setAutoFit(true);
//		buttonItem.addClickHandler(new ClickHandler() {
//
//			public void onClick(ClickEvent event) {
//				restoreDefault();
//			}
//		});
//		VerticalPanel userPrefPanel = new VerticalPanel();
//		userPrefPanel.add(userPreferenceForm);
//		userPreferenceForm.add(buttonItem);
//		// userPreferenceForm.setPadding(10);
//		/**
//		 * filing record Name group
//		 */
//		DynamicForm filingRecordNameForm = new DynamicForm();
//		// filingRecordNameForm.setWidth100();
//		filingRecordNameForm.setIsGroup(true);
//		// filingRecordNameForm.setAlign(Alignment.CENTER);
//		filingRecordNameForm.setGroupTitle(this.companyMessges
//				.filingRecordsName());
//
//		defautFileAsSettings = new SelectItem();
//		// defautFileAsSettings.setWidth("*");
//		// defautFileAsSettings.setHeight("20");
//		defautFileAsSettings.setDefaultValue("Company");
//		defautFileAsSettings.setTitle(this.companyMessges
//				.defaultFileAsSettings());
//		defautFileAsSettings
//				.setValueMap("Company", "First last", "Last, First");
//		filingRecordNameForm.setItems(defautFileAsSettings);
//		// filingRecordNameForm.setPadding(10);
//
//		/**
//		 * service Map Group
//		 */
//		DynamicForm serviceMapForm = new DynamicForm();
//		// serviceMapForm.setWidth100();
//		serviceMapForm.setIsGroup(true);
//		serviceMapForm.setGroupTitle(this.companyMessges.serviceMapping());
//		// serviceMapForm.setPadding(10);
//
//		// XXX--Previusly 'mangeServiceMappingsBtn' is ButtonItem
//		mangeServiceMappingsBtn = new Button();
//		mangeServiceMappingsBtn.setTitle(this.companyMessges
//				.manageServiceMappings());
//		mangeServiceMappingsBtn.setHeight("30");
//
//		serviceMapForm.add(mangeServiceMappingsBtn);
//
//		/**
//		 * supporting Logging
//		 */
//		DynamicForm supportLogging = new DynamicForm();
//		// supportLogging.setWidth100();
//		supportLogging.setHeight("*");
//		supportLogging.setIsGroup(true);
//		// supportLogging.setTitleOrientation(TitleOrientation.TOP);
//		supportLogging.setGroupTitle(this.companyMessges.supportLogging());
//		// supportLogging.setTop(20);
//		// supportLogging.setPadding(20);
//
//		// XXX NOT USED --Previusly 'logspaceTxt' is FloatItem
//		logspaceTxt = new FormItem() {
//
//			@Override
//			public Widget getMainWidget() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//		};
//		logspaceTxt.setTitle(this.companyMessges.logspsace());
//
//		clearlogBtn = new Button();
//		clearlogBtn.setTitle(this.companyMessges.clearLog());
//
//		logContentSelect = new SelectItem();
//		logContentSelect.setTitle(this.companyMessges.logContent());
//		logContentSelect.setDefaultValue("Do not Log");
//		logContentSelect.setValueMap("Do not log", "Critical errors Only",
//				"All errors", "All errors and messages");
//
//		supportLogging.setItems(logspaceTxt, logContentSelect);
//		VerticalPanel suportLogPanel = new VerticalPanel();
//		suportLogPanel.add(supportLogging);
//		// XXX NOT IN USE
//		suportLogPanel.add(clearlogBtn);
//
//		this.generalLayOut = new VerticalPanel();
//		this.generalLayOut.add(userPrefPanel);
//		this.generalLayOut.add(filingRecordNameForm);
//		this.generalLayOut.add(serviceMapForm);
//		this.generalLayOut.add(supportLogging);
//		// generalLayOut.add(10);
//
//		VerticalPanel generalTab = new VerticalPanel();
//		generalTab.add(this.generalLayOut);
//		return generalTab;
//	}
//
//	protected void restoreDefault() {
//
//	}
//
//	public void initAllCombos(List<ClientAccount> accountlist) {
//		ClientAccount openingBalaccount = null;
//		ClientAccount accountsReceivable = null;
//		ClientAccount accountsPayable = null;
//		ClientAccount salesTaxpayable = null;
//		ClientAccount cashDiscountGiven = null;
//		ClientAccount cashDiscountTaken = null;
//		ClientAccount payRollLiability = null;
//		ClientAccount undepositedFunds = null;
//		ClientAccount bankCharge = null;
//		ClientAccount retainedEarnings = null;
//		ClientAccount pendingItemrecicpts = null;
//
//		ClientAccount writeOffAccount = null;
//
//		for (ClientAccount account : accountlist) {
//			System.out.println("Account Name " + account.getName());
//			if (account.getName().equals(AccounterConstants.OPENING_BALANCE))
//				openingBalaccount = account;
//			else if (account.getName().equals(
//					AccounterConstants.ACCOUNTS_RECEIVABLE))
//				accountsReceivable = account;
//			else if (account.getName().equals(
//					AccounterConstants.ACCOUNTS_PAYABLE))
//				accountsPayable = account;
//			else if (account.getName().equals(
//					AccounterConstants.SALES_TAX_PAYABLE))
//				salesTaxpayable = account;
//			else if (account.getName().equals(
//					AccounterConstants.CASH_DISCOUNT_GIVEN))
//				cashDiscountGiven = account;
//			else if (account.getName().equals(
//					AccounterConstants.CASH_DISCOUNT_TAKEN))
//				cashDiscountTaken = account;
//			else if (account.getName().equals(
//					AccounterConstants.EMPLOYEE_PAYROLL_LIABILITIES))
//				payRollLiability = account;
//			else if (account.getName().equals(
//					AccounterConstants.UN_DEPOSITED_FUNDS))
//				undepositedFunds = account;
//			else if (account.getName().equals(AccounterConstants.BANK_CHARGE))
//				bankCharge = account;
//			else if (account.getName().equals(
//					AccounterConstants.RETAINED_EARNINGS))
//				retainedEarnings = account;
//			else if (account.getName().equals(
//					AccounterConstants.PENDING_ITEM_RECEIPTS))
//				pendingItemrecicpts = account;
//
//			else if (account.getName().equals(AccounterConstants.WRITE_OFF))
//				writeOffAccount = account;
//
//		}
//		openinBalcombo.initCombo(accountlist);
//		openinBalcombo.setComboItem(openingBalaccount);
//
//		accountsreceivablecombo.initCombo(accountlist);
//		accountsreceivablecombo.setComboItem(accountsReceivable);
//		accountsPayablecombo.initCombo(accountlist);
//		accountsPayablecombo.setComboItem(accountsPayable);
//
//		salesTaxPayablecombo.initCombo(accountlist);
//		salesTaxPayablecombo.setComboItem(salesTaxpayable);
//		cashDiscountGivencombo.initCombo(accountlist);
//		cashDiscountGivencombo.setComboItem(cashDiscountGiven);
//
//		cashDiscountTakencombo.initCombo(accountlist);
//		cashDiscountTakencombo.setComboItem(cashDiscountTaken);
//
//		payrollLiabilitycombo.initCombo(accountlist);
//		payrollLiabilitycombo.setComboItem(payRollLiability);
//
//		undepositedFoundscombo.initCombo(accountlist);
//		undepositedFoundscombo.setComboItem(undepositedFunds);
//
//		bankChargecombo.initCombo(accountlist);
//		bankChargecombo.setComboItem(bankCharge);
//
//		retainedEarningsCombo.initCombo(accountlist);
//		retainedEarningsCombo.setComboItem(retainedEarnings);
//
//		pendingItemrecicptsCombo.initCombo(accountlist);
//		pendingItemrecicptsCombo.setComboItem(pendingItemrecicpts);
//
//		jobresellAccountCombo.initCombo(accountlist);
//		writeOffAccountCombo.initCombo(accountlist);
//		writeOffAccountCombo.setComboItem(writeOffAccount);
//		defaultCashCombo.initCombo(accountlist);
//		openinBalcombo.setDisabled(true);
//		accountsreceivablecombo.setDisabled(true);
//		salesTaxPayablecombo.setDisabled(true);
//		accountsPayablecombo.setDisabled(true);
//		cashDiscountGivencombo.setDisabled(true);
//		cashDiscountTakencombo.setDisabled(true);
//		payrollLiabilitycombo.setDisabled(true);
//		undepositedFoundscombo.setDisabled(true);
//		bankChargecombo.setDisabled(true);
//		retainedEarningsCombo.setDisabled(true);
//		pendingItemrecicptsCombo.setDisabled(true);
//		jobresellAccountCombo.setDisabled(true);
//		writeOffAccountCombo.setDisabled(true);
//		defaultCashCombo.setDisabled(true);
//	}
//
//	public void setDefaultValues() {
//
//	}
//
//	public void close() {
//		// destroy();
//	}
// }
