package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.core.ClientInvoice;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.BrandingThemeCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class BrandingThemeComboDialog extends BaseDialog {
	private BrandingThemeCombo brandingThemeTypeCombo;
	private ClientTransaction clientTransaction;
	private ClientBrandingTheme brandingTheme;

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
		brandingThemeTypeCombo = new BrandingThemeCombo(Accounter.constants()
				.selectTheme());
		brandingTheme = new ClientBrandingTheme();
		brandingThemeTypeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientBrandingTheme>() {

					@Override
					public void selectedComboBoxItem(
							ClientBrandingTheme selectItem) {
						brandingTheme = selectItem;
					}
				});

		DynamicForm dynamicForm = new DynamicForm();
		VerticalPanel comboPanel = new VerticalPanel();
		HorizontalPanel buttonPanel = new HorizontalPanel();

		dynamicForm.setFields(brandingThemeTypeCombo);

		comboPanel.add(dynamicForm);
		comboPanel.add(buttonPanel);

		setBodyLayout(comboPanel);
	}

	private void print() {

		if (ClientTransaction.TYPE_INVOICE == clientTransaction.getType()) {
			UIUtils.downloadAttachment(
					((ClientInvoice) clientTransaction).getID(),
					ClientTransaction.TYPE_INVOICE, brandingTheme.getID());
		} else if (ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO == clientTransaction
				.getType()) {
			UIUtils.downloadAttachment(clientTransaction.getID(),
					ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO,
					brandingTheme.getID());
		}

	}

	@Override
	protected boolean onOK() {
		if (brandingThemeTypeCombo.getSelectedValue().equals(null)) {
			brandingThemeTypeCombo.setSelected(Accounter.constants()
					.standardTheme());
		}
		print();
		return true;
	}

}
