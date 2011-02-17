package com.vimukti.accounter.web.client;

import com.google.gwt.core.client.EntryPoint;
import com.vimukti.accounter.web.client.ui.StartupDialog;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;

public class Finance implements EntryPoint {

	public void onModuleLoad() {
		
		try {
			myTestFlow();
		} catch (Throwable e) {
//			//UIUtils.logError("An Error Has Occured\n Please Refer Log..", e);
		}
	}

	private void myTestFlow() {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreateFailed(Throwable t) {
//				UIUtils
//						.logError("An Error Has Occured\n Please Refer Log..",
//								t);
			}

			public void onCreated() {
				new StartupDialog();
			}

		});

	}

}
