package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientCurrency;

public class CurrencyCombo extends CustomCombo<ClientCurrency> {

	public CurrencyCombo(String title) {
		super(title);
		List<ClientCurrency> currency = new ArrayList<ClientCurrency>(
				getCompany().getCurrencies());
		initCombo(currency);

	}

	public CurrencyCombo(String title, boolean isAddNewRequired) {
		super(title, isAddNewRequired, 1);
		List<ClientCurrency> currency = new ArrayList<ClientCurrency>(
				getCompany().getCurrencies());
		initCombo(currency);
	}

	@Override
	protected String getDisplayName(ClientCurrency object) {

		if (object != null)
			return object.getFormalName() != null ? object.getFormalName() : "";
		else
			return "";
	}

	@Override
	protected String getColumnData(ClientCurrency object, int col) {
		switch (col) {
		case 0:
			return object.getFormalName();
		}
		return null;

	}

	@Override
	public String getDefaultAddNewCaption() {

		return comboMessages.newCurrency();
	}

	@Override
	public void onAddNew() {
		// ActionFactory.getNewCurrencyAction().run();
		// action.setCallback(new ActionCallback<ClientCurrency>() {
		//
		// @Override
		// public void actionResult(ClientCurrency result) {
		// addItemThenfireEvent(result);
		// }
		// });
		//
		// action.run(null, true);

	}

}
