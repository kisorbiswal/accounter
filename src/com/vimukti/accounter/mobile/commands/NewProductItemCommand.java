package com.vimukti.accounter.mobile.commands;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class NewProductItemCommand extends AbstractItemCreateCommand {

	@Override
	protected Result weightRequirement(Context context, ResultList list,
			Object selection) {
		return numberOptionalRequirement(context, list, selection, "weight",
				getConstants().weight(),
				getMessages().pleaseEnter(getConstants().weight()));
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
}
