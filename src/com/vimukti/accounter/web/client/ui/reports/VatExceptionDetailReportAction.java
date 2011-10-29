package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class VatExceptionDetailReportAction extends Action {

	private VATExceptionDetailReportView report;

	public VatExceptionDetailReportAction(String text) {
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

	public void runAsync(final Object data, final Boolean dependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				report = new VATExceptionDetailReportView();
				MainFinanceWindow.getViewManager().showView(report, data,
						dependent, VatExceptionDetailReportAction.this);

			}

			public void onCreateFailed(Throwable t) {
			}
		});

	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/reports.png";
	// }

	@Override
	public String getHistoryToken() {

		return "vatExceptionDetail";
	}

	@Override
	public String getHelpToken() {
		return "vat-ExceptionDetails";
	}
}
