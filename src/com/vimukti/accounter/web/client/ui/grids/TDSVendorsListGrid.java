package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.Lists.PayeeList;
import com.vimukti.accounter.web.client.ui.Accounter;

public class TDSVendorsListGrid extends BaseListGrid<PayeeList> {

	private boolean isTdsView;

	public TDSVendorsListGrid(boolean isMultiSelectionEnable, boolean isTdsView) {
		super(isMultiSelectionEnable, true);
		this.isTdsView = isTdsView;

	}

	@Override
	protected int[] setColTypes() {
		if (isTdsView) {
			return new int[] { ListGrid.COLUMN_TYPE_CHECK,
					ListGrid.COLUMN_TYPE_TEXT,
					ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
					ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
					ListGrid.COLUMN_TYPE_DECIMAL_TEXT };
		}
		return new int[] { ListGrid.COLUMN_TYPE_CHECK,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT, ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected void executeDelete(PayeeList object) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Object getColumnValue(PayeeList obj, int index) {
		switch (index) {
		case 0:
			return obj.isActive();
		case 1:
			return obj.getName();
		case 2:
			return obj.getBalance();
		case 3:
			return "0";
		case 4:
			return "0";
		}
		return "";

	}

	@Override
	public void onDoubleClick(PayeeList obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected int getCellWidth(int index) {
		// TODO Auto-generated method stub
		return super.getCellWidth(index);
	}

	@Override
	protected String[] getColumns() {

		String[] colArray = new String[5];
		for (int index = 0; index < colArray.length; index++) {
			switch (index) {
			case 0:
				colArray[index] = Accounter.constants().active();
				break;
			case 1:
				if (getCompany().getPreferences().isChargeSalesTax()) {
					colArray[index] = Accounter.messages().vendorName(
							Global.get().Vendor());
				}
				if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_INDIA) {
					colArray[index] = Accounter.messages().vendorName(
							Global.get().Vendor());
				}
				break;
			case 2:
				colArray[index] = Accounter.constants().originalAmount();
				break;
			case 3:
				colArray[index] = Accounter.constants().percentage();
				break;
			case 4:
				colArray[index] = Accounter.constants().tds();
				break;
			default:
				break;
			}
		}
		return colArray;
	}

}
