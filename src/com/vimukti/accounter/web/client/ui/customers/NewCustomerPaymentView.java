package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
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
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.AddressCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class NewCustomerPaymentView extends
		AbstractCustomerTransactionView<ClientCustomerPrePayment> {
	AccounterConstants accounterConstants = GWT
			.create(AccounterConstants.class);

	private CheckboxItem printCheck;
	private AmountField amountText, endBalText, customerBalText;
	protected double enteredBalance;
	private DynamicForm custForm;
	private DynamicForm payForm;
	Double toBeSetEndingBalance;
	Double toBeSetCustomerBalance;
	protected boolean isClose;
	protected String paymentMethod = UIUtils
			.getpaymentMethodCheckBy_CompanyType(Accounter.constants().check());

	private CheckboxItem thisisVATinclusive;

	private ArrayList<DynamicForm> listforms;
	protected String checkNumber = null;
	protected TextItem checkNo;
	boolean isChecked = false;
	private boolean locationTrackingEnabled;

	public NewCustomerPaymentView() {
		super(ClientTransaction.TYPE_CUSTOMER_PREPAYMENT);
		locationTrackingEnabled = getCompany().getPreferences()
				.isLocationTrackingEnabled();
	}

	@Override
	protected void initMemoAndReference() {
		if (isInViewMode()) {

			ClientCustomerPrePayment customerPrePayment = (ClientCustomerPrePayment) transaction;

			if (customerPrePayment != null) {
				memoTextAreaItem.setDisabled(true);
				setMemoTextAreaItem(customerPrePayment.getMemo());
				// setRefText(customerPrePayment.getReference());

			}
		}

	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();

		// Validations
		// 1. isValidTransactionDate?
		// 2. isInPreventPostingBeforeDate?
		// 3. payForm.validate?
		// 4. isNegativeAmount?

		if (!AccounterValidator.isValidTransactionDate(this.transactionDate)) {
			result.addError(transactionDateItem,
					accounterConstants.invalidateTransactionDate());
		}
		if (AccounterValidator
				.isInPreventPostingBeforeDate(this.transactionDate)) {
			result.addError(transactionDateItem,
					accounterConstants.invalidateDate());
		}

		result.add(payForm.validate());

		if (!AccounterValidator.isPositiveAmount(amountText.getAmount())) {
			amountText.textBox.addStyleName("highlightedFormItem");
			result.addError(amountText,
					accounterConstants.invalidNegativeAmount());
		}
		return result;
	}

	public static NewCustomerPaymentView getInstance() {

		return new NewCustomerPaymentView();

	}

	public void resetElements() {
		this.setCustomer(null);
		this.addressListOfCustomer = null;
		this.depositInAccount = null;
		this.paymentMethod = UIUtils
				.getpaymentMethodCheckBy_CompanyType(Accounter.constants()
						.check());
		amountText.setAmount(0D);
		endBalText.setAmount(0D);
		customerBalText.setAmount(0D);
		memoTextAreaItem.setValue("");
		// refText.setValue("");

	}

	protected void updateTransaction() {
		super.updateTransaction();

		transaction.setNumber(transactionNumber.getValue().toString());
		if (customer != null)

			transaction.setCustomer(getCustomer().getID());

		if (billingAddress != null)
			transaction.setAddress(billingAddress);
		if (depositInAccount != null)
			transaction.setDepositIn(depositInAccount.getID());
		if (!DecimalUtil.isEquals(enteredBalance, 0.00))
			transaction.setTotal(enteredBalance);
		if (paymentMethod != null)
			transaction.setPaymentMethod(paymentMethodCombo.getSelectedValue());

		if (checkNo.getValue() != null && !checkNo.getValue().equals("")) {
			String value;
			if (checkNo.getValue().toString()
					.equalsIgnoreCase(Accounter.constants().toBePrinted())) {
				value = String.valueOf(Accounter.constants().toBePrinted());
			} else {
				value = String.valueOf(checkNo.getValue());
			}
			transaction.setCheckNumber(value);
			// transaction.setCheckNumber(checkNo.getValue().toString()
			// .equalsIgnoreCase(
			// FinanceApplication.constants()
			// .toBePrinted()) ? null
			// : getCheckNoValue());
		} else {
			transaction.setCheckNumber("");

		}
		// transaction.setToBePrinted(isChecked);
		if (transaction.getID() != 0)

			printCheck.setValue(transaction.isToBePrinted());
		else
			printCheck.setValue(true);

		if (transactionDate != null)
			transaction.setDate(transactionDateItem.getEnteredDate().getDate());
		transaction.setMemo(getMemoTextAreaItem());
		// transaction.setReference(getRefText());

		if (toBeSetEndingBalance != null)
			transaction.setEndingBalance(toBeSetEndingBalance);
		if (toBeSetCustomerBalance != null)
			transaction.setCustomerBalance(toBeSetCustomerBalance);

		// if (amountText.getAmount() != null)
		// transaction.setUnusedAmount(amountText.getAmount());

		transaction.setType(ClientTransaction.TYPE_CUSTOMER_PREPAYMENT);

	}

	@Override
	protected void initTransactionViewData() {
		if (transaction == null) {
			setData(new ClientCustomerPrePayment());
			initDepositInAccounts();
		} else {
			ClientCompany comapny = getCompany();

			ClientCustomer customer = comapny.getCustomer(transaction
					.getCustomer());
			customerSelected(comapny.getCustomer(transaction.getCustomer()));
			this.billingAddress = transaction.getAddress();
			if (billingAddress != null)
				billToaddressSelected(billingAddress);
			// accountSelected(comapny.getAccount(customerPrePaymentToBeEdited
			// .getDepositIn()));
			amountText.setDisabled(true);
			amountText.setAmount(transaction.getTotal());
			customerBalText.setAmount(customer.getBalance());
			endBalText.setAmount(transaction.getEndingBalance());
			paymentMethodSelected(transaction.getPaymentMethod());
			this.depositInAccount = comapny.getAccount(transaction
					.getDepositIn());
			if (depositInAccount != null)
				depositInCombo.setComboItem(depositInAccount);
			printCheck.setDisabled(true);

			checkNo.setDisabled(true);
			paymentMethodCombo.setValue(transaction.getPaymentMethod());

			if (transaction.getCheckNumber() != null) {
				if (transaction.getCheckNumber().equals(
						Accounter.constants().toBePrinted())) {
					checkNo.setValue(Accounter.constants().toBePrinted());
					printCheck.setValue(true);
				} else {
					checkNo.setValue(transaction.getCheckNumber());
					printCheck.setValue(false);
				}
			}
		}
		// else if (customerPrePaymentToBeEdited.getCheckNumber() == null) {
		// checkNo.setValue(FinanceApplication.constants()
		// .toBePrinted());
		// printCheck.setValue(true);
		// }
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));
		initMemoAndReference();
		initTransactionNumber();
		initCustomers();
	}

	private void initCustomers() {
		// TODO Auto-generated method stub

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
		ClientCustomerPrePayment customerPrePayment = (ClientCustomerPrePayment) transaction;
		enteredBalance = amount;

		if (DecimalUtil.isLessThan(enteredBalance, 0)
				|| DecimalUtil.isGreaterThan(enteredBalance, 1000000000000.00)) {
			amountText.setAmount(0D);
			enteredBalance = 0D;
		}
		if (getCustomer() != null) {
			if (isInViewMode()
					&& getCustomer().getID() == (customerPrePayment
							.getCustomer())
					&& !DecimalUtil.isEquals(enteredBalance, 0)) {
				double cusBal = DecimalUtil.isLessThan(getCustomer()
						.getBalance(), 0) ? -1 * getCustomer().getBalance()
						: getCustomer().getBalance();
				toBeSetCustomerBalance = (cusBal - transaction.getTotal())
						+ enteredBalance;
			} else {
				toBeSetCustomerBalance = getCustomer().getBalance()
						- enteredBalance;
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
			if (isInViewMode()
					&& depositInAccount.getID() == (customerPrePayment
							.getDepositIn())
					&& !DecimalUtil.isEquals(enteredBalance, 0)) {
				toBeSetEndingBalance = toBeSetEndingBalance
						- transaction.getTotal();
			}
			endBalText.setAmount(toBeSetEndingBalance);

		}
	}

	private void setCheckNumber() {
		rpcUtilService.getNextCheckNumber(depositInAccount.getID(),
				new AccounterAsyncCallback<Long>() {

					public void onException(AccounterException t) {
						checkNo.setValue(Accounter.constants().toBePrinted());
						return;
					}

					public void onResultSuccess(Long result) {
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
		Label lab1 = new Label(Accounter.messages().customerPrePayment(
				Global.get().Customer()));
		lab1.setStyleName(Accounter.constants().labelTitle());
		// lab1.setHeight("35px");
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
		customerCombo = createCustomerComboItem(messages.customerName(Global
				.get().customer()));

		billToCombo = createBillToComboItem(customerConstants.address());
		billToCombo.setDisabled(true);

		// Ending and Vendor Balance
		endBalText = new AmountField(customerConstants.endingBalance(), this);
		endBalText.setHelpInformation(true);
		endBalText.setWidth(100);
		endBalText.setDisabled(true);

		customerBalText = new AmountField(Accounter.messages().customerBalance(
				Global.get().Customer()), this);
		customerBalText.setHelpInformation(true);
		customerBalText.setDisabled(true);
		customerBalText.setWidth(100);

		DynamicForm balForm = new DynamicForm();
		balForm.setFields(endBalText, customerBalText);
		balForm.getCellFormatter().setWidth(0, 0, "205px");

		// payment
		depositInCombo = createDepositInComboItem();
		depositInCombo.setPopupWidth("500px");

		amountText = new AmountField(customerConstants.amount(), this);
		amountText.setHelpInformation(true);
		amountText.setWidth(100);
		amountText.setRequired(true);
		amountText.addBlurHandler(getBlurHandler());

		paymentMethodCombo = createPaymentMethodSelectItem();
		// paymentMethodCombo.setWidth(100);
		// paymentMethodCombo.setDefaultValue(UIUtils
		// .getpaymentMethodCheckBy_CompanyType(FinanceApplication
		// .constants().check()));
		paymentMethodCombo.setComboItem(UIUtils
				.getpaymentMethodCheckBy_CompanyType(Accounter.constants()
						.check()));
		printCheck = new CheckboxItem(customerConstants.toBePrinted());
		printCheck.setValue(true);
		printCheck.addChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				isChecked = (Boolean) event.getValue();
				if (isChecked) {
					if (printCheck.getValue().toString()
							.equalsIgnoreCase("true")) {
						checkNo.setValue(Accounter.constants().toBePrinted());
						checkNo.setDisabled(true);
					} else {
						if (depositInAccount == null)
							checkNo.setValue(Accounter.constants()
									.toBePrinted());
						else if (isInViewMode()) {
							checkNo.setValue(((ClientCustomerPrePayment) transaction)
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
		checkNo.setValue(Accounter.constants().toBePrinted());
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
		payForm.setFields(customerCombo, billToCombo, depositInCombo,
				amountText, paymentMethodCombo, printCheck, checkNo,
				memoTextAreaItem);
		// memo and Reference
		endBalText
				.setAmount(depositInCombo.getSelectedValue() != null ? depositInCombo
						.getSelectedValue().getCurrentBalance() : 0.00);

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

		this.add(mainVLay);

		setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(balForm);
		listforms.add(payForm);

	}

	private AddressCombo createBillToComboItem(String address) {
		AddressCombo addressCombo = new AddressCombo(Accounter.constants()
				.address(), false);
		addressCombo.setHelpInformation(true);
		addressCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAddress>() {

					public void selectedComboBoxItem(ClientAddress selectItem) {

						billToaddressSelected(selectItem);

					}

				});

		addressCombo.setDisabled(isInViewMode());

		// formItems.add(addressCombo);

		return addressCombo;

	}

	private TextItem createCheckNumberItem() {
		TextItem checkNoTextItem = new TextItem(
				UIUtils.getpaymentMethodCheckBy_CompanyType(Accounter
						.constants().check()) + " " + "No");
		checkNoTextItem.setHelpInformation(true);
		return checkNoTextItem;
	}

	@Override
	public void saveAndUpdateView() {

		updateTransaction();
		saveOrUpdate(transaction);
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
						Accounter.showError(Accounter.constants()
								.noNegativeAmounts());
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
			if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK ? paymentMethod
					.equalsIgnoreCase(Accounter.constants().cheque())
					: paymentMethod.equalsIgnoreCase(Accounter.constants()
							.check())) {

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
		this.setCustomer(customer);
		if (customer != null && customerCombo != null) {
			// AGAIN Search for ClientCustomer by customerId.
			// customerCombo.setComboItem(getCompany().getCustomer(
			// customer.getID()));
			customerCombo.setComboItem(customer);
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

		AccounterCoreType type = UIUtils.getAccounterCoreType(transaction
				.getType());
		this.rpcDoSerivce.canEdit(type, transaction.id, editCallBack);

	}

	protected void enableFormItems() {
		setMode(EditMode.EDIT);
		customerCombo.setDisabled(isInViewMode());
		transactionDateItem.setDisabled(isInViewMode());
		transactionNumber.setDisabled(isInViewMode());
		printCheck.setDisabled(isInViewMode());
		amountText.setDisabled(isInViewMode());
		paymentMethodCombo.setDisabled(isInViewMode());
		paymentMethodSelected(paymentMethodCombo.getSelectedValue());
		if (printCheck.getValue().toString().equalsIgnoreCase("true")) {
			checkNo.setValue(Accounter.constants().toBePrinted());
			checkNo.setDisabled(true);
		}
		memoTextAreaItem.setDisabled(false);
		if (locationTrackingEnabled)
			locationCombo.setDisabled(isInViewMode());
		super.onEdit();
	}

	public void setTransactionDate(ClientFinanceDate transactionDate) {
		super.setTransactionDate(transactionDate);
		if (this.transactionDateItem != null
				&& this.transactionDateItem.getValue() != null) {
			updateNonEditableItems();
		}
	}

	@Override
	public void updateNonEditableItems() {
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
		// NOTHING TO DO.
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

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

	@Override
	protected String getViewTitle() {
		return Accounter.messages().customerPayment(Global.get().Customer());
	}

	@Override
	protected void initTransactionsItems() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isBlankTransactionGrid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void addNewData(ClientTransactionItem transactionItem) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void refreshTransactionGrid() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<ClientTransactionItem> getAllTransactionItems() {
		return null;
	}
}
