package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientPortletConfiguration;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;

public class WhoIOwePortlet extends Portlet {
	private DashboardOweGrid grid;

	public WhoIOwePortlet(ClientPortletConfiguration configuration) {
		super(configuration, messages.whoIOwe(), "");
	}

	@Override
	public void createBody() {

		AsyncCallback<List<ClientPayee>> callback = new AsyncCallback<List<ClientPayee>>() {

			@Override
			public void onSuccess(List<ClientPayee> result) {
				grid = new DashboardOweGrid();
				grid.init();
				if (result != null && !(result.isEmpty())) {
					grid.setRecords(result);
				} else {
					grid.addEmptyMessage(AccounterWarningType
							.getWarning(AccounterWarningType.RECORDSEMPTY));
				}
				body.add(grid);
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		};
		Accounter.createHomeService().getOwePayees(TYPE_I_OWE, callback);
	}

	@Override
	public void refreshWidget() {
		this.body.clear();
		createBody();
	}
}
