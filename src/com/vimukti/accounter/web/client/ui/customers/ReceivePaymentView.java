package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
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
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
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
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.CustomerCombo;
import com.vimukti.accounter.web.client.ui.combo.DepositInAccountCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.IPrintableView;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.edittable.tables.TransactionReceivePaymentTable;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

/**
 * 
 * @author Fernandez
 * @implemented By Fernandez
 */
public class ReceivePaymentView extends
		AbstractTransactionBaseView<ClientReceivePayment> implements
		IPrintableView {

	public AmountField customerNonEditablebalText;

	public AmountLabel unUsedCreditsText, unUsedPaymentsText;

	/**
	 * This amount is same as AmountReceived if TDS not enabled or customer
	 * don't have TDS
	 */
	private AmountLabel totalWithTDS;

	// public AmountLabel unUsedCreditsTextForeignCurrency,
	// unUsedPaymentsTextForeignCurrency;

	public AmountField amtText, tdsAmount;
	private DynamicForm payForm;
	private TextItem checkNo;

	private VerticalPanel mainVLay;

	private VerticalPanel gridLayout;

	private Label lab;

	private HorizontalPanel topHLay;

	public TransactionReceivePaymentTable gridView;

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
		super(ClientTransaction.TYPE_RECEIVE_PAYMENT);
	}

	protected void customerSelected(final ClientCustomer selectedCustomer) {

		ClientCurrency currency = getCurrency(selectedCustomer.getCurrency());
		amtText.setCurrency(currency);
		tdsAmount.setCurrency(currency);
		customerNonEditablebalText.setCurrency(currency);
		gridView.creditsAndPayments.clear();
		if (getCustomer() != null && customerCombo != null) {
			customerCombo.setComboItem(getCompany().getCustomer(
					selectedCustomer.getID()));
		}
		this.setCustomer(selectedCustomer);

		tdsAmount.setVisible(isTDSEnable());
		totalWithTDS.setVisible(isTDSEnable());
		if (!tdsAmount.isVisible()) {
			tdsAmount.setAmount(0.00D);
		}

		this.gridView.setCustomer(getCustomer());

		/*
		 * resetting the crdits dialog's refernce,so that a new object will
		 * created for opening credits dialog
		 */

		if (!isInViewMode()) {
			gridView.removeAllRecords();
			gridView.addLoadingImagePanel();
			getTransactionReceivePayments(selectedCustomer);
		}

		paymentMethodCombo.setComboItem(selectedCustomer.getPaymentMethod());

		this.paymentMethod = selectedCustomer.getPaymentMethod();

		setCustomerBalance(selectedCustomer.getBalance());

		this.customerBalance = selectedCustomer.getBalance();

		recalculateGridAmounts();

		if (currency.getID() != 0) {
			currencyWidget.setSelectedCurrencyFactorInWidget(currency,
					transactionDateItem.getDate().getDate());
		} else {
			currencyWidget.setSelectedCurrency(getBaseCurrency());
		}

		if (isMultiCurrencyEnabled()) {
			super.setCurrency(currency);
			setCurrencyFactor(currencyWidget.getCurrencyFactor());
			updateAmountsFromGUI();
		}

	}

	private void getTransactionReceivePayments(
			final ClientCustomer selectedCustomer) {
		if (selectedCustomer == null) {
			return;
		}

		long paymentDate = transactionDateItem.getDate().getDate();
		if (paymentDate == 0) {
			Accounter.showInformation(messages.pleaseSelect(messages
					.transactionDate()));
			return;
		}

		this.rpcUtilService
				.getTransactionReceivePayments(
						selectedCustomer.getID(),
						paymentDate,
						new AccounterAsyncCallback<ArrayList<ReceivePaymentTransactionList>>() {

							@Override
							public void onException(AccounterException caught) {
								Accounter.showError(messages
										.failedToGetRecievePayments(Global
												.get().customer())
										+ selectedCustomer.getName());
								gridView.addEmptyMessage(messages
										.noRecordsToShow());
							}

							@Override
							public void onResultSuccess(
									ArrayList<ReceivePaymentTransactionList> result) {
								if (result == null) {
									result = new ArrayList<ReceivePaymentTransactionList>();
								}

								receivePaymentTransactionList = result;

								if (!result.isEmpty()
										|| !transaction
												.getTransactionReceivePayment()
												.isEmpty()) {
									gridView.removeAllRecords();
									gridView.initCreditsAndPayments(selectedCustomer);
									addTransactionRecievePayments(result);
								} else {
									gridView.removeAllRecords();
									gridView.addEmptyMessage(messages
											.noRecordsToShow());
									totalInoiceAmt = 0.00d;
									totalDueAmt = 0.00d;
									transactionTotal = 0.00d;
									totalWithTDS.setAmount(0.00D);
									// updateFooterValues();
								}
							}

						});
	}

	public void calculateUnusedCredits() {

		if (getCompany().getPreferences().isCreditsApplyAutomaticEnable()) {
			List<ClientTransactionReceivePayment> allRows = gridView
					.getSelectedRecords();
			// First add all credits back
			for (ClientTransactionReceivePayment c : allRows) {
				if (c.creditsAppliedManually) {
					continue;
				}
				// First add all credits back
				for (ClientTransactionCreditsAndPayments ctcap : c
						.getTransactionCreditsAndPayments()) {
					for (ClientCreditsAndPayments ccap : gridView.creditsAndPayments) {
						if (ctcap.getCreditsAndPayments() == ccap.getID()) {
							ccap.setBalance(ccap.getBalance()
									+ ctcap.getAmountToUse());
						}
					}
				}
				// If we have got credits by this time then clear all
				// transaction cap
				if (gridView.creditsAndPayments.size() > 0) {
					c.getTransactionCreditsAndPayments().clear();
				}
			}
			// now distribute again
			for (ClientTransactionReceivePayment c : allRows) {
				if (c.creditsAppliedManually) {
					continue;
				}

				double due = c.getAmountDue();
				if (due <= 0) {
					continue;
				}
				double creditsApplied = 0.0;
				List<ClientTransactionCreditsAndPayments> transactionCreditsAndPayments = c
						.getTransactionCreditsAndPayments();
				for (ClientCreditsAndPayments ccap : gridView.creditsAndPayments) {
					if (ccap.getBalance() > 0 && due > 0) {
						ClientTransactionCreditsAndPayments ctcap = new ClientTransactionCreditsAndPayments();
						double amountToUse = Math.min(due, ccap.getBalance());
						ctcap.setAmountToUse(amountToUse);
						ctcap.setCreditsAndPayments(ccap.getID());
						transactionCreditsAndPayments.add(ctcap);
						ccap.setBalance(ccap.getBalance() - amountToUse);
						due -= amountToUse;
						creditsApplied += amountToUse;
					}
				}
				c.setAppliedCredits(creditsApplied, false);
				c.setCreditsApplied(true);
				// gridView.updatePayment(c);
				gridView.update(c);
				this.transactionTotal = getGridTotal();
				this.unUsedPayments = (totalWithTDS.getAmount() - transactionTotal);
				setUnusedPayments(unUsedPayments);
			}
		}

		this.unUsedCreditsText.setAmount(gridView.getUnusedCredits());

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

		for (ReceivePaymentTransactionList rpt : result) {

			ClientTransactionReceivePayment record = getTransactionPayBillByTransaction(
					rpt.getType(), rpt.getTransactionId());
			double amountDue = 0.00D;
			if (record == null) {
				record = new ClientTransactionReceivePayment();
				if (rpt.getType() == ClientTransaction.TYPE_INVOICE) {
					record.isInvoice = true;
					record.setInvoice(rpt.getTransactionId());
				} else if (rpt.getType() == ClientTransaction.TYPE_CUSTOMER_REFUNDS) {
					record.isInvoice = false;
					record.setCustomerRefund(rpt.getTransactionId());
				} else if (rpt.getType() == ClientTransaction.TYPE_JOURNAL_ENTRY) {
					record.isInvoice = false;
					record.setJournalEntry(rpt.getTransactionId());
				}
			} else {
				transaction.getTransactionReceivePayment().remove(record);
				amountDue = record.getPayment() + record.getAppliedCredits()
						+ record.getCashDiscount() + record.getWriteOff();
			}
			amountDue += rpt.getAmountDue();
			record.setAmountDue(amountDue);
			record.setDummyDue(amountDue);

			record.setDueDate(rpt.getDueDate() != null ? rpt.getDueDate()
					.getDate() : 0);
			record.setNumber(rpt.getNumber());

			record.setInvoiceAmount(rpt.getInvoiceAmount());

			record.setInvoice(rpt.getTransactionId());

			record.setDiscountDate(rpt.getDiscountDate() != null ? rpt
					.getDiscountDate().getDate() : 0);

			record.setDiscountAccount(0);
			record.setCashDiscount(0);

			record.setWriteOff(0);

			record.setAppliedCredits(rpt.getAppliedCredits(), false);

			record.setPayment(0);
			record.setWriteOffAccount(0);

			totalInoiceAmt += rpt.getInvoiceAmount();
			totalDueAmt += rpt.getAmountDue();

			records.add(record);

		}

		for (ClientTransactionReceivePayment payment : transaction
				.getTransactionReceivePayment()) {
			payment.setAmountDue(payment.getPayment()
					+ payment.getAppliedCredits() + payment.getCashDiscount()
					+ payment.getWriteOff());
			payment.setPayment(0.00D);
			payment.setCashDiscount(0.0D);
			payment.setWriteOff(0.0D);
			payment.setDiscountAccount(0);
			payment.setWriteOffAccount(0);
			payment.setAppliedCredits(0.00D, false);
			records.add(payment);
		}

		gridView.setAllRows(records);
		recalculateGridAmounts();
	}

	private ClientTransactionReceivePayment getTransactionPayBillByTransaction(
			int transactionType, long transactionId) {
		for (ClientTransactionReceivePayment bill : transaction
				.getTransactionReceivePayment()) {
			if ((transactionType == ClientTransaction.TYPE_INVOICE && bill
					.getInvoice() == transactionId)
					|| (transactionType == ClientTransaction.TYPE_CUSTOMER_REFUNDS && bill
							.getCustomerRefund() == transactionId)
					|| (transactionType == ClientTransaction.TYPE_JOURNAL_ENTRY && bill
							.getJournalEntry() == transactionId)) {
				return bill;
			}
		}
		return null;
	}

	public Double calculatePaymentForRecord(
			ClientTransactionReceivePayment record) {

		ClientTransactionReceivePayment trpRecord = record;
		Double amountDue = trpRecord.getAmountDue();

		Double cashDiscount = trpRecord.getCashDiscount();

		Double getWriteOffAmt = trpRecord.getWriteOff();

		Double appliedCredits = trpRecord.getAppliedCredits();

		return amountDue - (cashDiscount + getWriteOffAmt + appliedCredits);

	}

	public void setAmountRecieved(Double amountRecieved) {
		this.amountRecieved = amountRecieved;
		this.amtText.setAmount(amountRecieved);
		updateTotalWithTDS();
	}

	public Double getAmountRecieved() {
		return amountRecieved;
	}

	private void initListGrid() {
		gridView = new TransactionReceivePaymentTable(isTrackDiscounts(),
				!isInViewMode(), this) {

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

			@Override
			protected void setUnUsedCreditsTextAmount(Double unUsedCredits) {
				ReceivePaymentView.this.setUnUsedCredits(unUsedCredits);
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
			payment.setID(0);
			payment.setTransaction(receivePayment.getID());

			paymentsList.add(payment);
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
		lab.setStyleName("label-title");
		// transactionDateItem = createTransactionDateItem();
		transactionDateItem = new DateField(messages.date());
		transactionDateItem
				.setToolTip(messages.selectDateWhenTransactioCreated(this
						.getAction().getViewName()));
		transactionDateItem.setHelpInformation(true);
		if (transaction != null && transaction.getDate() != null) {
			transactionDateItem.setEnteredDate(transaction.getDate());
			setTransactionDate(transaction.getDate());

		} else {
			setTransactionDate(new ClientFinanceDate());
			transactionDateItem.setEnteredDate(new ClientFinanceDate());

		}

		transactionDateItem.setDisabled(isInViewMode());
		transactionDateItem.setShowTitle(false);

		transactionDateItem.setWidth(100);
		transactionDateItem.setColSpan(2);
		transactionDateItem
				.addDateValueChangeHandler(new DateValueChangeHandler() {

					@Override
					public void onDateValueChange(ClientFinanceDate date) {
						if (date != null) {
							try {
								ClientFinanceDate newDate = transactionDateItem
										.getValue();
								if (newDate != null) {
									setTransactionDate(newDate);
									gridView.removeAllRecords();
									gridView.addLoadingImagePanel();
									getTransactionReceivePayments(customer);
								}
							} catch (Exception e) {
								Accounter.showError(messages
										.invalidTransactionDate());
							}

						}
					}
				});
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

		customerCombo = createCustomerComboItem(messages.receivedFrom());

		amtText = new AmountField(messages.amountReceived(), this,
				getBaseCurrency());

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
						Accounter.showError(messages
								.noNegativeAmountsReceived());
						amtText.setAmount(0.00D);

					}
				} catch (Exception e) {
					if (e instanceof InvalidEntryException) {
						Accounter.showError(e.getMessage());
					}
					amtText.setAmount(0.00D);
				}
				updateTotalWithTDS();
			}

		});

		tdsAmount = new AmountField(messages.tdsAmount(), this,
				getBaseCurrency());
		tdsAmount.setDisabled(isInViewMode());
		tdsAmount.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				paymentAmountChanged(amountRecieved);
			}
		});

		memoTextAreaItem = createMemoTextAreaItem();
		paymentMethodCombo = createPaymentMethodSelectItem();
		checkNo = createCheckNumberItem();
		checkNo.setDisabled(true);

		payForm = new DynamicForm();
		// payForm.setWidth("90%");
		payForm.setIsGroup(true);
		payForm.setGroupTitle(messages.payment());

		payForm.setFields(customerCombo, amtText, paymentMethodCombo, checkNo);
		payForm.setStyleName("align-form");
		// payForm.getCellFormatter().setWidth(0, 0, "180px");

		customerNonEditablebalText = new AmountField(
				messages.payeeBalance(Global.get().Customer()), this,
				getBaseCurrency());
		customerNonEditablebalText.setHelpInformation(true);
		customerNonEditablebalText.setWidth(100);
		customerNonEditablebalText.setDisabled(true);

		depositInCombo = createDepositInComboItem();
		depositInCombo.setPopupWidth("500px");

		DynamicForm depoForm = new DynamicForm();
		if (locationTrackingEnabled)
			depoForm.setFields(locationCombo);
		depoForm.setIsGroup(true);
		depoForm.setGroupTitle(messages.deposit());
		depoForm.setFields(customerNonEditablebalText, depositInCombo);
		// depoForm.getCellFormatter().setWidth(0, 0, "203px");

		if (getPreferences().isClassTrackingEnabled()
				&& getPreferences().isClassOnePerTransaction()) {
			classListCombo = createAccounterClassListCombo();
			depoForm.setFields(classListCombo);
		}

		depoForm.setFields(tdsAmount);
		tdsAmount.setVisible(getCompany().getPreferences().isTDSEnabled());

		currencyWidget = createCurrencyFactorWidget();
		Label lab1 = new Label(messages.dueForPayment());

		initListGrid();

		unUsedCreditsText = new AmountLabel(
				messages.unusedCreditsWithCurrencyName(getCompany()
						.getPrimaryCurrency().getFormalName()));
		unUsedCreditsText.setHelpInformation(true);
		unUsedCreditsText.setDisabled(true);

		unUsedPaymentsText = new AmountLabel(
				messages.unusedPayments(getCompany().getPrimaryCurrency()
						.getFormalName()));
		unUsedPaymentsText.setHelpInformation(true);
		unUsedPaymentsText.setDisabled(true);

		DynamicForm textForm = new DynamicForm();
		textForm.setWidth("70%");
		textForm.setFields(unUsedCreditsText, unUsedPaymentsText);
		unUsedCreditsText.setVisible(!isInViewMode());

		totalWithTDS = new AmountLabel(messages.total());
		totalWithTDS.setHelpInformation(true);
		textForm.setFields(totalWithTDS);
		// textForm.addStyleName("textbold");

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
		rightVLay.setCellHorizontalAlignment(depoForm,
				HasHorizontalAlignment.ALIGN_RIGHT);
		if (isMultiCurrencyEnabled()) {
			rightVLay.add(currencyWidget);
			rightVLay.setCellHorizontalAlignment(currencyWidget,
					HasHorizontalAlignment.ALIGN_RIGHT);
			currencyWidget.setDisabled(isInViewMode());
		}

		topHLay = new HorizontalPanel();
		topHLay.addStyleName("fields-panel");
		topHLay.setWidth("100%");
		topHLay.setSpacing(10);
		topHLay.add(leftVLay);
		topHLay.add(rightVLay);
		topHLay.setCellWidth(leftVLay, "50%");
		topHLay.setCellWidth(rightVLay, "50%");
		topHLay.setCellHorizontalAlignment(rightVLay, ALIGN_RIGHT);

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
		mainVLay.add(voidedPanel);
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

		// if (isMultiCurrencyEnabled()) {
		// if (!isInViewMode()) {
		// unUsedCreditsTextForeignCurrency.hide();
		// }
		// unUsedPaymentsTextForeignCurrency.hide();
		// }

	}

	protected void updateTotalWithTDS() {
		totalWithTDS.setAmount(getTDSAmount() + amtText.getAmount());
	}

	private double getTDSAmount() {
		if (customer != null && customer.willDeductTDS()) {
			return tdsAmount.getAmount();
		}
		return 0.00D;
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
		if (saveAndCloseButton != null)
			saveAndCloseButton.setTabIndex(9);
		if (saveAndNewButton != null)
			saveAndNewButton.setTabIndex(10);
		cancelButton.setTabIndex(11);
		tdsAmount.setTabIndex(12);
	}

	protected void paymentAmountChanged(Double amount) {

		if (amount == null)
			amount = 0.00D;
		setAmountRecieved(amount);
		recalculateGridAmounts();

	}

	@Override
	public ClientReceivePayment saveView() {
		// ClientReceivePayment saveView = super.saveView();
		// if (saveView != null) {
		// updateTransaction();
		// }
		return null;
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
		transaction.setTotal(totalWithTDS.getAmount());
		transaction.setUnUsedCredits(this.unUsedCreditsText.getAmount());

		if (currency != null)
			transaction.setCurrency(currency.getID());
		transaction.setCurrencyFactor(currencyWidget.getCurrencyFactor());

		if (getPreferences().isTDSEnabled() && customer != null
				&& customer.willDeductTDS()) {
			transaction.setTdsTotal(tdsAmount.getAmount());
		}
	}

	public void setUnusedPayments(Double unusedAmounts) {
		if (unusedAmounts == null)
			unusedAmounts = 0.0D;
		this.unUsedPayments = unusedAmounts;

		this.unUsedPaymentsText.setAmount(unusedAmounts);
	}

	private void setUnUsedCredits(Double unusedCredits) {

		unUsedCreditsText.setAmount(unusedCredits);
		// unUsedCreditsTextForeignCurrency
		// .setAmount(getAmountInTransactionCurrency(unusedCredits));

	}

	protected void initTransactionTotalNonEditableItem() {
		if (transaction == null)
			return;

		ClientReceivePayment recievePayment = transaction;

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
			gridView.setTranactionId(transaction.id);
			if (transaction.getCustomer() != 0) {
				customerCombo.setComboItem(Accounter.getCompany().getCustomer(
						transaction.getCustomer()));
			}
			if (currencyWidget != null) {
				this.currency = getCompany().getCurrency(
						transaction.getCurrency());
				this.currencyFactor = transaction.getCurrencyFactor();
				currencyWidget.setSelectedCurrency(this.currency);
				// currencyWidget.currencyChanged(this.currency);
				currencyWidget.setCurrencyFactor(transaction
						.getCurrencyFactor());
				currencyWidget.setDisabled(isInViewMode());
			}
			this.setCustomer(getCompany()
					.getCustomer(transaction.getCustomer()));
			// customerSelected(getCompany()
			// .getCustomer(transaction.getCustomer()));

			depositInAccountSelected(getCompany().getAccount(
					transaction.getDepositIn()));

			if (tdsAmount != null) {
				tdsAmount.setAmount(transaction.getTdsTotal());
			}
			if (!isTDSEnable()) {
				tdsAmount.setVisible(transaction.getTdsTotal() != 0);
			} else {
				tdsAmount.setVisible(true);
			}

			this.transactionItems = transaction.getTransactionItems();
			memoTextAreaItem.setDisabled(isInViewMode());
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
			if (!tranReceivePaymnetsList.isEmpty()) {
				gridView.setTranReceivePayments(tranReceivePaymnetsList);
			} else {
				gridView.addEmptyMessage(messages.noRecordsToShow());
			}
		}
		initTransactionNumber();
		initAccounterClass();
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));
		initCustomers();
		if (isMultiCurrencyEnabled()) {
			// currencyWidget.setShowFactorField(true);
			if (isInViewMode())
				updateAmountsFromGUI();
		}
	}

	private void initListGridData(List<ClientTransactionReceivePayment> list) {
		if (list != null) {
			for (ClientTransactionReceivePayment receivePayment : list) {
				totalInoiceAmt += receivePayment.getInvoiceAmount();
				this.gridView.add(receivePayment);
				// this.gridView.selectRow(count);
			}
			this.transactionTotal = getGridTotal();
		}
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
		this.unUsedPayments = (totalWithTDS.getAmount() - transactionTotal);
		setUnusedPayments(unUsedPayments);
		calculateUnusedCredits();
	}

	@Override
	protected void paymentMethodSelected(String paymentMethod2) {
		if (paymentMethod2 == null)
			return;

		this.paymentMethod = paymentMethod2;
		if (paymentMethod.equalsIgnoreCase(messages.cheque())) {
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
			result.addError(transactionDateItem, messages.invalidateDate());
		}

		result.add(FormItem.validate(customerCombo, paymentMethodCombo,
				depositInCombo));

		if (gridView == null || gridView.getRecords().isEmpty()
				|| gridView.getSelectedRecords().size() == 0) {
			result.addError(gridView,
					messages.pleaseSelectAnyOneOfTheTransactions());
		} else if (gridView.getAllRows().isEmpty()) {
			result.addError(gridView, messages.selectTransaction());
		} else
			result.add(gridView.validateGrid());

		if (!isInViewMode()) {
			if (!AccounterValidator.isValidRecievePaymentAmount(
					totalWithTDS.getAmount(), this.transactionTotal)) {
				result.addError(amtText, messages.recievePaymentTotalAmount());
			}
		}

		if (!isInViewMode()
				&& DecimalUtil.isGreaterThan(unUsedPaymentsText.getAmount(), 0)) {
			result.addWarning(unUsedPaymentsText, messages.W_107());
		}
		ClientAccount bankAccount = depositInCombo.getSelectedValue();
		// check if the currency of accounts is valid or not
		if (bankAccount != null) {
			ClientCurrency bankCurrency = getCurrency(bankAccount.getCurrency());

			if (bankCurrency != getBaseCurrency() && bankCurrency != currency) {
				result.addError(depositInCombo,
						messages.selectProperBankAccount());
			}
		}
		return result;
	}

	@Override
	public void reload() {

		if (gridView == null)
			return;
		gridView.removeAllRecords();
		super.reload();
	}

	@Override
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
			AsyncCallback<Boolean> editCallBack = new AsyncCallback<Boolean>() {

				@Override
				public void onFailure(Throwable caught) {
					if (caught instanceof InvocationException) {
						Accounter.showMessage(messages.sessionExpired());
					} else {
						int errorCode = ((AccounterException) caught)
								.getErrorCode();
						Accounter.showError(AccounterExceptions
								.getErrorString(errorCode));

					}
				}

				@Override
				public void onSuccess(Boolean result) {
					if (result)
						enableFormItems();
				}

			};

			AccounterCoreType type = UIUtils.getAccounterCoreType(transaction
					.getType());
			this.rpcDoSerivce.canEdit(type, transaction.id, editCallBack);
		} else if (transaction.isVoid() || transaction.isDeleted()) {

			Accounter.showError(messages
					.youcanteditreceivePaymentitisvoidedordeleted());

		}
	}

	private void enableFormItems() {
		setMode(EditMode.EDIT);
		transactionDateItem.setDisabled(isInViewMode());
		transactionNumber.setDisabled(isInViewMode());
		unUsedCreditsText.setVisible(!isInViewMode());
		// customerCombo.setDisabled(isInViewMode());
		amtText.setDisabled(isInViewMode());
		paymentMethodCombo.setDisabled(isInViewMode());
		if (paymentMethod != null
				&& paymentMethod.equalsIgnoreCase(messages.cheque())) {
			checkNo.setDisabled(false);
		}

		depositInCombo.setDisabled(isInViewMode());
		super.onEdit();

		gridView.removeFromParent();
		initListGrid();
		gridView.setTranactionId(transaction.id);
		gridLayout.insert(gridView, 2);

		getTransactionReceivePayments(this.getCustomer());
		memoTextAreaItem.setDisabled(isInViewMode());
		// transaction = new ClientReceivePayment();
		data = transaction;
		if (locationTrackingEnabled)
			locationCombo.setDisabled(isInViewMode());
		if (currencyWidget != null) {
			currencyWidget.setDisabled(isInViewMode());
		}
		tdsAmount.setDisabled(isInViewMode());
		tdsAmount.setVisible(isTDSEnable());
		amtText.setAmount(0.00D);
		tdsAmount.setAmount(0.0D);
		paymentAmountChanged(0.00D);
		updateTotalWithTDS();
	}

	@Override
	public void print() {
		updateTransaction();
		UIUtils.downloadAttachment(transaction.getID(),
				ClientTransaction.TYPE_RECEIVE_PAYMENT);
	}

	@Override
	public void printPreview() {
		// NOTHING TO DO.
	}

	@Override
	protected String getViewTitle() {
		return messages.receivePayment();
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
				messages.depositIn());
		accountCombo.setHelpInformation(true);
		accountCombo.setRequired(true);

		accountCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
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
		gridView.updateAmountsFromGUI();
		paymentAmountChanged(amtText.getAmount());
		modifyForeignCurrencyTotalWidget();

	}

	public void modifyForeignCurrencyTotalWidget() {

		unUsedCreditsText.setTitle(messages
				.unusedCreditsWithCurrencyName(currencyWidget
						.getSelectedCurrency().getFormalName()));
		unUsedPaymentsText.setTitle(messages.unusedPayments(currencyWidget
				.getSelectedCurrency().getFormalName()));
		amtText.setTitle(messages.amountReceivedWithCurrencyName(currencyWidget
				.getSelectedCurrency().getFormalName()));
	}

	@Override
	protected boolean canRecur() {
		return false;
	}

	private boolean isTDSEnable() {
		if (customer != null) {
			return (getPreferences().isTDSEnabled() && customer.willDeductTDS());
		} else {
			return getPreferences().isTDSEnabled();
		}
	}

	protected void updateDiscountValues() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canPrint() {
		EditMode mode = getMode();
		if (mode == EditMode.CREATE || mode == EditMode.EDIT) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean canExportToCsv() {
		return false;
	}

}