package com.vimukti.accounter.web.client.ui.settings;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientWarehouse;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class WarehouseListGrid extends BaseListGrid<ClientWarehouse> {

	public WarehouseListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	@Override
	protected int getColumnType(int index) {
		switch (index) {
		case 0:
			return ListGrid.COLUMN_TYPE_TEXT;
		case 1:
			return ListGrid.COLUMN_TYPE_TEXT;
		case 2:
			return ListGrid.COLUMN_TYPE_TEXT;
		default:
			return 0;
		}
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT };
	}

	@Override
	protected String[] getColumns() {
		return new String[] { Accounter.constants().warehouseCode(),
				Accounter.constants().warehouseName(),
				Accounter.constants().ddiNumber() };
	}

	@Override
	protected void executeDelete(ClientWarehouse object) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Object getColumnValue(ClientWarehouse obj, int index) {
		switch (index) {
		case 0:
			return obj.getWarehouseCode();
		case 1:
			return obj.getName();
		case 2:
			return obj.getDDINumber();

		default:
			break;
		}
		return null;
	}

	@Override
	public void onDoubleClick(ClientWarehouse obj) {
		AccounterAsyncCallback<ClientWarehouse> callback = new AccounterAsyncCallback<ClientWarehouse>() {

			public void onException(AccounterException caught) {
			}

			public void onResultSuccess(ClientWarehouse result) {
				if (result != null) {
					ActionFactory.getWareHouseViewAction().run(result, false);
				}
			}

		};
		Accounter.createGETService().getObjectById(AccounterCoreType.WAREHOUSE,
				obj.getID(), callback);
	}

}
