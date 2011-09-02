package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayBill;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientTransactionPayBill;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.core.Lists.PayBillTransactionList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PayFromAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.combo.VendorCombo;
import com.vimukti.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.core.PercentageField;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.SelectItem;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.grids.TransactionPayBillGrid;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

public class PayBillView extends AbstractTransactionBaseView<ClientPayBill> {

	AmountField amtText;
	AmountField endBalText;
	DateField date;
	DateField dueDate;
	com.vimukti.accounter.web.client.externalization.AccounterConstants accounterConstants = Accounter
			.constants();
	protected String vendorPaymentMethod;
	private TransactionPayBillGrid gridView;
	protected AmountField cashDiscountTextItem;
	protected AmountField creditTextItem;
	public AmountLabel unUsedCreditsText;
	PercentageField tdsLabel;
	protected SelectCombo vendorPaymentMethodCombo;
	protected List<PayBillTransactionList> paybillTransactionList;
	protected List<PayBillTransactionList> filterList;
	protected List<PayBillTransactionList> tempList;
	private ClientFinanceDate dueDateOnOrBefore;
	private DynamicForm payForm, filterForm;
	private int size;
	// public double transactionTotal = 0.0d;
	public double totalAmountDue = 0.0d;
	public double totalCashDiscount = 0.0d;
	public double totalOriginalAmount = 0.0d;
	public double totalOrginalAmt = 0.0D, totalDueAmt = 0.0D,
			totalPayment = 0.0D, cashDiscount = 0.0d;
	private ArrayList<DynamicForm> listforms;
	private VerticalPanel gridLayout;

	// Upate
	protected PayFromAccountsCombo payFromCombo;
	protected ClientAccount payFromAccount;
	protected ClientVendor vendor;
	protected List<ClientVendor> vendors;
	protected VendorCombo vendorCombo;

	public PayBillView() {
		super(ClientTransaction.TYPE_PAY_BILL);
	}

	/*
	 * This method invoked when all records in grid are de-selected.It resets
	 * the non-editable fields
	 */
	public void resetTotlas() {
		amtText.setAmount(0.0);
		endBalText
				.setAmount(payFromCombo.getSelectedValue() != null ? payFromCombo
						.getSelectedValue().getTotalBalance() : 0.0);
	}

	/*
	 * This method invoked eachtime when there is a change in records and it
	 * updates the noneditable amount fields
	 */
	public void adjustAmountAndEndingBalance() {
		List<ClientTransactionPayBill> selectedRecords = gridView
				.getSelectedRecords();
		double toBeSetAmount = 0.0;
		for (ClientTransactionPayBill rec : selectedRecords) {
			toBeSetAmount += rec.getPayment();
		}
		if (this.transaction != null) {
			amtText.setAmount(toBeSetAmount);

			if (payFromAccount != null) {
				double toBeSetEndingBalance = 0.0;
				if (payFromAccount.isIncrease())
					toBeSetEndingBalance = payFromAccount.getTotalBalance()

							+ DataUtils.getBalance(
									amtText.getAmount().toString())
									.doubleValue();
				else
					toBeSetEndingBalance = payFromAccount.getTotalBalance()

							- DataUtils.getBalance(
									amtText.getAmount().toString())
									.doubleValue();
				endBalText.setAmount(toBeSetEndingBalance);
			}
		}

	}

