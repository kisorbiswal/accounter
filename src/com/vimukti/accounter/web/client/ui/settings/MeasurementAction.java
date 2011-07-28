package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class MeasurementAction extends Action {

	public MesurementListView view;

	public MeasurementAction(String text) {
		super(text);
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		preAddedListView(data, isDependent);
	}

	public void preAddedListView(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {
			public void onCreated() {
				try {
					view = new MesurementListView();
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, MeasurementAction.this);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			public void onCreateFailed(Throwable t) {
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

//	@Override
//	public ParentCanvas getView() {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public String getHistoryToken() {
		return "Measurements";
	}

}
