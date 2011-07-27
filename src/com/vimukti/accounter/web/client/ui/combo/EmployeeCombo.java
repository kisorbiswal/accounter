package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.ui.Accounter;

public class EmployeeCombo extends CustomCombo<ClientUser> {

	public List<ClientUser> users = new ArrayList<ClientUser>();
	private boolean isAdmin;

	public EmployeeCombo(String title) {
		super(title, false, 1);
		Accounter.createHomeService().getAllUsers(
				new AsyncCallback<List<ClientUser>>() {

					@Override
					public void onSuccess(List<ClientUser> result) {
						users = result;
						if (isAdmin) {
							initCombo(users);
						} else {
							for (ClientUser user : users) {
								if (user.getID() == Accounter.getUser().getID()) {
									List<ClientUser> tempUsers = new ArrayList<ClientUser>();
									tempUsers.add(user);
									initCombo(tempUsers);
									break;
								}
							}
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						if (caught instanceof InvocationException) {
							Accounter
									.showMessage("Your session expired, Please login again to continue");
						} else {
							Accounter.showError("Failed to load users list");
						}
					}
				});
	}

	@Override
	public SelectItemType getSelectItemType() {
		return null;
	}

	@Override
	protected String getDisplayName(ClientUser object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	protected String getColumnData(ClientUser object, int row, int col) {
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
			for (ClientUser user : users) {
				if (user.getID() == Accounter.getUser().getID()) {
					List<ClientUser> tempUsers = new ArrayList<ClientUser>();
					tempUsers.add(user);
					initCombo(tempUsers);
					break;
				}
			}
		}
	}

}
