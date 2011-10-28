package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AddNewButton;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCreditCardCharge;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
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
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.combo.VendorCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.edittable.tables.VendorAccountTransactionTable;
import com.vimukti.accounter.web.client.ui.edittable.tables.VendorItemTransactionTable;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.widgets.CurrencyWidget;

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
	protected Label titlelabel;
	protected TextAreaItem billToAreaItem;
	private List<ClientAccount> listOfAccounts;
	private boolean locationTrackingEnabled;
	private VendorAccountTransactionTable vendorAccountTransactionTable;
	private VendorItemTransactionTable vendorItemTransactionTable;
	private AddNewButton accountTableButton, itemTableButton;
	private TAXCodeCombo taxCodeSelect;
	private DisclosurePanel accountsDisclosurePanel, itemsDisclosurePanel;
	private CurrencyWidget currencyWidget;

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
		Set<ClientAddress> allAddress = new HashSet<ClientAddress>();
		if (selectedVendor != null) {
			allAddress = getSelectedVendor().getAddress();
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

			contactCombo.initCombo(new ArrayList<ClientContact>(allContacts));

			contactCombo.setComboItem(primaryContact);
		}
		if (transaction.getContact() != null)
			contactCombo.setSelected(transaction.getContact().getName());
		if (transaction.getPhone() != null)
			// FIXME check and fix the below code
			phoneSelect.setValue(transaction.getPhone());

		contactCombo.setDisabled(isInViewMode());
		phoneSelect.setDisabled(isInViewMode());
		billToAreaItem.setValue(null);
		for (ClientAddress toBeShown : allAddress) {
			if (toBeShown.getType() == ClientAddress.TYPE_BILL_TO) {
				billToAreaItem.setValue(getValidAddress(toBeShown));
			}
		}
		return;
		// if (primaryContact == null) {
		// contactNameSelect.setSelected("");
		// phoneSelect.setValue("");
		// return;
		// }

		// contactNameSelect.setSelected(primaryContact.getName());
		// phoneSelect.setValue(primaryContact.getBusinessPhone());

	}

	private String getValidAddress(ClientAddress address) {
		String toToSet = new String();
		if (address.getAddress1() != null && !address.getAddress1().isEmpty()) {
			toToSet = address.getAddress1().toString() + "\n";
		}

		if (address.getStreet() != null && !address.getStreet().isEmpty()) {
			toToSet += address.getStreet().toString() + "\n";
		}

		if (address.getCity() != null && !address.getCity().isEmpty()) {
			toToSet += address.getCity().toString() + "\n";
		}

		if (address.getStateOrProvinence() != null
				&& !address.getStateOrProvinence().isEmpty()) {
			toToSet += address.getStateOrProvinence() + "\n";
		}
		if (address.getZipOrPostalCode() != null
				&& !address.getZipOrPostalCode().isEmpty()) {
			toToSet += address.getZipOrPostalCode() + "\n";
		}
		if (address.getCountryOrRegion() != null
				&& !address.getCountryOrRegion().isEmpty()) {
			toToSet += address.getCountryOrRegion();
		}
		return toToSet;
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
			if (selectedVendor != null) {
				vendorNameSelect.setComboItem(selectedVendor);
				billToaddressSelected(selectedVendor.getSelectedAddress());
			}
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
								.getCheckNumber() : "");

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

			if (currencyWidget != null) {
				this.currency = getCompany().getCurrency(
						transaction.getCurrency());
				this.currencyFactor = transaction.getCurrencyFactor();
				currencyWidget.setSelectedCurrency(this.currency);
				// currencyWidget.currencyChanged(this.currency);
				currencyWidget.setCurrencyFactor(transaction
						.getCurrencyFactor());
			}
			transactionDateItem.setValue(transaction.getDate());
			contact = transaction.getContact();
			delivDate.setValue(new ClientFinanceDate(transaction
					.getDeliveryDate()));
			delivDate.setDisabled(isInViewMode());
			phoneSelect.setValue(transaction.getPhone());
			if (getPreferences().isTrackPaidTax()) {
				if (getPreferences().isTaxPerDetailLine()) {
					netAmount
							.setAmount(getAmountInTransactionCurrency(transaction
									.getNetAmount()));
					vatTotalNonEditableText
							.setAmount(getAmountInTransactionCurrency(transaction
									.getTotal() - transaction.getNetAmount()));
				} else {
					this.taxCode = getTaxCodeForTransactionItems(transaction
							.getTransactionItems());
					if (taxCode != null) {
						this.taxCodeSelect.setComboItem(taxCode);
					}
				}
			}
			transactionTotalNonEditableText
					.setAmount(getAmountInTransactionCurrency(transaction
							.getTotal()));

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
			vendorAccountTransactionTable
					.setRecords(getAccountTransactionItems(transaction
							.getTransactionItems()));
			vendorItemTransactionTable
					.setRecords(getItemTransactionItems(transaction
							.getTransactionItems()));

		}
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));
		initMemoAndReference();
		initTransactionNumber();
		addVendorsList();
		initAccounterClass();
		accountsDisclosurePanel.setOpen(checkOpen(
				transaction.getTransactionItems(),
				ClientTransactionItem.TYPE_ACCOUNT, true));
		itemsDisclosurePanel.setOpen(checkOpen(
				transaction.getTransactionItems(),
				ClientTransactionItem.TYPE_ITEM, false));
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

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();

		VerticalPanel regPanel = new VerticalPanel();
		regPanel.setCellHorizontalAlignment(dateNoForm, ALIGN_RIGHT);
		regPanel.add(dateNoForm);
		regPanel.getElement().getStyle().setPaddingRight(25, Unit.PX);

		labeldateNoLayout.setWidth("100%");
		// labeldateNoLayout.add(titlelabel);
		labeldateNoLayout.add(regPanel);
		labeldateNoLayout.setCellHorizontalAlignment(regPanel, ALIGN_RIGHT);
		if (!isTaxPerDetailLine())
			taxCodeSelect = createTaxCodeSelectItem();
		vendorNameSelect = new VendorCombo(Global.get().messages()
				.vendorName(Global.get().Vendor()));
		vendorNameSelect.setHelpInformation(true);
		vendorNameSelect.setWidth(100);
		// vendorNameSelect.setRequired(true);
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
						long code = selectedVendor.getTAXCode();
						if (taxCodeSelect != null) {
							if (code == 0)
								code = Accounter.getCompany()
										.getDefaultTaxCode();
							taxCodeSelect.setComboItem(getCompany().getTAXCode(
									code));
							taxCodeSelected(getCompany().getTAXCode(code));
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

		payMethSelect = new SelectCombo(Accounter.constants().paymentMethod());
		payMethSelect.setRequired(true);
		List<String> paymentMthds = new ArrayList<String>();
		paymentMthds.add(Accounter.constants().creditCard());
		payMethSelect.initCombo(paymentMthds);
		payMethSelect.setDefaultToFirstOption(true);
		payMethSelect.setDisabled(true);
		// payMethSelect.setComboItem(UIUtils
		// .getpaymentMethodCheckBy_CompanyType(Accounter.constants()
		// .check()));

		payFrmSelect = createPayFromselectItem();
		payFrmSelect.setPopupWidth("510px");
		payFrmSelect.setTitle(Accounter.constants().payFrom());
		payFromAccount = 0;
		payFrmSelect.setColSpan(0);
		// formItems.add(payFrmSelect);

		cheqNoText = new TextItem(Accounter.constants().chequeNo());
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
		if (locationTrackingEnabled)
			termsForm.setFields(locationCombo);

		termsForm.setFields(payMethSelect, payFrmSelect, delivDate);

		if (getPreferences().isClassTrackingEnabled()
				&& getPreferences().isClassOnePerTransaction()) {
			classListCombo = createAccounterClassListCombo();
			termsForm.setFields(classListCombo);
		}

		termsForm.getCellFormatter().getElement(0, 0)
				.setAttribute(Accounter.constants().width(), "203px");

		Label lab2 = new Label(Accounter.constants().itemsAndExpenses());

		netAmount = new AmountLabel(Accounter.constants().netAmount());
		netAmount.setDefaultValue(Accounter.constants().atozero());
		netAmount.setDisabled(true);

		transactionTotalNonEditableText = createTransactionTotalNonEditableLabel();

		vatTotalNonEditableText = createVATTotalNonEditableLabel();

		vatinclusiveCheck = new CheckboxItem(Accounter.constants()
				.amountIncludesVat());
		vatinclusiveCheck = getVATInclusiveCheckBox();

		vendorAccountTransactionTable = new VendorAccountTransactionTable(
				isTrackTax() && isTrackPaidTax(), isTaxPerDetailLine(), this) {

			@Override
			protected void updateNonEditableItems() {
				CreditCardChargeView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return CreditCardChargeView.this.isShowPriceWithVat();
			}
		};

		vendorAccountTransactionTable.setDisabled(isInViewMode());

		accountTableButton = new AddNewButton();
		accountTableButton.setEnabled(!isInViewMode());
		accountTableButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addAccount();
			}
		});

		FlowPanel accountFlowPanel = new FlowPanel();
		accountsDisclosurePanel = new DisclosurePanel("Itemize by Account");
		accountFlowPanel.add(vendorAccountTransactionTable);
		accountFlowPanel.add(accountTableButton);
		accountsDisclosurePanel.setContent(accountFlowPanel);
		accountsDisclosurePanel.setOpen(true);
		accountsDisclosurePanel.setWidth("100%");

		vendorItemTransactionTable = new VendorItemTransactionTable(
				isTrackTax(), isTaxPerDetailLine(), this) {

			@Override
			protected void updateNonEditableItems() {
				CreditCardChargeView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return CreditCardChargeView.this.isShowPriceWithVat();
			}
		};

		vendorItemTransactionTable.setDisabled(isInViewMode());

		itemTableButton = new AddNewButton();
		itemTableButton.setEnabled(!isInViewMode());
		itemTableButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addItem();
			}
		});

		FlowPanel itemsFlowPanel = new FlowPanel();
		itemsDisclosurePanel = new DisclosurePanel("Itemize by Product/Service");
		itemsFlowPanel.add(vendorItemTransactionTable);
		itemsFlowPanel.add(itemTableButton);
		itemsDisclosurePanel.setContent(itemsFlowPanel);
		itemsDisclosurePanel.setWidth("100%");

		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setWidth(100);
		memoTextAreaItem.setDisabled(false);

		// refText = new TextItem(Accounter.constants().reference());
		//
		// refText.setWidth(100);
		// refText.setDisabled(false);
		currencyWidget = createCurrencyWidget();
		DynamicForm memoForm = new DynamicForm();
		memoForm.setWidth("100%");
		memoForm.setFields(memoTextAreaItem);
		memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");

		DynamicForm vatCheckform = new DynamicForm();
		// vatCheckform.setFields(vatinclusiveCheck);

		DynamicForm totalForm = new DynamicForm();
		totalForm.setNumCols(2);
		totalForm.setWidth("100%");
		totalForm.setStyleName("boldtext");
		// totText = new AmountField(FinanceApplication.constants()
		// .total());
		// totText.setWidth(100);

		totForm = new DynamicForm();
		totForm.setWidth("100%");
		totForm.addStyleName("boldtext");
		totForm.getElement().getStyle().setMarginTop(10, Unit.PX);

		botPanel = new HorizontalPanel();
		botPanel.setWidth("100%");

		VerticalPanel bottompanel = new VerticalPanel();
		bottompanel.setWidth("100%");

		if (isTrackTax()) {
			totalForm.setFields(netAmount, vatTotalNonEditableText,
					transactionTotalNonEditableText);
			VerticalPanel vPanel = new VerticalPanel();
			vPanel.setHorizontalAlignment(ALIGN_RIGHT);
			vPanel.setWidth("100%");
			vPanel.add(totalForm);
			botPanel.add(memoForm);
			if (!isTaxPerDetailLine()) {
				DynamicForm form = new DynamicForm();
				form.setFields(taxCodeSelect);
				botPanel.add(form);
			}
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

		VerticalPanel rightHLay = new VerticalPanel();
		// rightHLay.setWidth("80%");
		rightHLay.setCellHorizontalAlignment(termsForm, ALIGN_RIGHT);
		rightHLay.add(termsForm);
		if (isMultiCurrencyEnabled()) {
			rightHLay.add(currencyWidget);
			currencyWidget.setDisabled(isInViewMode());
		}

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
		vLay1.add(accountsDisclosurePanel);
		vLay1.add(itemsDisclosurePanel);
		// vLay1.add(createAddNewButton());
		// menuButton.getElement().getStyle().setMargin(5, Unit.PX);
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

		// if (UIUtils.isMSIEBrowser())
		// resetFormView();

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

		if (isTrackTax())
			transaction.setNetAmount(getAmountInBaseCurrency(netAmount
					.getAmount()));
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

		transaction.setPaymentMethod(payMethSelect.getSelectedValue());

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

		// setting total
		transaction.setTotal(vendorAccountTransactionTable.getGrandTotal()
				+ vendorItemTransactionTable.getGrandTotal());
		// setting memo
		transaction.setMemo(getMemoTextAreaItem());
		// setting ref
		// creditCardCharge.setReference(UIUtils.toStr(refText.getValue()));

		if (currency != null)
			transaction.setCurrency(currency.getID());
		transaction.setCurrencyFactor(currencyWidget.getCurrencyFactor());
	}

	public void createAlterObject() {
		saveOrUpdate((ClientCreditCardCharge) transaction);

	}

	@Override
	public void updateNonEditableItems() {
		if (vendorAccountTransactionTable == null
				|| vendorItemTransactionTable == null) {
			return;
		}
		double lineTotal = vendorAccountTransactionTable.getLineTotal()
				+ vendorItemTransactionTable.getLineTotal();
		double grandTotal = vendorAccountTransactionTable.getGrandTotal()
				+ vendorItemTransactionTable.getGrandTotal();

		if (isTrackTax()) {
			transactionTotalNonEditableText
					.setAmount(getAmountInTransactionCurrency(grandTotal));
			netAmount.setAmount(getAmountInTransactionCurrency(lineTotal));
			vatTotalNonEditableText
					.setAmount(getAmountInTransactionCurrency(grandTotal
							- lineTotal));
		} else {
			transactionTotalNonEditableText
					.setAmount(getAmountInTransactionCurrency(grandTotal));
		}
	}

	@Override
	public ValidationResult validate() {
		try {
			ValidationResult result = super.validate();
			if (AccounterValidator
					.isInPreventPostingBeforeDate(transactionDate)) {
				result.addError(transactionDate,
						accounterConstants.invalidateDate());
			}

			result.add(vendorForm.validate());
			result.add(termsForm.validate());
			if (getAllTransactionItems().isEmpty()) {
				result.addError(vendorAccountTransactionTable,
						accounterConstants.blankTransaction());
			} else {
				result.add(vendorAccountTransactionTable.validateGrid());
				result.add(vendorItemTransactionTable.validateGrid());
			}
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
		vendorAccountTransactionTable.setDisabled(isInViewMode());
		vendorItemTransactionTable.setDisabled(isInViewMode());
		accountTableButton.setEnabled(!isInViewMode());
		itemTableButton.setEnabled(!isInViewMode());
		if (locationTrackingEnabled)
			locationCombo.setDisabled(isInViewMode());
		taxCodeSelect.setDisabled(isInViewMode());
		if(currencyWidget !=null){
			currencyWidget.setDisabled(isInViewMode());
		}
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
		vendorAccountTransactionTable.add(transactionItem);

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
		// menuButton.setTabIndex(11);
		saveAndCloseButton.setTabIndex(12);
		saveAndNewButton.setTabIndex(13);
		cancelButton.setTabIndex(14);

	}

	@Override
	protected void addAccountTransactionItem(ClientTransactionItem item) {
		vendorAccountTransactionTable.add(item);
	}

	@Override
	protected void addItemTransactionItem(ClientTransactionItem item) {
		vendorItemTransactionTable.add(item);
	}

	@Override
	public List<ClientTransactionItem> getAllTransactionItems() {
		List<ClientTransactionItem> list = new ArrayList<ClientTransactionItem>();
		list.addAll(vendorAccountTransactionTable.getRecords());
		list.addAll(vendorItemTransactionTable.getRecords());
		return list;
	}

	@Override
	protected void taxCodeSelected(ClientTAXCode taxCode) {
		this.taxCode = taxCode;
		if (taxCode != null) {
			taxCodeSelect.setComboItem(taxCode);
			vendorAccountTransactionTable.setTaxCode(taxCode.getID(), true);
			vendorItemTransactionTable.setTaxCode(taxCode.getID(), true);
		} else {
			taxCodeSelect.setValue("");
		}
	}

	@Override
	public void updateAmountsFromGUI() {
		// TODO Auto-generated method stub

	}
}
