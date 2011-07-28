package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

/**
 * 
 * @author Mandeep Singh
 */

public class CashFlowStatementAction extends Action {

	protected CashFlowStatementReport report;

	public CashFlowStatementAction(String text) {
		super(text);
		this.catagory = Accounter.constants().report();
	}

	public CashFlowStatementAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = Accounter.constants().report();
	}

	@Override
	public void run(Object data, Boolean dependent) {
		runAsync(data, dependent);
	}

	public void runAsync(final Object data, final Boolean dependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreated() {

				try {
					report = new CashFlowStatementReport();
					MainFinanceWindow.getViewManager().showView(report, data,
							dependent, CashFlowStatementAction.this);
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
	public ParentCanvas getView() {
		return this.report;
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().reports();
	}

	@Override
	public String getImageUrl() {
		return "/images/reports.png";
	}

	@Override
	public String getHistoryToken() {
		return "cashFlowReport";
	}

}
