package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientPortletConfiguration;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;

public class WhoOwesMePortlet extends Portlet {
	private DashboardOweGrid grid;

	public WhoOwesMePortlet(ClientPortletConfiguration configuration) {
		super(configuration, messages.whoOwesMe(), "");
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
		Accounter.createHomeService().getOwePayees(TYPE_OWE_TO_ME, callback);
	}

	@Override
	public void refreshWidget() {
		this.body.clear();
		createBody();
	}
}
