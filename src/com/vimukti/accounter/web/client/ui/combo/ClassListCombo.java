package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.ClientAccounterClass;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CreateClassDialog;

public class ClassListCombo extends CustomCombo<ClientAccounterClass> {

	private ValueCallBack<ClientAccounterClass> newClassHandler;

	public ClassListCombo(String title) {
		super(title, false, 1);
	}

	public ClassListCombo(String title, boolean isAddNewRequired) {
		super(title, isAddNewRequired, 1);
		initCombo(getCompany().getAccounterClasses());
	}

	@Override
	protected String getDisplayName(ClientAccounterClass object) {
		if (object != null)
			return object.getClassName() != null ? object.getClassName() : "";
		else
			return "";
	}

	@Override
	protected String getColumnData(ClientAccounterClass object, int row, int col) {
		switch (col) {
		case 0:
			return object.getClassName();
		}
		return null;
	}

	@Override
	public String getDefaultAddNewCaption() {
		return Accounter.constants().addNewTrackClass();
	}

	@Override
	public void onAddNew() {
		CreateClassDialog classDialog = new CreateClassDialog(null,Accounter.constants().createClass(), "");
		classDialog.addSuccessCallback(newClassHandler);
	}

	/**
	 * @param valueCallBack
	 */

	public void addNewAccounterClassHandler(
			ValueCallBack<ClientAccounterClass> newClassHandler) {
		this.newClassHandler = newClassHandler;
	}
}
