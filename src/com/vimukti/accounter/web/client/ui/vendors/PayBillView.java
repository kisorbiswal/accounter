package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayBill;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientTransactionPayBill;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.PayBillTransactionList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.SelectItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.grids.TransactionPayBillGrid;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

public class PayBillView extends AbstractVendorTransactionView<ClientPayBill> {

	AmountField amtText;
	AmountField endBalText;
	DateField date;
	DateField dueDate;

	protected String vendorPaymentMethod;
	private TransactionPayBillGrid gridView;
	protected AmountField cashDiscountTextItem;
	protected AmountField creditTextItem;
	public AmountLabel unUsedCreditsText;
	protected SelectCombo vendorPaymentMethodCombo;
	protected TextAreaItem memoTextAreaItem;
	protected List<PayBillTransactionList> paybillTransactionList;
	protected List<PayBillTransactionList> filterList;
	protected List<PayBillTransactionList> tempList;
	private ClientFinanceDate dueDateOnOrBefore;
	private DynamicForm payForm, filterForm;
	private int size;
	public double transactionTotal = 0.0d;
	public double totalAmountDue = 0.0d;
	public double totalCashDiscount = 0.0d;
	public double totalOriginalAmount = 0.0d;
	public double totalOrginalAmt = 0.0D, totalDueAmt = 0.0D,
			totalPayment = 0.0D, cashDiscount = 0.0d;
	private ArrayList<DynamicForm> listforms;
	private ClientPayBill billToBeEdited;
	private VerticalPanel gridLayout;

	public PayBillView() {
		super(ClientTransaction.TYPE_PAY_BILL, VENDOR_TRANSACTION_GRID);
		this.validationCount = 5;
	}

