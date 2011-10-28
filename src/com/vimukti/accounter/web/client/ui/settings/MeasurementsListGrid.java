package com.vimukti.accounter.web.client.ui.settings;

import java.util.List;

import com.vimukti.accounter.web.client.core.ClientMeasurement;
import com.vimukti.accounter.web.client.core.ClientUnit;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class MeasurementsListGrid extends BaseListGrid<ClientMeasurement> {

	private List<ClientUnit> units;

	public MeasurementsListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	@Override
	protected int getColumnType(int col) {
		switch (col) {
		case 0:
			return ListGrid.COLUMN_TYPE_TEXT;
		case 1:
			return ListGrid.COLUMN_TYPE_TEXT;
		case 2:
			return ListGrid.COLUMN_TYPE_TEXT;
		case 3:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXT;
		default:
			return 0;
		}
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT };
	}

	@Override
	protected String[] getColumns() {
		return new String[] { Accounter.constants().measurementName(),
				Accounter.constants().measurementDescription(),
				Accounter.constants().unitName(),
				Accounter.constants().getFactorName() };
	}

	@Override
	protected void executeDelete(ClientMeasurement object) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Object getColumnValue(ClientMeasurement obj, int index) {
		switch (index) {
		case 0:
			return obj.getName();
		case 1:
			return obj.getDesctiption();
		case 2:
			this.units = obj.getUnits();
			return getDefaultUnitType();
		case 3:
			this.units = obj.getUnits();
			return getDefaultUnitFactor();
		default:
			break;
		}
		return null;
	}

	private double getDefaultUnitFactor() {
		for (ClientUnit unit : this.units) {
			if (unit.isDefault()) {
				return unit.getFactor();
			} else
				return 1;
		}
		return 0;
	}

	private String getDefaultUnitType() {
		for (ClientUnit unit : this.units) {
			if (unit.isDefault()) {
				return unit.getType();
			} else
				return "";
		}
		return null;
	}

	@Override
	public void onDoubleClick(ClientMeasurement obj) {
		// TODO Auto-generated method stub

	}

}
