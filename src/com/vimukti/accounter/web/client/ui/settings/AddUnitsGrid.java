package com.vimukti.accounter.web.client.ui.settings;

import com.vimukti.accounter.web.client.core.ClientUnit;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;
import com.vimukti.accounter.web.client.ui.edittable.TextEditColumn;

public abstract class AddUnitsGrid extends EditTable<ClientUnit> {

	public AddUnitsGrid() {
	}

	protected void addEmptyRecords() {
		for (int i = 0; i < 4; i++) {
			ClientUnit item = new ClientUnit();
			add(item);
		}
	}

	@Override
	protected void initColumns() {
		this.addColumn(new TextEditColumn<ClientUnit>() {

			@Override
			protected String getValue(ClientUnit row) {
				return row.getType();
			}

			@Override
			protected void setValue(ClientUnit row, String value) {
				row.setType(value);
				updateUnits(row);
			}

			@Override
			protected String getColumnName() {
				return Accounter.constants().unitName();
			}
		});
		this.addColumn(new TextEditColumn<ClientUnit>() {

			@Override
			protected String getValue(ClientUnit row) {
				return String.valueOf(row.getFactor());
			}

			@Override
			protected void setValue(ClientUnit row, String value) {
				row.setFactor(Double.parseDouble(value));
				updateUnits(row);
			}

			@Override
			protected String getColumnName() {
				return Accounter.constants().getFactorName();
			}

		});
	}

	public abstract void updateUnits(ClientUnit clientUnit);
}
