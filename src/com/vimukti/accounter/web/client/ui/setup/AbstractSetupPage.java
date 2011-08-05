package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;

public abstract class AbstractSetupPage extends VerticalPanel {
	boolean progress;
	private Label header;
	private VerticalPanel mainPanel;
	public AccounterConstants accounterConstants;

	public AbstractSetupPage() {
		SetupWizard.setupPage = this;
		accounterConstants = Accounter.constants();
	}

	public abstract String getHeader();

	// public abstract String getFooter() {
	// return null;
	// }
	public boolean getProgress() {
		return progress;
	}

	public void setProgress(boolean progress) {
		this.progress = progress;
	}

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
