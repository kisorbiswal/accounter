package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AddButton;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientMakeDeposit;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientTransactionMakeDeposit;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.banking.AbstractBankTransactionView;
import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
import com.vimukti.accounter.web.client.ui.combo.CashBackAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
import com.vimukti.accounter.web.client.ui.combo.CustomerCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.MakeDepositAccountCombo;
import com.vimukti.accounter.web.client.ui.combo.OtherAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.combo.VendorCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.edittable.tables.MakeDepositTransactionTable;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

public class MakeDepositView extends
		AbstractBankTransactionView<ClientMakeDeposit> {
	DateItem date;

	DynamicForm depoForm;
	TextAreaItem memoText;

	AmountField cashBackAmountText;
	AmountLabel totText;
	TAXCodeCombo taxCodeSelect;
	TextItem cashBackMemoText, totAmtText;
	DynamicForm memoForm, totForm;
	DynamicForm form1, form2;

	private MakeDepositTransactionTable gridView;

	protected ClientTransactionMakeDeposit currentRecord;
	protected boolean isClose;

	private int TYPE_FINANCIAL_ACCOUNT = 1;
	private int TYPE_VENDOR = 2;
	private int TYPE_CUSTOMER = 3;

	private MakeDepositAccountCombo depositInSelect;
	private CashBackAccountsCombo cashBackAccountSelect;
	private OtherAccountsCombo financeAccountSelect;
	private CustomerCombo customerSelect;
	private CustomCombo<ClientVendor> vendorSelect;

	private ClientAccount selectedDepositInAccount;
	private ClientAccount selectedCashBackAccount;

	private String selectedItemId;

	// private ClientAccount selectedAccount;

	protected ClientCustomer customer;

	protected ClientVendor vendor;

	protected Long nextTransactionNumber;

	protected Long TransactionNumber;

	private Double calculatedTotal;

	private boolean isListEmpty;

	protected boolean isSelected;
	// private MakeDeposit transactionObject;

	private String transactionNumber;

	private List<ClientAccount> listOfAccounts;

	private HorizontalPanel bot1Panel;

	private ArrayList<DynamicForm> listforms;

	private AddButton addButton;
	private TextItem transNumber;

	// private VerticalPanel botRightPanel;

	com.vimukti.accounter.web.client.externalization.AccounterConstants accounterConstants = Accounter
			.constants();

	private boolean locationTrackingEnabled;

	public MakeDepositView() {
		super(ClientTransaction.TYPE_MAKE_DEPOSIT);
		calculatedTotal = 0D;
		locationTrackingEnabled = getCompany().getPreferences()
				.isLocationTrackingEnabled();
	}

	private void setTransactionNumberToMakeDepositObject() {
		AccounterAsyncCallback<String> getTransactionNumberCallback = new AccounterAsyncCallback<String>() {

			public void onException(AccounterException caught) {

			}

			public void onResultSuccess(String result) {

				if (result != null)
					setTransactionNumber(result);
				else
					onFailure(new Exception());

			}

		};

		Accounter.createHomeService().getNextTransactionNumber(
				ClientTransaction.TYPE_MAKE_DEPOSIT,
				getTransactionNumberCallback);
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	private void addTransactionMakeDepositsToGrid(
			List<ClientTransactionMakeDeposit> transactionMakeDepositList) {

		ClientCompany company = getCompany();

		ClientTransactionMakeDeposit records[] = new ClientTransactionMakeDeposit[transactionMakeDepositList
				.size()];

		Iterator<ClientTransactionMakeDeposit> it = transactionMakeDepositList
				.iterator();
		int i = 0;

		while (it.hasNext()) {
			ClientTransactionMakeDeposit entry = it.next();
			// For avoiding the deletion of transaction items before clicking on
			// the edit button
			entry.setIsNewEntry(false);
			records[i] = new ClientTransactionMakeDeposit();
			// records[i].setAttribute(ATTR_CHECK, true);
			records[i].setDate(entry.getDate());
			if (entry.getNumber() != null)
				records[i].setNumber(entry.getNumber());
			if (entry.getPaymentMethod() != null)
				records[i].setPaymentMethod(entry.getPaymentMethod());

			if (entry.getType() == ClientTransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT) {
				records[i].setType(TYPE_FINANCIAL_ACCOUNT);
				records[i].setAccount(entry.getAccount());
			} else if (entry.getType() == ClientTransactionMakeDeposit.TYPE_VENDOR) {
				if (entry.getVendor() != 0) {
					records[i].setType(TYPE_VENDOR);
					records[i].setVendor(entry.getVendor());
				}
			} else {
				if (entry.getCustomer() != 0) {
					records[i].setType(TYPE_CUSTOMER);

					records[i].setCustomer(entry.getCustomer());
				}

			}

			if (entry.getReference() != null)
				records[i].setReference(entry.getReference());

			records[i].setAmount(entry.getAmount());
			// records[i++].setEnabled(false);

		}
	}

	protected void validateCashBackAmount() {

		String amount = cashBackAmountText.getAmount().toString();
		if (amount.substring(0, 1)
				.equals("" + UIUtils.getCurrencySymbol() + "")) {
			amount = amount.substring(1);
		}
		try {
			Double cashBackAmount = Double.parseDouble(amount);
			if (DecimalUtil.isEquals(cashBackAmount, 0.00)) {

				if (DecimalUtil.isLessThan(cashBackAmount, 0.00)
						|| DecimalUtil.isGreaterThan(cashBackAmount,
								calculatedTotal)) {
					Accounter.showError(Accounter.constants()
							.cashBackAmountErrorMsg());
					cashBackAmount = 0.00;
					// cashBackAmountText.setValue("$0.00");
					cashBackAmountText.setAmount(0.00);
				} /*
				 * else if (cashBackAmount > 1000000000000.00) { SC.say(
				 * "Cash-back Amount should not exceed +UIUtils.getCurrencySymbol() +"
				 * 1,000,000,000,000.00" ); cashBackAmount = 0.00;
				 * cashBackAmountText.setValue(""+UIUtils.getCurrencySymbol()
				 * +"0.00"); }
				 */

			}

			Double diff = calculatedTotal.doubleValue()
					- cashBackAmount.doubleValue();
			// cashBackAmountText.setValue(UIUtils.format(cashBackAmount));
			cashBackAmountText.setAmount(cashBackAmount);
			// totText.setValue(UIUtils.format(diff));
			totText.setAmount(diff);
		} catch (Exception e) {
			Accounter.showError(Accounter.constants().enterValidAmount());
			// cashBackAmountText.setValue("$0.00");
			cashBackAmountText.setAmount(0.00);

		}

	}

	private void addTracsactionMakeDepositsToGrid() {

		gridView.addLoadingImagePanel();

		AccounterAsyncCallback<List<ClientTransactionMakeDeposit>> callback = new AccounterAsyncCallback<List<ClientTransactionMakeDeposit>>() {

			public void onException(AccounterException caught) {
				Accounter.showError(Accounter.constants()
						.makeDepostTransationsListFailed());
				gridView.removeAllRecords();
			}

			public void onResultSuccess(
					List<ClientTransactionMakeDeposit> result) {
				if (result == null) {
					onFailure(null);
					return;
				}

				if (result.size() != 0) {
					gridView.removeAllRecords();
					gridView.setRecords(result);

				} else if (!isInViewMode()) {
					gridView.removeAllRecords();
					Accounter.showError(Accounter.constants()
							.noDepositsToShow());
					// gridView.addEmptyMessage("No records to show");
				}

			}

		};

		Accounter.createHomeService().getTransactionMakeDeposits(callback);

	}

	// public void initVendorCombo() {
	// List<ClientVendor> result = getCompany().getActiveVendors();
	// if (result != null) {
	// allVendors = result;
	// vendorSelect.initCombo(result);
	//
	// }
	//
	// }
	//
	// private void initCustomerCombo() {
	// List<ClientCustomer> result = getCompany().getActiveCustomers();
	// if (result != null) {
	// allCustomers = result;
	// customerSelect.initCombo(result);
	// }
	// }

	public void getDepositInAccounts() {
		listOfAccounts = depositInSelect.getAccounts();

		depositInSelect.initCombo(listOfAccounts);

	}

	// protected boolean validateForm() {
	// boolean flag = true;
	// // if (UIUtils.unFormat(UIUtils.toStr(cashBackAmountText.getValue())) !=
	// // 0.00
	// // && selectedCashBackAccount == null) {
	// if (!DecimalUtil.isEquals(cashBackAmountText.getAmount(), 0.00)
	// && selectedCashBackAccount == null) {
	//
	// flag = false;
	// Accounter.showError(Accounter.constants()
	// .cashBackAccountShouldBeSelected());
	//
	// }
	//
	// return checkTotalAmount() && checkLastRecord() && flag
	// && depoForm.validate(false);
	// }

	private boolean checkTotalAmount() {

		// if (UIUtils.unFormat(UIUtils.toStr(cashBackAmountText.getValue())) ==
		// 0.00)
		if (!DecimalUtil.isEquals(cashBackAmountText.getAmount(), 0.00))
			return true;

		// if (UIUtils.unFormat(UIUtils.toStr(cashBackAmountText.getValue())) >
		// calculatedTotal) {
		if (DecimalUtil.isGreaterThan(cashBackAmountText.getAmount(),
				calculatedTotal)) {
			Accounter.showError(Accounter.constants()
					.cashBackAmountShouldnotBeGreaterthanDepositedAmount());
			return false;
		}
		return true;
	}

	private boolean checkLastRecord() {

		Object records[] = gridView.getRecords().toArray();
		ClientTransactionMakeDeposit rec = (ClientTransactionMakeDeposit) records[records.length - 1];
		// FIXME-- check the condition,there is no possiblity of type/account to
		// be '0'
		if (rec.getType() == 0 || (rec.getAccount() == 0)) {
			Accounter.showError(Accounter.messages().pleaseChooseAnAccount(
					Global.get().account()));
			return false;
		}
		return true;
	}

	public void initListGrid() {
		gridView = new MakeDepositTransactionTable() {

			@Override
			protected void updateNonEditableItems() {
				MakeDepositView.this.updateNonEditableItems();
			}
		};
		// gridView.setHeight("250px");
		gridView.setDisabled(isInViewMode());
		gridView.getElement().getStyle().setMarginTop(10, Unit.PX);
	}

	protected void validateAmountField(
			ClientTransactionMakeDeposit selectedRecord) {

		String amount = selectedRecord.getAmount() + "";
		// if (amount.substring(0, 1).equals("$")) {
		// amount = amount.substring(1);
		// }
		try {

			Double enteredAmount = DataUtils.getAmountStringAsDouble(amount);

			if (DecimalUtil.isLessThan(enteredAmount, -1000000000000.00)
					|| DecimalUtil.isGreaterThan(enteredAmount,
							1000000000000.00)) {
				Accounter.showError(Accounter.constants()
						.amountExceedsTheLimit());
				enteredAmount = 0.00;
			} else
				selectedRecord.setAmount(enteredAmount);
		} catch (Exception e) {
			Accounter.showError(Accounter.constants().invalidAmount());
			selectedRecord.setAmount(0.00);
		}

	}

	protected void updateTotalAmount() {

		calculatedTotal = 0D;
		for (ClientTransactionMakeDeposit rec : gridView.getRecords()) {

			ClientTransactionMakeDeposit record = (ClientTransactionMakeDeposit) rec;
			// FIXME--need to implement
			// if (record.getAttributeAsBoolean(ATTR_CHECK)) {
			// calculatedTotal += UIUtils.unFormat(
			// record.getAttribute(ATTR_AMOUNT)).doubleValue();
			// }

		}
		totAmtText.setValue(UIUtils.format(calculatedTotal));
		// totText.setValue(UIUtils.format((calculatedTotal - (UIUtils
		// .unFormat(UIUtils.toStr(cashBackAmountText.getValue()))))));
		totText.setAmount(calculatedTotal - cashBackAmountText.getAmount());

	}

	@Override
	public void saveAndUpdateView() {

		updateTransaction();
		saveOrUpdate(transaction);

	}

	private List<ClientTransactionMakeDeposit> getTransactionMakeDepositsList() {
		List<ClientTransactionMakeDeposit> transactionMakeDepositsList = new ArrayList<ClientTransactionMakeDeposit>();
		ClientTransactionMakeDeposit entry;
		// FIXME--need to check
		// for (IsSerializable rec : gridView.getRecords()) {
		// if (record.getAttributeAsBoolean(ATTR_CHECK)) {
		// entry = new ClientTransactionMakeDeposit();
		//
		// // setting date
		// entry.setDate(record.getAttributeAsDate(ATTR_DATE).getTime());
		//
		// // Setting cash account,number and payment method for old
		// // entries
		// if (record.getAttribute("cashAccountId") != null) {
		//
		// // setting isNewEntry for old entries .
		// entry.setIsNewEntry(false);
		//
		// entry.setCashAccount(FinanceApplication.getCompany()
		// .getAccount(
		// UIUtils.toLong(record
		// .getAttribute("cashAccountId")))
		// .getID());
		//
		// // setting DepositedTransactionId
		// entry.setDepositedTransaction(Long.parseLong(record
		// .getAttribute("transactionId")));
		// try {
		// Long number = UIUtils.toLong(record
		// .getAttribute(ATTR_NO));
		// entry.setNumber(number);
		// } catch (Exception e) {
		// }
		//
		// if (record.getAttributeAsString(ATTR_PAYMENT_METHOD) != null) {
		// entry.setPaymentMethod(record
		// .getAttributeAsString(ATTR_PAYMENT_METHOD));
		//
		// }
		//
		// else {
		// // setting isNewEntry for new entries .
		// entry.setIsNewEntry(true);
		// }
		//
		// // Setting Type and account or vendor or customer
		// String selectedType = record
		// .getAttributeAsString(ATTR_TYPE);
		// String account = record.getAttributeAsString(ATTR_ACCOUNT);
		// if (selectedType.equalsIgnoreCase("Financial Account")) {
		//
		// entry
		// .setType(ClientTransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT);
		//
		// for (ClientAccount temp : allAccounts) {
		// if (temp.getName().equalsIgnoreCase(account)) {
		// entry.setAccount(temp.getID());
		// break;
		// }
		// }
		//
		// } else if (selectedType.equalsIgnoreCase("Vendor")) {
		// entry.setType(ClientTransactionMakeDeposit.TYPE_VENDOR);
		//
		// for (ClientVendor temp : allVendors) {
		// if (temp.getName().equalsIgnoreCase(account)) {
		// entry.setVendor(temp.getID());
		// break;
		// }
		// }
		//
		// } else {
		// entry
		// .setType(ClientTransactionMakeDeposit.TYPE_CUSTOMER);
		// for (ClientCustomer temp : allCustomers) {
		// if (temp.getName().equalsIgnoreCase(account)) {
		// entry.setCustomer(temp.getID());
		// break;
		// }
		// }
		//
		// }
		//
		// // Setting Reference
		// String ref = record.getAttributeAsString(ATTR_REFERENCE);
		// if (ref != null)
		// entry.setReference(ref);
		//
		// // Setting amount
		// // String amount = record.getAttribute(ATTR_AMOUNT);
		// entry.setTotal(UIUtils.unFormat(record
		// .getAttribute(ATTR_AMOUNT)));
		//
		// transactionMakeDepositsList.add(entry);
		//
		// }
		//
		// }
		//
		// }
		return transactionMakeDepositsList;
	}

	private List<ClientTransactionMakeDeposit> getAllSelectedRecords(
			ClientMakeDeposit makeDeposit) {
		List<ClientTransactionMakeDeposit> selectedRecords = gridView
				.getRecords();

		for (ClientTransactionMakeDeposit rec : selectedRecords) {
			rec.setID(0);
			rec.setMakeDeposit(makeDeposit);
		}

		return selectedRecords;
	}

	protected void resetForms() {

		// depoForm.resetValues();
		// memoForm.resetValues();
		gridView.removeAllRecords();
		addTracsactionMakeDepositsToGrid();
		// totForm.resetValues();
		// form1.resetValues();
		// form2.resetValues();

	}

	public String getSelectedItemId() {
		return selectedItemId;
	}

	public void setSelectedItemId(String selectedItemId) {
		this.selectedItemId = selectedItemId;
	}

	@Override
	public void init() {
		super.init();
		TYPE_FINANCIAL_ACCOUNT = ClientTransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT;
		TYPE_VENDOR = ClientTransactionMakeDeposit.TYPE_VENDOR;
		TYPE_CUSTOMER = ClientTransactionMakeDeposit.TYPE_CUSTOMER;

		// createControls();
		// setSize("100%", "100%");
		// setOverflow(Overflow.AUTO);
	}

	@Override
	public void initData() {
		super.initData();
		getDepositInAccounts();

		if (isInViewMode()) {
			depositInSelect.setComboItem(getCompany().getAccount(
					((ClientMakeDeposit) transaction).getDepositIn()));
			this.selectedDepositInAccount = getCompany().getAccount(
					((ClientMakeDeposit) transaction).getDepositIn());
		}

		initFianancialAccounts();
		initCashBackAccounts();

		setTransactionNumberToMakeDepositObject();
		// addTracsactionMakeDepositsToGrid();
		// initVendorCombo();
		// initCustomerCombo();

	}

	private void initCashBackAccounts() {
		accountsList = new ArrayList<ClientAccount>();
		for (ClientAccount account : getCompany().getActiveAccounts()) {

			if (account.getType() != ClientAccount.TYPE_INVENTORY_ASSET
					&& account.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
					&& account.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE) {
				accountsList.add(account);
			}

		}
		cashBackAccountSelect.initCombo(accountsList);

	}

	@Override
	protected void initMemoAndReference() {

	}

	@Override
	protected void initTransactionViewData() {
		if (transaction == null) {
			setData(new ClientMakeDeposit());
		} else {
			date.setValue(transaction.getDate());
			memoText.setValue(transaction.getMemo());
			transNumber.setValue(transaction.getNumber());
			this.transactionItems = transaction.getTransactionItems();
			cashBackAmountText.setValue(String.valueOf(transaction
					.getCashBackAmount()));
			cashBackMemoText.setValue(transaction.getCashBackMemo());
			cashBackAccountSelect.setValue(transaction.getCashBackAccount());
			totText.setValue(String.valueOf(transaction.getTotal()));

			gridView.setRecords(transaction.getTransactionMakeDeposit());

			// gridView.setCanEdit(false);
		}
		// FIXME--need to implement this feature
		// gridView.setEnableMenu(false);

		// gridView.canDelete(true);
		// FIMXE--need to add this type
		// gridView.setEditEvent();
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));
		super.initTransactionViewData();

	}

	@Override
	protected void createControls() {
		listforms = new ArrayList<DynamicForm>();
		Label lab = new Label(Accounter.constants().makeDeposit());
		lab.removeStyleName("gwt-Label");
		lab.addStyleName("lable-title");
		lab.setStyleName(Accounter.constants().labelTitle());
		// lab.setHeight("50px");
		date = UIUtils.date(Accounter.constants().date(), this);

		// set the transactionDate while creation
		setTransactionDate(date.getValue());
		date.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate date) {
				if (date != null) {
					setTransactionDate(date);
				}
			}
		});
		locationCombo = createLocationCombo();
		// date.setWidth(100);
		transNumber = createTransactionNumberItem();
		DynamicForm dateForm = new DynamicForm();
		dateForm.setNumCols(6);
		dateForm.setStyleName("datenumber-panel");
		dateForm.setFields(date, transNumber);
		if (locationTrackingEnabled)
			dateForm.setFields(locationCombo);

		HorizontalPanel datepanel = new HorizontalPanel();
		datepanel.setWidth("100%");
		datepanel.add(dateForm);
		datepanel.setCellHorizontalAlignment(dateForm, ALIGN_RIGHT);

		depositInSelect = new MakeDepositAccountCombo(Accounter.constants()
				.depositIn());
		depositInSelect.setHelpInformation(true);
		depositInSelect.setRequired(true);
		// depositInSelect.setWidth(100);
		depositInSelect.setPopupWidth("450px");
		depositInSelect.setDisabled(isInViewMode());
		depositInSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {
					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectedDepositInAccount = selectItem;

					}

				});

		vendorSelect = new VendorCombo("");

		financeAccountSelect = new OtherAccountsCombo("");

		customerSelect = new CustomerCombo("");

		vendorSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {

					public void selectedComboBoxItem(ClientVendor selectItem) {
						selectedVendor = selectItem;

					}

				});
		financeAccountSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					public void selectedComboBoxItem(ClientAccount selectItem) {
						// selectedFinanceAccount = selectItem;

					}

				});

		customerSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientCustomer>() {

					public void selectedComboBoxItem(ClientCustomer selectItem) {
						// selectedCustomer = selectItem;

					}

				});

		memoText = new TextAreaItem(Accounter.constants().memo());
		memoText.setMemo(true, this);
		memoText.setHelpInformation(true);
		memoText.setWidth(100);

		memoForm = new DynamicForm();
		memoForm.setWidth("100%");
		memoForm.setFields(memoText);
		memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");

		depoForm = new DynamicForm();
		depoForm.setIsGroup(true);
		depoForm.setGroupTitle(Accounter.constants().deposit());
		depoForm.setFields(depositInSelect);
		if (getPreferences().isClassTrackingEnabled()
				&& getPreferences().isClassOnePerTransaction()) {
			classListCombo = createAccounterClassListCombo();
			depoForm.setFields(classListCombo);
		}
		depoForm.setWidth("40%");

		// Label lab1 = new Label(FinanceApplication.constants()
		// .paymentsReceived());

		addButton = new AddButton(this);

		addButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ClientTransactionMakeDeposit deposit = new ClientTransactionMakeDeposit();
				deposit.setIsNewEntry(true);
				deposit.setType(ClientTransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT);
				// deposit.set
				gridView.add(deposit);
			}
		});

		cashBackAccountSelect = new CashBackAccountsCombo(Accounter.messages()
				.cashBackAccount(Global.get().Account()));
		cashBackAccountSelect.setHelpInformation(true);
		cashBackAccountSelect.setAccountTypes(UIUtils
				.getOptionsByType(AccountCombo.CASH_BACK_ACCOUNTS_COMBO));
		// cashBackAccountSelect.setRequired(true);
		cashBackAccountSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {
					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectedCashBackAccount = selectItem;

					}

				});

		cashBackMemoText = new TextItem(Accounter.constants().cashBackMemo());
		cashBackMemoText.setHelpInformation(true);
		cashBackMemoText.setWidth(100);

		form1 = new DynamicForm();
		form1.setFields(cashBackMemoText, cashBackAccountSelect);
		form1.getCellFormatter().setWidth(0, 0, "180px");
		form1.setWidth("70%");

		cashBackAmountText = new AmountField(Accounter.constants()
				.cashBackAmount(), this);
		cashBackAmountText.setHelpInformation(true);
		cashBackAmountText.setWidth(100);
		cashBackAmountText.setDefaultValue("" + UIUtils.getCurrencySymbol()
				+ "0.00");
		cashBackAmountText.addChangedHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (event != null)
					validateCashBackAmount();
			}
		});

		totText = new AmountLabel(Accounter.constants().total());
		totText.setWidth("100px");
		totText.setDefaultValue("" + UIUtils.getCurrencySymbol() + "0.00");
		totText.setDisabled(true);
		((Label) totText.getMainWidget())
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

		form2 = new DynamicForm();
		form2.setFields(totText);
		form2.addStyleName("textbold");
		form2.setWidth("50%");
		form2.getElement().getStyle().setMarginTop(10, Unit.PX);

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.setWidth("100%");
		topHLay.add(depoForm);

		HorizontalPanel panel = new HorizontalPanel();
		panel.setHorizontalAlignment(ALIGN_LEFT);
		panel.add(addButton);
		panel.getElement().getStyle().setMarginTop(8, Unit.PX);

		addButton.setEnabled(!isInViewMode());

		HorizontalPanel botHLay = new HorizontalPanel();
		botHLay.setWidth("100%");
		botHLay.add(memoForm);
		botHLay.setCellHorizontalAlignment(memoForm, ALIGN_LEFT);
		botHLay.add(form2);
		botHLay.setCellHorizontalAlignment(form2, ALIGN_RIGHT);

		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setHorizontalAlignment(ALIGN_LEFT);
		vPanel.setWidth("100%");
		vPanel.add(panel);
		vPanel.setHorizontalAlignment(ALIGN_RIGHT);
		vPanel.add(botHLay);

		if (isInViewMode()) {
			date.setValue(transaction.getDate());
			depositInSelect.setComboItem(getCompany().getAccount(
					((ClientMakeDeposit) transaction).getDepositIn()));
			memoText.setDisabled(true);
			if (((ClientMakeDeposit) transaction).getMemo() != null)
				memoText.setValue(((ClientMakeDeposit) transaction).getMemo());
			cashBackAccountSelect.setComboItem(getCompany().getAccount(
					((ClientMakeDeposit) transaction).getCashBackAccount()));
			if (((ClientMakeDeposit) transaction).getCashBackMemo() != null)
				cashBackMemoText.setValue(((ClientMakeDeposit) transaction)
						.getCashBackMemo());
			// totAmtText
			// .setValue(UIUtils.format(((MakeDeposit) transactionObject)
			// .getTotalAmount()));
			// totAmtText.setAmount(((ClientMakeDeposit) transactionObject)
			// .getTotal());
			// totText.setValue(UIUtils.format(((MakeDeposit) transactionObject)
			// .getTotal()));
			totText.setAmount(((ClientMakeDeposit) transaction).getTotal());
			// cashBackAmountText.setValue(UIUtils
			// .format(((MakeDeposit) transactionObject)
			// .getCashBackAmount()));
			cashBackAmountText.setAmount(((ClientMakeDeposit) transaction)
					.getCashBackAmount());
			addTransactionMakeDepositsToGrid(transaction
					.getTransactionMakeDeposit());

			date.setDisabled(true);
			depositInSelect.setDisabled(true);
			cashBackAccountSelect.setDisabled(true);
			cashBackAmountText.setDisabled(true);
			cashBackMemoText.setDisabled(true);

		}

		initListGrid();

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(lab);
		mainVLay.add(datepanel);
		mainVLay.add(topHLay);
		// mainVLay.add(lab1);
		// mainVLay.add(addButton);

		mainVLay.add(gridView);

		mainVLay.add(vPanel);

		if (UIUtils.isMSIEBrowser()) {
			resetFormView();
		}

		this.add(mainVLay);

		// setSize("700", "600");

		/* Adding dynamic forms in list */
		listforms.add(depoForm);
		listforms.add(memoForm);
		// listforms.add(totForm);
		// listforms.add(form1);
		listforms.add(form2);
		settabIndexes();

	}

	private void initFianancialAccounts() {
		financeAccountSelect.initCombo(getCompany().getActiveAccounts());
	}

	@Override
	public void setData(ClientMakeDeposit data) {

		super.setData(data);
		if (isInViewMode() && (!transaction.isMakeDeposit()))
			try {
				throw new Exception(Accounter.constants()
						.unabletoLoadTheRequiredDeposit());
			} catch (Exception e) {
				e.printStackTrace();
			}

	}

	@Override
	public void updateNonEditableItems() {
		if (gridView == null)
			return;
		totText.setAmount(gridView.getTotal() - cashBackAmountText.getAmount());
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();

		// Validations
		// 1. if(isBlackTransaction(gridView))
		// ERROR
		// else
		// gridView validation
		// 2. if(!isValidTransactionDate(transactionDate)) ERROR
		// 3. if(isInPreventPostingBeforeDate(transactionDate)) ERROR
		// 4. depoForm validation
		// 5. if(!validMakeDepositCombo(selectedDepositInAccount, gridAcconts))
		// ERROR

		result.add(depoForm.validate());

		if ((gridView == null)
				|| (gridView != null && gridView.getRecords().isEmpty())) {
			result.addError(gridView, accounterConstants.blankTransaction());
		} else {
			result.add(gridView.validateGrid());
		}

		if (!AccounterValidator.isValidTransactionDate(transactionDate)) {
			result.addError(transactionDateItem,
					accounterConstants.invalidateTransactionDate());
		}
		if (AccounterValidator.isInPreventPostingBeforeDate(transactionDate)) {
			result.addError(transactionDateItem,
					accounterConstants.invalidateDate());
		}

		// result.add(depoForm.validate());

		// if
		// (AccounterValidator.isNegativeAmount(cashBackAmountText.getAmount()))
		// {
		// result.addError(cashBackAmountText,
		// accounterConstants.invalidNegativeAmount());
		// } else if (!AccounterValidator.isValidMakeDeposit_CashBackAmount(
		// cashBackAmountText.getAmount().doubleValue(),
		// totText.getAmount())) {
		// result.addError(cashBackAmountText, Accounter.constants()
		// .makeDepositCashBackAmount());
		// }

		// if (!AccounterValidator.validate_MakeDeposit_accountcombo(
		// selectedDepositInAccount, gridView)) {
		// result.addError(gridView, Accounter.constants()
		// .makedepositAccountValidation());
		// }
		return result;

	}

	public static MakeDepositView getInstance() {
		return new MakeDepositView();
	}

	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.depositInSelect.setFocus();
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
		// if (transactionObject.canEdit) {
		// Accounter.showWarning(AccounterWarningType.MAKEDEPOSIT_EDITING,
		// AccounterType.WARNING, new ErrorDialogHandler() {
		//
		// @Override
		// public boolean onYesClick()
		// throws InvalidEntryException {
		// voidTransaction();
		// return true;
		// }
		//
		// private void voidTransaction() {
		// AccounterAsyncCallback<Boolean> callback = new
		// AccounterAsyncCallback<Boolean>() {
		//
		// @Override
		// public void onException(AccounterException caught) {
		// Accounter
		// .showError("Failed to void Make Deposit");
		//
		// }
		//
		// @Override
		// public void onSuccess(Boolean result) {
		// if (result) {
		// enableFormItems();
		// } else
		//
		// onFailure(new Exception());
		// }
		//
		// };
		// makeDepositEdited = (ClientMakeDeposit) transactionObject;
		// if (makeDepositEdited != null) {
		// AccounterCoreType type = UIUtils
		// .getAccounterCoreType(makeDepositEdited
		// .getType());
		// rpcDoSerivce.voidTransaction(type,
		// makeDepositEdited.id, callback);
		// }
		//
		// }
		//
		// @Override
		// public boolean onNoClick() throws InvalidEntryException {
		//
		// return true;
		// }
		//
		// @Override
		// public boolean onCancelClick()
		// throws InvalidEntryException {
		//
		// return true;
		// }
		// });
		// }
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

	private void enableFormItems() {
		setMode(EditMode.EDIT);
		date.setDisabled(isInViewMode());
		depositInSelect.setDisabled(isInViewMode());
		transNumber.setDisabled(isInViewMode());
		addButton.setEnabled(!isInViewMode());
		gridView.setDisabled(isInViewMode());
		cashBackMemoText.setDisabled(isInViewMode());
		cashBackAmountText.setDisabled(isInViewMode());
		cashBackAccountSelect.setDisabled(isInViewMode());
		memoText.setDisabled(isInViewMode());

		// For deleting the transctionItems after we edit
		for (ClientTransactionMakeDeposit ctmd : transaction
				.getTransactionMakeDeposit())
			ctmd.setIsNewEntry(true);
		// transactionObject = null;
		if (locationTrackingEnabled)
			locationCombo.setDisabled(isInViewMode());
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
	protected void initTransactionTotalNonEditableItem() {
		// NOTHING TO DO.
	}

	public void resetFormView() {

		form1.getCellFormatter().setWidth(0, 1, "200px");
		form1.getCellFormatter().setWidth(0, 1, "200px");
		form2.getCellFormatter().setWidth(0, 1, "200px");
		form2.getCellFormatter().setWidth(0, 1, "200px");
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().makeDeposit();
	}

	protected void updateTransaction() {
		super.updateTransaction();
		// Setting date
		if (date != null) {
			transaction.setDate(date.getValue().getDate());
		}
		// Setting Deposit in
		if (depositInSelect.getSelectedValue() != null)
			transaction.setDepositIn(selectedDepositInAccount.getID());

		// Setting Memo
		if (memoText.getValue() != null)
			transaction.setMemo(UIUtils.toStr(memoText.getValue()));

		if (transNumber.getValue() != null)
			transaction.setNumber(transNumber.getValue());

		// setting transaction make deposits list
		List<ClientTransactionMakeDeposit> listOfTrannsactionMakeDeposits = getAllSelectedRecords(transaction);
		isListEmpty = false;
		if (listOfTrannsactionMakeDeposits.size() == 0)
			isListEmpty = true;
		else
			transaction
					.setTransactionMakeDeposit(listOfTrannsactionMakeDeposits);

		// Setting Cash back account
		transaction
				.setCashBackAccount(selectedCashBackAccount != null ? selectedCashBackAccount
						.getID() : 0);
		if (cashBackMemoText.getValue() != null)
			transaction.setCashBackMemo(cashBackMemoText.getValue().toString());

		// Setting Cash back amount
		transaction.setCashBackAmount(cashBackAmountText.getAmount());
		// Setting Total amount

		// Setting Total
		transaction.setTotal(totText.getAmount());

		// Setting Transaction type
		transaction.setType(ClientTransaction.TYPE_MAKE_DEPOSIT);
		super.saveAndUpdateView();

	}

	@Override
	protected void addNewData(ClientTransactionItem transactionItem) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void refreshTransactionGrid() {
		// TODO Auto-generated method stub

	}

	private void settabIndexes() {
		depositInSelect.setTabIndex(1);
		date.setTabIndex(2);
		transNumber.setTabIndex(3);
		memoText.setTabIndex(4);
		addButton.setTabIndex(5);
		saveAndCloseButton.setTabIndex(6);
		saveAndNewButton.setTabIndex(7);
		cancelButton.setTabIndex(8);

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
	protected void taxCodeSelected(ClientTAXCode taxCode) {
		// TODO Auto-generated method stub

	}
}
