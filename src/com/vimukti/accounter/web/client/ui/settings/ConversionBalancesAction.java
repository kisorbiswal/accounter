package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;

public class ConversionBalancesAction extends Action {
	private ConversionBalancesView view;

	public ConversionBalancesAction(String text) {
		super(text);
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	
//	@Override
//	public ParentCanvas getView() {
//		return null;
//	}

	public void run(Object data, Boolean isDependent, String endingDate,
			String year) {
		runAsync(data, isDependent, endingDate, year);
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent, null, null);
	}

	private void runAsync(final Object data, final Boolean isDependent,
			final String endingDate, final String year) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {

				try {
					view = new ConversionBalancesView(endingDate, year);
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, ConversionBalancesAction.this);

				} catch (Throwable e) {
					System.out.println(e.toString());
					onCreateFailed(e);
				}

			}

			public void onCreateFailed(Throwable t) {
			}
		});
	}

	@Override
	public String getHistoryToken() {
		return null;
	}

}
