package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientPayBill;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class NewVendorPaymentView extends
		AbstractTransactionBaseView<ClientPayBill> {

	private CheckboxItem printCheck;
	private AmountField amountText, endBalText, vendorBalText;
	protected double enteredBalance;
	private DynamicForm vendorForm;
	private DynamicForm payForm;
	Double toBeSetEndingBalance = 0.0D;
	Double toBeSetVendorBalance = 0.0D;
	protected boolean isClose;
	protected String paymentMethod = UIUtils
			.getpaymentMethodCheckBy_CompanyType(Accounter.constants().check());

	private CheckboxItem thisisVATinclusive;

	boolean isChecked = false;

	private ArrayList<DynamicForm> listforms;
	com.vimukti.accounter.web.client.externalization.AccounterConstants accounterConstants = Accounter
			.constants();

	private NewVendorPaymentView() {
		super(ClientTransaction.TYPE_PAY_BILL, 0);

	}

	public void resetElements() {
		this.setVendor(null);
		this.addressListOfVendor = null;
		this.payFromAccount = null;
		this.paymentMethod = UIUtils
				.getpaymentMethodCheckBy_CompanyType(Accounter.constants()
						.check());
		amountText.setAmount(0D);
		endBalText.setAmount(0D);
		vendorBalText.setAmount(0D);
		memoTextAreaItem.setValue("");
		// refText.setValue("");

	}

	@Override
	protected void initTransactionViewData() {

		if (transaction == null) {
			setData(new ClientPayBill());
		} else {
			ClientCompany comapny = getCompany();

			amountText.setAmount((Double) transaction.getUnusedAmount());
			ClientVendor vendor = comapny.getVendor(transaction.getVendor());
			vendorSelected(vendor);
			billToaddressSelected(transaction.getAddress());
			// accountSelected(comapny.getAccount(payBillToBeEdited.getPayFrom()));
			this.payFromAccount = comapny.getAccount(transaction.getPayFrom());
			if (payFromAccount != null)
				payFromCombo.setComboItem(payFromAccount);
			amountText.setDisabled(true);
			paymentMethodSelected(transaction.getPaymentMethod());
			if (transaction != null) {
				printCheck.setDisabled(true);
				checkNo.setDisabled(true);
				ClientPayBill clientPayBill = (ClientPayBill) transaction;
				paymentMethodCombo.setComboItem(clientPayBill
						.getPaymentMethod());
				paymentMethodCombo.setDisabled(true);
			}

			endBalText.setAmount(transaction.getEndingBalance());
			vendorBalText.setAmount(vendor.getBalance());

			if (transaction.getCheckNumber() != null) {
				if (transaction.getCheckNumber().equals(
						Accounter.constants().toBePrinted())) {
					checkNo.setValue(Accounter.constants().toBePrinted());
					printCheck.setValue(true);
				} else {
					checkNo.setValue(transaction.getCheckNumber());
					printCheck.setValue(false);
				}
			}
		}
		initMemoAndReference();
		initTransactionNumber();
		initPayFromAccounts();
	}

	@Override
	protected void createControls() {
		Label lab1 = new Label(messages.vendorPrePayment(Global.get().Vendor()));

		lab1.setStyleName(Accounter.constants().labelTitle());
		// lab1.setHeight("50px");
		// transaction date and number

		transactionDateItem = createTransactionDateItem();

		transactionNumber = createTransactionNumberItem();

		listforms = new ArrayList<DynamicForm>();

		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(4);
		dateNoForm.setStyleName("datenumber-panel");
		dateNoForm.setFields(transactionDateItem, transactionNumber);
		VerticalPanel datepanel = new VerticalPanel();
		datepanel.setWidth("100%");
		datepanel.add(dateNoForm);
		datepanel.setCellHorizontalAlignment(dateNoForm,
				HasHorizontalAlignment.ALIGN_RIGHT);
		datepanel.getElement().getStyle().setPaddingRight(15, Unit.PX);

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();
		labeldateNoLayout.setWidth("100%");
		// labeldateNoLayout.add(lab1);
		labeldateNoLayout.add(datepanel);

		// vendor and address
		vendorCombo = createVendorComboItem(Accounter.constants().payTo());

		billToCombo = createBillToComboItem();
		billToCombo.setDisabled(true);

		// Ending and Vendor Balance
		endBalText = new AmountField(Accounter.constants().endingBalance(),
				this);
		endBalText.setHelpInformation(true);
		endBalText.setWidth(100);
		endBalText.setDisabled(true);

		vendorBalText = new AmountField(messages.vendorBalance(Global.get()
				.Vendor()), this);
		vendorBalText.setHelpInformation(true);
		vendorBalText.setDisabled(true);
		vendorBalText.setWidth(100);

		DynamicForm balForm = new DynamicForm();
		balForm.setWidth("100%");
		balForm.setFields(endBalText, vendorBalText);
		balForm.getCellFormatter().setWidth(0, 0, "205px");

		// Payment
		payFromCombo = createPayFromCombo(Accounter.constants().payFrom());
		payFromCombo.setPopupWidth("500px");
		amountText = new AmountField(Accounter.constants().amount(), this);
		amountText.setHelpInformation(true);
		amountText.setWidth(100);
		amountText.setRequired(true);
		amountText.addBlurHandler(getBlurHandler());
		// amountText.addChangeHandler(new ChangeHandler() {
		//
		// @Override
		// public void onChange(ChangeEvent event) {
		// if (amountText.getValue() != null) {
		// if (amountText.getValue().toString().isEmpty())
		// amountText.setAmount(0.0);
		// else {
		// String s = amountText.getValue().toString();
		// Double d = Double.parseDouble(s);
		// amountText.setAmount(d);
		// }
		// adjustBalance();
		// }
		// }
		//
		// });

		paymentMethodCombo = createPaymentMethodSelectItem();
		// paymentMethodCombo.setWidth(100);
		// paymentMethodCombo.setDefaultValue(UIUtils
		// .getpaymentMethodCheckBy_CompanyType(FinanceApplication
		// .constants().check()));
		paymentMethodCombo.setComboItem(UIUtils
				.getpaymentMethodCheckBy_CompanyType(Accounter.constants()
						.check()));

		printCheck = new CheckboxItem(Accounter.constants().toBePrinted());
		printCheck.setValue(true);
		printCheck.addChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				isChecked = (Boolean) event.getValue();
				if (isChecked) {
					if (printCheck.getValue().toString()
							.equalsIgnoreCase("true")) {
						checkNo.setValue(Accounter.constants().toBePrinted());
						checkNo.setDisabled(true);
					} else {
						if (payFromAccount == null)
							checkNo.setValue(Accounter.constants()
									.toBePrinted());
						else if (isInViewMode()) {
							checkNo.setValue(((ClientPayBill) transaction)
									.getCheckNumber());
						}
					}
				} else

					// setCheckNumber();
					checkNo.setValue("");
				checkNo.setDisabled(false);

			}
		});
		checkNo = createCheckNumberItem(getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK ? Accounter
				.constants().chequeNo() : Accounter.constants().checkNo());
		checkNo.setValue(Accounter.constants().toBePrinted());
		checkNo.setWidth(100);
		checkNo.setDisabled(true);
		checkNo.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				checkNumber = checkNo.getValue().toString();
			}
		});

		payForm = UIUtils.form(Accounter.constants().payment());
		payForm.setWidth("80%");
		payForm.setHeight("90%");
		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setWidth(100);
		// refText = createRefereceText();
		// refText.setWidth(100);
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			payForm.setFields(vendorCombo, billToCombo, payFromCombo,
					amountText, paymentMethodCombo, printCheck, checkNo,
					memoTextAreaItem);
		else
			payForm.setFields(vendorCombo, billToCombo, payFromCombo,
					amountText, paymentMethodCombo, printCheck, checkNo,
					memoTextAreaItem);
		payForm.getCellFormatter().addStyleName(7, 0, "memoFormAlign");
		// memo and Reference

		endBalText
				.setAmount(payFromCombo.getSelectedValue() != null ? payFromCombo
						.getSelectedValue().getCurrentBalance() : 0.00);

		payForm.setCellSpacing(5);
		payForm.setWidth("100%");
		payForm.getCellFormatter().setWidth(0, 0, "160px");

		VerticalPanel leftPanel = new VerticalPanel();
		leftPanel.setWidth("100%");
		leftPanel.setSpacing(5);
		leftPanel.add(payForm);
		// leftPanel.add(payForm);
		// leftPanel.add(memoForm);

		VerticalPanel rightPanel = new VerticalPanel();
		rightPanel.setWidth("100%");
		rightPanel.add(balForm);
		rightPanel.setCellHorizontalAlignment(balForm,
				HasHorizontalAlignment.ALIGN_CENTER);

		HorizontalPanel hLay = new HorizontalPanel();
		hLay.setWidth("100%");
		hLay.setSpacing(10);
		hLay.add(leftPanel);
		hLay.add(rightPanel);
		hLay.setCellWidth(leftPanel, "50%");
		hLay.setCellWidth(rightPanel, "40%");

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.add(lab1);
		mainVLay.setSize("100%", "100%");
		mainVLay.add(labeldateNoLayout);
		mainVLay.add(hLay);

		if (UIUtils.isMSIEBrowser()) {
			payForm.getCellFormatter().setWidth(0, 1, "300px");
			payForm.setWidth("75%");
			balForm.getCellFormatter().setWidth(0, 1, "150px");
		}

		this.add(mainVLay);

		setSize("100%", "100%");
		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(balForm);
		listforms.add(payForm);

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

	protected void updateTransaction() {
		super.updateTransaction();
		if (transaction != null) {
			transaction.setPayBillType(ClientPayBill.TYPE_VENDOR_PAYMENT);

			if (getVendor() != null)
				transaction.setVendor(getVendor());

			if (billingAddress != null)
				transaction.setAddress(billingAddress);

			transaction.setPayFrom(payFromAccount);

			// adjustBalance();
			transaction.setTotal(enteredBalance);

			transaction.setPaymentMethod(paymentMethodCombo.getSelectedValue());

			if (checkNo.getValue() != null && !checkNo.getValue().equals("")) {
				String value;
				if (checkNo.getValue().toString()
						.equalsIgnoreCase(Accounter.constants().toBePrinted())) {
					value = String.valueOf(Accounter.constants().toBePrinted());
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

			// Setting Memo
			transaction.setMemo(getMemoTextAreaItem());

			// Setting Ref
			// transaction.setReference(getRefText());

			transaction.setEndingBalance(toBeSetEndingBalance);

			transaction.setVendorBalance(toBeSetVendorBalance);

			// Setting UnusedAmount
			transaction.setUnusedAmount(amountText.getAmount());

		}
	}

	@Override
	protected void paymentMethodSelected(String paymentMethod) {
		if (paymentMethod == null)
			return;

		if (paymentMethod != null) {
			this.paymentMethod = paymentMethod;
			if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK ? paymentMethod
					.equalsIgnoreCase(Accounter.constants().cheque())
					: paymentMethod.equalsIgnoreCase(Accounter.constants()
							.check())) {

				printCheck.setDisabled(false);
				checkNo.setDisabled(false);

			} else {
				printCheck.setDisabled(true);
				checkNo.setDisabled(true);
			}
		}

	}

	@Override
	protected void vendorSelected(ClientVendor vendor) {

		if (vendor == null)
			return;
		this.setVendor(vendor);
		if (vendor != null && vendorCombo != null) {
			vendorCombo.setComboItem(getCompany().getVendor(vendor.getID()));
		}
		this.addressListOfVendor = vendor.getAddress();
		initBillToCombo();
		adjustBalance();
	}

	protected void setCheckNumber() {

		rpcUtilService.getNextCheckNumber(payFromAccount.getID(),
				new AccounterAsyncCallback<Long>() {

					public void onException(AccounterException t) {
						// //UIUtils.logError(
						// "Failed to get the next check number!!", t);
						checkNo.setValue(Accounter.constants().toBePrinted());
						return;
					}

					public void onResultSuccess(Long result) {
						if (result == null)
							onFailure(null);

						checkNumber = String.valueOf(result);
						checkNo.setValue(result.toString());
					}

				});

	}

	public void adjustBalance() {

		enteredBalance = amountText.getAmount();

		if (DecimalUtil.isLessThan(enteredBalance, 0)
				|| DecimalUtil.isGreaterThan(enteredBalance, 1000000000000.00)) {
			amountText.setAmount(0D);
			enteredBalance = 0D;
		}
		if (getVendor() != null) {
			toBeSetVendorBalance = getVendor().getBalance() - enteredBalance;
			vendorBalText.setAmount(toBeSetVendorBalance);

		}
		if (payFromAccount != null) {
			if (payFromAccount.isIncrease()) {
				toBeSetEndingBalance = payFromAccount.getTotalBalance()
						+ enteredBalance;
			} else {
				toBeSetEndingBalance = payFromAccount.getTotalBalance()
						- enteredBalance;
			}
			endBalText.setAmount(toBeSetEndingBalance);

		}
	}

	@Override
	public void updateNonEditableItems() {
		// TODO Auto-generated method stub

	}

	@Override
	public ValidationResult validate() {

		ValidationResult result = super.validate();
		// Validations
		// 1. is valid transaction date?
		// 2. is in prevent posting before date?
		// 3. pay form valid?

		if (!AccounterValidator.isValidTransactionDate(this.transactionDate)) {
			result.addError(transactionDate,
					accounterConstants.invalidateTransactionDate());
		}
		if (AccounterValidator.isInPreventPostingBeforeDate(transactionDate)) {
			result.addError(transactionDate,
					accounterConstants.invalidateDate());
		}

		result.add(payForm.validate());

		return result;
	}

	// @Override
	// public void setViewConfiguration(ViewConfiguration viewConfiguration)
	// throws Exception {
	//
	// super.setViewConfiguration(viewConfiguration);
	//
	// if (isEdit && (!transactionObject.isPayBill()))
	// throw new Exception("Unable to load the Required PayBill....");
	//
	// if (viewConfiguration.isInitWithPayee()) {
	// ClientPayee payee = viewConfiguration.getPayeeObject();
	//
	// if (payee == null || (!payee.isVendor()))
	// throw new Exception("Required Vendor Could Not be Loaded...");
	// }
	//
	// }

	public static NewVendorPaymentView getInstance() {

		return new NewVendorPaymentView();

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

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.payFromCombo.addComboItem((ClientAccount) core);

			break;
		case AccounterCommand.DELETION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.payFromCombo.removeComboItem((ClientAccount) core);
			break;
		case AccounterCommand.UPDATION_SUCCESS:
			break;
		}

	}

	private BlurHandler getBlurHandler() {
		BlurHandler blurHandler = new BlurHandler() {

			Object value = null;

			public void onBlur(BlurEvent event) {
				try {

					value = amountText.getValue();

					if (value == null)
						return;

					Double amount = DataUtils.getAmountStringAsDouble(value
							.toString());
					if (DecimalUtil.isLessThan(amount, 0)) {
						Accounter.showError(Accounter.constants()
								.noNegativeAmounts());
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
		vendorCombo.setDisabled(isInViewMode());
		transactionDateItem.setDisabled(isInViewMode());
		transactionNumber.setDisabled(isInViewMode());
		printCheck.setDisabled(isInViewMode());
		checkNo.setDisabled(isInViewMode());
		amountText.setDisabled(isInViewMode());
		paymentMethodCombo.setDisabled(isInViewMode());
		paymentMethodSelected(paymentMethodCombo.getSelectedValue());
		if (printCheck.getValue().toString().equalsIgnoreCase("true")) {
			checkNo.setValue(Accounter.constants().toBePrinted());
			checkNo.setDisabled(true);
		}
		memoTextAreaItem.setDisabled(false);
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
		return this.amountText.getAmount();
	}

	@Override
	protected String getViewTitle() {
		return messages.vendorPayments(Global.get().Vendor());
	}

}
