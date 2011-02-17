package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.user.client.rpc.AsyncCallback;
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
import com.vimukti.accounter.web.client.core.ClientPaySalesTax;
import com.vimukti.accounter.web.client.core.ClientPayVAT;
import com.vimukti.accounter.web.client.core.ClientPurchaseOrder;
import com.vimukti.accounter.web.client.core.ClientReceivePayment;
import com.vimukti.accounter.web.client.core.ClientReceiveVAT;
import com.vimukti.accounter.web.client.core.ClientSalesOrder;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransferFund;
import com.vimukti.accounter.web.client.core.ClientVendorCreditMemo;
import com.vimukti.accounter.web.client.core.ClientWriteCheck;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BankingActionFactory;
import com.vimukti.accounter.web.client.ui.core.CompanyActionFactory;
import com.vimukti.accounter.web.client.ui.core.CustomersActionFactory;
import com.vimukti.accounter.web.client.ui.core.VendorsActionFactory;
import com.vimukti.accounter.web.client.ui.vat.VatActionFactory;

public class ReportsRPC {

	protected static <T extends IAccounterCore, A extends Action> void initCallBack(
			T c, final A a, final String stringID) {
		AsyncCallback<T> callback = new AsyncCallback<T>() {

			public void onFailure(Throwable caught) {
			}

			public void onSuccess(T result) {
				if (result != null) {
					UIUtils.runAction(result, a);
				}
			}

		};
		FinanceApplication.createGETService().getObjectById(c.getObjectType(),
				stringID, callback);
	}

	public static void getTaxAgency(String name) {
		UIUtils.runAction(FinanceApplication.getCompany().getTaxAgency(name),
				CompanyActionFactory.getNewTAXAgencyAction());

	}

	public static void getVendor(String name) {
		UIUtils.runAction(
				FinanceApplication.getCompany().getVendorByName(name),
				VendorsActionFactory.getNewVendorAction());
	}

	public static void openTransactionView(int transactionType,
			String transactionId) {
		switch (transactionType) {

		case ClientTransaction.TYPE_PAY_BILL:
			initCallBack(new ClientPayBill(), VendorsActionFactory
					.getPayBillsAction(), transactionId);
			break;
		case ClientTransaction.TYPE_PAY_VAT:
			initCallBack(new ClientPayVAT(),
					VatActionFactory.getpayVATAction(), transactionId);
			break;
		case ClientTransaction.TYPE_VENDOR_PAYMENT:
			initCallBack(new ClientPayBill(), VendorsActionFactory
					.getNewVendorPaymentAction(), transactionId);
			break;
		case ClientTransaction.TYPE_MAKE_DEPOSIT:
			initCallBack(new ClientMakeDeposit(), BankingActionFactory
					.getMakeDepositAction(), transactionId);
			break;
		case ClientTransaction.TYPE_ENTER_BILL:
			initCallBack(new ClientEnterBill(), VendorsActionFactory
					.getEnterBillsAction(), transactionId);
			break;
		case ClientTransaction.TYPE_CASH_PURCHASE:
			initCallBack(new ClientCashPurchase(), VendorsActionFactory
					.getNewCashPurchaseAction(), transactionId);
			break;
		case ClientTransaction.TYPE_CASH_SALES:
			initCallBack(new ClientCashSales(), CustomersActionFactory
					.getNewCashSaleAction(), transactionId);
			break;
		case ClientTransaction.TYPE_WRITE_CHECK:
			initCallBack(new ClientWriteCheck(), BankingActionFactory
					.getWriteChecksAction(), transactionId);
			break;
		case ClientTransaction.TYPE_CUSTOMER_REFUNDS:
			initCallBack(new ClientCustomerRefund(), CustomersActionFactory
					.getCustomerRefundAction(), transactionId);
			break;
		case ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO:
			initCallBack(new ClientCustomerCreditMemo(), CustomersActionFactory
					.getNewCreditsAndRefundsAction(), transactionId);
			break;
		case ClientTransaction.TYPE_RECEIVE_PAYMENT:
			initCallBack(new ClientReceivePayment(), CustomersActionFactory
					.getReceivePaymentAction(), transactionId);
			break;
		case ClientTransaction.TYPE_INVOICE:
			initCallBack(new ClientInvoice(), CustomersActionFactory
					.getNewInvoiceAction(), transactionId);
			break;
		case ClientTransaction.TYPE_CREDIT_CARD_CHARGE:
			initCallBack(new ClientCreditCardCharge(), BankingActionFactory
					.getCreditCardChargeAction(), transactionId);
			break;
		case ClientTransaction.TYPE_ESTIMATE:
			initCallBack(new ClientEstimate(), CustomersActionFactory
					.getNewQuoteAction(), transactionId);
			break;
		case ClientTransaction.TYPE_ISSUE_PAYMENT:
			initCallBack(new ClientIssuePayment(), VendorsActionFactory
					.getIssuePaymentsAction(), transactionId);
			break;
		case ClientTransaction.TYPE_TRANSFER_FUND:
			initCallBack(new ClientTransferFund(), BankingActionFactory
					.getTransferFundsAction(), transactionId);

			break;
		case ClientTransaction.TYPE_VENDOR_CREDIT_MEMO:
			initCallBack(new ClientVendorCreditMemo(), VendorsActionFactory
					.getNewCreditMemoAction(), transactionId);
			break;
		case ClientTransaction.TYPE_PAY_SALES_TAX:
			initCallBack(new ClientPaySalesTax(), CompanyActionFactory
					.getPaySalesTaxAction(), transactionId);
			break;
		case ClientTransaction.TYPE_JOURNAL_ENTRY:
			initCallBack(new ClientJournalEntry(), CompanyActionFactory
					.getNewJournalEntryAction(), transactionId);
			break;

		case ClientTransaction.TYPE_SALES_ORDER:
			initCallBack(new ClientSalesOrder(), CustomersActionFactory
					.getSalesOrderAction(), transactionId);
			break;

		case ClientTransaction.TYPE_PURCHASE_ORDER:
			initCallBack(new ClientPurchaseOrder(), VendorsActionFactory
					.getPurchaseOrderAction(), transactionId);
			break;

		case ClientTransaction.TYPE_ITEM_RECEIPT:
			initCallBack(new ClientItemReceipt(), VendorsActionFactory
					.getItemReceiptAction(), transactionId);
			break;

		case ClientTransaction.TYPE_CASH_EXPENSE:
			initCallBack(new ClientCashPurchase(), VendorsActionFactory
					.CashExpenseAction(), transactionId);
			break;

		case ClientTransaction.TYPE_CREDIT_CARD_EXPENSE:
			initCallBack(new ClientCreditCardCharge(), VendorsActionFactory
					.CreditCardExpenseAction(), transactionId);
			break;

		case ClientTransaction.TYPE_EMPLOYEE_EXPENSE:
			initCallBack(new ClientCashPurchase(), VendorsActionFactory
					.EmployeeExpenseAction(), transactionId);
			break;

		case ClientTransaction.TYPE_CUSTOMER_PREPAYMENT:
			initCallBack(new ClientCustomerPrePayment(), CustomersActionFactory
					.getNewCustomerPaymentAction(), transactionId);
			break;
		case ClientTransaction.TYPE_RECEIVE_VAT:
			initCallBack(new ClientReceiveVAT(), VatActionFactory
					.getreceiveVATAction(), transactionId);
			break;
		}

	}
}
