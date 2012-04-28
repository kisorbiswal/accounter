package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientUserInfo;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;

public class EmployeeCombo extends CustomCombo<ClientUserInfo> {

	public List<ClientUserInfo> users = new ArrayList<ClientUserInfo>();
	private boolean isAdmin;

	public EmployeeCombo(String title) {
		this(title, false);
		if (!Accounter.getUser().isAdminUser()) {
			isAdmin = false;
		}else{
			isAdmin = true;
		}
	}

	public EmployeeCombo(String title, boolean b) {
		super(title, b, 1,"EmployeeCombo");
		Accounter.createHomeService().getAllUsers(
				new AccounterAsyncCallback<ArrayList<ClientUserInfo>>() {
					@Override
					public void onResultSuccess(ArrayList<ClientUserInfo> result) {
						users = result;
						if (isAdmin) {
							initCombo(users);
						} else {
							for (ClientUserInfo user : users) {
								if (user.getID() == Accounter.getUser().getID()) {
									List<ClientUserInfo> tempUsers = new ArrayList<ClientUserInfo>();
									tempUsers.add(user);
									initCombo(tempUsers);
									break;
								}
							}
						}
					}

					@Override
					public void onException(AccounterException caught) {
						Accounter.showError(messages
								.failedtoloadEmployeeslist());
					}
				});
	}

	@Override
	protected String getDisplayName(ClientUserInfo object) {
		if (object != null)
			return object.getDisplayName() != null ? object.getDisplayName()
					: "";
		else
			return "";
	}

	@Override
	protected String getColumnData(ClientUserInfo object,  int col) {
		switch (col) {
		case 0:
			return object.getName();
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
			for (ClientUserInfo user : users) {
				if (user.getID() == Accounter.getUser().getID()) {
					List<ClientUserInfo> tempUsers = new ArrayList<ClientUserInfo>();
					tempUsers.add(user);
					initCombo(tempUsers);
					break;
				}
			}
		}
	}

}
