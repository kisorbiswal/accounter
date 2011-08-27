package com.vimukti.accounter.web.client.ui.customers;

import java.util.List;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.core.ClientInvoice;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.InvoicesList;
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
	private List<InvoicesList> list;

	public BrandingThemeComboDialog(String title, String desc,
			ClientTransaction clientTransaction) {
		super(title, desc);
		this.clientTransaction = clientTransaction;
		createControls();
	}

	public BrandingThemeComboDialog(String title, String desc,
			List<InvoicesList> list) {
		super(title, desc);
		this.list = list;
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
		brandingThemeTypeCombo.setThemeDialog(this);

		DynamicForm dynamicForm = new DynamicForm();
		VerticalPanel comboPanel = new VerticalPanel();
		HorizontalPanel buttonPanel = new HorizontalPanel();

		dynamicForm.setFields(brandingThemeTypeCombo);

		comboPanel.add(dynamicForm);
		comboPanel.add(buttonPanel);

		setBodyLayout(comboPanel);
	}

	private void print() {
		if (list == null) {
			// for printing individual invoice and customer credit memo pdf
			// documents
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
		} else {
			// for printing multiple documents
			StringBuffer ids = new StringBuffer();

			for (int i = 0; i < list.size(); i++) {
				InvoicesList invoice = list.get(i);

				if (invoice.isPrint()) {

					String id = String.valueOf(invoice.getTransactionId());

					ids = ids.append(id + ",");

				}
			}

			String[] arrayIds = ids.toString().split(",");
			int type = 0;
			for (int i = 0; i < list.size(); i++) {
				InvoicesList invoicesList = list.get(i);
				if (invoicesList.isPrint()) {
					if (Integer.parseInt(arrayIds[0]) == invoicesList
							.getTransactionId()) {

						type = invoicesList.getType();
					}
				}

			}

			if (type == ClientTransaction.TYPE_INVOICE) {
				UIUtils.downloadMultipleAttachment(ids.toString(),
						ClientTransaction.TYPE_INVOICE, brandingTheme.getID());

			} else if (type == ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO) {
				UIUtils.downloadMultipleAttachment(ids.toString(),
						ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO,
						brandingTheme.getID());

			}

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
