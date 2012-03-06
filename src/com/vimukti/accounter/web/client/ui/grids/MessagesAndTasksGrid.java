package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientMessageOrTask;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;

public class MessagesAndTasksGrid extends BaseListGrid<ClientMessageOrTask> {

	public MessagesAndTasksGrid() {
		super(false);
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	protected Object getColumnValue(ClientMessageOrTask obj, int index) {
		switch (index) {
		case 0:
			return Accounter.getFinanceImages().tickMark();
		case 1:
			if (obj.isSystemCreated()) {
				return messages.accounter();
			} else {
				return Accounter.getCompany().getUserById(obj.getCreatedBy())
						.getName();
			}
		case 2:
			return obj.getContent();
		case 3:
			return UIUtils.getDateByCompanyType(new ClientFinanceDate(obj
					.getDate()));
		case 4:
			return Accounter.getFinanceMenuImages().delete();
		}
		return null;
	}

	@Override
	protected String[] getSelectValues(ClientMessageOrTask obj, int index) {
		return null;
	}

	@Override
	protected void onValueChange(ClientMessageOrTask obj, int index,
			Object value) {

	}

	@Override
	protected boolean isEditable(ClientMessageOrTask obj, int row, int index) {
		return false;
	}

	@Override
	protected void onClick(ClientMessageOrTask obj, int row, int index) {
		if (index == 2) {
			Accounter.getMainFinanceWindow().historyChanged(
					obj.getActionToken());
		} else if (index == 4) {
			if (obj != null)
				showWarnDialog(obj);
		}
	}

	@Override
	protected int getCellWidth(int index) {

		switch (index) {
		case 0:
			return 35;
		case 1:
			return 65;
		case 3:
			return 70;
		case 4:
			if (UIUtils.isMSIEBrowser()) {
				return 25;
			} else {
				return 20;
			}
		}
		return -1;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.type(), messages.from(),
				messages.item(), messages.date(), "" };
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_IMAGE,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_DATE, ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected void executeDelete(ClientMessageOrTask data) {
		deleteObject(data);
	}

	@Override
	public void onDoubleClick(ClientMessageOrTask obj) {
		// TODO Auto-generated method stub

	}

}
