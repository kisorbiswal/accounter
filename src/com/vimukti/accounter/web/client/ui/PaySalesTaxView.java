package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPaySalesTax;
import com.vimukti.accounter.web.client.core.ClientPaySalesTaxEntries;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientTransactionPaySalesTax;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
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
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.AbstractTransactionGrid;
import com.vimukti.accounter.web.client.ui.grids.TransactionPaySalesTaxGrid;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

/**
 * @author Ravi kiran Garikapati
 * @modified by B.Srinivasa Rao
 * 
 */
public class PaySalesTaxView extends
		AbstractTransactionBaseView<ClientPaySalesTax> {
	private DateField billsDue;
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
	AccounterConstants accounterConstants = Accounter.constants();
	AccounterMessages accounterMessages = Accounter.messages();

	private TextItem transNumber;

	public PaySalesTaxView() {

		super(ClientTransaction.TYPE_PAY_SALES_TAX);
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
						Accounter.showError(Accounter.constants()
								.failedToGetTransactionNumber());
					}

					public void onResultSuccess(String result) {
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
		rpcUtilService
				.getPaySalesTaxEntries(
						billsDue.getEnteredDate().getDate(),
						new AccounterAsyncCallback<ArrayList<ClientPaySalesTaxEntries>>() {

							public void onException(AccounterException caught) {
								Accounter
										.showError(Accounter
												.constants()
												.failedtogettheTransactionPaySalesTaxList());
								grid.addEmptyMessage(Accounter.constants()
										.noRecordsToShow());

							}

							public void onResultSuccess(
									ArrayList<ClientPaySalesTaxEntries> result) {

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
		gridLayout.setWidth("100%");
		grid = new TransactionPaySalesTaxGrid(!isInViewMode(), true);
		grid.setCurrentView(this);
		grid.setCanEdit(true);
		grid.isEnable = false;
		grid.init();
		grid.setDisabled(isInViewMode());
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
		if (!isInViewMode()) {
			this.amountText.setAmount(totalAmount);
			this.endingBalanceText.setAmount(endingBalance);
		}
	}

	@Override
	public void saveAndUpdateView() {
		updateTransaction();
		saveOrUpdate(transaction);
	}

	protected void updateTransaction() {
		super.updateTransaction();
		transaction.setNumber(transactionNumber);

		transaction.setType(ClientTransaction.TYPE_PAY_SALES_TAX);
		if (transactionDateItem.getEnteredDate() != null)

			transaction.setDate(transactionDateItem.getEnteredDate().getDate());

		transaction.setPayFrom(selectedPayFromAccount.getID());

		transaction.setPaymentMethod(paymentMethod);
		if (billsDue.getValue() != null)

			transaction.setBillsDueOnOrBefore(((ClientFinanceDate) billsDue
					.getValue()).getDate());

		if (selectedTaxAgency != null)
			transaction.setTaxAgency(selectedTaxAgency.getID());

		transaction.setTotal(totalAmount);

		transaction.setEndingBalance(endingBalance);

		transaction.setTransactionPaySalesTax(getTransactionPatSalesTaxList());

	}

	private List<ClientTransactionPaySalesTax> getTransactionPatSalesTaxList() {

		List<ClientTransactionPaySalesTax> tpsList = new ArrayList<ClientTransactionPaySalesTax>();

		for (ClientTransactionPaySalesTax rec : grid.getSelectedRecords()) {
			rec.setTransaction(transaction);
			tpsList.add(rec);

		}

		return tpsList;
	}

	@Override
	protected void createControls() {
		listforms = new ArrayList<DynamicForm>();

		Label lab = new Label(Accounter.constants().paySalesTax());
		lab.setStyleName(Accounter.constants().labelTitle());

		transactionDateItem = createTransactionDateItem();
		transNumber = createTransactionNumberItem();

		DynamicForm dateForm = new DynamicForm();
		dateForm.setNumCols(4);
		dateForm.setStyleName("datenumber-panel");
		dateForm.setFields(transactionDateItem, transNumber);

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
		payFromAccCombo.setDisabled(isInViewMode());
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
				if (!isInViewMode())
					fillGrid();
				// if (transactionObject == null)
				// loadData(getfilterRecordsByDate(date, entries));
			}
		});
		billsDue.setDisabled(isInViewMode());
		taxAgencyCombo = new TAXAgencyCombo(Accounter.constants().taxAgency());
		taxAgencyCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXAgency>() {

					public void selectedComboBoxItem(ClientTAXAgency selectItem) {

						selectedTaxAgency = selectItem;
						if (selectedTaxAgency != null)
							filterlistbyTaxAgency(selectedTaxAgency);

					}

				});
		taxAgencyCombo.setDisabled(isInViewMode());
		filterForm = new DynamicForm();
		// filterForm.setWidth("100%");
		filterForm = UIUtils.form(Accounter.constants().filter());
		filterForm.setFields(payFromAccCombo, paymentMethodCombo, billsDue,
				taxAgencyCombo);
		filterForm.getCellFormatter().setWidth(0, 0, "210px");

		amountText = new AmountField(Accounter.constants().amount(), this);
		amountText.setValue("" + UIUtils.getCurrencySymbol() + " 0.00");
		amountText.setDisabled(true);

		endingBalanceText = new AmountField(Accounter.constants()
				.endingBalance(), this);
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
		this.add(mainVLay);
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
	protected void initTransactionViewData() {
		if (transaction == null) {
			setData(new ClientPaySalesTax());
			initTransactionNumber();
			initTaxAgencyCombo();
			getTaxItems();
			getPayFromAccounts();
			fillGrid();
			selectedPayFromAccount = payFromAccCombo.getSelectedValue();
			return;
		}
		setMode(EditMode.VIEW);

		selectedPayFromAccount = getCompany().getAccount(
				transaction.getPayFrom());
		payFromAccCombo.setComboItem(selectedPayFromAccount);
		selectedTaxAgency = getCompany().getTaxAgency(
				transaction.getTaxAgency());
		if (selectedTaxAgency != null)
			taxAgencyCombo.setComboItem(selectedTaxAgency);
		billsDue.setEnteredDate(new ClientFinanceDate(transaction
				.getBillsDueOnOrBefore()));
		transactionDateItem.setEnteredDate(transaction.getDate());
		endingBalanceText.setAmount(transaction.getEndingBalance());
		paymentMethodCombo.setComboItem(paymentMethod);
		amountText.setAmount(transaction.getTotal());
		List<ClientTransactionPaySalesTax> list = transaction
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
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();

		// Validations
		// 1. is valid transaction date?
		// 2. is in prevent posting before date?
		// 3. filter form valid?
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
		result.add(filterForm.validate());
		if (grid == null || grid.getRecords().isEmpty()) {
			result.addError(grid, accounterMessages
					.noTransactionsTo(accounterConstants.paySalesTax()));
		} else {
			result.add(grid.validateGrid());

			if (AccounterValidator.isZeroAmount(totalAmount)) {
				result.addError(amountText,
						accounterMessages.shouldNotbeZero(amountText.getName()));
			} else if (AccounterValidator.isNegativeAmount(totalAmount)) {
				result.addError(amountText, accounterMessages
						.shouldBePositive(amountText.getName()));
			}
		}
		return result;
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
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	public void setFilterByDateList(
			List<ClientTransactionPaySalesTax> filterByDateList) {
		this.filterByDateList = filterByDateList;
	}

	public List<ClientTransactionPaySalesTax> getFilterByDateList() {
		return filterByDateList;
	}

	public void onEdit() {
		if (transaction.canEdit) {
			Accounter.showWarning(AccounterWarningType.PAYSALESTAX_EDITING,
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
											.failedtovoidPaySalesTax());
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
		transactionDateItem.setDisabled(isInViewMode());
		payFromAccCombo.setDisabled(isInViewMode());
		paymentMethodCombo.setDisabled(isInViewMode());
		billsDue.setDisabled(isInViewMode());
		taxAgencyCombo.setDisabled(isInViewMode());
		grid.setDisabled(isInViewMode());

		super.onEdit();
		fillGrid();
		transaction = null;

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

	@Override
	public AbstractTransactionGrid<ClientTransactionItem> getTransactionGrid() {
		return null;
	}
}
