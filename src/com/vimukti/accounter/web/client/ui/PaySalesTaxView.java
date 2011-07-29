package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPaySalesTax;
import com.vimukti.accounter.web.client.core.ClientPaySalesTaxEntries;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionPaySalesTax;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
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
import com.vimukti.accounter.web.client.ui.grids.TransactionPaySalesTaxGrid;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

/**
 * @author Ravi kiran Garikapati
 * @modified by B.Srinivasa Rao
 * 
 */
public class PaySalesTaxView extends
		AbstractTransactionBaseView<ClientPaySalesTax> {
	private DateField date, billsDue;
	private AmountField amountText, endingBalanceText;
	private PayFromAccountsCombo payFromAccCombo;
	private TAXAgencyCombo taxAgencyCombo;

	private TransactionPaySalesTaxGrid grid;
	private VerticalPanel gridLayout;
	private DynamicForm filterForm;
	private DynamicForm balForm;

	public Double totalAmount = 0D;
	private Double endingBalance = 0D;
	private Double initialEndingBalance = 0D;
	private String transactionNumber;
	protected ClientTAXAgency selectedTaxAgency;
	private ClientAccount selectedPayFromAccount;
	private List<ClientTAXAgency> allTaxAgencies;
	private List<ClientAccount> payFromAccounts;
	private List<ClientTransactionPaySalesTax> filterByDateList;

	private List<ClientTAXItem> taxItems;
	private List<ClientPaySalesTaxEntries> entries;
	List<ClientTAXAgency> taxAgencies = new ArrayList<ClientTAXAgency>();
	private ArrayList<DynamicForm> listforms;

	private ClientPaySalesTax paySalesTax;
	private TextItem transNumber;

	public PaySalesTaxView() {

		super(ClientTransaction.TYPE_PAY_SALES_TAX,
				PAYSALESTAX_TRANSACTION_GRID);
		this.validationCount = 3;
	}

	private void getTaxItems() {

		taxItems = getCompany().getActiveTaxItems();

	}

	private void getPayFromAccounts() {
		payFromAccounts = payFromAccCombo.getAccounts();
		payFromAccCombo.initCombo(payFromAccounts);

	}

	protected void initTransactionNumber() {

		rpcUtilService.getNextTransactionNumber(
				ClientTransaction.TYPE_PAY_SALES_TAX,
				new AccounterAsyncCallback<String>() {

					public void onException(AccounterException caught) {
						Accounter
								.showError("Failed to get the transaction number");
					}

					public void onSuccess(String result) {
						if (result == null)
							onFailure(null);
						transactionNumber = result;
					}

				});

	}

	// fetches data from server and fills the grid.
	private void fillGrid() {
		grid.removeAllRecords();
		grid.addLoadingImagePanel();
		rpcUtilService.getPaySalesTaxEntries(billsDue.getEnteredDate()
				.getDate(),
				new AccounterAsyncCallback<List<ClientPaySalesTaxEntries>>() {

					public void onException(AccounterException caught) {
						Accounter
								.showError("Failed to get the TransactionPaySalesTaxList");
						grid.addEmptyMessage(Accounter.constants()
								.noRecordsToShow());

					}

					public void onSuccess(List<ClientPaySalesTaxEntries> result) {

						if (result == null) {

							onFailure(null);
						}
						entries = result;
						if (result.size() == 0) {
							// Accounter
							// .showInformation("No PaySalesTax list to show");
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
	protected void loadData(List<ClientPaySalesTaxEntries> result) {
		List<ClientTransactionPaySalesTax> records = new ArrayList<ClientTransactionPaySalesTax>();
		for (ClientPaySalesTaxEntries entry : result) {
			ClientTransactionPaySalesTax record = new ClientTransactionPaySalesTax();
			record.setPaySalesTaxEntry(entry);
			record.setTaxItem(entry.getTaxItem());
			record.setTaxDue(entry.getBalance());
			record.setTaxAgency(entry.getTaxAgency());
			record.setTaxRateCalculation(entry.getTaxRateCalculation());
			record.setTaxAdjustment(entry.getTaxAdjustment());
			record.setAmountToPay(0.0);
			records.add(record);
		}
		setFilterByDateList(records);
		if (selectedTaxAgency == null)
			grid.setRecords(records);
		else
			filterlistbyTaxAgency(selectedTaxAgency);

	}

	private void initTaxAgencyCombo() {
		allTaxAgencies = getCompany().getActiveTaxAgencies();
		taxAgencyCombo.initCombo(allTaxAgencies);
	}

	// resets all non-editable amount fields.
	public void refreshAmounts() {

		calculateEndingBalance();

		updateAmountAndEndingBalanceItems();

	}

	// initializes the grid.
	private void initListGrid() {

		gridLayout = new VerticalPanel();
		grid = new TransactionPaySalesTaxGrid(!isEdit, true);
		grid.setCurrentView(this);
		grid.setCanEdit(true);
		grid.isEnable = false;
		grid.init();
		grid.setDisabled(isEdit);
		grid.setHeight("200px");
		// if (!isEdit)
		// grid.updateFooterValues(FinanceApplication.constants()
		// .total()
		// + DataUtils.getAmountAsString(totalAmount), 3);

		gridLayout.add(grid);

	}

	private void calculateEndingBalance() {

		if (selectedPayFromAccount != null) {

			if (selectedPayFromAccount.isIncrease())
				endingBalance = initialEndingBalance + totalAmount;
			else
				endingBalance = initialEndingBalance - totalAmount;

		}
	}

	private void updateAmountAndEndingBalanceItems() {
		if (!isEdit) {
			this.amountText.setAmount(totalAmount);
			this.endingBalanceText.setAmount(endingBalance);
		}
	}

	@Override
	public void saveAndUpdateView() throws Exception {
		ClientPaySalesTax paySalesTax = getPaySalesTax();
		createObject(paySalesTax);
	}

	private ClientPaySalesTax getPaySalesTax() {

		ClientPaySalesTax paySalesTax = new ClientPaySalesTax();

		paySalesTax.setNumber(transactionNumber);

		paySalesTax.setType(ClientTransaction.TYPE_PAY_SALES_TAX);
		if (date.getEnteredDate() != null)

			paySalesTax.setDate(date.getEnteredDate().getDate());

		paySalesTax.setPayFrom(selectedPayFromAccount.getID());

		paySalesTax.setPaymentMethod(paymentMethod);
		if (billsDue.getValue() != null)

			paySalesTax.setBillsDueOnOrBefore(((ClientFinanceDate) billsDue
					.getValue()).getDate());

		if (selectedTaxAgency != null)
			paySalesTax.setTaxAgency(selectedTaxAgency.getID());

		paySalesTax.setTotal(totalAmount);

		paySalesTax.setEndingBalance(endingBalance);

		paySalesTax
				.setTransactionPaySalesTax(getTransactionPatSalesTaxList(paySalesTax));

		return paySalesTax;
	}

	private List<ClientTransactionPaySalesTax> getTransactionPatSalesTaxList(
			ClientPaySalesTax paySalesTax) {

		List<ClientTransactionPaySalesTax> tpsList = new ArrayList<ClientTransactionPaySalesTax>();

		for (ClientTransactionPaySalesTax rec : grid.getSelectedRecords()) {
			rec.setTransaction(paySalesTax);
			tpsList.add(rec);

		}

		return tpsList;
	}

	@Override
	protected void createControls() {
		listforms = new ArrayList<DynamicForm>();

		Label lab = new Label(Accounter.constants().paySalesTax());
		lab.setStyleName(Accounter.constants().labelTitle());

		date = new DateField(null);
		// date.setTitle(Accounter.constants().date());
		date.setEnteredDate(new ClientFinanceDate());
		date.setDisabled(isEdit);
		date.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String d = event.getValue().toString();
				date.setEnteredDate(new ClientFinanceDate(d));
				// fillGrid();
			}
		});
		transNumber = createTransactionNumberItem();

		DynamicForm dateForm = new DynamicForm();
		dateForm.setNumCols(4);
		dateForm.setStyleName("datenumber-panel");
		dateForm.setFields(date, transNumber);

		payFromAccCombo = new PayFromAccountsCombo(Accounter.constants()
				.payFrom());
		payFromAccCombo.setAccountTypes(UIUtils
				.getOptionsByType(AccountCombo.PAY_FROM_COMBO));
		payFromAccCombo.setRequired(true);

		payFromAccCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {
					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectedPayFromAccount = selectItem;
						initialEndingBalance = !DecimalUtil.isEquals(
								selectedPayFromAccount.getTotalBalance(), 0) ? selectedPayFromAccount
								.getTotalBalance() : 0D;
						calculateEndingBalance();

						endingBalanceText.setValue(DataUtils
								.getAmountAsString(endingBalance));

					}

				});
		payFromAccCombo.setDisabled(isEdit);
		paymentMethodCombo = createPaymentMethodSelectItem();
		paymentMethodCombo.setRequired(true);
		// paymentMethodCombo.setWidth(100);
		billsDue = new DateField(Accounter.constants().billsDueOnOrBefore());
		billsDue.setTitle(Accounter.constants().billsDueOnOrBefore());
		// billsDue.setEnteredDate(new ClientFinanceDate());
		// billsDue.setStartDate(new ClientFinanceDate());
		billsDue.setValue(new ClientFinanceDate());
		billsDue.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate date) {
				if (!isEdit)
					fillGrid();
				// if (transactionObject == null)
				// loadData(getfilterRecordsByDate(date, entries));
			}
		});
		billsDue.setDisabled(isEdit);
		taxAgencyCombo = new TAXAgencyCombo(Accounter.constants().taxAgency());
		taxAgencyCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXAgency>() {

					public void selectedComboBoxItem(ClientTAXAgency selectItem) {

						selectedTaxAgency = selectItem;
						if (selectedTaxAgency != null)
							filterlistbyTaxAgency(selectedTaxAgency);

					}

				});
		taxAgencyCombo.setDisabled(isEdit);
		filterForm = new DynamicForm();
		// filterForm.setWidth("100%");
		filterForm = UIUtils.form(Accounter.constants().filter());
		filterForm.setFields(payFromAccCombo, paymentMethodCombo, billsDue,
				taxAgencyCombo);
		filterForm.getCellFormatter().setWidth(0, 0, "210px");

		amountText = new AmountField(Accounter.constants().amount());
		amountText.setValue("" + UIUtils.getCurrencySymbol() + " 0.00");
		amountText.setDisabled(true);

		endingBalanceText = new AmountField(Accounter.constants()
				.endingBalance());
		endingBalanceText.setValue("" + UIUtils.getCurrencySymbol() + " 0.00");
		endingBalanceText.setDisabled(true);

		balForm = new DynamicForm();
		balForm = UIUtils.form(Accounter.constants().balances());
		balForm.setFields(amountText, endingBalanceText);
		balForm.getCellFormatter().setWidth(0, 0, "222px");

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setWidth("100%");
		leftVLay.add(filterForm);

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.setWidth("100%");
		topHLay.setSpacing(10);
		topHLay.add(filterForm);
		topHLay.add(leftVLay);
		topHLay.add(balForm);
		topHLay.setCellHorizontalAlignment(balForm, ALIGN_RIGHT);

		Label lab1 = new Label("" + Accounter.constants().billsToPay() + "");

		initListGrid();

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(lab);
		mainVLay.add(dateForm);
		mainVLay.setCellHorizontalAlignment(dateForm, ALIGN_RIGHT);
		mainVLay.add(topHLay);
		mainVLay.add(lab1);
		mainVLay.add(gridLayout);
		canvas.add(mainVLay);
		setSize("100%", "100%");
		/* Adding dynamic forms in list */
		listforms.add(filterForm);
		listforms.add(balForm);
	}

	protected void filterlistbyTaxAgency(ClientTAXAgency selectedTaxAgency) {
		List<ClientTransactionPaySalesTax> filterRecords = new ArrayList<ClientTransactionPaySalesTax>();
		if (filterByDateList != null)
			for (ClientTransactionPaySalesTax trpaySalesTax : getFilterByDateList()) {
				if (trpaySalesTax.getTaxAgency() != 0) {
					String taxAgencyname = getCompany().getTaxAgency(
							trpaySalesTax.getTaxAgency()).getName();
					String selectedagency = selectedTaxAgency.getName();
					if (taxAgencyname.equals(selectedagency))
						filterRecords.add(trpaySalesTax);
				}
				grid.setRecords(filterRecords);
			}
	}

	protected List<ClientPaySalesTaxEntries> getfilterRecordsByDate(
			ClientFinanceDate fillterdate,
			List<ClientPaySalesTaxEntries> entrylist) {
		List<ClientPaySalesTaxEntries> filterlist = new ArrayList<ClientPaySalesTaxEntries>();
		ClientFinanceDate transactionDate;
		ClientFinanceDate firstDate = new ClientFinanceDate(
				fillterdate.getYear(), fillterdate.getMonth() - 1, 01);
		ClientFinanceDate lastDate = new ClientFinanceDate(
				fillterdate.getYear(), fillterdate.getMonth(), 01);
		for (ClientPaySalesTaxEntries entry : entrylist) {
			transactionDate = new ClientFinanceDate(entry.getTransactionDate());
			if (isAfterDate(transactionDate, firstDate)
					&& transactionDate.before(lastDate)) {
				filterlist.add(entry);
			}
		}
		return filterlist;
	}

	private boolean isAfterDate(ClientFinanceDate transactionDate,
			ClientFinanceDate firstdate) {
		if (transactionDate.after(firstdate))
			return true;
		else if (UIUtils.isdateEqual(transactionDate, firstdate))
			return true;
		else
			return false;
	}

	@Override
	protected void initTransactionViewData(ClientTransaction transactionObject) {
		isEdit = true;
		paySalesTax = (ClientPaySalesTax) transactionObject;

		selectedPayFromAccount = getCompany().getAccount(
				paySalesTax.getPayFrom());
		payFromAccCombo.setComboItem(selectedPayFromAccount);
		selectedTaxAgency = getCompany().getTaxAgency(
				paySalesTax.getTaxAgency());
		if (selectedTaxAgency != null)
			taxAgencyCombo.setComboItem(selectedTaxAgency);
		billsDue.setEnteredDate(new ClientFinanceDate(paySalesTax
				.getBillsDueOnOrBefore()));
		date.setEnteredDate(paySalesTax.getDate());
		endingBalanceText.setAmount(paySalesTax.getEndingBalance());
		paymentMethodCombo.setComboItem(paymentMethod);
		amountText.setAmount(paySalesTax.getTotal());
		List<ClientTransactionPaySalesTax> list = paySalesTax
				.getTransactionPaySalesTax();
		int count = 0;
		for (ClientTransactionPaySalesTax record : list) {
			if (record != null) {
				grid.removeLoadingImage();
				grid.addData(record);
				grid.selectRow(count);
				count++;
			}
		}
		// grid.updateFooterValues(FinanceApplication.constants().total()
		// + DataUtils.getAmountAsString(paySalesTax.getTotal()), 2);

	}

	@Override
	protected void initTransactionViewData() {

		initTransactionNumber();
		initTaxAgencyCombo();
		getTaxItems();
		getPayFromAccounts();
		fillGrid();

	}

	@Override
	public boolean validate() throws InvalidEntryException,
			InvalidTransactionEntryException {
		switch (this.validationCount) {
		case 3:
			return AccounterValidator.validateForm(filterForm, false);
		case 2:
			return AccounterValidator.validateGrid(grid);
		case 1:
			return AccounterValidator.validateAmount(totalAmount);
		default:
			return false;
		}

	}

	@Override
	public void updateNonEditableItems() {
		// TODO Auto-generated method stub

	}

	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
	}

	@Override
	public void deleteFailed(Throwable caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

	}

	public void setFilterByDateList(
			List<ClientTransactionPaySalesTax> filterByDateList) {
		this.filterByDateList = filterByDateList;
	}

	public List<ClientTransactionPaySalesTax> getFilterByDateList() {
		return filterByDateList;
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		switch (command) {
		case AccounterCommand.CREATION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.payFromAccCombo.addComboItem((ClientAccount) core);
			if (core.getObjectType() == AccounterCoreType.TAXAGENCY)
				this.taxAgencyCombo
						.addItemThenfireEvent((ClientTAXAgency) core);

			break;
		case AccounterCommand.DELETION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.payFromAccCombo.removeComboItem((ClientAccount) core);
			if (core.getObjectType() == AccounterCoreType.TAXAGENCY)
				this.taxAgencyCombo.removeComboItem((ClientTAXAgency) core);

			break;
		case AccounterCommand.UPDATION_SUCCESS:
			break;
		}

	}

	public void onEdit() {
		if (transactionObject.canEdit) {
			Accounter.showWarning(AccounterWarningType.PAYSALESTAX_EDITING,
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
									Accounter
											.showError("Failed to void Pay Sales Tax");
								}

								@Override
								public void onSuccess(Boolean result) {
									if (result) {
										enableFormItems();
									} else

										onFailure(new Exception());
								}

							};
							if (paySalesTax != null) {
								AccounterCoreType type = UIUtils
										.getAccounterCoreType(paySalesTax
												.getType());
								rpcDoSerivce.voidTransaction(type,
										paySalesTax.id, callback);
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
		payFromAccCombo.setDisabled(isEdit);
		paymentMethodCombo.setDisabled(isEdit);
		billsDue.setDisabled(isEdit);
		taxAgencyCombo.setDisabled(isEdit);
		grid.setDisabled(isEdit);

		super.onEdit();
		fillGrid();
		transactionObject = null;

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {
		// NOTHING TO DO.
	}

	@Override
	protected String getViewTitle() {
		return UIUtils.title(Accounter.constants().paySalesTax());
	}
}
