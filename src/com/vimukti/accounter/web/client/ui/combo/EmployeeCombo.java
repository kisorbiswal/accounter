package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.ui.Accounter;

public class EmployeeCombo extends CustomCombo<ClientUser> {

	public EmployeeCombo(String title) {
		super(title);
		initCombo(getCompany().getUsersList());
	}

	@Override
	public SelectItemType getSelectItemType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getDisplayName(ClientUser object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getColumnData(ClientUser object, int row, int col) {
		// TODO Auto-generated method stub
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
