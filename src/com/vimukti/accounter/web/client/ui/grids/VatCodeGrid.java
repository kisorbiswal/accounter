package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.core.ClientTaxRates;
import com.vimukti.accounter.web.client.ui.FinanceApplication;

public class VatCodeGrid extends ListGrid<ClientTaxRates> {

	public VatCodeGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	@Override
	protected int getCellWidth(int index) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getColumnType(int col) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected Object getColumnValue(ClientTaxRates obj, int col) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { FinanceApplication.getVATMessages().rate(),
				FinanceApplication.getVATMessages().asof() };
	}

	@Override
	protected String[] getSelectValues(ClientTaxRates obj, int col) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean isEditable(ClientTaxRates obj, int row, int col) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onClick(ClientTaxRates obj, int row, int col) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDoubleClick(ClientTaxRates obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onValueChange(ClientTaxRates obj, int col, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	protected int sort(ClientTaxRates obj1, ClientTaxRates obj2, int index) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean validateGrid() {
		// TODO Auto-generated method stub
		return false;
	}

}
