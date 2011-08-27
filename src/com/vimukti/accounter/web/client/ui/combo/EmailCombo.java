package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;

import com.vimukti.accounter.web.client.ui.Accounter;

public class EmailCombo extends CustomCombo {

	public EmailCombo(String title) {
		super(title);

		initCombo(getToAddress());
	}

	public EmailCombo(String title, boolean isAddNewRequire) {
		super(title, isAddNewRequire, 1);
		initCombo(getToAddress());
	}

	public ArrayList<String> getToAddress() {
		String companyEmail = Accounter.getCompany().getCompanyEmail();

		ArrayList<String> toAdd = new ArrayList<String>();
		toAdd.add(companyEmail);

		return toAdd;
	}

	@Override
	protected String getDisplayName(Object object) {
		if (object != null)
			return object.toString() != null ? object.toString() : "";
		else
			return "";
	}

	@Override
	protected String getColumnData(Object object, int row, int col) {
		switch (col) {
		case 0:
			return object.toString();
		}
		return null;
	}

	@Override
	public String getDefaultAddNewCaption() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onAddNew() {
		// TODO Auto-generated method stub

	}

}
