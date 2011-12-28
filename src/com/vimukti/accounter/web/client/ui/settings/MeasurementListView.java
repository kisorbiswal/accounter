package com.vimukti.accounter.web.client.ui.settings;

import java.util.HashMap;
import java.util.Map;

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
		return messages.measurementList();
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
		return messages.measurementList();
	}

	@Override
	protected Action getAddNewAction() {
		if (!Accounter.getUser().getUserRole()
				.equalsIgnoreCase(messages.readOnly()))
			return ActionFactory.getAddMeasurementAction();
		else
			return null;
	}

	@Override
	protected String getAddNewLabelString() {
		if (!Accounter.getUser().getUserRole()
				.equalsIgnoreCase(messages.readOnly()))
			return messages.addNewMeasurement();
		else
			return "";
	}

	@Override
	public Map<String, Object> saveView() {
		Map<String, Object> map = new HashMap<String, Object>();
		// map.put("isActive", isActiveAccounts);
		map.put("start", start);
		return map;
	}

	@Override
	public void restoreView(Map<String, Object> viewDate) {

		if (viewDate == null || viewDate.isEmpty()) {
			return;
		}
		// isActiveAccounts = (Boolean) viewDate.get("isActive");
		start = (Integer) viewDate.get("start");
		onPageChange(start, getPageSize());
		// if (isActiveAccounts) {
		// viewSelect.setComboItem(messages.active());
		// } else {
		// viewSelect.setComboItem(messages.inActive());
		// }

	}

	@Override
	public void updateInGrid(ClientMeasurement objectTobeModified) {
		// TODO Auto-generated method stub

	}

}
