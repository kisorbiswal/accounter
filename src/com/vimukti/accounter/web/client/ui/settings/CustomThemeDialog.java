package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;

@SuppressWarnings("unchecked")
public class CustomThemeDialog extends BaseDialog {

	private HTML titleHTML;
	private TextBox titleBox;
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
		titleHTML = new HTML(FinanceApplication.getSettingsMessages()
				.yourTitle());
		titleBox = new TextBox();
		subTable.setWidget(0, 0, titleHTML);
		subTable.setWidget(1, 0, titleBox);

		buttonPanel = new HorizontalPanel();
		okbtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				okClicked();
			}
		});
		cancelBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cancelClicked();
			}
		});
		addInputDialogHandler(new InputDialogHandler() {

			@Override
			public boolean onOkClick() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onCancelClick() {
				hide();

			}
		});

		flexTable = new FlexTable();
		flexTable.setWidget(0, 0, subTable);
		flexTable.setWidget(1, 0, buttonPanel);
		setBodyLayout(flexTable);
	}

	@Override
	protected String getViewTitle() {
		return FinanceApplication.getSettingsMessages().newBrandThemeLabel();
	}
}
