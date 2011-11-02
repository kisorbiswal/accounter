package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientMeasurement;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class AddMeasurementAction extends Action<ClientMeasurement> {

	public AddMeasurementAction(String text) {
		super(text);
	}

	@Override
	public void run() {

		runAysnc(data, isDependent);

	}

	private void runAysnc(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				AddMeasurementView view = new AddMeasurementView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, AddMeasurementAction.this);

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

	// @Override
	// public ParentCanvas getView() {
	// // TODO Auto-generated method stub
	// return null;
	// }

	@Override
	public String getHistoryToken() {
		return "addMeasurement";
	}

	@Override
	public String getHelpToken() {
		return  "add-measurement";
	}

}
