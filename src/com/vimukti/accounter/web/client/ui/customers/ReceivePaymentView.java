package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientCurrency;
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
import com.vimukti.accounter.web.client.ui.edittable.tables.TransactionReceivePaymentTable;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.widgets.CurrencyWidget;

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
	private TextItem checkNo;

	private VerticalPanel mainVLay;

	private VerticalPanel gridLayout;

	private Label lab;

	private HorizontalPanel topHLay;

	public TransactionReceivePaymentTable gridView;

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
	private boolean locationTrackingEnabled;
	private CurrencyWidget currencyWidget;

	public ReceivePaymentView() {
		super(ClientTransaction.TYPE_RECEIVE_PAYMENT);
		locationTrackingEnabled = getCompany().getPreferences()
				.isLocationTrackingEnabled();

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
		gridView.newAppliedCreditsDialiog = null;
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

		recalculateGridAmounts();

		long currency = customer.getCurrency();
		if (currency != 0) {
			ClientCurrency clientCurrency = getCompany().getCurrency(currency);
			if (clientCurrency != null) {
				currencyWidget.setSelectedCurrency(clientCurrency);
			}
		}
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
									gridView.initCreditsAndPayments(selectedCustomer);
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

		this.unUsedCreditsText
				.setAmount(getAmountInTransactionCurrency(totalCredits));

	}

	private void setCustomerBalance(Double balance) {

		customerNonEditablebalText
				.setAmount(getAmountInTransactionCurrency(balance));

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

			record.setDueDate(receivePaymentTransaction.getDueDate() != null ? receivePaymentTransaction
					.getDueDate().getDate() : 0);
			record.setNumber(receivePaymentTransaction.getNumber());

			record.setInvoiceAmount(receivePaymentTransaction
					.getInvoiceAmount());

			record.setInvoice(receivePaymentTransaction.getTransactionId());
			record.setAmountDue(receivePaymentTransaction.getAmountDue());
			record.setDummyDue(receivePaymentTransaction.getAmountDue());
			record.setDiscountDate(receivePaymentTransaction.getDiscountDate() != null ? receivePaymentTransaction
					.getDiscountDate().getDate() : 0);

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
			gridView.add(record);
		}
		recalculateGridAmounts();
	}

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
		gridView = new TransactionReceivePaymentTable(!isInViewMode(), this) {

			@Override
			public void updateTotalPayment(Double payment) {
				transactionTotal = getGridTotal();
			}

			@Override
			protected void deleteTotalPayment(double payment) {
				ReceivePaymentView.this.transactionTotal -= payment;
			}

			@Override
			protected void recalculateGridAmounts() {
				ReceivePaymentView.this.recalculateGridAmounts();
			}

			@Override
			protected void setAmountRecieved(double totalInoiceAmt) {
				ReceivePaymentView.this.setAmountRecieved(totalInoiceAmt);
				ReceivePaymentView.this.recalculateGridAmounts();
			}

			@Override
			protected boolean isInViewMode() {
				return ReceivePaymentView.this.isInViewMode();
			}

			@Override
			protected void calculateUnusedCredits() {
				ReceivePaymentView.this.calculateUnusedCredits();

			}
		};
		gridView.setCustomer(this.getCustomer());
		gridView.setDisabled(isInViewMode());
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
			payment.setTransaction(receivePayment.getID());

			if (gridView.newAppliedCreditsDialiog != null) {
				List<ClientTransactionCreditsAndPayments> tranCreditsandPayments = gridView.newAppliedCreditsDialiog != null ? gridView.newAppliedCreditsDialiog
						.getTransactionCredits(payment)
						: new ArrayList<ClientTransactionCreditsAndPayments>();
				if (tranCreditsandPayments != null)
					for (ClientTransactionCreditsAndPayments transactionCreditsAndPayments : tranCreditsandPayments) {
						transactionCreditsAndPayments
								.setTransactionReceivePayment(payment);
					}

				payment.setTransactionCreditsAndPayments(tranCreditsandPayments);
			}
			paymentsList.add(payment);
			payment.getTempCredits().clear();
		}

		return paymentsList;
	}

	@Override
	protected void createControls() {
		if (transaction == null
				|| transaction.getStatus() == ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)
			lab = new Label(Utility.getTransactionName(transactionType));
		else {
			lab = new Label(Utility.getTransactionName(transactionType));
		}
		lab.setStyleName(Accounter.constants().labelTitle());
		transactionDateItem = createTransactionDateItem();
		transactionNumber = createTransactionNumberItem();

		listforms = new ArrayList<DynamicForm>();
		locationCombo = createLocationCombo();
		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(6);
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
				value = amtText.getAmount();
				if (value == null)
					return;
				Double amount = 0.00D;
				try {
					amount = DataUtils.getAmountStringAsDouble(value.toString());
					amtText.setAmount(DataUtils.isValidAmount(value.toString()) ? amount
							: 0.00D);
					paymentAmountChanged(amount);

					if (DecimalUtil.isLessThan(amount, 0)) {
						Accounter.showError(Accounter.constants()
								.noNegativeAmountsReceived());
						amtText.setAmount(0.00D);

					}
				} catch (Exception e) {
					if (e instanceof InvalidEntryException) {
						Accounter.showError(e.getMessage());
					}
					amtText.setAmount(0.00D);
				}

			}

		});

		memoTextAreaItem = createMemoTextAreaItem();
		paymentMethodCombo = createPaymentMethodSelectItem();
		checkNo = createCheckNumberItem();
		checkNo.setDisabled(true);

		payForm = new DynamicForm();
		payForm.setWidth("90%");
		payForm.setIsGroup(true);
		payForm.setGroupTitle(Accounter.constants().payment());

		payForm.setFields(customerCombo, amtText, paymentMethodCombo, checkNo);
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
		if (locationTrackingEnabled)
			depoForm.setFields(locationCombo);
		depoForm.setIsGroup(true);
		depoForm.setGroupTitle(Accounter.constants().deposit());
		depoForm.setFields(customerNonEditablebalText, depositInCombo);
		depoForm.getCellFormatter().setWidth(0, 0, "203px");

		if (getPreferences().isClassTrackingEnabled()
				&& getPreferences().isClassOnePerTransaction()) {
			classListCombo = createAccounterClassListCombo();
			depoForm.setFields(classListCombo);
		}
		currencyWidget = createCurrencyWidget();
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
		textForm.addStyleName("textbold");

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
		if (isMultiCurrencyEnabled()) {
			rightVLay.add(currencyWidget);
		}

		topHLay = new HorizontalPanel();
		topHLay.setWidth("100%");
		topHLay.setSpacing(10);
		topHLay.add(leftVLay);
		topHLay.add(rightVLay);
		topHLay.setCellWidth(leftVLay, "50%");
		topHLay.setCellWidth(rightVLay, "39%");

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

		this.add(mainVLay);
		setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(payForm);
		listforms.add(depoForm);
		listforms.add(textForm);

		settabIndexes();
	}

	private void settabIndexes() {
		customerCombo.setTabIndex(1);
		amtText.setTabIndex(2);
		paymentMethodCombo.setTabIndex(3);
		transactionDateItem.setTabIndex(4);
		transactionNumber.setTabIndex(5);
		customerNonEditablebalText.setTabIndex(6);
		depositInCombo.setTabIndex(7);
		memoTextAreaItem.setTabIndex(8);
		saveAndCloseButton.setTabIndex(9);
		saveAndNewButton.setTabIndex(10);
		cancelButton.setTabIndex(11);
	}

	protected void paymentAmountChanged(Double amount) {

		if (amount == null)
			amount = 0.00D;

		setAmountRecieved(amount);

		recalculateGridAmounts();

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
		if (memoTextAreaItem != null)
			transaction.setMemo(memoTextAreaItem.getValue().toString());

		transaction.setCheckNumber(checkNo.getValue().toString());

		transaction.setCustomerBalance(getCustomerBalance());

		transaction.setAmount(this.amountRecieved);

		transaction
				.setTransactionReceivePayment(getTransactionRecievePayments(transaction));

		transaction.setUnUsedPayments(this.unUsedPayments);
		transaction.setTotal(this.transactionTotal);
		transaction
				.setUnUsedCredits(getAmountInBaseCurrency(this.unUsedCreditsText
						.getAmount()));

		if (currency != null)
			transaction.setCurrency(currency.getID());
		transaction.setCurrencyFactor(currencyWidget.getCurrencyFactor());
	}

	public void setUnusedPayments(Double unusedAmounts) {
		if (unusedAmounts == null)
			unusedAmounts = 0.0D;
		this.unUsedPayments = unusedAmounts;
		this.unUsedPaymentsText.setAmount(unusedAmounts);

	}

	private void setUnUsedCredits(Double unusedCredits) {

		unUsedCreditsText
				.setAmount(getAmountInTransactionCurrency(unusedCredits));

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
			if (currencyWidget != null) {
				this.currency = getCompany().getCurrency(
						transaction.getCurrency());
				this.currencyFactor = transaction.getCurrencyFactor();
				currencyWidget.setSelectedCurrency(this.currency);
				// currencyWidget.currencyChanged(this.currency);
				currencyWidget.setCurrencyFactor(transaction
						.getCurrencyFactor());
			}
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
			if (transaction.getPaymentMethod() != null) {
				paymentMethodCombo.setComboItem(transaction.getPaymentMethod());
				paymentMethod = transaction.getPaymentMethod();
			}
			checkNo.setValue(transaction.getCheckNumber());
			// if (paymentToBeEdited.getReference() != null)
			// refText.setValue(paymentToBeEdited.getReference());
			paymentMethod = transaction.getPaymentMethod();
			setAmountRecieved(transaction.getAmount());

			initTransactionTotalNonEditableItem();
			List<ClientTransactionReceivePayment> tranReceivePaymnetsList = transaction
					.getTransactionReceivePayment();
			initListGridData(tranReceivePaymnetsList);
			this.clientAccounterClass = transaction.getAccounterClass();
			if (getPreferences().isClassTrackingEnabled()
					&& this.clientAccounterClass != null
					&& classListCombo != null) {
				classListCombo.setComboItem(this.getClientAccounterClass());
			}
			gridView.setTranReceivePayments(tranReceivePaymnetsList);
		}
		initTransactionNumber();
		initAccounterClass();
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));
		initCustomers();
	}

	private void initListGridData(List<ClientTransactionReceivePayment> list) {

		for (ClientTransactionReceivePayment receivePayment : list) {
			totalInoiceAmt += receivePayment.getInvoiceAmount();
			this.gridView.add(receivePayment);
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

	public void recalculateGridAmounts() {
		this.transactionTotal = getGridTotal();
		this.unUsedPayments = (amountRecieved - getAmountInTransactionCurrency(transactionTotal));
		setUnusedPayments(unUsedPayments);

		calculateUnusedCredits();
	}

	@Override
	protected void paymentMethodSelected(String paymentMethod2) {
		if (paymentMethod2 == null)
			return;

		this.paymentMethod = paymentMethod2;
		if (paymentMethod.equalsIgnoreCase(Accounter.constants().cheque())) {
			checkNo.setDisabled(false);
		} else {
			checkNo.setDisabled(true);
		}
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

		if (AccounterValidator
				.isInPreventPostingBeforeDate(this.transactionDate)) {
			result.addError(transactionDateItem,
					accounterConstants.invalidateDate());
		}

		result.add(FormItem.validate(customerCombo, paymentMethodCombo,
				depositInCombo));

		if (gridView == null || gridView.getRecords().isEmpty()
				|| gridView.getSelectedRecords().size() == 0) {
			result.addError(gridView, Accounter.constants()
					.pleaseSelectAnyOneOfTheTransactions());
		} else if (gridView.getAllRows().isEmpty()) {
			result.addError(gridView, Accounter.constants().selectTransaction());
		} else
			result.add(gridView.validateGrid());

		if (!isInViewMode()) {
			try {
				if (!AccounterValidator.isValidRecievePaymentAmount(
						DataUtils.getAmountStringAsDouble(amtText.getValue()
								.toString()),
						getAmountInTransactionCurrency(this.transactionTotal))) {
					result.addError(amtText, Accounter.constants()
							.recievePaymentTotalAmount());
				}
			} catch (Exception e) {
				result.addError(amtText, accounterConstants.invalidAmount());
			}
		}
		if (!isInViewMode()
				&& DecimalUtil
						.isGreaterThan(
								getAmountInBaseCurrency(unUsedPaymentsText
										.getAmount()), 0))
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
		if (paymentMethod != null
				&& paymentMethod.equalsIgnoreCase(Accounter.constants()
						.cheque())) {
			checkNo.setDisabled(false);
		}

		depositInCombo.setDisabled(isInViewMode());
		super.onEdit();

		gridView.removeFromParent();
		initListGrid();
		gridLayout.insert(gridView, 2);

		getTransactionReceivePayments(this.getCustomer());
		memoTextAreaItem.setDisabled(isInViewMode());
		transaction = new ClientReceivePayment();
		data = transaction;
		if (locationTrackingEnabled)
			locationCombo.setDisabled(isInViewMode());
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

		return accountCombo;

	}

	@Override
	public void updateNonEditableItems() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void refreshTransactionGrid() {

	}

	@Override
	public void updateAmountsFromGUI() {
		paymentAmountChanged(amtText.getAmount());
	}
}