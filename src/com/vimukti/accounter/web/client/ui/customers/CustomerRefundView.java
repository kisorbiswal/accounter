package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Label;
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
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PayFromAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class CustomerRefundView extends
		AbstractCustomerTransactionView<ClientCustomerRefund> {

	protected PayFromAccountsCombo payFromSelect;
	protected ClientAccount selectedAccount;
	protected AmountField amtText;
	private AmountField bankBalText, custBalText;
	private TextItem checkNoText;
	private CheckboxItem printCheck;
	private TAXCodeCombo taxCodeSelect;

	private Double endingBalance;
	private boolean isChecked = false;
	private String checkNumber;
	private ArrayList<DynamicForm> listforms;
	protected DynamicForm payForm;

	public CustomerRefundView() {
		super(ClientTransaction.TYPE_CUSTOMER_REFUNDS);

	}

	@Override
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

		// Job Tracking
		// Job Tracking
		if (getPreferences().isJobTrackingEnabled()) {
			jobListCombo.setValue("");
			jobListCombo.setCustomer(customer);
			jobListCombo.setEnabled(true);
		}
		ClientCurrency clientCurrency = getCurrency(customer.getCurrency());
		amtText.setCurrency(clientCurrency);
		bankBalText.setCurrency(clientCurrency);
		custBalText.setCurrency(clientCurrency);

		this.setCustomer(customer);
		if (customer != null && customerCombo != null) {
			customerCombo.setComboItem(getCompany().getCustomer(
					customer.getID()));
		}
		addressListOfCustomer = customer.getAddress();
		super.initBillToCombo();
		custBalText.setAmount(customer.getBalance());
		currencyWidget.setSelectedCurrencyFactorInWidget(clientCurrency,
				transactionDateItem.getDate().getDate());
	}

	@Override
	protected void paymentMethodSelected(String paymentMethod) {
		if (paymentMethod == null)
			return;

		if (paymentMethod != null) {
			this.paymentMethod = paymentMethod;
			if (paymentMethod.equalsIgnoreCase(messages.cheque())) {
				printCheck.setEnabled(true);
				checkNoText.setEnabled(true);
			} else {
				// paymentMethodCombo.setComboItem(paymentMethod);
				printCheck.setEnabled(false);
				checkNoText.setEnabled(false);
			}
		}

	}

	@Override
	protected void createControls() {

		Label lab1 = new Label(messages.customerRefund(Global.get().Customer()));
		lab1.setStyleName("label-title");
		transactionDateItem = createTransactionDateItem();
		transactionNumber = createTransactionNumberItem();

		listforms = new ArrayList<DynamicForm>();
		locationCombo = createLocationCombo();
		DynamicForm dateNoForm = new DynamicForm("datenumber-panel");
		dateNoForm.add(transactionDateItem, transactionNumber);
		StyledPanel labeldateNoLayout = new StyledPanel("labeldateNoLayout");
		// labeldateNoLayout.setWidth("100%");
		labeldateNoLayout.add(dateNoForm);
		// labeldateNoLayout.setCellHorizontalAlignment(dateNoForm,
		// HasHorizontalAlignment.ALIGN_RIGHT);
		labeldateNoLayout.getElement().getStyle().setPaddingRight(15, Unit.PX);

		StyledPanel totalLabel = new StyledPanel("totalLabel");
//		totalLabel.setWidth("100%");
		totalLabel.add(labeldateNoLayout);
		// totalLabel.setCellHorizontalAlignment(labeldateNoLayout,
		// ALIGN_RIGHT);

		customerCombo = createCustomerComboItem(messages.payTo());

		billToCombo = createBillToComboItem();
		billToCombo.setTitle(messages.address());
		billToCombo.setEnabled(false);

		custForm = new DynamicForm("custForm");

//		custForm.setWidth("100%");

		payFromSelect = new PayFromAccountsCombo(messages.payFrom());
		payFromSelect.setRequired(true);
		payFromSelect.setEnabled(!isInViewMode());
//		payFromSelect.setPopupWidth("500px");

		payFromSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {

						selectedAccount = selectItem;

						bankBalText.setAmount(selectedAccount
								.getTotalBalanceInAccountCurrency());
						bankBalText.setCurrency(getCompany().getCurrency(
								selectedAccount.getCurrency()));
					}

				});

		amtText = new AmountField(messages.amount(), this, getBaseCurrency(),
				"amtText");
		amtText.setRequired(true);
