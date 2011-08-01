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
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCreditRating;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientCustomerGroup;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientFiscalYear;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTAXItemGroup;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.AddressForm;
import com.vimukti.accounter.web.client.ui.EmailForm;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.PhoneFaxForm;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.CreditRatingCombo;
import com.vimukti.accounter.web.client.ui.combo.CustomerGroupCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PaymentTermsCombo;
import com.vimukti.accounter.web.client.ui.combo.PriceLevelCombo;
import com.vimukti.accounter.web.client.ui.combo.SalesPersonCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.combo.ShippingMethodsCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.combo.TaxGroupCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.ContactGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

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

	TAXCodeCombo custTaxCode;

	CheckboxItem statusCheck, selectCheckBox;

	PriceLevelCombo priceLevelSelect;
	CreditRatingCombo creditRatingSelect;

	TextItem bankAccountSelect;
	TextItem bankNameSelect;
	TextItem bankBranchSelect;

	ShippingMethodsCombo shipMethSelect;
	PaymentTermsCombo payTermsSelect;
	CustomerGroupCombo custGroupSelect;
	TaxGroupCombo taxGroupSelect;
	SalesPersonCombo salesPersonSelect;

	ContactGrid gridView;
	SelectCombo payMethSelect;

	private ClientCustomer takenCustomer;

	private DynamicForm customerForm, vatinfo;
	private DynamicForm accInfoForm;
	private AddressForm addrsForm;
	private PhoneFaxForm fonFaxForm;
	private EmailForm emailForm;

	private ClientFiscalYear fiscalYear;
	private DecoratedTabPanel tabSet;
	private String selectPaymentMethodFromDetialsTab;
	protected ClientPriceLevel selectPriceLevelFromDetailsTab;
	protected ClientCreditRating selectCreditRatingFromDetailsTab;
	protected ClientShippingMethod selectShippingMethodFromDetailsTab;
	protected ClientPaymentTerms selectPayTermFromDetailsTab;
	protected ClientCustomerGroup selectCustomerGroupFromDetailsTab;
	protected ClientTAXItemGroup selectTaxGroupFromDetailsTab;
	private ClientSalesPerson selectSalesPersonFromDetailsTab;
	private ClientTAXCode selectVatCodeFromDetailsTab;

	// protected List<ClientTaxAgency> taxAgencies = new
	// ArrayList<ClientTaxAgency>();

	protected boolean isClose;
	private boolean wait;

	AccounterConstants customerConstants;

	private static AccounterConstants actionsConstants;
	private ClientCompany company = getCompany();
	private ArrayList<DynamicForm> listforms;
	private TextItem custNoText;

	public CustomerView() {
		super();
		this.validationCount = 7;
		// this.removeStyleName("abstract_base_view");
	}

	private void initFiscalYear() {
		for (ClientFiscalYear fiscalYear : company.getFiscalYears()) {
			if (fiscalYear.getIsCurrentFiscalYear()) {
				CustomerView.this.fiscalYear = fiscalYear;
				// balanceDate.setEnteredDate(CustomerView.this.fiscalYear
				// .getStartDate());
				break;
			}
		}
	}

	private void initSalesPersonList() {
		salesPersonSelect.initCombo(company.getActiveSalesPersons());
		if (takenCustomer != null && takenCustomer.getSalesPerson() != 0)
			salesPersonSelect.setComboItem(company.getSalesPerson(takenCustomer
					.getSalesPerson()));

	}

	private void initTaxAgenciesList() {
		// List<ClientTaxAgency> result = FinanceApplication.getCompany()
		// .getActiveTaxAgencies();
		// if (result != null)
		// taxAgencies = result;
	}

	private void initTaxItemGroupList() {
		List<ClientTAXItemGroup> result = getCompany().getTaxItemGroups();

		for (ClientTAXItemGroup taxItemGroup : result) {
			if (taxItemGroup.getName().equalsIgnoreCase("None")) {
				selectTaxGroupFromDetailsTab = taxItemGroup;
			}
		}
		if (takenCustomer != null && takenCustomer.getTaxGroup() != null)
			taxGroupSelect.setComboItem(company.getTAXItemGroup(takenCustomer
					.getTaxItemGroups()));
		else
			taxGroupSelect.setComboItem(selectTaxGroupFromDetailsTab);
	}

	private void initCustomerGroupList() {

		custGroupSelect.initCombo(getCompany().getCustomerGroups());

		// Setting Customer Group
		if (takenCustomer != null && takenCustomer.getCustomerGroup() != 0)
			custGroupSelect.setComboItem(company.getCustomerGroup(takenCustomer
					.getCustomerGroup()));

	}

	public void initPaymentTermsList() {
		payTermsSelect.initCombo(getCompany().getPaymentsTerms());
		// Setting Payment Term
		if (takenCustomer != null && takenCustomer.getPaymentTerm() != 0)
			payTermsSelect.setComboItem(company.getPaymentTerms(takenCustomer
					.getPaymentTerm()));

	}

	public void initShippingMethodList() {

		shipMethSelect.initCombo(getCompany().getShippingMethods());
		// Setting Preferred Shipping Method
		if (takenCustomer != null && takenCustomer.getShippingMethod() != 0)
			shipMethSelect.setComboItem(company.getShippingMethod(takenCustomer
					.getShippingMethod()));
	}

	private void initCreditRatingList() {
		creditRatingSelect.initCombo(company.getCreditRatings());

		if (takenCustomer != null && takenCustomer.getCreditRating() != 0)
			creditRatingSelect.setComboItem(company
					.getCreditRating(takenCustomer.getCreditRating()));
	}

	private void initPriceLevelList() {

		priceLevelSelect.initCombo(getCompany().getPriceLevels());
		// Setting Preferred Shipping Method
		if (takenCustomer != null && takenCustomer.getPriceLevel() != 0)
			priceLevelSelect.setComboItem(company.getPriceLevel(takenCustomer
					.getPriceLevel()));
	}

	private void initVatCodeList() {
		List<ClientTAXCode> taxcodes = company.getActiveTaxCodes();
		if (taxcodes != null)
			custTaxCode.initCombo(taxcodes);
		if (takenCustomer != null && takenCustomer.getTAXCode() != 0)
			custTaxCode.setComboItem(company.getTAXCode(takenCustomer
					.getTAXCode()));
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

		canvas.add(mainVLay);
		canvas.getParent().removeStyleName("main-class-pannel");
		setSize("100%", "100%");
	}

	@Override
	public void saveAndUpdateView() throws Exception {

		if (!wait) {
			try {
				ClientCustomer customer = getCustomerObject();
				if (takenCustomer == null) {
					if (!isObjectExist(company.getCustomers(), customer))
						createObject(customer);
				} else
					alterObject(customer);
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}

	}

	public static <S extends IAccounterCore> boolean isObjectExist(
			List<S> list, ClientCustomer customer) throws InvalidEntryException {

		if (list == null || list.isEmpty())
			return false;
		for (S s : list) {
			ClientCustomer old = (ClientCustomer) s;
			if (customer.getName().equalsIgnoreCase(old.getName())) {
				for (S s2 : list) {
					ClientCustomer old2 = (ClientCustomer) s2;
					if (customer.getNumber().equals(old2.getNumber())) {
						throw new InvalidEntryException(Accounter.constants()
								.customerAlreadyExistsWithNameAndNo());
					}
				}
				throw new InvalidEntryException(Accounter.constants()
						.customerAlreadyExistsWithName());
			} else if (customer.getNumber().equals(old.getNumber())) {
				for (S s2 : list) {
					ClientCustomer old2 = (ClientCustomer) s2;
					if (customer.getName().equalsIgnoreCase(old2.getName())) {
						throw new InvalidEntryException(Accounter.constants()
								.customerAlreadyExistsWithNameAndNo());
					}
				}
				throw new InvalidEntryException(Accounter.constants()
						.customerAlreadyExistsWithNumber());
			} else if (checkIfNotNumber(customer.getNumber())) {
				throw new InvalidEntryException(Accounter.constants()
						.customerNumberShouldBeNumber());
			} else if (Integer.parseInt(customer.getNumber().toString()) < 1) {
				throw new InvalidEntryException(Accounter.constants()
						.customerNumberShouldBePos());
			}

		}
		return false;
	}

	@Override
	public void saveFailed(Throwable exception) {
		super.saveFailed(exception);
		// BaseView.errordata.setHTML(exception.getMessage());
		// BaseView.commentPanel.setVisible(true);
		// this.errorOccured = true;
		MainFinanceWindow.getViewManager().showError(exception.getMessage());
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
			saveFailed(new Exception());
		}

	}

	protected void save() {

	}

	protected void clearFields() {

		ActionFactory.getNewCustomerAction().run(null, false);

	}

	@Override
	public boolean validate() throws InvalidTransactionEntryException,
			InvalidEntryException {

		switch (this.validationCount) {
		case 7:
			String name = custNameText.getValue().toString();
			String number = custNoText.getValue().toString();
			if (takenCustomer == null) {
				// if (isObjectExistWithNameAndNumber(company.getCustomers(),
				// name, number)) {
				// throw new InvalidEntryException(
				// "A Customer already exists with this name and number");
				// } else if (isObjectExistWithName(company.getCustomers(),
				// name)) {
				// throw new InvalidEntryException(
				// "A Customer already exists with this name");
				// } else if (isObjectExistWithNumber(company.getCustomers(),
				// number)) {
				// throw new InvalidEntryException(
				// "A Customer already exists with this number");
				// } else
				return true;

			}
			/*
			 * else if (takenCustomer != null &&
			 * (!takenCustomer.getName().equalsIgnoreCase(name))) { if
			 * (Utility.isObjectExist(company.getCustomers(), name)) throw new
			 * InvalidEntryException( AccounterErrorType.ALREADYEXIST); else
			 * return true; }
			 */
			return false;
		case 6:
			return validateCustomerForm(customerForm);
		case 5:
			if (company.getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
				return AccounterValidator.validateFormItem(custTaxCode, false);
			return true;
		case 4:
			// Date customerSince = customerSinceDate.getEnteredDate();
			// return AccounterValidator.sinceDate(customerSince, this);
			return true;

		case 3:
			// return AccounterValidator.validateClosedFiscalYear(balanceDate
			// .getEnteredDate());
			return true;
		case 2:
			ClientFinanceDate asOfDate = balanceDate.getEnteredDate();

			return AccounterValidator.isPriorAsOfDate(asOfDate, this);
			// return true;
		case 1:
			return gridView.validateGrid();
		default:
			return true;

		}
	}

	private boolean validateCustomerForm(DynamicForm customerForm)
			throws InvalidEntryException {
		if (!customerForm.validate(false)) {
			if (tabSet.getTabBar().isTabEnabled(1))
				tabSet.selectTab(0);
			// throw new
			// InvalidEntryException(AccounterErrorType.REQUIRED_FIELDS);
		}
		return true;
	}

	private ClientCustomer getCustomerObject() {

		if (takenCustomer != null)
			customer = takenCustomer;
		else
			customer = new ClientCustomer();

		// Setting data from General Tab

		// Setting customer Name
		// customer.setName(UIUtils.toStr(custNameText.getValue()));
		customer.setName(custNameText.getValue().toString());
		// setting customer number
		customer.setNumber(custNoText.getValue().toString());

		customer.setType(ClientPayee.TYPE_CUSTOMER);
		// Setting File As
		// customer.setFileAs(UIUtils.toStr(fileAsText.getValue()));

		customer.setFileAs(fileAsText.getValue().toString());
		// Setting Addresses
		customer.setAddress(addrsForm.getAddresss());

		// Setting Phone
		// customer.setPhoneNumbers(fonFaxForm.getAllPhones());
		customer.setPhoneNo(fonFaxForm.businessPhoneText.getValue().toString());

		// Setting Fax
		// customer.setFaxNumbers(fonFaxForm.getAllFaxes());
		customer.setFaxNo(fonFaxForm.businessFaxText.getValue().toString());

		// Setting Email and Internet
		customer.setEmail(emailForm.businesEmailText.getValue().toString());

		// Setting web page Address
		customer.setWebPageAddress(emailForm.getWebTextValue());

		// Setting Active
		customer.setActive((Boolean) statusCheck.getValue());

		// Setting accout number
		customer.setBankAccountNo(bankAccountSelect.getValue().toString());

		// Setting Bank name
		customer.setBankName(bankNameSelect.getValue().toString());

		// Setting Branch name
		customer.setBankBranch(bankBranchSelect.getValue().toString());

		// Setting customer Since
		if (customerSinceDate != null
				&& customerSinceDate.getEnteredDate() != null)
			customer.setPayeeSince(customerSinceDate.getEnteredDate().getDate());

		// Setting Balance
		// Setting Balance
		if (takenCustomer == null) {
			double bal = balanceText.getAmount() != null ? balanceText
					.getAmount().doubleValue() : 0.0;
			customer.setOpeningBalance(bal);
		} else {
			if (DecimalUtil.isEquals(takenCustomer.getOpeningBalance(), 0)) {
				customer.setOpeningBalance(balanceText.getAmount());
			} else
				customer.setBalance(balanceText.getAmount());
		}
		// Setting Balance As of
		customer.setBalanceAsOf(balanceDate.getEnteredDate().getDate());

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
			customer.setContacts(allContacts);

		}

		// Setting Memo
		if (memoArea.getValue() != null)
			customer.setMemo(memoArea.getValue().toString());

		// Setting Data from Details Tab

		// Setting SalesPerson
		customer.setSalesPerson(Utility.getID(selectSalesPersonFromDetailsTab));

		// Setting Credit Limit

		if (creditLimitText.getAmount() != null) {
			customer.setCreditLimit(creditLimitText.getAmount());
		}

		// Setting Price Level
		customer.setPriceLevel(Utility.getID(selectPriceLevelFromDetailsTab));

		// Setting Credit Rating
		customer.setCreditRating(Utility
				.getID(selectCreditRatingFromDetailsTab));

		// Setting Preferred Shipping Method
		customer.setShippingMethod(Utility
				.getID(selectShippingMethodFromDetailsTab));

		// Setting Preferred Payment Method
		customer.setPaymentMethod(selectPaymentMethodFromDetialsTab);

		// Setting Preferred Payment Terms
		customer.setPaymentTerm(Utility.getID(selectPayTermFromDetailsTab));

		// Setting customer Group
		customer.setCustomerGroup(Utility
				.getID(selectCustomerGroupFromDetailsTab));
		if (company.getAccountingType() == 0)
			// Setting Tax Group
			customer.setTAXCode(Utility.getID(selectVatCodeFromDetailsTab));

		else if (company.getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			// setting Vat Code
			customer.setTAXCode(Utility.getID(selectVatCodeFromDetailsTab));
			if (vatregno.getValue() != null)
				customer.setVATRegistrationNumber(vatregno.getValue()
						.toString());

			// Setting Company to the customer
		}
		return customer;

	}

	private VerticalPanel getGeneralTab() {

		custNameText = new TextItem(customerConstants.customerName());
		custNameText.setHelpInformation(true);
		custNameText.setRequired(true);
		custNameText.setWidth(100);

		custNoText = new TextItem(customerConstants.customerNumber());
		custNoText.setHelpInformation(true);
		custNoText.setRequired(true);
		custNoText.setWidth(100);

		fileAsText = new TextItem(customerConstants.fileAs());
		fileAsText.setHelpInformation(true);
		fileAsText.setWidth(100);
		custNameText.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (event.getSource() != null)
					fileAsText.setValue(custNameText.getValue());
			}

		});
		if (takenCustomer == null)
			Accounter.createHomeService().getCustomerNumber(
					new AccounterAsyncCallback<String>() {

						@Override
						public void onSuccess(String result) {
							custNoText.setValue(result);
						}

						@Override
						public void onException(AccounterException caught) {
						}
					});

		customerForm = UIUtils.form(customerConstants.customer());

		customerForm.setFields(custNameText, custNoText);
		customerForm.setWidth("100%");
		customerForm.getCellFormatter().setWidth(0, 0, "205");

		// Element ele = DOM.createSpan();
		// ele.addClassName("star");
		// DOM.appendChild(DOM.getChild(customerForm.getElement(), 0), ele);

		accInfoForm = new DynamicForm();
		accInfoForm.setIsGroup(true);
		accInfoForm.setGroupTitle(customerConstants.accountInformation());

		statusCheck = new CheckboxItem(customerConstants.active());
		statusCheck.setValue(true);

		customerSinceDate = new DateField(customerConstants.customerSince());
		customerSinceDate.setHelpInformation(true);
		customerSinceDate.setEnteredDate(new ClientFinanceDate());

		balanceText = new AmountField(customerConstants.balance());
		balanceText.setHelpInformation(true);
		balanceDate = new DateField(customerConstants.balanceAsOf());
		balanceDate.setHelpInformation(true);
		ClientFinanceDate todaydate = new ClientFinanceDate();
		todaydate.setDay(todaydate.getDay());
		balanceDate.setDatethanFireEvent(todaydate);
		balanceDate.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate date) {
				if (takenCustomer == null) {
					ClientFinanceDate custSinceDate = customerSinceDate
							.getDate();
					if (date.before(custSinceDate)) {
						String msg = customerConstants.msg();
						// Accounter.showError(msg);
					}
				}

			}

		});
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
		AccounterButton addButton = new AccounterButton(Accounter.constants()
				.add());
		addButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ClientContact clientContact = new ClientContact();
				gridView.setDisabled(false);
				gridView.addData(clientContact);
			}
		});

		gridView = new ContactGrid();
		gridView.setDisabled(true);
		gridView.setCanEdit(true);
		gridView.setEditEventType(ListGrid.EDIT_EVENT_CLICK);
		gridView.isEnable = false;
		gridView.init();

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
		hPanel.getElement().getStyle().setFloat(Float.RIGHT);
		panel.add(hPanel);
		addButton.setType(AccounterButton.ADD_BUTTON);
		addButton.enabledAddButton();
		memoArea = new TextAreaItem();
		memoArea.setWidth("400px");
		memoArea.setTitle(customerConstants.memo());

		// AccounterButton addLinksButt = new AccounterButton("AddLinks");
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
		if (takenCustomer != null) {
			// Setting Customer Name
			custNameText.setValue(takenCustomer.getName());
			// Setting customer number
			custNoText.setValue(takenCustomer.getNumber());
			// Setting File as
			fileAsText.setValue(takenCustomer.getFileAs());
			// Setting AddressForm
			addrsForm = new AddressForm(takenCustomer.getAddress());
			addrsForm.setWidth("100%");
			// Setting Phone Fax Form
			fonFaxForm = new PhoneFaxForm(null, null);
			fonFaxForm.businessPhoneText.setValue(takenCustomer.getPhoneNo());
			fonFaxForm.businessFaxText.setValue(takenCustomer.getFaxNo());
			fonFaxForm.setWidth("100%");
			// Setting Email Form
			emailForm = new EmailForm(null, takenCustomer.getWebPageAddress());
			emailForm.businesEmailText.setValue(takenCustomer.getEmail());
			emailForm.setWidth("100%");
			// Setting Status Check
			statusCheck.setValue(takenCustomer.isActive());

			// Setting Customer Since
			customerSinceDate.setEnteredDate(new ClientFinanceDate(
					takenCustomer.getPayeeSince()));

			// Setting Balance
			if (!DecimalUtil.isEquals(takenCustomer.getBalance(), 0)) {
				balanceText.setAmount(takenCustomer.getBalance());
				balanceText.setDisabled(true);
			}

			if (!takenCustomer.isOpeningBalanceEditable())
				balanceText.setDisabled(true);

			// Setting Balance as of
			balanceDate.setEnteredDate(new ClientFinanceDate(takenCustomer
					.getBalanceAsOf()));
			balanceDate.setDisabled(true);
			// Setting Contacts
			gridView.initContacts(takenCustomer.getContacts());
			// gridView.setHeight("88px");

			// Setting Memo
			memoArea.setValue(takenCustomer.getMemo().toString());

		} else { // For Creating customer
			addrsForm = new AddressForm(null);
			addrsForm.setWidth("100%");
			fonFaxForm = new PhoneFaxForm(null, null);
			fonFaxForm.setWidth("100%");
			emailForm = new EmailForm(null, null);
			emailForm.setWidth("100%");
		}

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

		creditLimitText = new AmountField(customerConstants.creditLimit());
		creditLimitText.setHelpInformation(true);
		creditLimitText.setWidth(100);

		priceLevelSelect = new PriceLevelCombo(customerConstants.priceLevel());
		priceLevelSelect.setHelpInformation(true);
		priceLevelSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientPriceLevel>() {

					public void selectedComboBoxItem(ClientPriceLevel selectItem) {
						selectPriceLevelFromDetailsTab = selectItem;

					}

				});

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
		bankAccountSelect = new TextItem(customerConstants.bankAccountNo());
		bankAccountSelect.setHelpInformation(true);
		bankNameSelect = new TextItem(customerConstants.bankName());
		bankNameSelect.setHelpInformation(true);
		bankBranchSelect = new TextItem(customerConstants.bankBranch());
		bankBranchSelect.setHelpInformation(true);

		DynamicForm financeDitailsForm = UIUtils.form(customerConstants
				.financialDetails());

		financeDitailsForm.setFields(salesPersonSelect, priceLevelSelect,
				creditRatingSelect, bankNameSelect, bankAccountSelect,
				bankBranchSelect);
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

		custGroupSelect = new CustomerGroupCombo(
				customerConstants.customerGroup());
		custGroupSelect.setHelpInformation(true);
		custGroupSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientCustomerGroup>() {

					public void selectedComboBoxItem(
							ClientCustomerGroup selectItem) {
						selectCustomerGroupFromDetailsTab = selectItem;

					}

				});
		taxGroupSelect = new TaxGroupCombo(customerConstants.taxGroup());
		taxGroupSelect.setHelpInformation(true);
		taxGroupSelect.setRequired(true);
		taxGroupSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXItemGroup>() {

					public void selectedComboBoxItem(
							ClientTAXItemGroup selectItem) {
						selectTaxGroupFromDetailsTab = selectItem;
					}

				});

		vatregno = new TextItem(Accounter.constants().VATRegistrationNumber());
		vatregno.setHelpInformation(true);
		vatregno.setWidth(100);
		custTaxCode = new TAXCodeCombo(Accounter.constants().customerVATCode(),
				true);
		custTaxCode.setHelpInformation(true);
		custTaxCode
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXCode>() {

					public void selectedComboBoxItem(ClientTAXCode selectItem) {
						selectVatCodeFromDetailsTab = selectItem;

					}

				});

		DynamicForm termsForm = UIUtils.form(customerConstants.terms());

		int accounttype = getCompany().getAccountingType();

		if (accounttype == 1) {
			termsForm.setFields(payMethSelect, payTermsSelect, custGroupSelect,
					vatregno, custTaxCode);
			if (ClientCompanyPreferences.get().isDoProductShipMents()) {
				termsForm.setFields(shipMethSelect);
			}
		} else if (accounttype == 0) {
			custTaxCode.setTitle(customerConstants.taxGroup());
			// custTaxCode.setRequired(true);
			termsForm.setFields(payMethSelect, payTermsSelect, custGroupSelect,
					custTaxCode);
			if (ClientCompanyPreferences.get().isDoProductShipMents()) {
				termsForm.setFields(shipMethSelect);
			}
		}
		termsForm.setWidth("100%");

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setSize("100%", "100%");
		leftVLay.setHeight("350px");
		leftVLay.getElement().getStyle()
				.setBorderColor("none repeat scroll 0 0 #eee !important");
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

		if (takenCustomer != null) {
			// Setting salesPerson
			selectSalesPersonFromDetailsTab = getCompany().getSalesPerson(
					takenCustomer.getSalesPerson());

			bankAccountSelect.setValue(takenCustomer.getBankAccountNo());
			bankNameSelect.setValue(takenCustomer.getBankName());
			bankBranchSelect.setValue(takenCustomer.getBankBranch());

			// Setting Credit Limit Text
			if (!DecimalUtil.isEquals(takenCustomer.getCreditLimit(), 0))
				creditLimitText.setAmount(takenCustomer.getCreditLimit());

			// Setting price level
			selectPriceLevelFromDetailsTab = getCompany().getPriceLevel(
					takenCustomer.getPriceLevel());
			// Setting Credit Rating
			selectCreditRatingFromDetailsTab = getCompany().getCreditRating(
					takenCustomer.getCreditRating());
			// Setting Shipping Method
			selectShippingMethodFromDetailsTab = Accounter.getCompany()
					.getShippingMethod(takenCustomer.getShippingMethod());
			// Setting Payment Method
			// selectPaymentMethodFromDetialsTab = takenCustomer
			// .getPaymentMethod();
			payMethSelect.setComboItem(takenCustomer.getPaymentMethod());
			// Setting payemnt term
			selectPayTermFromDetailsTab = getCompany().getPaymentTerms(
					takenCustomer.getPaymentTerm());
			// Setting Customer Group
			selectCustomerGroupFromDetailsTab = getCompany().getCustomerGroup(
					takenCustomer.getCustomerGroup());
			// Setting Tax Group
			if (company.getAccountingType() == 0)
				selectTaxGroupFromDetailsTab = getCompany().getTAXItemGroup(
						takenCustomer.getTaxItemGroups());
			else {
				// settting vatcode

				selectVatCodeFromDetailsTab = getCompany().getTAXCode(
						takenCustomer.getTAXCode());
				// setting vatRegistrationNumber
				vatregno.setValue(takenCustomer.getVATRegistrationNumber());
			}
		}

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
	protected void initConstants() {
		super.initConstants();
		customerConstants = Accounter.constants();
		actionsConstants = Accounter.constants();
	}

	@Override
	public void init() {
		super.init();
		createControls();
		setSize("100%", "100%");
	}

	@Override
	public void initData() {
		if (takenCustomer == null)
			initFiscalYear();
		initTaxAgenciesList();
		initSalesPersonList();
		initCustomerGroupList();
		initPaymentTermsList();
		initShippingMethodList();
		initCreditRatingList();
		initPriceLevelList();
		if (takenCustomer != null && takenCustomer.getPhoneNo() != null)
			takenCustomer.setPhoneNo(takenCustomer.getPhoneNo());
		if (takenCustomer != null && takenCustomer.getFaxNo() != null)
			takenCustomer.setFaxNo(takenCustomer.getFaxNo());
		if (company.getAccountingType() == 1)
			initVatCodeList();
		else
			// initTaxItemGroupList();
			initVatCodeList();
		super.initData();

	}

	@Override
	public void setData(ClientCustomer data) {
		super.setData(data);
		if (data != null)
			takenCustomer = (ClientCustomer) data;
		else
			takenCustomer = null;
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

			if (core.getObjectType() == AccounterCoreType.PRICE_LEVEL)
				this.priceLevelSelect.addComboItem((ClientPriceLevel) core);

			if (core.getObjectType() == AccounterCoreType.CREDIT_RATING)
				this.creditRatingSelect.addComboItem((ClientCreditRating) core);

			if (core.getObjectType() == AccounterCoreType.SHIPPING_METHOD)
				this.shipMethSelect.addComboItem((ClientShippingMethod) core);

			if (core.getObjectType() == AccounterCoreType.PAYMENT_TERM)
				this.payTermsSelect.addComboItem((ClientPaymentTerms) core);

			if (core.getObjectType() == AccounterCoreType.CUSTOMER_GROUP)
				this.custGroupSelect.addComboItem((ClientCustomerGroup) core);

			// if (core.getObjectType() == AccounterCoreType.TAX_GROUP)
			// this.taxGroupSelect.addComboItem((ClientTaxGroup) core);

			if (core.getObjectType() == AccounterCoreType.TAX_ITEM_GROUP)
				this.taxGroupSelect.addComboItem((ClientTAXItemGroup) core);

			if (core.getObjectType() == AccounterCoreType.SALES_PERSON)
				this.salesPersonSelect.addComboItem((ClientSalesPerson) core);

			if (core.getObjectType() == AccounterCoreType.TAX_CODE)
				this.custTaxCode.addComboItem((ClientTAXCode) core);

			break;
		case AccounterCommand.UPDATION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.PRICE_LEVEL)
				this.priceLevelSelect.updateComboItem((ClientPriceLevel) core);

			if (core.getObjectType() == AccounterCoreType.CREDIT_RATING)
				this.creditRatingSelect
						.updateComboItem((ClientCreditRating) core);

			if (core.getObjectType() == AccounterCoreType.SHIPPING_METHOD)
				this.shipMethSelect
						.updateComboItem((ClientShippingMethod) core);

			if (core.getObjectType() == AccounterCoreType.PAYMENT_TERM)
				this.payTermsSelect.updateComboItem((ClientPaymentTerms) core);

			if (core.getObjectType() == AccounterCoreType.CUSTOMER_GROUP)
				this.custGroupSelect
						.updateComboItem((ClientCustomerGroup) core);

			// if (core.getObjectType() == AccounterCoreType.TAX_GROUP)
			// this.taxGroupSelect.updateComboItem((ClientTaxGroup) core);

			if (core.getObjectType() == AccounterCoreType.TAX_ITEM_GROUP)
				this.taxGroupSelect.updateComboItem((ClientTAXItemGroup) core);

			if (core.getObjectType() == AccounterCoreType.SALES_PERSON)
				this.salesPersonSelect
						.updateComboItem((ClientSalesPerson) core);

			if (core.getObjectType() == AccounterCoreType.TAX_CODE)
				this.custTaxCode.updateComboItem((ClientTAXCode) core);
			break;

		case AccounterCommand.DELETION_SUCCESS:
			if (core.getObjectType() == AccounterCoreType.PRICE_LEVEL)
				this.priceLevelSelect.removeComboItem((ClientPriceLevel) core);

			if (core.getObjectType() == AccounterCoreType.CREDIT_RATING)
				this.creditRatingSelect
						.removeComboItem((ClientCreditRating) core);

			if (core.getObjectType() == AccounterCoreType.SHIPPING_METHOD)
				this.shipMethSelect
						.removeComboItem((ClientShippingMethod) core);

			if (core.getObjectType() == AccounterCoreType.PAYMENT_TERM)
				this.payTermsSelect.removeComboItem((ClientPaymentTerms) core);

			if (core.getObjectType() == AccounterCoreType.CUSTOMER_GROUP)
				this.custGroupSelect
						.removeComboItem((ClientCustomerGroup) core);

			// if (core.getObjectType() == AccounterCoreType.TAX_GROUP)
			// this.taxGroupSelect.removeComboItem((ClientTaxGroup) core);

			if (core.getObjectType() == AccounterCoreType.TAX_ITEM_GROUP)
				this.taxGroupSelect.removeComboItem((ClientTAXItemGroup) core);

			if (core.getObjectType() == AccounterCoreType.SALES_PERSON)
				this.salesPersonSelect
						.removeComboItem((ClientSalesPerson) core);

			if (core.getObjectType() == AccounterCoreType.TAX_CODE)
				this.custTaxCode.removeComboItem((ClientTAXCode) core);

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
		// NOTHING TO DO
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().customer();
	}
}
