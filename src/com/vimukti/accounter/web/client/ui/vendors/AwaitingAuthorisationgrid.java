package com.vimukti.accounter.web.client.ui.vendors;

import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.reports.ReportsRPC;

/**
 * 
 * @author Uday Kumar
 * 
 */
public class AwaitingAuthorisationgrid extends BaseListGrid<BillsList> {
	AwaitingAuthorisationView view;

	public AwaitingAuthorisationgrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	@Override
	protected void executeDelete(BillsList object) {
		// its not using any where

	}

	public void setView(AwaitingAuthorisationView view) {
		this.view = view;
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DATE,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT };
	}

	@Override
	protected Object getColumnValue(BillsList billsList, int index) {
		switch (index) {

		case 0:
			return getstatus(billsList.getExpenseStatus());
		case 1:
			return billsList.getVendorName();
		case 2:
			return new ClientFinanceDate(billsList.getDate().getTime());
		case 3:
			return billsList.getOriginalAmount();
		}
		return null;
	}

	private String getstatus(int status) {
		switch (status) {
		case ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_SAVE:
			return "Draft";
		case ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_DELETE:
			return "Delete";
		case ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_SUBMITED_FOR_APPROVAL:
			return "Submit for Approval";
		case ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_APPROVED:
			return "Approved";
		case ClientCashPurchase.EMPLOYEE_EXPENSE_STATUS_DECLINED:
			return "Decline";
		default:
			break;
		}
		return "Draft";
	}

	@Override
	public void onDoubleClick(BillsList billsList) {
		ReportsRPC.openTransactionView(billsList.getType(),
				billsList.getTransactionId());

	}

	@Override
	public boolean validateGrid() throws InvalidTransactionEntryException {
		// its not using any where
		return false;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { "Status", "Paid To", " Receipt Date", "Total" };
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// its not using any where

	}

}
