package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AddButton;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCreditRating;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientCustomerGroup;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.AddressForm;
import com.vimukti.accounter.web.client.ui.EmailForm;
import com.vimukti.accounter.web.client.ui.PhoneFaxForm;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.CreditRatingCombo;
import com.vimukti.accounter.web.client.ui.combo.CurrencyCombo;
import com.vimukti.accounter.web.client.ui.combo.CustomerGroupCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PaymentTermsCombo;
import com.vimukti.accounter.web.client.ui.combo.SalesPersonCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.combo.ShippingMethodsCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.edittable.tables.ContactsTable;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/*
 * @modified by Rajesh.A,Ravi Kiran.G, Murali Annamneni,B.srinivasa rao
 * 
 * 
 */

public class CustomerView extends BaseView<ClientCustomer> {

	/*
	 * TextItem fileAsText, webText, linksText, creditLimitText, emailText,
	 * phoneText, faxText;
	 */
	TextItem custNameText, fileAsText;
	AmountField balanceText, creditLimitText;
	DateField balanceDate, customerSinceDate;
	TextItem linksText, accNameText, sortcode, accNumber, paymentref;
	TextItem vatregno;
	TextAreaItem memoArea;
	AddButton addButton;
	TAXCodeCombo custTaxCode;

	CheckboxItem statusCheck, selectCheckBox;

	// PriceLevelCombo priceLevelSelect;
	CreditRatingCombo creditRatingSelect;

	TextItem bankAccountSelect;
	TextItem bankNameSelect;
	TextItem bankBranchSelect;
	TextItem panNumberText;
	TextItem tinNumberText;
	TextItem cstNumberText;
	TextItem serviceTaxRegistrationNo;

	ShippingMethodsCombo shipMethSelect;
	PaymentTermsCombo payTermsSelect;
	CustomerGroupCombo custGroupSelect;
	SalesPersonCombo salesPersonSelect;

	ContactsTable gridView;
	SelectCombo payMethSelect;

	CurrencyCombo currencyCombo;

	// private ClientCustomer takenCustomer;

	private DynamicForm customerForm;
	private DynamicForm accInfoForm;
	private AddressForm addrsForm;
	private PhoneFaxForm fonFaxForm;
	private EmailForm emailForm;

	// private ClientFiscalYear fiscalYear;
	private DecoratedTabPanel tabSet;
	private String selectPaymentMethodFromDetialsTab;
	protected ClientPriceLevel selectPriceLevelFromDetailsTab;
	protected ClientCreditRating selectCreditRatingFromDetailsTab;
	protected ClientShippingMethod selectShippingMethodFromDetailsTab;
	protected ClientPaymentTerms selectPayTermFromDetailsTab;
	protected ClientCustomerGroup selectCustomerGroupFromDetailsTab;
	private ClientSalesPerson selectSalesPersonFromDetailsTab;
	private ClientTAXCode selectVatCodeFromDetailsTab;

	// protected List<ClientTaxAgency> taxAgencies = new
	// ArrayList<ClientTaxAgency>();

	protected boolean isClose;
	private boolean wait;

	AccounterConstants customerConstants = Accounter.constants();

	private ClientCompany company = getCompany();
	private ArrayList<DynamicForm> listforms;
	private TextItem custNoText;

	// private ClientCustomer customer;

	public CustomerView() {
		super();
	}

	// private void initFiscalYear() {
	// for (ClientFiscalYear fiscalYear : company.getFiscalYears()) {
	// if (fiscalYear.getIsCurrentFiscalYear()) {
	// CustomerView.this.fiscalYear = fiscalYear;
	// // balanceDate.setEnteredDate(CustomerView.this.fiscalYear
	// // .getStartDate());
	// break;
	// }
	// }
	// }

	private void initSalesPersonList() {
		salesPersonSelect.initCombo(company.getActiveSalesPersons());
		if (data != null && data.getSalesPerson() != 0)
			salesPersonSelect.setComboItem(company.getSalesPerson(data
					.getSalesPerson()));

	}

	// private void initTaxItemGroupList() {
	// List<ClientTAXItemGroup> result = getCompany().getTaxItemGroups();
	//
	// for (ClientTAXItemGroup taxItemGroup : result) {
	// if (taxItemGroup.getName().equalsIgnoreCase("None")) {
	// selectTaxGroupFromDetailsTab = taxItemGroup;
	// }
	// }
	// if (data != null && data.getTaxGroup() != null)
	// taxGroupSelect.setComboItem(company.getTAXItemGroup(data
	// .getTaxItemGroups()));
	// else
	// taxGroupSelect.setComboItem(selectTaxGroupFromDetailsTab);
	// }

	private void initCustomerGroupList() {

		custGroupSelect.initCombo(getCompany().getCustomerGroups());

		// Setting Customer Group
		if (data != null && data.getCustomerGroup() != 0)
			custGroupSelect.setComboItem(company.getCustomerGroup(data
					.getCustomerGroup()));

	}

