package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.user.client.ui.FlexTable;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;

public class NewBrandThemeDialog extends BaseDialog {

	private FlexTable mainTable;

	public NewBrandThemeDialog(String title, String desc) {
		super(title, desc);
		createControls();
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

	}

	private void createControls() {
		

	}
}
