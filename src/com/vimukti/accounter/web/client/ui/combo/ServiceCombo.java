package com.vimukti.accounter.web.client.ui.combo;

import com.google.gwt.user.client.History;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.HistoryTokenUtils;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.company.NewItemAction;
import com.vimukti.accounter.web.client.ui.core.CompanyActionFactory;
import com.vimukti.accounter.web.client.ui.core.CustomersActionFactory;
import com.vimukti.accounter.web.client.ui.core.VendorsActionFactory;

public class ServiceCombo extends CustomCombo<ClientItem> {
	// Type For Checking Request is from Customer View Or Vendor View
	private int type;

	// private final int TYPE_CUSTOMER = 1;
	// private final int TYPE_VENDOR = 2;

	public ServiceCombo(String title, int type) {
		super(title);
		this.type = type;
		initCombo(FinanceApplication.getCompany().getServiceItems());
	}

	public ServiceCombo(String title, int type, boolean isAddNewRequired) {
		super(title, isAddNewRequired, 1);
		this.type = type;
		initCombo(FinanceApplication.getCompany().getServiceItems());
	}

	@Override
	public String getDefaultAddNewCaption() {
		return comboConstants.newServiceItem();
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
		action.setType(1);
		HistoryTokenUtils.setPresentToken(action, null);
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