	public void initPaymentTermsList() {
		payTermsSelect.initCombo(getCompany().getPaymentsTerms());
		// Setting Payment Term
		if (data != null && data.getPaymentTerm() != 0)
			payTermsSelect.setComboItem(company.getPaymentTerms(data
					.getPaymentTerm()));

	}

	public void initShippingMethodList() {

		shipMethSelect.initCombo(getCompany().getShippingMethods());
		// Setting Preferred Shipping Method
		if (data != null && data.getShippingMethod() != 0)
			shipMethSelect.setComboItem(company.getShippingMethod(data
					.getShippingMethod()));
	}

	private void initCreditRatingList() {
		creditRatingSelect.initCombo(company.getCreditRatings());

		if (data != null && data.getCreditRating() != 0)
			creditRatingSelect.setComboItem(company.getCreditRating(data
					.getCreditRating()));
	}

	// private void initPriceLevelList() {
	//
	// priceLevelSelect.initCombo(getCompany().getPriceLevels());
	// // Setting Preferred Shipping Method
	// if (data != null && data.getPriceLevel() != 0)
	// priceLevelSelect.setComboItem(company.getPriceLevel(data
	// .getPriceLevel()));
	// }

	private void initVatCodeList() {
		List<ClientTAXCode> taxcodes = company.getActiveTaxCodes();
		if (taxcodes != null)
			custTaxCode.initCombo(taxcodes);
		if (data != null && data.getTAXCode() > 0)
			custTaxCode.setComboItem(company.getTAXCode(data.getTAXCode()));
	}

	private void createControls() {

		tabSet = new DecoratedTabPanel();

		listforms = new ArrayList<DynamicForm>();

		tabSet.add(getGeneralTab(), customerConstants.general());
		tabSet.add(getDetailsTab(), customerConstants.details());

		tabSet.selectTab(0);
		tabSet.setSize("100%", "100%");

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(tabSet);

		this.add(mainVLay);
		setSize("100%", "100%");
	}

	@Override
	public void saveAndUpdateView() {

		if (!wait) {
			// try {
			updateData();
			saveOrUpdate(getData());
			// } catch (Exception e) {
			// e.printStackTrace();
			// throw e;
			// }
		}

	}

	public static String objectExist(ClientCustomer customer) {

		String error = null;

		List<ClientCustomer> list = Accounter.getCompany().getCustomers();
		if (list == null || list.isEmpty())
			return "";
		for (ClientCustomer old : list) {
			if (old.getID() == customer.getID()) {
				continue;
			}
			if (customer.getName().equalsIgnoreCase(old.getName())) {
				for (ClientCustomer old2 : list) {
					if (customer.getNumber().equals(old2.getNumber())) {
						error = Accounter.messages()
								.customerAlreadyExistsWithNameAndNo(
										Global.get().customer());
						break;
					}
				}
				return Accounter.messages().customerAlreadyExistsWithName(
						Global.get().customer());
			} else if (customer.getNumber().equals(old.getNumber())) {
				for (ClientCustomer old2 : list) {
					if (customer.getName().equalsIgnoreCase(old2.getName())) {
						error = Accounter.messages()
								.customerAlreadyExistsWithNameAndNo(
										Global.get().customer());
						break;
					}
				}
				return Accounter.messages().customerAlreadyExistsWithNumber(
						Global.get().customer());
			} else if (checkIfNotNumber(customer.getNumber())) {
				error = Accounter.messages().customerNumberShouldBeNumber(
						Global.get().customer());
				break;
			} else if (Integer.parseInt(customer.getNumber().toString()) < 1) {
				error = Accounter.messages().customerNumberShouldBePos(
						Global.get().customer());
				break;
			}

		}
		return error;
	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		// BaseView.errordata.setHTML(exception.getMessage());
		// BaseView.commentPanel.setVisible(true);
		// this.errorOccured = true;
		AccounterException accounterException = (AccounterException) exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		if (result != null) {
			// if (takenCustomer == null)
			// Accounter.showInformation(FinanceApplication
			// .constants().newCustomerCreated());
			// else
			// Accounter.showInformation(FinanceApplication
			// .constants().customerUpdatedSuccessfully());

			super.saveSuccess(result);

		} else {
			saveFailed(new AccounterException());
		}

	}

	protected void save() {

	}

	protected void clearFields() {

		ActionFactory.getNewCustomerAction().run(null, false);

	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();

		// validate customer form
		// check whether the customer is already available or not
		// grid valid?

		result.add(customerForm.validate());
		ClientFinanceDate asOfDate = balanceDate.getEnteredDate();
		
		gridView.validate(result);

		if (AccounterValidator.isPriorToCompanyPreventPostingDate(asOfDate)) {
			result.addError(balanceDate, Accounter.constants().priorasOfDate());
		}
		data.setName(custNameText.getValue().toString());

		data.setNumber(custNoText.getValue().toString());

		// gridView.validateGrid();

		return result;

		// }
	}

