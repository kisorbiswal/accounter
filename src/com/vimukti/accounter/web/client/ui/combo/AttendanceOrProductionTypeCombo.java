package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.NewAttendanceProductionTypeAction;

public class AttendanceOrProductionTypeCombo extends
		CustomCombo<ClientAttendanceOrProductionType> {

	private int type;
	protected ArrayList<ClientAttendanceOrProductionType> list;
	private long selectedId;

	public AttendanceOrProductionTypeCombo(int type, String title,
			String styleName) {
		super(title, true, 1, styleName);
		this.type = type;
		getAttendanceProductionTypes();
	}

	private void initCombo(ArrayList<ClientAttendanceOrProductionType> result) {
		List<ClientAttendanceOrProductionType> list = Utility.filteredList(
				new ListFilter<ClientAttendanceOrProductionType>() {

					@Override
					public boolean filter(ClientAttendanceOrProductionType e) {
						if (type == 100) {
							return e.getType() == ClientAttendanceOrProductionType.TYPE_LEAVE_WITH_PAY
									|| e.getType() == ClientAttendanceOrProductionType.TYPE_LEAVE_WITHOUT_PAY;
						}
						return e.getType() == type;
					}
				}, result);
		initCombo(list);
	}

	private void getAttendanceProductionTypes() {
		Accounter
				.createPayrollService()
				.getAttendanceProductionTypes(
						new AsyncCallback<ArrayList<ClientAttendanceOrProductionType>>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onSuccess(
									ArrayList<ClientAttendanceOrProductionType> result) {
								list = result;
								initCombo(result);
								if (selectedId != 0) {
									setComboItem();
								}
							}
						});
	}

	public void setSelectedId(long selectedId) {
		this.selectedId = selectedId;
	}

	protected void setComboItem() {
		List<ClientAttendanceOrProductionType> comboItems2 = getComboItems();
		for (ClientAttendanceOrProductionType item : comboItems2) {
			if (selectedId == item.getID()) {
				setComboItem(item);
			}
		}
	}

	@Override
	protected String getDisplayName(ClientAttendanceOrProductionType object) {
		return object.getDisplayName();
	}

	@Override
	protected String getColumnData(ClientAttendanceOrProductionType object,
			int col) {
		if (col == 0) {
			return object.getName();
		}
		return null;
	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages.attendanceOrProductionType();
	}

	@Override
	public void onAddNew() {
		NewAttendanceProductionTypeAction action = ActionFactory
				.getNewAttendanceProductionTypeAction();
		action.setCallback(new ActionCallback<ClientAttendanceOrProductionType>() {

			@Override
			public void actionResult(ClientAttendanceOrProductionType result) {
				if (type == 100
						&& !(result.getType() == ClientAttendanceOrProductionType.TYPE_LEAVE_WITH_PAY || result
								.getType() == ClientAttendanceOrProductionType.TYPE_LEAVE_WITHOUT_PAY)) {
					return;
				}
				if (type == result.getType()) {
					list.add(result);
					addItemThenfireEvent(result);
				}

				itemAdded(result);
			}
		});

		action.run(null, true);
	}

	protected void itemAdded(ClientAttendanceOrProductionType obj) {
		// TODO Auto-generated method stub

	}

}
