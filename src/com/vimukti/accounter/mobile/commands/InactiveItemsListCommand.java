package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Item;

public class InactiveItemsListCommand extends ItemsCommand {

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	protected List<Item> getItems(Company company, Boolean isActive) {
		return super.getItems(company, false);
	}

}