	// private boolean validateCustomerForm(DynamicForm customerForm)
	// throws InvalidEntryException {
	// if (!customerForm.validate(false)) {
	// if (tabSet.getTabBar().isTabEnabled(1))
	// tabSet.selectTab(0);
	// // throw new
	// // InvalidEntryException(AccounterErrorType.REQUIRED_FIELDS);
	// }
	// return true;
	// }

	private void updateData() {

		// Setting data from General Tab

		// Setting customer Name
		// customer.setName(UIUtils.toStr(custNameText.getValue()));
		data.setName(custNameText.getValue().toString());
		// setting customer number
		data.setNumber(custNoText.getValue().toString());

		data.setType(ClientPayee.TYPE_CUSTOMER);
		// Setting File As
		// customer.setFileAs(UIUtils.toStr(fileAsText.getValue()));

		data.setFileAs(fileAsText.getValue().toString());
		// Setting Addresses
		data.setAddress(addrsForm.getAddresss());

		// Setting Phone
		// customer.setPhoneNumbers(fonFaxForm.getAllPhones());
		data.setPhoneNo(fonFaxForm.businessPhoneText.getValue().toString());

		// Setting Fax
		// customer.setFaxNumbers(fonFaxForm.getAllFaxes());
		data.setFaxNo(fonFaxForm.businessFaxText.getValue().toString());

		// Setting Email and Internet
		data.setEmail(emailForm.businesEmailText.getValue().toString());

		// Setting web page Address
		data.setWebPageAddress(emailForm.getWebTextValue());

		// Setting Active
		data.setActive((Boolean) statusCheck.getValue());

		// Setting accout number
		data.setBankAccountNo(bankAccountSelect.getValue().toString());

		// Setting currency
		// data.setCurrency(currencyCombo.getSelectedValue().toString());

		// Setting Bank name
		data.setBankName(bankNameSelect.getValue().toString());
		// Setting Branch name
		data.setBankBranch(bankBranchSelect.getValue().toString());
		if (getPreferences().isTrackTax()) {
			// setting Pan Number
			// data.setPanNumber(panNumberText.getValue().toString());
			if (getCountryPreferences().isSalesTaxAvailable()) {
				// setting for CST Number
				data.setCstNumber(cstNumberText.getValue().toString());
			}
			if (getCountryPreferences().isTDSAvailable()) {
				// setting for TIN Number
				data.setTinNumber(tinNumberText.getValue().toString());
			}
			if (getCountryPreferences().isServiceTaxAvailable()) {
				// setting for Service tax Num
				data.setServiceTaxRegistrationNumber(serviceTaxRegistrationNo
						.getValue().toString());
			}
		}
		// Setting customer Since
		if (customerSinceDate != null
				&& customerSinceDate.getEnteredDate() != null)
			data.setPayeeSince(customerSinceDate.getEnteredDate().getDate());

		// Setting Balance
		// Setting Balance
		if (DecimalUtil.isEquals(data.getOpeningBalance(), 0)) {
			data.setOpeningBalance(balanceText.getAmount());
		} else
			data.setBalance(balanceText.getAmount());
		// Setting Balance As of
		data.setBalanceAsOf(balanceDate.getEnteredDate().getDate());

		// Setting Contacts
		List<ClientContact> allGivenRecords = (List<ClientContact>) gridView
				.getRecords();
		// ListGridRecord selectedRecord = gridView.();
		//
		// if (selectedRecord != null) {
		// System.out.println("Selected Name is "
		// + selectedRecord.getAttribute(ATTR_CONTACT_NAME));
		//
		// }
		Set<ClientContact> allContacts = new HashSet<ClientContact>();

		if (allGivenRecords.isEmpty()) {
			data.setContacts(allContacts);
		}
		for (IsSerializable rec : allGivenRecords) {
			ClientContact tempRecord = (ClientContact) rec;
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
			data.setContacts(allContacts);

		}

		// Setting Memo
		if (memoArea.getValue() != null)
			data.setMemo(memoArea.getValue().toString());

		// Setting Data from Details Tab

		// Setting SalesPerson
		data.setSalesPerson(Utility.getID(selectSalesPersonFromDetailsTab));

		// Setting Credit Limit

		if (creditLimitText.getAmount() != null) {
			data.setCreditLimit(creditLimitText.getAmount());
		}

		// Setting Price Level
		data.setPriceLevel(Utility.getID(selectPriceLevelFromDetailsTab));

		// Setting Credit Rating
		data.setCreditRating(Utility.getID(selectCreditRatingFromDetailsTab));

		// Setting Preferred Shipping Method
		data.setShippingMethod(Utility
				.getID(selectShippingMethodFromDetailsTab));

		// Setting Preferred Payment Method
		data.setPaymentMethod(selectPaymentMethodFromDetialsTab);

		// Setting Preferred Payment Terms
		data.setPaymentTerm(Utility.getID(selectPayTermFromDetailsTab));

		// Setting customer Group
		data.setCustomerGroup(Utility.getID(selectCustomerGroupFromDetailsTab));
		if (getPreferences().isTrackTax()) {
			// Setting Tax Group
			data.setTAXCode(Utility.getID(selectVatCodeFromDetailsTab));
			if (vatregno.getValue() != null)
				data.setVATRegistrationNumber(vatregno.getValue().toString());
		}

	}

