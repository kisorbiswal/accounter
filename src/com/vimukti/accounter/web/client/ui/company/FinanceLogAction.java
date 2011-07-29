/**
 * 
 */
package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.FinanceLogView;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;

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

//	@Override
//	public ParentCanvas getView() {
//		return view;
//	}

	@Override
	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreateFailed(Throwable t) {
				// //UIUtils.logError("Failed To Load Manage Sales Tax Codes View",
				// t);

			}

			public void onCreated() {
				try {
					view = new FinanceLogView();
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, FinanceLogAction.this);
				} catch (Throwable e) {
					onCreateFailed(e);

				}

			}
		});

	}

	@Override
	public String getHistoryToken() {
		return "financeLog";
	}

}
