package com.vimukti.accounter.web.client.ui.core;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;

public class ReportUtility {

	public static int companyType;

	public static String getTransactionName(int transactionType) {
		AccounterConstants constants = Global.get().constants();
		String transactionName = null;
		switch (transactionType) {
		case ClientTransaction.MEMO_OPENING_BALANCE:
			transactionName = constants.openingBalance();
			break;
		case ClientTransaction.TYPE_CASH_SALES:
			transactionName = constants.cashSale();
			break;
		case ClientTransaction.TYPE_CASH_PURCHASE:
			transactionName = constants.cashPurchase();
			break;
		case ClientTransaction.TYPE_CREDIT_CARD_CHARGE:
			transactionName = constants.creditCardCharge();
			break;
		case ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO:
			transactionName = Global.get().messages()
					.customerCredit(Global.get().Customer());
			break;
		case ClientTransaction.TYPE_CUSTOMER_REFUNDS:
			transactionName = Global.get().messages()
					.customerRefund(Global.get().Customer());
			break;
		case ClientTransaction.TYPE_ENTER_BILL:
			transactionName = getVendorString(Global.get().messages()
					.vendorBill(Global.get().Vendor()), Global.get()
					.messages().vendorBill(Global.get().Vendor()));
			break;
		case ClientTransaction.TYPE_ESTIMATE:
			transactionName = constants.quote();
			break;
		case ClientTransaction.TYPE_INVOICE:
			transactionName = constants.invoice();
			break;
		case ClientTransaction.TYPE_ISSUE_PAYMENT:
			transactionName = constants.issuePayment();
			break;
		case ClientTransaction.TYPE_MAKE_DEPOSIT:
			transactionName = constants.depositTransferFunds();
			break;
		case ClientTransaction.TYPE_PAY_BILL:
			transactionName = getVendorString(Global.get().messages()
					.vendorPayment(Global.get().Vendor()), Global.get()
					.messages().vendorPayment(Global.get().Vendor()));
			break;
		case ClientTransaction.TYPE_VENDOR_PAYMENT:
			transactionName = getVendorString(Global.get().messages()
					.vendorPrePayment(Global.get().Vendor()), Global.get()
					.messages().vendorPrePayment(Global.get().Vendor()));
			break;
		case ClientTransaction.TYPE_RECEIVE_PAYMENT:
			transactionName = Global.get().messages()
					.customerPayment(Global.get().Customer());
			break;
		case ClientTransaction.TYPE_TRANSFER_FUND:
			transactionName = constants.transferFund();
			break;
		case ClientTransaction.TYPE_VENDOR_CREDIT_MEMO:
			transactionName = getVendorString(Global.get().messages()
					.vendorCredit(Global.get().Vendor()), Global.get()
					.messages().vendorCredit(Global.get().Vendor()));
			break;
		case ClientTransaction.TYPE_WRITE_CHECK:
			transactionName = constants.check();
			break;
		case ClientTransaction.TYPE_JOURNAL_ENTRY:
			transactionName = constants.journalEntry();
			break;
		case ClientTransaction.TYPE_PAY_SALES_TAX:
			transactionName = constants.payTax();
			break;
		case ClientTransaction.TYPE_RECEIVE_VAT:
			transactionName = constants.receiveVAT();
			break;
		case ClientTransaction.TYPE_SALES_ORDER:
			transactionName = constants.salesOrder();
			break;
		case ClientTransaction.TYPE_PURCHASE_ORDER:
			transactionName = constants.purchaseOrder();
			break;
		case ClientTransaction.TYPE_ITEM_RECEIPT:
			transactionName = constants.itemReceipt();
			break;
		case ClientTransaction.TYPE_CASH_EXPENSE:
			transactionName = constants.cashExpense();
			break;
		case ClientTransaction.TYPE_EMPLOYEE_EXPENSE:
			transactionName = constants.employeeExpense();
			break;
		case ClientTransaction.TYPE_CREDIT_CARD_EXPENSE:
			transactionName = constants.creditCardExpense();
			break;
		case ClientTransaction.TYPE_VAT_RETURN:
			transactionName = constants.vatReturn();
			break;
		case ClientTransaction.TYPE_PAY_VAT:
			transactionName = constants.payVAT();
			break;
		case ClientTransaction.TYPE_CUSTOMER_PREPAYMENT:
			transactionName = Global.get().messages()
					.customerPrePayment(Global.get().Customer());
		}
		return transactionName;
	}

	public static String getVendorString(String forUk, String forUs) {
		return companyType == ClientCompany.ACCOUNTING_TYPE_UK ? forUk : forUs;
	}
}
