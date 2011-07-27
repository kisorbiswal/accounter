package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class AddMeasurementAction extends Action {

	private AddMeasurementView view;

	public AddMeasurementAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run(Object data, Boolean isDependent) {

		addMeasurementAction(data, isDependent);

	}

	private void addMeasurementAction(final Object data,
			final Boolean isDependent) {

		view = new AddMeasurementView();
		try {
			MainFinanceWindow.getViewManager().showView(view, data,
					isDependent, AddMeasurementAction.this);
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
	public ParentCanvas getView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "Add Measurement";
	}

}
