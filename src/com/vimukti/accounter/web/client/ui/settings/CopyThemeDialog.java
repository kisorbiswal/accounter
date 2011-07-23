package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;
import com.vimukti.accounter.web.client.ui.core.ViewManager;

@SuppressWarnings("unchecked")
public class CopyThemeDialog extends BaseDialog {

	private ClientBrandingTheme theme;
	TextBox nameBox;

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
		Label yourLabel = new Label(Accounter.getSettingsMessages()
				.yourTitle());
		nameBox = new TextBox();
		okbtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				try {
					if (validate()) {
						if (!Utility.isObjectExist(Accounter
								.getCompany().getBrandingTheme(), nameBox
								.getText())) {
							ClientBrandingTheme brandingTheme = new ClientBrandingTheme();
							brandingTheme = setValues();
							brandingTheme.setThemeName(nameBox.getText());
							ViewManager.getInstance().createObject(
									brandingTheme, CopyThemeDialog.this);
						} else {
							MainFinanceWindow.getViewManager()
									.showErrorInCurrectDialog(
											"Theme name is already exist.");
						}
						// removeFromParent();
					}
				} catch (InvalidTransactionEntryException e) {
					e.printStackTrace();
				} catch (InvalidEntryException e) {
					e.printStackTrace();
				}

			}
		});
		cancelBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				removeFromParent();
			}
		});
		copyPanel.add(yourLabel);
		copyPanel.add(nameBox);

		setBodyLayout(copyPanel);

	}

	@Override
	protected boolean validate() throws InvalidTransactionEntryException,
			InvalidEntryException {
		String name = nameBox.getValue();
		if (name == null || name.isEmpty()) {
			MainFinanceWindow.getViewManager().showErrorInCurrectDialog(
					"Please enter Theme name.");
			return false;
		} else
			return true;
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

	@Override
	public void saveSuccess(IAccounterCore object) {
		removeFromParent();
		super.saveSuccess(object);
		SettingsActionFactory.getInvoiceBrandingAction().run(null, true);
	}

	@Override
	protected String getViewTitle() {
		return Accounter.getSettingsMessages().copyTheme();
	}
}
