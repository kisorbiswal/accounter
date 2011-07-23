package com.vimukti.accounter.web.client.ui.settings;

import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class RecentActivityListGrid extends ListGrid<ClientUser> {

	private UsersView usersView;

	public RecentActivityListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	protected int getColumnType(int index) {
		switch (index) {
		case 1:
			return ListGrid.COLUMN_TYPE_DATE;
		default:
			return ListGrid.COLUMN_TYPE_TEXT;
		}
	}

	public void setRecentActivityGridView(UsersView view) {
		this.usersView = view;
	}

	@Override
	public boolean validateGrid() throws InvalidTransactionEntryException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected int getCellWidth(int index) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { Accounter.getSettingsMessages().name(),
				Accounter.getSettingsMessages().loginDate() };
	}

	@Override
	protected Object getColumnValue(ClientUser obj, int index) {
		switch (index) {
		case 0:
			return obj.getFullName();
		case 1:
			return obj.getLastLogin();
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
		return 0;
	}

}
