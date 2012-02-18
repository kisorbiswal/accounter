package com.vimukti.accounter.mobile.commands.delete;

import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class TransactionDeleteCommand extends AbstractDeleteCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {

		String string = context.getString();
		String[] split = string.split(" ");
		AccounterCoreType accounterCoreType = CommandUtils
				.getAccounterCoreType(Integer.parseInt(split[0]));
		String transactionName = Utility.getTransactionName(Integer
				.parseInt(split[0]));
		try {
			delete(accounterCoreType, Long.parseLong(split[1]), context);
			addFirstMessage(context, "The transaction has been deleted.");
		} catch (AccounterException e) {
			addFirstMessage(context, e.getMessage());
		}
		return transactionName.replace(" ", "").toLowerCase() + "List";
	}
}
