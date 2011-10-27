package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;

public class TAXFilingFrequencyCombo extends CustomCombo<String> {

	private AccounterConstants constants = Accounter.constants();

	public TAXFilingFrequencyCombo(String title) {
		super(title, false, 1);
		initCombo(getTAXFilingFrequencies());
	}

	private List<String> getTAXFilingFrequencies() {
		List<String> list = new ArrayList<String>();
		list.add(constants.monthly());
		list.add(constants.quarterly());
		list.add(constants.halfYearly());
		list.add(constants.yearly());
		return list;
	}

	@Override
	protected String getDisplayName(String object) {
		return object;
	}

	@Override
	protected String getColumnData(String object, int col) {
		return object;
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

	public int getSelectedFrequency() {
		return comboItems.indexOf(selectedObject);
	}

	public String getSelectedFrequencyAsString() {
		return selectedObject;
	}

}
