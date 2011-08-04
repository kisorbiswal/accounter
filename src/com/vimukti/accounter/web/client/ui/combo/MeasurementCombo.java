package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.ClientMeasurement;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;

public class MeasurementCombo extends CustomCombo<ClientMeasurement> {

	public MeasurementCombo(String title) {
		super(title);
		// initCombo(getCompany().getMeasurements());
	}

	@Override
	protected String getDisplayName(ClientMeasurement object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public SelectItemType getSelectItemType() {
		return SelectItemType.MEASUREMENT;
	}

	@Override
	protected String getColumnData(ClientMeasurement object, int row, int col) {
		return null;
	}

	@Override
	public String getDefaultAddNewCaption() {
		return Accounter.constants().addNewMeasurement();
	}

	@Override
	public void onAddNew() {
		Action action = ActionFactory.getMeasurementsAction();
		action.setActionSource(this);
		action.run(null, true);
	}

}