//		amtText.setWidth(100);
		amtText.setEnabled(!isInViewMode());
		amtText.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				Double givenAmount = amtText.getAmount();
				if (DecimalUtil.isLessThan(givenAmount, 0)) {
					addError(amtText, messages.noNegativeAmounts());
					setRefundAmount(0.00D);

				} else if (!DecimalUtil.isLessThan(givenAmount, 0)) {
					setRefundAmount(givenAmount);

				}
			}
		});

		setRefundAmount(null);

		paymentMethodCombo = createPaymentMethodSelectItem();
		printCheck = new CheckboxItem(messages.toBePrinted(), "printCheck");
		printCheck.setValue(true);
//		printCheck.setWidth(100);
		printCheck.addChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				isChecked = event.getValue();
				if (isChecked) {
					if (printCheck.getValue().toString()
							.equalsIgnoreCase("true")) {
						checkNoText.setValue(messages.toBePrinted());
						checkNoText.setEnabled(false);
					} else {
						if (payFromSelect.getValue() == null)
							checkNoText.setValue(messages.toBePrinted());
						else if (transaction != null) {
							checkNoText.setValue(transaction.getCheckNumber());
						}
					}
				} else
					checkNoText.setValue("");
				checkNoText.setEnabled(true);

			}
		});

		checkNoText = new TextItem(messages.chequeNo(), "checkNoText");
		checkNoText.setValue(messages.toBePrinted());
//		checkNoText.setWidth(100);
		if (!paymentMethodCombo.getSelectedValue().equals(
				UIUtils.getpaymentMethodCheckBy_CompanyType(messages.check())))
			checkNoText.setEnabled(false);
		checkNoText.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				checkNumber = checkNoText.getValue().toString();
			}
		});

		memoTextAreaItem = createMemoTextAreaItem();

		bankBalText = new AmountField(messages.bankBalance(), this,
				getBaseCurrency(), "bankBalText");
		bankBalText.setEnabled(false);

		custBalText = new AmountField(messages.payeeBalance(Global.get()
				.Customer()), this, getBaseCurrency(), "custBalText");
		custBalText.setEnabled(false);

		custForm.add(customerCombo, billToCombo, payFromSelect, amtText,
				paymentMethodCombo, printCheck, checkNoText, memoTextAreaItem);
