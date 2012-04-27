package com.vimukti.accounter.web.client.ui.payroll;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientPayStructure;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.reports.ReportsRPC;

public class PayStructureListGrid extends BaseListGrid<ClientPayStructure> {

	public PayStructureListGrid() {
		super(false);
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_LINK,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { messages.employee(), messages.effectiveFrom(),
				messages.delete() };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { messages.employee(), messages.effectiveFrom(),
				messages.delete() };
	}

	@Override
	protected void executeDelete(ClientPayStructure object) {
		AccounterAsyncCallback<ClientPayStructure> callback = new AccounterAsyncCallback<ClientPayStructure>() {

			public void onException(AccounterException caught) {
			}

			public void onResultSuccess(ClientPayStructure result) {
				if (result != null) {
					deleteObject(result);

				}
			}

		};
		Accounter.createGETService().getObjectById(
				AccounterCoreType.PAY_STRUCTURE, object.getID(), callback);
	}

	@Override
	protected Object getColumnValue(ClientPayStructure obj, int index) {
		switch (index) {
		case 0:
			return obj.getEmployee() != null ? obj.getEmployee() : obj
					.getEmployeeGroup();
		case 1:
//			return obj.geteffectivefrom();
		case 2:
			return Accounter.getFinanceImages().delete();
		default:
			break;
		}
		return null;
	}

	@Override
	protected void onClick(ClientPayStructure obj, int row, int col) {
		switch (col) {
		case 2:
			showWarnDialog(obj);
			break;
		default:
			break;
		}
	}

	@Override
	public void onDoubleClick(ClientPayStructure obj) {
		ReportsRPC.openTransactionView(IAccounterCore.PAY_STRUCTURE,
				obj.getID());
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.employee(), messages.effectiveFrom(),
				messages.delete() };
	}

}
