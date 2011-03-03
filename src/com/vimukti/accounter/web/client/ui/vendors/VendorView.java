package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientEmail;
import com.vimukti.accounter.web.client.core.ClientFax;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientFiscalYear;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientPhone;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientVendorGroup;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.AddressForm;
import com.vimukti.accounter.web.client.ui.EmailForm;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.PhoneFaxForm;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.OtherAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.PaymentTermsCombo;
import com.vimukti.accounter.web.client.ui.combo.ShippingMethodsCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.combo.VendorGroupCombo;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.EmailField;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.core.VendorsActionFactory;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.SelectItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.ContactGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

/**
 * 
 * @author venki.p modified by Rajesh.A and Ravi kiran.G, Murali
 * 
 */

public class VendorView extends BaseView<ClientVendor> {

	DynamicForm vendorForm, accInfoForm;

	TextItem vendorNameText, fileAsText, accountText, webText, linksText,
			expenseAccountsText, federalText;
	TextAreaItem memoArea;
	DateField balanceDate, vendorSinceDate;
	EmailField emailText;
	AmountField creditLimitText, balanceText;
	TextItem vatRegistrationNumber;

	PaymentTermsCombo payTermsSelect;
	ShippingMethodsCombo preferredShippingSelect;
	OtherAccountsCombo expenseAccountsSelect;
	TAXCodeCombo vendorTaxCode;
	VendorGroupCombo vendorGroupSelect;
	SelectItem preferredPaymentSelect;
	CheckboxItem euVATexempVendor;
	TabPanel tabSet;

	LinkedHashMap<String, ClientAddress> allAddresses;
	LinkedHashMap<String, ClientPhone> allPhones;
	LinkedHashMap<String, ClientFax> allFaxes;
	LinkedHashMap<String, ClientEmail> allEmails;

	CheckboxItem statusCheck;

	ContactGrid gridView;

	List<ClientFax> faxList;
	List<ClientEmail> emailList;
	List<ClientPhone> phoneList;
	List<ClientAddress> addressList;

	List<ClientShippingMethod> shipMethodList;
	List<String> payMethodList;
	List<ClientAccount> expenseAccountList;
	List<ClientPaymentTerms> payTermList;
	List<ClientVendorGroup> vendorGroupList;

	LinkedHashMap<String, String> payTermMap;
	LinkedHashMap<String, String> payMethodMap;
	LinkedHashMap<String, String> shipMethodMap;
	LinkedHashMap<String, String> vendorGroupMap;

	private ClientFiscalYear fiscalYear;

	protected ClientAccount selectAccountFromDetailsTab;
	protected ClientShippingMethod selectShippingMethodFromDetailsTab;
	protected String selectPaymentMethodFromDetialsTab;
	protected ClientPaymentTerms selectPaymentTermFromDetailsTab;
	protected ClientVendorGroup selectVendorGroupFromDetailsTab;

	private AddressForm addrsForm;
	private PhoneFaxForm fonFaxForm;
	private EmailForm emailForm;

	private ClientVendor takenVendor;
	protected ClientAccount accountPayableAccount;
	private ClientCompany company;
	private boolean wait;

	VendorsMessages vendorConstants;
	private ArrayList<DynamicForm> listforms;

	protected ClientTAXCode selectTaxCodeFromDetailsTab;

	public VendorView() {
		super();
		this.validationCount = 5;

	}

	private void getFiscalYear() {
		List<ClientFiscalYear> result = FinanceApplication.getCompany()
				.getFiscalYears();
		if (result != null && takenVendor == null) {
			for (ClientFiscalYear fiscalYear : result) {
				if (fiscalYear != null && fiscalYear.getIsCurrentFiscalYear()) {
					if (fiscalYear != null
							&& fiscalYear.getIsCurrentFiscalYear()) {
						VendorView.this.fiscalYear = fiscalYear;
						// balanceDate.setEnteredDate(VendorView.this.fiscalYear
						// .getStartDate());
						break;
					}
				}
			}
		}
	}

	private void createControls() {

		listforms = new ArrayList<DynamicForm>();

		// setTitle(UIUtils.title(vendorConstants.newVendor()));
		tabSet = new TabPanel();
		tabSet.setSize("100%", "100%");

		tabSet.add(getGeneralTab(), vendorConstants.general());
		tabSet.add(getDetailsTab(), vendorConstants.details());
		tabSet.selectTab(0);
		tabSet.setSize("100%", "100%");

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(tabSet);
		canvas.add(mainVLay);

	}

