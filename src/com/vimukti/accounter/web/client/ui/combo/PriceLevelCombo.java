package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.ui.PriceLevelListDialog;

public class PriceLevelCombo extends CustomCombo<ClientPriceLevel> {

	public PriceLevelCombo(String title) {
		super(title);
	}

	@Override
	public String getDefaultAddNewCaption() {
		return comboMessages.newPriceLevel();
	}

	@Override
	protected String getDisplayName(ClientPriceLevel object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {
		PriceLevelListDialog priceLevelDialog = new PriceLevelListDialog("", "");
		priceLevelDialog.hide();
		priceLevelDialog.addCallBack(createAddNewCallBack());
		priceLevelDialog.showAddEditPriceLevel(null);
	}

	@Override
	protected String getColumnData(ClientPriceLevel object, int row, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

}
