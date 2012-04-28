package com.vimukti.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientComputaionFormulaFunction;
import com.vimukti.accounter.web.client.ui.edittable.DeleteColumn;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;
import com.vimukti.accounter.web.client.ui.edittable.StringListColumn;

public class ComputationFormulaTable extends
		EditTable<ClientComputaionFormulaFunction> {

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
		ClientComputaionFormulaFunction item = new ClientComputaionFormulaFunction();
		add(item);
	}

	@Override
	protected void initColumns() {
		this.addColumn(new StringListColumn<ClientComputaionFormulaFunction>() {

			@Override
			protected String getValue(ClientComputaionFormulaFunction row) {
				if (row.getFunctionType() == 0) {
					String string = functionTypesList.get(0);
					row.setFunctionType(functionTypesList.indexOf(string) + 1);
					return string;
				}
				return functionTypesList.get(row.getFunctionType() - 1);
			}

			@Override
			protected List<String> getData() {
				return functionTypesList;
			}

			@Override
			protected void setValue(ClientComputaionFormulaFunction row,
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

		this.addColumn(new DeleteColumn<ClientComputaionFormulaFunction>());
	}

	@Override
	protected boolean isInViewMode() {
		// TODO Auto-generated method stub
		return false;
	}

}
