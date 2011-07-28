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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientPayBill;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class NewVendorPaymentView extends
		AbstractVendorTransactionView<ClientPayBill> {

	private CheckboxItem printCheck;
	private AmountField amountText, endBalText, vendorBalText;
	protected ClientPayBill takenPayBill;
	protected double enteredBalance;
	private DynamicForm vendorForm;
	private DynamicForm payForm;
	Double toBeSetEndingBalance = 0.0D;
	Double toBeSetVendorBalance;
	protected boolean isClose;
	protected String paymentMethod = UIUtils
			.getpaymentMethodCheckBy_CompanyType(Accounter.constants().check());
	@SuppressWarnings("unused")
	private CheckboxItem thisisVATinclusive;

	boolean isChecked = false;

	private ArrayList<DynamicForm> listforms;

	private NewVendorPaymentView() {
		super(ClientTransaction.TYPE_PAY_BILL, 0);
		this.validationCount = 4;

	}

	@Override
	protected void initTransactionViewData() {
		// if (transactionObject == null)
		// resetElements();
		super.initTransactionViewData();
		initTransactionNumber();
		if (transactionObject == null)
			if (transactionObject == null)
				initPayFromAccounts();

	}

	public void resetElements() {
		this.vendor = null;
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
	protected void initTransactionViewData(ClientTransaction transactionObject) {
		ClientCompany comapny = getCompany();

		ClientPayBill payBillToBeEdited = (ClientPayBill) transactionObject;
		amountText.setAmount((Double) payBillToBeEdited.getUnusedAmount());
		ClientVendor vendor = comapny.getVendor(payBillToBeEdited.getVendor());
		vendorSelected(vendor);
		billToaddressSelected(payBillToBeEdited.getAddress());
		// accountSelected(comapny.getAccount(payBillToBeEdited.getPayFrom()));
		this.payFromAccount = comapny
				.getAccount(payBillToBeEdited.getPayFrom());
		if (payFromAccount != null)
			payFromCombo.setComboItem(payFromAccount);
		amountText.setDisabled(true);
		paymentMethodSelected(payBillToBeEdited.getPaymentMethod());
		if (transactionObject != null) {
			printCheck.setDisabled(true);
			checkNo.setDisabled(true);
			ClientPayBill clientPayBill = (ClientPayBill) transactionObject;
			paymentMethodCombo.setComboItem(clientPayBill.getPaymentMethod());
		}

		endBalText.setAmount(payBillToBeEdited.getEndingBalance());
		vendorBalText.setAmount(vendor.getBalance());

		if (payBillToBeEdited.getCheckNumber() != null) {
			if (payBillToBeEdited.getCheckNumber().equals(
					Accounter.constants().toBePrinted())) {
				checkNo.setValue(Accounter.constants().toBePrinted());
				printCheck.setValue(true);
			} else {
				checkNo.setValue(payBillToBeEdited.getCheckNumber());
				printCheck.setValue(false);
			}
		}
		initMemoAndReference();
		initTransactionViewData();

	}

	@Override
	protected void createControls() {
		Label lab1 = new Label(
				UIUtils.getVendorString(Accounter.constants()
						.supplierPrePayment(), Accounter.constants()
						.vendorPrePayment())
		// + "(" + getTransactionStatus() + ") "
		);
		lab1.setStyleName(Accounter.constants().lableTitle());
		// lab1.setHeight("50px");
		// transaction date and number

		transactionDateItem = createTransactionDateItem();

		transactionNumber = createTransactionNumberItem();

		listforms = new ArrayList<DynamicForm>();

		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(4);
		dateNoForm.setStyleName("datenumber-panel");
		dateNoForm.setFields(transactionDateItem, transactionNumber);
		forms.add(dateNoForm);
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

		// Ending and Vendor Balance
		endBalText = new AmountField(Accounter.constants().Endingbalance());
		endBalText.setHelpInformation(true);
		endBalText.setWidth(100);
		endBalText.setDisabled(true);

		vendorBalText = new AmountField(UIUtils.getVendorString(Accounter
				.constants().supplierBalance(), Accounter.constants()
				.Vendorbalance()));
		vendorBalText.setHelpInformation(true);
		vendorBalText.setDisabled(true);
		vendorBalText.setWidth(100);

		DynamicForm balForm = new DynamicForm();
		balForm.setWidth("100%");
		balForm.setFields(endBalText, vendorBalText);
		balForm.getCellFormatter().setWidth(0, 0, "205px");
		forms.add(balForm);

		// Payment
		payFromCombo = createPayFromCombo(Accounter.constants().Payfrom());
		payFromCombo.setPopupWidth("500px");
		amountText = new AmountField(Accounter.constants().Amount());
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

		printCheck = new CheckboxItem(Accounter.constants().Tobeprinted());
		printCheck.setValue(true);
		printCheck.addChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				isChecked = (Boolean) event.getValue();
				if (isChecked) {
					if (printCheck.getValue().toString()
							.equalsIgnoreCase("true")) {
						checkNo.setValue(Accounter.constants().Tobeprinted());
						checkNo.setDisabled(true);
					} else {
						if (payFromAccount == null)
							checkNo.setValueField(Accounter.constants()
									.Tobeprinted());
						else if (transactionObject != null) {
							checkNo.setValue(((ClientPayBill) transactionObject)
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
				.constants().chequeNo() : Accounter.constants().checkno());
		checkNo.setValue(Accounter.constants().Tobeprinted());
		checkNo.setWidth(100);
		checkNo.setDisabled(true);
		checkNo.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				checkNumber = checkNo.getValue().toString();
			}
		});

		payForm = UIUtils.form(Accounter.constants().Payment());
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

		canvas.add(mainVLay);

		setSize("100%", "100%");
		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(balForm);
		listforms.add(payForm);

	}

	@Override
	public void saveAndUpdateView() throws Exception {
		ClientPayBill payBill;
		if (transactionObject != null)
			payBill = (ClientPayBill) transactionObject;
		else
			payBill = new ClientPayBill();

		payBill.setPayBillType(ClientPayBill.TYPE_VENDOR_PAYMENT);

		payBill.setVendor(vendor);

		if (billingAddress != null)
			payBill.setAddress(billingAddress);

		payBill.setPayFrom(payFromAccount);

		// adjustBalance();
		payBill.setTotal(enteredBalance);

		payBill.setPaymentMethod(paymentMethodCombo.getSelectedValue());

		if (checkNo.getValue() != null && !checkNo.getValue().equals("")) {
			String value;
			if (checkNo.getValue().toString()
					.equalsIgnoreCase(Accounter.constants().toBePrinted())) {
				value = String.valueOf(Accounter.constants().Tobeprinted());
			} else {
				value = String.valueOf(checkNo.getValue());
			}
			payBill.setCheckNumber(value);

		} else {
			payBill.setCheckNumber("");

		}
		printCheck.setValue(payBill.isToBePrinted());

		// Setting Memo
		payBill.setMemo(getMemoTextAreaItem());

		// Setting Ref
		// payBill.setReference(getRefText());

		payBill.setEndingBalance(toBeSetEndingBalance);

		payBill.setVendorBalance(toBeSetVendorBalance);

		// Setting UnusedAmount
		payBill.setUnusedAmount(amountText.getAmount());

		transactionObject = payBill;

		super.saveAndUpdateView();
		payBill.setType(ClientTransaction.TYPE_PAY_BILL);

		if (transactionObject.getID() != 0)
			alterObject(payBill);
		else
			createObject(payBill);

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
		this.vendor = vendor;
		if (vendor != null && vendorCombo != null) {
			vendorCombo.setComboItem(getCompany().getVendor(vendor.getID()));
		}
		this.addressListOfVendor = vendor.getAddress();
		initBillToCombo();
		adjustBalance();
	}

	@Override
	protected void accountSelected(ClientAccount account) {

		if (account == null)
			return;
		this.payFromAccount = account;
		payFromCombo.setValue(payFromAccount);
		if (account != null && !(Boolean) printCheck.getValue()) {
			setCheckNumber();
		} else if (account == null)
			checkNo.setValue("");
		adjustBalance();
	}

	protected void setCheckNumber() {

		rpcUtilService.getNextCheckNumber(payFromAccount.getID(),
				new AsyncCallback<Long>() {

					public void onFailure(Throwable t) {
						// //UIUtils.logError(
						// "Failed to get the next check number!!", t);
						checkNo.setValue(Accounter.constants().Tobeprinted());
						return;
					}

					public void onSuccess(Long result) {
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
		if (vendor != null) {
			toBeSetVendorBalance = vendor.getBalance() - enteredBalance;
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
	protected void initMemoAndReference() {

		if (this.transactionObject != null) {

			ClientPayBill payBill = (ClientPayBill) transactionObject;

			if (payBill != null) {
				memoTextAreaItem.setDisabled(true);
				setMemoTextAreaItem(payBill.getMemo());
				// setRefText(payBill.getReference());

			}
		}
	}

	@Override
	public void updateNonEditableItems() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean validate() throws InvalidTransactionEntryException,
			InvalidEntryException {

		switch (this.validationCount) {
		case 4:
			return AccounterValidator
					.validateTransactionDate(this.transactionDate);
		case 3:
			return AccounterValidator.validateForm(payForm, false);
		case 2:
			if (DecimalUtil.isEquals(amountText.getAmount(), 0))
				throw new InvalidTransactionEntryException(
						AccounterErrorType.INVALID_NEGATIVE_AMOUNT);
		case 1:
			// return AccounterValidator.isNull(payFromAccount, paymentMethod);
			return true;

		default:
			return true;
		}

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
	public void deleteFailed(Throwable caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

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
		AsyncCallback<Boolean> editCallBack = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof InvocationException) {
					Accounter
							.showMessage("Your session expired, Please login again to continue");
				} else {
					Accounter.showError(((InvalidOperationException) (caught))
							.getDetailedMessage());
				}
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		AccounterCoreType type = UIUtils.getAccounterCoreType(transactionObject
				.getType());
		this.rpcDoSerivce.canEdit(type, transactionObject.id, editCallBack);

	}

	protected void enableFormItems() {
		isEdit = false;
		vendorCombo.setDisabled(isEdit);
		transactionDateItem.setDisabled(isEdit);
		transactionNumber.setDisabled(isEdit);
		printCheck.setDisabled(isEdit);
		checkNo.setDisabled(isEdit);
		amountText.setDisabled(isEdit);
		paymentMethodCombo.setDisabled(isEdit);
		paymentMethodSelected(paymentMethodCombo.getSelectedValue());
		if (printCheck.getValue().toString().equalsIgnoreCase("true")) {
			checkNo.setValue(Accounter.constants().Tobeprinted());
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

	@Override
	protected Double getTransactionTotal() {
		return this.amountText.getAmount();
	}

	@Override
	protected String getViewTitle() {
		return UIUtils.getVendorString(
				Accounter.constants().supplierPayments(), Accounter.constants()
						.vendorPayments());
	}
}
