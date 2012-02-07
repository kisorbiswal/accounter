package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.AccounterClassListDialog;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class AccounterClassListAction extends Action {

	public AccounterClassListAction() {
		super();
		this.catagory = messages.company();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(Object data, boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				AccounterClassListDialog dialog = new AccounterClassListDialog(
						messages.manageAccounterClass(), messages
								.toAddAccounterClass());
				dialog.show();

			}

		});

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
		return "accounter-Class-List";
	}

	@Override
	public String getHelpToken() {
		return "accounter-Class-List";
	}

	@Override
	public String getText() {
		return messages.accounterClassList();
	}

}
