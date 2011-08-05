package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.user.client.ui.VerticalPanel;

public abstract class AbstractSetupPage extends VerticalPanel {

	public abstract String getHeader();

	// public abstract String getFooter() {
	// return null;
	// }
	public abstract boolean getProgress();

	public abstract VerticalPanel getPageBody();

	private void createControls() {

	}
}
