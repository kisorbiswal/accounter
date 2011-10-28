package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

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
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientCustomerRefund;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PayFromAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.widgets.CurrencyWidget;

public class CustomerRefundView extends
		AbstractCustomerTransactionView<ClientCustomerRefund> {

	protected PayFromAccountsCombo payFromSelect;
	protected ClientAccount selectedAccount;
	protected AmountField amtText;
	private AmountField endBalText, custBalText;
	private TextItem checkNoText;
	private CheckboxItem printCheck;
	private TAXCodeCombo taxCodeSelect;

	private Double endingBalance;
	private boolean isChecked = false;
	private Double customerBalanceAmount;
	private String checkNumber;
	private ArrayList<DynamicForm> listforms;
	protected DynamicForm payForm;
	AccounterConstants accounterConstants = GWT
			.create(AccounterConstants.class);
	private boolean locationTrackingEnabled;
	private CurrencyWidget currencyWidget;

	public CustomerRefundView() {
		super(ClientTransaction.TYPE_CUSTOMER_REFUNDS);
		locationTrackingEnabled = getCompany().getPreferences()
				.isLocationTrackingEnabled();

	}

	public void initPayFromAccounts() {
		payFromSelect.setAccounts();

		if (transaction != null) {
			ClientAccount account = getCompany().getAccount(
					(transaction).getPayFrom());
			if (account != null) {
				payFromSelect.setComboItem(getCompany().getAccount(
						(transaction).getPayFrom()));
			}
		}

		selectedAccount = payFromSelect.getSelectedValue();

	}

	@Override
	protected void customerSelected(ClientCustomer customer) {
		if (customer == null)
			return;
		this.setCustomer(customer);
		if (customer != null && customerCombo != null) {
			customerCombo.setComboItem(getCompany().getCustomer(
					customer.getID()));
		}
		addressListOfCustomer = customer.getAddress();
		super.initBillToCombo();
		setCustomerBalance(customer.getBalance());
		refundAmountChanged(amtText.getAmount());
		long currency = customer.getCurrency();
		if (currency != 0) {
			ClientCurrency clientCurrency = getCompany().getCurrency(currency);
			if (clientCurrency != null) {
				currencyWidget.setSelectedCurrency(clientCurrency);
			}
		}
	}

	@Override
	protected void paymentMethodSelected(String paymentMethod) {
		if (paymentMethod == null)
			return;

		if (paymentMethod != null) {
			this.paymentMethod = paymentMethod;
			if (paymentMethod.equalsIgnoreCase(Accounter.constants().cheque())) {
				printCheck.setDisabled(false);
				checkNoText.setDisabled(false);
			} else {
				// paymentMethodCombo.setComboItem(paymentMethod);
				printCheck.setDisabled(true);
				checkNoText.setDisabled(true);
			}
		}

	}

	@Override
	protected void createControls() {

		Label lab1 = new Label(Accounter.messages().customerRefund(
				Global.get().Customer()));
		lab1.setStyleName(Accounter.constants().labelTitle());
		transactionDateItem = createTransactionDateItem();
		transactionNumber = createTransactionNumberItem();

		listforms = new ArrayList<DynamicForm>();
		locationCombo = createLocationCombo();
		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(6);
		dateNoForm.setStyleName("datenumber-panel");
		dateNoForm.setFields(transactionDateItem, transactionNumber);
		HorizontalPanel labeldateNoLayout = new HorizontalPanel();
		labeldateNoLayout.setWidth("100%");
		labeldateNoLayout.add(dateNoForm);
		labeldateNoLayout.setCellHorizontalAlignment(dateNoForm,
				HasHorizontalAlignment.ALIGN_RIGHT);
		labeldateNoLayout.getElement().getStyle().setPaddingRight(15, Unit.PX);

		HorizontalPanel totalLabel = new HorizontalPanel();
		totalLabel.setWidth("100%");
		totalLabel.add(labeldateNoLayout);
		totalLabel.setCellHorizontalAlignment(labeldateNoLayout, ALIGN_RIGHT);

		customerCombo = createCustomerComboItem(customerConstants.payTo());

		billToCombo = createBillToComboItem();
		billToCombo.setTitle(customerConstants.address());
		billToCombo.setDisabled(true);

		custForm = new DynamicForm();

		custForm.setWidth("100%");

		payFromSelect = new PayFromAccountsCombo(customerConstants.payFrom());
		payFromSelect.setHelpInformation(true);
		payFromSelect.setRequired(true);
		payFromSelect.setDisabled(isInViewMode());
		payFromSelect.setPopupWidth("500px");

		payFromSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {

						selectedAccount = selectItem;

						setEndingBalance(selectedAccount.getTotalBalance());
						refundAmountChanged(amtText.getAmount());
					}

				});

		amtText = new AmountField(customerConstants.amount(), this);
		amtText.setHelpInformation(true);
		amtText.setRequired(true);
		amtText.setWidth(100);
		amtText.setDisabled(isInViewMode());
		amtText.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				Double givenAmount = getAmountInBaseCurrency(amtText
						.getAmount());
				if (DecimalUtil.isLessThan(givenAmount, 0)) {
					addError(amtText, Accounter.constants().noNegativeAmounts());
					setRefundAmount(0.00D);

				}

				else if (!DecimalUtil.isLessThan(givenAmount, 0)) {
					try {
						if (!AccounterValidator.isAmountTooLarge(givenAmount))
							refundAmountChanged(givenAmount);
					} catch (InvalidEntryException e) {
					}

					setRefundAmount(givenAmount);

				}
			}
		});

		setRefundAmount(null);

		paymentMethodCombo = createPaymentMethodSelectItem();
		printCheck = new CheckboxItem(customerConstants.toBePrinted());
		printCheck.setValue(true);
		printCheck.setWidth(100);
		printCheck.addChangeHandler(new ValueChangeHandler<Boolean>() {

			public void onValueChange(ValueChangeEvent<Boolean> event) {
				isChecked = (Boolean) event.getValue();
				if (isChecked) {
					if (printCheck.getValue().toString().equalsIgnoreCase(
							"true")) {
						checkNoText.setValue(Accounter.constants()
								.toBePrinted());
						checkNoText.setDisabled(true);
					} else {
						if (payFromSelect.getValue() == null)
							checkNoText.setValue(Accounter.constants()
									.toBePrinted());
						else if (transaction != null) {
							checkNoText.setValue(transaction.getCheckNumber());
						}
					}
				} else
					checkNoText.setValue("");
				checkNoText.setDisabled(false);

			}
		});

		checkNoText = new TextItem(customerConstants.chequeNo());
		checkNoText.setValue(Accounter.constants().toBePrinted());
		checkNoText.setHelpInformation(true);
		checkNoText.setWidth(100);
		if (!paymentMethodCombo.getSelectedValue().equals(
				UIUtils.getpaymentMethodCheckBy_CompanyType(Accounter
						.constants().check())))
			checkNoText.setDisabled(true);
		checkNoText.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				checkNumber = checkNoText.getValue().toString();
			}
		});

		memoTextAreaItem = createMemoTextAreaItem();

		endBalText = new AmountField(customerConstants.endingBalance(), this);
		endBalText.setHelpInformation(true);
		endBalText.setDisabled(true);

		setEndingBalance(null);

		custBalText = new AmountField(Accounter.messages().customerBalance(
				Global.get().Customer()), this);
		custBalText.setHelpInformation(true);
		custBalText.setDisabled(true);
		setCustomerBalance(null);

		custForm.getCellFormatter().addStyleName(7, 0, "memoFormAlign");
		custForm.setFields(customerCombo, billToCombo, payFromSelect, amtText,
				paymentMethodCombo, printCheck, checkNoText, memoTextAreaItem);
		custForm.setCellSpacing(5);
		custForm.setWidth("100%");
		custForm.getCellFormatter().setWidth(0, 0, "160px");

		DynamicForm balForm = new DynamicForm();
		if (locationTrackingEnabled)
			balForm.setFields(locationCombo);
		balForm.setFields(endBalText, custBalText);
		balForm.getCellFormatter().setWidth(0, 0, "205px");

		if (getPreferences().isClassTrackingEnabled()
				&& getPreferences().isClassOnePerTransaction()) {
			classListCombo = createAccounterClassListCombo();
			balForm.setFields(classListCombo);
		}

		VerticalPanel leftPanel = new VerticalPanel();
		leftPanel.setWidth("100%");
		leftPanel.setSpacing(5);
		leftPanel.add(custForm);
		currencyWidget = createCurrencyWidget();
		VerticalPanel rightPanel = new VerticalPanel();
		rightPanel.setWidth("100%");
		rightPanel.add(balForm);
		rightPanel.setCellHorizontalAlignment(balForm,
				HasHorizontalAlignment.ALIGN_CENTER);
		if (isMultiCurrencyEnabled()) {
			rightPanel.add(currencyWidget);
			currencyWidget.setDisabled(isInViewMode());
		}
		HorizontalPanel hLay = new HorizontalPanel();
		hLay.setWidth("100%");
		hLay.setSpacing(10);
		hLay.add(leftPanel);
		hLay.setCellHorizontalAlignment(totalLabel, ALIGN_CENTER);
		hLay.add(rightPanel);
		hLay.setCellWidth(leftPanel, "50%");
		hLay.setCellWidth(rightPanel, "44%");

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setWidth("100%");
		mainVLay.add(lab1);
		mainVLay.add(totalLabel);
		mainVLay.add(hLay);

		this.add(mainVLay);

		setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(payForm);
		listforms.add(balForm);
		settabIndexes();

	}

	private void settabIndexes() {
		customerCombo.setTabIndex(1);
		billToCombo.setTabIndex(2);
		payFromSelect.setTabIndex(3);
		amtText.setTabIndex(4);
		paymentMethodCombo.setTabIndex(5);
		printCheck.setTabIndex(6);
		checkNoText.setTabIndex(7);
		memoTextAreaItem.setTabIndex(8);
		transactionDateItem.setTabIndex(9);
		transactionNumber.setTabIndex(10);
		endBalText.setTabIndex(11);
		custBalText.setTabIndex(12);
		saveAndCloseButton.setTabIndex(13);
		saveAndNewButton.setTabIndex(14);
		cancelButton.setTabIndex(15);

	}

	@Override
	public void saveAndUpdateView() {

		updateTransaction();

		saveOrUpdate((ClientCustomerRefund) transaction);

	}

	protected void updateTransaction() {
		super.updateTransaction();
		transaction.setDate(transactionDateItem.getEnteredDate().getDate());

		transaction.setNumber(transactionNumber.getValue().toString());
		if (customer != null)

			transaction.setPayTo(getCustomer().getID());

		if (billingAddress != null)
			transaction.setAddress(billingAddress);

		transaction.setEndingBalance(endingBalance);

		transaction.setCustomerBalance(customerBalanceAmount);
		if (selectedAccount != null)
			transaction.setPayFrom(selectedAccount.getID());

		transaction.setPaymentMethod(paymentMethod);
		if (checkNoText.getValue() != null
				&& !checkNoText.getValue().equals("")) {
			transaction.setCheckNumber(getCheckValue());
		} else
			transaction.setCheckNumber("");

		transaction.setIsToBePrinted(isChecked);
		transaction.setMemo(memoTextAreaItem.getValue().toString());

		transaction.setType(ClientTransaction.TYPE_CUSTOMER_REFUNDS);

		transaction.setTotal(getAmountInBaseCurrency(amtText.getAmount()));

		transaction.setBalanceDue(getAmountInBaseCurrency(amtText.getAmount()));
		if (currency != null)
			transaction.setCurrency(currency.getID());
		transaction.setCurrencyFactor(currencyWidget.getCurrencyFactor());
	}

	private String getCheckValue() {
		String value;
		if (!isInViewMode()) {
			if (checkNoText.getValue().equals(
					Accounter.constants().toBePrinted())) {
				value = String.valueOf(Accounter.constants().toBePrinted());

			} else
				value = String.valueOf(checkNoText.getValue());
		} else {
			String checknumber;
			checknumber = this.checkNumber;
			if (checknumber == null) {
				checknumber = Accounter.constants().toBePrinted();
			}
			if (checknumber.equals(Accounter.constants().toBePrinted()))
				value = Accounter.constants().toBePrinted();
			else
				value = String.valueOf(checknumber);
		}
		return value;
	}

	protected void refundAmountChanged(Double givenAmount) {
		if (selectedAccount != null) {
			endingBalance = selectedAccount.getTotalBalance();
			endingBalance -= givenAmount;
			setEndingBalance(endingBalance);
		}
		if (getCustomer() != null) {
			customerBalanceAmount = getCustomer().getBalance();
			customerBalanceAmount += givenAmount;
			setCustomerBalance(customerBalanceAmount);
		}

	}

	protected void setRefundAmount(Double amountValue) {
		if (amountValue == null)
			amountValue = 0.00D;
		amtText.setAmount(getAmountInTransactionCurrency(amountValue));
	}

	protected void setEndingBalance(Double totalBalance) {

		if (totalBalance == null)
			totalBalance = 0.0D;

		endBalText.setAmount(totalBalance);

		this.endingBalance = totalBalance;

	}

	@Override
	protected void initMemoAndReference() {
		if (transaction == null)
			return;

		String memo = ((ClientCustomerRefund) transaction).getMemo();

		if (memo != null) {
			memoTextAreaItem.setValue(memo);
		}

	}

	@Override
	protected void initSalesTaxNonEditableItem() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initTransactionTotalNonEditableItem() {

		if (transaction == null)
			return;

		ClientCustomerRefund customerRefund = ((ClientCustomerRefund) transaction);

		setCustomerBalance(customerRefund.getCustomerBalance());
		setEndingBalance(customerRefund.getEndingBalance());

	}

	@Override
	protected void initTransactionViewData() {
		if (transaction == null) {
			setData(new ClientCustomerRefund());
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
			this.setCustomer(getCompany().getCustomer(transaction.getPayTo()));
			customerSelected(getCompany().getCustomer(transaction.getPayTo()));

			amtText.setAmount(getAmountInTransactionCurrency(transaction
					.getTotal()));
			paymentMethodSelected(transaction.getPaymentMethod());
			if (transaction.getPaymentMethod().equals(constants.check())) {
				printCheck.setDisabled(isInViewMode());
				checkNoText.setDisabled(isInViewMode());
			} else {
				printCheck.setDisabled(true);
				checkNoText.setDisabled(true);
			}
			paymentMethodCombo.setValue(transaction.getPaymentMethod());

			if (transaction.getCheckNumber() != null) {
				if (transaction.getCheckNumber().equals(
						Accounter.constants().toBePrinted())) {
					checkNoText.setValue(Accounter.constants().toBePrinted());
					printCheck.setValue(true);
				} else {
					checkNoText.setValue(transaction.getCheckNumber());
					printCheck.setValue(false);
				}
			}
			this.selectedAccount = getCompany().getAccount(
					transaction.getPayFrom());
			if (selectedAccount != null)
				payFromSelect.setComboItem(selectedAccount);
			this.billingAddress = transaction.getAddress();
			if (billingAddress != null)
				billToaddressSelected(billingAddress);

			endBalText.setValue(DataUtils.getAmountAsString(transaction
					.getEndingBalance()));
			custBalText.setValue(DataUtils.getAmountAsString(transaction
					.getCustomerBalance()));
			memoTextAreaItem.setDisabled(true);
			memoTextAreaItem.setValue(transaction.getMemo());

			this.clientAccounterClass = transaction.getAccounterClass();
			if (getPreferences().isClassTrackingEnabled()
					&& this.clientAccounterClass != null
					&& classListCombo != null) {
				classListCombo.setComboItem(this.getClientAccounterClass());
			}
		}
		initRPCService();

		initTransactionNumber();
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));
		initCustomers();

		initPayFromAccounts();
	}

	private void initCustomers() {
		List<ClientCustomer> result = getCompany().getActiveCustomers();
		customerCombo.initCombo(result);
		customerCombo.setDisabled(isInViewMode());

	}

	public void setCustomerBalance(Double amount) {
		if (amount == null)
			amount = 0.0D;

		custBalText.setAmount(amount);

		this.customerBalanceAmount = amount;
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();
		if (isTrackTax()) {
			if (taxCodeSelect != null)
				if (!taxCodeSelect.validate()) {
					result.addError(taxCodeSelect, Accounter.messages()
							.pleaseEnter(taxCodeSelect.getTitle()));
				}
		}

		if (isBlankTransactionGrid()) {
			result.addError(null, accounterConstants.blankTransaction());
		}

		if (transaction.getTotal() <= 0) {
			amtText.highlight();
			result.addError(amtText, Accounter.messages()
					.valueCannotBe0orlessthan0(Accounter.constants().amount()));
		}
		if (!AccounterValidator.isValidCustomerRefundAmount(
				getAmountInBaseCurrency(amtText.getAmount()), payFromSelect
						.getSelectedValue())) {
			result.addWarning(amtText,
					AccounterWarningType.INVALID_CUSTOMERREFUND_AMOUNT);
		}
		return result;
	}

	@Override
	protected void priceLevelSelected(ClientPriceLevel priceLevel) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void salesPersonSelected(ClientSalesPerson person) {
		// TODO Auto-generated method stub

	}

	public List<DynamicForm> getForms() {
		// its not using any where
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
		this.rpcDoSerivce.canEdit(type, transaction.id, editCallBack);

	}

	protected void enableFormItems() {
		setMode(EditMode.EDIT);
		transactionDateItem.setDisabled(isInViewMode());
		transactionNumber.setDisabled(isInViewMode());
		customerCombo.setDisabled(isInViewMode());

		payFromSelect.setDisabled(isInViewMode());
		amtText.setDisabled(isInViewMode());
		memoTextAreaItem.setDisabled(isInViewMode());
		paymentMethodCombo.setDisabled(isInViewMode());
		paymentMethodSelected(transaction.getPaymentMethod());

		if (printCheck.getValue().toString().equalsIgnoreCase("true")) {
			checkNoText.setValue(Accounter.constants().toBePrinted());
		}
		if (locationTrackingEnabled)
			locationCombo.setDisabled(isInViewMode());
		if(currencyWidget !=null){
			currencyWidget.setDisabled(isInViewMode());
		}
		super.onEdit();
	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {
		// NOTHING TO DO
	}

	@Override
	protected void taxCodeSelected(ClientTAXCode taxCode) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		return Accounter.messages().customerRefund(Global.get().Customer());
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
		return Collections.emptyList();
	}

	@Override
	protected void addAccountTransactionItem(ClientTransactionItem item) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void addItemTransactionItem(ClientTransactionItem item) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAmountsFromGUI() {
		refundAmountChanged(amtText.getAmount());
	}
}
