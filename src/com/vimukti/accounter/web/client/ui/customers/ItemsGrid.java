package com.vimukti.accounter.web.client.ui.customers;

import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class ItemsGrid extends BaseListGrid<ClientTransactionItem> {

	public static final int TYPE_ITEM = 1;
	public static final int TYPE_COMMENT = 2;
	public static final int TYPE_SALESTAX = 3;
	public static final int TYPE_ACCOUNT = 4;
	public static final int TYPE_VATITEM = 5;
	public static final int TYPE_NONE = 0;
	public static final int TYPE_SERVICE = 6;

	public ItemsGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	@Override
	protected void executeDelete(ClientTransactionItem object) {
		// TODO Auto-generated method stub

	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT };
	}

	@Override
	protected Object getColumnValue(ClientTransactionItem obj, int index) {
		switch (index) {
		case 0:
			return getNameValue(obj);
		case 1:
			return obj.getQuantity();
		case 2:
			return DataUtils.getAmountAsString(obj.getUnitPrice());
		case 3:
			return DataUtils.getAmountAsString(obj.getLineTotal());
		default:
			return null;
		}
	}

	protected String getNameValue(ClientTransactionItem item) {
		switch (item.getType()) {

		case TYPE_ITEM:
			ClientItem itm = FinanceApplication.getCompany().getItem(
					item.getItem());
			return itm != null ? itm.getName() : "";
		case TYPE_ACCOUNT:
			ClientAccount account = FinanceApplication.getCompany().getAccount(
					item.getAccount());
			return account != null ? account.getDisplayName() : "";
		case TYPE_SALESTAX:
			ClientTAXItem vatItem = FinanceApplication.getCompany().getTaxItem(
					item.getVatItem());
			return vatItem != null ? vatItem.getDisplayName() : "";
		case TYPE_COMMENT:
			return item.getDescription() != null ? item.getDescription() : "";
		case TYPE_SERVICE:
			ClientItem serviceItm = FinanceApplication.getCompany().getItem(
					item.getItem());
			return serviceItm != null ? serviceItm.getName() : "";
		default:
			break;
		}
		return null;
	}

	@Override
	public void onDoubleClick(ClientTransactionItem obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean validateGrid() throws InvalidTransactionEntryException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { FinanceApplication.getCustomersMessages().name(),
				FinanceApplication.getCustomersMessages().quantity(),
				FinanceApplication.getCustomersMessages().unitPrice(),
				FinanceApplication.getCustomersMessages().total() };
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		// case 0:
		// return 70;
		case 1:
			return 55;
		case 2:
			return 65;
		case 3:
			return 70;
		default:
			return 0;
		}
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	public AccounterCoreType getType() {
		return AccounterCoreType.ITEM;
	}

}
