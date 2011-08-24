/**
 * 
 */
package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * @author Murali.A
 * 
 */
public class VATDetailsReportAction extends Action {

	private VATDetailReportView report;

	public VATDetailsReportAction(String text) {
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
	// return this.report;
	// }

	public void runAsync(final Object data, final Boolean dependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				report = new VATDetailReportView();
				MainFinanceWindow.getViewManager().showView(report, data,
						dependent, VATDetailsReportAction.this);

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Report...", t);
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

		return "vatDetail";
	}

	@Override
	public String getHelpToken() {
		return "vat-details";
	}

}
