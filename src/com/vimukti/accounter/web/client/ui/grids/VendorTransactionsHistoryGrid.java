package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.reports.TransactionHistory;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.reports.ReportsRPC;

public class VendorTransactionsHistoryGrid extends
		BaseListGrid<TransactionHistory> {

	protected ClientVendor selectedVendor;

	public VendorTransactionsHistoryGrid() {
		super(false);
	}

	boolean isDeleted;

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_LINK,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_IMAGE, };
	}

	@Override
	protected Object getColumnValue(TransactionHistory transactionHistory,
			int col) {
		switch (col) {
		case 0:
			return UIUtils.getDateByCompanyType(transactionHistory.getDate());
		case 1:
			return Utility.getTransactionName((transactionHistory.getType()));
		case 2:
			return transactionHistory.getNumber();
		case 3:
			return transactionHistory.getMemo();
		case 4:

			if (transactionHistory.getDueDate() != null)
				return UIUtils.getDateByCompanyType(transactionHistory
						.getDueDate());
			else
				return "";

		case 5:
			return transactionHistory.getAmount();

		case 6:
			ClientAccount account = getCompany().getAccount(
					transactionHistory.getAccType());
			if (account != null) {
				return account.getName();
			} else
				return "";
		case 7:
			if (!transactionHistory.getIsVoid())
				return Accounter.getFinanceImages().notvoid();
			else
				return Accounter.getFinanceImages().voided();
		default:
			break;
		}
		return null;
	}

	@Override
	protected String[] getColumns() {
		messages = messages;
		return new String[] { messages.date(), messages.type(), messages.no(),
				messages.memo(), messages.dueDate(), messages.amount(),
				messages.Account(), messages.status() };
	}

	@Override
	public void onDoubleClick(TransactionHistory obj) {
		if (Accounter.getUser().canDoInvoiceTransactions())
			ReportsRPC.openTransactionView(obj.getType(),
					obj.getTransactionId());
	}

	protected void onClick(TransactionHistory obj, int row, int col) {
		onDoubleClick(obj);
	}

	@Override
	public void addEmptyMessage(String msg) {
		super.addEmptyMessage(msg);
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 0:
			return 80;
		case 1:
			return 120;
		case 2:
			return 30;
		case 3:
			return -1;
		case 4:
			return 80;
		case 5:
			return 80;
		case 6:
			return 80;

		default:
			return 40;
		}
	}

	@Override
	protected void executeDelete(TransactionHistory object) {
	}

	public boolean isVoided(TransactionHistory obj) {
		return false;
	}

	public AccounterCoreType getAccounterCoreType(TransactionHistory obj) {

		return UIUtils.getAccounterCoreType(obj.getType());
	}

	@Override
	public void editComplete(TransactionHistory item, Object value, int col) {

		if (col == 0) {
		}
		super.editComplete(item, value, col);
	}

	public void setSelectedVendor(ClientVendor selectedVendor) {
		this.selectedVendor = selectedVendor;
	}

	public ClientVendor getSelectedVendor() {
		return selectedVendor;
	}

	@Override
	protected void addColumnData(TransactionHistory obj, int row, int col) {
		super.addColumnData(obj, row, col);
	}

	@Override
	public void addData(TransactionHistory obj) {
		super.addData(obj);

	}

}
