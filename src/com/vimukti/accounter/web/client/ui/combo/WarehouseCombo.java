package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.ClientWarehouse;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;

public class WarehouseCombo extends CustomCombo<ClientWarehouse> {

	public WarehouseCombo(String title) {
		super(title);
		// initCombo(Accounter.getCompany().getWarehouses());
	}

	public WarehouseCombo(String title, boolean isAddNewRequire) {
		super(title, isAddNewRequire, 1);
		// initCombo(Accounter.getCompany().getWarehouses());
	}

	@Override
	public String getDefaultAddNewCaption() {
		return comboMessages.addNewWareHouse();
	}

	@Override
	protected String getDisplayName(ClientWarehouse object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {
		Action<ClientWarehouse> action = ActionFactory.getWareHouseViewAction();
		action.setCallback(new ActionCallback<ClientWarehouse>() {

			@Override
			public void actionResult(ClientWarehouse result) {
				addItemThenfireEvent(result);

			}
		});
		action.run(null, true);

	}

	@Override
	protected String getColumnData(ClientWarehouse object, int row, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

}
