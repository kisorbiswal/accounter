package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.core.client.GWT;
import com.vimukti.accounter.core.Measurement;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class UnitsListGird extends ListGrid<Measurement> {

	MesurementListView view;
	AccounterConstants messages = Accounter.constants();

	public UnitsListGird(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.getMeasurmentName(),
				messages.getMeasurmentDescription(), messages.getUnitName() };
	}

	@Override
	protected int getColumnType(int col) {
		return ListGrid.COLUMN_TYPE_TEXT;
	}

	@Override
	protected int getCellWidth(int index) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean validateGrid() throws InvalidTransactionEntryException {
		// TODO Auto-generated method stub
		return false;
	}

	public void setView(MesurementListView preAddedListView) {
		this.view = preAddedListView;
	}

	@Override
	protected Object getColumnValue(Measurement obj, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String[] getSelectValues(Measurement obj, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onValueChange(Measurement obj, int index, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isEditable(Measurement obj, int row, int index) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onClick(Measurement obj, int row, int index) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDoubleClick(Measurement obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected int sort(Measurement obj1, Measurement obj2, int index) {
		// TODO Auto-generated method stub
		return 0;
	}
}
