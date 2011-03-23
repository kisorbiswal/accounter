package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Header extends HorizontalPanel {

	private HorizontalPanel hpanel1, hpanel2;
	private Label l1, userName;
	private HTML logout,help;
	private VerticalPanel vpanel;

	public Header() {
		createControls();
	}

	private void createControls() {
		hpanel1 = new HorizontalPanel();
		hpanel2 = new HorizontalPanel();
		hpanel2.setWidth("100%");
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
		help = new HTML("<span>Help</span><div class='help-content'></div>");
		help.addStyleName("help-style");
		hpanel1.add(l1);
		hpanel2.setStyleName("help-logout-sep");
		hpanel2.add(userName);
		hpanel2.add(help);
		hpanel2.add(logout);
		vpanel = new VerticalPanel();
		vpanel.setWidth("100%");
		vpanel.add(hpanel2);
		vpanel.add(hpanel1);
		this.add(vpanel);
		this.setWidth("100%");

	}

}
