package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayVAT;
import com.vimukti.accounter.web.client.core.ClientPayVATEntries;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionPayVAT;
import com.vimukti.accounter.web.client.core.ClientVATReturn;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PayFromAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXAgencyCombo;
import com.vimukti.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.TransactionPayVATGrid;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

public class PayVATView extends AbstractTransactionBaseView<ClientPayVAT> {

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
	protected ClientTAXAgency selectedVATAgency;
	private VerticalPanel gridLayout;
	private TransactionPayVATGrid grid;
	private Double totalAmount = 0.0D;
	private String transactionNumber;
	protected List<ClientPayVATEntries> entries;
	private ClientTAXAgency selectedTaxAgency;
	private double endingBalance;
	private ClientPayVAT payVAT;
	private ArrayList<ClientPayVATEntries> filterList;
	private ArrayList<ClientPayVATEntries> tempList;
	private ClientFinanceDate dueDateOnOrBefore;
	private DynamicForm fileterForm;
	private TextItem transNumber;
	private AccounterConstants companyConstants = Accounter.constants();

	public PayVATView() {
		super(ClientTransaction.TYPE_PAY_SALES_TAX, PAYVAT_TRANSACTION_GRID);
		this.validationCount = 3;
	}

	@Override
	protected void createControls() {
		listforms = new ArrayList<DynamicForm>();

		// setTitle(UIUtils.title(FinanceApplication.constants()
		// .payVAT()));

		Label lab = new Label(Accounter.constants().payVAT());
		lab.removeStyleName("gwt-Label");
		lab.setStyleName(Accounter.constants().labelTitle());
		// lab.setHeight("35px");
		transactionDateItem = createTransactionDateItem();

		transNumber = createTransactionNumberItem();
		transNumber.setTitle(Accounter.constants().no());

		payFromAccCombo = new PayFromAccountsCombo(companyConstants.payFrom());
		payFromAccCombo.setHelpInformation(true);
		payFromAccCombo.setAccountTypes(UIUtils
				.getOptionsByType(AccountCombo.PAY_FROM_COMBO));
		payFromAccCombo.setRequired(true);
		payFromAccCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectedPayFromAccount = selectItem;
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

		payFromAccCombo.setDisabled(isEdit);
		payFromAccCombo.setPopupWidth("500px");
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
		datepanel.setCellHorizontalAlignment(dateForm, ALIGN_RIGHT);

		mainform = new DynamicForm();
		// filterForm.setWidth("100%");
		mainform = UIUtils.form(companyConstants.filter());
		mainform.setFields(payFromAccCombo, paymentMethodCombo, billsDue);
		mainform.setWidth("80%");

		// fileterForm = new DynamicForm();
		// fileterForm.setFields(billsDue);
		// fileterForm.setWidth("80%");

		amountText = new AmountField(companyConstants.amount());
		amountText.setHelpInformation(true);
		amountText.setValue("" + UIUtils.getCurrencySymbol() + " 0.00");
		amountText.setDisabled(true);

		endingBalanceText = new AmountField(companyConstants.endingBalance());
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

		VerticalPanel rightVlay = new VerticalPanel();
		rightVlay.add(balForm);

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.setWidth("100%");
		topHLay.setSpacing(10);
		topHLay.add(leftVLay);
		topHLay.add(rightVlay);
		topHLay.setCellWidth(leftVLay, "50%");
		topHLay.setCellWidth(rightVlay, "36%");

		Label lab1 = new Label("" + companyConstants.billsToPay() + "");

		initListGrid();

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(lab);
		mainVLay.add(datepanel);
		mainVLay.add(topHLay);
		mainVLay.add(lab1);
		mainVLay.add(gridLayout);
		canvas.add(mainVLay);
		setSize("100%", "100%");
		/* Adding dynamic forms in list */
		listforms.add(mainform);
		listforms.add(balForm);

		selectedPayFromAccount = payFromAccCombo.getSelectedValue();
		initialEndingBalance = !DecimalUtil.isEquals(
				selectedPayFromAccount.getTotalBalance(), 0) ? selectedPayFromAccount
				.getTotalBalance() : 0D;

		calculateEndingBalance();

	}

