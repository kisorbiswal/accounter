package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.ui.ItemView;
import com.vimukti.accounter.web.client.ui.company.NewItemAction;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;

public class ServiceCombo extends CustomCombo<ClientItem> {
	// Type For Checking Request is from Customer View Or Vendor View
	private int type;

	// private final int TYPE_CUSTOMER = 1;
	// private final int TYPE_VENDOR = 2;

	public ServiceCombo(String title, int type) {
		super(title);
		this.type = type;
		initCombo(getCompany().getServiceItems());
	}

	public ServiceCombo(String title, int type, boolean isAddNewRequired) {
		super(title, isAddNewRequired, 1);
		this.type = type;
		initCombo(getCompany().getServiceItems());
	}

	@Override
	public String getDefaultAddNewCaption() {
		return comboMessages.newServiceItem();
	}

	@Override
	protected String getDisplayName(ClientItem object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {
		NewItemAction action;
		if (type == 1) {
			action = ActionFactory.getNewItemAction(true);
		} else {
			action = ActionFactory.getNewItemAction(false);
		}
		action.setCallback(new ActionCallback<ClientItem>() {

			@Override
			public void actionResult(ClientItem result) {
				if (result.isActive())
					addItemThenfireEvent(result);

			}
		});
		action.setType(ItemView.TYPE_SERVICE);

		action.run(null, true);
	}

	@Override
	protected String getColumnData(ClientItem object, int row, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

}
