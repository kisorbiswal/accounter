package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientCashSales;
import com.vimukti.accounter.web.client.core.ClientCreditCardCharge;
import com.vimukti.accounter.web.client.core.ClientCustomerCreditMemo;
import com.vimukti.accounter.web.client.core.ClientCustomerPrePayment;
import com.vimukti.accounter.web.client.core.ClientCustomerRefund;
import com.vimukti.accounter.web.client.core.ClientEnterBill;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientInvoice;
import com.vimukti.accounter.web.client.core.ClientIssuePayment;
import com.vimukti.accounter.web.client.core.ClientItemReceipt;
import com.vimukti.accounter.web.client.core.ClientJournalEntry;
import com.vimukti.accounter.web.client.core.ClientMakeDeposit;
import com.vimukti.accounter.web.client.core.ClientPayBill;
import com.vimukti.accounter.web.client.core.ClientPayTAX;
import com.vimukti.accounter.web.client.core.ClientPurchaseOrder;
import com.vimukti.accounter.web.client.core.ClientReceivePayment;
import com.vimukti.accounter.web.client.core.ClientReceiveVAT;
import com.vimukti.accounter.web.client.core.ClientSalesOrder;
import com.vimukti.accounter.web.client.core.ClientTAXAdjustment;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransferFund;
import com.vimukti.accounter.web.client.core.ClientVendorCreditMemo;
import com.vimukti.accounter.web.client.core.ClientWriteCheck;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;

public class ReportsRPC {

	protected static <T extends IAccounterCore, A extends Action> void initCallBack(
			T c, final A a, final long id) {
		AccounterAsyncCallback<T> callback = new AccounterAsyncCallback<T>() {

			public void onException(AccounterException caught) {
				Accounter.showMessage(Accounter.constants().sessionExpired());
			}

			public void onResultSuccess(T result) {
				if (result != null) {
					UIUtils.runAction(result, a);
				}
			}

		};
		Accounter.createGETService().getObjectById(c.getObjectType(), id,
				callback);
	}

	public static void getTaxAgency(long id) {
		UIUtils.runAction(Accounter.getCompany().getTaxAgency(id),
				ActionFactory.getNewTAXAgencyAction());

	}

	public static void getVendor(String name) {
		UIUtils.runAction(Accounter.getCompany().getVendorByName(name),
				ActionFactory.getNewVendorAction());
	}

