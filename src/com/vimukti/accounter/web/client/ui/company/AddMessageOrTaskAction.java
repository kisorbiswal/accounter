package com.vimukti.accounter.web.client.ui.company;

import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.customers.AddMessageOrTaskDialog;

public abstract class AddMessageOrTaskAction extends Action {

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run() {
		AddMessageOrTaskDialog dialog = new AddMessageOrTaskDialog(
				messages.messagesAndTasks()) {

			@Override
			protected void onSuccess() {
				updateData();
			};
		};
		dialog.center();
		dialog.show();

	}

	@Override
	public String getHistoryToken() {
		return "messageOrTask";
	}

	@Override
	public String getHelpToken() {
		// TODO Auto-generated method stub
		return null;
	}

	public abstract void updateData();

}
