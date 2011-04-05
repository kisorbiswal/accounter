package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.ClientReceivePayment;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientTransactionReceivePayment;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentTransactionList;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.core.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.grids.TransactionReceivePaymentGrid;

/**
 * 
 * @author Fernandez
 * @implemented By Fernandez
 */
public class ReceivePaymentView extends
		AbstractCustomerTransactionView<ClientReceivePayment> {

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

	protected List<ReceivePaymentTransactionList> receivePaymentTransactionList;

	protected ClientTAXCode selectedTaxCode = null;

	public Double amountRecieved = 0.0D, totalInoiceAmt = 0.0D,
			totalDueAmt = 0.0D;

	private Double prevAmountRecieved = 0.00D;

	public double unUsedPayments;

	private Double customerBalance;

	protected Boolean vatInclude = false;

	@SuppressWarnings("unused")
	private boolean gotCreditsAndPayments;

	private ArrayList<DynamicForm> listforms;

	private ClientReceivePayment paymentToBeEdited;

	public ReceivePaymentView() {
		super(ClientTransaction.TYPE_RECEIVE_PAYMENT,
				RECIEVEPAYMENT_TRANSACTION_GRID);
		validationCount = 6;

	}

	@Override
	protected void initTransactionViewData() {
		initTransactionNumber();
		initCustomers();
		initDepositInAccounts();

	}

	@Override
	protected void customerSelected(final ClientCustomer selectedCustomer) {
		if (selectedCustomer == null) {
			receivePaymentTransactionList = null;
			return;
		}
		if (customer != null && customerCombo != null) {
			customerCombo.setComboItem(FinanceApplication.getCompany()
					.getCustomer(selectedCustomer.getStringID()));
		}
		this.customer = selectedCustomer;
		this.gridView.setCustomer(customer);

		gotCreditsAndPayments = false;
		/*
		 * resetting the crdits dialog's refernce,so that a new object will
		 * created for opening credits dialog
		 */
		gridView.creditsAndPaymentsDialiog = null;
		gridView.creditsStack = null;
		gridView.initCreditsAndPayments(customer);

		if (transactionObject == null) {
			gridView.removeAllRecords();
			gridView.addLoadingImagePanel();
			getTransactionReceivePayments(selectedCustomer);

		}

		// if(selectedCustomer.getPaymentMethod())
		paymentMethodCombo.setComboItem(selectedCustomer.getPaymentMethod());

		this.paymentMethod = selectedCustomer.getPaymentMethod();

		setCustomerBalance(selectedCustomer.getBalance());

		this.customerBalance = selectedCustomer.getBalance();

		adjustAmountAndEndingBalance();
	}

	private void getTransactionReceivePayments(
			final ClientCustomer selectedCustomer) {

		long paymentDate = transactionDateItem.getDate().getTime();

		this.rpcUtilService.getTransactionReceivePayments(selectedCustomer
				.getStringID(), paymentDate,
				new AsyncCallback<List<ReceivePaymentTransactionList>>() {

					public void onFailure(Throwable caught) {
						Accounter.showError(FinanceApplication
								.getCustomersMessages()
								.failedToGetRecievePayments()
								+ selectedCustomer.getName());
						gridView.addEmptyMessage(FinanceApplication
								.getCustomersMessages().norecordstoshow());
					}

					public void onSuccess(
							List<ReceivePaymentTransactionList> result) {

						receivePaymentTransactionList = result;

						if (result.size() > 0) {
							gridView.removeAllRecords();
							gridView.initCreditsAndPayments(selectedCustomer);
							addTransactionRecievePayments(result);
						} else {
							gridView.addEmptyMessage(FinanceApplication
									.getCustomersMessages().norecordstoshow());
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
							.getDueDate().getTime()
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
							.getDiscountDate().getTime()
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
		// FIXME
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
		gridView = new TransactionReceivePaymentGrid(!isEdit, true);
		gridView.setPaymentView(this);
		gridView.setCustomer(this.customer);
		gridView.setCanEdit(!isEdit);
		gridView.init();
		gridView.setDisabled(isEdit);
		gridView.setEditEventType(ListGrid.EDIT_EVENT_CLICK);
		gridView.setHeight("200px");
	}

	protected ReceivePaymentTransactionList getRecievePayment(String attribute) {

		Long id = Long.parseLong(attribute);

		for (ReceivePaymentTransactionList recv : receivePaymentTransactionList) {
			if (recv.getTransactionId().equals(id)) {
				return recv;
			}
		}

		return null;
	}

	@SuppressWarnings("unused")
	private void adjustPaymentValue(ClientTransactionReceivePayment rec) {
		Double amountDue = rec.getAmountDue();
		Double cashDiscount = rec.getCashDiscount();
		Double credit = rec.getAppliedCredits();
		rec.setPayment(amountDue - cashDiscount - credit);
		adjustAmountAndEndingBalance();
	}

	private void createOrAlterReceivePayment() throws Exception {

		ClientReceivePayment receivePayment = getReceivePaymentObject();

		if (transactionObject == null)
			createObject(receivePayment);
		else
			alterObject(receivePayment);

	}

	private ClientReceivePayment getReceivePaymentObject() {

		ClientReceivePayment receivePayment = (transactionObject != null) ? (ClientReceivePayment) transactionObject
				: new ClientReceivePayment();

		receivePayment.setDate(transactionDateItem.getValue().getTime());
		if (paymentMethod != null)
			receivePayment.setPaymentMethod(paymentMethod);
		if (depositInAccount != null)
			receivePayment.setDepositIn(depositInAccount.getStringID());
		if (customer != null)
			receivePayment.setCustomer(customer.getStringID());
		if (transactionNumber != null)
			receivePayment.setNumber(transactionNumber.getValue().toString());
		// if (refText != null)
		// receivePayment.setReference(refText.getValue().toString());
		if (memoTextAreaItem != null)
			receivePayment.setMemo(memoTextAreaItem.getValue().toString());

		receivePayment.setCustomerBalance(getCustomerBalance());

		receivePayment.setAmount(this.amountRecieved);
		receivePayment.setTotal(this.gridView.getTotal());

		if (transactionObject == null)
			receivePayment
					.setTransactionReceivePayment(getTransactionRecievePayments(receivePayment));

		receivePayment.setUnUsedPayments(this.unUsedPayments);
		receivePayment.setTotal(this.transactionTotal);

		receivePayment.setUnUsedCredits(this.unUsedCreditsText.getAmount());

		return receivePayment;
	}

	private List<ClientTransactionReceivePayment> getTransactionRecievePayments(
			ClientReceivePayment receivePayment) {
		List<ClientTransactionReceivePayment> paymentsList = new ArrayList<ClientTransactionReceivePayment>();
		for (ClientTransactionReceivePayment payment : gridView
				.getSelectedRecords()) {
			// ClientAccount cashAcc =
			// FinanceApplication.getCompany().getAccount(
			// gridView.getAttribute(FinanceApplication
			// .getCustomersMessages().cashAccount(), gridView
			// .indexOf(payment)));
			// if (cashAcc != null)
			// payment.setDiscountAccount(cashAcc.getStringID());
			//
			// ClientAccount wrrittoff = FinanceApplication.getCompany()
			// .getAccount(
			// gridView.getAttribute(FinanceApplication
			// .getCustomersMessages().writeOff(),
			// gridView.indexOf(payment)));
			// if (wrrittoff != null)
			// payment.setWriteOffAccount(wrrittoff.getStringID());

			payment.setTransaction(receivePayment.getStringID());

			// List<ClientTransactionCreditsAndPayments> trpList =
			// (List<ClientTransactionCreditsAndPayments>) gridView
			// .getAttributeAsObject(FinanceApplication
			// .getCustomersMessages().creditsAndPayments(),
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
		if (transactionObject == null
				|| transactionObject.getStatus() == ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)
			lab = new Label(Utility.getTransactionName(transactionType));
		else {
			// lab = new Label(Utility.getTransactionName(transactionType) + "("
			// + getTransactionStatus() + ")");
			lab = new Label(Utility.getTransactionName(transactionType));
		}
		lab
				.setStyleName(FinanceApplication.getCustomersMessages()
						.lableTitle());
		lab.setHeight("35px");
		transactionDateItem = createTransactionDateItem();
		transactionNumber = createTransactionNumberItem();

		listforms = new ArrayList<DynamicForm>();

		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(4);
		dateNoForm.setFields(transactionDateItem, transactionNumber);

		forms.add(dateNoForm);

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

		customerCombo = createCustomerComboItem(customerConstants
				.receivedFrom());

		amtText = new AmountField(customerConstants.amountReceived());
		amtText.setHelpInformation(true);
		amtText.setWidth(100);
		amtText.setDisabled(isEdit);

		amtText.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				prevAmountRecieved = amountRecieved;
			}

		});

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
						Accounter.showError(FinanceApplication
								.getCustomersMessages()
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
		paymentMethodCombo.setWidth(100);

		payForm = new DynamicForm();
		payForm.setWidth("90%");
		payForm.setIsGroup(true);
		payForm.setGroupTitle(customerConstants.payment());

		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			payForm.setFields(customerCombo, amtText, paymentMethodCombo);
		} else
			payForm.setFields(customerCombo, amtText, paymentMethodCombo);
		payForm.setStyleName("align-form");
		payForm.getCellFormatter().setWidth(0, 0, "180px");

		forms.add(payForm);

		customerNonEditablebalText = new AmountField(customerConstants
				.customerBalance());
		customerNonEditablebalText.setHelpInformation(true);
		customerNonEditablebalText.setWidth(100);
		customerNonEditablebalText.setDisabled(true);

		depositInCombo = createDepositInComboItem();
		depositInCombo.setPopupWidth("500px");

		DynamicForm depoForm = new DynamicForm();
		// depoForm.setWidth("80%");
		depoForm.setIsGroup(true);
		depoForm.setGroupTitle(customerConstants.deposit());
		depoForm.setFields(customerNonEditablebalText, depositInCombo);
		depoForm.getCellFormatter().setWidth(0, 0, "203px");
		forms.add(depoForm);

		Label lab1 = new Label(FinanceApplication.getCustomersMessages()
				.dueForPayment());

		initListGrid();

		unUsedCreditsText = new AmountLabel(customerConstants.unusedCredits());
		unUsedCreditsText.setHelpInformation(true);
		unUsedCreditsText.setDisabled(true);

		unUsedPaymentsText = new AmountLabel(customerConstants.unusedPayments());
		unUsedPaymentsText.setHelpInformation(true);
		unUsedPaymentsText.setDisabled(true);

		DynamicForm textForm = new DynamicForm();
		textForm.setWidth("70%");
		textForm.setFields(unUsedCreditsText, unUsedPaymentsText);
		textForm.addStyleName("unused-payments");
		forms.add(textForm);

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

		@SuppressWarnings("unused")
		VerticalPanel gridAndBalances = new VerticalPanel();

//		HorizontalPanel hLay2 = new HorizontalPanel();
//		hLay2.setWidth("100%");
//		hLay2.setHorizontalAlignment(ALIGN_RIGHT);
//
//		hLay2.add(textForm);

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

		canvas.add(mainVLay);
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
			@SuppressWarnings("unused")
			ClientTAXCode code = (ClientTAXCode) selectedTaxCode;
			@SuppressWarnings("unused")
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
	public void saveAndUpdateView() throws Exception {

		try {
			createOrAlterReceivePayment();

		} catch (Exception e) {

			throw e;
		}

	}

	@SuppressWarnings("unused")
	private void distributeEnteredAmount(Double amount) {

		Double unusedAmounts = 0.0;
		// FIXME
		//
		// for (ClientTransactionReceivePayment record : this.gridView
		// .getRecords()) {
		//
		// TransactionRecievePaymentRecord transactionRecievePaymentRecord =
		// (TransactionRecievePaymentRecord) record;
		//
		// Double recordAmount = transactionRecievePaymentRecord
		// .getAmountDue();
		//
		// int result = amount.compareTo(recordAmount);
		//
		// switch (result) {
		// case 0:
		//
		// // transactionRecievePaymentRecord.setPaymentAmount(recordAmount)
		// // ;
		//
		// break;
		//
		// case -1:
		//
		// // transactionRecievePaymentRecord.setPaymentAmount(recordAmount)
		// // ;
		//
		// break;
		//
		// default:
		//
		// // transactionRecievePaymentRecord.setPaymentAmount(recordAmount)
		// // ;
		//
		// unusedAmounts = recordAmount - amount;
		//
		// break;
		// }
		//
		// if (unusedAmounts <= 0)
		// break;
		// amount = unusedAmounts;
		//
		// }

		setUnusedPayments(unusedAmounts);

	}

	public void setUnusedPayments(Double unusedAmounts) {
		if (unusedAmounts == null)
			unusedAmounts = 0.0D;
		this.unUsedPayments = unusedAmounts;
		this.unUsedPaymentsText.setAmount(unusedAmounts);

	}

	@Override
	protected void initMemoAndReference() {
		if (transactionObject == null)
			return;

		String memo = ((ClientReceivePayment) transactionObject).getMemo();

		if (memo != null) {
			memoTextAreaItem.setValue(memo);
		}

		// String refString = ((ClientReceivePayment) transactionObject)
		// .getReference();
		//
		// if (refString != null) {
		// refText.setValue(refString);
		// }

	}

	private void setUnUsedCredits(Double unusedCredits) {

		unUsedCreditsText.setAmount(unusedCredits);

	}

	@Override
	protected void initSalesTaxNonEditableItem() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initTransactionTotalNonEditableItem() {
		if (transactionObject == null)
			return;

		ClientReceivePayment recievePayment = ((ClientReceivePayment) transactionObject);

		setCustomerBalance(recievePayment.getCustomerBalance());

		Double unusedCredits = recievePayment.getUnUsedCredits();

		Double unusedPayments = recievePayment.getUnUsedPayments();

		setUnUsedCredits(unusedCredits);

		setUnusedPayments(unusedPayments);

	}

	@Override
	protected void initTransactionViewData(ClientTransaction transactionObject) {

		paymentToBeEdited = (ClientReceivePayment) transactionObject;

		this.customer = FinanceApplication.getCompany().getCustomer(
				paymentToBeEdited.getCustomer());
		customerSelected(FinanceApplication.getCompany().getCustomer(
				paymentToBeEdited.getCustomer()));

		depositInAccountSelected(FinanceApplication.getCompany().getAccount(
				paymentToBeEdited.getDepositIn()));

		this.transactionItems = paymentToBeEdited.getTransactionItems();
		if (paymentToBeEdited.getMemo() != null)
			memoTextAreaItem.setValue(paymentToBeEdited.getMemo());
		if (paymentToBeEdited.getPaymentMethod() != null)
			paymentMethodCombo.setComboItem(paymentToBeEdited.getPaymentMethod());
		// if (paymentToBeEdited.getReference() != null)
		// refText.setValue(paymentToBeEdited.getReference());
		setAmountRecieved(paymentToBeEdited.getAmount());
		initTransactionNumber();
		initTransactionTotalNonEditableItem();
		List<ClientTransactionReceivePayment> tranReceivePaymnetsList = paymentToBeEdited
				.getTransactionReceivePayment();
		if (FinanceApplication.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			initListGridData(tranReceivePaymnetsList);
		else {
			initListGridData(tranReceivePaymnetsList);

		}
		gridView.setTranReceivePayments(tranReceivePaymnetsList);

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

	@Override
	protected void priceLevelSelected(ClientPriceLevel priceLevel) {
		// NO Implementation Needed
	}

	@Override
	protected void salesPersonSelected(ClientSalesPerson person) {
		// NO Implementation Needed

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

	@Override
	protected void depositInAccountSelected(ClientAccount depositInAccount2) {
		this.depositInAccount = depositInAccount2;
		super.depositInAccountSelected(this.depositInAccount);
	}

	public Double getCustomerBalance() {

		return customerBalance != null ? customerBalance : 0.0D;

	}

	@Override
	public boolean validate() throws InvalidTransactionEntryException,
			InvalidTransactionEntryException {
		switch (validationCount) {
		case 6:
			return AccounterValidator
					.validateTransactionDate(this.transactionDate);
		case 5:
			return AccounterValidator.validateFormItem(customerCombo,
					paymentMethodCombo, depositInCombo);

		case 4:
			return AccounterValidator
					.validate_TaxAgency_FinanceAcount(depositInAccount);
		case 3:
			if (isEdit)
				return AccounterValidator.isBlankTransactionGrid(gridView);
			else
				return AccounterValidator.validateReceivePaymentGrid(gridView);
		case 2:
			try {
				if (!isEdit) {
					return (AccounterValidator.validateRecievePaymentAmount(
							DataUtils.getAmountStringAsDouble(amtText
									.getValue().toString()),
							this.transactionTotal));
				} else
					return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		case 1:
			if (!isEdit
					&& DecimalUtil.isGreaterThan(
							unUsedPaymentsText.getAmount(), 0))
				return AccounterValidator.validateRecievePayment(this);

			// case 1:
			// if(transactionTotal<=0.0)
			// return AccounterValidator.validatePayment();

		default:
			break;
		}

		try {
			Double amount = DataUtils.getAmountStringAsDouble(amtText
					.getValue().toString());

			Double gridTotals = getGridTotal();

			if (DecimalUtil.compare(amount, gridTotals) < 0) {

				throw new InvalidTransactionEntryException();
			}
		} catch (Exception e) {
		}
		return true;
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
		if (transactionObject.canEdit && !transactionObject.isVoid()) {

			Accounter.showWarning(AccounterWarningType.RECEIVEPAYMENT_EDITING,
					AccounterType.WARNING, new ErrorDialogHandler() {

						@Override
						public boolean onCancelClick()
								throws InvalidEntryException {
							return true;
						}

						@Override
						public boolean onNoClick() throws InvalidEntryException {
							return true;

						}

						@Override
						public boolean onYesClick()
								throws InvalidEntryException {
							voidTransaction();
							return true;
						}
					});

		} else if (transactionObject.isVoid() || transactionObject.isDeleted())

			Accounter
					.showError("You can't edit the ReceivePayment, since it is Voided or Deleted");
	}

	private void voidTransaction() {
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				Accounter.showError("Failed to void Receive Payment");
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					enableFormItems();

				} else
					onFailure(new Exception());

			}

		};
		if (paymentToBeEdited != null) {
			AccounterCoreType type = UIUtils
					.getAccounterCoreType(paymentToBeEdited.getType());
			rpcDoSerivce.voidTransaction(type, paymentToBeEdited.stringID,
					callback);
		}
	}

	private void enableFormItems() {
		isEdit = false;
		transactionDateItem.setDisabled(isEdit);
		transactionNumber.setDisabled(isEdit);

		customerCombo.setDisabled(isEdit);
		amtText.setDisabled(isEdit);
		paymentMethodCombo.setDisabled(isEdit);

		super.onEdit();

		gridView.removeFromParent();
		initListGrid();
		gridLayout.insert(gridView, 2);

		getTransactionReceivePayments(this.customer);

		transactionObject = null;

		// this.rpcUtilService.getTransactionReceivePayments(customer
		// .getStringID(),
		// new AsyncCallback<List<ReceivePaymentTransactionList>>() {
		//
		// public void onFailure(Throwable caught) {
		// Accounter.showError(FinanceApplication
		// .getCustomersMessages()
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
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void taxCodeSelected(ClientTAXCode taxCode) {
		// TODO Auto-generated method stub

	}

}