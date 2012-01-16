package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayTAX;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTAXReturn;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionPayTAX;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PayFromAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXAgencyCombo;
import com.vimukti.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.TransactionPayTAXGrid;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

public class PayTAXView extends AbstractTransactionBaseView<ClientPayTAX> {

	private ArrayList<DynamicForm> listforms;
	private DateField date;
	private PayFromAccountsCombo payFromAccCombo;
	private DateField billsDue;
	private TAXAgencyCombo taxAgencyCombo;
	private DynamicForm mainform;
	private AmountField amountText;
	private AmountField endingBalanceText;
	private DynamicForm balForm;
	protected ClientAccount selectedPayFromAccount;
	protected ClientTAXAgency selectedTAXAgency;
	private VerticalPanel gridLayout;
	private TransactionPayTAXGrid grid;
	private Double totalAmount = 0.0D;
	private String transactionNumber;
	private ClientFinanceDate dueDateOnOrBefore;
	private TextItem transNumber;
	private TextItem checkNoText;
	private CheckboxItem printCheck;
	private boolean isChecked = false;

	List<ClientTransactionPayTAX> records = new ArrayList<ClientTransactionPayTAX>();

	public PayTAXView() {
		super(ClientTransaction.TYPE_PAY_TAX);
	}

	@Override
	protected void createControls() {
		listforms = new ArrayList<DynamicForm>();
		currencyWidget = createCurrencyFactorWidget();
		// setTitle(UIUtils.title(FinanceApplication.constants()
		// .transaction()));

		Label lab = new Label(messages.payTax());
		lab.removeStyleName("gwt-Label");
		lab.setStyleName("label-title");
		// lab.setHeight("35px");
		transactionDateItem = createTransactionDateItem();

		transNumber = createTransactionNumberItem();
		transNumber.setTitle(messages.no());
		transNumber.setToolTip(messages
				.giveNoTo(this.getAction().getViewName()));

		payFromAccCombo = new PayFromAccountsCombo(messages.payFrom());
		payFromAccCombo.setHelpInformation(true);
		payFromAccCombo.setAccountTypes(UIUtils
				.getOptionsByType(AccountCombo.PAY_FROM_COMBO));
		payFromAccCombo.setRequired(true);
		payFromAccCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectedPayFromAccount = selectItem;
						selectedAccount(selectItem);
						ClientCurrency currency = getCurrency(selectItem
								.getCurrency());
						amountText.setCurrency(currency);
						endingBalanceText.setCurrency(currency);
						// initialEndingBalance = selectedPayFromAccount
						// .getTotalBalance() != 0 ? selectedPayFromAccount
						// .getTotalBalance()
						// : 0D;

						endingBalanceText.setAmount(selectedPayFromAccount
								.getTotalBalanceInAccountCurrency());

					}

				});

		payFromAccCombo.setDisabled(isInViewMode());
		payFromAccCombo.setPopupWidth("500px");
		paymentMethodCombo = createPaymentMethodSelectItem();
		paymentMethodCombo.setRequired(true);
		// paymentMethodCombo.setWidth(100);

		printCheck = new CheckboxItem(messages.toBePrinted());
		printCheck.setValue(true);
		printCheck.setWidth(100);
		printCheck.setDisabled(true);
		printCheck.addChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				isChecked = event.getValue();
				if (isChecked) {
					if (printCheck.getValue().toString()
							.equalsIgnoreCase("true")) {
						checkNoText.setValue(messages.toBePrinted());
						checkNoText.setDisabled(true);
					} else {
						if (payFromCombo.getValue() == null)
							checkNoText.setValue(messages.toBePrinted());
						else if (transaction != null) {
							checkNoText.setValue(transaction.getCheckNumber());
						}
					}
				} else
					checkNoText.setValue("");
				checkNoText.setDisabled(false);

			}
		});

		checkNoText = new TextItem(messages.chequeNo());
		checkNoText.setValue(messages.toBePrinted());
		checkNoText.setHelpInformation(true);
		checkNoText.setWidth(100);
		if (paymentMethodCombo.getSelectedValue() != null
				&& !paymentMethodCombo.getSelectedValue().equals(
						UIUtils.getpaymentMethodCheckBy_CompanyType(messages
								.check())))
			checkNoText.setDisabled(true);
		checkNoText.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				checkNumber = checkNoText.getValue().toString();
			}
		});

		billsDue = new DateField(messages.returnsDueOnOrBefore());
		billsDue.setHelpInformation(true);
		billsDue.setTitle(messages.returnsDueOnOrBefore());
		billsDue.setDisabled(isInViewMode());
		billsDue.setEnteredDate(new ClientFinanceDate());

		billsDue.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate date) {
				if (transaction == null) {
					dueDateOnOrBefore = date;
					filterGridByDueDate();
				}
			}
		});

		taxAgencyCombo = new TAXAgencyCombo(messages.taxAgency(), false);
		taxAgencyCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXAgency>() {

					@Override
					public void selectedComboBoxItem(ClientTAXAgency selectItem) {
						selectedTAXAgency = selectItem;
						filterlistbyTAXAgency(selectItem);

					}
				});

		DynamicForm dateForm = new DynamicForm();
		dateForm.setNumCols(4);
		dateForm.setStyleName("datenumber-panel");
		dateForm.setFields(transactionDateItem, transNumber);
		HorizontalPanel datepanel = new HorizontalPanel();
		datepanel.setWidth("100%");
		datepanel.add(dateForm);
		datepanel.setCellHorizontalAlignment(dateForm, ALIGN_RIGHT);

		mainform = new DynamicForm();
		// filterForm.setWidth("100%");
		mainform = UIUtils.form(messages.filter());
		mainform.setFields(payFromAccCombo, paymentMethodCombo, billsDue,
				taxAgencyCombo);
		// mainform.setWidth("80%");

		// fileterForm = new DynamicForm();
		// fileterForm.setFields(billsDue);
		// fileterForm.setWidth("80%");

		amountText = new AmountField(messages.amount(), this, getBaseCurrency());
		amountText.setHelpInformation(true);
		amountText.setValue("" + UIUtils.getCurrencySymbol() + " 0.00");
		amountText.setDisabled(true);

		endingBalanceText = new AmountField(messages.bankBalance(), this,
				getBaseCurrency());
		endingBalanceText.setHelpInformation(true);
		endingBalanceText.setValue("" + UIUtils.getCurrencySymbol() + " 0.00");
		endingBalanceText.setDisabled(true);

		balForm = new DynamicForm();
		balForm = UIUtils.form(messages.balances());
		balForm.setFields(amountText, endingBalanceText, printCheck,
				checkNoText);
		// balForm.getCellFormatter().setWidth(0, 0, "197px");

		if (getPreferences().isClassTrackingEnabled()
				&& getPreferences().isClassOnePerTransaction()) {
			classListCombo = createAccounterClassListCombo();
			balForm.setFields(classListCombo);
		}

		VerticalPanel leftVLay = new VerticalPanel();

		leftVLay.setWidth("100%");
		leftVLay.add(mainform);

		VerticalPanel rightVlay = new VerticalPanel();
		rightVlay.add(balForm);
		rightVlay.setCellHorizontalAlignment(balForm, ALIGN_RIGHT);
		if (isMultiCurrencyEnabled()) {
			rightVlay.add(currencyWidget);
			rightVlay.setCellHorizontalAlignment(currencyWidget,
					HasHorizontalAlignment.ALIGN_RIGHT);
			currencyWidget.setDisabled(isInViewMode());
		}
		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.addStyleName("fields-panel");
		topHLay.setWidth("100%");
		topHLay.setSpacing(10);
		topHLay.add(leftVLay);
		topHLay.add(rightVlay);
		topHLay.setCellWidth(leftVLay, "50%");
		topHLay.setCellWidth(rightVlay, "50%");
		topHLay.setCellHorizontalAlignment(rightVlay, ALIGN_RIGHT);

		Label lab1 = new Label("" + messages.billsToPay() + "");

		initListGrid();

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(lab);
		mainVLay.add(voidedPanel);
		mainVLay.add(datepanel);
		mainVLay.add(topHLay);
		mainVLay.add(lab1);
		mainVLay.add(gridLayout);
		this.add(mainVLay);
		setSize("100%", "100%");
		/* Adding dynamic forms in list */
		listforms.add(mainform);
		listforms.add(balForm);

		selectedPayFromAccount = payFromAccCombo.getSelectedValue();
		if (selectedPayFromAccount != null) {
			endingBalanceText.setAmount(selectedPayFromAccount
					.getTotalBalanceInAccountCurrency());
		}
		settabIndexes();

	}

	@Override
	protected void paymentMethodSelected(String paymentMethod) {
		if (paymentMethod == null)
			return;

		if (paymentMethod != null) {
			this.paymentMethod = paymentMethod;
			if (paymentMethod.equalsIgnoreCase(messages.cheque())) {
				printCheck.setDisabled(false);
				checkNoText.setDisabled(false);
			} else {
				// paymentMethodCombo.setComboItem(paymentMethod);
				printCheck.setDisabled(true);
				checkNoText.setDisabled(true);
			}
		}

	}

	/**
	 * 
	 * @param selectItem
	 */
	private void selectedAccount(ClientAccount selectItem) {
		ClientCurrency accountAccount = getCurrency(selectItem.getCurrency());
		if (accountAccount != null && accountAccount.getID() != 0) {
			currencyWidget.setSelectedCurrency(accountAccount);
		} else {
			currencyWidget.setSelectedCurrency(getBaseCurrency());
		}

		if (isMultiCurrencyEnabled()) {
			super.setCurrency(currency);
			setCurrencyFactor(1.0);
		}
	}

	private void settabIndexes() {

		payFromAccCombo.setTabIndex(1);
		paymentMethodCombo.setTabIndex(2);
		billsDue.setTabIndex(3);
		transactionDateItem.setTabIndex(4);
		transNumber.setTabIndex(5);
		amountText.setTabIndex(6);
		endingBalanceText.setTabIndex(7);
		currencyWidget.setTabIndex(8);
		saveAndCloseButton.setTabIndex(9);
		saveAndNewButton.setTabIndex(10);
		cancelButton.setTabIndex(11);
	}

	protected void filterGridByDueDate() {
		List<ClientTransactionPayTAX> filterList = new ArrayList<ClientTransactionPayTAX>();

		if (dueDateOnOrBefore != null) {
			for (ClientTransactionPayTAX cont : records) {
				ClientTAXReturn clientVATReturn = Accounter.getCompany()
						.getVatReturn(cont.getTAXReturn());
				ClientFinanceDate date = new ClientFinanceDate(
						clientVATReturn.getPeriodEndDate());
				if (date.equals(dueDateOnOrBefore)
						|| date.before(dueDateOnOrBefore))
					filterList.add(cont);
			}
		}

		loadData(filterList);
		int size = grid.getRecords().size();
		if (size == 0)
			grid.addEmptyMessage(messages.noRecordsToShow());
	}

	protected void filterlistbyTAXAgency(ClientTAXAgency selectedVATAgency) {

		List<ClientTransactionPayTAX> filterRecords = new ArrayList<ClientTransactionPayTAX>();
		for (ClientTransactionPayTAX tpt : records) {
			if (tpt.getTaxAgency() == selectedVATAgency.getID()) {
				filterRecords.add(tpt);
			}
		}

		loadData(filterRecords);
	}

	// initializes the grid.
	private void initListGrid() {

		gridLayout = new VerticalPanel();
		gridLayout.setWidth("100%");
		grid = new TransactionPayTAXGrid(true, true);
		grid.setCanEdit(!isInViewMode());
		grid.isEnable = false;
		grid.init();
		grid.setPayVATView(this);
		grid.setDisabled(isInViewMode());
		// grid.setHeight("200px");
		if (!isInViewMode()) {
			// grid.addFooterValue("Total", 1);
			// grid
			// .updateFooterValues(DataUtils
			// .getAmountAsString(totalAmount), 2);
		}
		gridLayout.add(grid);

	}

	@Override
	protected void initTransactionViewData() {

		if (transaction == null) {
			setData(new ClientPayTAX());
			initTransactionNumber();
			fillGrid();
			initPayFromAccounts();
			return;
		}

		if (isMultiCurrencyEnabled()) {
			if (transaction.getCurrency() > 0) {
				this.currency = getCompany().getCurrency(
						transaction.getCurrency());
			} else {
				this.currency = getCompany().getPreferences()
						.getPrimaryCurrency();
			}
			this.currencyFactor = transaction.getCurrencyFactor();
			if (this.currency != null) {
				currencyWidget.setSelectedCurrency(this.currency);
			}
			currencyWidget.setCurrencyFactor(transaction.getCurrencyFactor());
			currencyWidget.setDisabled(isInViewMode());
		}
		initAccounterClass();
		selectedPayFromAccount = getCompany().getAccount(
				transaction.getPayFrom());

		payFromAccCombo.setComboItem(selectedPayFromAccount);
		selectedTAXAgency = getCompany().getTaxAgency(
				transaction.getTaxAgency());
		if (selectedTAXAgency != null)
			taxAgencyCombo.setComboItem(selectedTAXAgency);

		if (isInViewMode()) {
			taxAgencyCombo.setDisabled(true);
			grid.setDisabled(true);

		}
		billsDue.setEnteredDate(new ClientFinanceDate(transaction
				.getBillsDueOnOrBefore()));
		transactionDateItem.setEnteredDate(transaction.getDate());
		transNumber.setValue(transaction.getNumber());

		if (selectedPayFromAccount != null) {
			endingBalanceText.setAmount(selectedPayFromAccount
					.getTotalBalanceInAccountCurrency());
		}
		paymentMethodCombo.setComboItem(transaction.getPaymentMethod());
		if (transaction.getPaymentMethod().equals(messages.check())) {
			printCheck.setDisabled(isInViewMode());
			checkNoText.setDisabled(isInViewMode());
		} else {
			printCheck.setDisabled(true);
			checkNoText.setDisabled(true);
		}

		if (transaction.getCheckNumber() != null) {
			if (transaction.getCheckNumber().equals(messages.toBePrinted())) {
				checkNoText.setValue(messages.toBePrinted());
				printCheck.setValue(true);
			} else {
				checkNoText.setValue(transaction.getCheckNumber());
				printCheck.setValue(false);
			}
		}

		amountText.setAmount(transaction.getTotal());
		if (selectedPayFromAccount != null) {
			amountText.setCurrency(getCurrency(selectedPayFromAccount
					.getCurrency()));
			endingBalanceText.setCurrency(getCurrency(selectedPayFromAccount
					.getCurrency()));
		}
		List<ClientTransactionPayTAX> list = transaction.getTransactionPayTax();
		int count = 0;
		grid.removeAllRecords();
		for (ClientTransactionPayTAX record : list) {
			if (record != null) {
				grid.addData(record);
				grid.selectRow(count);
				records.add(record);
				count++;
			}
		}

		// grid.updateFooterValues("Total"
		// + DataUtils.getAmountAsString(transaction.getTotal()), 2);

	}

	@Override
	protected void initPayFromAccounts() {
		// getPayFromAccounts();
		// payFromCombo.initCombo(payFromAccounts);
		// payFromCombo.setAccountTypes(UIUtils
		// .getOptionsByType(AccountCombo.payFromCombo));
		payFromAccCombo.setAccounts();
		// payFromCombo.setDisabled(isEdit);
		ClientAccount payFromAccount = payFromAccCombo.getSelectedValue();
		if (payFromAccount != null)
			payFromAccCombo.setComboItem(payFromAccount);
	}

	private void fillGrid() {
		// grid.addLoadingImagePanel();
		rpcUtilService
				.getPayTAXEntries(new AccounterAsyncCallback<List<ClientTransactionPayTAX>>() {

					@Override
					public void onException(AccounterException caught) {
						Accounter.showError(messages
								.failedtogettheTransactionPayVATList());
						grid.addEmptyMessage(messages.noFiledTaxEntriesToPay());

					}

					@Override
					public void onResultSuccess(
							List<ClientTransactionPayTAX> result) {
						if (result == null || result.isEmpty()) {
							grid.addEmptyMessage(messages
									.noFiledTaxEntriesToPay());
							return;
						}
						records = result;
						loadData(records);
					}
				});

	}

	// fills the list grid with data.
	protected void loadData(List<ClientTransactionPayTAX> result) {
		grid.removeAllRecords();
		if (result == null || result.isEmpty()) {
			grid.addEmptyMessage(messages.noFiledTaxEntriesToPay());
		} else {
			grid.setRecords(result);
		}
	}

	@Override
	protected void initTransactionNumber() {

		rpcUtilService.getNextTransactionNumber(ClientTransaction.TYPE_PAY_TAX,
				new AccounterAsyncCallback<String>() {

					@Override
					public void onException(AccounterException caught) {
						Accounter.showError(messages
								.failedToGetTransactionNumber());
					}

					@Override
					public void onResultSuccess(String result) {
						if (result == null)
							onFailure(null);
						transactionNumber = result;
						transNumber.setValue(result);
					}

				});

	}

	@Override
	public ValidationResult validate() {

		ValidationResult result = new ValidationResult();

		// Validations
		// 1. is valid transaction?
		// 2. is in prevent posting before date?
		// 3. main form valid?
		// 4. is blank transaction?
		// 5. grid valid?
		// 6. is positive amount?
		// if (!AccounterValidator.isValidTransactionDate(this.transactionDate))
		// {
		// result.addError(transactionDate,
		// messages.invalidateTransactionDate());
		// }

		if (AccounterValidator.isInPreventPostingBeforeDate(transactionDate)) {
			result.addError(transactionDate, messages.invalidateDate());
		}
		result.add(mainform.validate());

		if (grid == null || grid.getRecords().isEmpty()) {
			result.addError(grid,
					messages.youdonthaveanyfiledVATentriestoselect());
		} else {
			result.add(grid.validateGrid());
		}

		if (taxAgencyCombo != null) {
			if (taxAgencyCombo.getSelectedValue() == null) {
				result.addError(taxAgencyCombo,
						messages.pleaseSelectTAXAgencyToPayTAX());
			}
		}
		// ClientAccount bankAccount = payFromAccCombo.getSelectedValue();
		// // check if the currency of accounts is valid or not
		// if (bankAccount != null) {
		// ClientCurrency bankCurrency = getCurrency(bankAccount.getCurrency());
		// if (bankCurrency != getBaseCurrency() && bankCurrency != currency) {
		// result.addError(payFromAccCombo, messages
		// .selectProperBankAccount());
		// }
		// }
		return result;
	}

	@Override
	public ClientPayTAX saveView() {
		ClientPayTAX saveView = super.saveView();
		if (saveView != null) {
			updateTransaction();
		}
		return saveView;
	}

	@Override
	public void saveAndUpdateView() {
		updateTransaction();
		super.saveAndUpdateView();
		saveOrUpdate(transaction);
	}

	@Override
	protected void updateTransaction() {
		super.updateTransaction();
		transaction.setNumber(transactionNumber);
		transaction.setType(ClientTransaction.TYPE_PAY_TAX);

		if (transactionDateItem.getEnteredDate() != null)
			transaction.setDate(transactionDateItem.getEnteredDate().getDate());

		if (selectedPayFromAccount != null) {
			transaction.setPayFrom(selectedPayFromAccount.getID());
		}
		transaction.setPaymentMethod(paymentMethod);

		if (checkNoText.getValue() != null
				&& !checkNoText.getValue().equals("")) {
			transaction.setCheckNumber(getCheckValue());
		} else
			transaction.setCheckNumber("");

		// transaction.setIsToBePrinted(isChecked);

		if (billsDue.getValue() != null) {
			transaction.setBillsDueOnOrBefore((billsDue.getValue()).getDate());
		}
		if (selectedTAXAgency != null) {
			transaction.setTaxAgency(selectedTAXAgency.getID());
		}
		transaction.setTotal(totalAmount);
		if (currency != null) {
			transaction.setCurrency(currency.getID());
		}
		transaction.setCurrencyFactor(currencyWidget.getCurrencyFactor());
		transaction.setTransactionPayTax(getTransactionPayVATList());
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

	private List<ClientTransactionPayTAX> getTransactionPayVATList() {

		// List<ClientTransactionPayVAT> payVATList = new
		// ArrayList<ClientTransactionPayVAT>();
		//
		List<ClientTransactionPayTAX> selectedRecords = grid
				.getSelectedRecords();
		for (ClientTransactionPayTAX rec : selectedRecords) {
			rec.setID(0);
		}

		return selectedRecords;
	}

	/*
	 * This method invoked eachtime when there is a change in records and it
	 * updates the noneditable amount fields
	 */
	public void adjustAmountAndEndingBalance(double toBeSetAmount) {
		// List<ClientTransactionPayVAT> selectedRecords = grid
		// .getSelectedRecords();
		// double toBeSetAmount = 0.0;
		// for (ClientTransactionPayVAT rec : selectedRecords) {
		// toBeSetAmount += rec.getAmountToPay();
		// }
		// if (this.transaction == null) {
		amountText.setAmount(toBeSetAmount);
		totalAmount = toBeSetAmount;

	}

	@Override
	public void updateNonEditableItems() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<DynamicForm> getForms() {
		// NOTHING TO DO.
		return null;
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void onEdit() {
		super.onEdit();
		enableFormItems();
	}

	private void enableFormItems() {
		setMode(EditMode.EDIT);
		paymentMethodCombo.setDisabled(isInViewMode());
		billsDue.setDisabled(isInViewMode());
		taxAgencyCombo.setDisabled(isInViewMode());
		payFromAccCombo.setDisabled(isInViewMode());
		grid.setCanEdit(true);
		grid.setDisabled(false);
		if (printCheck.getValue().toString().equalsIgnoreCase("true")) {
			checkNoText.setValue(messages.toBePrinted());
		}
		if (isMultiCurrencyEnabled()) {
			currencyWidget.setDisabled(isInViewMode());
		}
		super.onEdit();

		// fillGrid();
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
	protected String getViewTitle() {
		return messages.payTax();
	}

	@Override
	protected void refreshTransactionGrid() {

	}

	@Override
	public void setFocus() {
		this.payFromAccCombo.setFocus();

	}

	@Override
	public void updateAmountsFromGUI() {

	}

	@Override
	protected boolean canRecur() {
		return false;
	}

	@Override
	protected void updateDiscountValues() {
		// TODO Auto-generated method stub

	}

}
