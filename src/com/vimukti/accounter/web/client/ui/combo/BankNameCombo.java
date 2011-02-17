package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.ClientBank;
import com.vimukti.accounter.web.client.ui.company.AddBankDialog;

public class BankNameCombo extends CustomCombo<ClientBank> {

	public BankNameCombo(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getDefaultAddNewCaption() {
		return comboConstants.newBankName();
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
	public SelectItemType getSelectItemType() {
		return SelectItemType.BANK_NAME;
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
