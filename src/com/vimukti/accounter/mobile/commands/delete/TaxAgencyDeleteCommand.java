package com.vimukti.accounter.mobile.commands.delete;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class TaxAgencyDeleteCommand extends AbstractDeleteCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		long taxagencyId = Long.parseLong(context.getString());
		try {
			delete(AccounterCoreType.TAXAGENCY, taxagencyId, context);
		} catch (AccounterException e) {
			addFirstMessage(context,
					"You can not delete. This Tax Agency Might be used in some transactions.");
		}
		return "TaxAgencies";
	}

}
