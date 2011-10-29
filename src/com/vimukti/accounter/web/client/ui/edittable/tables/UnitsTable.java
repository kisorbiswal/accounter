package com.vimukti.accounter.web.client.ui.edittable.tables;

import java.util.List;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.core.ClientUnit;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.edittable.CheckboxEditColumn;
import com.vimukti.accounter.web.client.ui.edittable.DeleteColumn;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;
import com.vimukti.accounter.web.client.ui.edittable.RenderContext;
import com.vimukti.accounter.web.client.ui.edittable.TextEditColumn;

public class UnitsTable extends EditTable<ClientUnit> {

	@Override
	protected void initColumns() {

		this.addColumn(new CheckboxEditColumn<ClientUnit>() {

			@Override
			protected void onChangeValue(boolean value, ClientUnit obj) {
				onSelectionChanged(obj, value);
			}

			@Override
			public IsWidget getHeader() {
				Label columnHeader = new Label(Accounter.constants()
						.defaultWare());
				return columnHeader;
			}

			@Override
			public void render(IsWidget widget,
					RenderContext<ClientUnit> context) {
				super.render(widget, context);
				((CheckBox) widget).setValue(context.getRow().isDefault());
			}
		});
		this.addColumn(new TextEditColumn<ClientUnit>() {

			@Override
			protected String getValue(ClientUnit row) {
				return row.getType();
			}

			@Override
			protected void setValue(ClientUnit row, String value) {
				row.setType(value);
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
			}

			@Override
			protected String getColumnName() {
				return Accounter.constants().factor();
			}

		});

		this.addColumn(new DeleteColumn<ClientUnit>());
	}

	private void onSelectionChanged(ClientUnit obj, boolean isChecked) {

		List<ClientUnit> records = getSelectedRecords(0);
		for (ClientUnit contact : records) {
			int index = indexOf(contact);
			checkColumn(index, 0, false);
			contact.setDefault(false);
		}

		int row = indexOf(obj);
		if (isChecked) {
			update(obj);
			obj.setDefault(true);
		}
		super.checkColumn(row, 0, isChecked);
	}

	public int indexOf(ClientUnit selectedObject) {
		return getAllRows().indexOf(selectedObject);
	}

	public List<ClientUnit> getRecords() {
		return getAllRows();
	}

	public boolean isEmpty() {
		return false;
	}

	public void validate(ValidationResult result) {
	}

	public ClientUnit getDefaultUnit() {
		List<ClientUnit> allRows = getAllRows();
		for (ClientUnit unit : allRows) {
			if (unit.isDefault()) {
				return unit;
			}
		}
		return null;
	}
}
