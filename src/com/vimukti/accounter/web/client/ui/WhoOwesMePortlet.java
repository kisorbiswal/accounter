package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientPortletConfiguration;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;

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
				if (Accounter.getUser().getPermissions()
						.getTypeOfPayBillsPayments() == RolePermissions.TYPE_YES) {
					createLink();
				}
				completeInitialization();
			}

			@Override
			public void onFailure(Throwable caught) {
				completeInitialization();
			}
		};
		Accounter.createHomeService().getOwePayees(TYPE_OWE_TO_ME, callback);
	}

	private void createLink() {
		StyledPanel linkPanel = new StyledPanel("linkPanel");
		Anchor receivePaymentLink = new Anchor(messages.addaNew(messages
				.receivedPayment()));
		receivePaymentLink.addStyleName("portlet_link");
		receivePaymentLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ActionFactory.getReceivePaymentAction().run();
			}
		});
		linkPanel.add(receivePaymentLink);
		body.add(linkPanel);
	}

	@Override
	public void refreshWidget() {
		super.refreshWidget();
		this.body.clear();
		createBody();
	}
}
