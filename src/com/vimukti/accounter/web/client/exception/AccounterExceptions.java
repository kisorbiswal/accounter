package com.vimukti.accounter.web.client.exception;

import com.google.gwt.core.client.GWT;
import com.vimukti.accounter.web.client.externalization.AccounterErrorMessages;
import com.vimukti.accounter.web.client.externalization.AccounterErrors;

public class AccounterExceptions {

	public static AccounterErrors accounterErrors = (AccounterErrors) GWT
			.create(AccounterErrors.class);
	public static AccounterErrorMessages accounterErrorMessages = (AccounterErrorMessages) GWT
			.create(AccounterErrorMessages.class);

	public static String getErrorString(int errorCode) {
		switch (errorCode) {
		case AccounterException.ERROR_NUMBER_CONFLICT:
			return accounterErrors.numberConflict();

		case AccounterException.ERROR_NAME_CONFLICT:
			return accounterErrors.nameConflict();

		case AccounterException.ERROR_TRAN_CONFLICT:
			return accounterErrors.transactionConflict();

		case AccounterException.ERROR_PERMISSION_DENIED:
			return accounterErrors.permissionDenied();

		case AccounterException.ERROR_INTERNAL:
			return accounterErrors.internal();

		case AccounterException.ERROR_ILLEGAL_ARGUMENT:
			return accounterErrors.illegalArgument();

		case AccounterException.ERROR_NO_SUCH_OBJECT:
			return accounterErrors.noSuchObject();

		case AccounterException.ERROR_DEPOSITED_FROM_UNDEPOSITED_FUNDS:
			return accounterErrors.depositedFromUndepositedFunds();

		case AccounterException.ERROR_CANT_EDIT:
			return accounterErrors.cantEdit();

		case AccounterException.ERROR_CANT_VOID:
			return accounterErrors.cantVoid();

		case AccounterException.ERROR_RECEIVE_PAYMENT_DISCOUNT_USED:
			return accounterErrors.receivePaymentDiscountUsed();

		case AccounterException.ERROR_OBJECT_IN_USE:
			return accounterErrors.objectInUse();

		case AccounterException.ERROR_VERSION_MISMATCH:
			return accounterErrors.objectModified();

		default:
			return null;
		}

	}

}
