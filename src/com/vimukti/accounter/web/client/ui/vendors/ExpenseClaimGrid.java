package com.vimukti.accounter.web.client.ui.vendors;

import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class ExpenseClaimGrid extends BaseListGrid<BillsList> {

	ExpenseClaimView view;

	public ExpenseClaimGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected int getColumnType(int index) {
		// if (index == 0) {
		// return ListGrid.COLUMN_TYPE_CHECK;
		// }
		return ListGrid.COLUMN_TYPE_TEXT;
	}

	public void setView(ExpenseClaimView view) {
		this.view = view;
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_DATE, ListGrid.COLUMN_TYPE_DATE,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT, };

	}

	@Override
	public boolean validateGrid() throws InvalidTransactionEntryException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected String[] getColumns() {
		// TODO Auto-generated method stub
		return new String[] { "Receipt From", "Recepit Date", "Date Enterred",
				"Status", "Amount" };
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void executeDelete(BillsList object) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Object getColumnValue(BillsList obj, int index) {
		switch (index) {
		// case 0:
		// return false;
		case 0:
			return obj.getVendorName();
		case 1:
			return obj.getDate().getTime();
		case 2:
			return obj.getDate().getTime();
		case 3:
			return obj.getStatus();

		case 4:
			return obj.getOriginalAmount();
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
	public void onDoubleClick(BillsList obj) {
		// TODO Auto-generated method stub

	}

}
