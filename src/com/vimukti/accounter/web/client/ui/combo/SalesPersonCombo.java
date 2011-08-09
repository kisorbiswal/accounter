package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.ui.company.NewSalesperSonAction;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;

public class SalesPersonCombo extends CustomCombo<ClientSalesPerson> {

	public SalesPersonCombo(String title) {
		super(title);
	}

	public SalesPersonCombo(String title, boolean isAddNewRequired) {
		super(title, isAddNewRequired, 1);
	}

	@Override
	public String getDefaultAddNewCaption() {
		return comboConstants.newSalesPerson();
	}

	@Override
	protected String getDisplayName(ClientSalesPerson object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {
		NewSalesperSonAction action = ActionFactory.getNewSalesperSonAction();
		action.setCallback(new ActionCallback<ClientSalesPerson>() {

			@Override
			public void actionResult(ClientSalesPerson result) {
				addItemThenfireEvent(result);

			}
		});

		action.run(null, true);
	}

	@Override
	protected String getColumnData(ClientSalesPerson object, int row, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}
}
