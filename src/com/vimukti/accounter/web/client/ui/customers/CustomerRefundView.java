package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientCustomerPrePayment;
import com.vimukti.accounter.web.client.core.ClientCustomerRefund;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PayFromAccountsCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class CustomerRefundView extends
		AbstractCustomerTransactionView<ClientCustomerRefund> {

	protected PayFromAccountsCombo payFromSelect;
	protected ClientAccount selectedAccount;
	protected AmountField amtText;
	private AmountField endBalText, custBalText;
	private TextItem checkNoText;
	private CheckboxItem printCheck;

	private Double refundAmount;
	private Double endingBalance;
	private boolean isChecked = false;
	private Double customerBalanceAmount;
	private List<ClientAccount> payFromAccounts;
	private String checkNumber;
	private ArrayList<DynamicForm> listforms;
	protected DynamicForm payForm;
	private ClientCustomerRefund refund;

	public CustomerRefundView() {
		super(ClientTransaction.TYPE_CUSTOMER_REFUNDS,
				CUSTOMER_TRANSACTION_GRID);
	}

	@Override
	protected void initTransactionViewData() {

		initRPCService();

		initTransactionNumber();

		initCustomers();

		initPayFromAccounts();

	}

	private void initPayFromAccounts() {
		// payFromSelect.initCombo(payFromAccounts);
		// getPayFromAccounts();
		payFromSelect.setAccounts();

		if (transactionObject != null) {
			payFromSelect.setComboItem(getCompany().getAccount(
					((ClientCustomerRefund) transactionObject).getPayFrom()));
		}

		selectedAccount = payFromSelect.getSelectedValue();

	}

	@Override
	protected void customerSelected(ClientCustomer customer) {
		if (customer == null)
			return;
		this.setCustomer(customer);
		if (customer != null && customerCombo != null) {
			customerCombo.setComboItem(getCompany().getCustomer(
					customer.getID()));
		}
		addressListOfCustomer = customer.getAddress();
		super.initBillToCombo();
		setCustomerBalance(customer.getBalance());
		// paymentMethodSelected(customer.getPaymentMethod());

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
				checkNoText.setDisabled(false);

			} else {
				// paymentMethodCombo.setComboItem(paymentMethod);
				printCheck.setDisabled(true);
				checkNoText.setDisabled(true);
			}
		}

	}

	@Override
	protected void createControls() {

		// Label lab1 = new Label(Utility.getTransactionName(transactionType)
		// + "(" + getTransactionStatus() + ")");
		Label lab1 = new Label(Utility.getTransactionName(transactionType));
		lab1.setStyleName(Accounter.constants().labelTitle());
		// lab1.setHeight("35px");
		transactionDateItem = createTransactionDateItem();
		transactionNumber = createTransactionNumberItem();

		listforms = new ArrayList<DynamicForm>();

		DynamicForm dateNoForm = new DynamicForm();
		dateNoForm.setNumCols(4);
		dateNoForm.setStyleName("datenumber-panel");
		dateNoForm.setFields(transactionDateItem, transactionNumber);
		forms.add(dateNoForm);

		HorizontalPanel labeldateNoLayout = new HorizontalPanel();
		labeldateNoLayout.setWidth("100%");
		labeldateNoLayout.add(dateNoForm);
		labeldateNoLayout.setCellHorizontalAlignment(dateNoForm,
				HasHorizontalAlignment.ALIGN_RIGHT);
		labeldateNoLayout.getElement().getStyle().setPaddingRight(15, Unit.PX);

		HorizontalPanel totalLabel = new HorizontalPanel();
		totalLabel.setWidth("100%");
		// totalLabel.add(lab1);
		totalLabel.add(labeldateNoLayout);
		totalLabel.setCellHorizontalAlignment(labeldateNoLayout, ALIGN_RIGHT);

		customerCombo = createCustomerComboItem(customerConstants.payTo());
		// customerCombo.setWidth(100);
		billToCombo = createBillToComboItem();
		billToCombo.setTitle(customerConstants.address());

		custForm = new DynamicForm();

		custForm.setWidth("100%");
		// custForm.setFields(customerCombo, billToCombo);
		custForm.setWidth("100%");

		// custForm.setFields(customerCombo, billToCombo);
		// custForm.setWidth("100%");
		// custForm.getCellFormatter().setWidth(0, 0, "150");

		payFromSelect = new PayFromAccountsCombo(customerConstants.payFrom());
		payFromSelect.setHelpInformation(true);
		payFromSelect.setRequired(true);
		payFromSelect.setDisabled(isEdit);
		// payFromSelect.setWidth("100%");
		payFromSelect.setPopupWidth("500px");
		payFromSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {

						selectedAccount = selectItem;

						setEndingBalance(selectedAccount.getTotalBalance());

					}

				});

		amtText = new AmountField(customerConstants.amount(), this);
		amtText.setHelpInformation(true);
		amtText.setRequired(true);
		amtText.setWidth(100);
		amtText.setDisabled(isEdit);
		amtText.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				try {

					amtText.setAmount(DataUtils.getAmountStringAsDouble(amtText
							.getValue().toString()));
					Double givenAmount = amtText.getAmount();
					if (DecimalUtil.isLessThan(givenAmount, 0)) {
						// BaseView.errordata.setHTML("<li> "
						// + FinanceApplication.constants()
						// .noNegativeAmounts() + ".");
						// BaseView.commentPanel.setVisible(true);
						addError(amtText, Accounter.constants()
								.noNegativeAmounts());
						// Accounter.showError(FinanceApplication
						// .constants().noNegativeAmounts());
						setRefundAmount(0.00D);

					}

					else if (!DecimalUtil.isLessThan(givenAmount, 0)) {
						if (!AccounterValidator.isAmountTooLarge(givenAmount))
							refundAmountChanged(givenAmount);
						// BaseView.errordata.setHTML("");
						// BaseView.commentPanel.setVisible(false);

						setRefundAmount(givenAmount);

					}

				} catch (Exception e) {
					if (e instanceof InvalidEntryException) {
						Accounter.showError(e.getMessage());
					}
					setRefundAmount(0.00D);
				}
			}
		});

		setRefundAmount(null);

		paymentMethodCombo = createPaymentMethodSelectItem();
		// paymentMethodCombo.setWidth(100);
		paymentMethodCombo.setComboItem(UIUtils
				.getpaymentMethodCheckBy_CompanyType(Accounter.constants()
						.check()));

		printCheck = new CheckboxItem(customerConstants.toBePrinted());
		printCheck.setValue(true);
		printCheck.setWidth(100);
		printCheck.addChangeHandler(new ValueChangeHandler<Boolean>() {

			public void onValueChange(ValueChangeEvent<Boolean> event) {
				isChecked = (Boolean) event.getValue();
				if (isChecked) {
					if (printCheck.getValue().toString()
							.equalsIgnoreCase("true")) {
						checkNoText.setValue(Accounter.constants()
								.toBePrinted());
						checkNoText.setDisabled(true);
					} else {
						if (payFromSelect.getValue() == null)
							checkNoText.setValueField(Accounter.constants()
									.toBePrinted());
						else if (transactionObject != null) {
							checkNoText
									.setValue(((ClientCustomerPrePayment) transactionObject)
											.getCheckNumber());
						}
					}
				} else
					// setCheckNumber();
					checkNoText.setValue("");
				checkNoText.setDisabled(false);

			}
		});

		checkNoText = new TextItem(
				getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK ? customerConstants
						.chequeNo() : customerConstants.checkNo());
		checkNoText.setValue(Accounter.constants().toBePrinted());
		checkNoText.setHelpInformation(true);
		checkNoText.setWidth(100);
		if (!paymentMethodCombo.getSelectedValue().equals(
				UIUtils.getpaymentMethodCheckBy_CompanyType(Accounter
						.constants().check())))
			checkNoText.setDisabled(true);
		checkNoText.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				checkNumber = checkNoText.getValue().toString();
			}
		});

		memoTextAreaItem = createMemoTextAreaItem();
		// refText = createRefereceText();
		// refText.setWidth(100);

		// payForm = new DynamicForm();
		// payForm.setWidth("100%");
		// payForm.setFields(payFromSelect, amtText, paymentMethodCombo,
		// printCheck, checkNoText);
		// forms.add(payForm);

		endBalText = new AmountField(customerConstants.endingBalance(), this);
		endBalText.setHelpInformation(true);
		endBalText.setDisabled(true);
		setEndingBalance(null);

		custBalText = new AmountField(customerConstants.customerBalance(), this);
		custBalText.setHelpInformation(true);
		custBalText.setDisabled(true);
		setCustomerBalance(null);

		// DynamicForm memoForm = new DynamicForm();
		// memoForm.setWidth("100%");
		// memoForm.setFields(memoTextAreaItem, refText);
		// memoForm.getCellFormatter().setWidth(0, 0, "150");
		custForm.getCellFormatter().addStyleName(7, 0, "memoFormAlign");
		custForm.setFields(customerCombo, billToCombo, payFromSelect, amtText,
				paymentMethodCombo, printCheck, checkNoText, memoTextAreaItem);
		custForm.setCellSpacing(5);
		custForm.setWidth("100%");
		custForm.getCellFormatter().setWidth(0, 0, "160px");

		DynamicForm balForm = new DynamicForm();
		balForm.setFields(endBalText, custBalText);
		balForm.getCellFormatter().setWidth(0, 0, "205px");
		forms.add(balForm);

		VerticalPanel leftPanel = new VerticalPanel();
		leftPanel.setWidth("100%");
		leftPanel.setSpacing(5);
		leftPanel.add(custForm);
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
		hLay.setCellHorizontalAlignment(totalLabel, ALIGN_CENTER);
		hLay.add(rightPanel);
		hLay.setCellWidth(leftPanel, "50%");
		hLay.setCellWidth(rightPanel, "44%");

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setWidth("100%");
		mainVLay.add(lab1);
		mainVLay.add(totalLabel);
		mainVLay.add(hLay);

		if (UIUtils.isMSIEBrowser()) {
			custForm.getCellFormatter().setWidth(0, 1, "300px");
			custForm.setWidth("75%");
		}
		this.add(mainVLay);

		setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(payForm);
		listforms.add(balForm);

	}

	@Override
	public void saveAndUpdateView() {

		if (transactionObject == null)
			refund = new ClientCustomerRefund();
		else
			refund = (ClientCustomerRefund) transactionObject;

		refund.setDate(transactionDateItem.getEnteredDate().getDate());

		refund.setNumber(transactionNumber.getValue().toString());

		refund.setPayTo(getCustomer().getID());

		if (billingAddress != null)
			refund.setAddress(billingAddress);

		refund.setEndingBalance(endingBalance);

		refund.setCustomerBalance(customerBalanceAmount);
		if (selectedAccount != null)
			refund.setPayFrom(selectedAccount.getID());

		refund.setPaymentMethod(paymentMethod);
		if (checkNoText.getValue() != null
				&& !checkNoText.getValue().equals("")) {
			refund.setCheckNumber(getCheckValue());
		} else
			refund.setCheckNumber("");

		refund.setIsToBePrinted(isChecked);
		refund.setMemo(memoTextAreaItem.getValue().toString());

		// refund.setReference(refText.getValue().toString());

		refund.setType(ClientTransaction.TYPE_CUSTOMER_REFUNDS);

		refund.setTotal(amtText.getAmount());

		refund.setBalanceDue(amtText.getAmount());

		transactionObject = refund;

		super.saveAndUpdateView();

		if (transactionObject.getID() == 0)
			createObject((ClientCustomerRefund) transactionObject);
		else
			alterObject((ClientCustomerRefund) transactionObject);

	}

	private String getCheckValue() {
		String value;
		if (!isEdit) {
			if (checkNoText.getValue().equals(
					Accounter.constants().toBePrinted())) {
				value = String.valueOf(Accounter.constants().toBePrinted());

			} else
				value = String.valueOf(checkNoText.getValue());
		} else {
			String checknumber;
			checknumber = this.checkNumber;
			if (checknumber.equals(Accounter.constants().toBePrinted()))
				value = Accounter.constants().toBePrinted();
			else
				value = String.valueOf(checknumber);
		}
		return value;
	}

	protected void refundAmountChanged(Double givenAmount) {
		if (selectedAccount != null) {
			endingBalance = selectedAccount.getTotalBalance();
			endingBalance -= givenAmount;
			setEndingBalance(endingBalance);
		}
		if (getCustomer() != null) {
			customerBalanceAmount = getCustomer().getBalance();
			customerBalanceAmount += givenAmount;
			setCustomerBalance(customerBalanceAmount);
		}

	}

	protected void setRefundAmount(Double amountValue) {
		if (amountValue == null)
			amountValue = 0.00D;
		amtText.setAmount(amountValue);
		// this.refundAmount = amountValue;

	}

	protected void setEndingBalance(Double totalBalance) {

		if (totalBalance == null)
			totalBalance = 0.0D;

		// if (refundAmount != null)
		// totalBalance -= refundAmount;

		endBalText.setAmount(totalBalance);

		this.endingBalance = totalBalance;

	}

	@Override
	protected void initMemoAndReference() {
		if (transactionObject == null)
			return;

		String memo = ((ClientCustomerRefund) transactionObject).getMemo();

		if (memo != null) {
			memoTextAreaItem.setValue(memo);
		}

		// String refString = ((ClientCustomerRefund) transactionObject)
		// .getReference();
		//
		// if (refString != null) {
		// refText.setValue(refString);
		// }

	}

	@Override
	protected void initSalesTaxNonEditableItem() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initTransactionTotalNonEditableItem() {

		if (transactionObject == null)
			return;

		ClientCustomerRefund customerRefund = ((ClientCustomerRefund) transactionObject);

		setCustomerBalance(customerRefund.getCustomerBalance());
		setEndingBalance(customerRefund.getEndingBalance());

	}

	@Override
	protected void initTransactionViewData(ClientTransaction transactionObject) {
		ClientCustomerRefund customerRefundTobeEdited = (ClientCustomerRefund) transactionObject;

		this.setCustomer(getCompany().getCustomer(
				customerRefundTobeEdited.getPayTo()));
		customerSelected(getCompany().getCustomer(
				customerRefundTobeEdited.getPayTo()));

		amtText.setAmount(customerRefundTobeEdited.getTotal());
		paymentMethodSelected(customerRefundTobeEdited.getPaymentMethod());
		if (transactionObject != null) {
			printCheck.setDisabled(true);

			checkNoText.setDisabled(true);
			ClientCustomerRefund clientCustomerRefund = (ClientCustomerRefund) transactionObject;
			paymentMethodCombo
					.setValue(clientCustomerRefund.getPaymentMethod());
		}

		if (customerRefundTobeEdited.getCheckNumber() != null) {
			if (customerRefundTobeEdited.getCheckNumber().equals(
					Accounter.constants().toBePrinted())) {
				checkNoText.setValue(Accounter.constants().toBePrinted());
				printCheck.setValue(true);
			} else {
				checkNoText.setValue(customerRefundTobeEdited.getCheckNumber());
				printCheck.setValue(false);
			}
		}
		this.selectedAccount = getCompany().getAccount(
				customerRefundTobeEdited.getPayFrom());
		if (selectedAccount != null)
			payFromSelect.setComboItem(selectedAccount);
		this.billingAddress = customerRefundTobeEdited.getAddress();
		if (billingAddress != null)
			billToaddressSelected(billingAddress);

		endBalText
				.setValue(DataUtils.getAmountAsString(customerRefundTobeEdited
						.getEndingBalance()));
		custBalText.setValue(DataUtils
				.getAmountAsString(customerRefundTobeEdited
						.getCustomerBalance()));
		memoTextAreaItem.setDisabled(true);
		memoTextAreaItem.setValue(customerRefundTobeEdited.getMemo());
		// refText.setValue(customerRefundTobeEdited.getReference());
		initTransactionNumber();
	}

	public void setCustomerBalance(Double amount) {
		if (amount == null)
			amount = 0.0D;
		//
		// if (refundAmount != null)
		// amount += refundAmount;

		custBalText.setAmount(amount);

		this.customerBalanceAmount = amount;
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		result.add(super.validate());
		if (!AccounterValidator.validateAmount(this.amtText.getAmount())) {
			result.addError(amtText, AccounterErrorType.INVALID_NEGATIVE_AMOUNT);
		}
		if (AccounterValidator.validateCustomerRefundAmount(
				amtText.getAmount(), selectedAccount)) {
			result.addWarning(amtText,
					AccounterWarningType.INVALID_CUSTOMERREFUND_AMOUNT);
		}
		return result;
	}

	@Override
	protected void priceLevelSelected(ClientPriceLevel priceLevel) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void salesPersonSelected(ClientSalesPerson person) {
		// TODO Auto-generated method stub

	}

	public List<DynamicForm> getForms() {
		// its not using any where
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

			if (core.getObjectType() == AccounterCoreType.CUSTOMER)
				this.customerCombo.addComboItem((ClientCustomer) core);

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.payFromSelect.addComboItem((ClientAccount) core);

			break;

		case AccounterCommand.UPDATION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.CUSTOMER)
				this.customerCombo.updateComboItem((ClientCustomer) core);

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.payFromSelect.updateComboItem((ClientAccount) core);

			break;

		case AccounterCommand.DELETION_SUCCESS:
			if (core.getObjectType() == AccounterCoreType.CUSTOMER)
				this.customerCombo.removeComboItem((ClientCustomer) core);

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.payFromSelect.removeComboItem((ClientAccount) core);

			break;
		}

	}

	@Override
	public void onEdit() {
		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showError(caught.getMessage());
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
		transactionDateItem.setDisabled(isEdit);
		transactionNumber.setDisabled(isEdit);
		customerCombo.setDisabled(isEdit);

		payFromSelect.setDisabled(isEdit);
		amtText.setDisabled(isEdit);
		memoTextAreaItem.setDisabled(isEdit);
		paymentMethodCombo.setDisabled(isEdit);
		paymentMethodSelected(paymentMethodCombo.getSelectedValue());
		if (printCheck.getValue().toString().equalsIgnoreCase("true")) {
			checkNoText.setValue(Accounter.constants().toBePrinted());
			checkNoText.setDisabled(true);
		}
		// paymentMethodSelected(paymentMethodCombo.getValue().toString());
		// checkNoText.setValue(((ClientCustomerRefund) transactionObject)
		// .getCheckNumber());
		// printCheck.setValue(((ClientCustomerRefund) transactionObject)
		// .getIsToBePrinted());
		// if (((ClientCustomerRefund) transactionObject).getIsToBePrinted()) {
		// checkNoText.setDisabled(true);
		// }
		// if (((ClientCustomerRefund) transactionObject).getIsToBePrinted()) {
		// checkNoText.setDisabled(true);
		// }
		// if (((ClientCustomerRefund) transactionObject).getIsToBePrinted()) {
		// checkNoText.setDisabled(true);
		// }
		super.onEdit();
	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {
		// NOTHING TO DO
	}

	@Override
	protected void taxCodeSelected(ClientTAXCode taxCode) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().customerRefund();
	}

}
