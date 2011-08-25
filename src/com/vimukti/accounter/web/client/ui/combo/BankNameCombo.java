package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.ClientBank;
import com.vimukti.accounter.web.client.ui.company.AddBankDialog;

public class BankNameCombo extends CustomCombo<ClientBank> {

	public BankNameCombo(String title) {
		super(title);
	}

	@Override
	public String getDefaultAddNewCaption() {
		return comboMessages.newBankName();
	}

	@Override
	protected String getDisplayName(ClientBank object) {
		if (object != null)
			return object.getDisplayName();
		else
			return "";
	}

	@Override
	public void onAddNew() {
		AddBankDialog bankNameDialog = new AddBankDialog(null);
		bankNameDialog.addCallBack(createAddNewCallBack());
	}


	@Override
	protected String getColumnData(ClientBank object, int row, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

}
