package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;

public class CurrencyCombo extends CustomCombo<ClientCurrency> {

	public CurrencyCombo(String title) {
		super(title);

	}

	@Override
	protected String getDisplayName(ClientCurrency object) {

		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	protected String getColumnData(ClientCurrency object,  int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;

	}

	@Override
	public String getDefaultAddNewCaption() {

		return comboMessages.newCurrency();
	}

	@Override
	public void onAddNew() {
		NewCurrencyAction action = ActionFactory.getNewCurrencyAction();
		action.setCallback(new ActionCallback<ClientCurrency>() {

			@Override
			public void actionResult(ClientCurrency result) {
				addItemThenfireEvent(result);
			}
		});

		action.run(null, true);

	}

}
