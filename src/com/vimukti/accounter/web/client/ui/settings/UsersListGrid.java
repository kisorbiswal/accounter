package com.vimukti.accounter.web.client.ui.settings;

import java.util.List;

import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.data.ClientUser;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
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
		case 3:
			return ListGrid.COLUMN_TYPE_DATE;
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
		return -1;
	}

	@Override
	protected String[] getColumns() {

		return new String[] { "Name", "Permissions", "Status", "Last Login",
				"Logins this week" };
	}

	public void setUsersView(UsersView usersView) {
		this.usersView = usersView;
	}

	@Override
	protected Object getColumnValue(ClientUser obj, int index) {
		switch (index) {
		case 0:
			return obj.getFullName();
		case 1:
			return obj.getRole();
		case 2:
			return obj.getStatus();
		case 3:
			return obj.getLastLogin().getDate();
		case 4:
			return "";
		default:
			return "";
		}
	}

	@Override
	protected String[] getSelectValues(ClientUser obj, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean isEditable(ClientUser obj, int row, int index) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onClick(ClientUser obj, int row, int index) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDoubleClick(ClientUser obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onValueChange(ClientUser obj, int index, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	protected int sort(ClientUser obj1, ClientUser obj2, int index) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void executeDelete(ClientUser object) {
		// TODO Auto-generated method stub

	}

	@Override
	protected int[] setColTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

	}

}
