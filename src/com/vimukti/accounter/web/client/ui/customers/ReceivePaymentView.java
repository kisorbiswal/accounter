package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientReceivePayment;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientTransactionReceivePayment;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentTransactionList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.CustomerCombo;
import com.vimukti.accounter.web.client.ui.combo.DepositInAccountCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.grids.TransactionReceivePaymentGrid;

/**
 * 
 * @author Fernandez
 * @implemented By Fernandez
 */
public class ReceivePaymentView extends
		AbstractTransactionBaseView<ClientReceivePayment> {

	public AmountField customerNonEditablebalText;
	public AmountLabel unUsedCreditsText;
	private AmountLabel unUsedPaymentsText;

	public AmountField amtText;
	private DynamicForm payForm;

	private VerticalPanel mainVLay;

	private VerticalPanel gridLayout;

	private Label lab;

	private HorizontalPanel topHLay;

	public TransactionReceivePaymentGrid gridView;

	AccounterConstants accounterConstants = Accounter.constants();

	protected List<ReceivePaymentTransactionList> receivePaymentTransactionList;

	protected ClientTAXCode selectedTaxCode = null;

	public Double amountRecieved = 0.0D, totalInoiceAmt = 0.0D,
			totalDueAmt = 0.0D, transactionTotal = 0.0D;

	public double unUsedPayments;

	private Double customerBalance;

	protected Boolean vatInclude = false;

	private ArrayList<DynamicForm> listforms;

	private CustomerCombo customerCombo;
	private ClientCustomer customer;
	private ClientAccount depositInAccount;
	private DepositInAccountCombo depositInCombo;

	public ReceivePaymentView() {
		super(ClientTransaction.TYPE_RECEIVE_PAYMENT,
				RECIEVEPAYMENT_TRANSACTION_GRID);

	}

	protected void customerSelected(final ClientCustomer selectedCustomer) {
		if (selectedCustomer == null) {
			receivePaymentTransactionList = null;
			return;
		}
		if (getCustomer() != null && customerCombo != null) {
			customerCombo.setComboItem(getCompany().getCustomer(
					selectedCustomer.getID()));
		}
		this.setCustomer(selectedCustomer);
		this.gridView.setCustomer(getCustomer());

		/*
		 * resetting the crdits dialog's refernce,so that a new object will
		 * created for opening credits dialog
		 */
		gridView.creditsAndPaymentsDialiog = null;
		gridView.creditsStack = null;
		gridView.initCreditsAndPayments(getCustomer());

		if (!isInViewMode()) {
			gridView.removeAllRecords();
			gridView.addLoadingImagePanel();
			getTransactionReceivePayments(selectedCustomer);
		}

		// }

		// if(selectedCustomer.getPaymentMethod())
		paymentMethodCombo.setComboItem(selectedCustomer.getPaymentMethod());

		this.paymentMethod = selectedCustomer.getPaymentMethod();

		setCustomerBalance(selectedCustomer.getBalance());

		this.customerBalance = selectedCustomer.getBalance();

		adjustAmountAndEndingBalance();
	}

	private void getTransactionReceivePayments(
			final ClientCustomer selectedCustomer) {

		long paymentDate = transactionDateItem.getDate().getDate();

		this.rpcUtilService
				.getTransactionReceivePayments(
						selectedCustomer.getID(),
						paymentDate,
						new AccounterAsyncCallback<ArrayList<ReceivePaymentTransactionList>>() {

							public void onException(AccounterException caught) {
								Accounter.showError(Accounter.messages()
										.failedToGetRecievePayments(
												Global.get().customer())
										+ selectedCustomer.getName());
								gridView.addEmptyMessage(Accounter.constants()
										.noRecordsToShow());
							}

							public void onResultSuccess(
									ArrayList<ReceivePaymentTransactionList> result) {

								receivePaymentTransactionList = result;

								if (result.size() > 0) {
									gridView.removeAllRecords();
									gridView
											.initCreditsAndPayments(selectedCustomer);
									addTransactionRecievePayments(result);
								} else {
									gridView.addEmptyMessage(Accounter
											.constants().noRecordsToShow());
									totalInoiceAmt = 0.00d;
									totalDueAmt = 0.00d;
									transactionTotal = 0.00d;
									// updateFooterValues();
								}
							}

						});
	}

	public void calculateUnusedCredits() {

		Double totalCredits = 0D;
		for (ClientCreditsAndPayments credit : gridView.updatedCustomerCreditsAndPayments) {

			totalCredits += credit.getBalance();
		}

		this.unUsedCreditsText.setAmount(totalCredits);

	}

	private void setCustomerBalance(Double balance) {

		customerNonEditablebalText.setAmount(balance);

	}

	public List<ClientTransactionReceivePayment> getRecords() {
		return null;// gridView.getRecords();
	}

	protected void addTransactionRecievePayments(
			List<ReceivePaymentTransactionList> result) {

		if (result == null)
			return;

		totalInoiceAmt = 0.00d;
		totalDueAmt = 0.00d;

		List<ClientTransactionReceivePayment> records = new ArrayList<ClientTransactionReceivePayment>();

		for (ReceivePaymentTransactionList receivePaymentTransaction : result) {

			ClientTransactionReceivePayment record = new ClientTransactionReceivePayment();

			record
					.setDueDate(receivePaymentTransaction.getDueDate() != null ? receivePaymentTransaction
							.getDueDate().getDate()
							: 0);
			record.setNumber(receivePaymentTransaction.getNumber());

			record.setInvoiceAmount(receivePaymentTransaction
					.getInvoiceAmount());

			record.setInvoice(receivePaymentTransaction.getTransactionId());
			record.setAmountDue(receivePaymentTransaction.getAmountDue());
			record.setDummyDue(receivePaymentTransaction.getAmountDue());
			record
					.setDiscountDate(receivePaymentTransaction
							.getDiscountDate() != null ? receivePaymentTransaction
							.getDiscountDate().getDate()
							: 0);

			record.setCashDiscount(receivePaymentTransaction.getCashDiscount());

			record.setWriteOff(receivePaymentTransaction.getWriteOff());

			record.setAppliedCredits(receivePaymentTransaction
					.getAppliedCredits());

			record.setPayment(0);

			totalInoiceAmt += receivePaymentTransaction.getInvoiceAmount();
			totalDueAmt += receivePaymentTransaction.getAmountDue();

			if (receivePaymentTransaction.getType() == ClientTransaction.TYPE_INVOICE) {
				record.isInvoice = true;
				record.setInvoice(receivePaymentTransaction.getTransactionId());
			} else if (receivePaymentTransaction.getType() == ClientTransaction.TYPE_CUSTOMER_REFUNDS) {
				record.isInvoice = false;
				record.setCustomerRefund(receivePaymentTransaction
						.getTransactionId());
			} else if (receivePaymentTransaction.getType() == ClientTransaction.TYPE_JOURNAL_ENTRY) {
				record.isInvoice = false;
				record.setJournalEntry(receivePaymentTransaction
						.getTransactionId());
			}
			records.add(record);
			gridView.addData(record);
		}
		// gridView.setRecords(records);
		recalculateGridAmounts();
		// updateFooterValues();

	}

	// public void updateFooterValues() {
	// gridView.updateFooterValues(
	// DataUtils.getAmountAsString(totalInoiceAmt), 2);
	// if (!isEdit)
	// gridView.updateFooterValues(DataUtils
	// .getAmountAsString(totalDueAmt), 3);
	// gridView.updateFooterValues(DataUtils
	// .getAmountAsString(transactionTotal), 8);
	// }

	public Double calculatePaymentForRecord(
			ClientTransactionReceivePayment record) {

		ClientTransactionReceivePayment trpRecord = (ClientTransactionReceivePayment) record;
		// FIXME :: required no changes but it has fix me so confirm it
		Double amountDue = trpRecord.getAmountDue();

		Double cashDiscount = trpRecord.getCashDiscount();

		Double getWriteOffAmt = trpRecord.getWriteOff();

		Double appliedCredits = trpRecord.getAppliedCredits();

		return amountDue - (cashDiscount + getWriteOffAmt + appliedCredits);

	}

	public void setAmountRecieved(Double amountRecieved) {
		this.amountRecieved = amountRecieved;
		this.amtText.setAmount(amountRecieved);
	}

	public Double getAmountRecieved() {
		return amountRecieved;
	}

	private void initListGrid() {
		gridView = new TransactionReceivePaymentGrid(!isInViewMode(), true);
		gridView.setPaymentView(this);
		gridView.setCustomer(this.getCustomer());
		gridView.setCanEdit(!isInViewMode());
		gridView.isEnable = false;
		gridView.init();
		gridView.setDisabled(isInViewMode());
		gridView.setEditEventType(ListGrid.EDIT_EVENT_CLICK);
		gridView.setHeight("200px");
	}

	protected ReceivePaymentTransactionList getRecievePayment(String attribute) {

		Long id = Long.parseLong(attribute);

		for (ReceivePaymentTransactionList recv : receivePaymentTransactionList) {
			if (recv.getTransactionId() == (id)) {
				return recv;
			}
		}

		return null;
	}

	private List<ClientTransactionReceivePayment> getTransactionRecievePayments(
			ClientReceivePayment receivePayment) {
		List<ClientTransactionReceivePayment> paymentsList = new ArrayList<ClientTransactionReceivePayment>();
		for (ClientTransactionReceivePayment payment : gridView
				.getSelectedRecords()) {
			// ClientAccount cashAcc =
			// FinanceApplication.getCompany().getAccount(
			// gridView.getAttribute(FinanceApplication
			// .constants().cashAccount(), gridView
			// .indexOf(payment)));
			// if (cashAcc != null)
			// payment.setDiscountAccount(cashAcc.getID());
			//
			// ClientAccount wrrittoff = FinanceApplication.getCompany()
			// .getAccount(
			// gridView.getAttribute(FinanceApplication
			// .constants().writeOff(),
			// gridView.indexOf(payment)));
			// if (wrrittoff != null)
			// payment.setWriteOffAccount(wrrittoff.getID());

			payment.setTransaction(receivePayment.getID());

			// List<ClientTransactionCreditsAndPayments> trpList =
			// (List<ClientTransactionCreditsAndPayments>) gridView
			// .getAttributeAsObject(FinanceApplication
			// .constants().creditsAndPayments(),
			// gridView.indexOf(payment));
			if (gridView.creditsAndPaymentsDialiog != null) {
				List<ClientTransactionCreditsAndPayments> tranCreditsandPayments = gridView.creditsAndPaymentsDialiog != null ? gridView.creditsAndPaymentsDialiog
						.getTransactionCredits(payment)
						: new ArrayList<ClientTransactionCreditsAndPayments>();
				if (tranCreditsandPayments != null)
					for (ClientTransactionCreditsAndPayments transactionCreditsAndPayments : tranCreditsandPayments) {
						transactionCreditsAndPayments
								.setTransactionReceivePayment(payment);
					}

				payment
						.setTransactionCreditsAndPayments(tranCreditsandPayments);
			}
			paymentsList.add(payment);
			payment.getTempCredits().clear();
		}

		return paymentsList;
	}

	protected void adjustAmountAndEndingBalance() {

	}

	@Override
	protected void createControls() {
		if (transaction == null
				|| transaction.getStatus() == ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)
			lab = new Label(Utility.getTransactionName(transactionType));
		else {
			// lab = new Label(Utility.getTransactionName(transactionType) + "("
			// + getTransactionStatus() + ")");
			lab = new Label(Utility.getTransactionName(transactionType));
		}
		lab.setStyleName(Accounter.constants().labelTitle());
		// lab.setHeight("35px");
		transactionDateItem = createTransactionDateItem();
		transactionNumber = createTransactionNumberItem();

		listforms = new ArrayList<DynamicForm>();

		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(4);
		dateNoForm.setStyleName("datenumber-panel");
		dateNoForm.setFields(transactionDateItem, transactionNumber);

		HorizontalPanel datepanel = new HorizontalPanel();
		datepanel.setWidth("100%");
		datepanel.add(dateNoForm);
		datepanel.setCellHorizontalAlignment(dateNoForm,
				HasHorizontalAlignment.ALIGN_RIGHT);
		datepanel.getElement().getStyle().setPaddingRight(15, Unit.PX);

		final HorizontalPanel labeldateNoLayout = new HorizontalPanel();
		labeldateNoLayout.setWidth("100%");
		// labeldateNoLayout.add(lab);
		labeldateNoLayout.add(datepanel);

		customerCombo = createCustomerComboItem(Accounter.constants()
				.receivedFrom());

		amtText = new AmountField(Accounter.constants().amountReceived(), this);
		amtText.setHelpInformation(true);
		amtText.setWidth(100);
		amtText.setDisabled(isInViewMode());

		amtText.addBlurHandler(new BlurHandler() {

			private Object value;

			@Override
			public void onBlur(BlurEvent event) {
				value = ((TextBox) event.getSource()).getValue();

				if (value == null)
					return;
				Double amount = 0.00D;
				try {
					amount = DataUtils
							.getAmountStringAsDouble(value.toString());
					setAmount(DataUtils.isValidAmount(value.toString()) ? amount
							: 0.00D);
					paymentAmountChanged(amount);

					if (DecimalUtil.isLessThan(amount, 0)) {
						Accounter.showError(Accounter.constants()
								.noNegativeAmountsReceived());
						setAmount(0.00D);

					}

					// if (!(gridView.getRecords().size() == 0)
					// && amount > 0D
					// && amount.doubleValue() != prevAmountRecieved
					// .doubleValue()) {
					// AccounterValidator
					// .distributePaymentToOutstandingInvoices(
					// ReceivePaymentView.this, amount);
					// }
				} catch (Exception e) {
					// if (value.toString().length() != 0)
					// Accounter
					// .showError(AccounterErrorType.INCORRECTINFORMATION);
					if (e instanceof InvalidEntryException) {
						Accounter.showError(e.getMessage());
					}
					setAmount(0.00D);
				}

			}

			public void setAmount(Double amount) {
				amtText.setValue(DataUtils.getAmountAsString(amount));
			}
		});

		memoTextAreaItem = createMemoTextAreaItem();
		// refText = createRefereceText();
		// refText.setWidth(100);
		paymentMethodCombo = createPaymentMethodSelectItem();
		// paymentMethodCombo.setWidth(100);

		payForm = new DynamicForm();
		payForm.setWidth("90%");
		payForm.setIsGroup(true);
		payForm.setGroupTitle(Accounter.constants().payment());

		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			payForm.setFields(customerCombo, amtText, paymentMethodCombo);
		} else
			payForm.setFields(customerCombo, amtText, paymentMethodCombo);
		payForm.setStyleName("align-form");
		payForm.getCellFormatter().setWidth(0, 0, "180px");

		customerNonEditablebalText = new AmountField(Accounter.messages()
				.customerBalance(Global.get().Customer()), this);
		customerNonEditablebalText.setHelpInformation(true);
		customerNonEditablebalText.setWidth(100);
		customerNonEditablebalText.setDisabled(true);

		depositInCombo = createDepositInComboItem();
		depositInCombo.setPopupWidth("500px");

		DynamicForm depoForm = new DynamicForm();
		// depoForm.setWidth("80%");
		depoForm.setIsGroup(true);
		depoForm.setGroupTitle(Accounter.constants().deposit());
		depoForm.setFields(customerNonEditablebalText, depositInCombo);
		depoForm.getCellFormatter().setWidth(0, 0, "203px");

		Label lab1 = new Label(Accounter.constants().dueForPayment());

		initListGrid();

		unUsedCreditsText = new AmountLabel(Accounter.constants()
				.unusedCredits());
		unUsedCreditsText.setHelpInformation(true);
		unUsedCreditsText.setDisabled(true);

		unUsedPaymentsText = new AmountLabel(Accounter.constants()
				.unusedPayments());
		unUsedPaymentsText.setHelpInformation(true);
		unUsedPaymentsText.setDisabled(true);

		DynamicForm textForm = new DynamicForm();
		textForm.setWidth("70%");
		textForm.setFields(unUsedCreditsText, unUsedPaymentsText);
		textForm.addStyleName("unused-payments");

		DynamicForm memoForm = new DynamicForm();
		memoForm.setWidth("100%");
		memoForm.setFields(memoTextAreaItem);
		memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");

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

		VerticalPanel rightVLay = new VerticalPanel();
		rightVLay.setWidth("100%");
		rightVLay.add(depoForm);
		// rightVLay.setCellHorizontalAlignment(depoForm, ALIGN_RIGHT);

		topHLay = new HorizontalPanel();
		topHLay.setWidth("100%");
		topHLay.setSpacing(10);
		topHLay.add(leftVLay);
		topHLay.add(rightVLay);
		topHLay.setCellWidth(leftVLay, "50%");
		topHLay.setCellWidth(rightVLay, "39%");

		VerticalPanel gridAndBalances = new VerticalPanel();

		// HorizontalPanel hLay2 = new HorizontalPanel();
		// hLay2.setWidth("100%");
		// hLay2.setHorizontalAlignment(ALIGN_RIGHT);
		//
		// hLay2.add(textForm);

		HorizontalPanel bottomAmtsLayout = new HorizontalPanel();

		bottomAmtsLayout.setWidth("100%");

		gridLayout = new VerticalPanel();
		gridLayout.setWidth("99%");
		gridLayout.setHeight("50%");
		gridLayout.add(lab1);
		gridLayout.add(gridView);
		gridLayout.add(bottomAmtsLayout);
		gridLayout.add(bottompanel);

		mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(lab);
		mainVLay.add(labeldateNoLayout);
		mainVLay.add(topHLay);
		mainVLay.add(gridLayout);

		if (UIUtils.isMSIEBrowser())
			payForm.getCellFormatter().setWidth(0, 1, "200px");

		this.add(mainVLay);
		setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(payForm);
		listforms.add(depoForm);
		listforms.add(textForm);

	}

	protected void paymentAmountChanged(Double amount) {

		if (amount == null)
			amount = 0.00D;

		setAmountRecieved(amount);

		// calculateVatFraction(amount);

		recalculateGridAmounts();

	}

	/**
	 * This method invoked when the value of 'amtText' field changed
	 * 
	 * @param amount
	 *            The amount about to pay
	 */
	public void calculateVatFraction(Double amount) {
		if (selectedTaxCode != null) {

			ClientTAXCode code = (ClientTAXCode) selectedTaxCode;

			double amt = 0.0d;
			try {
				amt = DataUtils.getAmountStringAsDouble(amtText.getValue()
						.toString());
			} catch (Exception e) {

			}
			// FIXME--need to check for UK version
			// fraction = amt
			// - 100
			// * (amt / (100 + code
			// .getNearestTaxRate(((Date) transactionDateItem
			// .getValue()).getTime())));
		} // else
		// fraction = 0.0;
		// if (vatFraction != null)
		// vatFraction.setAmount(fraction);
	}

	@Override
	public void saveAndUpdateView() {

		updateTransaction();
		saveOrUpdate(getData());

	}

	@Override
	protected void updateTransaction() {
		super.updateTransaction();

		transaction.setDate(transactionDateItem.getValue().getDate());
		if (paymentMethod != null)
			transaction.setPaymentMethod(paymentMethod);
		if (depositInAccount != null)
			transaction.setDepositIn(depositInAccount.getID());
		if (getCustomer() != null)
			transaction.setCustomer(getCustomer().getID());
		if (transactionNumber != null)
			transaction.setNumber(transactionNumber.getValue().toString());
		// if (refText != null)
		// receivePayment.setReference(refText.getValue().toString());
		if (memoTextAreaItem != null)
			transaction.setMemo(memoTextAreaItem.getValue().toString());

		transaction.setCustomerBalance(getCustomerBalance());

		transaction.setAmount(this.amountRecieved);

		transaction
				.setTransactionReceivePayment(getTransactionRecievePayments(transaction));

		transaction.setUnUsedPayments(this.unUsedPayments);
		transaction.setTotal(this.transactionTotal);

		transaction.setUnUsedCredits(this.unUsedCreditsText.getAmount());

	}

	public void setUnusedPayments(Double unusedAmounts) {
		if (unusedAmounts == null)
			unusedAmounts = 0.0D;
		this.unUsedPayments = unusedAmounts;
		this.unUsedPaymentsText.setAmount(unusedAmounts);

	}

	private void setUnUsedCredits(Double unusedCredits) {

		unUsedCreditsText.setAmount(unusedCredits);

	}

	protected void initTransactionTotalNonEditableItem() {
		if (transaction == null)
			return;

		ClientReceivePayment recievePayment = ((ClientReceivePayment) transaction);

		setCustomerBalance(recievePayment.getCustomerBalance());

		Double unusedCredits = recievePayment.getUnUsedCredits();

		Double unusedPayments = recievePayment.getUnUsedPayments();

		setUnUsedCredits(unusedCredits);

		setUnusedPayments(unusedPayments);

	}

	@Override
	protected void initTransactionViewData() {
		if (transaction == null) {
			setData(new ClientReceivePayment());
			initDepositInAccounts();
		} else {

			this.setCustomer(getCompany()
					.getCustomer(transaction.getCustomer()));
			customerSelected(getCompany()
					.getCustomer(transaction.getCustomer()));

			depositInAccountSelected(getCompany().getAccount(
					transaction.getDepositIn()));

			this.transactionItems = transaction.getTransactionItems();
			memoTextAreaItem.setDisabled(true);
			if (transaction.getMemo() != null)
				memoTextAreaItem.setValue(transaction.getMemo());
			if (transaction.getPaymentMethod() != null)
				paymentMethodCombo.setComboItem(transaction.getPaymentMethod());
			// if (paymentToBeEdited.getReference() != null)
			// refText.setValue(paymentToBeEdited.getReference());
			setAmountRecieved(transaction.getAmount());

			initTransactionTotalNonEditableItem();
			List<ClientTransactionReceivePayment> tranReceivePaymnetsList = transaction
					.getTransactionReceivePayment();
			initListGridData(tranReceivePaymnetsList);
			gridView.setTranReceivePayments(tranReceivePaymnetsList);
		}
		initTransactionNumber();
		initCustomers();
	}

	private void initListGridData(List<ClientTransactionReceivePayment> list) {

		for (ClientTransactionReceivePayment receivePayment : list) {
			totalInoiceAmt += receivePayment.getInvoiceAmount();
			this.gridView.addData(receivePayment);
			// this.gridView.selectRow(count);
		}
		this.transactionTotal = getGridTotal();
		// updateFooterValues();

	}

	public Double getGridTotal() {
		Double total = 0.0D;
		for (ClientTransactionReceivePayment record : gridView.getRecords()) {
			total += record.getPayment();
		}
		return total;

	}

	/*
	 * public class TransactionRecievePaymentRecord extends
	 * ClientTransactionReceivePayment {
	 * 
	 * public boolean isInvoice;
	 * 
	 * public boolean isSelected;
	 * 
	 * static final String ATTR_CASH_DISCOUNT = "Cash Discount";
	 * 
	 * public static final String ATTR_PAYMENTS = "payments"; public static
	 * final String ATTR_DISCOUNT_DATE = "Discount Date"; public static final
	 * String ATTR_AMOUNT_DUE = "Amount Due"; public static final String
	 * ATTR_WRITEOFF = "write off"; public static final String ATTR_DUEDATE =
	 * "duedate"; public static final String ATTR_INVOICE = "invoice"; public
	 * static final String ATTR_INVOICE_AMOUNT = "Invoice Amount"; public static
	 * final String ATTR_APPLIED_CREDITS = "applied Credits"; public static
	 * final String ATTR_ID = "id";
	 * 
	 * public TransactionRecievePaymentRecord() { super(); }
	 * 
	 * public void setCalculatedPaymentAmount() {
	 * 
	 * try { Double amount = amountDue - (cashDiscount + writeOffAmount +
	 * applyCreditsAmount);
	 * 
	 * setPaymentAmount(amount); } catch (Exception e) { e.printStackTrace(); //
	 * SC.logWarn(e.getMessage()); paymentAmount = 0.0D; try {
	 * setPaymentAmount(paymentAmount); } catch (Exception e1) { } }
	 * 
	 * }
	 * 
	 * CustomerCreditsAndPaymentsDialiog creditsAndPaymentsDialog;
	 * CashDiscountDialog cashDiscountDialog; WriteOffDialog writeOffDialog;
	 * 
	 * ClientAccount cashDiscountAccount; ClientAccount writeOffAccount;
	 * List<ClientTransactionCreditsAndPayments> transactionCreditsAndPayments =
	 * new ArrayList<ClientTransactionCreditsAndPayments>();
	 * 
	 * Double cashDiscount = 0.0D; Double writeOffAmount = 0.0D; Double
	 * applyCreditsAmount = 0.0D; Double paymentAmount = 0.0D; Double amountDue
	 * = 0.0D; Long invoiceNumber; Double invoiceAmount = 0.0D; Date
	 * discountDate; ClientInvoice invoice; ClientCustomerRefund customerRefund;
	 * 
	 * List<ClientCreditsAndPayments> selectedCreditsForThisRecord = new
	 * ArrayList<ClientCreditsAndPayments>();
	 * 
	 * public void setWriteOffDialog(WriteOffDialog writeOffDialog) {
	 * this.writeOffDialog = writeOffDialog; }
	 * 
	 * public void setCashDiscountDialog(CashDiscountDialog cashDiscountDialog)
	 * { this.cashDiscountDialog = cashDiscountDialog; }
	 * 
	 * public void setCreditsAndPaymentsDialog(
	 * CustomerCreditsAndPaymentsDialiog creditsAndPaymentsDialog) {
	 * this.creditsAndPaymentsDialog = creditsAndPaymentsDialog; }
	 * 
	 * public void setCustomerRefund(ClientCustomerRefund customerRefund) {
	 * this.customerRefund = customerRefund; }
	 * 
	 * // public ClientCustomerRefund getCustomerRefund() { // return
	 * customerRefund; // } // // public void setInvoice(ClientInvoice invoice)
	 * { // this.invoice = invoice; // } // // public ClientInvoice getInvoice()
	 * { // return invoice; // } // // public void setDiscountDate(Date
	 * discountDate) { // this.discountDate = discountDate; //
	 * setAttribute(ATTR_DISCOUNT_DATE, discountDate); // } // // public Date
	 * getDiscountDate() { // return discountDate; // }
	 * 
	 * public void setInvoiceAmount(Double invoiceAmount) { if (invoiceAmount !=
	 * null) this.invoiceAmount = invoiceAmount; else this.invoiceAmount = 0.0D;
	 * 
	 * // setAttribute(ATTR_INVOICE_AMOUNT, DataUtils //
	 * .getAmountAsString(this.invoiceAmount)); }
	 * 
	 * // // public Double getInvoiceAmount() { // return invoiceAmount; // }
	 * 
	 * public void setInvoiceNumber(Long invoiceNumber) { if (invoiceNumber !=
	 * null) this.invoiceNumber = invoiceNumber;
	 * 
	 * // setAttribute(ATTR_INVOICE, "" + invoiceNumber); }
	 * 
	 * public Long getInvoiceNumber() { return invoiceNumber; }
	 * 
	 * private Date dueDate;
	 * 
	 * public void setDueDate(Date dueDate) { this.dueDate = dueDate; //
	 * setAttribute(ATTR_DUEDATE, dueDate); }
	 * 
	 * // public Date getDueDate() { // return dueDate; // }
	 * 
	 * public void setAmountDue(Double amountDue) { if (amountDue != null)
	 * this.amountDue = amountDue; else this.amountDue = 0.0D; //
	 * setAttribute(ATTR_AMOUNT_DUE, DataUtils //
	 * .getAmountAsString(amountDue)); }
	 * 
	 * public Double getAmountDue() { return amountDue; }
	 * 
	 * public ClientAccount getCashDiscountAccount() { return
	 * cashDiscountAccount; }
	 * 
	 * public void setCashDiscountAccount(ClientAccount cashDiscountAccount) {
	 * this.cashDiscountAccount = cashDiscountAccount; }
	 * 
	 * // public ClientAccount getWriteOffAccount() { // return writeOffAccount;
	 * // }
	 * 
	 * public void setWriteOffAccount(ClientAccount writeOffAccount) {
	 * this.writeOffAccount = writeOffAccount; }
	 * 
	 * public List<ClientTransactionCreditsAndPayments>
	 * getTransactionCreditsAndPayments() { return
	 * transactionCreditsAndPayments; }
	 * 
	 * public void setTransactionCreditsAndPayments(
	 * List<ClientTransactionCreditsAndPayments> transactionCreditsAndPayments)
	 * { this.transactionCreditsAndPayments = transactionCreditsAndPayments; }
	 * 
	 * // public Double getCashDiscount() { // return cashDiscount; // }
	 * 
	 * public void setCashDiscount(Double cashDiscount) { // throws
	 * PaymentExcessException { if (cashDiscount == null) cashDiscount = 0.0D;
	 * 
	 * // if (cashDiscount < 0D || cashDiscount > 1000000000000.00) // throw new
	 * PaymentExcessException( // AccounterErrorType.INVALID_NEGATIVE_AMOUNT);
	 * // // if (!isValidAmount(cashDiscount, writeOffAmount, //
	 * applyCreditsAmount)) { // throw new PaymentExcessException( //
	 * AccounterErrorType.RECEIVEPAYMENT_AMOUNT_DUE); // } else {
	 * this.cashDiscount = cashDiscount; Double paymentAmount = amountDue -
	 * (cashDiscount + writeOffAmount + applyCreditsAmount);
	 * setPaymentAmount(paymentAmount); // setAttribute(ATTR_CASH_DISCOUNT,
	 * DataUtils // .getAmountAsString(cashDiscount));
	 * 
	 * // }
	 * 
	 * }
	 * 
	 * private boolean isValidAmount(Double cashDiscAmt, Double writeOffAmt,
	 * Double appCreditsAmt) {
	 * 
	 * Double total = cashDiscAmt + writeOffAmt + appCreditsAmt;
	 * 
	 * return total.compareTo(amountDue) <= 0; }
	 * 
	 * public Double getWriteOffAmout() { return writeOffAmount; }
	 * 
	 * public void setWriteOffAmout(Double writeOffAmout) { if (writeOffAmout ==
	 * null) writeOffAmout = 0.0D; // if (writeOffAmout < 0D || writeOffAmout >
	 * 1000000000000.00) // throw new PaymentExcessException( //
	 * AccounterErrorType.INVALID_NEGATIVE_AMOUNT); // // if
	 * (!(isValidAmount(cashDiscount, writeOffAmout, // applyCreditsAmount))) {
	 * // throw new PaymentExcessException(); // } else { this.writeOffAmount =
	 * writeOffAmout; Double paymentAmount = amountDue - (cashDiscount +
	 * writeOffAmount + applyCreditsAmount); setPaymentAmount(paymentAmount); //
	 * setAttribute(ATTR_WRITEOFF, DataUtils //
	 * .getAmountAsString(writeOffAmout));
	 * 
	 * // }
	 * 
	 * }
	 * 
	 * public Double getApplyCreditsAmount() { return applyCreditsAmount; }
	 * 
	 * public void setApplyCreditsAmount(Double applyCreditsAmount) { if
	 * (applyCreditsAmount == null) applyCreditsAmount = 0.0D;
	 * 
	 * // if (!(isValidAmount(cashDiscount, writeOffAmount, //
	 * applyCreditsAmount))) { // throw new PaymentExcessException( //
	 * AccounterErrorType.RECEIVEPAYMENT_AMOUNT_DUE); // } else {
	 * this.applyCreditsAmount = applyCreditsAmount;
	 * 
	 * Double paymentAmount = amountDue - (cashDiscount + writeOffAmount +
	 * applyCreditsAmount); setPaymentAmount(paymentAmount); //
	 * setAttribute(ATTR_APPLIED_CREDITS, DataUtils //
	 * .getAmountAsString(applyCreditsAmount));
	 * 
	 * // }
	 * 
	 * }
	 * 
	 * public Double getPaymentAmount() { return paymentAmount; }
	 * 
	 * public void setPaymentAmount(Double paymentAmount) { if (paymentAmount ==
	 * null) paymentAmount = 0.0D;
	 * 
	 * // if (paymentAmount < 0D || paymentAmount > 1000000000000.00); // throw
	 * new PaymentExcessException( //
	 * AccounterErrorType.INVALID_NEGATIVE_AMOUNT);
	 * 
	 * // if (!(isValidPayment(cashDiscount, writeOffAmount, //
	 * applyCreditsAmount, paymentAmount))) { // throw new
	 * PaymentExcessException(); // } else {
	 * 
	 * this.paymentAmount = paymentAmount; // setAttribute(ATTR_PAYMENTS,
	 * DataUtils // .getAmountAsString(paymentAmount));
	 * 
	 * // }
	 * 
	 * }
	 * 
	 * private boolean isValidPayment(Double cashDiscount, Double
	 * writeOffAmount, Double applyCreditsAmount, Double paymentAmount) {
	 * 
	 * Double total = cashDiscount + writeOffAmount + applyCreditsAmount +
	 * paymentAmount;
	 * 
	 * return total.compareTo(amountDue) <= 0; }
	 * 
	 * // public AccounterListGrid getGrid() { // return gridView; // }
	 * 
	 * }
	 */
	public void recalculateGridAmounts() {
		this.transactionTotal = getGridTotal();
		this.unUsedPayments = (amountRecieved - transactionTotal);
		setUnusedPayments(unUsedPayments);
	}

	@Override
	protected void paymentMethodSelected(String paymentMethod2) {
		super.paymentMethodSelected(paymentMethod2);
	}

	protected void depositInAccountSelected(ClientAccount depositInAccount2) {
		this.depositInAccount = depositInAccount2;
		if (depositInAccount != null && depositInCombo != null) {

			depositInCombo.setComboItem(getCompany().getAccount(
					depositInAccount.getID()));
			depositInCombo.setDisabled(isInViewMode());
		}

	}

	public Double getCustomerBalance() {

		return customerBalance != null ? customerBalance : 0.0D;

	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();

		// Validations
		// 1. isValidTransactionDate?
		// 2. isInPreventPostingBeforeDate?
		// 3. formItem validation
		// 4. isBlankTransaction?
		// 5. validateGrid?
		// 6. isValidReceivePaymentAmount?
		// 7. unUsedPaymentsAmount > 0 add warning
		if (!AccounterValidator.isValidTransactionDate(this.transactionDate)) {
			result.addError(transactionDateItem, accounterConstants
					.invalidateTransactionDate());
		}

		if (AccounterValidator
				.isInPreventPostingBeforeDate(this.transactionDate)) {
			result.addError(transactionDateItem, accounterConstants
					.invalidateDate());
		}

		result.add(FormItem.validate(customerCombo, paymentMethodCombo,
				depositInCombo));

		if (gridView == null || gridView.getRecords().isEmpty()
				|| gridView.getSelectedRecords().size() == 0) {
			result.addError(gridView, Accounter.constants().youDontHaveAnyTransactionsToMakeReceivePayment());
		} else if (AccounterValidator.isBlankTransaction(gridView)) {
			result
					.addError(gridView, Accounter.constants()
							.selectTransaction());
		} else
			result.add(gridView.validateGrid());

		if (!isInViewMode()) {
			try {
				if (!AccounterValidator.isValidRecievePaymentAmount(
						DataUtils.getAmountStringAsDouble(amtText.getValue()
								.toString()), this.transactionTotal)) {
					result.addError(amtText, Accounter.constants()
							.recievePaymentTotalAmount());
				}
			} catch (Exception e) {
				result.addError(amtText, accounterConstants.invalidAmount());
			}
		}
		if (!isInViewMode()
				&& DecimalUtil.isGreaterThan(unUsedPaymentsText.getAmount(), 0))
			result.addWarning(unUsedPaymentsText,
					AccounterWarningType.recievePayment);

		return result;
	}

	@Override
	public void reload() {

		if (gridView == null)
			return;
		gridView.removeAllRecords();
		super.reload();
	}

	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.customerCombo.setFocus();
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

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		switch (command) {
		case AccounterCommand.CREATION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.CUSTOMER)
				this.customerCombo.addComboItem((ClientCustomer) core);

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.depositInCombo.addComboItem((ClientAccount) core);

			break;

		case AccounterCommand.UPDATION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.CUSTOMER)
				this.customerCombo.updateComboItem((ClientCustomer) core);

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.depositInCombo.updateComboItem((ClientAccount) core);

			break;

		case AccounterCommand.DELETION_SUCCESS:
			if (core.getObjectType() == AccounterCoreType.CUSTOMER)
				this.customerCombo.removeComboItem((ClientCustomer) core);

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.depositInCombo.removeComboItem((ClientAccount) core);

			break;
		}

	}

	@Override
	public void onEdit() {
		if (transaction.canEdit && !transaction.isVoid()) {

			Accounter.showWarning(AccounterWarningType.RECEIVEPAYMENT_EDITING,
					AccounterType.WARNING, new ErrorDialogHandler() {

						@Override
						public boolean onCancelClick() {
							return true;
						}

						@Override
						public boolean onNoClick() {
							return true;

						}

						@Override
						public boolean onYesClick() {
							voidTransaction();
							return true;
						}
					});

		} else if (transaction.isVoid() || transaction.isDeleted())

			Accounter.showError(Accounter.constants()
					.youcanteditreceivePaymentitisvoidedordeleted());
	}

	private void voidTransaction() {
		AccounterAsyncCallback<Boolean> callback = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showError(Accounter.constants()
						.failedtovoidReceivePayment());
			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result) {
					enableFormItems();

				} else
					onFailure(new Exception());

			}

		};
		if (transaction != null) {
			AccounterCoreType type = UIUtils.getAccounterCoreType(transaction
					.getType());
			rpcDoSerivce.voidTransaction(type, transaction.id, callback);
		}
	}

	private void enableFormItems() {
		setMode(EditMode.EDIT);
		transactionDateItem.setDisabled(isInViewMode());
		transactionNumber.setDisabled(isInViewMode());

		customerCombo.setDisabled(isInViewMode());
		amtText.setDisabled(isInViewMode());
		paymentMethodCombo.setDisabled(isInViewMode());

		super.onEdit();

		gridView.removeFromParent();
		initListGrid();
		gridLayout.insert(gridView, 2);

		getTransactionReceivePayments(this.getCustomer());
		memoTextAreaItem.setDisabled(isInViewMode());
		transaction = new ClientReceivePayment();
		data = transaction;
		// this.rpcUtilService.getTransactionReceivePayments(customer
		// .getID(),
		// new
		// AccounterAsyncCallback<ArrayList<ReceivePaymentTransactionList>>() {
		//
		// public void onException(AccounterException caught) {
		// Accounter.showError(FinanceApplication
		// .constants()
		// .failedToGetRecievePayments()
		// + customer.getName());
		// }
		//
		// public void onSuccess(
		// List<ReceivePaymentTransactionList> result) {
		//
		// // List<ClientTransactionReceivePayment> list = gridView
		// // .getRecords();
		// // List<ReceivePaymentTransactionList> listTobeAded =
		// // new ArrayList<ReceivePaymentTransactionList>();
		// // boolean isUsed = false;
		// // for (ReceivePaymentTransactionList payment : result)
		// // {
		// // isUsed = false;
		// // for (ClientTransactionReceivePayment usedPayments :
		// // list) {
		// // if (usedPayments.getInvoice().equals(
		// // payment.getTransactionId()))
		// // isUsed = true;
		// // }
		// // if (!isUsed)
		// // listTobeAded.add(payment);
		// // }
		//
		// if (result != null && result.size() > 0) {
		// addTransactionRecievePayments(result);
		// }
		//
		// }
		//
		// });

		// getTransactionReceivePayments(this.customer);

	}

	@Override
	public void print() {
		// Nothing TO DO

	}

	@Override
	public void printPreview() {
		// NOTHING TO DO.
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().receivePayment();
	}

	private CustomerCombo createCustomerComboItem(String title) {

		CustomerCombo customerCombo = new CustomerCombo(title != null ? title
				: Global.get().customer());
		customerCombo.setHelpInformation(true);
		customerCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientCustomer>() {

					@Override
					public void selectedComboBoxItem(ClientCustomer selectItem) {
						customerSelected(selectItem);

					}

				});

		customerCombo.setRequired(true);
		customerCombo.setDisabled(isInViewMode());
		// formItems.add(customerCombo);
		return customerCombo;

	}

	public ClientCustomer getCustomer() {
		return customer;
	}

	public void setCustomer(ClientCustomer customer) {
		this.customer = customer;
	}

	private void initCustomers() {
		List<ClientCustomer> result = getCompany().getActiveCustomers();

		customerCombo.initCombo(result);
		// customerCombo.setHelpInformation(true);
		customerCombo.setDisabled(isInViewMode());

	}

	private void initDepositInAccounts() {
		depositInCombo.setAccounts();
		depositInAccount = depositInCombo.getSelectedValue();
		if (depositInAccount != null)
			depositInCombo.setComboItem(depositInAccount);
	}

	private DepositInAccountCombo createDepositInComboItem() {

		DepositInAccountCombo accountCombo = new DepositInAccountCombo(
				Accounter.constants().depositIn());
		accountCombo.setHelpInformation(true);
		accountCombo.setRequired(true);

		accountCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					public void selectedComboBoxItem(ClientAccount selectItem) {

						depositInAccountSelected(selectItem);

					}

				});
		accountCombo.setDisabled(isInViewMode());
		accountCombo.setAccounts();

		// formItems.add(accountCombo);

		return accountCombo;

	}

	@Override
	public void updateNonEditableItems() {
		// TODO Auto-generated method stub

	}
}