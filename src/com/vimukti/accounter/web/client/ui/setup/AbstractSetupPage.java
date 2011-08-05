package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public abstract class AbstractSetupPage extends VerticalPanel {

	private Label header;
	private VerticalPanel mainPanel;

	public AbstractSetupPage() {
		SetupWizard.setupPage = this;
	}

	public abstract String getHeader();

	// public abstract String getFooter() {
	// return null;
	// }
	public abstract boolean getProgress();

	public abstract boolean setProgress();

	public abstract VerticalPanel getPageBody();

	private void createControls() {
		header = new Label(getHeader());
		mainPanel.add(header);
	}

	public abstract void onLoad();

	public abstract void onSave();

	public AbstractSetupPage getView() {
		return this;
	}
}
