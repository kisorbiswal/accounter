package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;

public class CustomThemeDialog extends BaseDialog {

	private HTML titleHTML;
	private TextBox titleBox;
	private Button okButton, cancelButton;
	private FlexTable flexTable, subTable;
	private HorizontalPanel buttonPanel;

	public CustomThemeDialog(String title, String desc) {
		super(title, desc);
		createControls();
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	private void createControls() {
		subTable = new FlexTable();
		titleHTML = new HTML(
				"<p><font size='0px' face='Arial'><b>Your title for the new Branding Theme</b></font></p>");
		titleBox = new TextBox();
		subTable.setWidget(0, 0, titleHTML);
		subTable.setWidget(1, 0, titleBox);

		buttonPanel = new HorizontalPanel();
		okButton = new Button("OK");
		okButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

			}
		});
		cancelButton = new Button("Cancel");
		cancelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cancelClicked();

			}
		});
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		flexTable = new FlexTable();
		flexTable.setWidget(0, 0, subTable);
		flexTable.setWidget(1, 0, buttonPanel);
		okbtn.setVisible(false);
		cancelBtn.setVisible(false);
		mainPanel.add(flexTable);
	}
}
