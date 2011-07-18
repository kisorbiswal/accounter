package com.vimukti.accounter.web.client;

import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.RootPanel;
import com.vimukti.accounter.web.client.data.ClientIdentity;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.StartupDialog;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallBack;

public class Finance implements EntryPoint {

	private static ClientIdentity identity;

	public void onModuleLoad() {

		try {
			myTestFlow();
		} catch (Throwable e) {
			// //UIUtils.logError("An Error Has Occured\n Please Refer Log..",
			// e);
		}
	}

	private void myTestFlow() {
		AccounterAsync.createAsync(new CreateViewAsyncCallBack() {

			public void onCreateFailed(Throwable t) {
				// UIUtils
				// .logError("An Error Has Occured\n Please Refer Log..",
				// t);
			}

			public void onCreated() {
				checkCookie();
				// new StartupDialog();
			}

		});

	}

	private static void checkCookie() {
		if (GWT.isScript()) {
			ClientIdentity Clntidentity = getIdentity();
			if (Clntidentity != null) {
				initializeIdentity(Clntidentity);
			}
		} else {
			new StartupDialog();
		}
	}

	public static void initializeIdentity(ClientIdentity result) {
		Finance.identity = result;
	}

	public static ClientIdentity getIdentity() {
		if (identity == null) {
			getIdentityFromServer();
		}
		return identity;
	}


}

 