package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.ui.Accounter;

public class PayheadCombo extends CustomCombo<ClientPayHead> {

	public PayheadCombo(String title) {
		super(title, false, 1, "payheadcombo");
		getPayHeads();

	}

	private void getPayHeads() {
		Accounter.createPayrollService().getPayheads(0, 0,
				new AsyncCallback<PaginationList<ClientPayHead>>() {

					@Override
					public void onSuccess(PaginationList<ClientPayHead> result) {
						initCombo(result);
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}
				});
	}

	@Override
	protected String getDisplayName(ClientPayHead object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	protected String getColumnData(ClientPayHead object, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

	@Override
	public String getDefaultAddNewCaption() {
		return null;
	}

	@Override
	public void onAddNew() {
		// TODO Auto-generated method stub

	}

}