	/*
	 * This method invoked when all records in grid are de-selected.It resets
	 * the non-editable fields
	 */
	public void resetTotlas() {
		amtText.setAmount(0.0);
		endBalText
				.setAmount(payFromCombo.getSelectedValue() != null ? payFromCombo
						.getSelectedValue().getTotalBalance()
						: 0.0);
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
		if (this.transactionObject == null) {
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

	private ClientPayBill getPayBillObject() throws Exception {

		ClientPayBill payBill = null;
		if (transactionObject == null)
			payBill = new ClientPayBill();
		else {
			payBill = (ClientPayBill) transactionObject;
			return payBill;
		}

		// Setting Type of Enter Bill
		payBill.setType(ClientTransaction.TYPE_PAY_BILL);
		payBill.setPayBillType(ClientPayBill.TYPE_PAYBILL);

		// Setting Accounts Payable
		payBill.setAccountsPayable(getCompany()

		.getAccountsPayableAccount());

		// Setting Date
		payBill.setDate(date.getEnteredDate().getTime());

		// Setting Pay From
		payBill.setPayFrom(payFromAccount);

		// Setting payment method
		payBill.setPaymentMethod(paymentMethodCombo.getSelectedValue());

		// Setting Bill due or before
		if (dueDate.getEnteredDate() != null)
			payBill.setBillDueOnOrBefore(dueDate.getEnteredDate());
		// Setting Vendor
		if (vendor != null)
			payBill.setVendor(vendor);
		// Setting Amount
		payBill.setTotal(amtText.getAmount());

		// Setting ending Balance
		payBill.setEndingBalance(endBalText.getAmount());

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
			tpbRecord.setPayBill(payBill);

			ClientAccount cashAcc = getCompany()
					.getAccountByName(
							gridView.getAttribute(Accounter
									.getVendorsMessages().cashAccount(),
									gridView.indexOf(tpbRecord)));
			if (cashAcc != null)
				tpbRecord.setDiscountAccount(cashAcc.getID());

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
		if (transactionObject == null)
			payBill.setTransactionPayBill(transactionPayBill);

		payBill.setUnUsedCredits(this.unUsedCreditsText.getAmount());

		return payBill;

	}

	private void initListGrid() {
		gridView = new TransactionPayBillGrid(!isEdit, true);
		gridView.setCanEdit(!isEdit);
		gridView.setEditEventType(ListGrid.EDIT_EVENT_CLICK);
		gridView.setPaymentView(this);
		gridView.init();
		gridView.setHeight("200px");
		gridView.setDisabled(isEdit);
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

				record
						.setDiscountDate(curntRec.getDiscountDate() != null ? curntRec
								.getDiscountDate().getTime()
								: 0);

				record.setDueDate(curntRec.getDueDate() != null ? curntRec
						.getDueDate().getTime() : 0);

				record.setOriginalAmount(curntRec.getOriginalAmount());

				// record.setPayment(curntRec.getPayment());
				ClientVendor vendor = getCompany()
						.getVendorByName(curntRec.getVendorName());
				if (vendor != null)
					record.setVendor(vendor.getID());

				totalOrginalAmt += record.getOriginalAmount();
				totalDueAmt += record.getAmountDue();
				totalPayment += record.getPayment();
				cashDiscount += record.getCashDiscount();
				records.add(record);
			}
			// gridView.updateFooterValues(FinanceApplication.getVendorsMessages()
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

	@SuppressWarnings("unused")
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

		Label lab = new Label(Accounter.getVendorsMessages().PayBill());
		lab.removeStyleName("gwt-Label");
		lab.addStyleName(Accounter.getVendorsMessages().lableTitle());
		// lab.setHeight("50px");
		date = new DateField(vendorConstants.Date());
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
						Accounter.showError(Accounter
								.getVendorsMessages().invalidTransactionDate());
						setTransactionDate(new ClientFinanceDate());
						date.setEnteredDate(getTransactionDate());
					}

				}

			}

		});
		transactionNumber = createTransactionNumberItem();

		vendorCombo = createVendorComboItem(UIUtils.getVendorString(
				Accounter.getVendorsMessages().supplierName(),
				Accounter.getVendorsMessages().vendorName()));
		vendorCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {

					@Override
					public void selectedComboBoxItem(ClientVendor selectItem) {
						gridView.isAlreadyOpened = false;

						vendorSelected(selectItem);
					}
				});

		payFromCombo = createPayFromCombo(vendorConstants.Payfrom());
		payFromCombo.setPopupWidth("500px");
		paymentMethodCombo = createPaymentMethodSelectItem();
		// paymentMethodCombo.setWidth(100);
		paymentMethodCombo.setDefaultValue(Accounter
				.getVendorsMessages().onlineBanking());

		dueDate = new DateField(vendorConstants.filterByBilldueonorbefore());
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
		// filterForm.setGroupTitle(vendorConstants.Filter());
		// filterForm.setFields(dueDate);

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
		payForm.setGroupTitle(vendorConstants.Payment());
		payForm.setFields(vendorCombo, payFromCombo, paymentMethodCombo,
				dueDate);
		amtText = new AmountField(vendorConstants.Amount());
		amtText.setHelpInformation(true);
		amtText.setWidth(100);
		amtText.setValue("" + UIUtils.getCurrencySymbol() + "0.00");
		amtText.setDisabled(true);

		endBalText = new AmountField(vendorConstants.Endingbalance());
		endBalText.setHelpInformation(true);
		endBalText.setWidth(100);
		endBalText.setValue("" + UIUtils.getCurrencySymbol() + "0.00");
		endBalText.setDisabled(true);

		DynamicForm balForm = new DynamicForm();
		balForm.setWidth("100%");
		balForm.setIsGroup(true);
		balForm.setGroupTitle(vendorConstants.Balances());
		balForm.setFields(amtText, endBalText);

		Label lab1 = new Label(Accounter.getVendorsMessages()
				.billsDue());

		menuButton = createAddNewButton();

		initListGrid();

		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setWidth("100%");

		DynamicForm memoForm = new DynamicForm();
		memoForm.setWidth("100%");
		memoForm.setFields(memoTextAreaItem);
		memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");

		unUsedCreditsText = new AmountLabel(vendorConstants.unusedCredits());
		unUsedCreditsText.setDisabled(true);

		DynamicForm textForm = new DynamicForm();
		textForm.setNumCols(2);
		textForm.setWidth("70%");
		textForm.setStyleName("unused-payments");
		textForm.setFields(unUsedCreditsText);
		forms.add(textForm);

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

		canvas.add(mainVLay);
		setSize("100%", "100%");
		isEdit = transactionObject != null;

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

	@Override
	protected void vendorSelected(final ClientVendor vendor) {

		if (vendor == null) {
			paybillTransactionList = null;
			return;
		}

		this.vendor = vendor;
		/*
		 * resetting the crdits dialog's refernce,so that a new object will
		 * created for opening credits dialog
		 */
		gridView.creditsAndPaymentsDialiog = null;
		gridView.creditsStack = null;
		gridView.initCreditsAndPayments(vendor);
		gridView.removeAllRecords();

		if (transactionObject == null) {
			gridView.addLoadingImagePanel();
			getTransactionPayBills(vendor);

		}

	}

	private void getTransactionPayBills(ClientVendor vendor) {
		this.rpcUtilService.getTransactionPayBills(vendor.getID(),
				new AsyncCallback<List<PayBillTransactionList>>() {

					public void onFailure(Throwable caught) {
						// SC
						// .say("Failed to Get List of Transaction Recieve Payments for this Vendor"
						// + vendor.getName());
						gridView.addEmptyMessage("No records to show");
					}

					public void onSuccess(List<PayBillTransactionList> result) {

						paybillTransactionList = result;
						if (result.size() > 0) {
							addGridRecords(result);
							dueDateOnOrBefore = dueDate.getValue();
							clearGrid();
							filterGrid();
						} else {
							gridView.addEmptyMessage("No records to show");
							updateFooterValues();
						}
					}

				});

	}

	@Override
	protected void accountSelected(ClientAccount account) {

		super.accountSelected(account);

		adjustAmountAndEndingBalance();
	}

	@Override
	protected void initMemoAndReference() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initTransactionViewData(ClientTransaction transactionObject) {
		billToBeEdited = (ClientPayBill) transactionObject;

		paymentMethodCombo.setComboItem(billToBeEdited.getPaymentMethod());

		this.transactionItems = billToBeEdited.getTransactionItems();

		payFromCombo.setComboItem(getCompany().getAccount(
				billToBeEdited.getPayFrom()));
		date.setValue(billToBeEdited.getDate());
		date.setDisabled(true);
		accountSelected(getCompany().getAccount(
				billToBeEdited.getPayFrom()));

		dueDate.setValue(new ClientFinanceDate(billToBeEdited
				.getBillDueOnOrBefore()));
		dueDate.setDisabled(true);

		this.vendor = getCompany().getVendor(
				billToBeEdited.getVendor());
		vendorSelected(getCompany().getVendor(
				billToBeEdited.getVendor()));

		amtText.setAmount(billToBeEdited.getTotal());
		endBalText.setAmount(billToBeEdited.getEndingBalance());
		initListGridData(this.transactionObject.getTransactionPayBill());
		initTransactionTotalNonEditableItem();
		memoTextAreaItem.setDisabled(true);
		initTransactionViewData();
	}

	private void initListGridData(List<ClientTransactionPayBill> list) {
		// int count = 0;
		double totalOrgAmt = 0.0;
		// double totalDueAmount = 0.0;
		// double cashDiscount = 0.0;
		// double credits = 0.0;
		// double payment = 0.0;
		for (ClientTransactionPayBill receivePayment : list) {
			totalOrgAmt += receivePayment.getOriginalAmount();
			this.gridView.addData(receivePayment);
			// this.gridView.selectRow(count);
		}
	}

	@Override
	protected void initTransactionTotalNonEditableItem() {
		if (transactionObject == null)
			return;
		ClientPayBill paybill = ((ClientPayBill) transactionObject);
		Double unusedCredits = paybill.getUnUsedCredits();
		setUnUsedCredits(unusedCredits);
	}

	@Override
	public void updateNonEditableItems() {

	}

	@Override
	public boolean validate() throws InvalidEntryException,
			InvalidTransactionEntryException {
		switch (this.validationCount) {
		case 5:
			return AccounterValidator
					.validateTransactionDate(this.transactionDate);
		case 4:
			if (payForm != null)
				return AccounterValidator.validateForm(payForm, false);

		case 3:
			if (filterForm != null)
				return AccounterValidator.validateForm(filterForm, false);
			return true;
		case 2:
			if (!isEdit)
				return AccounterValidator.validateReceivePaymentGrid(gridView);

		case 1:
			// FIXME--need to implement this feature
			// return AccounterValidator.validate_Total_Exceeds_BankBalance(
			// endBalText.getAmount(), amtText.getAmount(), payFromAccount
			// .isIncrease(), this);
			// case 1:
			// if (transactionTotal <= 0)
			// return AccounterValidator.validatePayBill();
		default:
			return true;
		}
	}

	@Override
	protected void initTransactionViewData() {

		super.initTransactionViewData();
		initPayFromAccounts();

	}

	@Override
	public void saveAndUpdateView() throws Exception {

		ClientPayBill payBill = getPayBillObject();
		if (transactionObject == null) {
			createObject(payBill);
		} else {
			alterObject(payBill);
		}

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

		if (vendor != null) {

			for (PayBillTransactionList cont : filterList) {
				if (vendor.getName().toString().equalsIgnoreCase(
						cont.getVendorName().toString())) {

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
				record.setDiscountDate(cont.getDiscountDate().getTime());
			if (cont.getDueDate() != null)
				record.setDueDate(cont.getDueDate().getTime());

			record.setOriginalAmount(cont.getOriginalAmount());

			record.setPayment(cont.getPayment());

			// record.setVendor(FinanceApplication.getCompany().getVendor(
			// cont.getVendorName()).getID());

			records.add(record);
		}

		gridView.setRecords(records);
		size = records.size();
		if (size == 0)
			gridView.addEmptyMessage("No records to show");
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
	public void deleteFailed(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(Boolean result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		switch (command) {
		case AccounterCommand.CREATION_SUCCESS:
			if (core.getObjectType() == AccounterCoreType.VENDOR)
				vendorCombo.addItemThenfireEvent((ClientVendor) core);
			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				payFromCombo.addItemThenfireEvent((ClientAccount) core);

			break;
		case AccounterCommand.UPDATION_SUCCESS:
			break;
		case AccounterCommand.DELETION_SUCCESS:
			if (core.getObjectType() == AccounterCoreType.VENDOR)
				vendorCombo.removeComboItem((ClientVendor) core);
			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				payFromCombo.removeComboItem((ClientAccount) core);
			break;
		}
	}

	public void onEdit() {
		if (!transactionObject.isVoid()) {
			Accounter.showWarning(AccounterWarningType.PAYBILL_EDITING,
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
									if (caught instanceof InvocationException) {
										Accounter
												.showMessage("Your session expired, Please login again to continue");
									} else {
										Accounter
												.showError("Failed to void Pay Bill");
									}

								}

								@Override
								public void onSuccess(Boolean result) {
									if (result) {
										enableFormItems();
									} else

										onFailure(new Exception());
								}

							};
							if (billToBeEdited != null) {
								AccounterCoreType type = UIUtils
										.getAccounterCoreType(billToBeEdited
												.getType());
								rpcDoSerivce.voidTransaction(type,
										billToBeEdited.id, callback);
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
		} else if (transactionObject.isVoid() || transactionObject.isDeleted())
			Accounter
					.showError("This Transaction is already voided or Deleted, can't Modify");
	}

	private void enableFormItems() {

		isEdit = false;
		date.setDisabled(isEdit);
		vendorCombo.setDisabled(isEdit);
		paymentMethodCombo.setDisabled(isEdit);
		dueDate.setDisabled(isEdit);
		gridView.setDisabled(isEdit);

		super.onEdit();
		gridView.removeFromParent();
		initListGrid();
		gridLayout.insert(gridView, 2);
		getTransactionPayBills(this.vendor);
		memoTextAreaItem.setDisabled(isEdit);
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

	@Override
	protected Double getTransactionTotal() {
		return this.amtText.getAmount();
	}

	@Override
	protected String getViewTitle() {
		return Accounter.getVendorsMessages().payBills();
	}
}
