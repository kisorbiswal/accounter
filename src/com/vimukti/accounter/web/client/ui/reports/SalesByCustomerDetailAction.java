package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * 
 * @author Mandeep Singh
 */

public class SalesByCustomerDetailAction extends Action {

	public SalesByCustomerDetailAction(String text) {
		super(text);
		this.catagory = Accounter.constants().report();
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				AbstractReportView<SalesByCustomerDetail> report = new SalesByCustomerDetailReport();
				MainFinanceWindow.getViewManager().showView(report, data,
						isDependent, SalesByCustomerDetailAction.this);

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Report..", t);
			}
		});

	}

	// @Override
	// public ParentCanvas getView() {
	// // NOTHING TO DO.
	// return null;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().reports();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/reports.png";
	// }

	@Override
	public String getHistoryToken() {
		return "salesByCustomerDetail";
	}

	@Override
	public String getHelpToken() {
		return "sales-by-customer";
	}

}
