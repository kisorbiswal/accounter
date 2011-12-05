package com.vimukti.accounter.web.client.ui;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class DashboardOweGrid extends ListGrid<ClientPayee> {

	public DashboardOweGrid() {
		super(false);
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
		return -1;
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
		if (obj.isCustomer()) {
			if (Accounter.getUser().canDoInvoiceTransactions()) {
				AccounterAsyncCallback<ClientCustomer> callback = new AccounterAsyncCallback<ClientCustomer>() {

					public void onException(AccounterException caught) {
					}

					public void onResultSuccess(ClientCustomer result) {
						if (result != null) {
							ActionFactory.getStatementReport(false,
									result.getID()).run();
						}
					}

				};
				Accounter.createGETService().getObjectById(
						AccounterCoreType.CUSTOMER, obj.getID(), callback);
			}
		} else if (obj.isVendor()) {
			if (Accounter.getUser().canDoInvoiceTransactions()) {
				AccounterAsyncCallback<ClientPayee> callback = new AccounterAsyncCallback<ClientPayee>() {

					public void onException(AccounterException caught) {
					}

					public void onResultSuccess(ClientPayee result) {
						if (result != null) {
							if (result instanceof ClientVendor) {
								ActionFactory.getStatementReport(true,
										result.getID()).run();
							} else if (result instanceof ClientTAXAgency) {
								ActionFactory.getNewTAXAgencyAction().run(
										(ClientTAXAgency) result, false);
							}

						}
					}

				};
				if (obj.getType() == ClientPayee.TYPE_VENDOR) {
					Accounter.createGETService().getObjectById(
							AccounterCoreType.VENDOR, obj.getID(), callback);
				} else if (obj.getType() == ClientPayee.TYPE_TAX_AGENCY) {
					Accounter.createGETService().getObjectById(
							AccounterCoreType.TAXAGENCY, obj.getID(), callback);
				}
			}
		}
	}

	@Override
	protected int sort(ClientPayee obj1, ClientPayee obj2, int index) {
		return 0;
	}

}
