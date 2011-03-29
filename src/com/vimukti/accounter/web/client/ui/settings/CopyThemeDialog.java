package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.ViewManager;

public class CopyThemeDialog extends BaseDialog {

	private ClientBrandingTheme theme;
	private CopyThemeDialog dialog;

	public CopyThemeDialog(String title, String desc,
			ClientBrandingTheme brandingTheme) {
		super(title, desc);
		this.theme = brandingTheme;
		createControls();
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	private void createControls() {

		VerticalPanel copyPanel = new VerticalPanel();
		Label yourLabel = new Label(FinanceApplication.getSettingsMessages()
				.yourTitle());
		final TextBox yourBox = new TextBox();
		Button okButton = new Button(FinanceApplication.getSettingsMessages()
				.ok());
		okButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ClientBrandingTheme brandingTheme = new ClientBrandingTheme();
				brandingTheme = setValues();
				brandingTheme.setThemeName(yourBox.getText());
				ViewManager.getInstance().createObject(brandingTheme,
						CopyThemeDialog.this);
				removeFromParent();

			}
		});
		Button canButton = new Button(FinanceApplication.getSettingsMessages()
				.cancelButton());
		canButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				removeFromParent();
			}
		});
		copyPanel.add(yourLabel);
		copyPanel.add(yourBox);
		copyPanel.add(okButton);
		copyPanel.add(canButton);
		okbtn.setVisible(false);
		cancelBtn.setVisible(false);
		mainPanel.add(copyPanel);

	}

	protected ClientBrandingTheme setValues() {

		ClientBrandingTheme clientBrandingTheme = new ClientBrandingTheme();
		clientBrandingTheme.setPageSizeType(theme.getPageSizeType());
		clientBrandingTheme.setAddressPadding(theme.getAddressPadding());
		clientBrandingTheme.setBottomMargin(theme.getBottomMargin());
		clientBrandingTheme.setMarginsMeasurementType(theme
				.getMarginsMeasurementType());
		clientBrandingTheme.setFont(theme.getFont());
		clientBrandingTheme.setFontSize(theme.getFontSize());
		clientBrandingTheme.setOverDueInvoiceTitle(theme
				.getOverDueInvoiceTitle());
		clientBrandingTheme.setCreditMemoTitle(theme.getCreditMemoTitle());
		clientBrandingTheme.setStatementTitle(theme.getStatementTitle());
		clientBrandingTheme.setContactDetails(theme.getContactDetails());
		clientBrandingTheme.setTerms_And_Payment_Advice(theme
				.getTerms_And_Payment_Advice());
		clientBrandingTheme.setPayPalEmailID(theme.getPayPalEmailID());
		clientBrandingTheme.setShowLogo(theme.isShowLogo());
		clientBrandingTheme.setShowColumnHeadings(theme.isShowColumnHeadings());
		clientBrandingTheme.setShowRegisteredAddress(theme
				.isShowRegisteredAddress());
		clientBrandingTheme.setShowTaxColumn(theme.isShowTaxColumn());
		clientBrandingTheme.setShowTaxNumber(theme.isShowTaxNumber());
		clientBrandingTheme.setShowUnitPrice_And_Quantity(theme
				.isShowUnitPrice_And_Quantity());

		return clientBrandingTheme;
	}
}
