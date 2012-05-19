package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class TDSPayAction extends Action {

	public TDSPayAction() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
//		runAsync(data, isDependent);

	}

	private void runAsync(final Object data, final Boolean isDependent) {
//		GWT.runAsync(new RunAsyncCallback() {
//
//			public void onSuccess() {
				PayTDSView view = new PayTDSView(0);
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, TDSPayAction.this);
//			}
//
//			public void onFailure(Throwable e) {
//				Accounter.showError(Global.get().messages()
//						.unableToshowtheview());
//			}
//		});
		// AccounterAsync.createAsync(new CreateViewAsyncCallback() {
		//
		// public void onCreated() {
		//
		//
		//
		// }
		//
		// });
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
		return messages.payTDS();
	}

	@Override
	public String getHelpToken() {
		//
		return messages.payTDS();
	}

	@Override
	public String getText() {
		return messages.payTDS();
	}

}
