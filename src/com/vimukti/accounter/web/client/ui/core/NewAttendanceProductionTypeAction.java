package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientAttendanceOrProductionType;
import com.vimukti.accounter.web.client.ui.payroll.NewClientAttendanceOrProductionDialog;

public class NewAttendanceProductionTypeAction extends
		Action<ClientAttendanceOrProductionType> {

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run() {
		NewClientAttendanceOrProductionDialog comboDialog = new NewClientAttendanceOrProductionDialog(
				messages.attendanceOrProductionType(), data);
		comboDialog.setCallback(getCallback());
		comboDialog.show();
		comboDialog.center();
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHelpToken() {
		// TODO Auto-generated method stub
		return null;
	}

}
