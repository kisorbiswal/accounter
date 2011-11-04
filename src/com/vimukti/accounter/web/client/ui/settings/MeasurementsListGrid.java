package com.vimukti.accounter.web.client.ui.settings;

import java.util.List;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientMeasurement;
import com.vimukti.accounter.web.client.core.ClientUnit;
import com.vimukti.accounter.web.client.exception.AccounterException;
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
		case 4:
			return ListGrid.COLUMN_TYPE_IMAGE;
		default:
			return 0;
		}
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT, ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected String[] getColumns() {
		return new String[] { Accounter.constants().measurementName(),
				Accounter.constants().measurementDescription(),
				Accounter.constants().unitName(),
				Accounter.constants().getFactorName(),
				Accounter.constants().delete() };
	}

	@Override
	protected void executeDelete(ClientMeasurement object) {

		AccounterAsyncCallback<ClientMeasurement> callback = new AccounterAsyncCallback<ClientMeasurement>() {

			public void onException(AccounterException caught) {
			}

			public void onResultSuccess(ClientMeasurement result) {
				if (result != null) {
					deleteObject(result);
				}
			}
		};
		Accounter.createGETService().getObjectById(
				AccounterCoreType.MEASUREMENT, object.getID(), callback);

	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 4:
			return 40;
		default:
			break;
		}
		return 0;
	}

	@Override
	protected void onClick(ClientMeasurement obj, int row, int col) {
		switch (col) {
		case 4:
			showWarnDialog(obj);
			break;

		default:
			break;
		}
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
		case 4:
			return Accounter.getFinanceMenuImages().delete();
		default:
			break;
		}
		return null;
	}

	private double getDefaultUnitFactor() {
		for (ClientUnit unit : this.units) {
			if (unit.isDefault()) {
				return unit.getFactor();
			}
		}
		return 1;
	}

	private String getDefaultUnitType() {
		for (ClientUnit unit : this.units) {
			if (unit.isDefault()) {
				return unit.getType();
			}
		}
		return "";
	}

	@Override
	public void onDoubleClick(ClientMeasurement obj) {
		AddMeasurementAction action = new AddMeasurementAction(Accounter
				.constants().measurement());
		action.run(obj, false);
	}
}
