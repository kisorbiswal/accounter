package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.ClientItemGroup;
import com.vimukti.accounter.web.client.ui.ItemGroupListDialog;

public class ItemGroupCombo extends CustomCombo<ClientItemGroup> {

	public ItemGroupCombo(String title) {
		super(title,"ItemGroupCombo");
	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages.itemGroup();
	}

	@Override
	protected String getDisplayName(ClientItemGroup object) {
		return object != null ? object.getName() != null ? object.getName()
				: "" : "";
	}

	@Override
	public void onAddNew() {
		ItemGroupListDialog itemGroupDialog = new ItemGroupListDialog("", "");
		itemGroupDialog.removeFromParent();
		itemGroupDialog.showAddEditGroupDialog(null);
		itemGroupDialog.addCallBack(createAddNewCallBack());

	}

	@Override
	protected String getColumnData(ClientItemGroup object,  int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}
}
