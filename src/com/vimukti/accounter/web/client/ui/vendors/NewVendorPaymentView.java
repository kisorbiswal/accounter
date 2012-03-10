package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientPayBill;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.JNSI;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.TaxItemCombo;
import com.vimukti.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class NewVendorPaymentView extends
		AbstractTransactionBaseView<ClientPayBill> {

	private CheckboxItem printCheck;
	private AmountField amountText, endBalText, vendorBalText;
	private DynamicForm payForm;
	protected boolean isClose;
	protected String paymentMethod = UIUtils
			.getpaymentMethodCheckBy_CompanyType(messages.check());

	boolean isChecked = false;

	private ArrayList<DynamicForm> listforms;
	private TaxItemCombo tdsCombo;
	private CheckboxItem amountIncludeTds;
	private AmountLabel vendorPayment;
	private AmountLabel tdsAmount;
	private AmountLabel totalAmount;
	private StyledPanel tdsPanel;
	private double toBeSetEndingBalance;
	private double toBeSetVendorBalance;
	protected ClientCurrency selectCurrency;

	private NewVendorPaymentView() {
		super(ClientTransaction.TYPE_PAY_BILL);

	}

	public void resetElements() {
		this.setVendor(null);
		this.addressListOfVendor = null;
		this.payFromAccount = null;
		this.paymentMethod = UIUtils
				.getpaymentMethodCheckBy_CompanyType(messages.check());
		amountText.setAmount(0D);
		endBalText.setAmount(0D);
		vendorBalText.setAmount(0D);
		memoTextAreaItem.setValue("");
	}

	@Override
	protected void initTransactionViewData() {

		if (transaction == null) {
			setData(new ClientPayBill());
		} else {
			ClientCompany comapny = getCompany();

			if (currencyWidget != null) {
				this.currency = getCompany().getCurrency(
						transaction.getCurrency());
				this.currencyFactor = transaction.getCurrencyFactor();
				currencyWidget.setSelectedCurrency(this.currency);
				// currencyWidget.currencyChanged(this.currency);
				currencyWidget.setCurrencyFactor(transaction
						.getCurrencyFactor());
				currencyWidget.setEnabled(!isInViewMode());
			}

			if (transaction.isAmountIncludeTDS()) {
				amountText.setAmount(transaction.getUnusedAmount());
			} else {
				amountText.setAmount(transaction.getTotal()
						- transaction.getTdsTotal());
			}
			ClientVendor vendor = comapny.getVendor(transaction.getVendor());
			vendorCombo.select(vendor);
			vendorSelected(vendor);
			billToaddressSelected(transaction.getAddress());
			this.payFromAccount = comapny.getAccount(transaction.getPayFrom());
			if (payFromAccount != null)
				payFromCombo.select(payFromAccount);
			amountText.setEnabled(!isInViewMode());
			paymentMethodSelected(transaction.getPaymentMethod());
			if (transaction != null) {
				printCheck.setEnabled(false);
				checkNo.setEnabled(false);
				ClientPayBill clientPayBill = transaction;
				paymentMethodCombo.setComboItem(clientPayBill
						.getPaymentMethod());
				paymentMethodCombo.setEnabled(!isInViewMode());
			}

			// if (currency != null) {
			// currencyCombo.setValue(currency.getFormalName());
			// }

			endBalText.setAmount(transaction.getEndingBalance());
			if (vendor != null) {
				vendorBalText.setAmount(vendor.getBalance());
			}

			if (transaction.getCheckNumber() != null) {
				if (transaction.getCheckNumber().equals(messages.toBePrinted())) {
					checkNo.setValue(messages.toBePrinted());
					printCheck.setValue(true);
				} else {
					checkNo.setValue(transaction.getCheckNumber());
					printCheck.setValue(false);
				}
			}
			initAccounterClass();
			if (getPreferences().isTDSEnabled()) {
				long tdsTaxItem = transaction.getTdsTaxItem();
				ClientTAXItem taxItem = getCompany().getTAXItem(tdsTaxItem);
				tdsCombo.select(taxItem);
				amountIncludeTds.setValue(transaction.isAmountIncludeTDS());
				tdsCombo.setEnabled(false);
				amountIncludeTds.setEnabled(false);
			}
			adjustBalance();
		}
		initMemoAndReference();
		initTransactionNumber();
		initPayFromAccounts();
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));
	}

	@Override
	protected void initMemoAndReference() {
		memoTextAreaItem.setValue(transaction.getMemo());
		memoTextAreaItem.setDisabled(isInViewMode());
	}

	@Override
	protected void createControls() {
		Label lab1 = new Label(messages.payeePrePayment(Global.get().Vendor()));

		lab1.setStyleName("label-title");

		transactionDateItem = createTransactionDateItem();

		transactionNumber = createTransactionNumberItem();

		listforms = new ArrayList<DynamicForm>();
		locationCombo = createLocationCombo();
		DynamicForm dateNoForm = new DynamicForm("dateNoForm");
		dateNoForm.setStyleName("datenumber-panel");
		dateNoForm.add(transactionDateItem, transactionNumber);

		StyledPanel datepanel = new StyledPanel("datepanel");
//		datepanel.setWidth("100%");
		datepanel.add(dateNoForm);

		StyledPanel labeldateNoLayout = new StyledPanel("labeldateNoLayout");
//		labeldateNoLayout.setWidth("100%");
		labeldateNoLayout.add(datepanel);

		vendorCombo = createVendorComboItem(messages.payTo());

		billToCombo = createBillToComboItem();
		billToCombo.setEnabled(false);

		// Ending and Vendor Balance
		endBalText = new AmountField(messages.bankBalance(), this,
				getBaseCurrency(), "endBalText");
//		endBalText.setWidth(100);
		endBalText.setEnabled(false);

		vendorBalText = new AmountField(messages.payeeBalance(Global.get()
				.Vendor()), this, getBaseCurrency(), "vendorBalText");
		vendorBalText.setEnabled(false);
//		vendorBalText.setWidth(100);

		// currencyCombo = new CurrencyCombo(messages.currency());
		// currencyCombo.setDisabled(isInViewMode());
		// currencyCombo
		// .addSelectionChangeHandler(new
		// IAccounterComboSelectionChangeHandler<ClientCurrency>() {
		//
		// @Override
		// public void selectedComboBoxItem(ClientCurrency selectItem) {
		// selectCurrency = selectItem;
		// }
		// });

		DynamicForm balForm = new DynamicForm("balForm");
		if (locationTrackingEnabled)
			balForm.add(locationCombo);
		// balForm.setWidth("100%");
		// if (isMultiCurrencyEnabled())
		// balForm.setFields(currencyCombo);
		balForm.add(endBalText, vendorBalText);
		if (getPreferences().isClassTrackingEnabled()
				&& getPreferences().isClassOnePerTransaction()) {
			classListCombo = createAccounterClassListCombo();
			balForm.add(classListCombo);
		}
		// balForm.getCellFormatter().setWidth(0, 0, "205px");

		// Payment
		payFromCombo = createPayFromCombo(messages.payFrom());
//		payFromCombo.setPopupWidth("500px");
		amountText = new AmountField(messages.amount(), this,
				getBaseCurrency(), "amountText");
//		amountText.setWidth(100);
		amountText.setRequired(true);
		amountText.addBlurHandler(getBlurHandler());

		paymentMethodCombo = createPaymentMethodSelectItem();
		paymentMethodCombo.setComboItem(UIUtils
				.getpaymentMethodCheckBy_CompanyType(messages.check()));

		printCheck = new CheckboxItem(messages.toBePrinted(), "printCheck");
		printCheck.setValue(true);
		printCheck.addChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				isChecked = event.getValue();
				if (isChecked) {
					if (printCheck.getValue().toString()
							.equalsIgnoreCase("true")) {
						checkNo.setValue(messages.toBePrinted());
						checkNo.setEnabled(false);
					} else {
						if (payFromAccount == null)
							checkNo.setValue(messages.toBePrinted());
						else if (isInViewMode()) {
							checkNo.setValue(transaction.getCheckNumber());
						}
					}
				} else

					checkNo.setValue("");
				checkNo.setEnabled(true);

			}
		});
		checkNo = createCheckNumberItem();
		checkNo.setValue(messages.toBePrinted());
