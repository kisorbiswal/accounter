package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AddButton;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientEmail;
import com.vimukti.accounter.web.client.core.ClientFax;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientPhone;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientVendorGroup;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.AddressForm;
import com.vimukti.accounter.web.client.ui.EmailForm;
import com.vimukti.accounter.web.client.ui.PhoneFaxForm;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.CurrencyCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.OtherAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.PaymentTermsCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.combo.ShippingMethodsCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.combo.TaxItemCombo;
import com.vimukti.accounter.web.client.ui.combo.VendorGroupCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.EmailField;
import com.vimukti.accounter.web.client.ui.edittable.tables.ContactsTable;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

/**
 * 
 * @author venki.p modified by Rajesh.A and Ravi kiran.G, Murali
 * 
 */

public class VendorView extends BaseView<ClientVendor> {

	DynamicForm vendorForm, accInfoForm;

	TextItem vendorNoText;
	TextItem vendorNameText;
	TextItem fileAsText, accountText, bankNameText, bankBranchText, webText,
			linksText, expenseAccountsText, taxIDText, panNumberText,
			serviceTaxRegisterationNumber, taxID;
	TextAreaItem memoArea;
	DateField balanceDate, vendorSinceDate;
	EmailField emailText;
	AmountField creditLimitText, balanceText;
	TextItem vatRegistrationNumber;

	PaymentTermsCombo payTermsSelect;
	ShippingMethodsCombo preferredShippingSelect;
	OtherAccountsCombo expenseAccountsSelect;
	TAXCodeCombo vendorTaxCode;
	TaxItemCombo vendorTDSTaxCode;
	VendorGroupCombo vendorGroupSelect;
	SelectCombo preferredPaymentSelect;
	CurrencyCombo currencyCombo;
	CheckboxItem euVATexempVendor;
	CheckboxItem track1099MISC;
	CheckboxItem isTDS;
	TabPanel tabSet;
	AddButton addButton;

	LinkedHashMap<String, ClientAddress> allAddresses;
	LinkedHashMap<String, ClientPhone> allPhones;
	LinkedHashMap<String, ClientFax> allFaxes;
	LinkedHashMap<String, ClientEmail> allEmails;

	CheckboxItem statusCheck;

	ContactsTable gridView;

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

	// private ClientFiscalYear fiscalYear;

	protected ClientAccount selectAccountFromDetailsTab;
	protected ClientShippingMethod selectShippingMethodFromDetailsTab;
	protected String selectPaymentMethodFromDetialsTab;
	protected ClientPaymentTerms selectPaymentTermFromDetailsTab;
	protected ClientVendorGroup selectVendorGroupFromDetailsTab;
	// protected ClientCurrency clientCurrency;

	private AddressForm addrsForm;
	private PhoneFaxForm fonFaxForm;
	private EmailForm emailForm;

	// private ClientVendor takenVendor;
	protected ClientAccount accountPayableAccount;
	private ClientCompany company;
	private boolean wait;

	private ArrayList<DynamicForm> listforms;

	protected ClientTAXCode selectTaxCodeFromDetailsTab;
	protected ClientTAXItem selectTaxItemFromDetailsTab;

	public VendorView() {
		super();
	}

	// private void getFiscalYear() {
	// List<ClientFiscalYear> result = getCompany().getFiscalYears();
	// if (result != null && !isInViewMode()) {
	// for (ClientFiscalYear fiscalYear : result) {
	// if (fiscalYear != null && fiscalYear.getIsCurrentFiscalYear()) {
	// if (fiscalYear != null
	// && fiscalYear.getIsCurrentFiscalYear()) {
	// VendorView.this.fiscalYear = fiscalYear;
	// // balanceDate.setEnteredDate(VendorView.this.fiscalYear
	// // .getStartDate());
	// break;
	// }
	// }
	// }
	// }
	// }

	private void createControls() {

		listforms = new ArrayList<DynamicForm>();

		// setTitle(UIUtils.title(Accounter.constants().newVendor()));
		tabSet = new DecoratedTabPanel();
		tabSet.setSize("100%", "100%");

		tabSet.add(getGeneralTab(), Accounter.constants().general());
		tabSet.add(getDetailsTab(), Accounter.constants().details());
		tabSet.selectTab(0);
		tabSet.setSize("100%", "100%");

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(tabSet);
		this.add(mainVLay);

	}

	@Override
	public void saveAndUpdateView() {
		if (!wait) {
			updateVendorObject();
			saveOrUpdate(getData());
		}
	}

	@Override
	public ValidationResult validate() {

		ValidationResult result = new ValidationResult();
		result.add(vendorForm.validate());

		String name = vendorNameText.getValue();

		String taxNumber = taxID.getValue();

		ClientVendor vendorByName = company.getVendorByName(name);

		ClientCustomer customerByName = company.getCustomerByName(name);

		if (customerByName != null) {
			result.addError(vendorNameText, Accounter.constants()
					.alreadyExist());
			return result;
		}
		if (vendorByName != null
				&& !(this.getData().getID() == vendorByName.getID())) {
			result.addError(vendorNameText, Accounter.constants()
					.alreadyExist());
			return result;
		}
		data.setName(name);
		data.setVendorNumber(vendorNoText.getValue().toString());
		String error = objectExist(data);
		if (error != null && !error.isEmpty()) {
			result.addError(vendorNoText, error);
			return result;
		}

		ClientFinanceDate asOfDate = balanceDate.getEnteredDate();
		if (AccounterValidator.isPriorToCompanyPreventPostingDate(asOfDate)) {
			result.addError(balanceDate, Accounter.constants().priorasOfDate());
		}

		return result;
	}

