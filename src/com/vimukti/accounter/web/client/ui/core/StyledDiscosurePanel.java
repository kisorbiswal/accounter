package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.GwtDisclosurePanel;
import com.vimukti.accounter.web.client.ui.StyledPanel;

public class StyledDiscosurePanel extends GwtDisclosurePanel {

	Label titleLabel;
	StyledPanel contentPanel;
	private String title;

	public void setTitle(String title) {
		this.title = title;
		
	}

	public void setOpen(boolean checkOpen) {
		
	}

	public Widget getPanel() {
		return contentPanel;
	}

	public void setContent(StyledPanel content) {
		contentPanel = new StyledPanel("disclosurePanelcontent");
		titleLabel = new Label(title);
		titleLabel.getElement().setClassName("disclosurePanelTitle");
		contentPanel.add(titleLabel);
		contentPanel.add(content);
		
	}
}
