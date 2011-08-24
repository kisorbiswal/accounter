package com.vimukti.accounter.web.client.help;

import com.vimukti.accounter.web.client.ui.forms.CustomDialog;

public class HelpDialog extends CustomDialog {

	public HelpDialog(HelpPanel helpPannel) {
		this.setWidth("100%");
		this.setHeight("100%");
		this.setModal(false);
		this.addStyleName("helpdialog");
		this.setPopupPosition(470, 200);
		this.add(helpPannel);
	}

}
