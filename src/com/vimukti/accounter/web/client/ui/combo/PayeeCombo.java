package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.ui.banking.SelectPayeeDialog;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;

public class PayeeCombo extends CustomCombo<ClientPayee> {

	public PayeeCombo(String title) {
		super(title);
	}

	@Override
	public String getDefaultAddNewCaption() {
		return comboMessages.newPayee();
	}

	@Override
	protected String getDisplayName(ClientPayee object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {

		SelectPayeeDialog dialog = new SelectPayeeDialog();
		dialog.setCallback(new ActionCallback<ClientPayee>() {

			@Override
			public void actionResult(ClientPayee result) {
				addItemThenfireEvent(result);
			}
		});
		dialog.show();

		// NewPayeeAction action = ActionFactory.getNewPayeeAction();
		// action.setCallback(new ActionCallback<ClientPayee>() {
		//
		// @Override
		// public void actionResult(ClientPayee result) {
		// addItemThenfireEvent(result);
		// }
		// });
		// action.run(this, null, true);
	}

		// NewPayeeAction action = ActionFactory.getNewPayeeAction();
		// action.setCallback(new ActionCallback<ClientPayee>() {
		//
		// @Override
		// public void actionResult(ClientPayee result) {
		// addItemThenfireEvent(result);
		// }
		// });
		// action.run(this, null, true);

	@Override
	protected String getColumnData(ClientPayee object, int row, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

}
