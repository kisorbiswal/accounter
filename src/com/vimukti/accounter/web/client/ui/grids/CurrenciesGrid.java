package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.ui.Accounter;

public class CurrenciesGrid extends ListGrid<String>{
	public CurrenciesGrid() {
		super(true);
	}
	public CurrenciesGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected int getColumnType(int index) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected Object getColumnValue(String obj, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String[] getSelectValues(String obj, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onValueChange(String obj, int index, Object value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isEditable(String obj, int row, int index) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onClick(String obj, int row, int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDoubleClick(String obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected int sort(String obj1, String obj2, int index) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getCellWidth(int index) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected String[] getColumns() {
		return new String[]{Accounter.constants().select(), Accounter.constants().currencyName()};
	}
	

}
