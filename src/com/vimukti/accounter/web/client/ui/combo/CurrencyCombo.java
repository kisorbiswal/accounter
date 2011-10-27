package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.customers.CurrencyGroupListDialog;

public class CurrencyCombo extends CustomCombo<ClientCurrency> {
	private AccounterConstants constants = Global.get().constants();

	public CurrencyCombo(String title) {
		super(title);
		List<ClientCurrency> currency = new ArrayList<ClientCurrency>(
				getCompany().getCurrencies());
		initCombo(currency);
		setSelected(currency.get(0).getFormalName());

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

		CurrencyGroupListDialog groupListDialog = new CurrencyGroupListDialog(
				constants.manageCurrency(), constants.toAddCurrencyGroup());

		groupListDialog.hide();
		groupListDialog.addCallBack(createAddNewCallBack());
		groupListDialog.ShowAddEditDialog(null);

	}

}
