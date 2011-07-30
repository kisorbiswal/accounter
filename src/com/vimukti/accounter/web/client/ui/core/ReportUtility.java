package com.vimukti.accounter.web.client.ui.core;

import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;

public class ReportUtility {

	public static int companyType;

	public static String getTransactionName(int transactionType) {
		AccounterConstants constants = Accounter.constants();
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
			transactionName = constants.customerCredit();
			break;
		case ClientTransaction.TYPE_CUSTOMER_REFUNDS:
			transactionName = constants.customerRefund();
			break;
		case ClientTransaction.TYPE_ENTER_BILL:
			transactionName = getVendorString(constants.supplierBill(),
					constants.vendorBill());
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
			transactionName = getVendorString(constants.supplierPayment(),
					constants.vendorPayment());
			break;
		case ClientTransaction.TYPE_VENDOR_PAYMENT:
			transactionName = getVendorString(constants.supplierPrepayment(),
					constants.vendorPrepayment());
			break;
		case ClientTransaction.TYPE_RECEIVE_PAYMENT:
			transactionName = "Customer Payment";
			break;
		case ClientTransaction.TYPE_TRANSFER_FUND:
			transactionName = constants.transferFund();
			break;
		case ClientTransaction.TYPE_VENDOR_CREDIT_MEMO:
			transactionName = getVendorString(constants.supplierCredit(),
					constants.vendorCredit());
			break;
		case ClientTransaction.TYPE_WRITE_CHECK:
			transactionName = constants.check();
			break;
		case ClientTransaction.TYPE_JOURNAL_ENTRY:
			transactionName = constants.journalEntry();
			break;
		case ClientTransaction.TYPE_PAY_SALES_TAX:
			transactionName = constants.paySalesTax();
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
			transactionName = constants.VATReturn();
			break;
		case ClientTransaction.TYPE_PAY_VAT:
			transactionName = constants.payVAT();
			break;
		case ClientTransaction.TYPE_CUSTOMER_PREPAYMENT:
			transactionName = constants.customerPrePayment();
		}
		return transactionName;
	}

	public static String getVendorString(String forUk, String forUs) {
		return companyType == ClientCompany.ACCOUNTING_TYPE_UK ? forUk : forUs;
	}
}
