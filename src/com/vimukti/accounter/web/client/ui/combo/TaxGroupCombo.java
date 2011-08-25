package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.ClientTAXGroup;
import com.vimukti.accounter.web.client.core.ClientTAXItemGroup;
import com.vimukti.accounter.web.client.ui.company.ManageSalesTaxGroupsAction;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;

public class TaxGroupCombo extends CustomCombo<ClientTAXItemGroup> {

	public TaxGroupCombo(String title) {
		super(title);

	}

	@Override
	public String getDefaultAddNewCaption() {
		return comboMessages.newTaxGroup();
	}

	@Override
	protected String getDisplayName(ClientTAXItemGroup object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {
		ManageSalesTaxGroupsAction action = ActionFactory
				.getManageSalesTaxGroupsAction();
		action.setCallback(new ActionCallback<ClientTAXGroup>() {

			@Override
			public void actionResult(ClientTAXGroup result) {
				addItemThenfireEvent(result);

			}
		});

		action.run(null, true);
	}

	@Override
	protected String getColumnData(ClientTAXItemGroup object, int row, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

}
