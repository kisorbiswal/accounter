package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.core.ClientTaxRates;
import com.vimukti.accounter.web.client.ui.Accounter;

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
		return 0;
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	protected Object getColumnValue(ClientTaxRates obj, int col) {
		return null;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { Accounter.constants().rate(),
				Accounter.constants().asOf() };
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	protected String[] getSelectValues(ClientTaxRates obj, int col) {
		return null;
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	protected boolean isEditable(ClientTaxRates obj, int row, int col) {
		return false;
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	protected void onClick(ClientTaxRates obj, int row, int col) {

	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	public void onDoubleClick(ClientTaxRates obj) {
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	protected void onValueChange(ClientTaxRates obj, int col, Object value) {
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	protected int sort(ClientTaxRates obj1, ClientTaxRates obj2, int index) {
		return 0;
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	public boolean validateGrid() {
		return false;
	}

}
