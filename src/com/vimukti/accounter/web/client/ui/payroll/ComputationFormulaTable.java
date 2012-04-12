package com.vimukti.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientComputationFormulaFunction;
import com.vimukti.accounter.web.client.ui.edittable.DeleteColumn;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;
import com.vimukti.accounter.web.client.ui.edittable.StringListColumn;

public class ComputationFormulaTable extends
		EditTable<ClientComputationFormulaFunction> {

	String[] functionTypes = { "Add Pay Head", "Subtract Pay Head",
			"Divide Attendance", "Multiply Attendance" };
	List<String> functionTypesList = new ArrayList<String>();

	public ComputationFormulaTable() {
		for (int i = 0; i < functionTypes.length; i++) {
			functionTypesList.add(functionTypes[i]);
		}
		addEmptyRowAtLast();
	}

	@Override
	public void addEmptyRowAtLast() {
		ClientComputationFormulaFunction item = new ClientComputationFormulaFunction();
		add(item);
	}

	@Override
	protected void initColumns() {
		this.addColumn(new StringListColumn<ClientComputationFormulaFunction>() {

			@Override
			protected String getValue(ClientComputationFormulaFunction row) {
				if (row.getFunctionType() == 0) {
					return null;
				}
				return functionTypesList.get(row.getFunctionType() - 1);
			}

			@Override
			protected List<String> getData() {
				return functionTypesList;
			}

			@Override
			protected void setValue(ClientComputationFormulaFunction row,
					String newValue) {
				row.setFunctionType(functionTypesList.indexOf(newValue) + 1);
				update(row);
			}

			@Override
			protected String getColumnName() {
				return messages.functionType();
			}
		});

		this.addColumn(new ComputationFormulaTypeColumn() {
			@Override
			protected String getColumnName() {
				return "";
			}
		});

		this.addColumn(new DeleteColumn<ClientComputationFormulaFunction>());
	}

	@Override
	protected boolean isInViewMode() {
		// TODO Auto-generated method stub
		return false;
	}

}
