package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class CheckDetailReportAction extends Action {

	private CheckDetailReportView report;

	public CheckDetailReportAction(String text, String string) {
		super(text);
		this.catagory = Accounter.getReportsMessages().report();
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().reports();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		return this.report;
	}

	@Override
	public void run(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			@Override
			public void onCreated() {
				try {
					report = new CheckDetailReportView();
					MainFinanceWindow.getViewManager().showView(report, data,
							isDependent, CheckDetailReportAction.this);
				} catch (Throwable t) {
					onCreateFailed(t);

				}

			}

			@Override
			public void onCreateFailed(Throwable t) {

			}
		});

	}

	@Override
	public String getImageUrl() {
		return "/images/reports.png";
	}

	@Override
	public String getHistoryToken() {
		return "CheckDetailsReport";
	}

}
