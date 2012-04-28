package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.HistoryTokens;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class PayHeadDetailReportAction extends Action {

	public PayHeadDetailReportAction() {
		super();
		this.catagory = messages.reports();
	}

	@Override
	public String getText() {
		return messages.payHeadDetailReport();
	}

	@Override
	public void run() {
		runAsnyc(data, isDependent);
	}

	private void runAsnyc(final Object data, final boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				PayHeadDetailReport detailReport = new PayHeadDetailReport();
				MainFinanceWindow.getViewManager().showView(detailReport, data,
						isDependent, PayHeadDetailReportAction.this);
			}
		});
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getHistoryToken() {
		return HistoryTokens.PAY_HEAD_DETAIL_REPORT;
	}

	@Override
	public String getHelpToken() {
		return "payHeadDetailReport";
	}

}