//		checkNo.setWidth(100);
		checkNo.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				checkNumber = checkNo.getValue().toString();
			}
		});

		payForm = UIUtils.form(messages.payment());
		// payForm.setWidth("80%");
		// payForm.setHeight("90%");
		memoTextAreaItem = createMemoTextAreaItem();
//		memoTextAreaItem.setWidth(100);
		payForm.add(vendorCombo, billToCombo, payFromCombo, amountText,
				paymentMethodCombo, printCheck, checkNo, memoTextAreaItem);

		endBalText
				.setAmount(payFromCombo.getSelectedValue() != null ? payFromCombo
						.getSelectedValue().getCurrentBalance() : 0.00);

		// payForm.setWidth("100%");
		// payForm.getCellFormatter().setWidth(0, 0, "160px");

		StyledPanel leftPanel = new StyledPanel("leftPanel");
		// leftPanel.setWidth("100%");
		leftPanel.add(payForm);

		StyledPanel rightPanel = new StyledPanel("rightPanel");
		rightPanel.add(balForm);
		this.tdsCombo = new TaxItemCombo(messages.tds(),
				ClientTAXAgency.TAX_TYPE_TDS);
		tdsCombo.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXItem>() {

			@Override
			public void selectedComboBoxItem(ClientTAXItem selectItem) {
				adjustBalance();
			}
		});
		this.amountIncludeTds = new CheckboxItem(messages.amountIncludesTDS(),
				"amountIncludeTds");
		amountIncludeTds.addChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				adjustBalance();
			}
		});
		this.vendorPayment = new AmountLabel(messages.payeePayment(Global.get()
				.Vendor()));
		this.tdsAmount = new AmountLabel(messages.tdsAmount());
		this.totalAmount = new AmountLabel(messages.total());

		this.tdsPanel = new StyledPanel("tdsPanel");
