package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCreditCardCharge;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.banking.AbstractBankTransactionView;
import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
import com.vimukti.accounter.web.client.ui.combo.ContactCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PayFromAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.combo.VendorCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.edittable.tables.VendorTransactionTable;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class CreditCardChargeView extends
		AbstractBankTransactionView<ClientCreditCardCharge> {
	protected List<String> selectedComboList;
	protected DateField date, delivDate;;
	protected TextItem cheqNoText;
	// protected TextItem refText;
	AmountField totText;
	AccounterConstants accounterConstants = GWT
			.create(AccounterConstants.class);
	List<String> idPhoneNumberForContacts = new ArrayList<String>();
	List<String> idNamesForContacts = new ArrayList<String>();

	protected DynamicForm vendorForm, addrForm, phoneForm, termsForm, memoForm;
	protected SelectCombo payMethSelect;
	protected TextItem phoneSelect;

	VendorCombo vendorNameSelect;
	protected PayFromAccountsCombo payFrmSelect;
	private TextAreaItem addrArea;

	protected String selectPaymentMethod;

	// protected ClientVendor selectedVendor;

	private DynamicForm totForm;

	private HorizontalPanel botPanel, addLinkPanel;
	HorizontalPanel totPanel;

	private VerticalPanel leftVLay, botVLay;

	private ArrayList<DynamicForm> listforms;
	protected ClientContact contact;
	protected Label titlelabel;
	protected TextAreaItem billToAreaItem;
	private List<ClientAccount> listOfAccounts;
	private boolean locationTrackingEnabled;
	private VendorTransactionTable vendorTransactionTable;

	public CreditCardChargeView() {

		super(ClientTransaction.TYPE_CREDIT_CARD_CHARGE);

	}

	protected CreditCardChargeView(int type) {

		super(type);

	}

	// public CreditCardChargeView(CreditCardCharge creditCardChargeTaken) {
	// // this.creditCardChargeTaken = creditCardChargeTaken;
	//
	// // addpayFromAccountsList();
	// // addVendorsList();
	// // createControls();
	// super(creditCardChargeTaken);
	// }

	protected void addPhonesContactsAndAddress() {
		// Set<Address> allAddress = selectedVendor.getAddress();
		addressList = selectedVendor.getAddress();
		initBillToCombo();
		// billToCombo.setDisabled(isEdit);
		Set<ClientContact> allContacts;
		allContacts = selectedVendor.getContacts();
		Iterator<ClientContact> it = allContacts.iterator();
		// List<String> phones = new ArrayList<String>();
		ClientContact primaryContact = null;

		int i = 0;
		while (it.hasNext()) {
			ClientContact contact = it.next();
			if (contact.isPrimary())
				primaryContact = contact;
			idNamesForContacts.add(contact.getName());
			idPhoneNumberForContacts.add(contact.getBusinessPhone());
			// phones.add(contact.getBusinessPhone());
			i++;
		}

		// contactCombo.initCombo(idNamesForContacts);

		// phoneSelect.initCombo(phones);

		// ClientVendor cv = FinanceApplication.getCompany().getVendor(
		// creditCardChargeTaken.getVendor());
		if (transaction.getContact() != null)
			contactCombo.setSelected(transaction.getContact().getName());
		if (transaction.getPhone() != null)
			// FIXME check and fix the below code
			phoneSelect.setValue(transaction.getPhone());

		contactCombo.setDisabled(isInViewMode());
		phoneSelect.setDisabled(isInViewMode());
		return;
		// if (primaryContact == null) {
		// contactNameSelect.setSelected("");
		// phoneSelect.setValue("");
		// return;
		// }

		// contactNameSelect.setSelected(primaryContact.getName());
		// phoneSelect.setValue(primaryContact.getBusinessPhone());

		// for (Address toBeShown : allAddress) {
		// if (toBeShown.getType() == Address.TYPE_BILL_TO) {
		// billToAddress.put("1", toBeShown);
		// String toToSet = new String();
		// if (toBeShown.getStreet() != null) {
		// toToSet = toBeShown.getStreet().toString() + ",\n";
		// }
		//
		// if (toBeShown.getCity() != null) {
		// toToSet += toBeShown.getCity().toString() + ",\n";
		// }
		//
		// if (toBeShown.getStateOrProvinence() != null) {
		// toToSet += toBeShown.getStateOrProvinence() + ",\n";
		// }
		// if (toBeShown.getZipOrPostalCode() != null) {
		// toToSet += toBeShown.getZipOrPostalCode() + ",\n";
		// }
		// if (toBeShown.getCountryOrRegion() != null) {
		// toToSet += toBeShown.getCountryOrRegion();
		// }
		// addrArea.setValue(toToSet);
		// }
		// }

	}

	private void addVendorsList() {
		List<ClientVendor> result = getCompany().getActiveVendors();
		if (result != null) {
			initVendorsList(result);

		}
	}

	protected void initVendorsList(List<ClientVendor> result) {
		// First identify existing selected vendor
		for (ClientVendor vendor : result) {
			if (isInViewMode())
				if (vendor.getID() == transaction.getVendor()) {
					selectedVendor = vendor;
				}
		}
		vendorNameSelect.initCombo(result);
		vendorNameSelect.setDisabled(isInViewMode());
		if (isInViewMode()) {
			vendorNameSelect.setComboItem(selectedVendor);
			billToaddressSelected(selectedVendor.getSelectedAddress());
			addPhonesContactsAndAddress();
		}
	}

	public ClientContact getContactBasedOnId(String Id) {
		for (ClientContact cont : selectedVendor.getContacts()) {
			if (String.valueOf(cont.getID()).equalsIgnoreCase(Id))
				return cont;

		}

		return null;
	}

	@Override
	protected void initMemoAndReference() {
		if (isInViewMode()) {
			memoTextAreaItem.setDisabled(true);
			setMemoTextAreaItem(((ClientCreditCardCharge) transaction)
					.getMemo());
		}
		// refText.setValue(creditCardChargeTaken.getReference());

	}

	@Override
	protected void paymentMethodSelected(String paymentMethod2) {
		super.paymentMethodSelected(paymentMethod2);
		if (paymentMethod != null
				&& (paymentMethod
						.equals(com.vimukti.accounter.web.client.core.AccounterClientConstants.PAYMENT_METHOD_CHECK) || paymentMethod
						.equals(com.vimukti.accounter.web.client.core.AccounterClientConstants.PAYMENT_METHOD_CHECK_FOR_UK))) {
			if (isInViewMode()) {
				cheqNoText
						.setValue(transaction.getCheckNumber() != null ? transaction
								.getCheckNumber()
								: "");

			}
			cheqNoText.setDisabled(false);
		} else {
			cheqNoText.setValue("");
			cheqNoText.setDisabled(true);
		}
	}

	private void setDisableStaeForFormItems() {

		// for (FormItem formItem : formItems) {
		//
		// if (formItem != null)
		// formItem.setDisabled(isEdit);
		//
		// }

	}

	@Override
	protected void initTransactionViewData() {
		if (transaction == null) {
			setData(new ClientCreditCardCharge());
			resetElements();
			initpayFromAccountCombo();
		} else {
			transactionDateItem.setValue(transaction.getDate());
			contact = transaction.getContact();
			delivDate.setValue(new ClientFinanceDate(transaction
					.getDeliveryDate()));
			delivDate.setDisabled(isInViewMode());
			phoneSelect.setValue(transaction.getPhone());
			if (getCompany().getPreferences().isRegisteredForVAT()) {
				netAmount.setAmount(transaction.getNetAmount());
				vatTotalNonEditableText.setAmount(transaction.getTotal()
						- transaction.getNetAmount());
			}
			transactionTotalNonEditableText.setAmount(transaction.getTotal());

			if (vatinclusiveCheck != null) {
				setAmountIncludeChkValue(transaction.isAmountsIncludeVAT());
			}
			if (transaction.getPayFrom() != 0)
				payFromAccountSelected(transaction.getPayFrom());
			payFrmSelect.setComboItem(getCompany().getAccount(payFromAccount));
			payFrmSelect.setDisabled(isInViewMode());
			cheqNoText.setValue(transaction.getCheckNumber());
			cheqNoText.setDisabled(true);
			paymentMethodSelected(transaction.getPaymentMethod());
			payMethSelect.setComboItem(transaction.getPaymentMethod());
			vendorTransactionTable.removeAllRecords();
			vendorTransactionTable.setAllTransactionItems(transaction
					.getTransactionItems());

		}
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));
		initMemoAndReference();
		initTransactionNumber();
		addVendorsList();
	}

	private void initpayFromAccountCombo() {

		// listOfAccounts = Utility.getPayFromAccounts(FinanceApplication
		// .getCompany());
		// getPayFromAccounts();
		listOfAccounts = payFrmSelect.getAccounts();

		payFrmSelect.initCombo(listOfAccounts);
		payFrmSelect.setAccountTypes(UIUtils
				.getOptionsByType(AccountCombo.PAY_FROM_COMBO));
		payFrmSelect.setAccounts();
		payFrmSelect.setDisabled(isInViewMode());

		account = payFrmSelect.getSelectedValue();

		if (account != null)
			payFrmSelect.setComboItem(account);
	}

	private void resetElements() {
		selectedVendor = null;
		// transaction = null;
		billingAddress = null;
		addressList = null;
		// billToCombo.setDisabled(isEdit);
		paymentMethod = UIUtils.getpaymentMethodCheckBy_CompanyType(Accounter
				.constants().check());
		payFromAccount = 0;
		// phoneSelect.setValueMap("");
		setMemoTextAreaItem("");
		// refText.setValue("");
		cheqNoText.setValue("");

	}

	@Override
	protected void createControls() {
		locationTrackingEnabled = getCompany().getPreferences()
				.isLocationTrackingEnabled();

		titlelabel = new Label(Accounter.constants().creditCardCharge());
		titlelabel.removeStyleName("gwt-Label");
		titlelabel.addStyleName(Accounter.constants().labelTitle());
		// titlelabel.setHeight("35px");
		transactionDateItem = createTransactionDateItem();
		transactionNumber = createTransactionNumberItem();
		listforms = new ArrayList<DynamicForm>();
		locationCombo = createLocationCombo();
		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(6);
		dateNoForm.setStyleName("datenumber-panel");
		dateNoForm.setFields(transactionDateItem, transactionNumber);
		if (locationTrackingEnabled)
			dateNoForm.setFields(locationCombo);

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();

		VerticalPanel regPanel = new VerticalPanel();
		regPanel.setCellHorizontalAlignment(dateNoForm, ALIGN_RIGHT);
		regPanel.add(dateNoForm);
		regPanel.getElement().getStyle().setPaddingRight(25, Unit.PX);

		labeldateNoLayout.setWidth("100%");
		// labeldateNoLayout.add(titlelabel);
		labeldateNoLayout.add(regPanel);
		labeldateNoLayout.setCellHorizontalAlignment(regPanel, ALIGN_RIGHT);

		vendorNameSelect = new VendorCombo(Global.get().messages().vendorName(
				Global.get().Vendor()));
		vendorNameSelect.setHelpInformation(true);
		vendorNameSelect.setWidth(100);
		vendorNameSelect.setRequired(true);
		vendorNameSelect.setDisabled(false);

		vendorNameSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {

					public void selectedComboBoxItem(ClientVendor selectItem) {
						selectedVendor = selectItem;
						if (selectedVendor.getPaymentMethod() != null) {
							paymentMethodSelected(selectedVendor
									.getPaymentMethod());
							payMethSelect.setSelected(paymentMethod);
						}
						contactCombo.setDisabled(false);
						addPhonesContactsAndAddress();
						initContacts(selectItem);
					}

				});

		contactCombo = new ContactCombo(Accounter.constants().contactName(),
				true);
		contactCombo.setHelpInformation(true);
		contactCombo.setDisabled(true);
		contactCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientContact>() {

					@Override
					public void selectedComboBoxItem(ClientContact selectItem) {
						contactSelected(selectItem);

					}
				});
		contactCombo.addNewContactHandler(new ValueCallBack<ClientContact>() {

			@Override
			public void execute(ClientContact value) {
				addContactToVendor(value);
			}
		});
		// contactNameSelect.setWidth(100);
		// formItems.add(contactNameSelect);
		// billToCombo = createBillToComboItem();
		billToAreaItem = new TextAreaItem(Accounter.constants().billTo());
		billToAreaItem.setWidth(100);
		billToAreaItem.setDisabled(true);
		// formItems.add(billToCombo);
		phoneSelect = new TextItem(Accounter.constants().phone());
		phoneSelect.setToolTip(Accounter.messages().phoneNumber(
				Global.get().vendor()));
		phoneSelect.setHelpInformation(true);
		phoneSelect.setWidth(100);
		// formItems.add(phoneSelect);

		vendorForm = UIUtils.form(Accounter.constants().vendor());
		vendorForm.setWidth("100%");
		vendorForm.setFields(vendorNameSelect, contactCombo, phoneSelect,
				billToAreaItem);
		vendorForm.getCellFormatter().addStyleName(3, 0, "memoFormAlign");
		vendorForm.getCellFormatter().setWidth(0, 0, "180px");

		payMethSelect = createPaymentMethodSelectItem();
		payMethSelect.setTitle(Accounter.constants().paymentMethod());
		payMethSelect.setWidth(90);
		List<String> paymentMthds = new ArrayList<String>();
		paymentMthds.add(Accounter.constants().creditCard());
		payMethSelect.initCombo(paymentMthds);
		payMethSelect.setDefaultToFirstOption(true);
		payMethSelect.setDisabled(true);
		// payMethSelect.setComboItem(UIUtils
		// .getpaymentMethodCheckBy_CompanyType(Accounter.constants()
		// .check()));

		payFrmSelect = createPayFromselectItem();
		payFrmSelect.setWidth(90);
		payFrmSelect.setPopupWidth("510px");
		payFrmSelect.setTitle(Accounter.constants().payFrom());
		payFromAccount = 0;
		payFrmSelect.setColSpan(0);
		// formItems.add(payFrmSelect);

		cheqNoText = new TextItem(
				getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK ? Accounter
						.constants().chequeNo()
						: Accounter.constants().checkNo());
		cheqNoText.setHelpInformation(true);
		cheqNoText.setDisabled(isInViewMode());
		cheqNoText.setWidth(100);
		// formItems.add(cheqNoText);

		delivDate = new DateField(Accounter.constants().deliveryDate());
		delivDate.setHelpInformation(true);
		delivDate.setColSpan(1);
		delivDate.setValue(new ClientFinanceDate());
		// formItems.add(delivDate);

		termsForm = UIUtils.form(Accounter.constants().terms());
		termsForm.setWidth("100%");
		termsForm.setFields(payMethSelect, payFrmSelect, delivDate);

		if (getPreferences().isClassTrackingEnabled()
				&& getPreferences().isClassOnePerTransaction()) {
			classListCombo = createAccounterClassListCombo();
			termsForm.setFields(classListCombo);
		}

		termsForm.getCellFormatter().getElement(0, 0).setAttribute(
				Accounter.constants().width(), "203px");

		Label lab2 = new Label(Accounter.constants().itemsAndExpenses());

		netAmount = new AmountLabel(Accounter.constants().netAmount());
		netAmount.setDefaultValue(Accounter.constants().atozero());
		netAmount.setDisabled(true);

		transactionTotalNonEditableText = createTransactionTotalNonEditableLabel();

		vatTotalNonEditableText = createVATTotalNonEditableLabel();

		vatinclusiveCheck = new CheckboxItem(Accounter.constants()
				.amountIncludesVat());
		vatinclusiveCheck = getVATInclusiveCheckBox();

		vendorTransactionTable = new VendorTransactionTable() {

			@Override
			protected void updateNonEditableItems() {
				CreditCardChargeView.this.updateNonEditableItems();
			}

			@Override
			protected ClientTransaction getTransactionObject() {
				return CreditCardChargeView.this.getTransactionObject();
			}

			@Override
			protected ClientVendor getSelectedVendor() {
				return CreditCardChargeView.this.getSelectedVendor();
			}
			
			@Override
			public boolean isShowPriceWithVat() {
				return CreditCardChargeView.this.isShowPriceWithVat();
			}
		};
		vendorTransactionTable.setDisabled(isInViewMode());

		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setWidth(100);
		memoTextAreaItem.setDisabled(false);

		// refText = new TextItem(Accounter.constants().reference());
		//
		// refText.setWidth(100);
		// refText.setDisabled(false);

		DynamicForm memoForm = new DynamicForm();
		memoForm.setWidth("100%");
		memoForm.setFields(memoTextAreaItem);
		memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");

		DynamicForm vatCheckform = new DynamicForm();
		// vatCheckform.setFields(vatinclusiveCheck);

		DynamicForm totalForm = new DynamicForm();
		totalForm.setNumCols(2);
		totalForm.setWidth("100%");
		totalForm.setStyleName("invoice-total");
		// totText = new AmountField(FinanceApplication.constants()
		// .total());
		// totText.setWidth(100);

		totForm = new DynamicForm();
		totForm.setWidth("100%");
		totForm.addStyleName("unused-payments");
		totForm.getElement().getStyle().setMarginTop(10, Unit.PX);

		botPanel = new HorizontalPanel();
		botPanel.setWidth("100%");

		VerticalPanel bottompanel = new VerticalPanel();
		bottompanel.setWidth("100%");

		if (getCompany().getPreferences().isRegisteredForVAT()) {
			totalForm.setFields(netAmount, vatTotalNonEditableText,
					transactionTotalNonEditableText);
			VerticalPanel vPanel = new VerticalPanel();
			vPanel.setHorizontalAlignment(ALIGN_RIGHT);
			vPanel.setWidth("100%");
			vPanel.add(totalForm);

			botPanel.add(memoForm);
			botPanel.add(totalForm);
			botPanel.setCellWidth(totalForm, "30%");

			bottompanel.add(vPanel);
			bottompanel.add(botPanel);

			// totalForm.setFields(netAmount, vatTotalNonEditableText,
			// transactionTotalNonEditableText);
			// // botPanel.add(memoForm);
			// botPanel.add(vPanel);
			// botPanel.add(vatCheckform);
			// botPanel.setCellHorizontalAlignment(vatCheckform,
			// HasHorizontalAlignment.ALIGN_RIGHT);
			// botPanel.add(totalForm);
			// botPanel.setCellHorizontalAlignment(totalForm,
			// HasHorizontalAlignment.ALIGN_RIGHT);
		} else {
			totForm.setFields(transactionTotalNonEditableText);

			HorizontalPanel hPanel = new HorizontalPanel();
			hPanel.setWidth("100%");
			hPanel.add(memoForm);
			hPanel.setCellHorizontalAlignment(memoForm, ALIGN_LEFT);
			hPanel.add(totForm);
			hPanel.setCellHorizontalAlignment(totForm, ALIGN_RIGHT);

			VerticalPanel vpanel = new VerticalPanel();
			vpanel.setWidth("100%");
			vpanel.add(hPanel);

			bottompanel.add(vpanel);
		}

		leftVLay = new VerticalPanel();
		// leftVLay.setWidth("80%");
		leftVLay.add(vendorForm);

		HorizontalPanel rightHLay = new HorizontalPanel();
		// rightHLay.setWidth("80%");
		rightHLay.setCellHorizontalAlignment(termsForm, ALIGN_RIGHT);
		rightHLay.add(termsForm);

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.setWidth("100%");
		topHLay.add(leftVLay);
		topHLay.setSpacing(20);
		topHLay.setCellHorizontalAlignment(rightHLay, ALIGN_RIGHT);
		topHLay.add(rightHLay);
		topHLay.setCellWidth(leftVLay, "50%");
		topHLay.setCellWidth(rightHLay, "42%");

		VerticalPanel vLay1 = new VerticalPanel();
		// vLay1.add(lab2);
		// vLay1.add(addButton);
		// multi currency combo
		vLay1.add(vendorTransactionTable);
		vLay1.add(createAddNewButton());
		menuButton.getElement().getStyle().setMargin(5, Unit.PX);
		vLay1.setWidth("100%");
		vLay1.add(bottompanel);

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(titlelabel);
		mainVLay.add(labeldateNoLayout);
		mainVLay.add(topHLay);
		mainVLay.add(vLay1);

		this.add(mainVLay);

		setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(vendorForm);
		listforms.add(termsForm);
		listforms.add(memoForm);
		listforms.add(vatCheckform);
		listforms.add(totalForm);
		listforms.add(totForm);
		initViewType();

		if (UIUtils.isMSIEBrowser())
			resetFormView();

		if (isInViewMode()) {
			payFrmSelect.setComboItem(getCompany().getAccount(
					transaction.getPayFrom()));
		}
		settabIndexes();
	}

	// protected void payFromMethodSelected(Account account2) {
	// this.account = account2;
	//
	// }

	public void saveAndUpdateView() {

		updateTransaction();

		if (getCompany().getPreferences().isRegisteredForVAT())
			transaction.setNetAmount(netAmount.getAmount());
		// creditCardCharge.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
		// .getValue());

		super.saveAndUpdateView();

		createAlterObject();
	}

	protected void updateTransaction() {
		super.updateTransaction();
		// Setting Type
		transaction.setType(ClientTransaction.TYPE_CREDIT_CARD_CHARGE);

		// setting date
		if (transactionDateItem != null)
			transaction.setDate(transactionDateItem.getValue().getDate());
		// setting number
		if (transactionNumber != null)
			transaction.setNumber(transactionNumber.getValue().toString());

		if (selectedVendor != null) {
			// setting vendor
			transaction.setVendor(selectedVendor.getID());

			// setting contact
			if (contact != null) {
				transaction.setContact(contact);
			}
			// if (contactNameSelect.getValue() != null) {
			// // ClientContact contact = getContactBasedOnId(contactNameSelect
			// // .getValue().toString());
			// transaction
			// .setContact(getContactBasedOnId(contactNameSelect
			// .getValue().toString()));
			//
			// }
		}

		// Setting Address
		if (billingAddress != null)
			transaction.setVendorAddress(billingAddress);

		// setting phone
		if (phoneSelect.getValue() != null)
			transaction.setPhone(phoneSelect.getValue().toString());

		// Setting payment method

		transaction.setPaymentMethod(paymentMethod);

		// Setting pay from
		if (payFrmSelect.getSelectedValue() != null)
			payFromAccount = payFrmSelect.getSelectedValue().getID();
		if (payFromAccount != 0) {

			transaction.setPayFrom(getCompany().getAccount(payFromAccount)
					.getID());
		}

		// setting check no
		if (cheqNoText.getValue() != null)
			transaction.setCheckNumber(cheqNoText.getValue().toString());

		if (vatinclusiveCheck != null) {
			transaction.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
					.getValue());
		}

		// setting delivery date
		transaction.setDeliveryDate(UIUtils.toDate(delivDate.getValue()));

		// Setting transactions
		transaction.setTransactionItems(vendorTransactionTable.getAllRows());

		// setting total
		transaction.setTotal(vendorTransactionTable.getTotal());
		// setting memo
		transaction.setMemo(getMemoTextAreaItem());
		// setting ref
		// creditCardCharge.setReference(UIUtils.toStr(refText.getValue()));
	}

	public void createAlterObject() {
		saveOrUpdate((ClientCreditCardCharge) transaction);

	}

	@Override
	public void updateNonEditableItems() {

		if (getCompany().getPreferences().isRegisteredForVAT()) {
			transactionTotalNonEditableText.setAmount(vendorTransactionTable
					.getTotal());
			netAmount.setAmount(vendorTransactionTable.getGrandTotal());
			vatTotalNonEditableText.setAmount(vendorTransactionTable.getTotal()
					- vendorTransactionTable.getGrandTotal());
		} else {
			transactionTotalNonEditableText.setAmount(vendorTransactionTable
					.getTotal());
		}
	}

	@Override
	public ValidationResult validate() {
		try {
			ValidationResult result = super.validate();
			// Validations
			// 1. if(! validTransactionDate(transactionDate)) ERROR
			// if transaction date is with in the open fiscal year range, then
			// it is
			// valid transaction date

			// 2. if( isInPreventPostingBeforeDate(transactionDate)) ERROR
			// i.e the transaction date should not be before the company's
			// preferred
			// preventPostingBeforeDate

			// 3. vendorForm validation
			// 4. termsForm validation
			// 5. if(isBlankTransation(vendorTransactionGrid)) ERROR
			// 6. vendorTransactionGrid validation

			if (!AccounterValidator.isValidTransactionDate(transactionDate)) {
				result.addError(transactionDate, accounterConstants
						.invalidateTransactionDate());
			}

			if (AccounterValidator
					.isInPreventPostingBeforeDate(transactionDate)) {
				result.addError(transactionDate, accounterConstants
						.invalidateDate());
			}

			result.add(vendorForm.validate());
			result.add(termsForm.validate());
			if (vendorTransactionTable.getAllRows().isEmpty()) {
				result.addError(vendorTransactionTable, accounterConstants
						.blankTransaction());
			} else
				result.add(vendorTransactionTable.validateGrid());
			return result;
		} catch (Exception e) {
			System.err.println(e);
		}
		return null;

	}

	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.vendorNameSelect.setFocus();
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

	public void onEdit() {
		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				int errorCode = caught.getErrorCode();
				Accounter.showError(AccounterExceptions
						.getErrorString(errorCode));
			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		AccounterCoreType type = UIUtils.getAccounterCoreType(transaction
				.getType());
		if (type != null) {
			this.rpcDoSerivce.canEdit(type, transaction.id, editCallBack);
		}

	}

	protected void enableFormItems() {
		setMode(EditMode.EDIT);
		transactionDateItem.setDisabled(isInViewMode());
		transactionNumber.setDisabled(isInViewMode());
		// payMethSelect.setDisabled(isEdit);
		if (paymentMethod.equals(Accounter.constants().check())
				|| paymentMethod.equals(Accounter.constants().cheque())) {
			cheqNoText.setDisabled(isInViewMode());
		} else {
			cheqNoText.setDisabled(!isInViewMode());
		}
		delivDate.setDisabled(isInViewMode());
		// billToCombo.setDisabled(isEdit);
		vendorNameSelect.setDisabled(isInViewMode());
		contactCombo.setDisabled(isInViewMode());
		phoneSelect.setDisabled(isInViewMode());
		payFrmSelect.setDisabled(isInViewMode());
		memoTextAreaItem.setDisabled(isInViewMode());
		vendorTransactionTable.setDisabled(isInViewMode());
		if (locationTrackingEnabled)
			locationCombo.setDisabled(isInViewMode());
		super.onEdit();

	}

	protected void initViewType() {

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// its not using any where

	}

	@Override
	protected void initTransactionTotalNonEditableItem() {
		// its not using any where

	}

	private void resetFormView() {
		vendorForm.getCellFormatter().setWidth(0, 1, "200px");
		// refText.setWidth("200px");
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().creditCardCharge();
	}

	public ClientVendor getSelectedVendor() {
		return selectedVendor;
	}

	/**
	 * @param value
	 */
	protected void addContactToVendor(final ClientContact contact) {
		ClientVendor selectedVendor = vendorNameSelect.getSelectedValue();
		if (selectedVendor == null) {
			return;
		}
		selectedVendor.addContact(contact);
		AccounterAsyncCallback<Long> asyncallBack = new AccounterAsyncCallback<Long>() {

			public void onException(AccounterException caught) {
				caught.printStackTrace();
			}

			public void onResultSuccess(Long result) {
				contactSelected(contact);
			}

		};
		Accounter.createCRUDService().update(selectedVendor, asyncallBack);
	}

	@Override
	protected void addNewData(ClientTransactionItem transactionItem) {
		vendorTransactionTable.add(transactionItem);

	}

	@Override
	protected void refreshTransactionGrid() {

	}

	private void settabIndexes() {
		vendorNameSelect.setTabIndex(1);
		contactCombo.setTabIndex(2);
		phoneSelect.setTabIndex(3);
		billToAreaItem.setTabIndex(4);
		transactionDateItem.setTabIndex(5);
		transactionNumber.setTabIndex(6);
		payMethSelect.setTabIndex(7);
		payFrmSelect.setTabIndex(8);
		delivDate.setTabIndex(9);
		memoTextAreaItem.setTabIndex(10);
		menuButton.setTabIndex(11);
		saveAndCloseButton.setTabIndex(12);
		saveAndNewButton.setTabIndex(13);
		cancelButton.setTabIndex(14);

	}
}
