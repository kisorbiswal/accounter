package com.vimukti.accounter.web.client.ui.combo;

import java.util.Arrays;

import com.vimukti.accounter.web.client.ui.Accounter;

public class SelectCombo extends CustomCombo<String> {

	// public static final int ACCOUNT_TYPES = 6;

	public SelectCombo(String title) {
		super(title, false, 1);
		super.setToopTip(Accounter.messages().selectWhichWeHaveInOurCompany(
				title));
		// if (type == SHIP_TO) {
		// initCombo(new ClientAddress().getAddressTypes());
		// } else if (type == PHONE) {
		// initCombo(new ClientAddress().getAddressTypes());
		// } else if (type == EMAIL) {
		// initCombo(new ClientAddress().getAddressTypes());
		// } else if (type == FAX){
		// initCombo(new ClientAddress().getAddressTypes());
		// }else if (type==ADDRESS) {
		//
		// }
	}

	// @Override
	// public SelectItemType getSelectItemType() {
	// SelectItemType itemType = null;
	// if (type == SHIP_TO) {
	// itemType = SelectItemType.SHIP_TO;
	// } else if (type == PHONE) {
	// itemType = SelectItemType.PHONE;
	// } else if (type == EMAIL) {
	// itemType = SelectItemType.EMAIL;
	// } else if (type == FAX) {
	// itemType = SelectItemType.FAX;
	// } else if (type == ADDRESS) {
	// itemType = SelectItemType.ADDRESS;
	// }
	// return itemType;
	// }

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
	protected String getDisplayName(String object) {
		if (object != null)
			return object.toString() != null ? object.toString() : "";
		else
			return "";
	}

	@Override
	protected String getColumnData(String object, int row, int col) {
		switch (col) {
		case 0:
			return object.toString();
		}
		return null;
	}

	@Override
	public void setValue(Object value) {

		super.setValue(value);
	}

	/**
	 * 
	 */
	public void setValueMap(String... strings) {
		comboItems.addAll(Arrays.asList(strings));
	}

}
