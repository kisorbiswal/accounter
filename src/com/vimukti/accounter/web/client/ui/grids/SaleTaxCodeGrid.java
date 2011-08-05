package com.vimukti.accounter.web.client.ui.grids;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTaxRates;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.AddEditSalesTaxCodeView;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;

public class SaleTaxCodeGrid extends ListGrid<ClientTaxRates> {

	private List<Integer> cellsWidth = new ArrayList<Integer>();
	private AddEditSalesTaxCodeView saleTaxCodeView;

	public SaleTaxCodeGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 2) {
			return 15;
		}
		return -1;
	}

	@Override
	protected int getColumnType(int col) {
		switch (col) {
		case 0:
			return ListGrid.COLUMN_TYPE_TEXTBOX;
		case 1:
			return ListGrid.COLUMN_TYPE_DATE;
		case 2:
			return ListGrid.COLUMN_TYPE_IMAGE;
		}
		return -1;

	}

	@Override
	protected Object getColumnValue(ClientTaxRates taxRates, int col) {
		switch (col) {
		case 0:
			return DataUtils.getDiscountString(taxRates.getRate());
		case 1:
			return new ClientFinanceDate(taxRates.getAsOf()).toString();
		case 2:
			return Accounter.getFinanceMenuImages().delete();
			// return "/images/delete.png";
		}
		return null;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { Accounter.constants().rate(),
				Accounter.constants().asOf(), "" };
	}

	@Override
	protected String[] getSelectValues(ClientTaxRates obj, int col) {
		// NOTHING TO DO.
		return null;
	}

	@Override
	protected boolean isEditable(ClientTaxRates obj, int row, int col) {
		return true;
	}

	@Override
	protected void onClick(ClientTaxRates obj, int row, int col) {
		if (col == 2) {
			deleteRecord(obj);
		}
	}

	@Override
	public void onDoubleClick(ClientTaxRates obj) {
		// NOTHING TO DO.
	}

	@Override
	protected void onValueChange(ClientTaxRates obj, int col, Object value) {
		// NOTHING TO DO.
	}

	@Override
	protected int sort(ClientTaxRates obj1, ClientTaxRates obj2, int index) {
		// NOTHING TO DO.
		return 0;
	}

	@Override
	public void editComplete(ClientTaxRates item, Object value, int col) {
		switch (col) {
		case 0:
			String selectedValue = value.toString();
			if (selectedValue != null)
				saleTaxCodeView.validateRateField(selectedValue);
			item.setRate(saleTaxCodeView.getValidRate());

			break;

		case 1:
			ClientFinanceDate d = UIUtils.stringToDate(value.toString());
			List<ClientTaxRates> records = this.getRecords();
			records.remove(item);
			if (saleTaxCodeView.validateDateField(d, records))
				item.setAsOf(d);
			break;

		default:
			break;
		}
		updateData(item);

	}

	public void setCurrentView(AddEditSalesTaxCodeView view) {
		saleTaxCodeView = view;
	}

}
