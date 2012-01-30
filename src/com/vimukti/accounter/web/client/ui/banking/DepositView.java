package com.vimukti.accounter.web.client.ui.banking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AddNewButton;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientLocation;
import com.vimukti.accounter.web.client.core.ClientMakeDeposit;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionDepositItem;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.edittable.tables.TransactionDepositTable;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

public class DepositView extends AbstractTransactionBaseView<ClientMakeDeposit> {

	private VerticalPanel mainPanel, leftPanel;
	private AccountCombo depositToCombo;
	private DynamicForm depositToForm;
	private TransactionDepositTable transactionDepositTable;
	private boolean locationTrackingEnabled;
	private AddNewButton depositTableButton;
	private DynamicForm totalForm = new DynamicForm();
	private AmountLabel totalLabel;
	private ArrayList<DynamicForm> listforms;
	private List<ClientTransactionDepositItem> transactionDepositItems;
	private List<ClientAccount> deposiInAccounts;

	private DepositView() {
		super(ClientTransaction.TYPE_MAKE_DEPOSIT);

	}

	public static DepositView getInstance() {
		return new DepositView();
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	protected void initMemoAndReference() {
		memoTextAreaItem.setDisabled(true);
		setMemoTextAreaItem(transaction.getMemo());
	}

	@Override
	public void init() {
		super.init();
		initTransactionNumber();
	}

	@Override
	protected void initTransactionViewData() {
		if (transaction == null) {
			setData(new ClientMakeDeposit());
		} else {
			ClientAccount depositTo = getCompany().getAccount(
					transaction.getDepositTo());
			if (currencyWidget != null) {
				if (transaction.getCurrency() > 1) {
					this.currency = getCompany().getCurrency(
							transaction.getCurrency());
				} else {
					this.currency = getCompany().getPrimaryCurrency();
				}
				this.currencyFactor = transaction.getCurrencyFactor();
				if (this.currency != null) {
					currencyWidget.setSelectedCurrency(this.currency);
				}
				// currencyWidget.currencyChanged(this.currency);
				currencyWidget.setCurrencyFactor(transaction
						.getCurrencyFactor());
				currencyWidget.setDisabled(isInViewMode());
			}

			if (depositTo != null) {
				depositToCombo.setSelected(depositTo.getName());
			}

			transactionDateItem.setValue(transaction.getDate());
			transactionNumber.setValue(transaction.getNumber());

			paymentMethodCombo.setValue(transaction.getPaymentMethod());

			if (locationCombo != null) {
				ClientLocation location = getCompany().getLocation(
						transaction.getLocation());
				if (location != null) {
					locationCombo.setSelected(location.getName());
				}
			}
			transactionDepositItems = transaction.getTransactionDepositItems();
			if (transactionDepositItems != null
					&& !transactionDepositItems.isEmpty()) {
				transactionDepositTable.setRecords(transactionDepositItems);
			}
			foreignCurrencyamountLabel.setAmount(transaction.getTotal());
			totalLabel
					.setAmount(getAmountInBaseCurrency(transaction.getTotal()));
		}
		if (isMultiCurrencyEnabled()) {
			updateAmountsFromGUI();
		}
	}

	@Override
	protected void createControls() {
		Label lab = new Label(messages.makeDeposit());
		lab.setStyleName("label-title");

		locationTrackingEnabled = getCompany().getPreferences()
				.isLocationTrackingEnabled();

		mainPanel = new VerticalPanel();
		mainPanel.setSize("100%", "100%");

		depositToForm = new DynamicForm();
		leftPanel = new VerticalPanel();

		depositToCombo = new AccountCombo(messages.depositTo()) {

			@Override
			protected List<ClientAccount> getAccounts() {
				deposiInAccounts = new ArrayList<ClientAccount>();
				for (ClientAccount account : getCompany().getActiveAccounts()) {
					if (Arrays.asList(ClientAccount.SUBBASETYPE_CURRENT_ASSET,
							ClientAccount.SUBBASETYPE_CURRENT_LIABILITY,
							ClientAccount.SUBBASETYPE_EQUITY).contains(
							account.getSubBaseType())) {

						deposiInAccounts.add(account);
					}
				}
				return deposiInAccounts;

			}
		};
		depositToCombo.setDisabled(this.isInViewMode());
		depositToCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						accountSelected(selectItem);
					}
				});

		depositToForm.setFields(depositToCombo);

		transactionDateItem = createTransactionDateItem();
		transactionDateItem.setTitle(messages.billDate());
		transactionDateItem
				.addDateValueChangeHandler(new DateValueChangeHandler() {

					@Override
					public void onDateValueChange(ClientFinanceDate date) {
						if (date != null) {
							setTransactionDate(date);
						}
					}
				});
		transactionNumber = createTransactionNumberItem();
		transactionNumber.setTitle(messages.no());

		paymentMethodCombo = new SelectCombo(messages.paymentMethod());
		paymentMethodCombo.setDisabled(this.isInViewMode());

		String listString[] = new String[] { messages.cash(),
				messages.creditCard(), messages.directDebit(),
				messages.masterCard(), messages.onlineBanking(),
				messages.standingOrder(), messages.switchMaestro() };
		List<String> selectedComboList = new ArrayList<String>();
		for (int i = 0; i < listString.length; i++) {
			selectedComboList.add(listString[i]);
		}
		paymentMethodCombo.initCombo(selectedComboList);

		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(6);
		dateNoForm.setStyleName("datenumber-panel");
		dateNoForm.setFields(transactionDateItem, transactionNumber);
		HorizontalPanel datepanel = new HorizontalPanel();
		datepanel.setWidth("100%");
		datepanel.add(dateNoForm);
		datepanel.setCellHorizontalAlignment(dateNoForm,
				HasHorizontalAlignment.ALIGN_RIGHT);
		datepanel.getElement().getStyle().setPaddingRight(25, Unit.PX);

		locationCombo = createLocationCombo();
		locationCombo.setHelpInformation(true);

		if (locationTrackingEnabled)
			depositToForm.setFields(locationCombo);
		depositToForm.setItems(paymentMethodCombo);

		leftPanel.add(depositToForm);
		currencyWidget = createCurrencyFactorWidget();
		if (isMultiCurrencyEnabled()) {
			leftPanel.add(currencyWidget);
			currencyWidget.setDisabled(isInViewMode());
		}

		transactionDepositTable = new TransactionDepositTable(
				isCustomerAllowedToAdd(), this) {

			@Override
			protected boolean isInViewMode() {
				return DepositView.this.isInViewMode();
			}

			@Override
			protected void updateNonEditableItems() {
				DepositView.this.updateNonEditableItems();
			}
		};

		depositTableButton = new AddNewButton();
		depositTableButton.setEnabled(!isInViewMode());
		depositTableButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addAccount();
			}
		});

		FlowPanel accountFlowPanel = new FlowPanel();
		accountFlowPanel.add(transactionDepositTable);
		accountFlowPanel.add(depositTableButton);

		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setDisabled(this.isInViewMode());

		DynamicForm memoForm = new DynamicForm();
		memoForm.setWidth("100%");
		memoForm.setFields(memoTextAreaItem);
		memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");

		totalLabel = new AmountLabel(
				messages.currencyTotal(currency != null ? currency
						.getFormalName() : getCompany().getPrimaryCurrency()
						.getFormalName()));
		foreignCurrencyamountLabel = createForeignCurrencyAmountLable(getCompany()
				.getPrimaryCurrency());

		totalForm.setWidth("100%");
		totalForm.setStyleName("boldtext");
		totalForm.setFields(totalLabel);
		if (isMultiCurrencyEnabled()) {
			totalForm.setFields(foreignCurrencyamountLabel);
		}

		transactionDepositTable.setDisabled(this.isInViewMode());

		HorizontalPanel bottomLayout = new HorizontalPanel();
		bottomLayout.setWidth("100%");

		VerticalPanel bottompanel = new VerticalPanel();
		bottompanel.setWidth("100%");

		bottomLayout.add(memoForm);
		bottomLayout.add(totalForm);
		bottomLayout.setCellWidth(totalForm, "30%");

		bottompanel.add(bottomLayout);

		mainPanel.setSize("100%", "100%");
		mainPanel.add(lab);
		mainPanel.add(voidedPanel);
		mainPanel.add(datepanel);
		mainPanel.add(leftPanel);
		mainPanel.add(accountFlowPanel);
		mainPanel.add(bottompanel);

		listforms = new ArrayList<DynamicForm>();
		listforms.add(depositToForm);
		listforms.add(memoForm);
		listforms.add(dateNoForm);
		listforms.add(totalForm);

		if (isMultiCurrencyEnabled()) {
			foreignCurrencyamountLabel.hide();
		}

		this.add(mainPanel);
	}

	@Override
	protected void accountSelected(ClientAccount account) {
		long currency = account.getCurrency();
		ClientCurrency clientCurrency = getCompany().getPrimaryCurrency();
		if (currency != clientCurrency.getID()) {
			clientCurrency = getCompany().getCurrency(currency);

			currencyWidget.setSelectedCurrencyFactorInWidget(clientCurrency,
					transactionDateItem.getDate().getDate());
		} else {

			if (clientCurrency != null) {
				currencyWidget
						.setSelectedCurrencyFactorInWidget(clientCurrency,
								transactionDateItem.getDate().getDate());
			}
		}
		if (isMultiCurrencyEnabled()) {
			DepositView.this.setCurrency(clientCurrency);
			setCurrencyFactor(currencyWidget.getCurrencyFactor());
			updateAmountsFromGUI();
		}
		transactionDepositTable.resetRecords();
		updateNonEditableItems();
	}

	@Override
	public void saveAndUpdateView() {
		updateTransaction();
		super.saveAndUpdateView();
		saveOrUpdate(transaction);
	}

	@Override
	protected void updateTransaction() {
		if (transaction == null)
			return;
		super.updateTransaction();

		transactionDepositItems = new ArrayList<ClientTransactionDepositItem>();

		List<ClientTransactionDepositItem> list = getAllTransactionDepositItems();
		if (list != null) {
			for (ClientTransactionDepositItem item : list) {
				if (!item.isEmpty()) {
					transactionDepositItems.add(item);
				}
			}
			transaction.setTransactionDepositItems(transactionDepositItems);
		}

		// Setting Payee
		ClientAccount depositTo = depositToCombo.getSelectedValue();
		if (depositTo != null)
			transaction.setDepositTo(depositTo.getID());

		// Setting Total
		transaction.setTotal(transactionDepositTable.getLineTotal());

		// Setting Memo
		transaction.setMemo(getMemoTextAreaItem());

		transaction.setPaymentMethod(paymentMethodCombo.getSelectedValue());

		if (currency != null)
			transaction.setCurrency(currency.getID());
		transaction.setCurrencyFactor(currencyWidget.getCurrencyFactor());

	}

	public List<ClientTransactionDepositItem> getAllTransactionDepositItems() {
		return transactionDepositTable.getRecords();
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();
		ClientAccount selectedDepositInAccount = depositToCombo
				.getSelectedValue();
		if (selectedDepositInAccount == null) {
			result.addError(depositToCombo,
					messages.pleaseSelect(messages.depositTo()));
			return result;
		}
		if (transactionDepositItems != null
				&& transactionDepositItems.size() != 0) {
			for (ClientTransactionDepositItem transactionItem : transactionDepositItems) {
				long selectedDepositFromAccount = transactionItem.getAccount();
				if (selectedDepositInAccount.getID() == selectedDepositFromAccount) {
					result.addError(depositToCombo, messages
							.dipositAccountAndTransferAccountShouldBeDiff());
					return result;
				}

				ClientAccount fromAccount = getCompany().getAccount(
						selectedDepositFromAccount);
				if (fromAccount == null) {
					result.addError(depositToCombo,
							messages.pleaseEnter(messages.depositFrom()));
					return result;
				}
				ClientPayee payee = getCompany().getPayee(
						transactionItem.getReceivedFrom());
				if (selectedDepositInAccount.getCurrency() != fromAccount
						.getCurrency()
						|| (payee != null && selectedDepositInAccount
								.getCurrency() != payee.getCurrency())) {
					result.addError(depositToCombo,
							messages.mustBeSameCurrency());
					return result;
				}

				if (transactionItem != null) {
					if (transactionItem.getTotal() != null) {
						if (transactionItem.getTotal() <= 0) {
							result.addError(
									"TransactionItem"
											+ transactionItem.getAccount()
											+ transactionItem.getAccount(),
									messages.pleaseEnterAccAndAmount());
						}
					} else {
						result.addError("TransactionItem",
								messages.pleaseEnterAccAndAmount());
					}
				} else {
					result.addError("TransactionItem",
							messages.pleaseEnter(messages.transactionItem()));
				}
				if (getPreferences().isClassTrackingEnabled()
						&& !getPreferences().isClassOnePerTransaction()
						&& getPreferences().isWarnOnEmptyClass()
						&& transactionItem.getAccounterClass() == null) {
					// TODO
				}
			}
		} else {
			result.addError("TransactionItem",
					messages.pleaseEnterAccAndAmount());
		}

		if (AccounterValidator.isInPreventPostingBeforeDate(transactionDate)) {
			result.addError(transactionDate, messages.invalidateDate());
		}
		result.add(depositToForm.validate());

		if (transactionDepositTable.getAllRows().isEmpty()) {
			result.addError(transactionDepositTable,
					messages.blankTransaction());
		} else {
			result.add(transactionDepositTable.validateGrid());
		}
		return result;
	}

	private boolean isCustomerAllowedToAdd() {
		if (transaction != null) {
			List<ClientTransactionItem> transactionItems = transaction
					.getTransactionItems();
			for (ClientTransactionItem clientTransactionItem : transactionItems) {
				if (clientTransactionItem.isBillable()
						|| clientTransactionItem.getCustomer() != 0) {
					return true;
				}
			}
			List<ClientTransactionDepositItem> transactionDepositItems2 = transaction
					.getTransactionDepositItems();
			for (ClientTransactionDepositItem clientTransactionDepositItem : transactionDepositItems2) {
				if (clientTransactionDepositItem.isBillable()
						|| clientTransactionDepositItem.getCustomer() != 0) {
					return true;
				}
			}
		}
		if (getPreferences().isBillableExpsesEnbldForProductandServices()
				&& getPreferences()
						.isProductandSerivesTrackingByCustomerEnabled()) {
			return true;
		}
		return false;
	}

	@Override
	public void updateNonEditableItems() {
		if (transactionDepositTable == null || transactionDepositTable == null) {
			return;
		}
		double lineTotal = transactionDepositTable.getLineTotal();
		totalLabel.setAmount(getAmountInBaseCurrency(lineTotal));
		foreignCurrencyamountLabel.setAmount(lineTotal);
		if (currency != null) {
			transaction.setCurrency(currency.getID());
		}
	}

	@Override
	protected void refreshTransactionGrid() {
		transactionDepositTable.updateTotals();
	}

	@Override
	public void updateAmountsFromGUI() {
		modifyForeignCurrencyTotalWidget();
		transactionDepositTable.updateAmountsFromGUI();
	}

	public void modifyForeignCurrencyTotalWidget() {
		String formalName = currencyWidget.getSelectedCurrency()
				.getFormalName();
		if (currencyWidget.isShowFactorField()) {
			foreignCurrencyamountLabel.hide();
		} else {
			foreignCurrencyamountLabel.show();
			foreignCurrencyamountLabel.setTitle(messages
					.currencyTotal(formalName));
		}
	}

	@Override
	protected String getViewTitle() {
		return messages.deposit();
	}

	@Override
	public List<DynamicForm> getForms() {
		return listforms;
	}

	@Override
	public void setFocus() {
		depositToCombo.setFocus();
	}

	@Override
	protected void updateDiscountValues() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEdit() {

		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				int errorCode = caught.getErrorCode();
				String errorString = null;
				if (errorCode != 0) {
					errorString = AccounterExceptions.getErrorString(errorCode);
				} else {
					errorString = caught.getMessage();
				}
				Accounter.showError(errorString);
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
		depositToCombo.setDisabled(this.isInViewMode());
		paymentMethodCombo.setDisabled(this.isInViewMode());
		depositTableButton.setEnabled(!isInViewMode());
		memoTextAreaItem.setDisabled(this.isInViewMode());
		transactionDepositTable.setDisabled(this.isInViewMode());
		currencyWidget.setDisabled(this.isInViewMode());
		super.onEdit();
	}

	@Override
	protected boolean canVoid() {
		return false;
	}

	@Override
	protected void addAccount() {
		ClientTransactionDepositItem transactionItem = new ClientTransactionDepositItem();

		transactionDepositTable.add(transactionItem);

	}

	@Override
	public String getTitle() {
		return messages.makeDeposit();
	}
}