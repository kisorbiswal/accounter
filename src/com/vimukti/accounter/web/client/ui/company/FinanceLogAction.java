/**
 * 
 */
package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.FinanceLogView;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

/**
 * @author vimukti27
 * 
 */
public class FinanceLogAction extends Action {

	protected FinanceLogView view;

	public FinanceLogAction(String text) {
		super(text);
		catagory = Accounter.constants().help();
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	// @Override
	// public ParentCanvas getView() {
	// return view;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				view = new FinanceLogView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, FinanceLogAction.this);

			}

			@Override
			public void onFailure(Throwable arg0) {
				Accounter
						.showError(Accounter.constants().unableToshowtheview());

			}
		});
	}

	@Override
	public String getHistoryToken() {
		return "financeLog";
	}

	@Override
	public String getHelpToken() {
		return "finance-log";
	}

}
