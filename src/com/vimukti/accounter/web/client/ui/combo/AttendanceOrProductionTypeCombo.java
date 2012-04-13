package com.vimukti.accounter.web.client.ui.combo;

import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.NewAttendanceProductionTypeAction;

public class AttendanceOrProductionTypeCombo extends
		CustomCombo<ClientAttendanceOrProductionType> {

	private int type;

	public AttendanceOrProductionTypeCombo(int type, String title,
			String styleName) {
		super(title, true, 1, styleName);
		this.type = type;
		initCombo();
	}

	private void initCombo() {
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
				}, getCompany().getAttendanceProductionTypes());
		if (type == ClientAttendanceOrProductionType.TYPE_LEAVE_WITH_PAY) {
			ClientAttendanceOrProductionType clientAttendanceOrProductionType = new ClientAttendanceOrProductionType();
			clientAttendanceOrProductionType.setName("Not Applicable");
			list.add(0, clientAttendanceOrProductionType);
		}
		initCombo(list);
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
				addItemThenfireEvent(result);
			}
		});

		action.run(null, true);
	}

}
