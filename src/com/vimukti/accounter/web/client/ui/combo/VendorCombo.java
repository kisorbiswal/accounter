package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.HistoryTokenUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.VendorsActionFactory;
import com.vimukti.accounter.web.client.ui.vendors.NewVendorAction;

public class VendorCombo extends CustomCombo<ClientVendor> {

	public VendorCombo(String title) {
		super(title);
		initCombo(getCompany().getActiveVendors());
	}

	public VendorCombo(String title, boolean isAddNewRequired) {
		super(title, isAddNewRequired, 1);
		initCombo(getCompany().getActiveVendors());
	}

	@Override
	public String getDefaultAddNewCaption() {
		return UIUtils.getVendorString(Accounter
				.getAccounterComboConstants().newSupplier(), Accounter
				.getAccounterComboConstants().newVendor());

	}

	@Override
	protected String getDisplayName(ClientVendor object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {
		NewVendorAction action = VendorsActionFactory.getNewVendorAction();
		action.setActionSource(this);
		
		action.run(null, true);
	}

	@Override
	public SelectItemType getSelectItemType() {
		return SelectItemType.VENDOR;
	}

	@Override
	protected String getColumnData(ClientVendor object, int row, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

}
