package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.ClientPayStructureDestination;

public class EmployeesAndGroupsCombo extends
		CustomCombo<ClientPayStructureDestination> {

	public EmployeesAndGroupsCombo(String title, String styleName) {
		super(title, false, 1, styleName);
	}

	@Override
	protected String getDisplayName(ClientPayStructureDestination object) {
		return object.getDisplayName();
	}

	@Override
	protected String getColumnData(ClientPayStructureDestination object, int col) {
		if (col == 0) {
			return object.getDisplayName();
		}
		return null;
	}

	@Override
	public String getDefaultAddNewCaption() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onAddNew() {
		// TODO Auto-generated method stub

	}

}
