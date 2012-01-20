package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.reports.ReportsRPC;

public class DraftsListGrid extends BaseListGrid<ClientTransaction> {

	public DraftsListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { COLUMN_TYPE_DATE, COLUMN_TYPE_DECIMAL_TEXT,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_DECIMAL_TEXT };
	}

	@Override
	protected void executeDelete(ClientTransaction object) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Object getColumnValue(ClientTransaction obj, int index) {
		switch (index) {
		case 0:
			return UIUtils.getDateByCompanyType(new ClientFinanceDate(obj
					.getTransactionDate()));
		case 1:
			return UIUtils.getTransactionTypeName(obj.getType());
		case 2:
			return obj.getName() == null ? " " : obj.getName();
		case 3:
			return DataUtils.amountAsStringWithCurrency(obj.getNetAmount(),
					obj.getCurrency());
		case 4:
			return DataUtils.amountAsStringWithCurrency(obj.getTotal(),
					obj.getCurrency());
		}
		return null;
	}

	@Override
	public void onDoubleClick(ClientTransaction obj) {
		AccounterAsyncCallback<ClientTransaction> callback = new AccounterAsyncCallback<ClientTransaction>() {

			@Override
			public void onException(AccounterException caught) {
			}

			@Override
			public void onResultSuccess(ClientTransaction result) {
				if (result != null) {
					ReportsRPC.openTransactionView(result);
				}
			}

		};
		Accounter.createGETService().getObjectById(obj.getObjectType(), obj.id,
				callback);
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.transactionDate(), messages.type(),
				messages.payee(), messages.totalAmount() };
	}

}
