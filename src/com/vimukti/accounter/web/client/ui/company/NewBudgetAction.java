package com.vimukti.accounter.web.client.ui.company;

import java.util.List;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.NewAccountView;
import com.vimukti.accounter.web.client.ui.core.Action;

public class NewBudgetAction extends Action<ClientAccount> {

	private List<Integer> accountTypes;
	protected NewAccountView view;

	public NewBudgetAction(String newCustomer) {
		super(newCustomer);
		this.catagory = Accounter.constants().company();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHelpToken() {
		// TODO Auto-generated method stub
		return null;
	}

}
