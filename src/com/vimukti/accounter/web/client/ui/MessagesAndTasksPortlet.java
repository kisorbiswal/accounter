package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientMessageOrTask;
import com.vimukti.accounter.web.client.core.ClientPortletConfiguration;
import com.vimukti.accounter.web.client.ui.grids.MessagesAndTasksGrid;

public class MessagesAndTasksPortlet extends Portlet {

	private MessagesAndTasksGrid grid;

	public MessagesAndTasksPortlet(ClientPortletConfiguration configuration) {
		super(configuration, messages.messagesAndTasks(), "");
		this.getElement().setId("MessagesAndTasksPortlet");
	}

	@Override
	public void createBody() {
		grid = new MessagesAndTasksGrid();
		grid.init();
		body.add(grid);
		updateData();
	}

	private void updateData() {
		grid.removeAllRecords();
		Accounter.createHomeService().getMessagesAndTasks(
				new AsyncCallback<List<ClientMessageOrTask>>() {

					@Override
					public void onSuccess(List<ClientMessageOrTask> result) {
						if (result != null && !result.isEmpty()) {
							grid.setRecords(result);
						} else {
							grid.addEmptyMessage(messages.noRecordsToShow());
						}
						completeInitialization();
					}

					@Override
					public void onFailure(Throwable caught) {
						completeInitialization();
					}
				});
	}

	@Override
	public void refreshWidget() {
		super.refreshWidget();
		this.body.clear();
		createBody();
	}
}
