package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterClientConstants;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientIssuePayment;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionIssuePayment;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.core.Lists.IssuePaymentTransactionsList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PayFromAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.TransactionIssuePaymentGrid;

/**
 * @author Ravi Kiran.G
 * 
 */
public class IssuePaymentDialog extends BaseDialog<ClientIssuePayment> {

	private PayFromAccountsCombo accountCombo;
	private SelectCombo payMethodSelect;
	private List<String> payMethodItemList;

	private TransactionIssuePaymentGrid grid;
	private VerticalPanel gridLayout;
	private Label totalLabel;
	private Label amountLabel;
	protected String selectedpaymentMethod;
	public Double totalAmount = 0D;
	private List<ClientAccount> payFromAccounts;
	private ClientAccount selectedPayFromAccount;
	private TextItem checkNoText;
	private VerticalPanel mainVLay;
	private DynamicForm payForm;
	HorizontalPanel bottomLabelLayOut;
	private String checkNo;
	private String transactionNumber;
	public int validationCount;

	public IssuePaymentDialog(String text, String description) {
		super(text, description);
		this.validationCount = 3;
		createControls();
		getPayFromAccounts();
		setTransactionNumber();
		center();
	}

	private void setTransactionNumber() {

		rpcUtilService.getNextTransactionNumber(
				ClientTransaction.TYPE_ISSUE_PAYMENT,
				new AccounterAsyncCallback<String>() {

					public void onException(AccounterException caught) {
						// UIUtils.logError(
						// "Failed to get the Transaction Number..",
						// caught);
						// //UIUtils.logError(
						// "Failed to get the Transaction Number..",
						// caught);
						return;
					}

					public void onResultSuccess(String result) {
						if (result == null)
							onFailure(null);
						IssuePaymentDialog.this.transactionNumber = result;
					}

				});

	}

	// /**
	// * This method fills the grid with records while initialising the dialog,
	// * See FinanceTool.getChecks() for the record types displaying in this
	// * dialog
	// *
	// */
	// private void fillGrid() {
	//
	// rpcUtilService
	// .getChecks(new
	// AccounterAsyncCallback<ArrayList<IssuePaymentTransactionsList>>() {
	//
	// public void onException(AccounterException caught) {
	// // UIUtils
	// // .logError(
	// // "Failed to get the IssuePaymetTransactionsList",
	// // caught);
	//
	// }
	//
	// public void onSuccess(
	// List<IssuePaymentTransactionsList> result) {
	//
	// for (IssuePaymentTransactionsList entry : result)
	// addRecord(entry);
	//
	// }
	//
	// });
	//
	// }

	protected void addRecord(IssuePaymentTransactionsList entry) {

		ClientTransactionIssuePayment record = new ClientTransactionIssuePayment();

		setValuesToRecord(record, entry);
		grid.addData(record);

	}

	private void setValuesToRecord(ClientTransactionIssuePayment record,
			IssuePaymentTransactionsList entry) {

		if (entry.getDate() != null)
			record.setDate(entry.getDate().getDate());
		if (entry.getNumber() != null)
			record.setNumber(entry.getNumber());
		record.setName(entry.getName() != null ? entry.getName() : "");
		record.setMemo(entry.getMemo() != null ? entry.getMemo() : "");
		if (entry.getAmount() != null)
			record.setAmount(entry.getAmount());
		if (entry.getPaymentMethod() != null)
			record.setPaymentMethod(entry.getPaymentMethod());
		record.setRecordType(entry.getType());

		switch (entry.getType()) {
		case ClientTransaction.TYPE_WRITE_CHECK:
			record.setWriteCheck(entry.getTransactionId());
			record.setRecordType(ClientTransaction.TYPE_WRITE_CHECK);
			break;
		case ClientTransaction.TYPE_CASH_PURCHASE:
		case ClientTransaction.TYPE_CASH_EXPENSE:
		case ClientTransaction.TYPE_EMPLOYEE_EXPENSE:
			record.setCashPurchase(entry.getTransactionId());
			record.setRecordType(ClientTransaction.TYPE_CASH_PURCHASE);
			break;
		case ClientTransaction.TYPE_CUSTOMER_REFUNDS:
			record.setCustomerRefund(entry.getTransactionId());
			record.setRecordType(ClientTransaction.TYPE_CUSTOMER_REFUNDS);
			break;
		case ClientTransaction.TYPE_PAY_SALES_TAX:
			record.setPaySalesTax(entry.getTransactionId());
			record.setRecordType(ClientTransaction.TYPE_PAY_SALES_TAX);
			break;
		case ClientTransaction.TYPE_PAY_BILL:
			record.setPayBill(entry.getTransactionId());
			record.setRecordType(ClientTransaction.TYPE_PAY_BILL);
			break;
		case ClientTransaction.TYPE_CREDIT_CARD_CHARGE:
		case ClientTransaction.TYPE_CREDIT_CARD_EXPENSE:
			record.setCreditCardCharge(entry.getTransactionId());
			record.setRecordType(ClientTransaction.TYPE_CREDIT_CARD_CHARGE);
			break;
		case ClientTransaction.TYPE_RECEIVE_VAT:
			record.setReceiveVAT(entry.getTransactionId());
			record.setRecordType(ClientTransaction.TYPE_RECEIVE_VAT);
			break;
		case ClientTransaction.TYPE_PAY_VAT:
			record.setPayVAT(entry.getTransactionId());
			record.setRecordType(ClientTransaction.TYPE_PAY_VAT);
			break;
		case ClientTransaction.TYPE_CUSTOMER_PREPAYMENT:
			record.setCustomerPrepayment(entry.getTransactionId());
			record.setRecordType(ClientTransaction.TYPE_CUSTOMER_PREPAYMENT);
			break;

		}

		// record.setID(entry.getTransactionId());

	}

