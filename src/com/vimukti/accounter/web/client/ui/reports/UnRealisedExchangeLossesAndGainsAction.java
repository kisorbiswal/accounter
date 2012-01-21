package com.vimukti.accounter.web.client.ui.reports;

import java.util.Map;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class UnRealisedExchangeLossesAndGainsAction extends
		Action<Map<Long, Double>> {

	private ClientFinanceDate enteredDate;

	public UnRealisedExchangeLossesAndGainsAction() {
		super();
		this.catagory = messages.report();
	}

	@Override
	public String getText() {
		return messages.unRealisedExchangeLossesAndGains();
	}

	@Override
	public void run() {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {
				UnRealisedExchangeLossesAndGainsReport report = new UnRealisedExchangeLossesAndGainsReport();
				report.setExchangeRates(data);
				report.setEnteredDate(enteredDate);
				MainFinanceWindow.getViewManager().showView(report, null,
						isDependent,
						UnRealisedExchangeLossesAndGainsAction.this);

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
		return "unrealisedExchangeLossesAndGains";
	}

	@Override
	public String getHelpToken() {
		return "unrealisedExchangeLossesAndGains";
	}

	public void setEnterDate(ClientFinanceDate date) {
		this.enteredDate = date;
	}

}
