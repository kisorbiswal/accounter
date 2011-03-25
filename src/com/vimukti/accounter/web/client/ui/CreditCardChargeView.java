package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCreditCardCharge;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.banking.AbstractBankTransactionView;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.combo.VendorCombo;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class CreditCardChargeView extends
		AbstractBankTransactionView<ClientCreditCardCharge> {
	protected List<String> selectedComboList;
	protected DateField date, delivDate;;
	protected TextItem cheqNoText;
	// protected TextItem refText;
	AmountField totText;

	protected DynamicForm vendorForm, addrForm, phoneForm, termsForm, memoForm;
	protected SelectCombo contactNameSelect, phoneSelect, payMethSelect;

	VendorCombo vendorNameSelect;
	@SuppressWarnings("unused")
	private TextAreaItem addrArea;

	protected String selectPaymentMethod;

	protected ClientCreditCardCharge creditCardChargeTaken;

	// protected ClientVendor selectedVendor;

	private DynamicForm totForm;
	@SuppressWarnings("unused")
	private HorizontalPanel botPanel, addLinkPanel;
	HorizontalPanel totPanel;
	@SuppressWarnings("unused")
	private VerticalPanel leftVLay, botVLay;

	private ArrayList<DynamicForm> listforms;
	protected ClientContact contact;
	protected Label titlelabel;

	protected CreditCardChargeView() {

		super(ClientTransaction.TYPE_CREDIT_CARD_CHARGE,
				VENDOR_TRANSACTION_GRID);
		this.validationCount = 5;

	}

	protected CreditCardChargeView(int type) {

		super(type, VENDOR_TRANSACTION_GRID);

	}

	@Override
	public void setData(ClientCreditCardCharge data) {
		super.setData(data);
		if (isEdit && (!transactionObject.isCreditCardCharge()))
			try {
				throw new Exception(FinanceApplication.getFinanceUIConstants()
						.UnableToLoadRequiredCreditCardCharge());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	// public CreditCardChargeView(CreditCardCharge creditCardChargeTaken) {
	// // this.creditCardChargeTaken = creditCardChargeTaken;
	//
	// // addpayFromAccountsList();
	// // addVendorsList();
	// // createControls();
	// super(creditCardChargeTaken);
	// }

	public static CreditCardChargeView getInstance() {

		return new CreditCardChargeView();
	}

	protected void addPhonesContactsAndAddress() {
		// Set<Address> allAddress = selectedVendor.getAddress();
		addressList = selectedVendor.getAddress();
		initBillToCombo();
		billToCombo.setDisabled(isEdit);
		Set<ClientContact> allContacts;
		allContacts = selectedVendor.getContacts();
		Iterator<ClientContact> it = allContacts.iterator();
		List<String> phones = new ArrayList<String>();
		ClientContact primaryContact = null;
		List<String> idNamesForContacts = new ArrayList<String>();
		int i = 0;
		while (it.hasNext()) {
			ClientContact contact = it.next();
			if (contact.isPrimary())
				primaryContact = contact;
			idNamesForContacts.add(contact.getName());
			phones.add(contact.getBusinessPhone());
			i++;
		}

		contactNameSelect.initCombo(idNamesForContacts);
		phoneSelect.initCombo(phones);

		if (creditCardChargeTaken != null) {
			// ClientVendor cv = FinanceApplication.getCompany().getVendor(
			// creditCardChargeTaken.getVendor());
			if (creditCardChargeTaken.getContact() != null)
				contactNameSelect.setSelected(creditCardChargeTaken
						.getContact().getStringID());
			if (creditCardChargeTaken.getPhone() != null)
				// FIXME check and fix the below code
				phoneSelect.setSelected(creditCardChargeTaken.getPhone());

			contactNameSelect.setDisabled(isEdit);
			phoneSelect.setDisabled(isEdit);
			return;
		}
		if (primaryContact == null) {
			contactNameSelect.setSelected("");
			phoneSelect.setSelected("");
			return;
		}

		contactNameSelect.setSelected(String.valueOf(primaryContact
				.getStringID()));
		phoneSelect.setSelected(primaryContact.getBusinessPhone());

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
		List<ClientVendor> result = FinanceApplication.getCompany()
				.getActiveVendors();
		if (result != null) {
			initVendorsList(result);

		}
	}

	protected void initVendorsList(List<ClientVendor> result) {
		// First identify existing selected vendor
		for (ClientVendor vendor : result) {
			if (creditCardChargeTaken != null)
				if (vendor.getStringID().equalsIgnoreCase(
						creditCardChargeTaken.getVendor())) {
					selectedVendor = vendor;
				}
		}
		vendorNameSelect.initCombo(result);
		vendorNameSelect.setDisabled(isEdit);
		if (creditCardChargeTaken != null) {
			vendorNameSelect.setComboItem(selectedVendor);
			billToaddressSelected(selectedVendor.getSelectedAddress());
			addPhonesContactsAndAddress();
		}
	}

	public ClientContact getContactBasedOnId(String Id) {
		for (ClientContact cont : selectedVendor.getContacts()) {
			if (String.valueOf(cont.getStringID()).equalsIgnoreCase(Id))
				return cont;

		}

		return null;
	}

	@Override
	protected void initMemoAndReference() {
		if (transactionObject != null)
			setMemoTextAreaItem(((ClientCreditCardCharge) transactionObject)
					.getMemo());
		// refText.setValue(creditCardChargeTaken.getReference());

	}

	protected void initTransactionViewData() {

		if (transactionObject == null) {
			resetElements();
			initpayFromAccountCombo();
		}
		// super.initTransactionViewData();
		// initMemoAndReference();
		initTransactionNumber();
		addVendorsList();
		// setDisableStaeForFormItems();

	}

	@Override
	protected void paymentMethodSelected(String paymentMethod2) {
		super.paymentMethodSelected(paymentMethod2);
		if (paymentMethod != null
				&& (paymentMethod.equals(ClientCompany.CHECK) || paymentMethod
						.equals(ClientCompany.CHECK_FOR_UK))) {
			if (creditCardChargeTaken != null) {
				cheqNoText
						.setValue(creditCardChargeTaken.getCheckNumber() != null ? creditCardChargeTaken
								.getCheckNumber()
								: "");

			}
			cheqNoText.setDisabled(false);
		} else {
			cheqNoText.setValue("");
			cheqNoText.setDisabled(true);
		}
	}

	@SuppressWarnings("unused")
	private void setDisableStaeForFormItems() {

		for (FormItem formItem : formItems) {

			if (formItem != null)
				formItem.setDisabled(isEdit);

		}

	}

	@Override
	protected void initTransactionViewData(ClientTransaction transactionObject) {
		creditCardChargeTaken = (ClientCreditCardCharge) transactionObject;
		transactionDateItem.setValue(creditCardChargeTaken.getDate());
		contact = creditCardChargeTaken.getContact();
		delivDate.setValue(new ClientFinanceDate(creditCardChargeTaken
				.getDeliveryDate()));
		delivDate.setDisabled(isEdit);

		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK) {
			netAmount.setAmount(creditCardChargeTaken.getNetAmount());
			vatTotalNonEditableText.setAmount(creditCardChargeTaken.getTotal()
					- creditCardChargeTaken.getNetAmount());
		}
		transactionTotalNonEditableText.setAmount(creditCardChargeTaken
				.getTotal());

		if (vatinclusiveCheck != null) {
			setAmountIncludeChkValue(transactionObject.isAmountsIncludeVAT());
		}
		if (creditCardChargeTaken.getPayFrom() != null
				&& creditCardChargeTaken.getPayFrom().length() != 0)
			payFromAccountSelected(creditCardChargeTaken.getPayFrom());
		payFrmSelect.setComboItem(FinanceApplication.getCompany().getAccount(
				payFromAccount));
		payFrmSelect.setDisabled(isEdit);
		paymentMethodSelected(creditCardChargeTaken.getPaymentMethod());
		payMethSelect.setComboItem(creditCardChargeTaken.getPaymentMethod());
		vendorTransactionGrid.setCanEdit(false);
		initMemoAndReference();
		initTransactionViewData();

	}

	private void resetElements() {
		selectedVendor = null;
		creditCardChargeTaken = null;
		billingAddress = null;
		addressList = null;
		billToCombo.setDisabled(isEdit);
		paymentMethod = UIUtils
				.getpaymentMethodCheckBy_CompanyType(FinanceApplication
						.getCustomersMessages().check());
		payFromAccount = "";
		phoneSelect.setValueMap("");
		setMemoTextAreaItem("");
		// refText.setValue("");
		cheqNoText.setValue("");

	}

	@Override
	protected void createControls() {
		setTitle(bankingConstants.creditCardCharge());
		titlelabel = new Label(FinanceApplication.getFinanceUIConstants()
				.creditCardCharge());
		titlelabel.addStyleName(FinanceApplication.getFinanceUIConstants()
				.lableTitle());
		titlelabel.setHeight("35px");
		transactionDateItem = createTransactionDateItem();
		transactionNumber = createTransactionNumberItem();

		listforms = new ArrayList<DynamicForm>();

		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(4);
		dateNoForm.setFields(transactionDateItem, transactionNumber);
		forms.add(dateNoForm);

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();

		VerticalPanel regPanel = new VerticalPanel();
		regPanel.setCellHorizontalAlignment(dateNoForm, ALIGN_RIGHT);
		regPanel.add(dateNoForm);
		regPanel.getElement().getStyle().setPaddingRight(25, Unit.PX);

		labeldateNoLayout.setWidth("100%");
		// labeldateNoLayout.add(titlelabel);
		labeldateNoLayout.add(regPanel);
		labeldateNoLayout.setCellHorizontalAlignment(regPanel, ALIGN_RIGHT);

		vendorNameSelect = new VendorCombo(UIUtils.getVendorString(
				FinanceApplication.getVendorsMessages().supplierName(),
				FinanceApplication.getVendorsMessages().vendorName()));
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
						addPhonesContactsAndAddress();
					}

				});

		contactNameSelect = new SelectCombo(bankingConstants.contactName());
		contactNameSelect.setHelpInformation(true);
		contactNameSelect.setWidth(100);
		formItems.add(contactNameSelect);
		billToCombo = createBillToComboItem();
		formItems.add(billToCombo);
		phoneSelect = new SelectCombo(bankingConstants.phone());
		phoneSelect.setHelpInformation(true);
		phoneSelect.setWidth(100);
		forms.add(phoneForm);
		formItems.add(phoneSelect);

		vendorForm = UIUtils.form(bankingConstants.Vendor());
		vendorForm.setWidth("100%");
		vendorForm.setFields(vendorNameSelect, contactNameSelect, phoneSelect,
				billToCombo);
		vendorForm.getCellFormatter().setWidth(0, 0, "180px");

		forms.add(vendorForm);

		payMethSelect = createPaymentMethodSelectItem();
		payMethSelect.setTitle(FinanceApplication.getFinanceUIConstants()
				.paymentMethod());
		payMethSelect.setWidth(100);
		payMethSelect.setDefaultValue(FinanceApplication.getVendorsMessages()
				.cheque());

		payFrmSelect = createPayFromselectItem();
		payFrmSelect.setWidth(100);
		payFrmSelect.setPopupWidth("510px");
		payFrmSelect.setTitle(FinanceApplication.getFinanceUIConstants()
				.payFrom());
		payFromAccount = "";
		payFrmSelect.setColSpan(0);
		formItems.add(payFrmSelect);

		cheqNoText = new TextItem(
				FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK ? bankingConstants
						.chequeNo()
						: bankingConstants.chequeNo());
		cheqNoText.setHelpInformation(true);
		cheqNoText.setWidth(100);
		cheqNoText.setDisabled(isEdit);
		formItems.add(cheqNoText);

		delivDate = new DateField(bankingConstants.deliveryDate());
		delivDate.setHelpInformation(true);
		delivDate.setColSpan(1);
		delivDate.setValue(new ClientFinanceDate());
		formItems.add(delivDate);

		termsForm = UIUtils.form(bankingConstants.terms());
		termsForm.setWidth("100%");
		termsForm.setFields(payMethSelect, payFrmSelect, cheqNoText, delivDate);
		termsForm.getCellFormatter().getElement(0, 0).setAttribute(
				FinanceApplication.getCustomersMessages().width(), "203px");
		forms.add(termsForm);

		Label lab2 = new Label("Items and expenses");

		Button addButton = createAddNewButton();// new Button(FinanceApplication

		netAmount = new AmountLabel(FinanceApplication.getBankingsMessages()
				.netAmount());
		netAmount.setDefaultValue("£0.00");
		netAmount.setDisabled(true);

		transactionTotalNonEditableText = createTransactionTotalNonEditableLabel();

		vatTotalNonEditableText = createVATTotalNonEditableLabel();

		vatinclusiveCheck = new CheckboxItem(FinanceApplication
				.getBankingsMessages().amountincludesVat());
		vatinclusiveCheck = getVATInclusiveCheckBox();

		vendorTransactionGrid = getGrid();// new VendorTransactionUKGrid();
		vendorTransactionGrid.setTransactionView(this);
		vendorTransactionGrid.setCanEdit(true);
		vendorTransactionGrid.setEditEventType(ListGrid.EDIT_EVENT_CLICK);
		vendorTransactionGrid.init();
		vendorTransactionGrid.setDisabled(isEdit);

		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setWidth(100);
		memoTextAreaItem.setDisabled(false);

		// refText = new TextItem(bankingConstants.reference());
		//	
		// refText.setWidth(100);
		// refText.setDisabled(false);

		DynamicForm memoForm = new DynamicForm();
		memoForm.setWidth("100%");
		memoForm.setFields(memoTextAreaItem);
		memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");
		forms.add(memoForm);

		DynamicForm vatCheckform = new DynamicForm();
		// vatCheckform.setFields(vatinclusiveCheck);

		DynamicForm totalForm = new DynamicForm();
		totalForm.setNumCols(2);
		totalForm.setWidth("50%");
		totalForm.setStyleName("invoice-total");
		// totText = new AmountField(FinanceApplication.getFinanceUIConstants()
		// .total());
		// totText.setWidth(100);

		totForm = new DynamicForm();
		totForm.setWidth("100%");
		totForm.setWidth("auto");

		botPanel = new HorizontalPanel();
		botPanel.setWidth("100%");

		VerticalPanel bottompanel = new VerticalPanel();
		bottompanel.setWidth("100%");

		HorizontalPanel panel = new HorizontalPanel();
		panel.setHorizontalAlignment(ALIGN_RIGHT);
		panel.add(createAddNewButton());

		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			totalForm.setFields(netAmount, vatTotalNonEditableText,
					transactionTotalNonEditableText);
			VerticalPanel vPanel = new VerticalPanel();
			vPanel.setHorizontalAlignment(ALIGN_RIGHT);
			vPanel.setWidth("100%");
			vPanel.add(panel);
			vPanel.add(totalForm);

			botPanel.add(memoForm);
			botPanel.add(totalForm);

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
			VerticalPanel vpanel = new VerticalPanel();
			vpanel.add(panel);
			vpanel.add(memoForm);
			vpanel.add(totForm);

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
		vLay1.add(vendorTransactionGrid);
		vLay1.add(bottompanel);

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(titlelabel);
		mainVLay.add(labeldateNoLayout);
		mainVLay.add(topHLay);
		mainVLay.add(vLay1);

		canvas.add(mainVLay);

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

		if (transactionObject != null) {
			ClientCreditCardCharge creditCardCharge = (ClientCreditCardCharge) transactionObject;
			payFrmSelect.setComboItem(FinanceApplication.getCompany()
					.getAccount(creditCardCharge.getPayFrom()));
		}
	}

	// protected void payFromMethodSelected(Account account2) {
	// this.account = account2;
	//
	// }

	public void saveAndUpdateView() throws Exception {

		ClientCreditCardCharge creditCardCharge = prepareObject();
		transactionObject = creditCardCharge;

		if (accountType == ClientCompany.ACCOUNTING_TYPE_UK)
			creditCardCharge.setNetAmount(netAmount.getAmount());
		// creditCardCharge.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
		// .getValue());

		super.saveAndUpdateView();

		createAlterObject();
	}

	protected ClientCreditCardCharge prepareObject() throws Exception {

		ClientCreditCardCharge creditCardCharge = transactionObject != null ? (ClientCreditCardCharge) transactionObject
				: new ClientCreditCardCharge();
		if (creditCardChargeTaken != null)
			creditCardCharge = creditCardChargeTaken;
		else
			creditCardCharge = new ClientCreditCardCharge();

		// Setting Type
		creditCardCharge.setType(ClientTransaction.TYPE_CREDIT_CARD_CHARGE);

		// setting date
		if (transactionDateItem != null)
			creditCardCharge.setDate(transactionDateItem.getValue().getTime());
		// setting number
		if (transactionNumber != null)
			creditCardCharge.setNumber(transactionNumber.getValue().toString());
		if (selectedVendor != null) {

			// setting vendor
			creditCardCharge.setVendor(selectedVendor.getStringID());

			// setting contact
			if (contact != null) {
				creditCardCharge.setContact(contact);
			}
			// if (contactNameSelect.getValue() != null) {
			// // ClientContact contact = getContactBasedOnId(contactNameSelect
			// // .getValue().toString());
			// creditCardCharge
			// .setContact(getContactBasedOnId(contactNameSelect
			// .getValue().toString()));
			//
			// }
		}

		// Setting Address
		if (billingAddress != null)
			creditCardCharge.setVendorAddress(billingAddress);

		// setting phone
		if (phoneSelect.getSelectedValue() != null)
			creditCardCharge.setPhone(phoneSelect.getSelectedValue());

		// Setting payment method

		creditCardCharge.setPaymentMethod(paymentMethod);

		// Setting pay from
		payFromAccount = payFrmSelect.getSelectedValue().getStringID();
		if (payFromAccount != null && payFromAccount.length() != 0) {

			creditCardCharge.setPayFrom(FinanceApplication.getCompany()
					.getAccount(payFromAccount).getStringID());
		}

		// setting check no
		if (cheqNoText.getValue() != null)
			creditCardCharge.setCheckNumber(cheqNoText.getValue().toString());

		if (vatinclusiveCheck != null) {
			creditCardCharge.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
					.getValue());
		}

		// setting delivery date
		creditCardCharge.setDeliveryDate(UIUtils.toDate(delivDate.getValue()));

		// Setting transactions
		creditCardCharge.setTransactionItems(vendorTransactionGrid
				.getallTransactions(creditCardCharge));

		// setting total
		creditCardCharge.setTotal(vendorTransactionGrid.getTotal());
		// setting memo
		creditCardCharge.setMemo(getMemoTextAreaItem());
		// setting ref
		// creditCardCharge.setReference(UIUtils.toStr(refText.getValue()));

		transactionObject = creditCardCharge;

		return creditCardCharge;
	}

	public void createAlterObject() {
		if (transactionObject.getStringID() != null)
			alterObject((ClientCreditCardCharge) transactionObject);
		else
			createObject((ClientCreditCardCharge) transactionObject);

	}

	@Override
	public void updateNonEditableItems() {

		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			transactionTotalNonEditableText.setAmount(vendorTransactionGrid
					.getTotal());
			netAmount.setAmount(vendorTransactionGrid.getGrandTotal());
			vatTotalNonEditableText.setAmount(vendorTransactionGrid.getTotal()
					- vendorTransactionGrid.getGrandTotal());
		} else {
			transactionTotalNonEditableText.setAmount(vendorTransactionGrid
					.getTotal());
		}
	}

	@Override
	public boolean validate() throws InvalidTransactionEntryException,
			InvalidEntryException {

		switch (this.validationCount) {
		case 5:
			return AccounterValidator.validateTransactionDate(transactionDate);
		case 4:
			return AccounterValidator.validateForm(vendorForm);
		case 3:
			return AccounterValidator.validateForm(termsForm);
		case 2:
			return AccounterValidator.isBlankTransaction(vendorTransactionGrid);
		case 1:
			return vendorTransactionGrid.validateGrid();
		default:
			return true;

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
		this.vendorNameSelect.setFocus();
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

		super.fitToSize(height, width);

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

		switch (command) {
		case AccounterCommand.CREATION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.VENDOR)
				this.vendorNameSelect.addComboItem((ClientVendor) core);
			break;

		case AccounterCommand.DELETION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.VENDOR)
				this.vendorNameSelect.removeComboItem((ClientVendor) core);

			break;
		case AccounterCommand.UPDATION_SUCCESS:
			break;
		}

	}

	public void onEdit() {
		AsyncCallback<Boolean> editCallBack = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				Accounter.showError(((InvalidOperationException) (caught))
						.getDetailedMessage());
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		AccounterCoreType type = UIUtils.getAccounterCoreType(transactionObject
				.getType());
		this.rpcDoSerivce.canEdit(type, transactionObject.stringID,
				editCallBack);

	}

	protected void enableFormItems() {
		isEdit = false;
		transactionDateItem.setDisabled(isEdit);
		transactionNumber.setDisabled(isEdit);
		payMethSelect.setDisabled(isEdit);
		if (paymentMethod.equals(FinanceApplication.getVendorsMessages()
				.check())
				|| paymentMethod.equals(FinanceApplication.getVendorsMessages()
						.cheque())) {
			cheqNoText.setDisabled(isEdit);
		} else {
			cheqNoText.setDisabled(!isEdit);
		}
		delivDate.setDisabled(isEdit);
		billToCombo.setDisabled(isEdit);
		vendorNameSelect.setDisabled(isEdit);
		contactNameSelect.setDisabled(isEdit);
		phoneSelect.setDisabled(isEdit);
		payFrmSelect.setDisabled(isEdit);
		vendorTransactionGrid.setCanEdit(true);

		vendorTransactionGrid.setDisabled(isEdit);
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
		// TODO Auto-generated method stub

	}

	@Override
	protected void initTransactionTotalNonEditableItem() {
		// TODO Auto-generated method stub

	}

	private void resetFormView() {
		vendorForm.getCellFormatter().setWidth(0, 1, "200px");
		// refText.setWidth("200px");
	}
}
