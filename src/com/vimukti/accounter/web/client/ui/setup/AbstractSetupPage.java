package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;

public abstract class AbstractSetupPage extends VerticalPanel {
	protected boolean progress;
	protected Label header;
	protected AccounterConstants accounterConstants;
	protected AccounterMessages accounterMessages;
	protected ClientCompanyPreferences preferences;

	public AbstractSetupPage(ClientCompanyPreferences preferences) {
		this.preferences = preferences;
		accounterConstants = Accounter.constants();
		accounterMessages = Accounter.messages();
		Label pageHeader = new Label(getHeader());
		pageHeader.addStyleName("setup_header_label");
		this.add(pageHeader);
		this.add(getPageBody());
		this.setStyleName("setup_panel");
	}

	public AbstractSetupPage() {
		this(Accounter.getCompany().getPreferences());
	}

	public abstract String getHeader();

	public boolean getProgress() {
		return progress;
	}

	public void setProgress(boolean progress) {
		this.progress = progress;
	}

	public abstract VerticalPanel getPageBody();

	public abstract void onLoad();

	public abstract void onSave();

	public abstract boolean doShow();

	public void setPreferences(ClientCompanyPreferences preferences) {
		this.preferences = preferences;
	}

}
