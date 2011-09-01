package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;

public class CustomThemeDialog extends BaseDialog {

	private HTML titleHTML;
	private TextBox titleBox;
	private FlexTable flexTable, subTable;
	private HorizontalPanel buttonPanel;

	public CustomThemeDialog(String title, String desc) {
		super(title, desc);
		createControls();
	}


	private void createControls() {
		subTable = new FlexTable();
		titleHTML = new HTML(Accounter.constants().yourTitle());
		titleBox = new TextBox();
		subTable.setWidget(0, 0, titleHTML);
		subTable.setWidget(1, 0, titleBox);

		buttonPanel = new HorizontalPanel();

		flexTable = new FlexTable();
		flexTable.setWidget(0, 0, subTable);
		flexTable.setWidget(1, 0, buttonPanel);
		setBodyLayout(flexTable);
	}

	@Override
	protected boolean onOK() {
		// TODO Auto-generated method stub
		return true;
	}

}
