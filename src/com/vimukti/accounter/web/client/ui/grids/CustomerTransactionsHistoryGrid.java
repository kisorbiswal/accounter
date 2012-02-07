package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.TransactionHistory;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.reports.ReportsRPC;

public class CustomerTransactionsHistoryGrid extends
		BaseListGrid<TransactionHistory> {

	protected ClientCustomer selectedCustomer;

	public CustomerTransactionsHistoryGrid() {
		super(false);
	}

	boolean isDeleted;

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_LINK, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_IMAGE, };
	}

	@Override
	protected Object getColumnValue(TransactionHistory transactionHistory,
			int col) {
		switch (col) {
		case 0:
			return UIUtils.getDateByCompanyType(transactionHistory.getDate());
		case 1:
			return transactionHistory.getName();
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
			return DataUtils.getAmountAsStringInCurrency(transactionHistory
					.getAmount(),
					getCompany().getCurrency(selectedCustomer.getCurrency())
							.getSymbol());

			// return transactionHistory.getAmount();

		case 6:
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
		return new String[] { messages.date(), messages.type(), messages.no(),
				messages.memo(), messages.dueDate(), messages.amount(),
				messages.status() };
	}

	@Override
	public void onDoubleClick(TransactionHistory obj) {
		if (Accounter.getUser().canDoInvoiceTransactions())
			ReportsRPC.openTransactionView(obj.getType(),
					obj.getTransactionId());
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 0:
			return 90;
		case 1:
			return -1;
		case 2:
			return 30;
		case 3:
			return -1;
		case 4:
			return 90;
		case 5:
			return 80;

		default:
			return 50;
		}
	}

	@Override
	protected void executeDelete(TransactionHistory object) {
		// NOTHING TO DO.
	}

	public boolean isVoided(TransactionHistory obj) {
		return false;
		// return obj.isVoided();
	}

	public AccounterCoreType getAccounterCoreType(TransactionHistory obj) {

		return UIUtils.getAccounterCoreType(obj.getType());
	}

	public void setSelectedCustomer(ClientCustomer selectedCustomer) {
		this.selectedCustomer = selectedCustomer;
	}

	public ClientCustomer getSelectedCustomer() {
		return selectedCustomer;
	}

	@Override
	protected int sort(TransactionHistory obj1, TransactionHistory obj2,
			int index) {
		switch (index) {
		case 0:
			ClientFinanceDate date = obj1.getDate();
			ClientFinanceDate date2 = obj2.getDate();
			return date.compareTo(date2);
		case 1:
			String name2 = obj1.getName().toLowerCase();
			String name = obj2.getName().toLowerCase();
			return name2.compareTo(name);
		case 2:
			String number = obj1.getNumber();
			String number2 = obj2.getNumber();
			return number.compareTo(number2);
		case 3:
			String memo = obj1.getMemo().toLowerCase();
			String memo2 = obj2.getMemo().toLowerCase();
			return memo.compareTo(memo2);
		case 4:

			ClientFinanceDate dueDate = obj1.getDueDate();
			ClientFinanceDate dueDate2 = obj2.getDueDate();
			return dueDate.compareTo(dueDate2);

		case 5:
			Double amount = obj1.getAmount();
			Double amount2 = obj2.getAmount();
			return amount.compareTo(amount2);
		default:
			break;
		}

		return 0;
	}
}
