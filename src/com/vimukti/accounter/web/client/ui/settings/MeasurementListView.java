package com.vimukti.accounter.web.client.ui.settings;

import java.util.ArrayList;

import com.vimukti.accounter.web.client.core.ClientMeasurement;
import com.vimukti.accounter.web.client.core.PaginationList;
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
		return Accounter.messages().measurementList();
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		Accounter.createHomeService().getAllUnits(this);
	}

	@Override
	public void onSuccess(PaginationList<ClientMeasurement> result) {
		super.onSuccess(result);
		grid.sort(10, false);
	}

	@Override
	protected void initGrid() {
		viewSelect.setVisible(false);
		grid = new MeasurementsListGrid(false);
		grid.init();
	}

	@Override
	protected String getListViewHeading() {
		return Accounter.messages().measurementList();
	}

	@Override
	protected Action getAddNewAction() {
		if (!Accounter.getUser().getUserRole()
				.equalsIgnoreCase(messages().readOnly()))
			return ActionFactory.getAddMeasurementAction();
		else
			return null;
	}

	@Override
	protected String getAddNewLabelString() {
		if (!Accounter.getUser().getUserRole()
				.equalsIgnoreCase(messages().readOnly()))
			return Accounter.messages().addNewMeasurement();
		else
			return "";
	}

	@Override
	public void updateInGrid(ClientMeasurement objectTobeModified) {
		// TODO Auto-generated method stub

	}

}
