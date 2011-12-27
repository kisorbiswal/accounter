package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientMessageOrTask;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;

public class MessagesAndTasksGrid extends ListGrid<ClientMessageOrTask> {

	public MessagesAndTasksGrid() {
		super(false);
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	protected int getColumnType(int index) {
		switch (index) {
		case 0:
			return ListGrid.COLUMN_TYPE_IMAGE;
		case 1:
			return ListGrid.COLUMN_TYPE_TEXT;
		case 2:
			return ListGrid.COLUMN_TYPE_TEXT;
		case 3:
			return ListGrid.COLUMN_TYPE_DATE;
		}
		return 0;
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
		}
	}

	@Override
	public void onDoubleClick(ClientMessageOrTask obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected int sort(ClientMessageOrTask obj1, ClientMessageOrTask obj2,
			int index) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getCellWidth(int index) {

		switch (index) {
		case 0:
			return 30;
		case 1:
			return 65;
		case 3:
			return 70;
		}
		return -1;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.type(), messages.from(),
				messages.item(), messages.date() };
	}

}