	private VerticalPanel getGeneralTab() {

		custNameText = new TextItem(Accounter.messages().customerName(
				Global.get().Customer()));
		TextBox t = new TextBox();
		if (quickAddText != null) {
			custNameText.setValue(quickAddText);
		}

		custNameText.setToolTip(Accounter.messages().customerMeaning(
				Global.get().customer()));
		custNameText.setHelpInformation(true);
		custNameText.setRequired(true);
		custNameText.setWidth(100);
		custNameText.setDisabled(isInViewMode());

		custNoText = new TextItem(Accounter.messages().customerNumber(
				Global.get().Customer()));
		custNoText.setHelpInformation(true);
		custNoText.setRequired(true);
		custNoText.setWidth(100);
		custNameText.setDisabled(isInViewMode());

		fileAsText = new TextItem(customerConstants.fileAs());
		fileAsText.setHelpInformation(true);
		fileAsText.setWidth(100);
		fileAsText.setDisabled(isInViewMode());
		custNameText.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (event.getSource() != null)
					fileAsText.setValue(custNameText.getValue());
			}

		});

		customerForm = UIUtils.form(Global.get().customer());

		if (getCompany().getPreferences().getUseCustomerId()) {
			customerForm.setFields(custNameText, custNoText);
		} else {
			customerForm.setFields(custNameText);
		}
		customerForm.setWidth("100%");
		customerForm.getCellFormatter().setWidth(0, 0, "205");

		// Element ele = DOM.createSpan();
		// ele.addClassName("star");
		// DOM.appendChild(DOM.getChild(customerForm.getElement(), 0), ele);

		accInfoForm = new DynamicForm();
		accInfoForm.setIsGroup(true);
		accInfoForm.setGroupTitle(Accounter.messages().accountInformation(
				Global.get().Account()));

		statusCheck = new CheckboxItem(customerConstants.active());
		statusCheck.setValue(true);
		statusCheck.setDisabled(isInViewMode());

		customerSinceDate = new DateField(Accounter.messages().customerSince(
				Global.get().Customer()));
		customerSinceDate.setHelpInformation(true);
		customerSinceDate.setDisabled(isInViewMode());
		customerSinceDate.setEnteredDate(new ClientFinanceDate());

		balanceText = new AmountField(customerConstants.balance(), this);
		balanceText.setHelpInformation(true);
		balanceText.setDisabled(isInViewMode());
		balanceDate = new DateField(customerConstants.balanceAsOf());
		balanceDate.setHelpInformation(true);
		ClientFinanceDate todaydate = new ClientFinanceDate();
		todaydate.setDay(todaydate.getDay());
		balanceDate.setDatethanFireEvent(todaydate);
		// balanceDate.addDateValueChangeHandler(new DateValueChangeHandler() {
		//
		// @Override
		// public void onDateValueChange(ClientFinanceDate date) {
		// if (data == null) {
		// ClientFinanceDate custSinceDate = customerSinceDate
		// .getDate();
		// if (date.before(custSinceDate)) {
		// String msg = customerConstants.msg();
		// // Accounter.showError(msg);
		// }
		// }
		//
		// }
		//
		// });
		// balanceDate.setUseTextField(true);
		// balanceDate.setTitle(customerConstants.balanceAsOf());
		// balanceDate.setEnteredDate(new Date(company.getPreferences()
		// .getPreventPostingBeforeDate()));
		// balanceDate.addDateValueChangeHandler(new DateValueChangeHandler() {
		//
		// @Override
		// public void onDateValueChange(Date date) {
		//
		// }
		// });
		// if (fiscalYear != null) {
		// fiscalYear.setStartDate(company.getPreferences()
		// .getStartOfFiscalYear());
		// balanceDate.setEnteredDate(fiscalYear.getStartDate());
		// }

		accInfoForm.setWidth("100%");
		accInfoForm.setFields(statusCheck, customerSinceDate, balanceText,
				balanceDate);

		Label l1 = new Label(Accounter.constants().contacts());
		addButton = new AddButton(this);

		addButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ClientContact clientContact = new ClientContact();
				gridView.setDisabled(false);
				gridView.add(clientContact);
				gridView.checkColumn(0, 0, true);
			}
		});
		addButton.setEnabled(!isInViewMode());

		gridView = new ContactsTable();
		gridView.setDisabled(isInViewMode());

		// gridView.setCanEdit(!isInViewMode());
		// gridView.setEditEventType(ListGrid.EDIT_EVENT_CLICK);
		// gridView.isEnable = false;
		// gridView.init();

		VerticalPanel panel = new VerticalPanel() {
			@Override
			protected void onAttach() {

				// gridView.setHeight("88px");

				super.onAttach();
			}
		};
		panel.setWidth("100%");
		panel.add(l1);
		panel.add(gridView);
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.add(addButton);
		hPanel.getElement().getStyle().setMarginTop(8, Unit.PX);
		hPanel.getElement().getStyle().setFloat(Float.LEFT);
		panel.add(hPanel);
		memoArea = new TextAreaItem();
		memoArea.setWidth("400px");
		memoArea.setTitle(customerConstants.memo());
		memoArea.setToolTip(Accounter.messages().writeCommentsForThis(
				this.getAction().getViewName()));
		// Button addLinksButt = new Button("AddLinks");
		// linksText = new TextItem("");
		// linksText.setWidth(100);
		DynamicForm memoForm = new DynamicForm();
		// memoForm.setWidth("100%");
		memoForm.setFields(memoArea);
		memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");
		// memoForm.setWidget(2, 0, addLinksButt);
		// memoForm.setWidget(2, 1, linksText.getMainWidget());
		HorizontalPanel bottomLayout = new HorizontalPanel();
		// bottomLayout.setWidth("100%");
		bottomLayout.add(memoForm);

		// For Editing customer

		addrsForm = new AddressForm(null);
		addrsForm.setWidth("100%");
		addrsForm.setDisabled(isInViewMode());
		fonFaxForm = new PhoneFaxForm(null, null, this, this.getAction()
				.getViewName());
		fonFaxForm.setWidth("100%");
		fonFaxForm.setDisabled(isInViewMode());
		emailForm = new EmailForm(null, null, this, this.getAction()
				.getViewName());
		emailForm.setWidth("100%");
		emailForm.setDisabled(isInViewMode());

		/* Adding Dynamic Forms in List */
		listforms.add(customerForm);
		listforms.add(accInfoForm);
		listforms.add(memoForm);
		addrsForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");
		addrsForm.getCellFormatter().addStyleName(0, 1, "memoFormAlign");
		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setWidth("100%");
		leftVLay.add(customerForm);
		leftVLay.add(accInfoForm);
		// leftVLay.add(fonFaxForm);
		// leftVLay.add(emailForm);

		VerticalPanel rightVLay = new VerticalPanel();
		rightVLay.setWidth("100%");
		rightVLay.add(addrsForm);
		rightVLay.add(fonFaxForm);
		rightVLay.add(emailForm);

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.setSpacing(5);
		topHLay.setWidth("100%");
		topHLay.add(leftVLay);
		topHLay.add(rightVLay);
		topHLay.setCellWidth(rightVLay, "50%");

		HorizontalPanel contHLay = new HorizontalPanel();

		VerticalPanel mainVlay = new VerticalPanel();

		mainVlay.add(topHLay);
		mainVlay.add(contHLay);
		mainVlay.add(panel);
		// mainVlay.add(memoForm);
		// mainVlay.add(bottomLayout);
		mainVlay.setWidth("100%");

		if (UIUtils.isMSIEBrowser())
			resetFromView();

		return mainVlay;

	}

	private void resetFromView() {
		addrsForm.getCellFormatter().setWidth(0, 0, "75");
		addrsForm.getCellFormatter().setWidth(0, 1, "125");

		fonFaxForm.getCellFormatter().setWidth(0, 0, "75");
		fonFaxForm.getCellFormatter().setWidth(0, 1, "125");

		emailForm.getCellFormatter().setWidth(0, 0, "190");
		emailForm.getCellFormatter().setWidth(0, 1, "150");

		memoArea.getMainWidget().setWidth("250px");

	}

	protected void adjustFormWidths(int titlewidth, int listBoxWidth) {

		addrsForm.getCellFormatter().getElement(0, 0)
				.setAttribute("width", titlewidth + "");

		addrsForm.getCellFormatter().getElement(0, 1)
				.setAttribute(Accounter.constants().width(), "185px");

		fonFaxForm.getCellFormatter().getElement(0, 0)
				.setAttribute(Accounter.constants().width(), "240px");
		// fonFaxForm.getCellFormatter().getElement(0, 1).setAttribute(
		// FinanceApplication.constants().width(), "185px");

		customerForm.getCellFormatter().getElement(0, 0).getStyle()
				.setWidth(150, Unit.PX);
		emailForm.getCellFormatter().getElement(0, 0)
				.setAttribute(Accounter.constants().width(), "240px");
		// emailForm.getCellFormatter().getElement(0, 1).setAttribute(
		// FinanceApplication.constants().width(), "");
		accInfoForm.getCellFormatter().getElement(0, 0)
				.setAttribute(Accounter.constants().width(), "150px");

	}

	private HorizontalPanel getDetailsTab() {

		salesPersonSelect = new SalesPersonCombo(
				customerConstants.salesPerson());
		salesPersonSelect.setHelpInformation(true);
		salesPersonSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientSalesPerson>() {

					public void selectedComboBoxItem(
							ClientSalesPerson selectItem) {
						selectSalesPersonFromDetailsTab = selectItem;

					}

				});

		// DynamicForm salesForm = UIUtils.form(customerConstants.sales());
		// salesForm.setFields(salesPersonSelect);
		// salesForm.setWidth("100%");

		creditLimitText = new AmountField(customerConstants.creditLimit(), this);
		creditLimitText.setHelpInformation(true);
		creditLimitText.setWidth(100);

		// priceLevelSelect = new
		// PriceLevelCombo(customerConstants.priceLevel());
		// priceLevelSelect.setHelpInformation(true);
		// priceLevelSelect
		// .addSelectionChangeHandler(new
		// IAccounterComboSelectionChangeHandler<ClientPriceLevel>() {
		//
		// public void selectedComboBoxItem(ClientPriceLevel selectItem) {
		// selectPriceLevelFromDetailsTab = selectItem;
		//
		// }
		//
		// });

		creditRatingSelect = new CreditRatingCombo(
				customerConstants.creditRating());
		creditRatingSelect.setHelpInformation(true);
		creditRatingSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientCreditRating>() {

					public void selectedComboBoxItem(
							ClientCreditRating selectItem) {
						selectCreditRatingFromDetailsTab = selectItem;
					}

				});
		currencyCombo = new CurrencyCombo(Accounter.constants().currency());
		currencyCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientCurrency>() {

					@Override
					public void selectedComboBoxItem(ClientCurrency selectItem) {
						// selectCurrency = selectItem;
					}
				});
		bankAccountSelect = new TextItem(Accounter.messages().bankAccountNo(
				Global.get().Account()));
		bankAccountSelect.setHelpInformation(true);
		bankNameSelect = new TextItem(customerConstants.bankName());
		bankNameSelect.setHelpInformation(true);
		bankBranchSelect = new TextItem(customerConstants.bankBranch());
		bankBranchSelect.setHelpInformation(true);
		panNumberText = new TextItem(Accounter.messages().panNumber(
				Global.get().Account()));
		panNumberText.setHelpInformation(true);
		cstNumberText = new TextItem(customerConstants.cstNumber());
		cstNumberText.setHelpInformation(true);
		serviceTaxRegistrationNo = new TextItem(
				customerConstants.serviceTaxRegistrationNumber());
		serviceTaxRegistrationNo.setHelpInformation(true);
		tinNumberText = new TextItem(customerConstants.tinNumber());
		tinNumberText.setHelpInformation(true);

		DynamicForm financeDitailsForm = UIUtils.form(customerConstants
				.financialDetails());

		financeDitailsForm.setFields(salesPersonSelect, creditRatingSelect,
				bankNameSelect, bankAccountSelect, bankBranchSelect);

		if (getPreferences().isTrackTax()) {

			if (getCountryPreferences().isSalesTaxAvailable()) {
				financeDitailsForm.setFields(cstNumberText);
			}
			if (getCountryPreferences().isServiceTaxAvailable()) {
				financeDitailsForm.setFields(serviceTaxRegistrationNo);
			}
			if (getCountryPreferences().isTDSAvailable()) {
				financeDitailsForm.setFields(tinNumberText);
			}

		}
		financeDitailsForm.setWidth("100%");

		shipMethSelect = new ShippingMethodsCombo(
				customerConstants.preferredShippingMethod());
		shipMethSelect.setHelpInformation(true);
		shipMethSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientShippingMethod>() {

					public void selectedComboBoxItem(
							ClientShippingMethod selectItem) {
						selectShippingMethodFromDetailsTab = selectItem;

					}

				});

		payMethSelect = UIUtils.getPaymentMethodCombo();
		payMethSelect.setHelpInformation(true);
		// payMethSelect.setWidth(100);

		payMethSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {
					@Override
					public void selectedComboBoxItem(String selectItem) {
						selectPaymentMethodFromDetialsTab = payMethSelect
								.getSelectedValue();
					}
				});
		selectPaymentMethodFromDetialsTab = payMethSelect.getSelectedValue();
		payTermsSelect = new PaymentTermsCombo(customerConstants.paymentTerms());
		payTermsSelect.setHelpInformation(true);
		payTermsSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientPaymentTerms>() {

					public void selectedComboBoxItem(
							ClientPaymentTerms selectItem) {
						selectPayTermFromDetailsTab = selectItem;

					}

				});

		custGroupSelect = new CustomerGroupCombo(Accounter.messages()
				.customerGroup(Global.get().Customer()));
		custGroupSelect.setHelpInformation(true);
		custGroupSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientCustomerGroup>() {

					public void selectedComboBoxItem(
							ClientCustomerGroup selectItem) {
						selectCustomerGroupFromDetailsTab = selectItem;

					}

				});

		vatregno = new TextItem(Accounter.constants().vatRegistrationNumber());
		vatregno.setHelpInformation(true);
		vatregno.setWidth(100);
		custTaxCode = new TAXCodeCombo(constants.taxCode(), true);
		custTaxCode.setHelpInformation(true);
		custTaxCode
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXCode>() {

					public void selectedComboBoxItem(ClientTAXCode selectItem) {
						selectVatCodeFromDetailsTab = selectItem;
					}

				});

		DynamicForm termsForm = UIUtils.form(customerConstants.terms());

		termsForm.setFields(payMethSelect, payTermsSelect, custGroupSelect);

		if (getPreferences().isTrackTax()) {
			if (getCountryPreferences().isVatAvailable()) {
				termsForm.setFields(vatregno);
			}
			termsForm.setFields(custTaxCode);
			// custTaxCode.setRequired(true);
		}
		if (getPreferences().isDoProductShipMents()) {
			termsForm.setFields(shipMethSelect);
		}
		salesPersonSelect.setDisabled(isInViewMode());
		creditLimitText.setDisabled(isInViewMode());
		// priceLevelSelect.setDisabled(isInViewMode());
		creditRatingSelect.setDisabled(isInViewMode());
		currencyCombo.setDisabled(isInViewMode());
		bankAccountSelect.setDisabled(isInViewMode());
		bankNameSelect.setDisabled(isInViewMode());
		bankBranchSelect.setDisabled(isInViewMode());
		panNumberText.setDisabled(isInViewMode());
		cstNumberText.setDisabled(isInViewMode());
		serviceTaxRegistrationNo.setDisabled(isInViewMode());
		tinNumberText.setDisabled(isInViewMode());
		shipMethSelect.setDisabled(isInViewMode());
		payMethSelect.setDisabled(isInViewMode());
		payTermsSelect.setDisabled(isInViewMode());
		custGroupSelect.setDisabled(isInViewMode());
		vatregno.setDisabled(isInViewMode());
		custTaxCode.setDisabled(isInViewMode());

		termsForm.setWidth("100%");

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setSize("100%", "100%");
		leftVLay.setHeight("350px");
		// leftVLay.getElement().getStyle()
		// .setBorderColor("none repeat scroll 0 0 #eee !important");
		VerticalPanel rightVLay = new VerticalPanel();
		rightVLay.setWidth("100%");

		rightVLay.add(termsForm);

		// leftVLay.add(salesForm);

		leftVLay.add(financeDitailsForm);

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.setSpacing(15);
		topHLay.add(leftVLay);
		topHLay.add(rightVLay);
		topHLay.setSize("100%", "100%");

		// listforms.add(salesForm);
		listforms.add(financeDitailsForm);
		listforms.add(termsForm);

		if (UIUtils.isMSIEBrowser()) {
			financeDitailsForm.getCellFormatter().setWidth(0, 1, "200px");
			// salesForm.getCellFormatter().setWidth(0, 1, "200px");
			termsForm.getCellFormatter().setWidth(0, 1, "200px");
			financeDitailsForm.setWidth("80%");
			// salesForm.setWidth("80%");
			termsForm.setWidth("80%");
		}

		return topHLay;
	}

	@Override
	public void init() {
		super.init();
		createControls();
		setSize("100%", "100%");
	}

	@Override
	public void initData() {
		// if (takenCustomer == null)
		// initFiscalYear();
		if (data == null) {
			setData(new ClientCustomer());
		}
		// initTaxAgenciesList();
		initMainValues();
		initSalesPersonList();
		initCustomerGroupList();
		initPaymentTermsList();
		initShippingMethodList();
		initCreditRatingList();
		// initPriceLevelList();
		if (data != null && data.getPhoneNo() != null)
			data.setPhoneNo(data.getPhoneNo());
		if (data != null && data.getFaxNo() != null)
			data.setFaxNo(data.getFaxNo());
		if (getPreferences().isTrackTax()) {
			initVatCodeList();
		}
		super.initData();

	}

	private void initMainValues() {
		// Setting Customer Name
		custNameText.setValue(data.getName());
		// Setting customer number
		if (data.getID() == 0) {
			Accounter.createHomeService().getCustomerNumber(
					new AccounterAsyncCallback<String>() {

						@Override
						public void onResultSuccess(String result) {
							custNoText.setValue(result);
						}

						@Override
						public void onException(AccounterException caught) {
						}
					});
		} else {
			custNoText.setValue(data.getNumber());
		}
		// Setting File as
		fileAsText.setValue(data.getFileAs());
		fonFaxForm.businessPhoneText.setValue(data.getPhoneNo());
		fonFaxForm.businessFaxText.setValue(data.getFaxNo());
		emailForm.businesEmailText.setValue(data.getEmail());
		emailForm.webText.setValue(data.getWebPageAddress());

		// Setting Status Check
		statusCheck.setValue(data.isActive());

		// Setting Customer Since
		customerSinceDate.setEnteredDate(new ClientFinanceDate(data
				.getPayeeSince()));
		if (!DecimalUtil.isEquals(data.getBalance(), 0)) {
			balanceText.setAmount(data.getBalance());
			balanceText.setDisabled(true);
		}

		// Setting Balance as of
		balanceDate
				.setEnteredDate(new ClientFinanceDate(data.getBalanceAsOf()));
		balanceDate.setDisabled(true);
		// Setting Memo
		memoArea.setValue(data.getMemo());

		bankAccountSelect.setValue(data.getBankAccountNo());
		bankNameSelect.setValue(data.getBankName());
		bankBranchSelect.setValue(data.getBankBranch());
		panNumberText.setValue(data.getPanNumber());
		cstNumberText.setValue(data.getCstNumber());
		serviceTaxRegistrationNo.setValue(data
				.getServiceTaxRegistrationNumber());
		tinNumberText.setValue(data.getTinNumber());

		if (getPreferences().isTrackTax()
				&& getCountryPreferences().isVatAvailable()) {
			// setting vatRegistrationNumber
			vatregno.setValue(data.getVATRegistrationNumber());
		}

		// Setting AddressForm
		addrsForm.setAddress(data.getAddress());
		// Setting Contacts
		gridView.setAllRows(new ArrayList<ClientContact>(data.getContacts()));

		// Setting salesPerson
		selectSalesPersonFromDetailsTab = getCompany().getSalesPerson(
				data.getSalesPerson());

		// Setting Credit Limit Text
		if (!DecimalUtil.isEquals(data.getCreditLimit(), 0))
			creditLimitText.setAmount(data.getCreditLimit());

		// Setting price level
		selectPriceLevelFromDetailsTab = getCompany().getPriceLevel(
				data.getPriceLevel());
		// Setting Credit Rating
		selectCreditRatingFromDetailsTab = getCompany().getCreditRating(
				data.getCreditRating());
		// Setting Shipping Method
		selectShippingMethodFromDetailsTab = Accounter.getCompany()
				.getShippingMethod(data.getShippingMethod());
		// Setting Payment Method
		// selectPaymentMethodFromDetialsTab = takenCustomer
		// .getPaymentMethod();
		payMethSelect.setComboItem(data.getPaymentMethod());
		// Setting payemnt term
		selectPayTermFromDetailsTab = getCompany().getPaymentTerms(
				data.getPaymentTerm());
		// Setting Customer Group
		selectCustomerGroupFromDetailsTab = getCompany().getCustomerGroup(
				data.getCustomerGroup());
		if (getPreferences().isTrackTax()) {
			selectVatCodeFromDetailsTab = getCompany().getTAXCode(
					data.getTAXCode());
		}
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
	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */

	@Override
	public void setFocus() {
		this.custNameText.setFocus();
		// this.custNoText.setFocus();
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);
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

		this.rpcDoSerivce.canEdit(AccounterCoreType.CUSTOMER, data.getID(),
				editCallBack);

	}

	private void enableFormItems() {
		setMode(EditMode.EDIT);
		custNameText.setDisabled(isInViewMode());
		addButton.setEnabled(!isInViewMode());
		custNoText.setDisabled(isInViewMode());
		customerSinceDate.setDisabled(isInViewMode());
		balanceText.setDisabled(isInViewMode());
		addrsForm.setDisabled(isInViewMode());
		statusCheck.setDisabled(isInViewMode());
		fonFaxForm.setDisabled(isInViewMode());
		emailForm.setDisabled(isInViewMode());
		gridView.setDisabled(isInViewMode());
		salesPersonSelect.setDisabled(isInViewMode());
		creditLimitText.setDisabled(isInViewMode());
		// priceLevelSelect.setDisabled(isInViewMode());
		creditRatingSelect.setDisabled(isInViewMode());
		currencyCombo.setDisabled(isInViewMode());
		bankAccountSelect.setDisabled(isInViewMode());
		bankNameSelect.setDisabled(isInViewMode());
		bankBranchSelect.setDisabled(isInViewMode());
		panNumberText.setDisabled(isInViewMode());
		cstNumberText.setDisabled(isInViewMode());
		serviceTaxRegistrationNo.setDisabled(isInViewMode());
		tinNumberText.setDisabled(isInViewMode());
		shipMethSelect.setDisabled(isInViewMode());
		payMethSelect.setDisabled(isInViewMode());
		payTermsSelect.setDisabled(isInViewMode());
		custGroupSelect.setDisabled(isInViewMode());
		vatregno.setDisabled(isInViewMode());
		custTaxCode.setDisabled(isInViewMode());
		super.onEdit();

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// NOTHING TO DO
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().customer();
	}
}
