package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class TAXAgencyListAction extends Action {

	public TAXAgencyListAction() {
		super();
		this.catagory = messages.tax();
	}

	@Override
	public void run() {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				MainFinanceWindow.getViewManager().showView(
						new TAXAgencyListView(), null, false,
						TAXAgencyListAction.this);

			}
		});

	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().vendors();
	}

	@Override
	public String getHistoryToken() {

		return "TAXAgencyList";
	}

	@Override
	public String getHelpToken() {
		return "taxAgency-list";
	}

	@Override
	public String getText() {
		return messages.payeeList(messages
				.taxAgencies());
	}

}
