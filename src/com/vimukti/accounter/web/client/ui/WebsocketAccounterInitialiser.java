package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.win8.LoginPanel;

public class WebsocketAccounterInitialiser extends AccounterInitialiser {

	public WebsocketAccounterInitialiser(Accounter accounter) {
		super();
		this.accounter = accounter;
		initWindowsGui();
	}

	private void initWindowsGui() {
		LoginPanel loginPanel = new LoginPanel(this);
		accounter.removeLoadingImage();
		showView(loginPanel);
	}

	public void showView(Widget view) {
		RootPanel.get("mainWindow").clear();
		RootPanel.get("mainWindow").add(view);
	}

	public void loadCompany(Long companyId) {
		accounter.createWindowsRPCService().getCompany(companyId,
				new AccounterAsyncCallback<ClientCompany>() {

					@Override
					public void onException(AccounterException exception) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onResultSuccess(ClientCompany result) {
						accounter.gotCompany(result);
					}
				});
	}
}
