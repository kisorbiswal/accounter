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
	public void onAddNew() {

	}

	public void setDefaultToFirstOption(boolean b) {
		if (b) {
			this.setComboItem(comboItems.get(0));
		}
	}

	@Override
	public SelectItemType getSelectItemType() {
		return SelectItemType.CONTACTS;
	}

	@Override
	public void setValue(Object value) {
		super.setValue(value);
	}

	@Override
	protected String getDisplayName(ClientContact object) {
		if (object != null)
			return object.getName().toString() != null ? object.getName()
					.toString() : "";
		else
			return "";
	}

	@Override
	protected String getColumnData(ClientContact object, int row, int col) {
		switch (col) {
		case 0:
			return object.getName().toString();
		}
		return null;
	}

}
