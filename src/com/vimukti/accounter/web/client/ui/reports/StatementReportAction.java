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

public class StatementReportAction extends Action {

	private final boolean isVendor;
	private final long payeeId;

	public StatementReportAction(long payeeId, boolean isVendor) {
		super();
		this.isVendor = isVendor;
		this.payeeId = payeeId;
		this.catagory = messages.report();

	}

	public void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				StatementReport report = new StatementReport(isVendor, payeeId);
				MainFinanceWindow.getViewManager().showView(report, data,
						isDependent, StatementReportAction.this);
			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
		
	}

	// @Override
	// public ParentCanvas getView() {
	// return this.report;
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

		if (isVendor == true) {
			return "vendorStatement";
		} else {
			return "customerStatement";
		}
	}

	@Override
	public String getHelpToken() {
		return "statement-report";
	}

	@Override
	public String getText() {
		String text = "";
		try {
			if (isVendor) {
				text = messages.payeeStatement(Global.get().Vendor());
			} else {
				text = messages.payeeStatement(Global.get().Customer());
			}
		} catch (Exception e) {
			System.err.println(e);
		}
		return text;
	}

}
