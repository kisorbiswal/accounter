package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;

/**
 * 
 * @author Mandeep Singh
 */

public class TransactionDetailByTaxCodeAction extends Action {

	public TransactionDetailByTaxCodeAction(String text) {
		super(text);
		this.catagory = Accounter.constants().report();
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {

				try {
					//
					// AbstractReportView<TransactionDetailByTaxcode> report =
					// new TransactionDetailByTaxCodeReport();
					// MainFinanceWindow.getViewManager().showView(report, data,
					// isDependent, TransactionDetailByTaxCodeAction.this);
					Accounter
							.showInformation("This Report Not Yet Implemented");
				} catch (Throwable t) {
					onCreateFailed(t);
				}

			}

			public void onCreateFailed(Throwable t) {
				// UIUtils.logError("Failed to Load Report...", t);
			}
		});

	}

	// @Override
	// public ParentCanvas getView() {
	// // not required for this class
	// return null;
	// }

	@Override
	public void run(Object data, Boolean isDependent) {
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

		return "transactionDetailByTaxCode";
	}

}
