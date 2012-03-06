package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.StyledPanel;

public class StyledDiscosurePanel {

	Label titleLabel;
	StyledPanel contentPanel;
	private String title;

	public StyledDiscosurePanel(String title) {
		this.title = title;
	}

	public void setContent(StyledPanel contents) {
		contentPanel = new StyledPanel("disclosurePanelcontent");
		titleLabel = new Label(title);
		titleLabel.getElement().setClassName("disclosurePanelTitle");
		contentPanel.add(titleLabel);
		contentPanel.add(contents);

	}

	public Widget getPanel() {
		return contentPanel;
	}
}
