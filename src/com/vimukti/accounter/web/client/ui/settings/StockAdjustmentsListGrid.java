package com.vimukti.accounter.web.client.ui.settings;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientStockAdjustment;
import com.vimukti.accounter.web.client.core.ClientUnit;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class StockAdjustmentsListGrid extends BaseListGrid<StockAdjustmentList> {

	public StockAdjustmentsListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_LINK, ListGrid.COLUMN_TYPE_TEXT };
	}

	@Override
	protected void executeDelete(StockAdjustmentList object) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Object getColumnValue(StockAdjustmentList obj, int index) {
		switch (index) {
		case 0:
			return obj.getWareHouse();
		case 1:
			return obj.getItem();
		case 2:
			StringBuffer result = new StringBuffer();
			ClientUnit unit = getCompany().getUnitById(
					obj.getQuantity().getUnit());
			result.append(obj.getQuantity().getValue());
			result.append(" ");
			result.append(unit.getName());
			return result.toString();
		}
		return null;
	}

	@Override
	public void onDoubleClick(StockAdjustmentList obj) {
		AccounterAsyncCallback<ClientStockAdjustment> callback = new AccounterAsyncCallback<ClientStockAdjustment>() {

			public void onException(AccounterException caught) {
			}

			public void onResultSuccess(ClientStockAdjustment result) {
				if (result != null) {
					ActionFactory.getStockAdjustmentAction().run(result, false);
				}
			}

		};
		Accounter.createGETService().getObjectById(
				AccounterCoreType.STOCK_ADJUSTMENT, obj.getStockAdjustment(),
				callback);
	}

	@Override
	protected String[] getColumns() {
		return new String[] { Accounter.messages().wareHouse(),
				Accounter.messages().itemName(),
				Accounter.messages().adjustmentQty() };
	}

}