//		this.tdsPanel.setWidth("100%");

		if (getPreferences().isTDSEnabled()) {
			DynamicForm form = new DynamicForm("form");
			form.add(tdsCombo, amountIncludeTds);
			tdsPanel.add(form);

			DynamicForm amountsForm = new DynamicForm("amountsForm");
//			amountsForm.setHeight("70px");
			amountsForm.setStyleName("boldtext");
			amountsForm.add(vendorPayment);
			amountsForm.add(tdsAmount);
			amountsForm.add(totalAmount);
			tdsPanel.add(amountsForm);
			// tdsPanel.setCellHeight(amountsForm, "200px");
			rightPanel.add(tdsPanel);
		}

		currencyWidget = createCurrencyFactorWidget();
		if (isMultiCurrencyEnabled()) {
			rightPanel.add(currencyWidget);
		}

		StyledPanel hLay = new StyledPanel("hLay");
//		hLay.setWidth("100%");
		hLay.add(leftPanel);
		hLay.add(rightPanel);

		StyledPanel mainVLay = new StyledPanel("mainVLay");
		mainVLay.add(lab1);
		mainVLay.add(voidedPanel);
//		mainVLay.setSize("100%", "100%");
		mainVLay.add(labeldateNoLayout);
		mainVLay.add(hLay);

		this.add(mainVLay);

