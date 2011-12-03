package com.vimukti.accounter.web.client.ui.core;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

public class AccounterWarningType {
	public static final int default_IncomeAccount = 101;
	public static final int default_ExpenseAccount = 102;
	public static final int default_IncomeAccountNonInventory = 103;
	public static final int default_ExpenseAccountNonInventory = 104;
	public static final int EMPTY_CLASS = 105;
	public static final int saveOrClose = 106;
	public static final int recievePayment = 107;
	public static final int transferFromAccount = 108;
	public static final int INVALID_CUSTOMERREFUND_AMOUNT = 109;
	public static final int RECORDSEMPTY = 110;
	public static final int NOT_YET_IMPLEMENTED = 111;
	public static final int RECEIVEPAYMENT_EDITING = 112;
	public static final int CUSTOMER_EDITING = 113;
	public static final int VENDOR_EDITING = 114;
	public static final int PAYBILL_EDITING = 115;
	public static final int TAXREFUND_EDITING = 116;
	public static final int WAREHOUSE_TRANSFER_EDITING = 117;

	public static String getWarning(int warningType) {
		AccounterMessages messages = Global.get().messages();
		switch (warningType) {
		case default_IncomeAccount:
			return messages.W_101();
		case default_ExpenseAccount:
			return messages.W_102();
		case default_IncomeAccountNonInventory:
			return messages.W_103();
		case default_ExpenseAccountNonInventory:
			return messages.W_104();
		case EMPTY_CLASS:
			return messages.W_105();
		case saveOrClose:
			return messages.W_106();
		case recievePayment:
			return messages.W_107();
		case transferFromAccount:
			return messages.W_108();
		case INVALID_CUSTOMERREFUND_AMOUNT:
			return messages.W_109();
		case RECORDSEMPTY:
			return messages.W_110();
		case NOT_YET_IMPLEMENTED:
			return messages.W_111();
		case RECEIVEPAYMENT_EDITING:
			return messages.W_112();
		case CUSTOMER_EDITING:
			return messages.W_113();
		case VENDOR_EDITING:
			return messages.W_114();
		case PAYBILL_EDITING:
			return messages.W_115();
		case TAXREFUND_EDITING:
			return messages.W_116();
		case WAREHOUSE_TRANSFER_EDITING:
			return messages.W_117();
		default:
			break;
		}
		return null;

	}
}
