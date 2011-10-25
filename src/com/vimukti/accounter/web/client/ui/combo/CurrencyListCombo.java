package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.ui.CoreUtils;

/**
 * this class is used to display all the currencies list
 * 
 */
public class CurrencyListCombo extends CustomCombo<ClientCurrency> {
	public CurrencyListCombo(String text) {
		super(text);
		
		initCombo(getCurrencyList());

	}

	public List<ClientCurrency> getCurrencyList() {
		return CoreUtils.getCurrencies(getCompany().getCurrencies());

	}

	protected String getColumnData(ClientCurrency object, int row, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

	@Override
	public String getDefaultAddNewCaption() {
		return null;
	}

	@Override
	protected String getDisplayName(ClientCurrency object) {

		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {
	}

	@Override
	protected String getColumnData(ClientCurrency object, int col) {
		return object.getName();
	}

}
