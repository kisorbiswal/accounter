package com.vimukti.accounter.mobile.commands.delete;

import com.vimukti.accounter.core.Invoice;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class TransactionDeleteCommand extends AbstractDeleteCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {

		long transactionId = Long.parseLong(context.getString());
		Invoice serverObjectById = (Invoice) CommandUtils.getServerObjectById(
				transactionId, AccounterCoreType.INVOICE);
		serverObjectById.setVoid(true);
		serverObjectById.setStatus(ClientTransaction.STATUS_DELETED);

		try {
			voidTransaction(AccounterCoreType.INVOICE, transactionId, context);
		} catch (AccounterException e) {
			addFirstMessage(context, "Failed");
		}
		return "Invoices List";
	}
}
