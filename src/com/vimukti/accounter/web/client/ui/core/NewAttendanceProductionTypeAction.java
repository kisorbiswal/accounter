package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.vimukti.accounter.web.client.ui.HistoryTokens;
import com.vimukti.accounter.web.client.ui.payroll.NewClientAttendanceOrProductionDialog;

public class NewAttendanceProductionTypeAction extends
		Action<ClientAttendanceOrProductionType> {

	@Override
	public String getText() {
		return messages.attendanceOrProductionType();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final ClientAttendanceOrProductionType data,
			boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				NewClientAttendanceOrProductionDialog comboDialog = new NewClientAttendanceOrProductionDialog(
						messages.attendanceOrProductionType(), data);
				comboDialog.setCallback(getCallback());
				comboDialog.show();
				comboDialog.center();
			}
		});

	}

	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHistoryToken() {
		return HistoryTokens.ATTENDANCE_PRODUCTION_TYPE;
	}

	@Override
	public String getHelpToken() {
		// TODO Auto-generated method stub
		return null;
	}

}
