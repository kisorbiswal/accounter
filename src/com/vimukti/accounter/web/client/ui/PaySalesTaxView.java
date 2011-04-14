package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
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
import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PayFromAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXAgencyCombo;
import com.vimukti.accounter.web.client.ui.company.CompanyMessages;
import com.vimukti.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.core.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
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

	@SuppressWarnings("unused")
	private List<ClientTAXItem> taxItems;
	private List<ClientPaySalesTaxEntries> entries;
	List<ClientTAXAgency> taxAgencies = new ArrayList<ClientTAXAgency>();
	private ArrayList<DynamicForm> listforms;

	private CompanyMessages companyConstants = GWT
			.create(CompanyMessages.class);
	private ClientPaySalesTax paySalesTax;

	public PaySalesTaxView() {

		super(ClientTransaction.TYPE_PAY_SALES_TAX,
				PAYSALESTAX_TRANSACTION_GRID);
		this.validationCount = 3;
	}

	private void getTaxItems() {

		taxItems = FinanceApplication.getCompany().getActiveTaxItems();

	}

	private void getPayFromAccounts() {
		payFromAccounts = payFromAccCombo.getAccounts();
		payFromAccCombo.initCombo(payFromAccounts);

	}

	protected void initTransactionNumber() {

		rpcUtilService.getNextTransactionNumber(
				ClientTransaction.TYPE_PAY_SALES_TAX,
				new AsyncCallback<String>() {

					public void onFailure(Throwable caught) {

						Accounter
								.showError("Failed to get the transaction number");
						return;
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
				.getTime(),
				new AsyncCallback<List<ClientPaySalesTaxEntries>>() {

					public void onFailure(Throwable caught) {

						Accounter
								.showError("Failed to get the TransactionPaySalesTaxList");
						grid.addEmptyMessage(FinanceApplication
								.getCustomersMessages().norecordstoshow());

						return;

					}

					public void onSuccess(List<ClientPaySalesTaxEntries> result) {

						if (result == null) {

							onFailure(null);
						}
						entries = result;
						if (result.size() == 0) {
							// Accounter
							// .showInformation("No PaySalesTax list to show");
							grid.addEmptyMessage(FinanceApplication
									.getCustomersMessages().norecordstoshow());
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
		allTaxAgencies = FinanceApplication.getCompany().getActiveTaxAgencies();
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

		grid.init();
		grid.setDisabled(isEdit);
		grid.setHeight("200px");
		if (!isEdit)
			// grid.updateFooterValues(FinanceApplication.getVendorsMessages()
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

		this.amountText.setAmount(totalAmount);
		this.endingBalanceText.setAmount(endingBalance);

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

			paySalesTax.setDate(date.getEnteredDate().getTime());

		paySalesTax.setPayFrom(selectedPayFromAccount.getStringID());

		paySalesTax.setPaymentMethod(paymentMethod);
		if (billsDue.getValue() != null)

			paySalesTax.setBillsDueOnOrBefore(((ClientFinanceDate) billsDue
					.getValue()).getTime());

		if (selectedTaxAgency != null)
			paySalesTax.setTaxAgency(selectedTaxAgency.getStringID());

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

		setTitle(UIUtils.title(FinanceApplication.getFinanceUIConstants()
				.paySalesTax()));

		Label lab = new Label(FinanceApplication.getFinanceUIConstants()
				.paySalesTax());
		lab
				.setStyleName(FinanceApplication.getCustomersMessages()
						.lableTitle());

		date = new DateField(companyConstants.date());
		date.setTitle(companyConstants.date());
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

		payFromAccCombo = new PayFromAccountsCombo(companyConstants.payFrom());
		payFromAccCombo.setAccountTypes(UIUtils
				.getOptionsByType(AccountCombo.PAY_FROM_COMBO));
		payFromAccCombo.setRequired(true);

		payFromAccCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {
					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectedPayFromAccount = selectItem;
						initialEndingBalance = !DecimalUtil.isEquals(
								selectedPayFromAccount.getTotalBalance(), 0) ? selectedPayFromAccount
								.getTotalBalance()
								: 0D;
						calculateEndingBalance();

						endingBalanceText.setValue(DataUtils
								.getAmountAsString(endingBalance));

					}

				});
		payFromAccCombo.setDisabled(isEdit);
		paymentMethodCombo = createPaymentMethodSelectItem();
		paymentMethodCombo.setRequired(true);
		paymentMethodCombo.setWidth(100);
		billsDue = new DateField(companyConstants.billsDueOnOrBefore());
		billsDue.setTitle(companyConstants.billsDueOnOrBefore());
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
		taxAgencyCombo = new TAXAgencyCombo(companyConstants.taxAgency());
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
		filterForm = UIUtils.form(companyConstants.filter());
		filterForm.setFields(date, payFromAccCombo, paymentMethodCombo,
				billsDue, taxAgencyCombo);

		amountText = new AmountField(companyConstants.amount());
		amountText.setValue("" + UIUtils.getCurrencySymbol() + " 0.00");
		amountText.setDisabled(true);

		endingBalanceText = new AmountField(companyConstants.endingBalance());
		endingBalanceText.setValue("" + UIUtils.getCurrencySymbol() + " 0.00");
		endingBalanceText.setDisabled(true);

		balForm = new DynamicForm();
		balForm = UIUtils.form(companyConstants.balances());
		balForm.setFields(amountText, endingBalanceText);

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setWidth("100%");
		leftVLay.add(filterForm);

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.setWidth("100%");
		topHLay.setSpacing(10);
		topHLay.add(filterForm);
		topHLay.add(leftVLay);
		topHLay.add(balForm);

		Label lab1 = new Label("" + companyConstants.billsToPay() + "");

		initListGrid();

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(lab);
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
				if (trpaySalesTax.getTaxAgency() != null) {
					String taxAgencyname = FinanceApplication.getCompany()
							.getTaxAgency(trpaySalesTax.getTaxAgency())
							.getName();
					String selectedagency = selectedTaxAgency.getName();
					if (taxAgencyname.equals(selectedagency))
						filterRecords.add(trpaySalesTax);
				}
				grid.setRecords(filterRecords);
			}
	}

	@SuppressWarnings("deprecation")
	protected List<ClientPaySalesTaxEntries> getfilterRecordsByDate(
			ClientFinanceDate fillterdate,
			List<ClientPaySalesTaxEntries> entrylist) {
		List<ClientPaySalesTaxEntries> filterlist = new ArrayList<ClientPaySalesTaxEntries>();
		ClientFinanceDate transactionDate;
		ClientFinanceDate firstDate = new ClientFinanceDate(fillterdate
				.getYear(), fillterdate.getMonth() - 1, 01);
		ClientFinanceDate lastDate = new ClientFinanceDate(fillterdate
				.getYear(), fillterdate.getMonth(), 01);
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
		paySalesTax = (ClientPaySalesTax) transactionObject;

		selectedPayFromAccount = FinanceApplication.getCompany().getAccount(
				paySalesTax.getPayFrom());
		payFromAccCombo.setComboItem(selectedPayFromAccount);
		selectedTaxAgency = FinanceApplication.getCompany().getTaxAgency(
				paySalesTax.getTaxAgency());
		if (selectedTaxAgency != null)
			taxAgencyCombo.setComboItem(selectedTaxAgency);
		billsDue.setEnteredDate(new ClientFinanceDate(paySalesTax
				.getBillsDueOnOrBefore()));
		date.setEnteredDate(paySalesTax.getDate());
		endingBalanceText.setAmount(paySalesTax.getEndingBalance());
		paymentMethodCombo.setComboItem(paymentMethod);
		amountText.setValue(paySalesTax.getTotal());
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
//		grid.updateFooterValues(FinanceApplication.getVendorsMessages().total()
//				+ DataUtils.getAmountAsString(paySalesTax.getTotal()), 2);

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
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(Boolean result) {
		// TODO Auto-generated method stub

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
							AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

								@Override
								public void onFailure(Throwable caught) {
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
										paySalesTax.stringID, callback);
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
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}
}
