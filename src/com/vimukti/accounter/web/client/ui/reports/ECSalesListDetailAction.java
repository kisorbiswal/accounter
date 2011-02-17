package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class ECSalesListDetailAction extends Action {

	protected ECSalesListDetailReport report;

	public ECSalesListDetailAction(String text) {
		super(text);
		this.catagory = FinanceApplication.getReportsMessages().report();
	}

	public ECSalesListDetailAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = FinanceApplication.getReportsMessages().report();
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return FinanceApplication.getFinanceMenuImages().reports();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		return this.report;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);

	}

	private void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {

				try {
					report = new ECSalesListDetailReport();
					MainFinanceWindow.getViewManager().showView(report, data,
							isDependent, ECSalesListDetailAction.this);
				} catch (Throwable t) {
					onCreateFailed(t);
				}

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Report..", t);
			}
		});

	}

	@Override
	public String getImageUrl() {
		return "/images/reports.png";
	}

}
