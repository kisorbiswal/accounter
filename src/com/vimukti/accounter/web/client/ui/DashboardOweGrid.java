package com.vimukti.accounter.web.client.ui;

import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class DashboardOweGrid extends ListGrid<ClientPayee> {

	public DashboardOweGrid() {
		super(false);
	}

	@Override
	public void init() {
		super.init();
		this.header.getElement().getParentElement().getParentElement()
				.addClassName("dashboard_grid_header");
	}

	@Override
	protected int getColumnType(int index) {
		switch (index) {
		case 0:
			return ListGrid.COLUMN_TYPE_LABEL;
		case 1:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXT;
		default:
			break;
		}
		return 0;
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 1) {
			return 200;
		} else {
			return -1;
		}
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.name(), messages.balance() };
	}

	@Override
	protected Object getColumnValue(ClientPayee obj, int index) {
		switch (index) {
		case 0:
			return obj.getName();
		case 1:
			return DataUtils.amountAsStringWithCurrency(obj.getBalance(),
					obj.getCurrency());
		default:
			break;
		}
		return null;
	}

	@Override
	protected String[] getSelectValues(ClientPayee obj, int index) {
		return null;
	}

	@Override
	protected void onValueChange(ClientPayee obj, int index, Object value) {

	}

	@Override
	protected boolean isEditable(ClientPayee obj, int row, int index) {
		return false;
	}

	@Override
	protected void onClick(ClientPayee obj, int row, int index) {
		onClickOnObj(obj);
	}

	@Override
	public void onDoubleClick(ClientPayee obj) {
		onClickOnObj(obj);
	}

	private void onClickOnObj(ClientPayee obj) {
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			if (obj instanceof ClientTAXAgency) {
				ActionFactory.getNewTAXAgencyAction().run(
						(ClientTAXAgency) obj, false);
				return;
			}
			ActionFactory.getStatementReport(obj.isVendor(), obj.getID()).run();
		}
	}

	@Override
	protected int sort(ClientPayee obj1, ClientPayee obj2, int index) {
		return 0;
	}

}
