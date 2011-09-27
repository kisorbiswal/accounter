package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.Lists.ClientTDSInfo;
import com.vimukti.accounter.web.client.ui.Accounter;

public class TDSVendorsListGrid extends BaseListGrid<ClientTDSInfo> {

	private boolean isTdsView;

	public TDSVendorsListGrid(boolean isMultiSelectionEnable, boolean isTdsView) {
		super(isMultiSelectionEnable, true);
		this.isTdsView = isTdsView;

	}

	@Override
	protected int[] setColTypes() {
		// if (true) {
		return new int[] { ListGrid.COLUMN_TYPE_CHECK,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT };
		// } else {
		// return new int[] { ListGrid.COLUMN_TYPE_CHECK,
		// ListGrid.COLUMN_TYPE_TEXT,
		// ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
		// ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
		// ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
		// ListGrid.COLUMN_TYPE_IMAGE };
		// }

	}

	@Override
	protected void executeDelete(ClientTDSInfo object) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Object getColumnValue(ClientTDSInfo obj, int index) {
		switch (index) {
		case 0:
			return obj.getVendor().isActive();
		case 1:
			return obj.getVendor().getName();

		case 2:
			return obj.getDate().toString();
		case 3:
			return obj.getOrginalBalance();
		case 4:
			return obj.getPayment();
		case 5:
			return amountAsString(getCompany().getTAXItem(
					obj.getVendor().getTaxItemCode()).getTaxRate())
					+ "%";
		case 6:
			return amountAsString(obj.getTdsAmount());
		default:
			break;
		}
		return null;

	}

	@Override
	public void onDoubleClick(ClientTDSInfo obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 0)
			return 20;
		else if (index == 3)
			return 20;
		else
			return 50;
	}

	@Override
	protected String[] getColumns() {

		String[] colArray = new String[7];
		for (int index = 0; index < colArray.length; index++) {
			switch (index) {
			case 0:
				colArray[index] = Accounter.constants().active();
				break;
			case 1:
				colArray[index] = Accounter.messages().vendorName(
						Global.get().Vendor());
				break;

			case 2:
				colArray[index] = Accounter.constants().date();
				break;
			case 3:
				colArray[index] = Accounter.constants().originalAmount();
				break;
			case 4:
				colArray[index] = Accounter.constants().payment();
				break;
			case 5:
				colArray[index] = Accounter.constants().percentage();
				break;
			case 6:
				colArray[index] = Accounter.constants().tds();
				break;
			default:
				break;
			}
		}
		return colArray;
	}

}
