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
public class TAXItemExceptionDetailReport extends Action {

	public TAXItemExceptionDetailReport(String text) {
		super(text);
		this.catagory = Accounter.constants().report();
	}

	@Override
	public void run() {

		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean dependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				TAXitemExceptionReport report = new TAXitemExceptionReport();
				MainFinanceWindow.getViewManager().showView(report, data,
						dependent, TAXItemExceptionDetailReport.this);

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Report...", t);
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
		return Accounter.getFinanceMenuImages().reports();
	}

	@Override
	public String getHistoryToken() {
		return "TaxItem ExceptionDetails";
	}

	@Override
	public String getHelpToken() {
		return "TaxItem-ExceptionDetails";
	}
}
