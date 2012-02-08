package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ui.Accounter;

public class FreeUserPage extends VerticalPanel {

	public FreeUserPage() {
		initPage();
	}

	private void initPage() {
		Label freeUserTextLabel = new Label(Accounter.getMessages()
				.freeUserText());
		add(freeUserTextLabel);
	}

}
