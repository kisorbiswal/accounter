package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.HistoryTokenUtils;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CustomersActionFactory;

public class CustomerCombo extends CustomCombo<ClientCustomer> {

	public CustomerCombo(String title) {
		super(title);
		initCombo(getCompany().getActiveCustomers());

	}

	public CustomerCombo(String title, boolean isAddNewRequire) {
		super(title, isAddNewRequire, 1);
		initCombo(getCompany().getActiveCustomers());

	}

	@Override
	public String getDefaultAddNewCaption() {
		return comboConstants.newCustomer();
	}

	@Override
	protected String getDisplayName(ClientCustomer object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {
		Action action = CustomersActionFactory.getNewCustomerAction();
		action.setActionSource(this);
		
		action.run(null, true);
	}

	@Override
	public SelectItemType getSelectItemType() {
		return SelectItemType.CUSTOMER;
	}

	@Override
	protected String getColumnData(ClientCustomer object, int row, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

}