	protected void updateTransaction() {
		super.updateTransaction();
		// Setting Type of Enter Bill
		transaction.setType(ClientTransaction.TYPE_PAY_BILL);
		transaction.setPayBillType(ClientPayBill.TYPE_PAYBILL);

		// Setting Accounts Payable
		transaction.setAccountsPayable(getCompany()

		.getAccountsPayableAccount());

		// Setting Date
		transaction.setDate(date.getEnteredDate().getDate());

		// Setting Pay From
		transaction.setPayFrom(payFromAccount);

		// Setting payment method
		transaction.setPaymentMethod(paymentMethodCombo.getSelectedValue());

		// Setting Bill due or before
		if (dueDate.getEnteredDate() != null)
			transaction.setBillDueOnOrBefore(dueDate.getEnteredDate());
		// Setting Vendor
		if (getVendor() != null)
			transaction.setVendor(getVendor());
		// Setting Amount
		
		
		transaction.setTotal(amtText.getAmount());

		// Setting ending Balance
		transaction.setEndingBalance(endBalText.getAmount());

		transaction.setMemo(memoTextAreaItem.getValue().toString());

		// Setting Transactions
		List<ClientTransactionPayBill> selectedRecords = gridView
				.getSelectedRecords();

		List<ClientTransactionPayBill> transactionPayBill = new ArrayList<ClientTransactionPayBill>();
		for (ClientTransactionPayBill tpbRecord : selectedRecords) {
			PayBillTransactionList payBillTX = paybillTransactionList
					.get(gridView.indexOf(tpbRecord));

			if (payBillTX.getType() == ClientTransaction.TYPE_ENTER_BILL) {
				tpbRecord.setEnterBill(payBillTX.getTransactionId());
			} else if (payBillTX.getType() == ClientTransaction.TYPE_MAKE_DEPOSIT) {
				tpbRecord.setTransactionMakeDeposit(payBillTX
						.getTransactionId());
			} else if (payBillTX.getType() == ClientTransaction.TYPE_JOURNAL_ENTRY) {
				tpbRecord.setJournalEntry(payBillTX.getTransactionId());
			}
			tpbRecord.setAccountsPayable(getCompany()
					.getAccountsPayableAccount());
			tpbRecord.setPayBill(transaction);

			// ClientAccount cashAcc = getCompany().getAccountByName(
			// gridView.getAttribute(Accounter.constants().cashAccount(),
			// gridView.indexOf(tpbRecord)));
			// if (cashAcc != null)
			// tpbRecord.setDiscountAccount(cashAcc.getID());

			List<ClientTransactionCreditsAndPayments> trpList = gridView.creditsAndPaymentsDialiog != null ? gridView.creditsAndPaymentsDialiog
					.getTransactionCredits(tpbRecord)
					: new ArrayList<ClientTransactionCreditsAndPayments>();
			if (trpList != null)
				for (ClientTransactionCreditsAndPayments temp : trpList) {
					temp.setTransactionPayBill(tpbRecord);
				}
			tpbRecord.setTransactionCreditsAndPayments(trpList);
			if (tpbRecord.getTempCredits() != null)
				tpbRecord.getTempCredits().clear();
			transactionPayBill.add(tpbRecord);
		}
		transaction.setTransactionPayBill(transactionPayBill);

		transaction.setUnUsedCredits(this.unUsedCreditsText.getAmount());

	}

	private void initListGrid() {
		gridView = new TransactionPayBillGrid(!isInViewMode(), true);
		gridView.setCanEdit(!isInViewMode());
		gridView.setEditEventType(ListGrid.EDIT_EVENT_CLICK);
		gridView.setPaymentView(this);
		gridView.init();
		gridView.setHeight("200px");
		gridView.setDisabled(isInViewMode());
	}

	/*
	 * This method invoked each time when there is a change in cashdicount or
	 * credits or on record's paymentvalue change
	 */
	public void adjustPaymentValue(ClientTransactionPayBill rec) {
		Double amountDue = rec.getAmountDue();
		Double cashDiscount = rec.getCashDiscount();
		Double credit = rec.getAppliedCredits();
		Double payments = amountDue - (cashDiscount + credit);

		// (rec).setPayment(payments);
		(rec).setCashDiscount(cashDiscount);
		(rec).setAppliedCredits(credit);

		gridView.updateAmountDue(rec);

		gridView.updateData(rec);
		adjustAmountAndEndingBalance();
	}

