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
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
import com.vimukti.accounter.web.client.ui.combo.DepositInAccountCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.TAXAgencyCombo;
import com.vimukti.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.TransactionReceiveVATGrid;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

/**
 * 
 * @author Tirupathi
 * 
 */
public class RecieveVATView extends
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
	private ClientReceiveVAT receiveVAT;
	private ArrayList<ClientReceiveVATEntries> filterList;
	private ArrayList<ClientReceiveVATEntries> tempList;
	private ClientFinanceDate dueDateOnOrBefore;

	private DynamicForm fileterForm;
	private TextItem transNumber;
	private AccounterConstants companyConstants = Accounter.constants();

	public RecieveVATView() {
		super(ClientTransaction.TYPE_PAY_SALES_TAX, RECEIVEVAT_TRANSACTION_GRID);
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

		depositInAccCombo.setDisabled(isEdit);
		depositInAccCombo.setPopupWidth("500px");
		paymentMethodCombo = createPaymentMethodSelectItem();
		paymentMethodCombo.setRequired(true);
		// paymentMethodCombo.setWidth(100);

		billsDue = new DateField(companyConstants.returnsDueOnOrBefore());
		billsDue.setHelpInformation(true);
		billsDue.setTitle(companyConstants.returnsDueOnOrBefore());
		billsDue.setDisabled(isEdit);
		billsDue.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate date) {
				if (transactionObject == null) {
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
		/* Adding dynamic forms in list */
		listforms.add(mainform);
		listforms.add(balForm);

		selectedDepositInAccount = depositInAccCombo.getSelectedValue();
		initialEndingBalance = !DecimalUtil.isEquals(
				selectedDepositInAccount.getTotalBalance(), 0) ? selectedDepositInAccount
				.getTotalBalance() : 0D;

		calculateEndingBalance();

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
		grid = new TransactionReceiveVATGrid(!isEdit, true) {
			@Override
			protected String[] getColumns() {
				return new String[] { companyConstants.vatAgency(),
						companyConstants.taxDue(),
						companyConstants.amountToReceive() };
			}
		};
		grid.setCanEdit(!isEdit);
		grid.isEnable = false;
		grid.init();
		grid.setRecieveVATView(this);
		grid.setDisabled(isEdit);
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
	protected void initTransactionViewData(ClientTransaction transactionObject) {
		receiveVAT = (ClientReceiveVAT) transactionObject;
		selectedDepositInAccount = getCompany().getAccount(
				receiveVAT.getDepositIn());
		depositInAccCombo.setComboItem(selectedDepositInAccount);
		selectedVATAgency = getCompany()
				.getTaxAgency(receiveVAT.getVatAgency());
		if (selectedVATAgency != null)
			vatAgencyCombo.setComboItem(selectedVATAgency);

		billsDue.setEnteredDate(new ClientFinanceDate(receiveVAT
				.getReturnsDueOnOrBefore()));
		transactionDateItem.setEnteredDate(receiveVAT.getDate());
		transNumber.setValue(receiveVAT.getNumber());
		endingBalanceText.setAmount(receiveVAT.getEndingBalance());
		paymentMethodCombo.setComboItem(receiveVAT.getPaymentMethod());
		amountText.setValue(receiveVAT.getTotal());
		List<ClientTransactionReceiveVAT> list = receiveVAT
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

	@Override
	protected void initTransactionViewData() {
		initTransactionNumber();
		fillGrid();
		initPayFromAccounts();
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
				.getReceiveVATEntries(new AccounterAsyncCallback<List<ClientReceiveVATEntries>>() {

					@Override
					public void onException(AccounterException caught) {
						Accounter.showError(Accounter.constants()
								.failedtogettheTransactionPayVATList());
						grid.addEmptyMessage(Accounter.constants()
								.noRecordsToShow());

					}

					@Override
					public void onSuccess(List<ClientReceiveVATEntries> result) {
						if (result == null) {

							onFailure(null);
						}
						entries = result;
						if (result.size() == 0) {
							// Accounter.showInformation("No PayVAT list to show");
							grid.addEmptyMessage(Accounter.constants()
									.noRecordsToShow());
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

					public void onSuccess(String result) {
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
		result.add(mainform.validate());
		if (isEdit) {
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
		if (isEdit) {
			if (!AccounterValidator.validateAmount(totalAmount)) {
				// FIXME Need to Configm Object
				result.addError("TotalAmount", AccounterErrorType.amount);
			}
		}
		return result;

	}

	@Override
	public void saveAndUpdateView() {
		ClientReceiveVAT receiveVAT = getReceiveSalesTax();
		createObject(receiveVAT);
	}

	private ClientReceiveVAT getReceiveSalesTax() {

		ClientReceiveVAT receiveVAT = new ClientReceiveVAT();

		receiveVAT.setNumber(transactionNumber);
		receiveVAT.setType(ClientTransaction.TYPE_RECEIVE_VAT);

		if (transactionDateItem.getEnteredDate() != null)
			receiveVAT.setDate(transactionDateItem.getEnteredDate().getDate());

		receiveVAT.setDepositIn(selectedDepositInAccount.getID());
		receiveVAT.setPaymentMethod(paymentMethod);

		if (billsDue.getValue() != null)
			receiveVAT.setReturnsDueOnOrBefore((billsDue.getValue()).getDate());

		if (selectedTaxAgency != null)
			receiveVAT.setVatAgency(selectedTaxAgency.getID());

		receiveVAT.setTotal(totalAmount);
		receiveVAT.setEndingBalance(endingBalance);

		receiveVAT
				.setClientTransactionReceiveVAT(getTransactionReceiveVATList());

		return receiveVAT;
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
		if (this.transactionObject == null) {
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
		}

	}

	@Override
	public void updateNonEditableItems() {

	}

	@Override
	public List<DynamicForm> getForms() {
		return null;
	}

	@Override
	public void deleteFailed(Throwable caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

	}

	public void onEdit() {

		if (transactionObject.canEdit) {
			Accounter.showWarning(AccounterWarningType.PAYVAT_EDITING,
					AccounterType.WARNING, new ErrorDialogHandler() {

						@Override
						public boolean onYesClick()
								throws InvalidEntryException {
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
								public void onSuccess(Boolean result) {
									if (result) {
										enableFormItems();
									} else

										onFailure(new Exception());
								}

							};
							if (receiveVAT != null) {
								AccounterCoreType type = UIUtils
										.getAccounterCoreType(receiveVAT
												.getType());
								rpcDoSerivce.voidTransaction(type,
										receiveVAT.id, callback);
							}

						}

						@Override
						public boolean onNoClick() throws InvalidEntryException {

							return true;
						}

						@Override
						public boolean onCancelClick()
								throws InvalidEntryException {

							return true;
						}
					});
		}

	}

	private void enableFormItems() {
		isEdit = false;
		date.setDisabled(isEdit);
		paymentMethodCombo.setDisabled(isEdit);
		billsDue.setDisabled(isEdit);
		vatAgencyCombo.setDisabled(isEdit);
		depositInAccCombo.setDisabled(isEdit);
		super.onEdit();

		fillGrid();
		transactionObject = null;

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

}