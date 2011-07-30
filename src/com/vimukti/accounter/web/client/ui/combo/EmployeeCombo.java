package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;

public class EmployeeCombo extends CustomCombo<ClientEmployee> {

	public List<ClientEmployee> users = new ArrayList<ClientEmployee>();
	private boolean isAdmin;

	public EmployeeCombo(String title) {
		super(title, false, 1);
		Accounter.createHomeService().getAllEmployees(
				new AccounterAsyncCallback<List<ClientEmployee>>() {
					@Override
					public void onSuccess(List<ClientEmployee> result) {
						users = result;
						if (isAdmin) {
							initCombo(users);
						} else {
							for (ClientEmployee user : users) {
								if (user.getID() == Accounter.getUser().getID()) {
									List<ClientEmployee> tempUsers = new ArrayList<ClientEmployee>();
									tempUsers.add(user);
									initCombo(tempUsers);
									break;
								}
							}
						}
					}

					@Override
					public void onException(AccounterException caught) {
						Accounter.showError(Accounter.constants().failedtoloadEmployeeslist());
					}
				});
	}

	@Override
	public SelectItemType getSelectItemType() {
		return null;
	}

	@Override
	protected String getDisplayName(ClientEmployee object) {
		if (object != null)
			return object.getDisplayName() != null ? object.getDisplayName()
					: "";
		else
			return "";
	}

	@Override
	protected String getColumnData(ClientEmployee object, int row, int col) {
		switch (col) {
		case 0:
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

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
		if (isAdmin) {
			initCombo(users);
		} else {
			for (ClientEmployee user : users) {
				if (user.getID() == Accounter.getUser().getID()) {
					List<ClientEmployee> tempUsers = new ArrayList<ClientEmployee>();
					tempUsers.add(user);
					initCombo(tempUsers);
					break;
				}
			}
		}
	}

}
