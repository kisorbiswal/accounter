package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class RealisedExchangeLossesAndGainsAction extends Action {

	public RealisedExchangeLossesAndGainsAction() {
		super();
		this.catagory = messages.report();
	}

	@Override
	public String getText() {
		return Global.get().messages().realisedExchangeLossesAndGains();
	}

	@Override
	public void run() {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				RealisedExchangeLossesAndGainsReport report = new RealisedExchangeLossesAndGainsReport();
				MainFinanceWindow.getViewManager().showView(report, data,
						isDependent, RealisedExchangeLossesAndGainsAction.this);

			}

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed to Load Report..", t);
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
		return "realisedExchangeLossesOrGains";
	}

	@Override
	public String getHelpToken() {
		return "realisedExchangeLossesOrGains";
	}

}
