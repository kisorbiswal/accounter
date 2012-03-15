package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class StyleTabPanelImpl extends GwtTabPanel{


	private StyledPanel tabPanel;

	public StyleTabPanelImpl() {
		tabPanel = new StyledPanel("tabPanel");
	}
	
	@Override
	public void add(StyledPanel generalTab, String general) {
		
		Label tabTitle = new Label(general);
		tabTitle.setStyleName("tabTitle");
		
		tabPanel.add(tabTitle);
		tabPanel.add(generalTab);
	}

	@Override
	public Widget getPanel() {
		return tabPanel;
	}



}