	/*
	 * This method adds the records(bills & makedeposits(newlycreated rec's)) to
	 * the grid when vendor changed
	 */
	private void addGridRecords(List<PayBillTransactionList> billsList) {
		clearGrid();

		if (billsList != null) {
			totalOrginalAmt = 0.0d;
			totalDueAmt = 0.0d;
			totalPayment = 0.0d;
			cashDiscount = 0.0d;
			List<ClientTransactionPayBill> records = new ArrayList<ClientTransactionPayBill>();
			for (PayBillTransactionList curntRec : billsList) {
				ClientTransactionPayBill record = new ClientTransactionPayBill();

				record.setAmountDue(curntRec.getAmountDue());
				record.setDummyDue(curntRec.getAmountDue());

				record.setBillNumber(curntRec.getBillNumber());

				record.setCashDiscount(curntRec.getCashDiscount());

				record.setAppliedCredits(curntRec.getCredits());

				record.setDiscountDate(curntRec.getDiscountDate() != null ? curntRec
						.getDiscountDate().getDate() : 0);

				record.setDueDate(curntRec.getDueDate() != null ? curntRec
						.getDueDate().getDate() : 0);

				record.setOriginalAmount(curntRec.getOriginalAmount());

				// record.setPayment(curntRec.getPayment());
				ClientVendor vendor = getCompany().getVendorByName(
						curntRec.getVendorName());
				if (vendor != null)
					record.setVendor(vendor.getID());

				totalOrginalAmt += record.getOriginalAmount();
				totalDueAmt += record.getAmountDue();
				totalPayment += record.getPayment();
				cashDiscount += record.getCashDiscount();
				records.add(record);
			}
			// gridView.updateFooterValues(FinanceApplication.constants()
			// .subTotal(), 1);
			// if (!isEdit)
			// gridView.updateFooterValues(DataUtils
			// .getAmountAsString(totalOrginalAmt), 2);
			// if (!isEdit)
			// gridView.updateFooterValues(DataUtils
			// .getAmountAsString(totalDueAmt), 3);
			//
			// gridView.updateFooterValues(DataUtils
			// .getAmountAsString(totalPayment), isEdit ? 6 : 7);
			// if (!isEdit)
			// gridView.updateFooterValues(DataUtils
			// .getAmountAsString(cashDiscount), 5);
			gridView.setRecords(records);
			size = records.size();
		}
	}

	public void updateFooterValues() {
		/* if no records are there */
		totalOrginalAmt = 0.0D;
		totalDueAmt = 0.0D;
		cashDiscount = 0.0D;
		// if (!isEdit)
		// gridView.updateFooterValues(DataUtils
		// .getAmountAsString(totalOrginalAmt), 2);
		// if (!isEdit)
		// gridView.updateFooterValues(DataUtils
		// .getAmountAsString(totalDueAmt), 3);
		// gridView.updateFooterValues(DataUtils.getAmountAsString(totalPayment),
		// isEdit ? 6 : 7);
		// if (!isEdit)
		// gridView.updateFooterValues(DataUtils
		// .getAmountAsString(cashDiscount), 5);
	}

	private ClientVendor getVendorById(long id) {
		for (ClientVendor temp : vendors) {
			if (temp.getID() == id)
				return temp;
		}
		return null;
	}