	public String objectExist(ClientVendor vendor) {

		String error = null;

		List<ClientVendor> list = Accounter.getCompany().getVendors();
		if (list == null || list.isEmpty())
			return "";
		for (ClientVendor old : list) {
			if (old.getID() == vendor.getID()) {
				continue;
			}
			if (vendor.getName().equalsIgnoreCase(old.getName())) {
				if (getCompany().getPreferences().getUseVendorId()) {
					for (ClientVendor client : list) {
						if (vendor.getVendorNumber().equals(
								client.getVendorNumber())) {
							error = Accounter.messages()
									.vendorAlreadyExistsWithTheNameAndNumber(
											Global.get().Vendor());
							break;
						}
					}
				}
				error = Accounter.messages().vendorAlreadyExistsWithTheName(
						Global.get().vendor());
				break;
			} else if (getCompany().getPreferences().getUseVendorId()) {
				if (vendor.getVendorNumber().isEmpty()) {
					error = Accounter.messages()
							.pleaseEnterVendorNumberItShouldNotBeEmpty(
									Global.get().vendor());
					break;
				} else if (vendor.getVendorNumber().equals(
						old.getVendorNumber())) {
					error = Accounter.messages()
							.vendorAlreadyExistsWithTheNumber(
									Global.get().Vendor());
					break;
				} else if (checkIfNotNumber(vendor.getVendorNumber())) {
					error = Accounter.messages().vendorNumberShouldBeNumber(
							Global.get().Vendor());
					break;
				} else if (Integer
						.parseInt(vendor.getVendorNumber().toString()) < 1) {
					error = Accounter.messages().vendorNumberShouldBePos(
							Global.get().Vendor());
					break;
				}
			}

		}
		return error;
	}

