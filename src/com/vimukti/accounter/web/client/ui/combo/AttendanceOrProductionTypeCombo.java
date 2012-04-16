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
							return e.getPeriodType() == ClientAttendanceOrProductionType.TYPE_LEAVE_WITH_PAY
									|| e.getPeriodType() == ClientAttendanceOrProductionType.TYPE_LEAVE_WITHOUT_PAY;
						}
						return e.getPeriodType() == type;
					}
				}, result);
		if (type == ClientAttendanceOrProductionType.TYPE_LEAVE_WITH_PAY) {
			ClientAttendanceOrProductionType clientAttendanceOrProductionType = new ClientAttendanceOrProductionType();
			clientAttendanceOrProductionType.setName("Not Applicable");
			list.add(0, clientAttendanceOrProductionType);
		}
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
							}
						});
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
						&& !(result.getPeriodType() == ClientAttendanceOrProductionType.TYPE_LEAVE_WITH_PAY || result
								.getPeriodType() == ClientAttendanceOrProductionType.TYPE_LEAVE_WITHOUT_PAY)) {
					return;
				}
				if (type != result.getPeriodType()) {
					return;
				}
				list.add(result);
				addItemThenfireEvent(result);
			}
		});

		action.run(null, true);
	}

}