	@Override
	protected void createControls() {

		listforms = new ArrayList<DynamicForm>();

		Label lab = new Label(Accounter.constants().payBill());
		lab.removeStyleName("gwt-Label");
		lab.addStyleName(Accounter.constants().labelTitle());
		// lab.setHeight("50px");
		date = new DateField(Accounter.constants().date());
		date.setToolTip(Accounter.messages().selectDateWhenTransactioCreated(
				this.getAction().getViewName()));
		date.setHelpInformation(true);
		// date.setUseTextField(true);
		date.setEnteredDate(new ClientFinanceDate());
		setTransactionDate(new ClientFinanceDate());
		date.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate changedDate) {
				if (changedDate != null) {
					try {
						ClientFinanceDate newDate = date.getValue();
						if (newDate != null)
							setTransactionDate(newDate);
					} catch (Exception e) {
						Accounter.showError(Accounter.constants()
								.invalidTransactionDate());
						setTransactionDate(new ClientFinanceDate());
						date.setEnteredDate(getTransactionDate());
					}

				}

			}

		});
		transactionNumber = createTransactionNumberItem();

		vendorCombo = createVendorComboItem(messages.vendorName(Global.get()
				.Vendor()));
		vendorCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {

					@Override
					public void selectedComboBoxItem(ClientVendor selectItem) {
						gridView.isAlreadyOpened = false;

						vendorSelected(selectItem);
					}
				});

		payFromCombo = createPayFromCombo(Accounter.constants().payFrom());
		payFromCombo.setPopupWidth("500px");
		paymentMethodCombo = createPaymentMethodSelectItem();
		// paymentMethodCombo.setWidth(100);
		paymentMethodCombo.setDefaultValue(Accounter.constants()
				.onlineBanking());

		dueDate = new DateField(Accounter.constants()
				.filterByBilldueonorbefore());
		dueDate.setHelpInformation(true);
		dueDate.setValue(new ClientFinanceDate());
		// dueDate.setUseTextField(true);
		// dueDate.setWidth(100);

		dueDate.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate date) {
				if (paybillTransactionList != null) {
					// if (event.getSource() != null) {
					// dueDateOnOrBefore = ((DateField) event.getSource())
					// .getValue();
					dueDateOnOrBefore = dueDate.getValue();
					clearGrid();
					filterGrid();
					// }
				}

			}
		});

		vendorPaymentMethodCombo = createPaymentMethodSelectItem();
		vendorPaymentMethodCombo.setRequired(false);
		vendorPaymentMethodCombo.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (vendorPaymentMethodCombo.getSelectedValue() != null)
					vendorPaymentMethod = ((SelectItem) event.getSource())
							.getValue().toString();
				if (paybillTransactionList != null) {
					clearGrid();
					filterGrid();
				}

			}
		});

		// filterForm = new DynamicForm();
		// filterForm.setIsGroup(true);
		// filterForm.setGroupTitle(Accounter.constants().Filter());
		// filterForm.setFields(dueDate);
		tdsLabel = new PercentageField(this, Accounter.constants().tds());
		tdsLabel.setDisabled(true);

		DynamicForm dateForm = new DynamicForm();
		dateForm.setNumCols(4);
		dateForm.setStyleName("datenumber-panel");
		dateForm.setFields(date, transactionNumber);

		HorizontalPanel datepanel = new HorizontalPanel();
		datepanel.setWidth("100%");
		datepanel.add(dateForm);
		datepanel.setCellHorizontalAlignment(dateForm, ALIGN_RIGHT);

		payForm = new DynamicForm();
		payForm.setWidth("80%");
		payForm.setIsGroup(true);
		payForm.setGroupTitle(Accounter.constants().payment());
		payForm.setFields(vendorCombo, payFromCombo, paymentMethodCombo,
				dueDate);
		amtText = new AmountField(Accounter.constants().amount(), this);
		amtText.setHelpInformation(true);
		amtText.setWidth(100);
		amtText.setValue("" + UIUtils.getCurrencySymbol() + "0.00");
		amtText.setDisabled(true);

		endBalText = new AmountField(Accounter.constants().endingBalance(),
				this);
		endBalText.setHelpInformation(true);
		endBalText.setWidth(100);
		endBalText.setValue("" + UIUtils.getCurrencySymbol() + "0.00");
		endBalText.setDisabled(true);

		DynamicForm balForm = new DynamicForm();
		balForm.setWidth("100%");
		balForm.setIsGroup(true);
		balForm.setGroupTitle(Accounter.constants().balances());
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_INDIA) {
			balForm.setFields(tdsLabel, amtText, endBalText);
		} else {
			balForm.setFields(amtText, endBalText);
		}

		Label lab1 = new Label(Accounter.constants().billsDue());

		menuButton = createAddNewButton();

		initListGrid();

		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setWidth("100%");

		DynamicForm memoForm = new DynamicForm();
		memoForm.setWidth("100%");
		memoForm.setFields(memoTextAreaItem);
		memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");

		unUsedCreditsText = new AmountLabel(Accounter.constants()
				.unusedCredits());
		unUsedCreditsText.setDisabled(true);

		DynamicForm textForm = new DynamicForm();
		textForm.setNumCols(2);
		textForm.setWidth("70%");
		textForm.setStyleName("unused-payments");
		textForm.setFields(unUsedCreditsText);

		HorizontalPanel bottompanel = new HorizontalPanel();
		bottompanel.setWidth("100%");
		bottompanel.add(memoForm);
		bottompanel.setCellHorizontalAlignment(memoForm,
				HasHorizontalAlignment.ALIGN_LEFT);
		bottompanel.add(textForm);
		bottompanel.setCellHorizontalAlignment(textForm,
				HasHorizontalAlignment.ALIGN_RIGHT);

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setWidth("100%");
		leftVLay.add(payForm);

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.setWidth("100%");
		topHLay.setSpacing(10);
		topHLay.add(leftVLay);
		topHLay.add(balForm);

		// HorizontalPanel hLay2 = new HorizontalPanel();
		// hLay2.setWidth("100%");
		// hLay2.setHorizontalAlignment(ALIGN_RIGHT);
		//
		// hLay2.add(textForm);
		HorizontalPanel bottomAmtsLayout = new HorizontalPanel();

		bottomAmtsLayout.setWidth("100%");
		gridLayout = new VerticalPanel();
		gridLayout.add(lab1);
		gridLayout.add(gridView);
		gridLayout.add(bottomAmtsLayout);
		gridLayout.add(bottompanel);
		gridLayout.setWidth("100%");

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(lab);
		mainVLay.add(datepanel);
		mainVLay.add(topHLay);
		mainVLay.add(gridLayout);

		if (UIUtils.isMSIEBrowser()) {
			payForm.getCellFormatter().setWidth(0, 0, "50%");
			payForm.setWidth("65%");
			balForm.getCellFormatter().setWidth(0, 1, "150px");
		}

		this.add(mainVLay);
		setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(payForm);
		listforms.add(balForm);
		listforms.add(textForm);

	}

	private void setUnUsedCredits(Double unusedCredits) {
		unUsedCreditsText.setAmount(unusedCredits);
	}

	public void calculateUnusedCredits() {

		Double totalCredits = 0D;
		for (ClientCreditsAndPayments credit : gridView.updatedCustomerCreditsAndPayments) {
			totalCredits += credit.getBalance();
		}

		this.unUsedCreditsText.setAmount(totalCredits);

	}

	protected void vendorSelected(final ClientVendor vendor) {

		ClientTAXItem taxItem = getCompany()
				.getTAXItem(vendor.getTaxItemCode());
		if (taxItem != null) {
			tdsLabel.setPercentage(taxItem.getTaxRate());
		} else {
			tdsLabel.setPercentage(0.0);
		}
		if (vendor == null) {
			paybillTransactionList = null;
			return;
		}

		this.setVendor(vendor);
		/*
		 * resetting the crdits dialog's refernce,so that a new object will
		 * created for opening credits dialog
		 */
		gridView.creditsAndPaymentsDialiog = null;
		gridView.creditsStack = null;
		gridView.initCreditsAndPayments(vendor);
		gridView.removeAllRecords();

		if (transaction.id == 0) {
			gridView.addLoadingImagePanel();
			getTransactionPayBills(vendor);

		}

	}

	private void getTransactionPayBills(ClientVendor vendor) {
		this.rpcUtilService
				.getTransactionPayBills(
						vendor.getID(),
						new AccounterAsyncCallback<ArrayList<PayBillTransactionList>>() {

							public void onException(AccounterException caught) {
								// SC
								// .say("Failed to Get List of Transaction Recieve Payments for this Vendor"
								// + vendor.getName());
								gridView.addEmptyMessage(Accounter.constants()
										.noRecordsToShow());
							}

							public void onResultSuccess(
									ArrayList<PayBillTransactionList> result) {

								paybillTransactionList = result;
								if (result.size() > 0) {
									addGridRecords(result);
									dueDateOnOrBefore = dueDate.getValue();
									clearGrid();
									filterGrid();
								} else {
									gridView.addEmptyMessage(Accounter
											.constants().noRecordsToShow());
									updateFooterValues();
								}
							}

						});

	}

	protected void accountSelected(ClientAccount account) {

		if (account == null)
			return;
		this.payFromAccount = account;

		adjustAmountAndEndingBalance();
	}

	// @Override
	// protected void initMemoAndReference() {
	// // NOTHING TO DO.
	// }

	@Override
	protected void initTransactionViewData() {
		if (transaction == null) {
			setData(new ClientPayBill());
		} else {

			paymentMethodCombo.setComboItem(transaction.getPaymentMethod());
			paymentMethodCombo.setDisabled(isInViewMode());

			this.transactionItems = transaction.getTransactionItems();

			payFromCombo.setComboItem(getCompany().getAccount(
					transaction.getPayFrom()));
			date.setValue(transaction.getDate());
			date.setDisabled(true);
			accountSelected(getCompany().getAccount(transaction.getPayFrom()));

			dueDate.setValue(new ClientFinanceDate(transaction
					.getBillDueOnOrBefore()));
			dueDate.setDisabled(true);

			transactionNumber.setValue(transaction.getNumber());
			transactionNumber.setDisabled(isInViewMode());

			this.setVendor(getCompany().getVendor(transaction.getVendor()));
			vendorSelected(getCompany().getVendor(transaction.getVendor()));

			amtText.setAmount(transaction.getTotal());
			endBalText.setAmount(transaction.getEndingBalance());
			initListGridData(this.transaction.getTransactionPayBill());
			initTransactionTotalNonEditableItem();
			memoTextAreaItem.setDisabled(true);
		}
		// super.initTransactionViewData();
		initVendors();
		initTransactionTotalNonEditableItem();
		try {
			this.initMemoAndReference();
		} catch (Exception e) {
			System.err.println(e);
		}

		initPayFromAccounts();
	}

	private void initListGridData(List<ClientTransactionPayBill> list) {
		int count = 0;
		double totalOrgAmt = 0.0;
		// double totalDueAmount = 0.0;
		// double cashDiscount = 0.0;
		// double credits = 0.0;
		// double payment = 0.0;
		for (ClientTransactionPayBill receivePayment : list) {
			totalOrgAmt += receivePayment.getOriginalAmount();
			this.gridView.addData(receivePayment);
			this.gridView.selectRow(count);
			count++;
		}
	}

	protected void initTransactionTotalNonEditableItem() {
		if (transaction == null)
			return;
		ClientPayBill paybill = ((ClientPayBill) transaction);
		Double unusedCredits = paybill.getUnUsedCredits();
		setUnUsedCredits(unusedCredits);
	}

	@Override
	public void updateNonEditableItems() {

	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();

		// Validations
		// 1. is valid transaction date?
		// 2. is in prevent posting before date?
		// 3. is pay form valid?
		// 4. filterForm valid?
		// 5. do select transaction?
		// 6. grid valid?

		if (!AccounterValidator.isValidTransactionDate(this.transactionDate)) {
			result.addError(transactionDate,
					accounterConstants.invalidateTransactionDate());
		}

		if (AccounterValidator.isInPreventPostingBeforeDate(transactionDate)) {
			result.addError(transactionDate,
					accounterConstants.invalidateDate());
		}
		ValidationResult payFormValidationResult = payForm.validate();
		if (payFormValidationResult.haveErrors()
				|| payFormValidationResult.haveWarnings()) {
			result.add(payFormValidationResult);
			return result;
		}

		if (filterForm != null) {
			result.add(filterForm.validate());
			return result;
		}
		if (!isInViewMode()) {
			if (AccounterValidator.isBlankTransaction(gridView)) {
				result.addError(
						vendorTransactionGrid,
						Accounter.messages().noBillsAreAvailableFirstAddABill(
								Global.get().Vendor()));
			}

			else {

				result.add(gridView.validateGrid());
			}
		}
		return result;
	}

	@Override
	public void saveAndUpdateView() {

		updateTransaction();
		saveOrUpdate(transaction);

	}

	public static PayBillView getInstance() {
		return new PayBillView();
	}

	protected void clearGrid() {
		gridView.removeAllRecords();
	}

	protected void filterGrid() {
		filterList = new ArrayList<PayBillTransactionList>();
		tempList = new ArrayList<PayBillTransactionList>();

		filterList.addAll(paybillTransactionList);

		if (dueDateOnOrBefore != null) {
			for (PayBillTransactionList cont : filterList) {
				if (cont.getDueDate().before(dueDateOnOrBefore)
						|| cont.getDueDate().equals(
								UIUtils.stringToDate(UIUtils
										.dateToString(dueDateOnOrBefore))))
					tempList.add(cont);

			}
			filterList.clear();
			filterList.addAll(tempList);
			tempList.clear();
		}

		if (getVendor() != null) {

			for (PayBillTransactionList cont : filterList) {
				if (getVendor().getName().toString()
						.equalsIgnoreCase(cont.getVendorName().toString())) {

					tempList.add(cont);
				}
			}
			filterList.clear();
			filterList.addAll(tempList);
			tempList.clear();
		}

		List<ClientTransactionPayBill> records = new ArrayList<ClientTransactionPayBill>();
		for (PayBillTransactionList cont : filterList) {
			ClientTransactionPayBill record = new ClientTransactionPayBill();

			record.setAmountDue(cont.getAmountDue());
			record.setDummyDue(cont.getAmountDue());

			record.setBillNumber(cont.getBillNumber());

			record.setCashDiscount(cont.getCashDiscount());

			record.setAppliedCredits(cont.getCredits());
			if (cont.getDiscountDate() != null)
				record.setDiscountDate(cont.getDiscountDate().getDate());
			if (cont.getDueDate() != null)
				record.setDueDate(cont.getDueDate().getDate());

			record.setOriginalAmount(cont.getOriginalAmount());

			record.setPayment(cont.getPayment());

			// record.setVendor(FinanceApplication.getCompany().getVendor(
			// cont.getVendorName()).getID());

			records.add(record);
		}

		gridView.setRecords(records);
		size = records.size();
		if (size == 0)
			gridView.addEmptyMessage(Accounter.constants().noRecordsToShow());
	}

	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.vendorCombo.setFocus();
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// Nothing To Do

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// Nothing TO DO

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}


	public void onEdit() {
		if (!transaction.isVoid()) {
			Accounter.showWarning(AccounterWarningType.PAYBILL_EDITING,
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
											.failedtovoidPayBill());

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
		} else if (transaction.isVoid() || transaction.isDeleted())
			Accounter
					.showError(Accounter.constants().failedtovoidTransaction());
	}

	private void enableFormItems() {

		setMode(EditMode.EDIT);
		date.setDisabled(isInViewMode());
		vendorCombo.setDisabled(isInViewMode());
		paymentMethodCombo.setDisabled(isInViewMode());
		dueDate.setDisabled(isInViewMode());
		gridView.setDisabled(isInViewMode());

		super.onEdit();
		gridView.removeFromParent();
		initListGrid();
		gridLayout.insert(gridView, 2);
		getTransactionPayBills(this.getVendor());
		memoTextAreaItem.setDisabled(isInViewMode());
		transaction = new ClientPayBill();
		data = transaction;
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// NOTHING TO DO.
	}

	protected Double getTransactionTotal() {
		return this.amtText.getAmount();
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().payBills();
	}

	// Update methods

	public ClientVendor getVendor() {
		return vendor;
	}

	public void setVendor(ClientVendor vendor) {
		this.vendor = vendor;
	}

	public VendorCombo createVendorComboItem(String title) {

		VendorCombo vendorCombo = new VendorCombo(title != null ? title
				: Global.get().Vendor());
		vendorCombo.setHelpInformation(true);
		vendorCombo.setRequired(true);
		vendorCombo.setDisabled(isInViewMode());
		// vendorCombo.setShowDisabled(false);
		vendorCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {

					@Override
					public void selectedComboBoxItem(ClientVendor selectItem) {
						vendorSelected(selectItem);

					}

				});

		// vendorCombo.setShowDisabled(false);
		return vendorCombo;

	}

	public PayFromAccountsCombo createPayFromCombo(String title) {

		PayFromAccountsCombo payFromCombo = new PayFromAccountsCombo(title);
		payFromCombo.setHelpInformation(true);
		payFromCombo.setRequired(true);
		payFromCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					public void selectedComboBoxItem(ClientAccount selectItem) {
						accountSelected(selectItem);
						// selectedAccount = (Account) selectItem;
						// adjustBalance();

					}

				});
		payFromCombo.setDisabled(isInViewMode());
		// payFromCombo.setShowDisabled(false);
		// formItems.add(payFromCombo);
		return payFromCombo;
	}

	protected void initVendors() {

		if (vendorCombo == null)
			return;
		List<ClientVendor> result = getCompany().getActiveVendors();
		vendors = result;

		vendorCombo.initCombo(result);
		vendorCombo.setDisabled(isInViewMode());

		if (getVendor() != null)
			vendorCombo.setComboItem(getVendor());
	}

	protected void initPayFromAccounts() {
		// getPayFromAccounts();
		// payFromCombo.initCombo(payFromAccounts);
		// payFromCombo.setAccountTypes(UIUtils
		// .getOptionsByType(AccountCombo.payFromCombo));
		payFromCombo.setAccounts();
		payFromCombo.setDisabled(isInViewMode());
		payFromAccount = payFromCombo.getSelectedValue();
		if (payFromAccount != null)
			payFromCombo.setComboItem(payFromAccount);
	}
}
