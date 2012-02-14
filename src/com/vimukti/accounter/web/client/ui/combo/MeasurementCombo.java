package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.ClientMeasurement;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.settings.AddMeasurementAction;

public class MeasurementCombo extends CustomCombo<ClientMeasurement> {

	public MeasurementCombo(String title, boolean isAddNewRequire) {
		super(title, isAddNewRequire, 1);
		initCombo(getCompany().getMeasurements());
	}

	@Override
	protected String getDisplayName(ClientMeasurement object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	protected String getColumnData(ClientMeasurement object, int col) {
		return object.getName();
	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages.measurement();
	}

	@Override
	public void onAddNew() {
		AddMeasurementAction action = ActionFactory.getAddMeasurementAction();
		action.setCallback(new ActionCallback<ClientMeasurement>() {

			@Override
			public void actionResult(ClientMeasurement result) {
				if (result.getDisplayName() != null)
					addItemThenfireEvent(result);
			}
		});
		action.run(null, true);
	}
}
