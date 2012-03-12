package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientReminder;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.reports.ReportsRPC;

public class RemindersListGrid extends BaseListGrid<ClientReminder> {

	public RemindersListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
		this.getElement().setId("RemindersListGrid");
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_LINK,
				ListGrid.COLUMN_TYPE_LINK, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT };
	}

	@Override
	protected void executeDelete(ClientReminder object) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Object getColumnValue(ClientReminder obj, int index) {
		ClientTransaction transaction = obj.getRecurringTransaction()
				.getTransaction();
		switch (index) {
		case 0:
			return obj.getName();
		case 1:
			return messages.editAndCreate();
		case 2:
			return UIUtils.getDateByCompanyType(new ClientFinanceDate(obj
					.getTransactionDate()));
		case 3:
			return Utility.getTransactionName(transaction.getType());
		case 4:
			return DataUtils.amountAsStringWithCurrency(transaction.getTotal(),
					transaction.getCurrency());
		}
		return null;
	}

	@Override
	public void onDoubleClick(ClientReminder obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.name(), "", messages.transactionDate(),
				messages.transactionType(), messages.amount() };
	}

	@Override
	protected void onClick(final ClientReminder obj, int row, int col) {
		if (!isCanOpenTransactionView(0, obj.getRecurringTransaction()
				.getTransaction().getType())) {
			return;
		}
		if (col == 1) {
			AccounterAsyncCallback<ClientTransaction> callBack = new AccounterAsyncCallback<ClientTransaction>() {

				@Override
				public void onException(AccounterException exception) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onResultSuccess(ClientTransaction result) {
					if (result != null) {
						result.setTobeDeleteReminder(obj.getID());
						ReportsRPC.openTransactionView(result);
					}
				}
			};
			Accounter.createHomeService().getTransactionToCreate(
					obj.getRecurringTransaction(), obj.getTransactionDate(),
					callBack);
		} else if (col == 0) {
			ReportsRPC.openTransactionView(obj.getRecurringTransaction()
					.getTransaction().getType(), obj.getRecurringTransaction()
					.getTransaction().getID());
		}
	}
}
