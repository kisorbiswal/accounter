package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.ClientShippingMethod;
import com.vimukti.accounter.web.client.ui.ShippingMethodListDialog;

public class ShippingMethodsCombo extends CustomCombo<ClientShippingMethod> {

	public ShippingMethodsCombo(String title) {
		super(title);
		initCombo(getCompany().getShippingMethods());
	}

	public ShippingMethodsCombo(String title, boolean isAddNewRequired) {
		super(title, isAddNewRequired, 1);
		initCombo(getCompany().getShippingMethods());
	}

	@Override
	public String getDefaultAddNewCaption() {
		return comboMessages.newShippingMethod();
	}

	@Override
	protected String getDisplayName(ClientShippingMethod object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {
		ShippingMethodListDialog shippingMethod = new ShippingMethodListDialog(
				"", "");
		shippingMethod.addCallBack(createAddNewCallBack());
		shippingMethod.hide();
		shippingMethod.showAddEditTermDialog(null);
	}

	@Override
	protected String getColumnData(ClientShippingMethod object, int row, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

}