	protected void filterGrid() {
		filterList = new ArrayList<ClientPayVATEntries>();
		tempList = new ArrayList<ClientPayVATEntries>();

		filterList.addAll(entries);

		if (dueDateOnOrBefore != null) {
			for (ClientPayVATEntries cont : filterList) {
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

		if (selectedPayFromAccount != null) {

			if (selectedPayFromAccount.isIncrease())
				endingBalance = initialEndingBalance + totalAmount;
			else
				endingBalance = initialEndingBalance - totalAmount;

			endingBalanceText.setValue(DataUtils
					.getAmountAsString(endingBalance));

		}
	}

	protected void filterlistbyVATAgency(ClientTAXAgency selectedVATAgency) {
		List<ClientTransactionPayVAT> filterRecords = new ArrayList<ClientTransactionPayVAT>();
		String selectedagency = selectedVATAgency.getName();
		for (ClientTransactionPayVAT payVAT : grid.getRecords()) {
			String taxAgencyname = getCompany().getTaxAgency(
					payVAT.getTaxAgency()).getName();
			if (taxAgencyname.equals(selectedagency))
				filterRecords.add(payVAT);
		}
		grid.setRecords(filterRecords);
	}

	// initializes the grid.
	private void initListGrid() {

		gridLayout = new VerticalPanel();
		gridLayout.setWidth("100%");
		grid = new TransactionPayVATGrid(!isEdit, true);
		grid.setCanEdit(!isEdit);
		grid.isEnable = false;
		grid.init();
		grid.setPayVATView(this);
		grid.setDisabled(isEdit);
		grid.setHeight("200px");
		if (!isEdit) {
			// grid.addFooterValue("Total", 1);
			// grid
			// .updateFooterValues(DataUtils
			// .getAmountAsString(totalAmount), 2);
		}
		gridLayout.add(grid);

	}

	@Override
	protected void initTransactionViewData(ClientTransaction transactionObject) {
		payVAT = (ClientPayVAT) transactionObject;
		selectedPayFromAccount = getCompany().getAccount(payVAT.getPayFrom());
		payFromAccCombo.setComboItem(selectedPayFromAccount);
		selectedVATAgency = getCompany().getTaxAgency(payVAT.getVatAgency());
		if (selectedVATAgency != null)
			taxAgencyCombo.setComboItem(selectedVATAgency);

		billsDue.setEnteredDate(new ClientFinanceDate(payVAT
				.getReturnsDueOnOrBefore()));
		transactionDateItem.setEnteredDate(payVAT.getDate());
		transNumber.setValue(payVAT.getNumber());

		endingBalanceText.setAmount(payVAT.getEndingBalance());
		paymentMethodCombo.setComboItem(payVAT.getPaymentMethod());
		amountText.setValue(payVAT.getTotal());
		List<ClientTransactionPayVAT> list = payVAT
				.getClientTransactionPayVAT();
		int count = 0;
		for (ClientTransactionPayVAT record : list) {
			if (record != null) {
				grid.addData(record);
				grid.selectRow(count);
				count++;
			}
		}
		// grid.updateFooterValues("Total"
		// + DataUtils.getAmountAsString(payVAT.getTotal()), 2);

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
		payFromAccCombo.setAccounts();
		// payFromCombo.setDisabled(isEdit);
		ClientAccount payFromAccount = payFromAccCombo.getSelectedValue();
		if (payFromAccount != null)
			payFromAccCombo.setComboItem(payFromAccount);
	}

	private void fillGrid() {
		grid.addLoadingImagePanel();
		rpcUtilService
				.getPayVATEntries(new AccounterAsyncCallback<List<ClientPayVATEntries>>() {

					@Override
					public void onException(AccounterException caught) {
						Accounter.showError(Accounter.constants()
								.failedtogettheTransactionPayVATList());
						grid.addEmptyMessage(Accounter.constants()
								.noRecordsToShow());

					}

					@Override
					public void onSuccess(List<ClientPayVATEntries> result) {
						if (result == null) {

							onException(null);
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
	protected void loadData(List<ClientPayVATEntries> result) {

		List<ClientTransactionPayVAT> records = new ArrayList<ClientTransactionPayVAT>();
		for (ClientPayVATEntries entry : result) {
			ClientTransactionPayVAT clientEntry = new ClientTransactionPayVAT();

			clientEntry.setTaxAgency(entry.getVatAgency());
			clientEntry.setVatReturn(entry.getVatReturn());
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

		rpcUtilService.getNextTransactionNumber(ClientTransaction.TYPE_PAY_VAT,
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
	public boolean validate() throws InvalidEntryException,
			InvalidTransactionEntryException {
		switch (this.validationCount) {
		case 3:
			return AccounterValidator.validateForm(mainform, false);
		case 2:
			if (grid.getRecords().isEmpty()) {
				throw new InvalidTransactionEntryException(Accounter
						.constants().youdonthaveanyfiledVATentriestoselect());
			} else {
				return AccounterValidator.validateGrid(grid);
			}
		case 1:
			return AccounterValidator.validateAmount(totalAmount);
		default:
			return false;
		}

	}

	@Override
	public void saveAndUpdateView() throws Exception {
		transactionObject = getPaySalesTax();
		ClientPayVAT payVAT = getPaySalesTax();
		super.saveAndUpdateView();
		createObject(payVAT);
	}

	private ClientPayVAT getPaySalesTax() {

		ClientPayVAT payVAT = new ClientPayVAT();

		payVAT.setNumber(transactionNumber);
		payVAT.setType(ClientTransaction.TYPE_PAY_VAT);

		if (transactionDateItem.getEnteredDate() != null)
			payVAT.setDate(transactionDateItem.getEnteredDate().getDate());

		payVAT.setPayFrom(selectedPayFromAccount.getID());
		payVAT.setPaymentMethod(paymentMethod);

		if (billsDue.getValue() != null)
			payVAT.setReturnsDueOnOrBefore((billsDue.getValue()).getDate());

		if (selectedTaxAgency != null)
			payVAT.setVatAgency(selectedTaxAgency.getID());

		payVAT.setTotal(totalAmount);
		payVAT.setEndingBalance(endingBalance);

		payVAT.setClientTransactionPayVAT(getTransactionPayVATList());
		transactionObject = payVAT;
		return payVAT;
	}

	private List<ClientTransactionPayVAT> getTransactionPayVATList() {

		// List<ClientTransactionPayVAT> payVATList = new
		// ArrayList<ClientTransactionPayVAT>();
		//
		// for (ClientTransactionPayVAT rec : grid.getSelectedRecords()) {
		// // rec.setTransaction(paySalesTax);
		// rec.setVatReturn(vatReturn)
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
			if (selectedPayFromAccount != null) {
				double toBeSetEndingBalance = 0.0;
				if (selectedPayFromAccount.isIncrease())
					toBeSetEndingBalance = selectedPayFromAccount
							.getTotalBalance()

							+ DataUtils.getBalance(
									amountText.getAmount().toString())
									.doubleValue();
				else
					toBeSetEndingBalance = selectedPayFromAccount
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
		// TODO Auto-generated method stub

	}

	@Override
	public List<DynamicForm> getForms() {
		// NOTHING TO DO.
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
											.failedtovoidPayVAT());

								}

								@Override
								public void onSuccess(Boolean result) {
									if (result) {
										enableFormItems();
									} else

										onFailure(new Exception());
								}

							};
							if (payVAT != null) {
								AccounterCoreType type = UIUtils
										.getAccounterCoreType(payVAT.getType());
								rpcDoSerivce.voidTransaction(type, payVAT.id,
										callback);
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
		taxAgencyCombo.setDisabled(isEdit);
		payFromAccCombo.setDisabled(isEdit);
		super.onEdit();

		fillGrid();
		transactionObject = null;

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
		return Accounter.constants().payVAT();
	}

}
