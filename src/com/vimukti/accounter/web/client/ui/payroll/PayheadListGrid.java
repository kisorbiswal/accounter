package com.vimukti.accounter.web.client.ui.payroll;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.reports.ReportsRPC;

public class PayheadListGrid extends BaseListGrid<ClientPayHead> {

	public PayheadListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_LINK,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.name(), messages.paySlipName(),
				messages.payHeadType(), messages.calculationPeriod(),
				messages.delete() };
	}

	@Override
	protected void executeDelete(ClientPayHead object) {
		AccounterAsyncCallback<ClientPayHead> callback = new AccounterAsyncCallback<ClientPayHead>() {

			public void onException(AccounterException caught) {
			}

			public void onResultSuccess(ClientPayHead result) {
				if (result != null) {
					deleteObject(result);

				}
			}

		};
		Accounter.createGETService().getObjectById(AccounterCoreType.PAY_HEAD,
				object.getID(), callback);
	}

	@Override
	protected Object getColumnValue(ClientPayHead obj, int col) {
		switch (col) {
		case 0:
			return obj.getName();
		case 1:
			return obj.getNameToAppearInPaySlip();
		case 2:
			return ClientPayHead.getPayHeadType(obj.getType());
		case 3:
			return ClientPayHead.getCalculationType(obj.getCalculationType());
		case 4:
			return Accounter.getFinanceImages().delete();
		default:
			break;
		}
		return null;
	}

	@Override
	protected void onClick(ClientPayHead obj, int row, int col) {
		switch (col) {
		case 4:
			showWarnDialog(obj);
			break;
		default:
			break;
		}
	}

	@Override
	public void onDoubleClick(ClientPayHead obj) {
		ReportsRPC.openTransactionView(IAccounterCore.PAY_HEAD, obj.getID());
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { messages.name(), messages.paySlipName(),
				messages.payHeadType(), messages.calculationType(),
				messages.delete() };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { messages.name(), messages.paySlipName(),
				messages.payHeadType(), messages.calculationType(),
				messages.delete() };
	}

}
