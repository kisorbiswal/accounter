package com.vimukti.accounter.web.client.ui.payroll;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientPayStructure;
import com.vimukti.accounter.web.client.ui.HistoryTokens;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class NewPayStructureAction extends Action<ClientPayStructure> {

	@Override
	public String getText() {
		return messages.newPayee(messages.payStructure());
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final ClientPayStructure data,
			final boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				NewPayStructureView view = new NewPayStructureView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewPayStructureAction.this);

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHistoryToken() {
		return HistoryTokens.NEW_PAYSTRUCTURE;
	}

	@Override
	public String getHelpToken() {
		return "new_pay_structure";
	}

}
