package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.reports.UnbilledCostsByJob;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class UnbilledCostsByJobAction extends Action<UnbilledCostsByJob> {

	public UnbilledCostsByJobAction() {
		super();
		this.catagory = messages.report();
	}

	@Override
	public String getText() {
		return messages.unbilledCostsByJob();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	private void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				UnBilledCostsByJobReport costsByJobReport = new UnBilledCostsByJobReport();
				MainFinanceWindow.getViewManager().showView(costsByJobReport,
						data, isDependent, UnbilledCostsByJobAction.this);
			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
//		AccounterAsync.createAsync(new CreateViewAsyncCallback() {
//
//			@Override
//			public void onCreated() {
//			
//			}
//
//		});
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "UnbilledCostsByJob";
	}

	@Override
	public String getHelpToken() {
		return "Unbilled-Costs-By-Job";
	}

}
