package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientPortletConfiguration;

public class BankAccountsPortlet extends Portlet {
	private DashBoardBankAccountGrid grid;

	public BankAccountsPortlet(ClientPortletConfiguration configuration) {
		super(configuration, messages.bankAccounts(), messages
				.gotoBankAccountsList());
	}

	@Override
	public void createBody() {
		Accounter.createHomeService().getAccounts(ClientAccount.TYPE_BANK,
				new AsyncCallback<ArrayList<ClientAccount>>() {

					@Override
					public void onSuccess(ArrayList<ClientAccount> result) {
						grid = new DashBoardBankAccountGrid();
						grid.init();
						if (result != null && !(result.isEmpty())) {
							grid.setRecords(result);
						}
						body.add(grid);
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}
				});
		grid = new DashBoardBankAccountGrid();

	}

	@Override
	public void refreshWidget() {
		this.body.clear();
		createBody();
	}
}
