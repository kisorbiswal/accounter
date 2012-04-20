package com.vimukti.accounter.web.client.ui.payroll;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.edittable.AbstractDropDownTable;

public class EmployeeDropDownTable extends
		AbstractDropDownTable<ClientEmployee> {

	private static List<ClientEmployee> list = new ArrayList<ClientEmployee>();

	public EmployeeDropDownTable(boolean isAddNewRequired) {
		super(list, isAddNewRequired);
		initList();
	}

	private void initList() {
		Accounter.createPayrollService().getEmployees(0, 0,
				new AsyncCallback<PaginationList<ClientEmployee>>() {

					@Override
					public void onSuccess(PaginationList<ClientEmployee> result) {
						list = result;
						reInitData();
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}
				});
	}

	@Override
	public List<ClientEmployee> getTotalRowsData() {
		return list;
	}

	@Override
	protected ClientEmployee getAddNewRow() {
		ClientEmployee employee = new ClientEmployee();
		employee.setName(messages.comboDefaultAddNew(messages.employee()));
		return employee;
	}

	@Override
	public void initColumns() {
		TextColumn<ClientEmployee> textColumn = new TextColumn<ClientEmployee>() {

			@Override
			public String getValue(ClientEmployee object) {
				return object.getDisplayName();
			}
		};
		this.addColumn(textColumn);
	}

	@Override
	protected boolean filter(ClientEmployee t, String string) {
		return getDisplayValue(t).toLowerCase().startsWith(string);
	}

	@Override
	protected String getDisplayValue(ClientEmployee value) {
		return value.getDisplayName();
	}

	@Override
	protected void addNewItem(String text) {
		NewEmployeeAction action = ActionFactory.getNewEmployeeAction();
		action.setCallback(new ActionCallback<ClientEmployee>() {

			@Override
			public void actionResult(ClientEmployee result) {
				selectRow(result);

			}
		});
		action.run(null, true);
	}

	@Override
	protected void addNewItem() {
		addNewItem("");

	}

}
