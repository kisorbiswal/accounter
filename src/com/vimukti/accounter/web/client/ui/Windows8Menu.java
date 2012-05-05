package com.vimukti.accounter.web.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.win8.Windows8MenuView;

public class Windows8Menu extends WebMenu {

	public Windows8Menu() {
	}

	@Override
	void initialize(boolean isTouch) {
	}

	@Override
	public Widget asWidget() {
		
		Button button = new Button("menu");
		button.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
			}
		});
		
		return button; 
	}
}
