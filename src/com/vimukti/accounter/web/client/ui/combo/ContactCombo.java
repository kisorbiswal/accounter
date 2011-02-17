package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.ClientContact;

public class ContactCombo extends CustomCombo<ClientContact> {

	public ContactCombo(String title) {
		super(title);
	}

	@Override
	public String getDefaultAddNewCaption() {
		return null;
	}

	@Override
	protected String getDisplayName(ClientContact object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {

	}

	@Override
	public SelectItemType getSelectItemType() {
		return SelectItemType.CONTACTS;
	}

	@Override
	protected String getColumnData(ClientContact object, int row, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

}
