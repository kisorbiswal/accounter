package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientMeasurement;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class MeasurementListAction extends Action<ClientMeasurement> {

	MeasurementListView view;

	public MeasurementListAction(String text) {
		super(text);
		this.catagory = Accounter.constants().inventory();

	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(ClientMeasurement data, boolean isDependent) {
		try {
			view = new MeasurementListView();
			MainFinanceWindow.getViewManager().showView(view, data, false,
					MeasurementListAction.this);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		return "MeasurementList";
	}

	@Override
	public String getHelpToken() {
		return "measurement";
	}

}
