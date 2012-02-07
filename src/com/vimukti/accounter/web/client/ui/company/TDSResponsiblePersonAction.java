package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;
import com.vimukti.accounter.web.client.ui.vat.TDSResponsiblePersonDetailsView;

public class TDSResponsiblePersonAction extends Action {

	protected TDSResponsiblePersonDetailsView view;

	public TDSResponsiblePersonAction() {
		super();
		this.catagory = messages.deducatorMasters();
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				view = new TDSResponsiblePersonDetailsView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, TDSResponsiblePersonAction.this);
			}

		});
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "personDetails";
	}

	@Override
	public String getHelpToken() {
		return "personDetails";
	}

	@Override
	public String getText() {
		return messages.particularForPersonResponsibleForTaxDeduction();
	}
}
