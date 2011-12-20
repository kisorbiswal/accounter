package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientPortletConfiguration;

public class WhoOwesMePortlet extends Portlet {
	private DashboardOweGrid grid;

	public WhoOwesMePortlet(ClientPortletConfiguration configuration) {
		super(configuration, messages.whoOwesMe(), "", "100%");
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
					grid.addEmptyMessage(messages.noRecordsToShow());
				}
				body.add(grid);
				completeInitialization();
			}

			@Override
			public void onFailure(Throwable caught) {
				completeInitialization();
			}
		};
		Accounter.createHomeService().getOwePayees(TYPE_OWE_TO_ME, callback);
	}

	@Override
	public void refreshWidget() {
		super.refreshWidget();
		this.body.clear();
		createBody();
	}
}