	@Override
	public void saveAndUpdateView() throws Exception {
		if (!wait) {
			try {
				ClientVendor vendor = getVendorObject();
				if (takenVendor == null) {
					if (Utility
							.isObjectExist(FinanceApplication.getCompany()
									.getVendors(), vendorNameText.getValue()
									.toString())) {
						if (tabSet.getTabBar().isTabEnabled(1)) {
							tabSet.selectTab(0);
							throw new InvalidEntryException("Vendor"
									+ AccounterErrorType.ALREADYEXIST);
						}
					} else
						createObject(vendor);
				} else
					alterObject(vendor);
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
	}

	@Override
	public boolean validate() throws InvalidEntryException,
			InvalidTransactionEntryException {

		switch (this.validationCount) {
		case 5:
			String name = vendorNameText.getValue().toString();
			if (takenVendor == null) {
				if (Utility.isObjectExist(company.getVendors(), name)) {
					throw new InvalidEntryException(
							AccounterErrorType.ALREADYEXIST);
				} else
					return true;
			} /*
			 * else if (takenVendor != null &&
			 * (!takenVendor.getName().equalsIgnoreCase(name))) { if
			 * (Utility.isObjectExist(company.getVendors(), name)) throw new
			 * InvalidEntryException( AccounterErrorType.ALREADYEXIST); else
			 * return true; }
			 */

		case 4:
			return AccounterValidator.validateForm(vendorForm);

		case 3:
			// Date vendorSince = vendorSinceDate.getEnteredDate();
			// return AccounterValidator.sinceDate(vendorSince, this);
			return true;

		case 2:
			ClientFinanceDate asOfDate = balanceDate.getEnteredDate();
			return AccounterValidator.isPriorAsOfDate(asOfDate, this);
			// return AccounterValidator.createNecessaryFiscalYears(fiscalYear,
			// asOfDate, this);
			// return true;
		case 1:
			return gridView.validateGrid();
		default:
			return true;

		}

	}

	@SuppressWarnings("deprecation")
	private VerticalPanel getGeneralTab() {
		vendorNameText = new TextItem(UIUtils.getVendorString(
				FinanceApplication.getVendorsMessages().supplierName(),
				FinanceApplication.getVendorsMessages().vendorName()));
		vendorNameText.setHelpInformation(true);
		vendorNameText.setRequired(true);
		vendorNameText.setWidth(100);

		fileAsText = new TextItem(vendorConstants.fileAs());
		fileAsText.setHelpInformation(true);
		fileAsText.setWidth(100);

		vendorNameText.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (event.getSource() != null)
					fileAsText.setValue(vendorNameText.getValue());
			}

		});

		vendorForm = UIUtils.form(UIUtils.getVendorString(FinanceApplication
				.getVendorsMessages().supplier(), FinanceApplication
				.getVendorsMessages().vendor()));
		vendorForm.setFields(vendorNameText);
		vendorForm.setWidth("100%");
		vendorForm.setStyleName(FinanceApplication.getVendorsMessages()
				.venderForm());
		vendorForm.getCellFormatter().setWidth(0, 0, "205");

		accInfoForm = new DynamicForm();
		accInfoForm.setIsGroup(true);
		accInfoForm.setWidth("100%");
		accInfoForm.setGroupTitle(vendorConstants.accountinformation());

		statusCheck = new CheckboxItem(vendorConstants.active());
		statusCheck.setValue(true);

		vendorSinceDate = new DateField(UIUtils.getVendorString(
				FinanceApplication.getVendorsMessages().supplierSince(),
				FinanceApplication.getVendorsMessages().vendorSince()));
		vendorSinceDate.setHelpInformation(true);
		vendorSinceDate.setEnteredDate(new ClientFinanceDate());