	private void getPayFromAccounts() {
		payFromAccounts = new ArrayList<ClientAccount>();
		payFromAccounts = accountCombo.getAccounts();
		accountCombo.initCombo(payFromAccounts);
		accountCombo.setAccountTypes(UIUtils
				.getOptionsByType(AccountCombo.PAY_FROM_COMBO));
		selectedPayFromAccount = accountCombo.getSelectedValue();
	}

	private void createControls() {
		setWidth("80px");
		mainPanel.setSpacing(3);

		payMethodSelect = new SelectCombo(Accounter.constants().paymentMethod());
		payMethodSelect.setHelpInformation(true);
		payMethodSelect.setRequired(true);
		payMethodItemList = new ArrayList<String>();
		payMethodItemList
				.add(UIUtils
						.getpaymentMethodCheckBy_CompanyType(AccounterClientConstants.PAYMENT_METHOD_CHECK));
		payMethodSelect.initCombo(payMethodItemList);
		payMethodSelect.setSelectedItem(0);

		payMethodSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						payMethodSelect.getSelectedValue();
						paymentMethodSelected(payMethodSelect
								.getSelectedValue());
					}

				});

		accountCombo = new PayFromAccountsCombo(Accounter.messages().account(
				Global.get().account()), false);
		accountCombo.setHelpInformation(true);
		accountCombo.setRequired(true);
		accountCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectedPayFromAccount = selectItem;
						changeGridData(selectedPayFromAccount);
						if (selectedPayFromAccount != null)
							setStartingCheckNumber(selectedPayFromAccount);

					}

				});

		payForm = new DynamicForm();
		payForm.setWidth("50%");
		payForm.setFields(payMethodSelect, accountCombo);

		Label label = new Label();
		label.setText(Accounter.constants().paymentsToBeIssued());
		initListGrid();

		mainVLay = new VerticalPanel();
		mainVLay.setWidth("800px");

		mainVLay.add(payForm);
		mainVLay.add(label);
		mainVLay.add(gridLayout);

		setBodyLayout(mainVLay);
		headerLayout.setWidth("800px");
		headerLayout.setHeight("15%");
		// footerLayout.setCellWidth(okbtn, "85%");
		footerLayout.removeStyleName("dialogfooter");

	}

	protected void setStartingCheckNumber(ClientAccount account) {

		if (checkNoText != null) {
			rpcUtilService.getNextIssuepaymentCheckNumber(account.getID(),
					new AccounterAsyncCallback<Long>() {

						public void onException(AccounterException caught) {
							// UIUtils.logError(
							// "Failed to get the Check number..", caught);
							// //UIUtils.logError(
							// "Failed to get the Check number..", caught);

						}

						public void onResultSuccess(Long result) {

							if (result == null) {
								onFailure(null);
								return;
							}
							// setCheckNo(result);
							// CheckNoText.setValue(getCheckNo());
							checkNoText.setValue("");
						}

					});

		}

	}

	protected ValidationResult validate() {
		ValidationResult result = FormItem.validate(payMethodSelect,
				accountCombo);
		if (grid.getRecords().isEmpty()) {
			result.addError(grid, Accounter.constants()
					.noTransactionIsAvailableToIssuePayments());
		} else {
			if (grid.getSelectedRecords().size() == 0)
				result.addError(grid, Accounter.constants()
						.pleaseSelectAnyOneOfTheTransactions());
		}
		// result.add(grid.validateGrid());
		return result;
	}

	private boolean validateCheckNo() {
		boolean valid = true;
		try {
			String number = checkNoText.getValue().toString();
			setCheckNo(number);
		} catch (NumberFormatException e) {
			valid = false;
			Accounter.showError(Accounter.constants().invalidChequeNumber());
		}

		return valid;
	}

	protected void createIssuePayment() {

		ClientIssuePayment issuePayment = getIssuePaymentObject();
		saveOrUpdate(issuePayment);
		// Accounter.showError(AccounterErrorType.FAILEDREQUEST);

		// public void onSuccess(String result) {
		// if (result == null) {
		// onFailure(null);
		// return;
		// }
		// Accounter.showInformation(FinanceApplication
		// .constants().issuePaymentWith()
		// + transactionNumber
		// + FinanceApplication.constants()
		// .createdSuccessfully());
		// IssuePaymentDialog.this.removeFromParent();
		// }
		//
		// });

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		// Accounter.showInformation(FinanceApplication.constants()
		// .issuePaymentWith()
		// + transactionNumber
		// + FinanceApplication.constants()
		// .createdSuccessfully());
		IssuePaymentDialog.this.removeFromParent();
		super.saveSuccess(object);
		// ActionFactory.getExpensesAction(null).run(null, true);
	}

	public void saveFailed(AccounterException exception) {
		// Accounter.showError(AccounterErrorType.FAILEDREQUEST);
		super.saveFailed(exception);
		AccounterException accounterException = (AccounterException) exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
	}

	private ClientIssuePayment getIssuePaymentObject() {

		ClientIssuePayment issuePayment = new ClientIssuePayment();

		issuePayment.setType(ClientTransaction.TYPE_ISSUE_PAYMENT);

		issuePayment.setNumber(transactionNumber);

		issuePayment.setDate(new ClientFinanceDate().getDate());

		issuePayment.setPaymentMethod(selectedpaymentMethod);

		issuePayment.setAccount(selectedPayFromAccount.getID());

		issuePayment.setTotal(totalAmount);
		String chkNo;
		if (checkNoText != null) {
			chkNo = (checkNoText.getValue().toString().isEmpty()) ? "0"
					: (checkNoText.getValue().toString());
			issuePayment.setCheckNumber(chkNo);
		}
		issuePayment
				.setTransactionIssuePayment(getTransactionIssuePayments(issuePayment));

		return issuePayment;
	}

	/*
	 * This method fills the grid with the records which has this account
	 * selected while creating them.
	 */
	protected void changeGridData(ClientAccount selectedPayFromAccount2) {
		if (selectedPayFromAccount2 != null) {
			grid.removeAllRecords();
			grid.addLoadingImagePanel();
			rpcUtilService
					.getChecks(
							selectedPayFromAccount2.getID(),
							new AccounterAsyncCallback<ArrayList<IssuePaymentTransactionsList>>() {

								public void onException(AccounterException t) {

									// UIUtils
									// .logError(
									// "Failed to get the IssuePaymentTransactionsList..",
									// t);
									grid.addEmptyMessage(Accounter.constants()
											.noRecordsToShow());

								}

								public void onResultSuccess(
										ArrayList<IssuePaymentTransactionsList> result) {

									if (result == null) {
										onFailure(null);
										return;
									}
									if (result.size() > 0) {
										grid.removeAllRecords();
										for (IssuePaymentTransactionsList entry : result) {
											addRecord(entry);
										}
									} else
										grid.addEmptyMessage(Accounter
												.constants().noRecordsToShow());

								}

							});
		}

	}

	protected void removeGridData() {
		grid.removeAllRecords();
		// totalAmount = 0D;
	}

	private void initListGrid() {
		gridLayout = new VerticalPanel();
		gridLayout.setWidth("100%");
		gridLayout.setHeight("100px");
		grid = new TransactionIssuePaymentGrid();
		grid.isEnable = false;
		grid.init();
		grid.setHeight("200px");
		grid.setIssuePaymentView(this);
		// grid.addFooterValues("", "", "", "", FinanceApplication
		// .constants().total(), DataUtils
		// .getAmountAsString(0.00));

		// bottomLabelLayOut = new HorizontalPanel();
		// bottomLabelLayOut.setWidth("100%");
		// bottomLabelLayOut.setHeight("100px");
		Label emptyLabel = new Label();
		emptyLabel.setWidth("25%");
		totalLabel = new Label();
		totalLabel.setWidth("30%");
		totalLabel.setText(Accounter.constants().totalAmount());

		amountLabel = new Label();
		amountLabel.setText("" + UIUtils.getCurrencySymbol() + "0");
		amountLabel.setWidth("20%");

		gridLayout.add(grid);
		// gridLayout.add(bottomLabelLayOut);

	}

	private void paymentMethodSelected(String selectedpaymentMethod1) {
		selectedpaymentMethod = selectedpaymentMethod1;
		if (!selectedpaymentMethod.isEmpty()) {
			checkNoText = new TextItem(Accounter.constants().startingChequeNo());
			checkNoText.setHelpInformation(true);
			checkNoText.setWidth(100);
			// checkNoText.setRequired(true);
			// if (selectedPayFromAccount != null)
			// setStartingCheckNumber(selectedPayFromAccount);
			payForm.removeAllRows();
			payForm.setFields(payMethodSelect, accountCombo, checkNoText);
			changeGridData(selectedPayFromAccount);
		} else {
			payForm.removeAllRows();
			payForm.setFields(payMethodSelect, accountCombo);
		}
	}

	private List<ClientTransactionIssuePayment> getTransactionIssuePayments(
			ClientIssuePayment issuePayment) {
		List<ClientTransactionIssuePayment> transactionIssuePaymentsList = new ArrayList<ClientTransactionIssuePayment>();

		ClientTransactionIssuePayment entry;

		for (ClientTransactionIssuePayment record : grid.getSelectedRecords()) {
			entry = new ClientTransactionIssuePayment();
			if (record.getDate() != 0)
				entry.setDate(record.getDate());
			if (record.getNumber() != null)
				entry.setNumber(record.getNumber());

			if (record.getName() != null)
				entry.setName(record.getName());

			entry.setAmount(record.getAmount());
			entry.setMemo(record.getMemo());

			if (record.getPaymentMethod() != null) {
				entry.setPaymentMethod(record.getPaymentMethod());
			}

			switch (record.getRecordType()) {
			case ClientTransaction.TYPE_WRITE_CHECK:
				entry.setWriteCheck(record.getWriteCheck());
				entry.setRecordType(ClientTransaction.TYPE_WRITE_CHECK);
				break;
			case ClientTransaction.TYPE_CASH_PURCHASE:
			case ClientTransaction.TYPE_CASH_EXPENSE:
			case ClientTransaction.TYPE_EMPLOYEE_EXPENSE:
				entry.setCashPurchase(record.getCashPurchase());
				entry.setRecordType(ClientTransaction.TYPE_CASH_PURCHASE);
				break;
			case ClientTransaction.TYPE_CUSTOMER_REFUNDS:
				entry.setCustomerRefund(record.getCustomerRefund());
				entry.setRecordType(ClientTransaction.TYPE_CUSTOMER_REFUNDS);
				break;
			case ClientTransaction.TYPE_PAY_SALES_TAX:
				entry.setPaySalesTax(record.getPaySalesTax());
				entry.setRecordType(ClientTransaction.TYPE_PAY_SALES_TAX);
				break;
			case ClientTransaction.TYPE_PAY_BILL:
				entry.setPayBill(record.getPayBill());
				entry.setRecordType(ClientTransaction.TYPE_PAY_BILL);
				break;
			case ClientTransaction.TYPE_CREDIT_CARD_CHARGE:
			case ClientTransaction.TYPE_CREDIT_CARD_EXPENSE:
				entry.setCreditCardCharge(record.getCreditCardCharge());
				entry.setRecordType(ClientTransaction.TYPE_CREDIT_CARD_CHARGE);
				break;
			case ClientTransaction.TYPE_RECEIVE_VAT:
				entry.setReceiveVAT(record.getReceiveVAT());
				entry.setRecordType(ClientTransaction.TYPE_RECEIVE_VAT);
				break;
			case ClientTransaction.TYPE_PAY_VAT:
				entry.setPayVAT(record.getPayVAT());
				entry.setRecordType(ClientTransaction.TYPE_PAY_VAT);
				break;
			case ClientTransaction.TYPE_CUSTOMER_PREPAYMENT:
				entry.setCustomerPrepayment(record.getCustomerPrepayment());
				entry.setRecordType(ClientTransaction.TYPE_CUSTOMER_PREPAYMENT);
				break;

			}

			entry.setTransaction(issuePayment);

			transactionIssuePaymentsList.add(entry);

		}
		return transactionIssuePaymentsList;

	}

	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}

	public String getCheckNo() {
		return checkNo;
	}

	@Override
	public Object getGridColumnValue(ClientIssuePayment obj, int index) {
		// NOTHING TO DO.
		return null;
	}

	@Override
	protected boolean onOK() {
		createIssuePayment();
		return true;
	}

	@Override
	public void setFocus() {
		payMethodSelect.setFocus();

	}

}
