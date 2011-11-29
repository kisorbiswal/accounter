package com.vimukti.accounter.web.client.exception;

import com.google.gwt.core.client.GWT;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

public class AccounterExceptions {

	public static AccounterMessages accounterMessages = (AccounterMessages) GWT
			.create(AccounterMessages.class);

	public static String getErrorString(int errorCode) {
		switch (errorCode) {
		case AccounterException.ERROR_NUMBER_CONFLICT:
			return accounterMessages.numberConflict();

		case AccounterException.ERROR_NAME_CONFLICT:
			return accounterMessages.nameConflict();

		case AccounterException.ERROR_TRAN_CONFLICT:
			return accounterMessages.transactionConflict();

		case AccounterException.ERROR_PERMISSION_DENIED:
			return accounterMessages.permissionDenied();

		case AccounterException.ERROR_INTERNAL:
			return accounterMessages.internal();

		case AccounterException.ERROR_ILLEGAL_ARGUMENT:
			return accounterMessages.illegalArgument();

		case AccounterException.ERROR_NO_SUCH_OBJECT:
			return accounterMessages.noSuchObject();

		case AccounterException.ERROR_DEPOSITED_FROM_UNDEPOSITED_FUNDS:
			return accounterMessages.depositedFromUndepositedFunds();

		case AccounterException.ERROR_CANT_EDIT:
			return accounterMessages.cantEdit();

		case AccounterException.ERROR_CANT_VOID:
			return accounterMessages.cantVoid();

		case AccounterException.ERROR_RECEIVE_PAYMENT_DISCOUNT_USED:
			return accounterMessages.receivePaymentDiscountUsed();

		case AccounterException.ERROR_OBJECT_IN_USE:
			return accounterMessages.objectInUse();

		case AccounterException.ERROR_VERSION_MISMATCH:
			return accounterMessages.objectModified();
		case AccounterException.ERROR_TRANSACTION_RECONCILIED:
			return accounterMessages.transactionReconcilied();
		case AccounterException.USED_IN_INVOICE:
			return accounterMessages.usedinInvoiceSoYoucantEdit();
		case AccounterException.INVOICE_PAID_VOID_IT:
			return accounterMessages.usedinReceivepayYoucantEdit();

		default:
			return null;
		}

	}

}
