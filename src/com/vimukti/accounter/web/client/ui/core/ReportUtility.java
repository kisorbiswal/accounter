package com.vimukti.accounter.web.client.ui.core;

import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientTransaction;

public class ReportUtility {

	public static int companyType;

	public static String getTransactionName(int transactionType) {

		String transactionName = null;
		switch (transactionType) {
		case ClientTransaction.MEMO_OPENING_BALANCE:
			transactionName = "Opening Balance";
			break;
		case ClientTransaction.TYPE_CASH_SALES:
			transactionName = "Cash Sale";
			break;
		case ClientTransaction.TYPE_CASH_PURCHASE:
			transactionName = "Cash Purchase";
			break;
		case ClientTransaction.TYPE_CREDIT_CARD_CHARGE:
			transactionName = "Credit Card Charge";
			break;
		case ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO:
			transactionName = "Customer Credit";
			break;
		case ClientTransaction.TYPE_CUSTOMER_REFUNDS:
			transactionName = "Customer Refund";
			break;
		case ClientTransaction.TYPE_ENTER_BILL:
			transactionName = getVendorString("Supplier Bill", "Vendor Bill");
			break;
		case ClientTransaction.TYPE_ESTIMATE:
			transactionName = "Quote";
			break;
		case ClientTransaction.TYPE_INVOICE:
			transactionName = "Invoice";
			break;
		case ClientTransaction.TYPE_ISSUE_PAYMENT:
			transactionName = "Issue Payment";
			break;
		case ClientTransaction.TYPE_MAKE_DEPOSIT:
			transactionName = "Deposit/Transfer Funds";
			break;
		case ClientTransaction.TYPE_PAY_BILL:
			transactionName = getVendorString("Supplier Payment",
					"Vendor Payment");
			break;
		case ClientTransaction.TYPE_VENDOR_PAYMENT:
			transactionName = getVendorString("Supplier Prepayment",
					"Vendor Prepayment");
			break;
		case ClientTransaction.TYPE_RECEIVE_PAYMENT:
			transactionName = "Customer Payment";
			break;
		case ClientTransaction.TYPE_TRANSFER_FUND:
			transactionName = "Transfer Fund";
			break;
		case ClientTransaction.TYPE_VENDOR_CREDIT_MEMO:
			transactionName = getVendorString("Supplier Credit",
					"Vendor Credit");
			break;
		case ClientTransaction.TYPE_WRITE_CHECK:
			transactionName = "Check";
			break;
		case ClientTransaction.TYPE_JOURNAL_ENTRY:
			transactionName = "Journal Entry";
			break;
		case ClientTransaction.TYPE_PAY_SALES_TAX:
			transactionName = "Pay Sales Tax";
			break;
		case ClientTransaction.TYPE_RECEIVE_VAT:
			transactionName = "Receive VAT ";
			break;
		case ClientTransaction.TYPE_SALES_ORDER:
			transactionName = "Sales Order";
			break;
		case ClientTransaction.TYPE_PURCHASE_ORDER:
			transactionName = "Purchase Order";
			break;
		case ClientTransaction.TYPE_ITEM_RECEIPT:
			transactionName = "Item Receipt";
			break;
		case ClientTransaction.TYPE_CASH_EXPENSE:
			transactionName = "Cash Expense";
			break;
		case ClientTransaction.TYPE_EMPLOYEE_EXPENSE:
			transactionName = "Employee Expense";
			break;
		case ClientTransaction.TYPE_CREDIT_CARD_EXPENSE:
			transactionName = "Credit Card Expense";
			break;
		case ClientTransaction.TYPE_VAT_RETURN:
			transactionName = "VAT Return ";
			break;
		case ClientTransaction.TYPE_PAY_VAT:
			transactionName = "Pay VAT";
			break;
		case ClientTransaction.TYPE_CUSTOMER_PREPAYMENT:
			transactionName = "Customer PrePayment";
		}
		return transactionName;
	}

	public static String getVendorString(String forUk, String forUs) {
		return companyType == ClientCompany.ACCOUNTING_TYPE_UK ? forUk : forUs;
	}
}