		accountText = new TextItem(vendorConstants.accountno());
		accountText.setHelpInformation(true);
		balanceText = new AmountField(vendorConstants.balance());
		balanceText.setHelpInformation(true);
		balanceDate = new DateField(vendorConstants.balanceasof());
		balanceDate.setHelpInformation(true);
		ClientFinanceDate todaydate = new ClientFinanceDate();
		todaydate.setDate(todaydate.getDate() + 1);
		balanceDate.setDatethanFireEvent(todaydate);
		balanceDate.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate date) {
				if (takenVendor == null) {
					ClientFinanceDate vendSinceDate = vendorSinceDate.getDate();
					if (date.before(vendSinceDate)) {
						String msg = vendorConstants.msg();
						// Accounter.showError(msg);
					}
				}

			}

		});

		accInfoForm.setStyleName("vender-form");
		accInfoForm.setFields(statusCheck, vendorSinceDate, accountText,
				balanceText, balanceDate);

		Label l1 = new Label(FinanceApplication.getVendorsMessages().contacts());

		Button addButton = new Button(FinanceApplication.getVendorsMessages()
				.add());
		addButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ClientContact clientContact = new ClientContact();
				gridView.addData(clientContact);
			}
		});

		gridView = new ContactGrid();
		gridView.setCanEdit(true);
		gridView.setEditEventType(ListGrid.EDIT_EVENT_DBCLICK);
		gridView.init();

		VerticalPanel panel = new VerticalPanel() {
			@Override
			protected void onAttach() {

				gridView.setHeight("88px");

				super.onAttach();
			}
		};
		panel.add(l1);

		panel.add(gridView);
		panel.add(addButton);

		memoArea = new TextAreaItem();
		memoArea.setHelpInformation(true);
		memoArea.setWidth(100);
		memoArea.setTitle(vendorConstants.memo());

		// For Editing Vendor
		if (takenVendor != null) {
			// Setting Vendor Name
			vendorNameText.setValue(takenVendor.getName());
			// Setting File as
			fileAsText.setValue(takenVendor.getFileAs());
			takenVendor.getPrimaryContact();
			// Setting AddressForm
			addrsForm = new AddressForm(takenVendor.getAddress());
			addrsForm.setWidth("100%");
			addrsForm.setStyleName(FinanceApplication.getVendorsMessages()
					.venderForm());
			// Setting Phone Fax Form
			fonFaxForm = new PhoneFaxForm(takenVendor.getPhoneNumbers(),
					takenVendor.getFaxNumbers());
			fonFaxForm.setWidth("100%");
			// Setting Email Form
			emailForm = new EmailForm(takenVendor.getEmails(), takenVendor
					.getWebPageAddress());
			emailForm.setWidth("100%");
			// Setting Status Check
			statusCheck.setValue(takenVendor.isActive());

			vendorSinceDate.setEnteredDate(new ClientFinanceDate(takenVendor
					.getPayeeSince()));

			// Setting Account No
			accountText.setValue(takenVendor.getAccountNumber());
			// Setting Balance
			if (!DecimalUtil.isEquals(takenVendor.getBalance(), 0)) {
				balanceText.setAmount(takenVendor.getBalance());

			} else {
				balanceText.setAmount(0.0);
			}
			balanceText.setDisabled(!takenVendor.isOpeningBalanceEditable());
			// Setting Balance as of
			balanceDate.setEnteredDate(new ClientFinanceDate(takenVendor
					.getBalanceAsOf()));
			balanceDate.setDisabled(true);

			// Setting Contacts
			gridView.initContacts(takenVendor.getContacts());

			// Setting Memo
			memoArea.setValue(takenVendor.getMemo());

		} else { // For Creating Vendor
			addrsForm = new AddressForm(null);
			addrsForm.setWidth("100%");
			addrsForm.setStyleName(FinanceApplication.getVendorsMessages()
					.venderForm());
			fonFaxForm = new PhoneFaxForm(null, null);
			fonFaxForm.setWidth("100%");
			emailForm = new EmailForm(null, null);
			emailForm.setWidth("100%");
		}
		DynamicForm memoForm = new DynamicForm();
		memoForm.setStyleName("align-form");
		memoForm.setWidth("100%");
		memoForm.setItems(memoArea);

		VerticalPanel bottomPanel = new VerticalPanel();
		bottomPanel.setWidth("100%");
		bottomPanel.add(memoForm);

		HorizontalPanel vendorHPanel = new HorizontalPanel();
		vendorHPanel.setWidth("100%");
		vendorHPanel.setCellHorizontalAlignment(vendorForm, ALIGN_RIGHT);
		vendorHPanel.add(vendorForm);

		HorizontalPanel addressHPanel = new HorizontalPanel();
		addressHPanel.setWidth("100%");
		addressHPanel.add(addrsForm);

		VerticalPanel leftVLay = new VerticalPanel();
		// leftVLay.setHorizontalAlignment(ALIGN_RIGHT);
		leftVLay.setWidth("100%");
		leftVLay.setCellHorizontalAlignment(vendorHPanel, ALIGN_RIGHT);
		leftVLay.add(vendorHPanel);
		leftVLay.add(addressHPanel);
		leftVLay.add(fonFaxForm);

		VerticalPanel rightVLay = new VerticalPanel();
		rightVLay.setWidth("100%");

		HorizontalPanel emailHPanel = new HorizontalPanel();
		emailHPanel.setCellHorizontalAlignment(emailForm, ALIGN_LEFT);
		emailHPanel.setWidth("90%");
		emailHPanel.add(emailForm);

		VerticalPanel accInfoHPanel = new VerticalPanel();
		accInfoHPanel.setWidth("90%");
		accInfoHPanel.add(accInfoForm);
		rightVLay.add(emailHPanel);
		rightVLay.add(accInfoHPanel);

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.setWidth("100%");
		topHLay.add(leftVLay);
		topHLay.add(rightVLay);

		HorizontalPanel contHLay = new HorizontalPanel();

		VerticalPanel mainVlay = new VerticalPanel();
		mainVlay.add(topHLay);
		mainVlay.add(contHLay);
		mainVlay.add(panel);
		mainVlay.add(bottomPanel);

		/* Adding dynamic forms in list */
		listforms.add(vendorForm);
		listforms.add(accInfoForm);
		listforms.add(memoForm);

		if (UIUtils.isMSIEBrowser()) {
			resetFromView();
			accInfoHPanel.setWidth("100%");
		}

		return mainVlay;
	}

	private void resetFromView() {
		addrsForm.getCellFormatter().setWidth(0, 0, "75");
		addrsForm.getCellFormatter().setWidth(0, 1, "125");

		fonFaxForm.getCellFormatter().setWidth(0, 0, "75");
		fonFaxForm.getCellFormatter().setWidth(0, 1, "125");

		emailForm.getCellFormatter().setWidth(0, 0, "150");
		emailForm.getCellFormatter().setWidth(0, 1, "150");

		memoArea.getMainWidget().setWidth("250px");

	}

	private VerticalPanel getDetailsTab() {

		Label lab = new Label(UIUtils.getVendorString(FinanceApplication
				.getVendorsMessages().supplier(), FinanceApplication
				.getVendorsMessages().vendor()));

		expenseAccountsSelect = new OtherAccountsCombo(vendorConstants
				.account());
		expenseAccountsSelect.setHelpInformation(true);
		expenseAccountsSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {
					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectAccountFromDetailsTab = selectItem;
					}
				});

		creditLimitText = new AmountField(vendorConstants.creditlimit());
		creditLimitText.setHelpInformation(true);
		creditLimitText.setWidth(100);

		preferredShippingSelect = new ShippingMethodsCombo(vendorConstants
				.preferredShippingMethod());
		preferredShippingSelect.setHelpInformation(true);
		preferredShippingSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientShippingMethod>() {
					public void selectedComboBoxItem(
							ClientShippingMethod selectItem) {
						selectShippingMethodFromDetailsTab = selectItem;
					}

				});

		preferredPaymentSelect = UIUtils.getPaymentMethodCombo();
		preferredPaymentSelect.setWidth(100);
		preferredPaymentSelect.addChangedHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				selectPaymentMethodFromDetialsTab = preferredPaymentSelect
						.getValue().toString();
			}
		});

		payTermsSelect = new PaymentTermsCombo(vendorConstants.paymentTerms());
		payTermsSelect.setHelpInformation(true);
		payTermsSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientPaymentTerms>() {
					public void selectedComboBoxItem(
							ClientPaymentTerms selectItem) {
						selectPaymentTermFromDetailsTab = selectItem;
					}
				});

		DynamicForm financeDetailsForm = new DynamicForm();
		financeDetailsForm.setIsGroup(true);
		financeDetailsForm.setWidth("100%");
		financeDetailsForm.setGroupTitle(vendorConstants.financialDetails());
		financeDetailsForm
				.setFields(expenseAccountsSelect, creditLimitText,
						preferredShippingSelect, preferredPaymentSelect,
						payTermsSelect);

		vendorGroupSelect = new VendorGroupCombo(UIUtils.getVendorString(
				FinanceApplication.getVendorsMessages().supplierGroup(),
				FinanceApplication.getVendorsMessages().vendorGroup()));
		vendorGroupSelect.setHelpInformation(true);
		vendorGroupSelect.setWidth(100);
		vendorGroupSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendorGroup>() {
					public void selectedComboBoxItem(
							ClientVendorGroup selectItem) {
						selectVendorGroupFromDetailsTab = selectItem;
					}

				});
		NewVendorAction newVendorAction = (NewVendorAction) this.getAction();
		if (newVendorAction.getOpenedFrom() == NewVendorAction.FROM_CREDIT_CARD_EXPENSE) {
			vendorGroupList = FinanceApplication.getCompany().getVendorGroups();

			for (ClientVendorGroup vendorGroup : vendorGroupList) {
				if (vendorGroup.getName().equals(
						FinanceApplication.getVendorsMessages()
								.creditCardCompanies())) {
					// vendorGroupSelect.setComboItem(vendorGroup);
					vendorGroupSelect.addItemThenfireEvent(vendorGroup);
					break;
				}
			}
		}

		federalText = new TextItem(vendorConstants.federalTaxId());
		federalText.setHelpInformation(true);
		federalText.setWidth(100);

		DynamicForm vendorGrpForm = new DynamicForm();
		vendorGrpForm.setIsGroup(false);

		/*
		 * In UK n US versions we need different widths as for the view
		 * requirement
		 */
		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			vendorGrpForm.setWidth("88%");
		else
			vendorGrpForm.setWidth("490");

		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			vendorGrpForm.setFields(vendorGroupSelect, federalText);
		else
			vendorGrpForm.setFields(vendorGroupSelect);

		vatRegistrationNumber = new TextItem(vendorConstants
				.vatRegistrationNumber());
		vatRegistrationNumber.setHelpInformation(true);
		vatRegistrationNumber.setWidth(100);
		vendorTaxCode = new TAXCodeCombo(UIUtils.getVendorString(
				FinanceApplication.getVendorsMessages().supplierVatCode(),
				FinanceApplication.getVendorsMessages().vendorVatCode()), false);
		vendorTaxCode.setHelpInformation(true);
		vendorTaxCode.setWidth(100);
		vendorTaxCode
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXCode>() {

					public void selectedComboBoxItem(ClientTAXCode selectItem) {
						selectTaxCodeFromDetailsTab = selectItem;
					}

				});

		DynamicForm vatform = new DynamicForm();
		vatform.setIsGroup(true);
		vatform.setWidth("50%");
		vatform.setGroupTitle(vendorConstants.vatDetails());
		vatform.setFields(vatRegistrationNumber, vendorTaxCode);
		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setSize("100%", "100%");
		leftVLay.setSpacing(10);
		leftVLay.add(financeDetailsForm);

		VerticalPanel rVLayout = new VerticalPanel();
		rVLayout.setSize("100%", "100%");
		rVLayout.setSpacing(10);
		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
			rVLayout.add(vendorGrpForm);
		} else {
			rVLayout.add(vendorGrpForm);
			rVLayout.add(vatform);

		}

		HorizontalPanel mainHLay = new HorizontalPanel();
		mainHLay.setSize("100%", "100%");
		mainHLay.add(leftVLay);
		mainHLay.add(rVLayout);

		VerticalPanel mainVLayout = new VerticalPanel();
		mainVLayout.setSize("100%", "100%");
		mainVLayout.add(lab);
		mainVLayout.add(mainHLay);

		if (takenVendor != null) {
			// Setting Account
			selectAccountFromDetailsTab = FinanceApplication.getCompany()
					.getAccount(takenVendor.getExpenseAccount());
			// Setting Credit Limit Text
			if (!DecimalUtil.isEquals(takenVendor.getCreditLimit(), 0))
				creditLimitText.setAmount(takenVendor.getCreditLimit());

			// Setting Preferred shipping method
			selectShippingMethodFromDetailsTab = FinanceApplication
					.getCompany().getShippingMethod(
							takenVendor.getShippingMethod());

			// Setting Payment Method
			selectPaymentMethodFromDetialsTab = takenVendor.getPaymentMethod();

			// Setting payment Terms
			selectPaymentTermFromDetailsTab = FinanceApplication.getCompany()
					.getPaymentTerms((takenVendor.getPaymentTermsId()));

			// Setting Vendor Group
			selectVendorGroupFromDetailsTab = FinanceApplication.getCompany()
					.getVendorGroup(takenVendor.getVendorGroup());

			if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
				// if (vatRegistrationNumber.getValue() != null)
				// vendor.setVATRegistrationNumber(vatRegistrationNumber.getValue().toString());
				vatRegistrationNumber.setValue(takenVendor
						.getVATRegistrationNumber());
				vendorTaxCode
						.setSelected(vendorTaxCode.getDisplayName(takenVendor
								.getTAXCode() != null ? FinanceApplication
								.getCompany().getTAXCode(
										takenVendor.getTAXCode()) : null));
			} else {
				// Setting Federal Id
				federalText.setValue(takenVendor.getFederalTaxId());
			}

		}

		/* Adding dynamic forms in list */
		listforms.add(financeDetailsForm);
		listforms.add(vendorGrpForm);
		listforms.add(vatform);

		if (UIUtils.isMSIEBrowser()) {
			financeDetailsForm.getCellFormatter().setWidth(0, 1, "200px");
			vendorGrpForm.getCellFormatter().setWidth(0, 1, "200px");
			vatform.getCellFormatter().setWidth(0, 1, "200px");
			financeDetailsForm.setWidth("75%");
			vendorGrpForm.setWidth("75%");
			vatform.setWidth("75%");
		}

		return mainVLayout;
	}

	protected void adjustFormWidths(int titlewidth, int listBoxWidth) {

		addrsForm.getCellFormatter().getElement(0, 0).setAttribute(
				FinanceApplication.getVendorsMessages().width(),
				titlewidth + "");
		addrsForm.getCellFormatter().getElement(0, 1).setAttribute(
				FinanceApplication.getVendorsMessages().width(),
				listBoxWidth + "");

		fonFaxForm.getCellFormatter().getElement(0, 0).setAttribute(
				FinanceApplication.getVendorsMessages().width(),
				titlewidth + "");
		fonFaxForm.getCellFormatter().getElement(0, 1).setAttribute(
				FinanceApplication.getVendorsMessages().width(),
				listBoxWidth + "");

		vendorForm.getCellFormatter().getElement(0, 0).getStyle().setWidth(
				titlewidth + listBoxWidth, Unit.PX);
		emailForm.getCellFormatter().getElement(0, 0).setAttribute(
				FinanceApplication.getVendorsMessages().width(),
				titlewidth + titlewidth + "");
		emailForm.getCellFormatter().getElement(0, 1).setAttribute(
				FinanceApplication.getVendorsMessages().width(),
				listBoxWidth + "");
		accInfoForm.getCellFormatter().getElement(0, 0).setAttribute(
				FinanceApplication.getVendorsMessages().width(),
				listBoxWidth + "");

	}

	@Override
	protected void onLoad() {
		int titlewidth = fonFaxForm.getCellFormatter().getElement(0, 0)
				.getOffsetWidth();
		int listBoxWidth = fonFaxForm.getCellFormatter().getElement(0, 1)
				.getOffsetWidth();

		adjustFormWidths(titlewidth, listBoxWidth);
		super.onLoad();
	}

	@Override
	protected void onAttach() {

		int titlewidth = fonFaxForm.getCellFormatter().getElement(0, 0)
				.getOffsetWidth();
		int listBoxWidth = fonFaxForm.getCellFormatter().getElement(0, 1)
				.getOffsetWidth();

		adjustFormWidths(titlewidth, listBoxWidth);

		super.onAttach();
	}

	@Override
	public void saveFailed(Throwable exception) {
		super.saveFailed(exception);
		String msg;
		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			msg = FinanceApplication.getVendorsMessages()
					.duplicationOfSupplierNameAreNotAllowed();
		} else
			msg = FinanceApplication.getVendorsMessages()
					.duplicationOfVendorNameAreNotAllowed();
		Accounter.showError(msg);
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		if (result != null) {
			// if (takenVendor == null) {
			// Accounter.showInformation(FinanceApplication
			// .getVendorsMessages().newVendorCreated());
			//
			// } else {
			// Accounter.showInformation(FinanceApplication
			// .getVendorsMessages().vendorUpdatedSuccessfully());
			//
			// }
			super.saveSuccess(result);

		} else {
			saveFailed(new Exception());
		}

	}

	protected void clearFields() {
		VendorsActionFactory.getNewVendorAction().run(null, true);
	}

	private ClientVendor getVendorObject() {

		ClientVendor vendor;
		if (takenVendor != null)
			vendor = takenVendor;
		else
			vendor = new ClientVendor();

		// Setting data from General Tab

		// Setting Vendor Name
		vendor
				.setName(vendorNameText.getValue().toString() != null ? vendorNameText
						.getValue().toString()
						: "");

		// Setting File As
		vendor.setFileAs(fileAsText.getValue().toString());

		// Setting Addresses
		vendor.setAddress(addrsForm.getAddresss());

		// Setting Phone
		vendor.setPhoneNumbers(fonFaxForm.getAllPhones());

		// Setting Fax
		vendor.setFaxNumbers(fonFaxForm.getAllFaxes());

		// Setting Email and Internet
		vendor.setEmails(emailForm.getAllEmails());

		// Setting web page Address
		vendor.setWebPageAddress(emailForm.getWebTextValue());

		// Setting Active
		vendor.setActive((Boolean) statusCheck.getValue());

		// Setting Vendor Since
		vendor.setPayeeSince(vendorSinceDate.getEnteredDate().getTime());

		// Setting Account Number
		vendor.setAccountNumber(accountText.getValue().toString());

		// Setting Balance
		if (takenVendor == null) {
			double bal = balanceText.getAmount() != null ? balanceText
					.getAmount().doubleValue() : 0.0;
			vendor.setOpeningBalance(bal);
		} else {
			if (DecimalUtil.isEquals(takenVendor.getOpeningBalance(), 0)) {
				vendor.setOpeningBalance(balanceText.getAmount());
			} else
				vendor.setBalance(balanceText.getAmount());
		}

		// Setting Balance As of
		if (balanceDate.getEnteredDate() != null)
			vendor.setBalanceAsOf(balanceDate.getEnteredDate().getTime());
		// Setting Contacts
		List<ClientContact> allGivenRecords = gridView.getRecords();

		Set<ClientContact> allContacts = new HashSet<ClientContact>();

		for (Object tmpRec : allGivenRecords) {
			ClientContact tempRecord = (ClientContact) tmpRec;
			ClientContact contact = new ClientContact();

			if (tempRecord == null) {
				contact.setPrimary(false);
				continue;
			}
			contact.setName(tempRecord.getName());
			contact.setTitle(tempRecord.getTitle());
			contact.setBusinessPhone(tempRecord.getBusinessPhone());
			contact.setEmail(tempRecord.getEmail());
			if (tempRecord.isPrimary() == Boolean.TRUE)
				contact.setPrimary(true);
			else
				contact.setPrimary(false);
			if (!contact.getName().equals("") || !contact.getTitle().equals("")
					|| !contact.getBusinessPhone().equals("")
					|| !contact.getEmail().equals("")) {
				allContacts.add(contact);

			}
		}
		vendor.setContacts(allContacts);

		// Setting Memo
		if (memoArea.getValue() != null)
			vendor.setMemo((String) memoArea.getValue());

		// Setting Data from Details Tab

		// Setting Expense Account
		vendor.setExpenseAccount(Utility.getId(selectAccountFromDetailsTab));

		// Setting Credit Limit
		vendor.setCreditLimit(creditLimitText.getAmount());

		// Setting Preferred Shipping Method
		vendor.setShippingMethod(Utility
				.getId(selectShippingMethodFromDetailsTab));

		// Setting Preferred Payment Method
		vendor
				.setPaymentMethod(selectPaymentMethodFromDetialsTab != null ? selectPaymentMethodFromDetialsTab
						: preferredPaymentSelect.getValue().toString());
		// Setting Preferred Payment Terms
		vendor.setPaymentTerms(Utility.getId(selectPaymentTermFromDetailsTab));

		// Setting Vendor Group
		vendor.setVendorGroup(Utility.getId(selectVendorGroupFromDetailsTab));
		if (FinanceApplication.getCompany().getAccountingType() == 0)
			if (federalText.getValue() != null) {
				vendor.setFederalTaxId(federalText.getValue().toString());

				// Setting Account Payable
				vendor.setAccountsPayable(FinanceApplication.getCompany()
						.getAccountsPayableAccount());
				// Seting opening balance accounts
				vendor.setOpeningBalanceAccount(FinanceApplication.getCompany()
						.getOpeningBalancesAccount());
				vendor.setAccountsPayable(FinanceApplication.getCompany()
						.getAccountsPayableAccount());

				// Setting opening balance accounts

				if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
					if (vatRegistrationNumber != null) {

						String vatReg = vatRegistrationNumber.getValue() != null ? vatRegistrationNumber
								.getValue().toString()
								: "";
						vendor
								.setVATRegistrationNumber(vatReg.length() != 0 ? vatReg
										: null);

					}
					if (vendorTaxCode != null)
						vendor.setTAXCode(Utility
								.getId(selectTaxCodeFromDetailsTab));
				}
			}
		return vendor;

	}

	public void addAccountsToList() {
		List<ClientAccount> allAccounts = expenseAccountsSelect.getAccounts();
		expenseAccountsSelect.initCombo(allAccounts);
		if (takenVendor != null) {
			ClientAccount temp = FinanceApplication.getCompany().getAccount(
					takenVendor.getExpenseAccount());
			// Setting Expense Account
			if (temp != null)
				expenseAccountsSelect.setComboItem(temp);
			for (@SuppressWarnings("unused")
			ClientAccount tempAccount : allAccounts) {
				// FIXME
				// if (tempAccount.getStringID() == (3))
				// accountPayableAccount = tempAccount;
			}
		}

	}

	public void addVendorGroupList() {

		vendorGroupSelect.initCombo(FinanceApplication.getCompany()
				.getVendorGroups());
		// Setting Vendor Group
		if (takenVendor != null) {
			if (takenVendor.getVendorGroup() != null
					&& takenVendor.getVendorGroup().length() != 0)
				vendorGroupSelect.setComboItem(company
						.getVendorGroup(takenVendor.getVendorGroup()));
		}

	}

	public void addShippingMethodList() {
		preferredShippingSelect.initCombo(FinanceApplication.getCompany()
				.getShippingMethods());
		// Setting Preferred Shipping Method
		if (takenVendor != null) {
			if (takenVendor.getShippingMethod().length() != 0
					&& takenVendor.getShippingMethod() != null)
				preferredShippingSelect.setComboItem(company
						.getShippingMethod(takenVendor.getShippingMethod()));

		}
	}

	public void addPaymentMethodList() {
		// Setting Preferred Payment Method
		if (takenVendor != null) {
			if (takenVendor.getPaymentMethod() != null)
				preferredPaymentSelect
						.setDefaultValue(selectPaymentMethodFromDetialsTab);
		}

	}

	public void addPaymentTermsList() {

		payTermsSelect.initCombo(FinanceApplication.getCompany()
				.getPaymentsTerms());
		// Setting Payment Term
		if (takenVendor != null) {
			if (takenVendor.getPaymentTermsId().length() != 0
					&& takenVendor.getPaymentTermsId() != null)
				payTermsSelect.setComboItem(company.getPaymentTerms(takenVendor
						.getPaymentTermsId()));
		}

	}

	@Override
	protected void initConstants() {

		super.initConstants();
		vendorConstants = (VendorsMessages) GWT.create(VendorsMessages.class);
	}

	@Override
	public void init() {
		super.init();
		createControls();
		setSize("100%", "100%");
	}

	@Override
	public void initData() {
		super.initData();
		company = FinanceApplication.getCompany();
		getFiscalYear();
		addAccountsToList();
		addVendorGroupList();
		if (company.getAccountingType() == 1)
			addSuplierTaxCode();
		addShippingMethodList();
		addPaymentMethodList();
		addPaymentTermsList();

	}

	private void addSuplierTaxCode() {
		vendorTaxCode.initCombo(FinanceApplication.getCompany()
				.getActiveTaxCodes());
		if (takenVendor != null) {
			vendorTaxCode.setSelected(vendorTaxCode.getDisplayName(takenVendor
					.getTAXCode() != null ? FinanceApplication.getCompany()
					.getTAXCode(takenVendor.getTAXCode()) : null));
			vendorTaxCode.setComboItem(FinanceApplication.getCompany()
					.getTAXCode(takenVendor.getTAXCode()));
		}
	}

	@Override
	public void setData(ClientVendor data) {
		super.setData(data);
		if (data != null)
			takenVendor = (ClientVendor) data;
	}

	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.vendorNameText.setFocus();
	}

	@Override
	public void fitToSize(int height, int width) {
		// super.fitToSize(height, width);

	}

	@Override
	public void deleteFailed(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(Boolean result) {
		// TODO Auto-generated method stub

	}

	// CustomCombo<?> getComboType(AccounterCoreType coreType) {
	// if (coreType == AccounterCoreType.PAYMENT_TERM)
	// return payTermsSelect;
	// if (coreType == AccounterCoreType.SHIPPING_METHOD)
	// return preferredShippingSelect;
	// if (coreType == AccounterCoreType.ACCOUNT)
	// return expenseAccountsSelect;
	// if (coreType == AccounterCoreType.VATCODE
	// && FinanceApplication.getCompany().getAccountingType() ==
	// ClientCompany.ACCOUNTING_TYPE_UK)
	// return supplierVatCode;
	// if (coreType == AccounterCoreType.VENDOR_GROUP)
	// return vendorGroupSelect;
	// return null;
	// }

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		switch (command) {
		case AccounterCommand.CREATION_SUCCESS:
			if (core.getObjectType() == AccounterCoreType.PAYMENT_TERM)
				payTermsSelect.addComboItem((ClientPaymentTerms) core);
			if (core.getObjectType() == AccounterCoreType.SHIPPING_METHOD)
				preferredShippingSelect
						.addComboItem((ClientShippingMethod) core);
			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.expenseAccountsSelect.addComboItem((ClientAccount) core);
			if (core.getObjectType() == AccounterCoreType.TAX_CODE)
				this.vendorTaxCode.addComboItem((ClientTAXCode) core);
			if (core.getObjectType() == AccounterCoreType.VENDOR_GROUP)
				this.vendorGroupSelect.addComboItem((ClientVendorGroup) core);
			break;
		case AccounterCommand.UPDATION_SUCCESS:
			if (core.getObjectType() == AccounterCoreType.PAYMENT_TERM)
				payTermsSelect.updateComboItem((ClientPaymentTerms) core);
			if (core.getObjectType() == AccounterCoreType.SHIPPING_METHOD)
				preferredShippingSelect
						.updateComboItem((ClientShippingMethod) core);
			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.expenseAccountsSelect
						.updateComboItem((ClientAccount) core);
			if (core.getObjectType() == AccounterCoreType.TAX_CODE
					&& FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK)
				this.vendorTaxCode.updateComboItem((ClientTAXCode) core);
			if (core.getObjectType() == AccounterCoreType.VENDOR_GROUP)
				this.vendorGroupSelect
						.updateComboItem((ClientVendorGroup) core);
			break;

		case AccounterCommand.DELETION_SUCCESS:
			if (core.getObjectType() == AccounterCoreType.PAYMENT_TERM)
				payTermsSelect.removeComboItem((ClientPaymentTerms) core);
			if (core.getObjectType() == AccounterCoreType.SHIPPING_METHOD)
				preferredShippingSelect
						.removeComboItem((ClientShippingMethod) core);
			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.expenseAccountsSelect
						.removeComboItem((ClientAccount) core);
			if (core.getObjectType() == AccounterCoreType.TAX_CODE
					&& FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK)
				this.vendorTaxCode.removeComboItem((ClientTAXCode) core);
			if (core.getObjectType() == AccounterCoreType.VENDOR_GROUP)
				this.vendorGroupSelect
						.removeComboItem((ClientVendorGroup) core);
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