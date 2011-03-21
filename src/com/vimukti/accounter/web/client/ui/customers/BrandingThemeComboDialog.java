package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.core.ClientInvoice;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.BrandingThemeCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

@SuppressWarnings("unchecked")
public class BrandingThemeComboDialog extends BaseDialog {
	private BrandingThemeCombo brandingThemeTypeCombo;
	private ClientTransaction clientTransaction;
	private ClientBrandingTheme brandingTheme;
	private Button okButton, cancelButton;

	public BrandingThemeComboDialog(String title, String desc,
			ClientTransaction clientTransaction) {
		super(title, desc);
		this.clientTransaction = clientTransaction;
		createControls();
	}

	public BrandingThemeComboDialog(String title, String desc) {
		super(title, desc);
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

	}

	private void createControls() {
		brandingThemeTypeCombo = new BrandingThemeCombo(FinanceApplication
				.getSettingsMessages().selectTheme());
		brandingTheme = new ClientBrandingTheme();
		okButton = new Button(FinanceApplication.getSettingsMessages().ok());
		cancelButton = new Button(FinanceApplication.getSettingsMessages()
				.cancelButton());
		brandingThemeTypeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientBrandingTheme>() {

					@Override
					public void selectedComboBoxItem(
							ClientBrandingTheme selectItem) {
						brandingTheme = selectItem;
					}
				});

		okButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (brandingThemeTypeCombo.getSelectedValue().equals(null)) {
					brandingThemeTypeCombo
							.setSelected(FinanceApplication
									.getSettingsMessages()
									.standardTheme());
				}
				print();
				hide();
			}
		});
		cancelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		okbtn.setVisible(false);
		cancelBtn.setVisible(false);

		DynamicForm dynamicForm = new DynamicForm();
		VerticalPanel comboPanel = new VerticalPanel();
		HorizontalPanel buttonPanel = new HorizontalPanel();

		dynamicForm.setFields(brandingThemeTypeCombo);

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		comboPanel.add(dynamicForm);
		comboPanel.add(buttonPanel);

		mainPanel.add(comboPanel);
	}

	private void print() {
		UIUtils.downloadAttachment(((ClientInvoice) clientTransaction)
				.getStringID(), ClientTransaction.TYPE_INVOICE, brandingTheme
				.getStringID());
	}
}
