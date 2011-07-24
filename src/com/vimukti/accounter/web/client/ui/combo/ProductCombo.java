package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.HistoryTokenUtils;
import com.vimukti.accounter.web.client.ui.company.NewItemAction;
import com.vimukti.accounter.web.client.ui.core.CustomersActionFactory;
import com.vimukti.accounter.web.client.ui.core.VendorsActionFactory;

public class ProductCombo extends CustomCombo<ClientItem> {
	// Type For Checking Request is from Customer View Or Vendor View
	private int type;

	// private final int TYPE_CUSTOMER = 1;
	// private final int TYPE_VENDOR = 2;

	public ProductCombo(String title, int type) {
		super(title);
		this.type = type;
		initCombo(getCompany().getProductItems());
	}

	public ProductCombo(String title, int type, boolean isAddNewRequired) {
		super(title, isAddNewRequired, 1);
		this.type = type;
		initCombo(getCompany().getProductItems());
	}

	@Override
	public String getDefaultAddNewCaption() {
		return comboConstants.newItem();
	}

	@Override
	protected String getDisplayName(ClientItem object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {
		NewItemAction action;
		if (type == 1) {
			action = CustomersActionFactory.getNewItemAction();
		} else {
			action = VendorsActionFactory.getNewItemAction();
		}
		action.setActionSource(this);
		action.setType(2);
		
		action.run(null, true);
	}

	@Override
	public SelectItemType getSelectItemType() {
		return SelectItemType.ITEM;
	}

	@Override
	protected String getColumnData(ClientItem object, int row, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

}
