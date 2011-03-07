package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class Header extends HorizontalPanel {

	private HorizontalPanel hpanel1;
	private Label l1, helpLabel, userName;
	private HTML logout;

	public Header() {
		createControls();
	}

	private void createControls() {
		hpanel1 = new HorizontalPanel();
		hpanel1.addStyleName("main-color1");
		hpanel1.setWidth("100%");
		l1 = new Label();
		l1.setText("Demo Company (Global)");
		l1.addStyleName("label2-style");
		userName = new Label();
		userName.setText(FinanceApplication.getCompanyMessages().userName(
				FinanceApplication.clientIdentity.getDisplayName()));
		userName.addStyleName("userName-style");
		logout = new HTML("<a href='/do/logout'>Logout</a>");
		logout.addStyleName("logout-html");
		helpLabel = new Label("Help");
		helpLabel.addStyleName("help-style");
		hpanel1.add(l1);
		hpanel1.add(userName);
		hpanel1.add(logout);
		hpanel1.add(helpLabel);
		this.add(hpanel1);
		this.setWidth("100%");

	}

}
