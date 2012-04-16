package com.vimukti.accounter.web.client.ui.payroll;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientPayrollUnit;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;

public class PayRollUnitCombo extends CustomCombo<ClientPayrollUnit> {

	private boolean isAddNew;
	protected ArrayList<ClientPayrollUnit> list = new ArrayList<ClientPayrollUnit>();

	public PayRollUnitCombo(String title, String styleName) {
		super(title, "payRollUnitCombo");
		getPayrollUnits();
	}

	private void getPayrollUnits() {
		Accounter.createPayrollService().getPayrollUnitsList(0, 0,
				new AsyncCallback<PaginationList<ClientPayrollUnit>>() {

					@Override
					public void onSuccess(
							PaginationList<ClientPayrollUnit> result) {
						list = result;
						initCombo(result);
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}
				});

	}

	@Override
	protected String getDisplayName(ClientPayrollUnit object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {

		isAddNew = true;
		NewPayrollUnitAction action = ActionFactory.getNewPayrollUnitAction();
		action.setCallback(new ActionCallback<ClientPayrollUnit>() {

			@Override
			public void actionResult(ClientPayrollUnit result) {
				if (result.getDisplayName() != null)
					isAddNew = false;
				list.add(result);
				addItemThenfireEvent(result);
			}
		});

		action.run(null, true);
	}

	@Override
	protected String getColumnData(ClientPayrollUnit object, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

	@Override
	protected void selectionFaildOnClose() {
		if (isAddNew) {
			return;
		}
	}

	@Override
	public String getDefaultAddNewCaption() {

		return messages.payrollUnit();
	}

}
