package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientReceiveVAT;
import com.vimukti.accounter.web.client.core.ClientReceiveVATEntries;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionReceiveVAT;
import com.vimukti.accounter.web.client.core.ClientVATReturn;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
import com.vimukti.accounter.web.client.ui.combo.DepositInAccountCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.TAXAgencyCombo;
import com.vimukti.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.TransactionReceiveVATGrid;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

/**
 * 
 * @author Tirupathi
 * 
 */
public class ReceiveVATView extends
		AbstractTransactionBaseView<ClientReceiveVAT> {

	private static final int RECEIVEVAT_TRANSACTION_GRID = 0;
	private ArrayList<DynamicForm> listforms;
	private DateField date;
	private DepositInAccountCombo depositInAccCombo;
	private DateField billsDue;
	private TAXAgencyCombo vatAgencyCombo;
	private DynamicForm mainform;
	private AmountField amountText;
	private AmountField endingBalanceText;
	private DynamicForm balForm;
	protected ClientAccount selectedDepositInAccount;
	protected double initialEndingBalance;
	protected ClientTAXAgency selectedVATAgency;
	private VerticalPanel gridLayout;
	private TransactionReceiveVATGrid grid;
	private Double totalAmount = 0.0D;
	private String transactionNumber;
	protected List<ClientReceiveVATEntries> entries;
	private ClientTAXAgency selectedTaxAgency;
	private double endingBalance;
	private ArrayList<ClientReceiveVATEntries> filterList;
	private ArrayList<ClientReceiveVATEntries> tempList;
	private ClientFinanceDate dueDateOnOrBefore;
	AccounterConstants accounterConstants = Accounter.constants();

	private DynamicForm fileterForm;
	private TextItem transNumber;
	private AccounterConstants companyConstants = Accounter.constants();

	public ReceiveVATView() {
		super(ClientTransaction.TYPE_PAY_SALES_TAX);
	}

	protected void createControls() {
		listforms = new ArrayList<DynamicForm>();

		// setTitle(UIUtils.title(FinanceApplication.constants()
		// .payVAT()));

		Label lab = new Label(Accounter.constants().receiveVAT());
		lab.setStyleName(Accounter.constants().labelTitle());
		// lab.setHeight("35px");
		// date = new DateField(companyConstants.date());
		// date.setHelpInformation(true);
		// date.setTitle(companyConstants.date());
		// date.setEnteredDate(new ClientFinanceDate());
		// date.setDisabled(isEdit);
		transactionDateItem = createTransactionDateItem();

		transNumber = createTransactionNumberItem();
		transNumber.setTitle(Accounter.constants().no());
		transNumber.setToolTip(Accounter.messages().giveNoTo(
				this.getAction().getViewName()));

		depositInAccCombo = new DepositInAccountCombo(
				companyConstants.depositIn());
		depositInAccCombo.setHelpInformation(true);
		depositInAccCombo.setAccountTypes(UIUtils
				.getOptionsByType(AccountCombo.DEPOSIT_IN_ACCOUNT));
		depositInAccCombo.setRequired(true);
		depositInAccCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectedDepositInAccount = selectItem;
						// initialEndingBalance = selectedPayFromAccount
						// .getTotalBalance() != 0 ? selectedPayFromAccount
						// .getTotalBalance()
						// : 0D;

						initialEndingBalance = !DecimalUtil.isEquals(
								selectedDepositInAccount.getTotalBalance(), 0) ? selectedDepositInAccount
								.getTotalBalance() : 0D;

						calculateEndingBalance();
					}

				});

		depositInAccCombo.setDisabled(isInViewMode());
		depositInAccCombo.setPopupWidth("500px");
		paymentMethodCombo = createPaymentMethodSelectItem();
		paymentMethodCombo.setRequired(true);
		// paymentMethodCombo.setWidth(100);

		billsDue = new DateField(companyConstants.returnsDueOnOrBefore());
		billsDue.setHelpInformation(true);
		billsDue.setTitle(companyConstants.returnsDueOnOrBefore());
		billsDue.setDisabled(isInViewMode());
		billsDue.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate date) {
				if (transaction == null) {
					dueDateOnOrBefore = date;
					filterGrid();
				}
			}
		});

		// vatAgencyCombo = new VATAgencyCombo("Filter By "
		// + companyConstants.vatAgency());
		// vatAgencyCombo.setDisabled(isEdit);
		// vatAgencyCombo
		// .addSelectionChangeHandler(new
		// IAccounterComboSelectionChangeHandler<ClientVATAgency>() {
		//
		// public void selectedComboBoxItem(ClientVATAgency selectItem) {
		//
		// selectedVATAgency = selectItem;
		// if (selectedVATAgency != null)
		// filterlistbyVATAgency(selectedVATAgency);
		//
		// }
		//
		// });
		DynamicForm dateForm = new DynamicForm();
		dateForm.setNumCols(4);
		dateForm.setStyleName("datenumber-panel");
		dateForm.setFields(transactionDateItem, transNumber);
		HorizontalPanel datepanel = new HorizontalPanel();
		datepanel.setWidth("100%");
		datepanel.add(dateForm);
		datepanel.setCellHorizontalAlignment(dateForm,
				HasHorizontalAlignment.ALIGN_RIGHT);

		mainform = new DynamicForm();
		// filterForm.setWidth("100%");
		mainform = UIUtils.form(companyConstants.filter());
		mainform.setFields(depositInAccCombo, paymentMethodCombo, billsDue);
		mainform.setWidth("80%");

		// fileterForm = new DynamicForm();
		// fileterForm.setFields(billsDue);
		// fileterForm.setWidth("80%");

		amountText = new AmountField(companyConstants.amount(), this);
		amountText.setHelpInformation(true);
		amountText.setValue("" + UIUtils.getCurrencySymbol() + " 0.00");
		amountText.setDisabled(true);

		endingBalanceText = new AmountField(companyConstants.endingBalance(),
				this);
		endingBalanceText.setHelpInformation(true);
		endingBalanceText.setValue("" + UIUtils.getCurrencySymbol() + " 0.00");
		endingBalanceText.setDisabled(true);

		balForm = new DynamicForm();
		balForm = UIUtils.form(companyConstants.balances());
		balForm.setFields(amountText, endingBalanceText);

		if (getPreferences().isClassTrackingEnabled()
				&& getPreferences().isClassOnePerTransaction()) {
			classListCombo = createAccounterClassListCombo();
			balForm.setFields(classListCombo);
		}

		balForm.getCellFormatter().setWidth(0, 0, "197px");

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setWidth("100%");
		leftVLay.add(mainform);
		// leftVLay.add(fileterForm);

		VerticalPanel rightVLay = new VerticalPanel();
		rightVLay.add(balForm);

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.setWidth("100%");
		topHLay.setSpacing(10);
		topHLay.add(leftVLay);
		topHLay.add(rightVLay);
		topHLay.setCellWidth(leftVLay, "50%");
		topHLay.setCellWidth(rightVLay, "36%");

		Label lab1 = new Label("" + companyConstants.billsToReceive() + "");

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
		listforms.add(mainform);
		listforms.add(balForm);

		selectedDepositInAccount = depositInAccCombo.getSelectedValue();
		initialEndingBalance = selectedDepositInAccount == null ? 0D
				: !DecimalUtil.isEquals(
						selectedDepositInAccount.getTotalBalance(), 0) ? selectedDepositInAccount
						.getTotalBalance() : 0D;
		calculateEndingBalance();
		settabIndexes();

	}

	protected void filterGrid() {
		filterList = new ArrayList<ClientReceiveVATEntries>();
		tempList = new ArrayList<ClientReceiveVATEntries>();

		filterList.addAll(entries);

		if (dueDateOnOrBefore != null) {
			for (ClientReceiveVATEntries cont : filterList) {
				ClientVATReturn clientVATReturn = Accounter.getCompany()
						.getVatReturn(cont.getVatReturn());
				ClientFinanceDate date = new ClientFinanceDate(
						clientVATReturn.getVATperiodEndDate());
				if (date.equals(dueDateOnOrBefore)
						|| date.before(dueDateOnOrBefore))
					tempList.add(cont);
			}
			filterList.clear();
			filterList.addAll(tempList);
			tempList.clear();
		}

		loadData(filterList);
		int size = grid.getRecords().size();
		if (size == 0)
			grid.addEmptyMessage(Accounter.constants().noRecordsToShow());
	}

	private void calculateEndingBalance() {

		if (selectedDepositInAccount != null) {

			if (selectedDepositInAccount.isIncrease())
				endingBalance = initialEndingBalance + totalAmount;
			else
				endingBalance = initialEndingBalance - totalAmount;

			endingBalanceText.setValue(DataUtils
					.getAmountAsString(endingBalance));

		}
	}

	protected void filterlistbyVATAgency(ClientTAXAgency selectedVATAgency) {
		List<ClientTransactionReceiveVAT> filterRecords = new ArrayList<ClientTransactionReceiveVAT>();
		String selectedagency = selectedVATAgency.getName();
		for (ClientTransactionReceiveVAT receiveVAT : grid.getRecords()) {
			String taxAgencyname = getCompany().getTaxAgency(
					receiveVAT.getTaxAgency()).getName();
			if (taxAgencyname.equals(selectedagency))
				filterRecords.add(receiveVAT);
		}
		grid.setRecords(filterRecords);
	}

	// initializes the grid.
	private void initListGrid() {

		gridLayout = new VerticalPanel();
		grid = new TransactionReceiveVATGrid(!isInViewMode(), true);
		grid.setCanEdit(!isInViewMode());
		grid.isEnable = false;
		grid.init();
		grid.setRecieveVATView(this);
		grid.setDisabled(isInViewMode());
		grid.setHeight("200px");
		// if (!isEdit) {
		// // grid.addFooterValue("Total", 1);
		// grid
		// .updateFooterValues(DataUtils
		// .getAmountAsString(totalAmount), 2);
		// }
		gridLayout.add(grid);
		gridLayout.setWidth("100%");

	}

	@Override
	protected void initTransactionViewData() {
		if (transaction == null) {
			setData(new ClientReceiveVAT());
			initTransactionNumber();
			fillGrid();
			initPayFromAccounts();
			return;
		}

		selectedDepositInAccount = getCompany().getAccount(
				transaction.getDepositIn());
		depositInAccCombo.setComboItem(selectedDepositInAccount);
		selectedVATAgency = getCompany().getTaxAgency(
				transaction.getVatAgency());
		if (selectedVATAgency != null)
			vatAgencyCombo.setComboItem(selectedVATAgency);

		billsDue.setEnteredDate(new ClientFinanceDate(transaction
				.getReturnsDueOnOrBefore()));
		transactionDateItem.setEnteredDate(transaction.getDate());
		transNumber.setValue(transaction.getNumber());
		endingBalanceText.setAmount(transaction.getEndingBalance());
		paymentMethodCombo.setComboItem(transaction.getPaymentMethod());
		amountText.setValue(String.valueOf(transaction.getTotal()));
		List<ClientTransactionReceiveVAT> list = transaction
				.getClientTransactionReceiveVAT();
		int count = 0;
		for (ClientTransactionReceiveVAT record : list) {
			if (record != null) {
				grid.addData(record);
				grid.selectRow(count);
				count++;
			}
		}
		// grid.updateFooterValues("Total"
		// + DataUtils.getAmountAsString(receiveVAT.getTotal()), 2);

	}

	protected void initPayFromAccounts() {
		// getPayFromAccounts();
		// payFromCombo.initCombo(payFromAccounts);
		// payFromCombo.setAccountTypes(UIUtils
		// .getOptionsByType(AccountCombo.payFromCombo));
		depositInAccCombo.setAccounts();
		// payFromCombo.setDisabled(isEdit);
		ClientAccount DepositInAccount = depositInAccCombo.getSelectedValue();
		if (DepositInAccount != null)
			depositInAccCombo.setComboItem(DepositInAccount);
	}

	private void fillGrid() {
		grid.addLoadingImagePanel();
		rpcUtilService
				.getReceiveVATEntries(new AccounterAsyncCallback<ArrayList<ClientReceiveVATEntries>>() {

					@Override
					public void onException(AccounterException caught) {
						Accounter.showError(Accounter.constants()
								.failedtogettheTransactionPayVATList());
						grid.addEmptyMessage(Accounter.constants()
								.noFiledVatEntriesToReceive());

					}

					@Override
					public void onResultSuccess(
							ArrayList<ClientReceiveVATEntries> result) {
						if (result == null) {

							onFailure(null);
						}
						entries = result;
						if (result.size() == 0) {
							// Accounter.showInformation("No PayVAT list to show");
							grid.addEmptyMessage(Accounter.constants()
									.noFiledVatEntriesToReceive());
						} else {

							// loadData(getfilterRecordsByDate(billsDue
							// .getEnteredDate(), entries));
							loadData(entries);

						}

					}
				});

	}

	// fills the list grid with data.
	protected void loadData(List<ClientReceiveVATEntries> result) {

		List<ClientTransactionReceiveVAT> records = new ArrayList<ClientTransactionReceiveVAT>();
		for (ClientReceiveVATEntries entry : result) {
			ClientTransactionReceiveVAT clientEntry = new ClientTransactionReceiveVAT();

			clientEntry.setTaxAgency(entry.getVatAgency());
			clientEntry.setVatReturn(entry.getVatReturn());
			// clientEntry.setAmountToReceive(entry.getAmount())
			double total = entry.getAmount();
			double balance = entry.getBalance();
			// clientEntry
			// .setTaxDue(total - balance > 0.0 ? total - balance : 0.0);
			clientEntry.setTaxDue(balance);

			records.add(clientEntry);
		}
		// setFilterByDateList(records);
		// if (selectedTaxAgency == null)
		grid.setRecords(records);
		// else
		// filterlistbyTaxAgency(selectedTaxAgency);

	}

	protected void initTransactionNumber() {

		rpcUtilService.getNextTransactionNumber(
				ClientTransaction.TYPE_RECEIVE_VAT,
				new AccounterAsyncCallback<String>() {

					public void onException(AccounterException caught) {
						Accounter.showError(Accounter.constants()
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
		if (!AccounterValidator.isValidTransactionDate(this.transactionDate)) {
			result.addError(transactionDate,
					accounterConstants.invalidateTransactionDate());
		}

		if (AccounterValidator.isInPreventPostingBeforeDate(transactionDate)) {
			result.addError(transactionDate,
					accounterConstants.invalidateDate());
		}
		result.add(mainform.validate());
		if (isInViewMode()) {
			if (grid.getRecords().isEmpty()) {
				result.addError(grid, Accounter.constants()
						.youdonthaveanyfiledVATentriestoselect());
			}
		}
		if (grid == null || grid.getRecords().isEmpty()
				|| grid.getSelectedRecords().size() == 0) {
			result.addError(grid, Accounter.constants()
					.youdonthaveanyfiledVATentriestoselect());
		} else {
			result.add(grid.validateGrid());
		}
		if (isInViewMode()) {
			if (!AccounterValidator.isPositiveAmount(totalAmount)) {
				// FIXME Need to Configm Object
				result.addError("TotalAmount", accounterConstants.amount());
			}
		}
		return result;

	}

	@Override
	public void saveAndUpdateView() {
		updateTransaction();
		saveOrUpdate(transaction);
	}

	protected void updateTransaction() {
		super.updateTransaction();
		transaction.setNumber(transactionNumber);
		transaction.setType(ClientTransaction.TYPE_RECEIVE_VAT);

		if (transactionDateItem.getEnteredDate() != null)
			transaction.setDate(transactionDateItem.getEnteredDate().getDate());

		transaction.setDepositIn(selectedDepositInAccount.getID());
		transaction.setPaymentMethod(paymentMethod);

		if (billsDue.getValue() != null)
			transaction
					.setReturnsDueOnOrBefore((billsDue.getValue()).getDate());

		if (selectedTaxAgency != null)
			transaction.setVatAgency(selectedTaxAgency.getID());

		transaction.setTotal(totalAmount);
		transaction.setEndingBalance(endingBalance);

		transaction
				.setClientTransactionReceiveVAT(getTransactionReceiveVATList());

	}

	private List<ClientTransactionReceiveVAT> getTransactionReceiveVATList() {

		// List<ClientTransactionPayVAT> payVATList = new
		// ArrayList<ClientTransactionPayVAT>();
		//
		// for (ClientTransactionPayVAT rec : grid.getSelectedRecords()) {
		// // rec.setTransaction(paySalesTax);
		// rec.setVatReturn(vatReturn)setRecords
		// payVATList.add(rec);
		//
		// }

		return grid.getSelectedRecords();
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
		if (selectedDepositInAccount != null) {
			double toBeSetEndingBalance = 0.0;
			if (selectedDepositInAccount.isIncrease())
				toBeSetEndingBalance = selectedDepositInAccount
						.getTotalBalance()

						+ DataUtils.getBalance(
								amountText.getAmount().toString())
								.doubleValue();
			else
				toBeSetEndingBalance = selectedDepositInAccount
						.getTotalBalance()

						- DataUtils.getBalance(
								amountText.getAmount().toString())
								.doubleValue();
			endingBalanceText.setAmount(toBeSetEndingBalance);
		}
		// }

	}

	@Override
	public void updateNonEditableItems() {

	}

	@Override
	public List<DynamicForm> getForms() {
		return null;
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	public void onEdit() {

		if (transaction.canEdit) {
			Accounter.showWarning(AccounterWarningType.PAYVAT_EDITING,
					AccounterType.WARNING, new ErrorDialogHandler() {

						@Override
						public boolean onYesClick() {
							voidTransaction();
							return true;
						}

						private void voidTransaction() {
							AccounterAsyncCallback<Boolean> callback = new AccounterAsyncCallback<Boolean>() {

								@Override
								public void onException(
										AccounterException caught) {
									Accounter.showError(Accounter.constants()
											.failedtovoidReceiveVAT());

								}

								@Override
								public void onResultSuccess(Boolean result) {
									if (result) {
										enableFormItems();
									} else

										onFailure(new Exception());
								}

							};
							if (isInViewMode()) {
								AccounterCoreType type = UIUtils
										.getAccounterCoreType(transaction
												.getType());
								rpcDoSerivce.voidTransaction(type,
										transaction.id, callback);
							}

						}

						@Override
						public boolean onNoClick() {

							return true;
						}

						@Override
						public boolean onCancelClick() {

							return true;
						}
					});
		}

	}

	private void enableFormItems() {
		setMode(EditMode.EDIT);
		date.setDisabled(isInViewMode());
		paymentMethodCombo.setDisabled(isInViewMode());
		billsDue.setDisabled(isInViewMode());
		vatAgencyCombo.setDisabled(isInViewMode());
		depositInAccCombo.setDisabled(isInViewMode());
		super.onEdit();

		fillGrid();
		transaction = null;

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().receiveVAT();
	}

	@Override
	protected void refreshTransactionGrid() {

	}

	@Override
	public void setFocus() {
		this.depositInAccCombo.setFocus();

	}

	private void settabIndexes() {
		depositInAccCombo.setTabIndex(1);
		paymentMethodCombo.setTabIndex(2); 
		billsDue.setTabIndex(3);
		transactionDateItem.setTabIndex(4);
		transNumber.setTabIndex(5);
		amountText.setTabIndex(6);
		endingBalanceText.setTabIndex(7);
		saveAndCloseButton.setTabIndex(8);
		saveAndNewButton.setTabIndex(9);
		cancelButton.setTabIndex(10);
		
	}


}