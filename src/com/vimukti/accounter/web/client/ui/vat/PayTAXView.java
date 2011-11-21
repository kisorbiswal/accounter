package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

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
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PayFromAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXAgencyCombo;
import com.vimukti.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.EditMode;
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
	protected double initialEndingBalance;
	protected ClientTAXAgency selectedTAXAgency;
	private VerticalPanel gridLayout;
	private TransactionPayTAXGrid grid;
	private Double totalAmount = 0.0D;
	private String transactionNumber;
	private double endingBalance;
	private ClientFinanceDate dueDateOnOrBefore;
	private TextItem transNumber;

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
		lab.setStyleName(messages.labelTitle());
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

						initialEndingBalance = !DecimalUtil.isEquals(
								selectedPayFromAccount.getTotalBalance(), 0) ? selectedPayFromAccount
								.getTotalBalance() : 0D;

						calculateEndingBalance();

					}

				});

		payFromAccCombo.setDisabled(isInViewMode());
		payFromAccCombo.setPopupWidth("500px");
		paymentMethodCombo = createPaymentMethodSelectItem();
		paymentMethodCombo.setRequired(true);
		// paymentMethodCombo.setWidth(100);

		billsDue = new DateField(messages.returnsDueOnOrBefore());
		billsDue.setHelpInformation(true);
		billsDue.setTitle(messages.returnsDueOnOrBefore());
		billsDue.setDisabled(isInViewMode());

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
		balForm.setFields(amountText, endingBalanceText);
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
		initialEndingBalance = selectedPayFromAccount == null ? 0D
				: !DecimalUtil.isEquals(
						selectedPayFromAccount.getTotalBalance(), 0) ? selectedPayFromAccount
						.getTotalBalance() : 0D;

		calculateEndingBalance();
		settabIndexes();

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

	private void calculateEndingBalance() {

		if (selectedPayFromAccount != null) {

			if (selectedPayFromAccount.isIncrease())
				endingBalance = initialEndingBalance + totalAmount;
			else
				endingBalance = initialEndingBalance - totalAmount;

			endingBalanceText.setValue(DataUtils
					.getAmountAsString(endingBalance));

		}
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

		endingBalanceText.setAmount(getAmountInTransactionCurrency(transaction
				.getEndingBalance()));
		paymentMethodCombo.setComboItem(transaction.getPaymentMethod());
		amountText.setAmount(getAmountInTransactionCurrency(transaction
				.getTotal()));
		if (selectedPayFromAccount != null) {
			amountText.setCurrency(getCurrency(selectedPayFromAccount
					.getCurrency()));
			endingBalanceText.setCurrency(getCurrency(selectedPayFromAccount
					.getCurrency()));
		}
		List<ClientTransactionPayTAX> list = transaction.getTransactionPayTax();
		int count = 0;
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

	protected void initTransactionNumber() {

		rpcUtilService.getNextTransactionNumber(ClientTransaction.TYPE_PAY_TAX,
				new AccounterAsyncCallback<String>() {

					public void onException(AccounterException caught) {
						Accounter.showError(messages
								.failedToGetTransactionNumber());
					}

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
	public void saveAndUpdateView() {
		updateTransaction();
		super.saveAndUpdateView();
		saveOrUpdate(transaction);
	}

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

		if (billsDue.getValue() != null) {
			transaction.setBillsDueOnOrBefore((billsDue.getValue()).getDate());
		}
		if (selectedTAXAgency != null) {
			transaction.setTaxAgency(selectedTAXAgency.getID());
		}
		transaction.setTotal(totalAmount);
		transaction.setEndingBalance(endingBalance);
		if (currency != null) {
			transaction.setCurrency(currency.getID());
		}
		transaction.setCurrencyFactor(currencyWidget.getCurrencyFactor());
		transaction.setTransactionPayTax(getTransactionPayVATList());
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
		amountText.setAmount(getAmountInTransactionCurrency(toBeSetAmount));
		totalAmount = toBeSetAmount;
		if (selectedPayFromAccount != null) {
			double toBeSetEndingBalance = 0.0;
			if (selectedPayFromAccount.isIncrease())
				toBeSetEndingBalance = selectedPayFromAccount.getTotalBalance()
						+ DataUtils.getBalance(
								getAmountInBaseCurrency(amountText.getAmount())
										.toString()).doubleValue();
			else
				toBeSetEndingBalance = selectedPayFromAccount.getTotalBalance()
						- DataUtils.getBalance(
								getAmountInBaseCurrency(amountText.getAmount())
										.toString()).doubleValue();
			endingBalanceText
					.setAmount(getAmountInTransactionCurrency(toBeSetEndingBalance));
			// }
		}

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

	public void onEdit() {
		super.onEdit();
		paymentMethodCombo.setDisabled(false);
		billsDue.setDisabled(false);
		payFromAccCombo.setDisabled(false);
		grid.setCanEdit(true);
		grid.setDisabled(false);

	}

	private void enableFormItems() {
		setMode(EditMode.EDIT);
		date.setDisabled(isInViewMode());
		paymentMethodCombo.setDisabled(isInViewMode());
		billsDue.setDisabled(isInViewMode());
		taxAgencyCombo.setDisabled(isInViewMode());
		payFromAccCombo.setDisabled(isInViewMode());
		super.onEdit();

		// fillGrid();
		transaction = null;

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

}
