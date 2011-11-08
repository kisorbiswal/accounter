package com.vimukti.accounter.web.client.translate;

import com.google.gwt.user.client.ui.VerticalPanel;

public class TranslationWizard extends VerticalPanel {

	public TranslationWizard() {
		StatusPanel statusPanel = new StatusPanel(TranslationWizard.this);
		statusPanel.setWidth("100%");
		this.add(statusPanel);
	}
}