	private boolean isObjectExist(long id, String name) {
		List<ClientVendor> vendors = getCompany().getVendors();
		for (ClientVendor old : vendors) {
			if (old.getID() == id) {
				continue;
			}
			if (old.getName().toLowerCase().equals(name.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	private VerticalPanel getGeneralTab() {
		vendorNameText = new TextItem(
				messages.vendorName(Global.get().Vendor()));
		if (quickAddText != null) {
			vendorNameText.setValue(quickAddText);
		}
		vendorNameText.setHelpInformation(true);
		vendorNameText.setRequired(true);
		vendorNameText.setWidth(100);
		vendorNameText.setDisabled(isInViewMode());

		vendorNoText = new TextItem(
				messages.vendorNumber(Global.get().Vendor()));
		vendorNoText.setHelpInformation(true);
		vendorNoText.setRequired(true);
		vendorNoText.setWidth(100);

		fileAsText = new TextItem(Accounter.constants().fileAs());
		fileAsText.setHelpInformation(true);
		fileAsText.setWidth(100);

		taxID = new TextItem("Tax ID");
		taxID.setHelpInformation(true);
		taxID.setWidth(100);

		vendorNameText.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (event.getSource() != null)
					fileAsText.setValue(vendorNameText.getValue());
			}

		});

		vendorForm = UIUtils.form(Global.get().Vendor());
		if (getCompany().getPreferences().getUseVendorId()) {
			vendorForm.setFields(vendorNameText, vendorNoText);
		} else {
			vendorForm.setFields(vendorNameText);

		}
		vendorForm.setWidth("100%");
		vendorForm.setStyleName(Accounter.constants().venderForm());
		vendorForm.getCellFormatter().setWidth(0, 0, "245px");

		accInfoForm = new DynamicForm();
		accInfoForm.setIsGroup(true);
		accInfoForm.setWidth("100%");
		accInfoForm.setGroupTitle(messages.accountInformation(Global.get()
				.Account()));

		statusCheck = new CheckboxItem(constants.active());
		statusCheck.setValue(true);
		statusCheck.setDisabled(isInViewMode());

		vendorSinceDate = new DateField(messages.vendorSince(Global.get()
				.Vendor()));
		vendorSinceDate.setDisabled(isInViewMode());
		track1099MISC = new CheckboxItem(Accounter.constants().track1099Form());
		track1099MISC.setValue(false);

		vendorSinceDate.setHelpInformation(true);
		vendorSinceDate.setEnteredDate(new ClientFinanceDate());

		balanceText = new AmountField(Accounter.constants().balance(), this);
		balanceText.setHelpInformation(true);
		balanceDate = new DateField(Accounter.constants().balanceAsOf());
		balanceDate.setHelpInformation(true);
		ClientFinanceDate todaydate = new ClientFinanceDate();
		todaydate.setDay(todaydate.getDay());
		balanceDate.setDatethanFireEvent(todaydate);
		balanceDate.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate date) {
				if (!isInViewMode()) {
					ClientFinanceDate vendSinceDate = vendorSinceDate.getDate();
					if (date.before(vendSinceDate)) {
						String msg = Accounter.messages().msg(
								Global.get().Vendor());
						// Accounter.showError(msg);
					}
				}

			}

		});

		accInfoForm.setStyleName("vender-form");
		accInfoForm.setFields(statusCheck, vendorSinceDate, balanceText,
				balanceDate);
		if (getPreferences().isTrackTax()) {
			if (getCountryPreferences().isSalesTaxAvailable()) {
				accInfoForm.setFields(taxID);
			}
			if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
				accInfoForm.setFields(track1099MISC);
			}
		}

		Label l1 = new Label(Accounter.constants().contacts());

		addButton = new AddButton(this);

		addButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ClientContact clientContact = new ClientContact();
				gridView.setDisabled(false);
				if (gridView.getRecords().isEmpty()) {
					clientContact.setPrimary(true);
				}
				gridView.add(clientContact);
			}
		});

		gridView = new ContactsTable();
		gridView.setDisabled(isInViewMode());
		// gridView.setCanEdit(!isInViewMode());
		// gridView.setEditEventType(ListGrid.EDIT_EVENT_DBCLICK);
		// gridView.isEnable = false;
		// gridView.init();

		VerticalPanel panel = new VerticalPanel() {
			@Override
			protected void onAttach() {

				// gridView.setHeight("88px");

				super.onAttach();
			}
		};
		panel.add(l1);
		panel.setWidth("100%");
		panel.add(gridView);
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setHorizontalAlignment(ALIGN_RIGHT);
		hPanel.add(addButton);
		hPanel.getElement().getStyle().setMarginTop(8, Unit.PX);
		hPanel.getElement().getStyle().setFloat(Float.LEFT);
		panel.add(hPanel);
		addButton.setEnabled(!isInViewMode());

		memoArea = new TextAreaItem();
		memoArea.setToolTip(Accounter.messages().writeCommentsForThis(
				this.getAction().getViewName()));
		memoArea.setHelpInformation(true);
		memoArea.setWidth("400px");
		memoArea.setTitle(Accounter.constants().memo());

		addrsForm = new AddressForm(null);
		addrsForm.setWidth("100%");
		addrsForm.setDisabled(isInViewMode());
		// addrsForm.setStyleName(FinanceApplication.constants()
		// .venderForm());
		fonFaxForm = new PhoneFaxForm(null, null, this, this.getAction()
				.getViewName());
		fonFaxForm.setWidth("100%");
		fonFaxForm.setDisabled(isInViewMode());
		emailForm = new EmailForm(null, null, this, this.getAction()
				.getViewName());
		emailForm.setWidth("100%");
		emailForm.setDisabled(isInViewMode());
		DynamicForm memoForm = new DynamicForm();
		memoForm.setStyleName("align-form");
		memoForm.setWidth("100%");
		memoForm.setItems(memoArea);
		memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");

		VerticalPanel bottomPanel = new VerticalPanel();
		bottomPanel.setWidth("100%");
		bottomPanel.add(memoForm);

		// HorizontalPanel vendorHPanel = new HorizontalPanel();
		// vendorHPanel.setWidth("100%");
		// vendorHPanel.setCellHorizontalAlignment(vendorForm, ALIGN_RIGHT);
		// vendorHPanel.add(vendorForm);

		// HorizontalPanel addressHPanel = new HorizontalPanel();
		// addressHPanel.setWidth("100%");
		// addressHPanel.add(addrsForm);

		VerticalPanel leftVLay = new VerticalPanel();
		// leftVLay.setHorizontalAlignment(ALIGN_RIGHT);
		leftVLay.setWidth("100%");
		// leftVLay.setCellHorizontalAlignment(vendorHPanel, ALIGN_RIGHT);
		leftVLay.add(vendorForm);
		leftVLay.add(accInfoForm);
		// leftVLay.add(fonFaxForm);

		VerticalPanel rightVLay = new VerticalPanel();
		rightVLay.setWidth("100%");
		rightVLay.add(addrsForm);
		rightVLay.add(fonFaxForm);
		rightVLay.add(emailForm);
		addrsForm.getCellFormatter().addStyleName(0, 0, "addrsFormCellAlign");
		addrsForm.getCellFormatter().addStyleName(0, 1, "addrsFormCellAlign");

		// HorizontalPanel emailHPanel = new HorizontalPanel();
		// emailHPanel.setCellHorizontalAlignment(emailForm, ALIGN_LEFT);
		// emailHPanel.setWidth("90%");
		// emailHPanel.add(emailForm);

		VerticalPanel accInfoHPanel = new VerticalPanel();
		accInfoHPanel.setWidth("100%");

		// rightVLay.setCellHorizontalAlignment(accInfoForm, ALIGN_RIGHT);

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.setWidth("100%");
		topHLay.add(leftVLay);
		topHLay.add(rightVLay);
		topHLay.setCellWidth(rightVLay, "50Osave%");

		HorizontalPanel contHLay = new HorizontalPanel();

		VerticalPanel mainVlay = new VerticalPanel();
		mainVlay.add(topHLay);
		mainVlay.add(contHLay);
		mainVlay.add(panel);
		// mainVlay.add(bottomPanel);

		/* Adding dynamic forms in list */
		listforms.add(vendorForm);
		listforms.add(accInfoForm);
		listforms.add(memoForm);

		// if (UIUtils.isMSIEBrowser()) {
		// resetFromView();
		// accInfoHPanel.setWidth("100%");
		// }

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

		Label lab = new Label(Global.get().Vendor());

		expenseAccountsSelect = new OtherAccountsCombo(Accounter.messages()
				.account(Global.get().account()));
		expenseAccountsSelect.setHelpInformation(true);
		expenseAccountsSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {
					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectAccountFromDetailsTab = selectItem;
					}
				});
		expenseAccountsSelect.setDisabled(isInViewMode());

		creditLimitText = new AmountField(Accounter.constants().creditLimit(),
				this);
		creditLimitText.setHelpInformation(true);
		creditLimitText.setWidth(100);
		creditLimitText.setDisabled(isInViewMode());

		preferredShippingSelect = new ShippingMethodsCombo(Accounter
				.constants().preferredShippingMethod());
		preferredShippingSelect.setHelpInformation(true);
		preferredShippingSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientShippingMethod>() {
					public void selectedComboBoxItem(
							ClientShippingMethod selectItem) {
						selectShippingMethodFromDetailsTab = selectItem;
					}

				});
		preferredShippingSelect.setDisabled(isInViewMode());

		preferredPaymentSelect = UIUtils.getPaymentMethodCombo();
		// preferredPaymentSelect.setWidth(100);
		preferredPaymentSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						selectPaymentMethodFromDetialsTab = preferredPaymentSelect
								.getSelectedValue();
					}
				});
		preferredPaymentSelect.setDisabled(isInViewMode());

		payTermsSelect = new PaymentTermsCombo(Accounter.constants()
				.paymentTerms());
		payTermsSelect.setHelpInformation(true);
		payTermsSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientPaymentTerms>() {
					public void selectedComboBoxItem(
							ClientPaymentTerms selectItem) {
						selectPaymentTermFromDetailsTab = selectItem;
					}
				});
		payTermsSelect.setDisabled(isInViewMode());
		accountText = new TextItem(messages.accountNo(Global.get().Account()));
		accountText.setHelpInformation(true);
		accountText.setDisabled(isInViewMode());
		bankNameText = new TextItem(Accounter.constants().bankName());
		bankNameText.setHelpInformation(true);
		bankNameText.setDisabled(isInViewMode());
		bankBranchText = new TextItem(Accounter.constants().bankBranch());
		bankBranchText.setHelpInformation(true);
		bankBranchText.setDisabled(isInViewMode());
		currencyCombo = new CurrencyCombo(Accounter.constants().currency());
		currencyCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientCurrency>() {

					@Override
					public void selectedComboBoxItem(ClientCurrency selectItem) {
						// clientCurrency = selectItem;
					}
				});
		currencyCombo.setDisabled(isInViewMode());
		DynamicForm financeDetailsForm = new DynamicForm();
		financeDetailsForm.setIsGroup(true);
		financeDetailsForm.setWidth("100%");
		financeDetailsForm.setGroupTitle(Accounter.constants()
				.financialDetails());
		financeDetailsForm.setFields(expenseAccountsSelect, creditLimitText,
				preferredShippingSelect, preferredPaymentSelect,
				payTermsSelect, accountText, bankNameText, bankBranchText);

		vendorGroupSelect = new VendorGroupCombo(messages.vendorGroup(Global
				.get().Vendor()));
		vendorGroupSelect.setHelpInformation(true);
		// vendorGroupSelect.setWidth(100);
		vendorGroupSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendorGroup>() {
					public void selectedComboBoxItem(
							ClientVendorGroup selectItem) {
						selectVendorGroupFromDetailsTab = selectItem;
					}

				});
		vendorGroupSelect.setDisabled(isInViewMode());
		NewVendorAction newVendorAction = (NewVendorAction) this.getAction();
		if (newVendorAction.getOpenedFrom() == NewVendorAction.FROM_CREDIT_CARD_EXPENSE) {
			vendorGroupList = getCompany().getVendorGroups();

			for (ClientVendorGroup vendorGroup : vendorGroupList) {
				// id 1 for creditCardCompanies
				if (vendorGroup.getID() == 1) {
					// vendorGroupSelect.setComboItem(vendorGroup);
					vendorGroupSelect.addItemThenfireEvent(vendorGroup);
					selectVendorGroupFromDetailsTab = vendorGroup;
					break;
				}
			}
		}

		taxIDText = new TextItem(Accounter.constants().taxId());
		taxIDText.setHelpInformation(true);
		taxIDText.setWidth(100);
		taxIDText.setDisabled(isInViewMode());
		vendorTDSTaxCode = new TaxItemCombo(messages.vendorTDSCode(Global.get()
				.Vendor()), ClientTAXItem.TAX_TYPE_TDS);
		vendorTDSTaxCode.setHelpInformation(true);
		vendorTDSTaxCode.setWidth(100);
		vendorTDSTaxCode.setDisabled(isInViewMode());
		// panNumber=new TextItem(Accounter.constants().panNumber());
		// panNumber.setHelpInformation(true);
		// panNumber.setWidth("100%");

		vendorTDSTaxCode
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXItem>() {

					public void selectedComboBoxItem(ClientTAXItem selectItem) {
						selectTaxItemFromDetailsTab = selectItem;
					}

				});
		isTDS = new CheckboxItem(Accounter.constants().tdsApplicable());
		isTDS.setValue(true);
		isTDS.addChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {

				if (isTDS.isChecked()) {
					vendorTDSTaxCode.setDisabled(false);
				} else {
					vendorTDSTaxCode.setDisabled(true);
				}
			}
		});
		isTDS.setDisabled(isInViewMode());
		panNumberText = new TextItem(Accounter.messages().panNumber(
				Global.get().Account()));
		panNumberText.setHelpInformation(true);
		panNumberText.setWidth(100);
		panNumberText.setDisabled(isInViewMode());
		serviceTaxRegisterationNumber = new TextItem(Accounter.constants()
				.serviceTaxRegistrationNumber());
		serviceTaxRegisterationNumber.setHelpInformation(true);
		serviceTaxRegisterationNumber.setWidth(100);
		serviceTaxRegisterationNumber.setDisabled(isInViewMode());
		DynamicForm vendorGrpForm = new DynamicForm();
		vendorGrpForm.setIsGroup(false);

		/*
		 * In UK n US versions we need different widths as for the view
		 * requirement
		 */
		vendorGrpForm.setWidth("100%");
		vendorGrpForm.setFields(vendorGroupSelect);

		vendorGrpForm.getCellFormatter().getElement(0, 0)
				.setAttribute(Accounter.constants().width(), "44%");

		vatRegistrationNumber = new TextItem(Accounter.constants().taxRegNo());
		vatRegistrationNumber.setHelpInformation(true);
		vatRegistrationNumber.setWidth(100);
		vatRegistrationNumber.setDisabled(isInViewMode());
		vendorTaxCode = new TAXCodeCombo(constants.taxCode(), false);
		vendorTaxCode.setHelpInformation(true);
		vendorTaxCode.setWidth(100);
		vendorTaxCode
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXCode>() {

					public void selectedComboBoxItem(ClientTAXCode selectItem) {
						selectTaxCodeFromDetailsTab = selectItem;
					}

				});
		vendorTaxCode.setDisabled(isInViewMode());
		DynamicForm vatform = new DynamicForm();
		vatform.setIsGroup(true);
		vatform.setWidth("100%");
		vatform.setGroupTitle(Accounter.constants().vatDetails());
		if (getPreferences().isTrackTax()) {
			if (getCountryPreferences().isVatAvailable()) {
				vatform.setFields(vatRegistrationNumber);
			}
			vatform.setFields(vendorTaxCode);
			if (getCountryPreferences().isTDSAvailable()) {
				vatform.setFields(isTDS, vendorTDSTaxCode);
			}
			if (getCountryPreferences().isServiceTaxAvailable()) {
				vatform.setFields(serviceTaxRegisterationNumber);
			}
		}
		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setSize("100%", "100%");
		leftVLay.setHeight("350px");
		// leftVLay.getElement().getStyle()
		// .setBorderColor("none repeat scroll 0 0 #eee !important");
		leftVLay.setSpacing(10);
		leftVLay.add(financeDetailsForm);

		VerticalPanel rVLayout = new VerticalPanel();
		rVLayout.setWidth("100%");
		rVLayout.setSpacing(10);
		rVLayout.add(vendorGrpForm);
		if (getPreferences().isTrackTax()) {
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

		/* Adding dynamic forms in list */
		listforms.add(financeDetailsForm);
		listforms.add(vendorGrpForm);
		listforms.add(vatform);

		// if (UIUtils.isMSIEBrowser()) {
		// financeDetailsForm.getCellFormatter().setWidth(0, 1, "200px");
		// vendorGrpForm.getCellFormatter().setWidth(0, 1, "200px");
		// vatform.getCellFormatter().setWidth(0, 1, "200px");
		// financeDetailsForm.setWidth("75%");
		// vendorGrpForm.setWidth("75%");
		// vatform.setWidth("75%");
		// }

		return mainVLayout;
	}

	protected void adjustFormWidths(int titlewidth, int listBoxWidth) {

		addrsForm.getCellFormatter().getElement(0, 0)
				.setAttribute(Accounter.constants().width(), "25px");
		addrsForm.getCellFormatter().getElement(0, 1)
				.setAttribute(Accounter.constants().width(), "186px");

		fonFaxForm.getCellFormatter().getElement(0, 0)
				.setAttribute(Accounter.constants().width(), "240px");
		// fonFaxForm.getCellFormatter().getElement(0, 1).setAttribute(
		// FinanceApplication.constants().width(), "185px");

		vendorForm.getCellFormatter().getElement(0, 0).getStyle()
				.setWidth(150, Unit.PX);
		emailForm.getCellFormatter().getElement(0, 0)
				.setAttribute(Accounter.constants().width(), "240px");
		// emailForm.getCellFormatter().getElement(0, 1).setAttribute(
		// FinanceApplication.constants().width(), "");
		accInfoForm.getCellFormatter().getElement(0, 0)
				.setAttribute(Accounter.constants().width(), "150px");

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
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		AccounterException accounterException = (AccounterException) exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		if (result != null) {
			// if (takenVendor == null) {
			// Accounter.showInformation(FinanceApplication
			// .constants().newVendorCreated());
			//
			// } else {
			// Accounter.showInformation(FinanceApplication
			// .constants().vendorUpdatedSuccessfully());
			//
			// }
			super.saveSuccess(result);

		} else {
			saveFailed(new AccounterException());
		}

	}

	protected void clearFields() {
		ActionFactory.getNewVendorAction().run(null, true);
	}

	private void updateVendorObject() {

		// Setting data from General Tab

		// Setting Vendor Name
		data.setName(vendorNameText.getValue().toString() != null ? vendorNameText
				.getValue().toString() : "");

		data.setVendorNumber(vendorNoText.getValue().toString());

		// Setting File As
		data.setFileAs(fileAsText.getValue().toString());

		data.setType(ClientPayee.TYPE_VENDOR);

		// Setting Addresses
		data.setAddress(addrsForm.getAddresss());

		data.setTdsApplicable(isTDS.getValue());

		if (selectTaxItemFromDetailsTab != null) {
			data.setTaxItemCode(Utility.getID(selectTaxItemFromDetailsTab));
		}

		// Setting Phone
		// data.setPhoneNumbers(fonFaxForm.getAllPhones());
		data.setPhoneNo(fonFaxForm.businessPhoneText.getValue().toString());

		// Setting Fax
		// data.setFaxNumbers(fonFaxForm.getAllFaxes());
		data.setFaxNo(fonFaxForm.businessFaxText.getValue().toString());

		// Setting Email and Internet
		data.setEmail(emailForm.businesEmailText.getValue().toString());

		// Setting web page Address
		data.setWebPageAddress(emailForm.getWebTextValue());

		// Setting MISC 1099 form value
		// data.setActive((Boolean) track1099MISC.getValue());

		// Setting Active
		data.setActive((Boolean) statusCheck.getValue());

		// Setting Vendor Since
		data.setPayeeSince(vendorSinceDate.getEnteredDate().getDate());

		// Setting Currency
		if (currencyCombo.getSelectedValue() != null)
			data.setCurrency(currencyCombo.getSelectedValue().toString());
		// Setting Balance
		if (!isInViewMode()) {
			double bal = balanceText.getAmount() != null ? balanceText
					.getAmount().doubleValue() : 0.0;
			data.setOpeningBalance(bal);
		} else {
			if (DecimalUtil.isEquals(data.getOpeningBalance(), 0)) {
				data.setOpeningBalance(balanceText.getAmount());
			} else
				data.setBalance(balanceText.getAmount());
		}

		// Setting Balance As of
		if (balanceDate.getEnteredDate() != null)
			data.setBalanceAsOf(balanceDate.getEnteredDate().getDate());
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
		data.setContacts(allContacts);

		// Setting Memo
		if (memoArea.getValue() != null)
			data.setMemo((String) memoArea.getValue());

		// Setting Data from Details Tab

		// Setting Expense Account
		data.setExpenseAccount(Utility.getID(selectAccountFromDetailsTab));

		// Setting Credit Limit
		data.setCreditLimit(creditLimitText.getAmount());

		// Setting Preferred Shipping Method
		data.setShippingMethod(Utility
				.getID(selectShippingMethodFromDetailsTab));

		// Setting Preferred Payment Method
		data.setPaymentMethod(selectPaymentMethodFromDetialsTab != null ? selectPaymentMethodFromDetialsTab
				: preferredPaymentSelect.getSelectedValue());
		// Setting Preferred Payment Terms
		data.setPaymentTerms(Utility.getID(selectPaymentTermFromDetailsTab));

		// Setting Vendor Group
		data.setVendorGroup(Utility.getID(selectVendorGroupFromDetailsTab));
		if (getPreferences().isTrackTax()) {
			if (taxIDText.getValue() != null) {
				data.setFederalTaxId(taxIDText.getValue().toString());
			}
			if (vendorTaxCode != null)
				data.setTAXCode(Utility.getID(selectTaxCodeFromDetailsTab));
			// if (panNumberText.getValue() != null) {
			// data.setPanNumber(panNumberText.getValue().toString());
			// }
			if (getCountryPreferences().isServiceTaxAvailable()) {
				if (serviceTaxRegisterationNumber.getValue() != null) {
					data.setServiceTaxRegistrationNumber(serviceTaxRegisterationNumber
							.getValue().toString());
				}
			}
			if (getCountryPreferences().isVatAvailable()) {
				if (vatRegistrationNumber != null) {
					String vatReg = vatRegistrationNumber.getValue() != null ? vatRegistrationNumber
							.getValue().toString() : "";
					data.setVATRegistrationNumber(vatReg.length() != 0 ? vatReg
							: null);
				}
			}
		}
		// Setting Account Number
		data.setBankAccountNo(accountText.getValue().toString());
		data.setBankName(bankNameText.getValue().toString());
		data.setBankBranch(bankBranchText.getValue().toString());

		// Here No Need to Set AccountPayableAccount. All Vendors will have the
		// Only one Payable Account

		// data.setAccountsPayable(getCompany().getAccountsPayableAccount());
		// Seting opening balance accounts
		data.setOpeningBalanceAccount(getCompany().getOpeningBalancesAccount());
		// data.setAccountsPayable(getCompany().getAccountsPayableAccount());

		// Setting opening balance accounts

		data.setTaxId(taxID.getValue());
		data.setTrackPaymentsFor1099(track1099MISC.isChecked());

	}

	public void addAccountsToList() {
		List<ClientAccount> allAccounts = expenseAccountsSelect.getAccounts();
		expenseAccountsSelect.initCombo(allAccounts);

		if (isInViewMode()) {
			ClientAccount temp = getCompany().getAccount(
					data.getExpenseAccount());
			// Setting Expense Account
			if (temp != null)
				expenseAccountsSelect.setComboItem(temp);
		}

	}

	public void addVendorGroupList() {

		vendorGroupSelect.initCombo(getCompany().getVendorGroups());
		// Setting Vendor Group
		if (isInViewMode()) {
			if (data.getVendorGroup() != 0)
				vendorGroupSelect.setComboItem(company.getVendorGroup(data
						.getVendorGroup()));
		}

	}

	public void addShippingMethodList() {
		preferredShippingSelect.initCombo(getCompany().getShippingMethods());
		// Setting Preferred Shipping Method
		if (isInViewMode()) {
			if (data.getShippingMethod() != 0)
				preferredShippingSelect.setComboItem(company
						.getShippingMethod(data.getShippingMethod()));

		}
	}

	public void addPaymentMethodList() {
		// Setting Preferred Payment Method
		if (isInViewMode()) {
			if (getData().getPaymentMethod() != null)
				preferredPaymentSelect
						.setComboItem(selectPaymentMethodFromDetialsTab);
		}

	}

	public void addPaymentTermsList() {

		payTermsSelect.initCombo(getCompany().getPaymentsTerms());
		// Setting Payment Term
		if (isInViewMode()) {
			if (getData().getPaymentTermsId() != 0)
				payTermsSelect.setComboItem(company.getPaymentTerms(getData()
						.getPaymentTermsId()));
		}

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
		if (data == null) {
			setData(new ClientVendor());
		}
		company = getCompany();
		// getFiscalYear();
		addAccountsToList();
		addVendorGroupList();
		if (getPreferences().isTrackTax()) {
			addSuplierTaxCode();
		}
		addShippingMethodList();
		addPaymentTermsList();
		if (data != null && data.getPhoneNo() != null)
			data.setPhoneNo(data.getPhoneNo());
		if (data != null && data.getFaxNo() != null)
			data.setFaxNo(data.getFaxNo());
		initMainValues();
		addPaymentMethodList();
	}

	private void initMainValues() {
		// Setting Vendor Name
		vendorNameText.setValue(data.getName());

		if (data.getID() == 0) {
			Accounter.createHomeService().getVendorNumber(
					new AccounterAsyncCallback<String>() {

						@Override
						public void onResultSuccess(String result) {
							vendorNoText.setValue(result);
						}

						@Override
						public void onException(AccounterException caught) {
						}
					});
		} else {
			vendorNoText.setValue(data.getVendorNumber());
		}

		vendorNoText.setValue(data.getVendorNumber());
		// Setting File as
		fileAsText.setValue(data.getFileAs());
		data.getPrimaryContact();
		// Setting AddressForm
		addrsForm.setAddress(data.getAddress());
		// addrsForm.setStyleName(FinanceApplication.constants()
		// .venderForm());
		// Setting Phone Fax Form
		fonFaxForm.businessPhoneText.setValue(data.getPhoneNo());
		fonFaxForm.businessFaxText.setValue(data.getFaxNo());
		// Setting Email Form
		emailForm.businesEmailText.setValue(data.getEmail());
		emailForm.webText.setValue(data.getWebPageAddress());
		emailForm.setWidth("100%");
		// Setting Status Check
		statusCheck.setValue(data.isActive());

		track1099MISC.setValue(data.isActive());

		vendorSinceDate.setEnteredDate(new ClientFinanceDate(data
				.getPayeeSince()));

		// Setting Account No
		// accountText.setValue(takenVendor.getBankAccountNo());
		// Setting Balance
		if (!DecimalUtil.isEquals(data.getBalance(), 0)) {
			balanceText.setAmount(data.getBalance());

		} else {
			balanceText.setAmount(0.0);
		}

		balanceText.setDisabled(isInViewMode());
		// Setting Balance as of
		balanceDate
				.setEnteredDate(new ClientFinanceDate(data.getBalanceAsOf()));
		balanceDate.setDisabled(true);

		// Setting Contacts
		// gridView.setAllRows(new
		// ArrayList<ClientContact>(data.getContacts()));
		int row = 0;
		for (ClientContact clientContact : data.getContacts()) {
			if (clientContact.isPrimary()) {
				gridView.add(clientContact);
				gridView.checkColumn(row, 0, true);
			} else {
				gridView.add(clientContact);
			}
			row++;
		}

		// Setting Memo
		memoArea.setValue(data.getMemo());

		// Setting Account
		selectAccountFromDetailsTab = getCompany().getAccount(
				data.getExpenseAccount());

		accountText.setValue(data.getBankAccountNo() != null ? data
				.getBankAccountNo().toString() : "");
		bankNameText.setValue(data.getBankName() != null ? data.getBankName()
				.toString() : "");
		bankBranchText.setValue(data.getBankBranch() != null ? data
				.getBankBranch().toString() : "");

		// Setting Credit Limit Text
		if (!DecimalUtil.isEquals(data.getCreditLimit(), 0))
			creditLimitText.setAmount(data.getCreditLimit());

		// Setting Preferred shipping method
		selectShippingMethodFromDetailsTab = Accounter.getCompany()
				.getShippingMethod(data.getShippingMethod());

		// Setting Payment Method
		selectPaymentMethodFromDetialsTab = data.getPaymentMethod();

		// Setting payment Terms
		selectPaymentTermFromDetailsTab = getCompany().getPaymentTerms(
				(data.getPaymentTermsId()));

		isTDS.setValue(data.isTdsApplicable());
		if (isTDS.getValue()) {
			vendorTDSTaxCode.setDisabled(false);
		}
		vendorTDSTaxCode
				.setSelected(vendorTDSTaxCode.getDisplayName(getCompany()
						.getTAXItem(data.getTaxItemCode())));

		taxID.setValue(data.getTaxId());

		track1099MISC.setValue(data.isTrackPaymentsFor1099());

		// Setting Vendor Group
		ClientVendorGroup vendorGroup = getCompany().getVendorGroup(
				data.getVendorGroup());
		if (vendorGroup != null) {
			selectVendorGroupFromDetailsTab = vendorGroup;
		}
		if (getPreferences().isTrackTax()) {
			if (getCountryPreferences().isVatAvailable()) {
				vatRegistrationNumber.setValue(data.getVATRegistrationNumber());
				vendorTaxCode.setSelected(vendorTaxCode.getDisplayName(data
						.getTAXCode() != 0 ? Accounter.getCompany().getTAXCode(
						data.getTAXCode()) : null));
			} else {
				// Setting Federal Id
				taxIDText.setValue(data.getFederalTaxId());
				if (getCountryPreferences().isServiceTaxAvailable()) {
					serviceTaxRegisterationNumber.setValue(data
							.getServiceTaxRegistrationNumber());
				}
			}
		}
	}

	private void addSuplierTaxCode() {
		vendorTaxCode.initCombo(getCompany().getActiveTaxCodes());
		if (isInViewMode()) {
			vendorTaxCode.setSelected(vendorTaxCode.getDisplayName(data
					.getTAXCode() != 0 ? getCompany().getTAXCode(
					data.getTAXCode()) : null));
			vendorTaxCode.setComboItem(getCompany().getTAXCode(
					data.getTAXCode()));
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
		this.vendorNameText.setFocus();
	}

	@Override
	public void fitToSize(int height, int width) {
		// super.fitToSize(height, width);

	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

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

		this.rpcDoSerivce.canEdit(AccounterCoreType.VENDOR, data.getID(),
				editCallBack);
	}

	protected void enableFormItems() {
		setMode(EditMode.EDIT);
		vendorNameText.setDisabled(isInViewMode());
		statusCheck.setDisabled(isInViewMode());
		addButton.setEnabled(!isInViewMode());
		vendorSinceDate.setDisabled(isInViewMode());
		addrsForm.setDisabled(isInViewMode());
		fonFaxForm.setDisabled(isInViewMode());
		emailForm.setDisabled(isInViewMode());
		gridView.setDisabled(isInViewMode());

		balanceText.setDisabled(!data.isOpeningBalanceEditable());
		expenseAccountsSelect.setDisabled(isInViewMode());
		creditLimitText.setDisabled(isInViewMode());
		preferredShippingSelect.setDisabled(isInViewMode());
		preferredPaymentSelect.setDisabled(isInViewMode());
		payTermsSelect.setDisabled(isInViewMode());
		accountText.setDisabled(isInViewMode());
		bankNameText.setDisabled(isInViewMode());
		bankBranchText.setDisabled(isInViewMode());
		vendorGroupSelect.setDisabled(isInViewMode());
		panNumberText.setDisabled(isInViewMode());
		serviceTaxRegisterationNumber.setDisabled(isInViewMode());
		vatRegistrationNumber.setDisabled(isInViewMode());
		vendorTaxCode.setDisabled(isInViewMode());
		isTDS.setDisabled(isInViewMode());
		vendorTDSTaxCode.setDisabled(isInViewMode());
		taxIDText.setDisabled(isInViewMode());
		super.onEdit();

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	protected String getViewTitle() {
		// return messages.newSupplier(Global.get().Vendor());
		return " ";
	}
}