//		setSize("100%", "100%");
		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(balForm);
		listforms.add(payForm);

		settabIndexes();

	}

	private void settabIndexes() {
		vendorCombo.setTabIndex(1);
		billToCombo.setTabIndex(2);
		payFromCombo.setTabIndex(3);
		amountText.setTabIndex(4);
		paymentMethodCombo.setTabIndex(5);
		printCheck.setTabIndex(6);
		checkNo.setTabIndex(7);
		memoTextAreaItem.setTabIndex(8);
		transactionDateItem.setTabIndex(9);
		transactionNumber.setTabIndex(10);
		endBalText.setTabIndex(11);
		vendorBalText.setTabIndex(12);
		if (saveAndCloseButton != null)
			saveAndCloseButton.setTabIndex(13);
		if (saveAndNewButton != null)
			saveAndNewButton.setTabIndex(14);
		cancelButton.setTabIndex(15);
	}

	public ClientPayBill saveView() {
		ClientPayBill saveView = super.saveView();
		if (saveView != null) {
			updateTransaction();
		}
		return saveView;
	}

	@Override
	public void saveAndUpdateView() {
		updateTransaction();
		super.saveAndUpdateView();
		if (transaction != null) {
			transaction.setType(ClientTransaction.TYPE_PAY_BILL);
			saveOrUpdate(transaction);
		}

	}

	@Override
	protected void accountSelected(ClientAccount account) {
		super.accountSelected(account);
		this.endBalText.setAmount(account.getCurrentBalance());
		endBalText.setCurrency(getCurrency(account.getCurrency()));
		adjustBalance();
	}

	@Override
	protected void updateTransaction() {
		super.updateTransaction();
		if (transaction != null) {
			transaction.setPayBillType(ClientPayBill.TYPE_VENDOR_PAYMENT);

			if (getVendor() != null) {
				transaction.setVendor(getVendor());

				if (billingAddress != null)
					transaction.setAddress(billingAddress);

				if (payFromAccount != null)
					transaction.setPayFrom(payFromAccount);
				if (getPreferences().isTDSEnabled()
						&& getVendor().isTdsApplicable()) {
					transaction.setTotal(totalAmount.getAmount());
					ClientTAXItem selectedValue = tdsCombo.getSelectedValue();
					if (selectedValue != null) {
						transaction.setTdsTaxItem(selectedValue.getID());
					}
					transaction.setTdsTotal(tdsAmount.getAmount());
					transaction
							.setAmountIncludeTDS(amountIncludeTds.getValue());
				} else {
					transaction.setTotal(totalAmount.getAmount());
				}

				transaction.setPaymentMethod(paymentMethodCombo
						.getSelectedValue());

				if (checkNo.getValue() != null
						&& !checkNo.getValue().equals("")) {
					String value;
					if (checkNo.getValue().toString()
							.equalsIgnoreCase(messages.toBePrinted())) {
						value = String.valueOf(messages.toBePrinted());
					} else {
						value = String.valueOf(checkNo.getValue());
					}
					transaction.setCheckNumber(value);
				} else {
					transaction.setCheckNumber("");
				}
				if (transaction.getID() != 0) {
					printCheck.setValue(transaction.isToBePrinted());
				} else
					printCheck.setValue(true);

				// if (currencyCombo.getSelectedValue() != null)
				// transaction.setCurrency(currencyCombo.getSelectedValue()
				// .getID());

				// Setting Memo
				transaction.setMemo(getMemoTextAreaItem());

				transaction.setEndingBalance(toBeSetEndingBalance);

				transaction.setVendorBalance(toBeSetVendorBalance);

				// Setting UnusedAmount
				transaction.setUnusedAmount(transaction.getTotal());

				if (currency != null) {
					transaction.setCurrency(currency.getID());
				}
				transaction.setCurrencyFactor(currencyWidget
						.getCurrencyFactor());
			}
		}
	}

	@Override
	protected void paymentMethodSelected(String paymentMethod) {
		if (paymentMethod == null)
			return;

		if (paymentMethod != null) {
			this.paymentMethod = paymentMethod;
			if (paymentMethod.equalsIgnoreCase(messages.cheque())) {
				printCheck.setEnabled(true);
				checkNo.setEnabled(true);
			} else {
				printCheck.setEnabled(false);
				checkNo.setEnabled(false);
			}
		}

	}

	@Override
	protected void vendorSelected(ClientVendor vendor) {
		if (vendor == null) {
			return;
		}
		this.setVendor(vendor);
		ClientCurrency clientCurrency = getCurrency(vendor.getCurrency());
		amountText.setCurrency(clientCurrency);
		vendorBalText.setCurrency(clientCurrency);

		tdsPanel.setVisible(vendor.isTdsApplicable());
		this.addressListOfVendor = vendor.getAddress();
		initBillToCombo();
		long taxItemCode = vendor.getTaxItemCode();
		ClientTAXItem taxItem = getCompany().getTAXItem(taxItemCode);
		if (taxItem != null) {
			tdsCombo.setComboItem(taxItem);
		}
		vendorBalText.setAmount(vendor.getBalance());
		adjustBalance();
		currencyWidget.setSelectedCurrencyFactorInWidget(clientCurrency,
				transactionDateItem.getValue().getDate());
		if (!isInViewMode()) {
			paymentMethodCombo.setComboItem(vendor.getPaymentMethod());
		}
	}

	protected void setCheckNumber() {

		rpcUtilService.getNextCheckNumber(payFromAccount.getID(),
				new AccounterAsyncCallback<Long>() {

					@Override
					public void onException(AccounterException t) {
						// //UIUtils.logError(
						// "Failed to get the next check number!!", t);
						checkNo.setValue(messages.toBePrinted());
						return;
					}

					@Override
					public void onResultSuccess(Long result) {
						if (result == null)
							onFailure(null);

						checkNumber = String.valueOf(result);
						checkNo.setValue(result.toString());
					}

				});

	}

	public void adjustBalance() {

		double enteredBalance = amountText.getAmount();
		if (DecimalUtil.isLessThan(enteredBalance, 0)
				|| DecimalUtil.isGreaterThan(enteredBalance, 1000000000000.00)) {
			amountText.setAmount(0D);
			return;
		}

		double vendorPayment = 0.00D;
		double tdsAmount = 0.00D;
		double totalAmount = 0.00D;
		if (getPreferences().isTDSEnabled() && getVendor() != null
				&& getVendor().isTdsApplicable()) {
			if (!amountIncludeTds.isChecked()) {
				vendorPayment = amountText.getAmount();
				ClientTAXItem selectedTax = tdsCombo.getSelectedValue();
				if (selectedTax != null) {
					tdsAmount = vendorPayment
							* (selectedTax.getTaxRate() / 100);
				}
				totalAmount = vendorPayment + tdsAmount;
			} else {
				totalAmount = amountText.getAmount();
				ClientTAXItem selectedTax = tdsCombo.getSelectedValue();
				double taxRate = 0.00D;
				if (selectedTax != null) {
					taxRate = selectedTax.getTaxRate();
				}
				vendorPayment = (totalAmount * 100) / (100 + taxRate);
				tdsAmount = totalAmount - vendorPayment;
			}
		} else {
			vendorPayment = enteredBalance;
			totalAmount = enteredBalance;
		}

		this.vendorPayment.setAmount(vendorPayment);
		this.tdsAmount.setAmount(tdsAmount);
		this.totalAmount.setAmount(totalAmount);

		if (getVendor() != null) {
			toBeSetVendorBalance = getVendor().getBalance() - totalAmount;
			// vendorBalText
			// .setAmount(getAmountInTransactionCurrency(toBeSetVendorBalance));

		}
		if (payFromAccount != null) {
			double balanceToBeUpdate;
			if (payFromAccount.getCurrency() == getPreferences()
					.getPrimaryCurrency().getID()) {
				balanceToBeUpdate = enteredBalance;
			} else {
				balanceToBeUpdate = enteredBalance;
			}

			if (payFromAccount.isIncrease()) {
				toBeSetEndingBalance = payFromAccount
						.getTotalBalanceInAccountCurrency()
						+ balanceToBeUpdate
						* vendor.getCurrencyFactor();
			} else {
				toBeSetEndingBalance = payFromAccount
						.getTotalBalanceInAccountCurrency()
						- balanceToBeUpdate
						* vendor.getCurrencyFactor();
			}

			// endBalText
			// .setAmount(getAmountInTransactionCurrency(toBeSetEndingBalance));
		}
	}

	@Override
	public void updateNonEditableItems() {

	}

	@Override
	public ValidationResult validate() {

		ValidationResult result = super.validate();

		if (vendorCombo.getSelectedValue() == null) {
			vendorCombo.setValue("");
		}
		if (AccounterValidator.isInPreventPostingBeforeDate(transactionDate)) {
			result.addError(transactionDate, messages.invalidateDate());
		}

		if (!AccounterValidator.isPositiveAmount(amountText.getAmount())) {
			amountText.textBox.addStyleName("highlightedFormItem");
			result.addError(amountText,
					messages.valueCannotBe0orlessthan0(messages.amount()));
		}

		ClientAccount bankAccount = payFromCombo.getSelectedValue();
		// check if the currency of accounts is valid or not
		if (bankAccount != null) {
			ClientCurrency bankCurrency = getCurrency(bankAccount.getCurrency());
			if (bankCurrency != getBaseCurrency() && bankCurrency != currency) {
				result.addError(payFromCombo,
						messages.selectProperBankAccount());
			}
		}

		result.add(payForm.validate());

		if (getPreferences().isTDSEnabled() && vendor != null
				&& vendor.isTdsApplicable()) {
			ClientTAXItem selectedValue = tdsCombo.getSelectedValue();
			if (selectedValue == null) {
				result.addError(tdsCombo, messages.pleaseSelectTDS());
			}
		}
		return result;
	}

	public static NewVendorPaymentView getInstance() {

		return new NewVendorPaymentView();

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
		this.vendorCombo.setFocus();
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

	private BlurHandler getBlurHandler() {
		BlurHandler blurHandler = new BlurHandler() {

			Object value = null;

			@Override
			public void onBlur(BlurEvent event) {
				try {

					value = amountText.getValue();

					if (value == null)
						return;

					Double amount = DataUtils.getAmountStringAsDouble(JNSI
							.getCalcultedAmount(value.toString()));
					if (DecimalUtil.isLessThan(amount, 0)) {
						Accounter.showError(messages.noNegativeAmounts());
						amountText.setAmount(0.00D);

					}

					amountText
							.setAmount(DataUtils.isValidAmount(amount + "") ? amount
									: 0.0);

					adjustBalance();

				} catch (Exception e) {
					if (e instanceof InvalidEntryException) {
						Accounter.showError(e.getMessage());
					}
					amountText.setAmount(0.0);
				}

			}
		};

		return blurHandler;
	}

	@Override
	public void onEdit() {
		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				int errorCode = caught.getErrorCode();
				Accounter.showError(AccounterExceptions
						.getErrorString(errorCode));
			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		AccounterCoreType type = UIUtils.getAccounterCoreType(transaction
				.getType());
		this.rpcDoSerivce.canEdit(type, transaction.id, editCallBack);

	}

	protected void enableFormItems() {
		setMode(EditMode.EDIT);
		vendorCombo.setEnabled(!isInViewMode());
		transactionDateItem.setEnabled(!isInViewMode());
		transactionNumber.setEnabled(!isInViewMode());
		printCheck.setEnabled(!isInViewMode());
		checkNo.setEnabled(!isInViewMode());
		amountText.setEnabled(!isInViewMode());
		paymentMethodCombo.setEnabled(!isInViewMode());
		// currencyCombo.setDisabled(isInViewMode());
		paymentMethodSelected(paymentMethodCombo.getSelectedValue());
		if (printCheck.getValue().toString().equalsIgnoreCase("true")) {
			checkNo.setValue(messages.toBePrinted());
			checkNo.setEnabled(false);
		}
		if (paymentMethodCombo.getSelectedValue().equalsIgnoreCase(
				messages.cheque())
				&& printCheck.getValue().toString().equalsIgnoreCase("true")) {
			checkNo.setValue(messages.toBePrinted());
			checkNo.setEnabled(true);
		}
		memoTextAreaItem.setDisabled(false);
		if (locationTrackingEnabled)
			locationCombo.setEnabled(!isInViewMode());
		tdsCombo.setEnabled(true);
		amountIncludeTds.setEnabled(true);
		if (currencyWidget != null) {
			currencyWidget.setEnabled(!isInViewMode());
		}

		super.onEdit();

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
		return this.totalAmount.getAmount();
	}

	@Override
	protected String getViewTitle() {
		return messages.payeePayment(Global.get().Vendor());
	}

	@Override
	protected void refreshTransactionGrid() {

	}

	@Override
	public void updateAmountsFromGUI() {
		adjustBalance();
	}

	@Override
	protected boolean canRecur() {
		return false;
	}

	protected void updateDiscountValues() {
		// TODO Auto-generated method stub

	}
}
