package com.vimukti.accounter.web.client.ui.vendors;

import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.reports.ReportsRPC;

/**
 * 
 * @author Uday Kumar
 * 
 */
public class PreviousClaimGrid extends BaseListGrid<BillsList> {

	PreviousClaimsView view;

	public PreviousClaimGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	public void setView(PreviousClaimsView view) {
		this.view = view;
	}

	@Override
	protected int getColumnType(int col) {
		return ListGrid.COLUMN_TYPE_TEXT;
	}

	@Override
	protected void executeDelete(BillsList billsList) {
		// NOTHING TO DO.
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_DATE, ListGrid.COLUMN_TYPE_DATE,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT };
	}

	@Override
	protected Object getColumnValue(BillsList billsList, int index) {
		switch (index) {
		// case 0:
		// return false;
		case 0:
			return billsList.getVendorName();
		case 1:
			return new ClientFinanceDate(billsList.getDate().getDate());
		case 2:
			return new ClientFinanceDate(billsList.getDueDate().getDate());
		case 3:
			return getstatus(billsList.getExpenseStatus());

		case 4:
			return billsList.getOriginalAmount();
		}
		return null;
	}

	private String getstatus(int status) {
		switch (status) {
		case ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_SAVE:
			return Accounter.constants().draft();
		case ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_DELETE:
			return Accounter.constants().delete();
		case ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_SUBMITED_FOR_APPROVAL:
			return Accounter.constants().submitForApproval();
		case ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_APPROVED:
			return Accounter.constants().approved();
		case ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_DECLINED:
			return Accounter.constants().decline();
		default:
			break;
		}
		return Accounter.constants().draft();
	}

	@Override
	public void onDoubleClick(BillsList billsList) {
		ReportsRPC.openTransactionView(billsList.getType(),
				billsList.getTransactionId());
	}

	@Override
	protected String[] getColumns() {
		return new String[] { Accounter.constants().receiptFrom(),
				Accounter.constants().receiptDate(),
				Accounter.constants().dateEntered(),
				Accounter.constants().status(), Accounter.constants().amount() };
	}


}
