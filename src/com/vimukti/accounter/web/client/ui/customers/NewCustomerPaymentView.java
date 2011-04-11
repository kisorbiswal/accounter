package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientCustomerPrePayment;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.AddressCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class NewCustomerPaymentView extends
		AbstractCustomerTransactionView<ClientCustomerPrePayment> {

	private CheckboxItem printCheck;
	private AmountField amountText, endBalText, customerBalText;
	protected ClientCustomerPrePayment takenCustomerPrePayment;
	protected double enteredBalance;
	private DynamicForm custForm;
	private DynamicForm payForm;
	Double toBeSetEndingBalance;
	Double toBeSetCustomerBalance;
	protected boolean isClose;
	protected String paymentMethod = UIUtils
			.getpaymentMethodCheckBy_CompanyType(FinanceApplication
					.getCustomersMessages().check());
	@SuppressWarnings("unused")
	private CheckboxItem thisisVATinclusive;

	private ArrayList<DynamicForm> listforms;
	protected String checkNumber = null;
	protected TextItem checkNo;
	boolean isChecked = false;

	public NewCustomerPaymentView() {
		super(ClientTransaction.TYPE_CUSTOMER_PREPAYMENT, 0);
		this.validationCount = 4;
	}

	@Override
	protected void initMemoAndReference() {
		if (this.transactionObject != null) {

			ClientCustomerPrePayment customerPrePayment = (ClientCustomerPrePayment) transactionObject;

			if (customerPrePayment != null) {

				setMemoTextAreaItem(customerPrePayment.getMemo());
				// setRefText(customerPrePayment.getReference());

			}
		}

	}

	@Override
	public boolean validate() throws InvalidTransactionEntryException,
			InvalidEntryException {

		switch (this.validationCount) {
		case 4:
			return AccounterValidator
					.validateTransactionDate(this.transactionDate);
		case 3:
			return AccounterValidator.validateForm(payForm);
		case 2:
			if (DecimalUtil.isEquals(amountText.getAmount(), 0))
				throw new InvalidTransactionEntryException(
						AccounterErrorType.INVALID_NEGATIVE_AMOUNT);
		case 1:
			return AccounterValidator.isNull(depositInAccount, paymentMethod);

		default:
			return true;
		}

	}

	public static NewCustomerPaymentView getInstance() {

		return new NewCustomerPaymentView();

	}

	@Override
	protected void initTransactionViewData() {
		// super.initTransactionViewData();
		initTransactionNumber();
		if (transactionObject == null)
			initDepositInAccounts();
	}

	public void resetElements() {
		this.customer = null;
		this.addressListOfCustomer = null;
		this.depositInAccount = null;
		this.paymentMethod = UIUtils
				.getpaymentMethodCheckBy_CompanyType(FinanceApplication
						.getCustomersMessages().check());
		amountText.setAmount(0D);
		endBalText.setAmount(0D);
		customerBalText.setAmount(0D);
		memoTextAreaItem.setValue("");
		// refText.setValue("");

	}

	private ClientTransaction getPrePaymentObject() {
		try {

			ClientCustomerPrePayment customerPrePayment = transactionObject != null ? (ClientCustomerPrePayment) transactionObject
					: new ClientCustomerPrePayment();

			customerPrePayment.setNumber(transactionNumber.getValue()
					.toString());
			customerPrePayment.setCustomer(customer.getStringID());

			if (billingAddress != null)
				customerPrePayment.setAddress(billingAddress);
			if (depositInAccount != null)
				customerPrePayment.setDepositIn(depositInAccount.getStringID());
			if (!DecimalUtil.isEquals(enteredBalance, 0.00))
				customerPrePayment.setTotal(enteredBalance);
			if (paymentMethod != null)
				customerPrePayment.setPaymentMethod(paymentMethodCombo.getSelectedValue());

			if (checkNo.getValue() != null && !checkNo.getValue().equals("")) {
				String value;
				if (checkNo.getValue().toString()
						.equalsIgnoreCase(
								FinanceApplication.getCustomersMessages()
										.toBePrinted())) {
					value = String.valueOf(FinanceApplication
							.getVendorsMessages().Tobeprinted());
				} else {
					value = String.valueOf(checkNo.getValue());
				}
				customerPrePayment.setCheckNumber(value);
				// customerPrePayment.setCheckNumber(checkNo.getValue().toString()
				// .equalsIgnoreCase(
				// FinanceApplication.getCustomersMessages()
				// .toBePrinted()) ? null
				// : getCheckNoValue());
			} else {
				customerPrePayment.setCheckNumber("");

			}
			// customerPrePayment.setToBePrinted(isChecked);
			printCheck.setValue(customerPrePayment.isToBePrinted());

			transactionObject = customerPrePayment;

			if (transactionDate != null)
				transactionObject.setDate(transactionDate.getTime());
			customerPrePayment.setMemo(getMemoTextAreaItem());
			// customerPrePayment.setReference(getRefText());

			if (toBeSetEndingBalance != null)
				customerPrePayment.setEndingBalance(toBeSetEndingBalance);
			if (toBeSetCustomerBalance != null)
				customerPrePayment.setCustomerBalance(toBeSetCustomerBalance);

			// if (amountText.getAmount() != null)
			// customerPrePayment.setUnusedAmount(amountText.getAmount());

			customerPrePayment
					.setType(ClientTransaction.TYPE_CUSTOMER_PREPAYMENT);
			transactionObject = customerPrePayment;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return transactionObject;
	}

	@Override
	protected void initTransactionViewData(ClientTransaction transactionObject) {
		ClientCompany comapny = FinanceApplication.getCompany();

		ClientCustomerPrePayment customerPrePaymentToBeEdited = (ClientCustomerPrePayment) transactionObject;
		ClientCustomer customer = comapny
				.getCustomer(customerPrePaymentToBeEdited.getCustomer());
		customerSelected(comapny.getCustomer(customerPrePaymentToBeEdited
				.getCustomer()));
		this.billingAddress = customerPrePaymentToBeEdited.getAddress();
		if (billingAddress != null)
			billToaddressSelected(billingAddress);
		// accountSelected(comapny.getAccount(customerPrePaymentToBeEdited
		// .getDepositIn()));
		amountText.setDisabled(true);
		amountText.setAmount(customerPrePaymentToBeEdited.getTotal());
		customerBalText.setAmount(toBeSetCustomerBalance);
		endBalText.setAmount(toBeSetEndingBalance);
		paymentMethodSelected(customerPrePaymentToBeEdited.getPaymentMethod());
		this.depositInAccount = comapny.getAccount(customerPrePaymentToBeEdited
				.getDepositIn());
		if (depositInAccount != null)
			depositInCombo.setComboItem(depositInAccount);
		if (transactionObject != null) {
			printCheck.setDisabled(true);

			checkNo.setDisabled(true);
			ClientCustomerPrePayment clientCustomerPrePayment = (ClientCustomerPrePayment) transactionObject;
			paymentMethodCombo.setValue(clientCustomerPrePayment
					.getPaymentMethod());
		}

		if (customerPrePaymentToBeEdited.getCheckNumber() != null) {
			if (customerPrePaymentToBeEdited.getCheckNumber().equals(
					FinanceApplication.getCustomersMessages().toBePrinted())) {
				checkNo.setValue(FinanceApplication.getCustomersMessages()
						.toBePrinted());
				printCheck.setValue(true);
			} else {
				checkNo.setValue(customerPrePaymentToBeEdited.getCheckNumber());
				printCheck.setValue(false);
			}
		}
		// else if (customerPrePaymentToBeEdited.getCheckNumber() == null) {
		// checkNo.setValue(FinanceApplication.getCustomersMessages()
		// .toBePrinted());
		// printCheck.setValue(true);
		// }
		initMemoAndReference();
		initTransactionViewData();
	}

	private void accountSelected(ClientAccount account) {
		if (account == null)
			return;
		this.depositInAccount = account;
		depositInCombo.setValue(depositInAccount);
		if (account != null && !(Boolean) printCheck.getValue()) {
			setCheckNumber();
		} else if (account == null)
			checkNo.setValue("");
		adjustBalance(amountText.getAmount());
	}

	private void adjustBalance(double amount) {
		ClientCustomerPrePayment customerPrePayment = (ClientCustomerPrePayment) transactionObject;
		enteredBalance = amount;

		if (DecimalUtil.isLessThan(enteredBalance, 0)
				|| DecimalUtil.isGreaterThan(enteredBalance, 1000000000000.00)) {
			amountText.setAmount(0D);
			enteredBalance = 0D;
		}
		if (customer != null) {
			if (transactionObject != null
					&& customer.getStringID().equals(
							customerPrePayment.getCustomer())
					&& !DecimalUtil.isEquals(enteredBalance, 0)) {
				double cusBal = DecimalUtil
						.isLessThan(customer.getBalance(), 0) ? -1
						* customer.getBalance() : customer.getBalance();
				toBeSetCustomerBalance = (cusBal - transactionObject.getTotal())
						+ enteredBalance;
			} else {
				toBeSetCustomerBalance = customer.getBalance() - enteredBalance;
			}
			customerBalText.setAmount(toBeSetCustomerBalance);

		}
		if (depositInAccount != null) {

			if (depositInAccount.isIncrease()) {
				toBeSetEndingBalance = depositInAccount.getTotalBalance()
						+ enteredBalance;
			} else {
				toBeSetEndingBalance = depositInAccount.getTotalBalance()
						- enteredBalance;
			}
			if (transactionObject != null
					&& depositInAccount.getStringID().equals(
							customerPrePayment.getDepositIn())
					&& !DecimalUtil.isEquals(enteredBalance, 0)) {
				toBeSetEndingBalance = toBeSetEndingBalance
						- transactionObject.getTotal();
			}
			endBalText.setAmount(toBeSetEndingBalance);

		}
	}

	private void setCheckNumber() {
		rpcUtilService.getNextCheckNumber(depositInAccount.getStringID(),
				new AsyncCallback<Long>() {

					public void onFailure(Throwable t) {
						checkNo.setValue(FinanceApplication
								.getCustomersMessages().toBePrinted());
						return;
					}

					public void onSuccess(Long result) {
						if (result == null)
							onFailure(null);

						checkNumber = String.valueOf(result);
						checkNo.setValue(result.toString());
					}

				});

	}

	@Override
	protected void priceLevelSelected(ClientPriceLevel priceLevel) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void salesPersonSelected(ClientSalesPerson person) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createControls() {
		setTitle(customerConstants.customerPayment());
		Label lab1 = new Label(FinanceApplication.getCustomersMessages()
				.customerPrePayment());
		lab1.setStyleName(FinanceApplication.getCustomersMessages()
				.lableTitle());
//		lab1.setHeight("35px");
		transactionDateItem = createTransactionDateItem();

		transactionNumber = createTransactionNumberItem();

		listforms = new ArrayList<DynamicForm>();

		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(4);
		dateNoForm.setStyleName("datenumber-panel");
		dateNoForm.setFields(transactionDateItem, transactionNumber);
		forms.add(dateNoForm);
		HorizontalPanel datepanel = new HorizontalPanel();
		datepanel.setWidth("100%");
		datepanel.add(dateNoForm);
		datepanel.setCellHorizontalAlignment(dateNoForm,
				HasHorizontalAlignment.ALIGN_RIGHT);
		datepanel.getElement().getStyle().setPaddingRight(15, Unit.PX);

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();
		labeldateNoLayout.setWidth("100%");
		// labeldateNoLayout.add(lab1);
		labeldateNoLayout.add(datepanel);
		labeldateNoLayout.setCellHorizontalAlignment(datepanel, ALIGN_RIGHT);
		// customer and address
		customerCombo = createCustomerComboItem(customerConstants
				.customerName());

		billToCombo = createBillToComboItem(customerConstants.address());

		// Ending and Vendor Balance
		endBalText = new AmountField(customerConstants.endingBalance());
		endBalText.setHelpInformation(true);
		endBalText.setWidth(100);
		endBalText.setDisabled(true);

		customerBalText = new AmountField(customerConstants.customerBalance());
		customerBalText.setHelpInformation(true);
		customerBalText.setDisabled(true);
		customerBalText.setWidth(100);

		DynamicForm balForm = new DynamicForm();
		balForm.setFields(endBalText, customerBalText);
		balForm.getCellFormatter().setWidth(0, 0, "205px");
		forms.add(balForm);

		// payment
		depositInCombo = createDepositInComboItem();
		depositInCombo.setPopupWidth("500px");

		amountText = new AmountField(customerConstants.amount());
		amountText.setHelpInformation(true);
		amountText.setWidth(100);
		amountText.setRequired(true);
		amountText.addBlurHandler(getBlurHandler());

		paymentMethodCombo = createPaymentMethodSelectItem();
		paymentMethodCombo.setWidth(100);
//		paymentMethodCombo.setDefaultValue(UIUtils
//				.getpaymentMethodCheckBy_CompanyType(FinanceApplication
//						.getCustomersMessages().check()));
		paymentMethodCombo.setComboItem(FinanceApplication.getCustomersMessages().cheque());
		printCheck = new CheckboxItem(customerConstants.toBePrinted());
		printCheck.setValue(true);
		printCheck.addChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				isChecked = (Boolean) event.getValue();
				if (isChecked) {
					if (printCheck.getValue().toString().equalsIgnoreCase(
							"true")) {
						checkNo.setValue(FinanceApplication
								.getCustomersMessages().toBePrinted());
						checkNo.setDisabled(true);
					} else {
						if (depositInAccount == null)
							checkNo.setValueField(FinanceApplication
									.getVendorsMessages().Tobeprinted());
						else if (transactionObject != null) {
							checkNo
									.setValue(((ClientCustomerPrePayment) transactionObject)
											.getCheckNumber());
						}
					}
				} else
					// setCheckNumber();
					checkNo.setValue("");
				checkNo.setDisabled(false);

			}
		});

		checkNo = createCheckNumberItem();
		checkNo.setValue(FinanceApplication.getCustomersMessages()
				.toBePrinted());
		checkNo.setWidth(100);
		checkNo.setDisabled(true);
		checkNo.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				checkNumber = checkNo.getValue().toString();
			}
		});

		payForm = UIUtils.form(customerConstants.payment());
		payForm.getCellFormatter().addStyleName(7, 0, "memoFormAlign");
		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setWidth(100);
		// refText = createRefereceText();
		// refText.setWidth(100);
		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			payForm.setFields(customerCombo, billToCombo, depositInCombo,
					amountText, paymentMethodCombo, printCheck, checkNo,
					memoTextAreaItem);
		else
			payForm.setFields(customerCombo, billToCombo, depositInCombo,
					amountText, paymentMethodCombo, printCheck, checkNo,
					memoTextAreaItem);
		// memo and Reference

		payForm.setCellSpacing(5);
		payForm.setWidth("100%");
		payForm.getCellFormatter().setWidth(0, 0, "160px");

		VerticalPanel leftPanel = new VerticalPanel();
		leftPanel.setWidth("100%");
		leftPanel.setSpacing(5);
		leftPanel.add(payForm);
		// leftPanel.add(payForm);
		// leftPanel.add(memoForm);

		VerticalPanel rightPanel = new VerticalPanel();
		rightPanel.setWidth("100%");
		rightPanel.add(balForm);
		rightPanel.setCellHorizontalAlignment(balForm,
				HasHorizontalAlignment.ALIGN_CENTER);

		HorizontalPanel hLay = new HorizontalPanel();
		hLay.setWidth("100%");
		hLay.setSpacing(10);
		hLay.add(leftPanel);
		hLay.add(rightPanel);
		hLay.setCellWidth(leftPanel, "50%");
		hLay.setCellWidth(rightPanel, "44%");

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(lab1);
		mainVLay.add(labeldateNoLayout);
		mainVLay.add(hLay);

		if (UIUtils.isMSIEBrowser()) {
			payForm.getCellFormatter().setWidth(0, 1, "300px");
			payForm.setWidth("75%");
			balForm.getCellFormatter().setWidth(0, 1, "150px");
		}

		canvas.add(mainVLay);

		setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(balForm);
		listforms.add(payForm);

	}

	private AddressCombo createBillToComboItem(String address) {
		AddressCombo addressCombo = new AddressCombo(FinanceApplication
				.getCustomersMessages().address());
		addressCombo.setHelpInformation(true);
		addressCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAddress>() {

					public void selectedComboBoxItem(ClientAddress selectItem) {

						billToaddressSelected(selectItem);

					}

				});

		addressCombo.setDisabled(isEdit);

		formItems.add(addressCombo);

		return addressCombo;

	}

	private TextItem createCheckNumberItem() {
		TextItem checkNoTextItem = new TextItem(UIUtils
				.getpaymentMethodCheckBy_CompanyType(FinanceApplication
						.getCustomersMessages().check())
				+ " " + "No");
		checkNoTextItem.setHelpInformation(true);
		return checkNoTextItem;
	}

	@Override
	public void saveAndUpdateView() throws Exception {

		try {
			transactionObject = getPrePaymentObject();
			if (transactionObject.getStringID() == null)
				createObject((ClientCustomerPrePayment) transactionObject);
			else
				alterObject((ClientCustomerPrePayment) transactionObject);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	private String getCheckNoValue() {
		return checkNumber;
	}

	private BlurHandler getBlurHandler() {
		BlurHandler blurHandler = new BlurHandler() {

			Object value = null;

			public void onBlur(BlurEvent event) {
				try {

					value = amountText.getValue();

					if (value == null)
						return;

					Double amount = DataUtils.getAmountStringAsDouble(value
							.toString());
					if (DecimalUtil.isLessThan(amount, 0)) {
						Accounter.showError(FinanceApplication
								.getCustomersMessages().noNegativeAmounts());
						amountText.setAmount(0.00D);

					}

					amountText
							.setAmount(DataUtils.isValidAmount(amount + "") ? amount
									: 0.0);

					adjustBalance(amountText.getAmount());

				} catch (Exception e) {
					if (e instanceof InvalidEntryException) {
						Accounter.showError(e.getMessage());
					}
					amountText.setAmount(0.0);
				}

			}
		};

		return blurHandler;
	}

	@Override
	protected void paymentMethodSelected(String paymentMethod) {
		if (paymentMethod == null)
			return;

		if (paymentMethod != null) {
			this.paymentMethod = paymentMethod;
			if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK ? paymentMethod
					.equalsIgnoreCase(FinanceApplication.getVendorsMessages()
							.cheque())
					: paymentMethod.equalsIgnoreCase(FinanceApplication
							.getVendorsMessages().check())) {

				printCheck.setDisabled(false);
				checkNo.setDisabled(false);

			} else {
				printCheck.setDisabled(true);
				checkNo.setDisabled(true);
			}
		}

	}

	@Override
	protected void customerSelected(ClientCustomer customer) {

		if (customer == null)
			return;
		this.customer = customer;
		if (customer != null && customerCombo != null) {
			customerCombo.setComboItem(FinanceApplication.getCompany()
					.getCustomer(customer.getStringID()));
		}
		this.addressListOfCustomer = customer.getAddress();
		initBillToCombo();
		adjustBalance(amountText.getAmount());

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
		this.customerCombo.setFocus();
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		switch (command) {
		case AccounterCommand.CREATION_SUCCESS:

			// if (core.getObjectType() == AccounterCoreType.CUSTOMER)
			// this.customerCombo.addComboItem((ClientCustomer) core);

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.depositInCombo.addComboItem((ClientAccount) core);

			break;
		case AccounterCommand.DELETION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.depositInCombo.removeComboItem((ClientAccount) core);

			break;
		case AccounterCommand.UPDATION_SUCCESS:
			//
			// if (core.getObjectType() == AccounterCoreType.ACCOUNT)
			// this.depositInCombo.updateComboItem((ClientAccount) core);
			//
			// if (core.getObjectType() == AccounterCoreType.CUSTOMER)
			// this.customerCombo.updateComboItem((ClientCustomer) core);

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
		customerCombo.setDisabled(isEdit);
		transactionDateItem.setDisabled(isEdit);
		transactionNumber.setDisabled(isEdit);
		printCheck.setDisabled(isEdit);
		amountText.setDisabled(isEdit);
		paymentMethodCombo.setDisabled(isEdit);
		paymentMethodSelected(paymentMethodCombo.getSelectedValue());
		if (printCheck.getValue().toString().equalsIgnoreCase("true")) {
			checkNo.setValue(FinanceApplication.getCustomersMessages()
					.toBePrinted());
			checkNo.setDisabled(true);
		}
		super.onEdit();
	}

	public void setTransactionDate(ClientFinanceDate transactionDate) {
		super.setTransactionDate(transactionDate);
		if (this.transactionDateItem != null
				&& this.transactionDateItem.getValue() != null)
			updateNonEditableItems();
	}

	public void updateNonEditaleItems() {
		if (endBalText != null)
			this.endBalText.setAmount(toBeSetEndingBalance);
		if (customerBalText != null)
			this.customerBalText.setAmount(toBeSetCustomerBalance);
	}

	@Override
	protected void initSalesTaxNonEditableItem() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initTransactionTotalNonEditableItem() {
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

	@Override
	public void deleteFailed(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(Boolean result) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void depositInAccountSelected(ClientAccount depositInAccount2) {
		super.depositInAccountSelected(depositInAccount2);
		adjustBalance(amountText.getAmount());
	}

	@Override
	protected void taxCodeSelected(ClientTAXCode taxCode) {
		// TODO Auto-generated method stub

	}
}
