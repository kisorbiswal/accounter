package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientPortletConfiguration;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;

public class BankAccountsPortlet extends Portlet {
	private DashBoardBankAccountGrid grid;

	public BankAccountsPortlet(ClientPortletConfiguration configuration) {
		super(configuration, messages.bankAccounts(), messages
				.gotoBankAccountsList(), "60%");
	}

	@Override
	public void goToClicked() {
		ActionFactory.getChartOfAccountsAction(ClientAccount.TYPE_BANK).run();
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
						} else {
							grid.addEmptyMessage(messages.noBanksToShow());
						}
						body.add(grid);
						completeInitialization();
					}

					@Override
					public void onFailure(Throwable caught) {
						completeInitialization();
					}
				});
		grid = new DashBoardBankAccountGrid();

	}

	@Override
	public void refreshWidget() {
		super.refreshWidget();
		this.body.clear();
		createBody();
	}
}
