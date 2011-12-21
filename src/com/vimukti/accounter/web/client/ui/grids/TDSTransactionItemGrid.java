package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientTDSTransactionItem;

public class TDSTransactionItemGrid extends
		BaseListGrid<ClientTDSTransactionItem> {

	public TDSTransactionItemGrid() {
		super(false, true);
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_CHECK,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT };
	}

	@Override
	protected void executeDelete(ClientTDSTransactionItem object) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Object getColumnValue(ClientTDSTransactionItem obj, int index) {

		switch (index) {
		case 0:
			return null;
		case 1:
			return null;
		case 2:
			return null;
		case 3:
			return null;
		case 4:
			return null;
		default:
			break;
		}
		return null;
	}

	@Override
	public void onDoubleClick(ClientTDSTransactionItem obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String[] getColumns() {
		String[] colArray = new String[5];
		for (int index = 0; index < colArray.length; index++) {
			switch (index) {
			case 0:
				colArray[index] = messages.applied();
				break;
			case 1:
				colArray[index] = Global.get().Vendor();
				break;
			case 2:
				colArray[index] = "tax amount";
				break;
			case 3:
				colArray[index] = messages.totalAmount();
				break;
			case 4:
				colArray[index] = messages.dateEntered();
				break;

			default:
				break;
			}
		}
		return colArray;

	}

}
