package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.ClientTAXItemGroup;
import com.vimukti.accounter.web.client.ui.SalesTaxGroupListDialog;

public class TaxGroupCombo extends CustomCombo<ClientTAXItemGroup> {

	public TaxGroupCombo(String title) {
		super(title);

	}

	@Override
	public String getDefaultAddNewCaption() {
		return comboConstants.newTaxGroup();
	}

	@Override
	protected String getDisplayName(ClientTAXItemGroup object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onAddNew() {
		SalesTaxGroupListDialog taxGroupDialog = new SalesTaxGroupListDialog(
				"", "");
		taxGroupDialog.addCallBack(createAddNewCallBack());
		taxGroupDialog.hide();
		taxGroupDialog.showAddEditTaxGroup(null);
	}

	@Override
	public SelectItemType getSelectItemType() {
		return SelectItemType.TAX_GROUP;
	}

	@Override
	protected String getColumnData(ClientTAXItemGroup object, int row, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

}
