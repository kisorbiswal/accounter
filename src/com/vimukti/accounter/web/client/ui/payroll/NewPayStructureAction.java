package com.vimukti.accounter.web.client.ui.payroll;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientPayStructure;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.HistoryTokens;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

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
		GWT.runAsync(NewPayStructureAction.class, new RunAsyncCallback() {

			public void onSuccess() {
				NewPayStructureView view = new NewPayStructureView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewPayStructureAction.this);
			}

			@Override
			public void onFailure(Throwable reason) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
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
