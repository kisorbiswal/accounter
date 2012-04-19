package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * 
 * @author Mandeep Singh
 */

public class CashFlowStatementAction extends Action {


	public CashFlowStatementAction() {
		super();
		this.catagory = messages.report();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean dependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				CashFlowStatementReport report = new CashFlowStatementReport();
				MainFinanceWindow.getViewManager().showView(report, data,
						dependent, CashFlowStatementAction.this);

			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
//		AccounterAsync.createAsync(new CreateViewAsyncCallback() {
//
//			public void onCreated() {
//				
//			}
//
//		});

	}

	// @Override
	// public ParentCanvas getView() {
	// return this.report;
	// }

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
		return "cashFlowReport";
	}

	@Override
	public String getHelpToken() {
		return "cash-flow";
	}

	@Override
	public String getText() {
		return messages.cashFlowReport();
	}

}
