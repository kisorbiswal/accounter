package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ui.Header;

public class SetupWizard extends VerticalPanel {
	private VerticalPanel panel;
	private Header header;
	private AbstractSetupPage setupPage;

	public SetupWizard() {
		header = new Header();
		setupPage = new SetupStartPage(this);
		creteControls();
		add(panel);
	}

	public void setView(AbstractSetupPage setupPage) {
		this.setupPage = setupPage;
	}

	public AbstractSetupPage getView() {
		return setupPage;
	}

	public void creteControls() {
		try {
			if (panel != null) {
				panel.removeFromParent();
			}

			panel = new VerticalPanel();
			panel.add(header);
			panel.add(getView());
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
