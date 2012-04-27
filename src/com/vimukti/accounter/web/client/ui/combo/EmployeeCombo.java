package com.vimukti.accounter.web.client.ui.combo;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.payroll.NewEmployeeAction;

public class EmployeeCombo extends CustomCombo<ClientEmployee> {

	private long employeeId;
	private boolean isItemsAdded;

	public EmployeeCombo(String title) {
		this(title, false);
		getEmployees();
	}

	private void getEmployees() {
		isItemsAdded = false;
		Accounter.createPayrollService().getEmployees(0, 0,
				new AsyncCallback<PaginationList<ClientEmployee>>() {

					@Override
					public void onSuccess(PaginationList<ClientEmployee> result) {
						initCombo(result);
						EmployeeCombo.this.isItemsAdded = true;
						setSelectedItem();
					}

					@Override
					public void onFailure(Throwable caught) {

					}
				});
	}

	public EmployeeCombo(String title, boolean b) {
		super(title, b, 1, "employeecombo");
		getEmployees();
	}

	@Override
	protected String getDisplayName(ClientEmployee object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	protected String getColumnData(ClientEmployee object, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages.employee();
	}

	@Override
	public void onAddNew() {
		NewEmployeeAction action = ActionFactory.getNewEmployeeAction();
		action.setCallback(new ActionCallback<ClientEmployee>() {

			@Override
			public void actionResult(ClientEmployee result) {
				addItemThenfireEvent(result);
			}
		});

		action.run(null, true);
	}

	public void setSelectedEmployee(long employeeId) {
		this.employeeId = employeeId;
		if (isItemsAdded) {
			setSelectedItem();
		}
	}

	private void setSelectedItem() {
		if (this.employeeId != 0) {
			for (ClientEmployee emp : getComboItems()) {
				if (emp.getID() == employeeId) {
					setComboItem(emp);
					break;
				}
			}
		}
	}

}
