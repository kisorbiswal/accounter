package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.ui.FlowPanel;

public class WebMenu {
	IMenuFactory menuFactory = null;
	private FlowPanel widget;
	public WebMenu() {
		// TODO Auto-generated constructor stub
	}

	void initialize() {
		boolean isTouch = false;
		
		if (isTouch) {
			menuFactory = new TouchMenuFactory();
		} else {
			menuFactory = new DesktopMenuFactory();
		}
	}
	
	void createMenu(){

		AccounterMenuBar menubar = new AccounterMenuBar(menuFactory);
		widget.add(menubar);
		
	}

	public void hasWidget(FlowPanel window) {
		widget = window;
	}

}
