package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCustomerGroup;
import com.vimukti.accounter.web.client.ui.customers.CustomerGroupListDialog;

public class CustomerGroupCombo extends CustomCombo<ClientCustomerGroup> {

	public CustomerGroupCombo(String title) {
		super(title,"customerGroupCombo");
	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages.payeeGroup(Global.get().Customer());
	}

	@Override
	protected String getDisplayName(ClientCustomerGroup object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {
		CustomerGroupListDialog customerGroupDialog = new CustomerGroupListDialog(
				"", "");
		customerGroupDialog.hide();
		customerGroupDialog.addCallBack(createAddNewCallBack());
		customerGroupDialog.showAddEditGroupDialog(null);
	}

	@Override
	protected String getColumnData(ClientCustomerGroup object,  int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}
}
