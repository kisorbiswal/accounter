package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientMeasurement;
import com.vimukti.accounter.web.client.ui.Accounter;
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
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				view = new MesurementListView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, MeasurementAction.this);

			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

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

}
