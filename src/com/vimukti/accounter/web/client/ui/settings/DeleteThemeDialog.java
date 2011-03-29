package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.ViewManager;

public class DeleteThemeDialog extends BaseDialog {

	private HTML deleteHtml, undoneHtml;
	private Button deleteButton, cancelButton;
	private ClientBrandingTheme brandingTheme;
	private DeleteThemeDialog deleteThemeDialog;

	public DeleteThemeDialog(String title, String desc,
			ClientBrandingTheme theme) {
		super(title, desc);
		this.brandingTheme = theme;
		createControls();
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	private void createControls() {
		VerticalPanel deletePanel = new VerticalPanel();

		deleteHtml = new HTML("<p>Are you sure you want to delete <strong>"
				+ brandingTheme.getThemeName() + "</strong> theme?</p>");
		undoneHtml = new HTML(FinanceApplication.getSettingsMessages()
				.undoneHtml());

		HorizontalPanel buttonPanel = new HorizontalPanel();
		deleteButton = new Button(FinanceApplication.getSettingsMessages()
				.deleteButton());
		deleteButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				removeFromParent();
				if (!brandingTheme.isDefault()) {
					ViewManager.getInstance().deleteObject(brandingTheme,
							AccounterCoreType.BRANDINGTHEME,
							DeleteThemeDialog.this);
				} else
					Accounter.showError("We Cannot Delete this theme "
							+ brandingTheme.getThemeName());

			}
		});
		cancelButton = new Button(FinanceApplication.getSettingsMessages()
				.cancelButton());
		cancelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				removeFromParent();
			}
		});
		buttonPanel.add(deleteButton);
		buttonPanel.add(cancelButton);

		deletePanel.add(deleteHtml);
		deletePanel.add(undoneHtml);
		deletePanel.add(buttonPanel);

		okbtn.setVisible(false);
		cancelBtn.setVisible(false);
		mainPanel.add(deletePanel);
	}
}
