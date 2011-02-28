package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Header extends VerticalPanel {

	private HorizontalPanel hpanel1;
	private Label l1, l2, l3;

	public Header() {
		createControls();
	}

	private void createControls() {
		hpanel1 = new HorizontalPanel();
		hpanel1.addStyleName("main-color1");
		hpanel1.setWidth("100%");
		hpanel1.setHeight("50px");
		l1 = new Label();
		l1.setText("Demo Company (Global)");
		l1.addStyleName("label2-style");
		l2 = new Label("Logout");
		l3 = new Label("Help");
		hpanel1.add(l1);
		hpanel1.add(l2);
		hpanel1.add(l3);
		this.add(hpanel1);

	}
}