	public static void openTransactionView(int transactionType,
			long transactionId) {
		switch (transactionType) {

		case ClientTransaction.TYPE_PAY_BILL:
			initCallBack(new ClientPayBill(),
					ActionFactory.getPayBillsAction(), transactionId);
			break;
		case ClientTransaction.TYPE_VENDOR_PAYMENT:
			initCallBack(new ClientPayBill(),
					ActionFactory.getNewVendorPaymentAction(), transactionId);
			break;
		case ClientTransaction.TYPE_MAKE_DEPOSIT:
			initCallBack(new ClientMakeDeposit(),
					ActionFactory.getMakeDepositAction(), transactionId);
			break;
		case ClientTransaction.TYPE_ENTER_BILL:
			initCallBack(new ClientEnterBill(),
					ActionFactory.getEnterBillsAction(), transactionId);
			break;
		case ClientTransaction.TYPE_CASH_PURCHASE:
			initCallBack(new ClientCashPurchase(),
					ActionFactory.getNewCashPurchaseAction(), transactionId);
			break;
		case ClientTransaction.TYPE_CASH_SALES:
			initCallBack(new ClientCashSales(),
					ActionFactory.getNewCashSaleAction(), transactionId);
			break;
		case ClientTransaction.TYPE_WRITE_CHECK:
			initCallBack(new ClientWriteCheck(),
					ActionFactory.getWriteChecksAction(), transactionId);
			break;
		case ClientTransaction.TYPE_CUSTOMER_REFUNDS:
			initCallBack(new ClientCustomerRefund(),
					ActionFactory.getCustomerRefundAction(), transactionId);
			break;
		case ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO:
			initCallBack(new ClientCustomerCreditMemo(),
					ActionFactory.getNewCreditsAndRefundsAction(),
					transactionId);
			break;
		case ClientTransaction.TYPE_RECEIVE_PAYMENT:
			initCallBack(new ClientReceivePayment(),
					ActionFactory.getReceivePaymentAction(), transactionId);
			break;
		case ClientTransaction.TYPE_INVOICE:
			initCallBack(new ClientInvoice(),
					ActionFactory.getNewInvoiceAction(), transactionId);
			break;
		case ClientTransaction.TYPE_CREDIT_CARD_CHARGE:
			initCallBack(new ClientCreditCardCharge(),
					ActionFactory.getCreditCardChargeAction(), transactionId);
			break;
		case ClientTransaction.TYPE_ESTIMATE:
			initCallBack(new ClientEstimate(), ActionFactory.getNewQuoteAction(
					ClientEstimate.QUOTES, Accounter.constants().newQuote()),
					transactionId);
			break;
		case ClientTransaction.TYPE_ISSUE_PAYMENT:
			initCallBack(new ClientIssuePayment(),
					ActionFactory.getIssuePaymentsAction(), transactionId);
			break;
		case ClientTransaction.TYPE_TRANSFER_FUND:
			initCallBack(new ClientTransferFund(),
					ActionFactory.getTransferFundsAction(), transactionId);

			break;
		case ClientTransaction.TYPE_VENDOR_CREDIT_MEMO:
			initCallBack(new ClientVendorCreditMemo(),
					ActionFactory.getNewCreditMemoAction(), transactionId);
			break;
		case ClientTransaction.TYPE_PAY_TAX:
			initCallBack(new ClientPayTAX(), ActionFactory.getpayTAXAction(),
					transactionId);
			break;
		case ClientTransaction.TYPE_JOURNAL_ENTRY:
			initCallBack(new ClientJournalEntry(),
					ActionFactory.getNewJournalEntryAction(), transactionId);
			break;

		case ClientTransaction.TYPE_SALES_ORDER:
			initCallBack(new ClientSalesOrder(),
					ActionFactory.getSalesOrderAction(), transactionId);
			break;

		case ClientTransaction.TYPE_PURCHASE_ORDER:
			initCallBack(new ClientPurchaseOrder(),
					ActionFactory.getPurchaseOrderAction(), transactionId);
			break;

		case ClientTransaction.TYPE_ITEM_RECEIPT:
			initCallBack(new ClientItemReceipt(),
					ActionFactory.getItemReceiptAction(), transactionId);
			break;

		case ClientTransaction.TYPE_CASH_EXPENSE:
			initCallBack(new ClientCashPurchase(),
					ActionFactory.CashExpenseAction(), transactionId);
			break;

		case ClientTransaction.TYPE_CREDIT_CARD_EXPENSE:
			initCallBack(new ClientCreditCardCharge(),
					ActionFactory.CreditCardExpenseAction(), transactionId);
			break;

		case ClientTransaction.TYPE_EMPLOYEE_EXPENSE:
			initCallBack(new ClientCashPurchase(),
					ActionFactory.EmployeeExpenseAction(), transactionId);
			break;

		case ClientTransaction.TYPE_CUSTOMER_PREPAYMENT:
			initCallBack(new ClientCustomerPrePayment(),
					ActionFactory.getNewCustomerPaymentAction(), transactionId);
			break;
		case ClientTransaction.TYPE_RECEIVE_TAX:
			initCallBack(new ClientReceiveVAT(),
					ActionFactory.getreceiveVATAction(), transactionId);
			break;
		case ClientTransaction.TYPE_ADJUST_SALES_TAX:
		case ClientTransaction.TYPE_ADJUST_VAT_RETURN:
			initCallBack(new ClientTAXAdjustment(),
					ActionFactory.getAdjustTaxAction(), transactionId);
			break;
		}

	}
}
