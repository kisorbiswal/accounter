package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.CompanyAndFeatures;
import com.vimukti.accounter.web.client.core.CompanyDetails;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.win8.CompaniesPanel;
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
		String[] credentials = autoLogin(PASSWORD_CRED_RESOURCE);
		if (credentials == null) {
			showLogIn();
			return;
		}
		AccounterAsyncCallback<ArrayList<CompanyDetails>> callback = new AccounterAsyncCallback<ArrayList<CompanyDetails>>() {

			@Override
			public void onException(AccounterException exception) {
				showLogIn();
			}

			@Override
			public void onResultSuccess(ArrayList<CompanyDetails> result) {
				if (result.size() == 1) {
					loadCompany(result.get(0).getCompanyId());
					return;
				}
				hideLoading();
				showView(new CompaniesPanel(result,
						WebsocketAccounterInitialiser.this));
			}
		};
		Accounter.createWindowsRPCService().login(credentials[0],
				credentials[1], false, callback);
	}

	private void showLogIn() {
		hideLoading();
		LoginPanel loginPanel = new LoginPanel(this);
		showView(loginPanel);
	}

	private native void hideLoading() /*-{
		$wnd.document.getElementById('mainWindow').style.display = '';
		$wnd.document.getElementById('loading').style.display = 'none';
	}-*/;

	private native String[] autoLogin(String resource) /*-{
		try {
			var passwordVault = new Windows.Security.Credentials.PasswordVault;

			var passwordCredentials = passwordVault.findAllByResource(resource);

			if (passwordCredentials.Size == 0) {
				return null;
			}

			var pc = passwordVault.retrieve(resource, passwordCredentials
					.getAt(0).userName);
			var credentials = new Array;
			credentials[0] = pc.userName;
			credentials[1] = pc.password;
			return credentials;
		} catch (e) { // No stored credentials
			return null;
		}
	}-*/;

	public void showView(Widget view) {
		RootPanel.get("mainWindow").clear();
		RootPanel.get("mainWindow").add(view);
	}

	public void loadCompany(Long companyId) {
		showProgress();
		accounter.createWindowsRPCService().getCompany(companyId,
				new AccounterAsyncCallback<CompanyAndFeatures>() {

					@Override
					public void onException(AccounterException exception) {

					}

					@Override
					public void onResultSuccess(CompanyAndFeatures result) {
						hideLoading();
						hideProgress();
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

	public native void showProgress() /*-{
		$wnd.document.getElementById('indeterminate').style.display = '';
	}-*/;

	public native void hideProgress() /*-{
		$wnd.document.getElementById('indeterminate').style.display = 'none';
	}-*/;
}
