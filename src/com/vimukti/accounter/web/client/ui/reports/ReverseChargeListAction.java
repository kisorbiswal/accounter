package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class ReverseChargeListAction extends Action {

	protected ReverseChargeListReport report;

	public ReverseChargeListAction(String text) {
		super(text);
		this.catagory = Accounter.constants().report();
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().reports();
	}

	// @Override
	// public ParentCanvas getView() {
	//
	// return this.report;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				report = new ReverseChargeListReport();
				MainFinanceWindow.getViewManager().showView(report, data,
						isDependent, ReverseChargeListAction.this);

			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

			}
		});
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/reports.png";
	// }

	@Override
	public String getHistoryToken() {
		return null;
	}

	@Override
	public String getHelpToken() {
		return "reverse-charge-list";
	}

}
