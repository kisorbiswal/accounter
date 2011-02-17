package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.ui.banking.NewPayeeAction;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CompanyActionFactory;

public class PayeeCombo extends CustomCombo<ClientPayee> {

	public PayeeCombo(String title) {
		super(title);
	}

	@Override
	public String getDefaultAddNewCaption() {
		return comboConstants.newPayee();
	}

	@Override
	protected String getDisplayName(ClientPayee object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {
		NewPayeeAction action = CompanyActionFactory.getNewPayeeAction();
		action.setActionSource(this);
		action.run(this, null, true);
	}

	@Override
	public SelectItemType getSelectItemType() {
		return SelectItemType.PAYEE;
	}

	@Override
	protected String getColumnData(ClientPayee object, int row, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

}
