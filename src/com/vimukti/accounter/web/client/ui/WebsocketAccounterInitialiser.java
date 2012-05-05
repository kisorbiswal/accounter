package com.vimukti.accounter.web.client.ui;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.CompanyAndFeatures;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.win8.LoginPanel;

public class WebsocketAccounterInitialiser extends AccounterInitialiser {

	public WebsocketAccounterInitialiser() {
		// initWindowsGui();
	}

	@Override
	public void initalize() {
		initWindowsGui();
	}

	private void initWindowsGui() {
		LoginPanel loginPanel = new LoginPanel(this);
		showView(loginPanel);
	}

	public void showView(Widget view) {
		RootPanel.get("mainWindow").clear();
		RootPanel.get("mainWindow").add(view);
	}

	public void loadCompany(Long companyId) {
		accounter.createWindowsRPCService().getCompany(companyId,
				new AccounterAsyncCallback<CompanyAndFeatures>() {

					@Override
					public void onException(AccounterException exception) {

					}

					@Override
					public void onResultSuccess(CompanyAndFeatures result) {
						Set features = new HashSet(result.getFeatures());
						accounter.setFeatures(features);
						accounter.gotCompany(result.getClientCompany());

					}
				});
	}

	@Override
	public Boolean isIpad() {
		return false;
	}
}
