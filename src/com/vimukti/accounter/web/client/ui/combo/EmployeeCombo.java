package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.payroll.NewEmployeeAction;

public class EmployeeCombo extends CustomCombo<ClientEmployee> {

	public EmployeeCombo(String title) {
		this(title, false);
		List<ClientEmployee> employees = new ArrayList<ClientEmployee>(
				getCompany().getEmployees());
		initCombo(employees);
	}

	public EmployeeCombo(String title, boolean b) {
		super(title, b, 1, "employeecombo");
		List<ClientEmployee> employees = new ArrayList<ClientEmployee>(
				getCompany().getEmployees());
		initCombo(employees);
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

}
