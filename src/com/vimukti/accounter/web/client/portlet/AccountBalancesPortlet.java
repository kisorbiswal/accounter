package com.vimukti.accounter.web.client.portlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientPortletConfiguration;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Portlet;

public class AccountBalancesPortlet extends Portlet {
	private AccountBalancesPortletToolBar toolBar;
	private VerticalPanel gridPanel;

	public AccountBalancesPortlet(ClientPortletConfiguration configuration) {
		super(configuration, messages.accountBalances(), "", "100%");
		this.setConfiguration(configuration);
	}

	@Override
	public void createBody() {
		gridPanel = new VerticalPanel();
		createToolbar();
		this.body.add(gridPanel);
	}

	private void createToolbar() {
		toolBar = new AccountBalancesPortletToolBar() {

			@Override
			protected void accountTypeChanged(String selectItem) {
				AccountBalancesPortlet.this.clearGrid();
				Map<String, String> portletMap = new HashMap<String, String>();
				portletMap.put(ACCOUNT_TYPE, selectItem);
				AccountBalancesPortlet.this.getConfiguration().setPortletMap(
						portletMap);
				AccountBalancesPortlet.this.updateData(Utility
						.getAccountType(selectItem));
			}

			@Override
			protected String updateComboData() {
				if (getConfiguration().getPortletMap().get(ACCOUNT_TYPE) != null) {
					return getConfiguration().getPortletMap().get(ACCOUNT_TYPE);
				} else {
					return messages.income();
				}
			}

		};
		this.body.add(toolBar);
	}

	protected void updateData(int accountType) {
		List<ClientAccount> accounts = Accounter.getCompany().getAccounts(
				accountType);
		AccountBalancesGrid grid = new AccountBalancesGrid();
		grid.init();
		if (accounts != null && (!accounts.isEmpty())) {
			List<ClientAccount> filteredAccounts = new ArrayList<ClientAccount>();
			for (ClientAccount account : accounts) {
				if (account.getTotalBalanceInAccountCurrency() != 0.0) {
					filteredAccounts.add(account);
				}
			}
			if (filteredAccounts != null && (!filteredAccounts.isEmpty())) {
				grid.setRecords(filteredAccounts);
			} else {
				grid.addEmptyMessage(messages.noRecordsToShow());
			}
		} else {
			grid.addEmptyMessage(messages.noRecordsToShow());
		}
		gridPanel.add(grid);
		completeInitialization();
	}

	public void clearGrid() {
		gridPanel.clear();
	}

	@Override
	public void refreshWidget() {
		super.refreshWidget();
		this.body.clear();
		createBody();
	}
}
