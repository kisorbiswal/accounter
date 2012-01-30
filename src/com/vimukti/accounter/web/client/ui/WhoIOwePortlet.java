package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientPortletConfiguration;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;

public class WhoIOwePortlet extends Portlet {
	private DashboardOweGrid grid;

	public WhoIOwePortlet(ClientPortletConfiguration configuration) {
		super(configuration, messages.whoIOwe(), "", "100%");
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
						.getTypeOfPayBillsPayments() == RolePermissions.TYPE_YES
						&& getPreferences().isKeepTrackofBills()) {
					createLink();
				}
				completeInitialization();
			}

			@Override
			public void onFailure(Throwable caught) {
				completeInitialization();
				// TODO Auto-generated method stub

			}
		};
		Accounter.createHomeService().getOwePayees(TYPE_I_OWE, callback);
	}

	private void createLink() {
		HorizontalPanel linkPanel = new HorizontalPanel();
		Anchor payBillLink = new Anchor(messages.addaNew(messages.payBill()));
		payBillLink.addStyleName("portlet_link");
		payBillLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ActionFactory.getPayBillsAction().run();
			}
		});
		linkPanel.add(payBillLink);
		linkPanel.getElement().getStyle().setPadding(7, Unit.PX);
		linkPanel.setCellHorizontalAlignment(payBillLink,
				HasAlignment.ALIGN_RIGHT);
		body.add(linkPanel);
	}

	@Override
	public void refreshWidget() {
		super.refreshWidget();
		this.body.clear();
		createBody();
	}
}
