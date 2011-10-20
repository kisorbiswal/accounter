package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.web.client.core.ClientItem;

public class InactiveItemsListCommand extends ItemsCommand {

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	protected List<ClientItem> getItems(Company company, Boolean isActive) {
		return super.getItems(false);
	}

}
