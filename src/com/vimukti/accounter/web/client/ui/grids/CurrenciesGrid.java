package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.ui.Accounter;

public class CurrenciesGrid extends BaseListGrid<ClientCurrency> {
	public CurrenciesGrid() {
		super(true);
	}

	@Override
	protected int getColumnType(int index) {
		return super.getColumnType(index);
	}

	@Override
	protected Object getColumnValue(ClientCurrency obj, int index) {
		switch (index) {
		case 1:
			return obj.getFormalName();
		case 2:
			return obj.getSymbol();
		default:
			break;
		}
		return null;
	}

	@Override
	protected String[] getSelectValues(ClientCurrency obj, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onValueChange(ClientCurrency obj, int index, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isEditable(ClientCurrency obj, int row, int index) {
		return false;
	}

	@Override
	protected void onClick(ClientCurrency obj, int row, int index) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDoubleClick(ClientCurrency obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected int sort(ClientCurrency obj1, ClientCurrency obj2, int index) {

		switch (index) {
		case 1:
			return obj1.getFormalName().compareTo(obj2.getFormalName());

		case 2:
			return obj1.getSymbol().compareTo(obj2.getSymbol());

		default:
			break;
		}

		return 0;

	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 0)
			return 50;
		if (index == 1)
			return 100;
		if (index == 2)
			return 60;
		return -1;

	}

	@Override
	protected String[] getColumns() {
		return new String[] { Accounter.constants().select(),
				Accounter.constants().countryName(),
				Accounter.constants().currencyCode() };
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { COLUMN_TYPE_CHECK, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_TEXT };
	}

	@Override
	protected void executeDelete(ClientCurrency object) {
		// TODO Auto-generated method stub

	}

}