//		custForm.setWidth("100%");
		// custForm.getCellFormatter().setWidth(0, 0, "160px");

		DynamicForm balForm = new DynamicForm("balForm");
		if (locationTrackingEnabled)
			balForm.add(locationCombo);
		balForm.add(bankBalText, custBalText);
		// balForm.getCellFormatter().setWidth(0, 0, "205px");

		if (getPreferences().isClassTrackingEnabled()
				&& getPreferences().isClassOnePerTransaction()) {
			classListCombo = createAccounterClassListCombo();
			balForm.add(classListCombo);
		}
		jobListCombo = createJobListCombo();
		if (getPreferences().isJobTrackingEnabled()) {
			jobListCombo.setEnabled(false);
			balForm.add(jobListCombo);
		}

		StyledPanel leftPanel = new StyledPanel("leftPanel");
		leftPanel.add(custForm);
		currencyWidget = createCurrencyFactorWidget();
		StyledPanel rightPanel = new StyledPanel("rightPanel");
		rightPanel.add(balForm);
		if (isMultiCurrencyEnabled()) {
			rightPanel.add(currencyWidget);
			currencyWidget.setEnabled(!isInViewMode());
		}
		StyledPanel hLay = new StyledPanel("hLay");
		hLay.addStyleName("fields-panel");
		// hLay.setSpacing(10);
		hLay.add(leftPanel);
		// hLay.setCellHorizontalAlignment(totalLabel, ALIGN_CENTER);
		hLay.add(rightPanel);
		// hLay.setCellWidth(leftPanel, "50%");
		// hLay.setCellWidth(rightPanel, "50%");

		StyledPanel mainVLay = new StyledPanel("rightPanel");
		mainVLay.add(lab1);
		mainVLay.add(voidedPanel);
		mainVLay.add(totalLabel);
		mainVLay.add(hLay);

		this.add(mainVLay);

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
		bankBalText.setTabIndex(11);
		custBalText.setTabIndex(12);
		if (saveAndCloseButton != null)
			saveAndCloseButton.setTabIndex(13);
		if (saveAndNewButton != null)
			saveAndNewButton.setTabIndex(14);
		cancelButton.setTabIndex(15);

	}

	@Override
	public void saveAndUpdateView() {

		updateTransaction();

		saveOrUpdate(transaction);

	}

	@Override
	public ClientCustomerRefund saveView() {
		ClientCustomerRefund saveView = super.saveView();
		if (saveView != null) {
			updateTransaction();
		}
		return saveView;
	}

	@Override
	protected void updateTransaction() {
		super.updateTransaction();
		transaction.setDate(transactionDateItem.getEnteredDate().getDate());

		transaction.setNumber(transactionNumber.getValue().toString());
		if (customer != null)

			transaction.setPayTo(getCustomer().getID());

		if (billingAddress != null)
			transaction.setAddress(billingAddress);

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

		transaction.setTotal(amtText.getAmount());

		transaction.setBalanceDue(amtText.getAmount());
		if (getPreferences().isJobTrackingEnabled()) {
			if (jobListCombo.getSelectedValue() != null)
				transaction.setJob(jobListCombo.getSelectedValue().getID());
		}
		if (currency != null)
			transaction.setCurrency(currency.getID());
		transaction.setCurrencyFactor(currencyWidget.getCurrencyFactor());
	}

	private String getCheckValue() {
		String value;
		if (!isInViewMode()) {
			if (checkNoText.getValue().equals(messages.toBePrinted())) {
				value = String.valueOf(messages.toBePrinted());

			} else
				value = String.valueOf(checkNoText.getValue());
		} else {
			String checknumber;
			checknumber = this.checkNumber;
			if (checknumber == null) {
				checknumber = messages.toBePrinted();
			}
			if (checknumber.equals(messages.toBePrinted()))
				value = messages.toBePrinted();
			else
				value = String.valueOf(checknumber);
		}
		return value;
	}

	protected void setRefundAmount(Double amountValue) {
		if (amountValue == null)
			amountValue = 0.00D;
		amtText.setAmount(amountValue);
	}

	@Override
	protected void initMemoAndReference() {
		if (transaction == null)
			return;

		String memo = transaction.getMemo();

		if (memo != null) {
			memoTextAreaItem.setValue(memo);
			memoTextAreaItem.setDisabled(isInViewMode());
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
				currencyWidget.setEnabled(!isInViewMode());
			}
			this.setCustomer(getCompany().getCustomer(transaction.getPayTo()));
			customerSelected(getCompany().getCustomer(transaction.getPayTo()));

			amtText.setAmount(transaction.getTotal());
			paymentMethodSelected(transaction.getPaymentMethod());
			if (transaction.getPaymentMethod().equals(messages.check())) {
				printCheck.setEnabled(!isInViewMode());
				checkNoText.setEnabled(!isInViewMode());
			} else {
				printCheck.setEnabled(false);
				checkNoText.setEnabled(false);
			}
			paymentMethodCombo.setValue(transaction.getPaymentMethod());

			if (transaction.getCheckNumber() != null) {
				if (transaction.getCheckNumber().equals(messages.toBePrinted())) {
					checkNoText.setValue(messages.toBePrinted());
					printCheck.setValue(true);
				} else {
					checkNoText.setValue(transaction.getCheckNumber());
					printCheck.setValue(false);
				}
			}
			this.selectedAccount = getCompany().getAccount(
					transaction.getPayFrom());
			if (selectedAccount != null) {
				payFromSelect.setComboItem(selectedAccount);
				bankBalText.setAmount(selectedAccount
						.getTotalBalanceInAccountCurrency());
				bankBalText.setCurrency(getCompany().getCurrency(
						selectedAccount.getCurrency()));

			}
			this.billingAddress = transaction.getAddress();
			if (billingAddress != null)
				billToaddressSelected(billingAddress);

			if (customer != null) {
				custBalText.setAmount(customer.getBalance());
			}

			memoTextAreaItem.setDisabled(true);
			memoTextAreaItem.setValue(transaction.getMemo());

			this.clientAccounterClass = getCompany().getAccounterClass(
					transaction.getAccounterClass());
			if (getPreferences().isClassTrackingEnabled()
					&& this.clientAccounterClass != null
					&& classListCombo != null) {
				classListCombo.setComboItem(this.getClientAccounterClass());
			}
			if (getPreferences().isJobTrackingEnabled()) {
				jobSelected(Accounter.getCompany().getjob(transaction.getJob()));
				jobListCombo.setEnabled(false);
			}
		}
		initRPCService();

		initTransactionNumber();
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));
		initCustomers();

		initPayFromAccounts();
		if (isMultiCurrencyEnabled()) {
			updateAmountsFromGUI();
		}
	}

	private void initCustomers() {
		List<ClientCustomer> result = getCompany().getActiveCustomers();
		customerCombo.initCombo(result);
		customerCombo.setEnabled(!isInViewMode());

	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();
		if (isTrackTax()) {
			if (taxCodeSelect != null)
				if (!taxCodeSelect.validate()) {
					result.addError(taxCodeSelect,
							messages.pleaseEnter(taxCodeSelect.getTitle()));
				}
		}

		if (isBlankTransactionGrid()) {
			result.addError(null, messages.blankTransaction());
		}

		if (transaction.getTotal() <= 0) {
			amtText.highlight();
			result.addError(amtText,
					messages.valueCannotBe0orlessthan0(messages.amount()));
		}
		if (!AccounterValidator.isValidCustomerRefundAmount(
				amtText.getAmount(), payFromSelect.getSelectedValue())) {
			result.addWarning(amtText, messages.W_109());
		}
		ClientAccount bankAccount = payFromSelect.getSelectedValue();
		// check if the currency of accounts is valid or not
		if (bankAccount != null) {
			ClientCurrency bankCurrency = getCurrency(bankAccount.getCurrency());
			if (bankCurrency != getBaseCurrency() && bankCurrency != currency) {
				result.addError(payFromCombo,
						messages.selectProperBankAccount());
			}
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

	@Override
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
		transactionDateItem.setEnabled(!isInViewMode());
		transactionNumber.setEnabled(!isInViewMode());
		customerCombo.setEnabled(!isInViewMode());

		payFromSelect.setEnabled(!isInViewMode());
		amtText.setEnabled(!isInViewMode());
		memoTextAreaItem.setEnabled(!isInViewMode());
		paymentMethodCombo.setEnabled(!isInViewMode());
		paymentMethodSelected(transaction.getPaymentMethod());

		if (printCheck.getValue().toString().equalsIgnoreCase("true")) {
			checkNoText.setValue(messages.toBePrinted());
		}
		if (locationTrackingEnabled)
			locationCombo.setEnabled(!isInViewMode());
		if (currencyWidget != null) {
			currencyWidget.setEnabled(!isInViewMode());
		}
		if (!currencyWidget.isShowFactorField()) {
			currencyWidget.setEnabled(!isInViewMode());
		}

		super.onEdit();
		jobListCombo.setEnabled(!isInViewMode());
		if (customer != null) {
			jobListCombo.setCustomer(customer);
		}
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
		return messages.customerRefund(Global.get().Customer());
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
		// refundAmountChanged(amtText.getAmount());
	}

	protected void updateDiscountValues() {
		// TODO Auto-generated method stub

	}
}
