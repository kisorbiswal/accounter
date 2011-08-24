package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientMeasurement;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class MeasurementAction extends Action<ClientMeasurement> {

	public MesurementListView view;

	public MeasurementAction(String text) {
		super(text);
	}

	@Override
	public void run() {
		preAddedListView(data, isDependent);
	}

	public void preAddedListView(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {
			public void onCreated() {
				view = new MesurementListView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, MeasurementAction.this);
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
		return "Measurements";
	}

	@Override
	public String getHelpToken() {
		return "add-measurement";
	}

}
