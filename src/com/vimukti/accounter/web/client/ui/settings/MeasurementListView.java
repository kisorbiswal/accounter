package com.vimukti.accounter.web.client.ui.settings;

import com.vimukti.accounter.web.client.core.ClientMeasurement;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseListView;

public class MeasurementListView extends BaseListView<ClientMeasurement> {

	@Override
	public void init() {
		super.init();
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().measurementList();
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		Accounter.createHomeService().getAllUnits(this);
	}

	@Override
	protected void initGrid() {
		grid = new MeasurementsListGrid(false);
		grid.init();
	}

	@Override
	protected String getListViewHeading() {
		return Accounter.constants().measurementList();
	}

	@Override
	protected Action getAddNewAction() {
		return ActionFactory.getAddMeasurementAction();
	}

	@Override
	protected String getAddNewLabelString() {
		return Accounter.constants().addNewMeasurement();
	}

	@Override
	public void updateInGrid(ClientMeasurement objectTobeModified) {
		// TODO Auto-generated method stub

	}

}
