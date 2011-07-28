package com.vimukti.accounter.web.client.ui.settings;

import java.util.Date;
import java.util.List;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class UsersListGrid extends BaseListGrid<ClientUser> {
	private UsersView usersView;

	public UsersListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	@Override
	protected int getColumnType(int index) {
		switch (index) {
		case 2:
			return ListGrid.COLUMN_TYPE_DATE;
		case 4:
			return ListGrid.COLUMN_TYPE_IMAGE;
		default:
			return ListGrid.COLUMN_TYPE_TEXT;
		}
	}

	@Override
	public void addRecords(List<ClientUser> list) {
		super.addRecords(list);
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public boolean validateGrid() throws InvalidTransactionEntryException {
		return false;
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		// case 5:
		// return 25;
		// case 2:
		// return 100;
		case 4:
			return 25;
		default:
			return -1;
		}
	}

	@Override
	protected String[] getColumns() {

		return new String[] { Accounter.constants().name(),
				Accounter.constants().permissions(),
				// FinanceApplication.constants().status(),
				Accounter.constants().lastLogin(),
				Accounter.constants().loginCount(), "" };
	}

	public void setUsersView(UsersView usersView) {
		this.usersView = usersView;
	}

	@Override
	protected Object getColumnValue(ClientUser obj, int index) {
		switch (index) {
		case 0:
			return obj.getName();
		case 1:
			// if (obj.isCanDoUserManagement())
			// return obj.getUserRole() + " + Manage Users";
			// else
			return obj.getUserRole();
			// case 2:
			// // return obj.getStatus();
			// if (obj.isActive())
			// return "Active";
			// else
			// return "Pending";
		case 2:
			return obj.getLastLogin() != 0 ? DateTimeFormat.getFormat(
					"dd MMM yyyy, hh:mm a")
					.format(new Date(obj.getLastLogin())) : "";
		case 3:
			return String.valueOf(obj.getLoginCount());
		case 4:
			return Accounter.getFinanceImages().delete();
		default:
			return "";
		}
	}

	@Override
	protected String[] getSelectValues(ClientUser obj, int index) {
		return null;
	}

	@Override
	protected boolean isEditable(ClientUser obj, int row, int index) {
		return false;
	}

	@Override
	protected void onClick(ClientUser obj, int row, int index) {

		if (index == 4) {
			if (Accounter.getUser().isCanDoUserManagement()) {
				showWarnDialog(obj);
			} else {
				Accounter
						.showInformation("you dont have permissions to delete user");
			}
		}
		/*
		 * if (!FinanceApplication.getUser().isCanDoUserManagement()) return; if
		 * (index == 4) { showWarnDialog(obj); }
		 */
	}

	@Override
	public void onDoubleClick(ClientUser obj) {
		if (Accounter.getUser().isCanDoUserManagement()) {
			SettingsActionFactory.getInviteUserAction().run(obj, true);
		}
	}

	@Override
	protected void onValueChange(ClientUser obj, int index, Object value) {

	}

	@Override
	protected int sort(ClientUser obj1, ClientUser obj2, int index) {
		return 0;
	}

	@Override
	protected void executeDelete(ClientUser object) {
		ViewManager.getInstance().deleteObject(object, AccounterCoreType.USER,
				this);
	}

	@Override
	protected int[] setColTypes() {
		return null;
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

	}

	@Override
	public void deleteFailed(Throwable caught) {
		if (caught instanceof InvalidOperationException)
			Accounter.showError(((InvalidOperationException) caught)
					.getDetailedMessage());
		else
			Accounter.showError("You can't delete this user");
		caught.fillInStackTrace();
	}

}
