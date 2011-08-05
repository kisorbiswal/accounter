package com.vimukti.accounter.web.client.ui.settings;

import com.vimukti.accounter.web.client.core.ClientMeasurement;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class UnitsListGird extends ListGrid<ClientMeasurement> {

	MesurementListView view;
	AccounterConstants messages = Accounter.constants();

	public UnitsListGird(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.measurementName(),
				messages.measurementDescription(), messages.getUnitName() };
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

	public void setView(MesurementListView preAddedListView) {
		this.view = preAddedListView;
	}

	@Override
	protected Object getColumnValue(ClientMeasurement obj, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String[] getSelectValues(ClientMeasurement obj, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onValueChange(ClientMeasurement obj, int index, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isEditable(ClientMeasurement obj, int row, int index) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onClick(ClientMeasurement obj, int row, int index) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDoubleClick(ClientMeasurement obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected int sort(ClientMeasurement obj1, ClientMeasurement obj2, int index) {
		// TODO Auto-generated method stub
		return 0;
	}
}
