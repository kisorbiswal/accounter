package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;

public class VendorDetailsPanel extends VerticalPanel {
	ClientVendor selectedVendor;
	LabelItem name, email, currency, fax, vendorsince, webpageadress, notes;
	AmountLabel balance, openingBalance;
	Label heading, vendName;
	protected static final AccounterMessages messages = Global.get().messages();

	public VendorDetailsPanel(ClientVendor clientVendor) {
		this.selectedVendor = clientVendor;
		createControls();
		if (clientVendor != null) {
			showVendorDetails(clientVendor);
		}

	}

	private void createControls() {

		name = new LabelItem();
		name.setTitle(messages.name());

		email = new LabelItem();
		email.setTitle(messages.email());

		balance = new AmountLabel(messages.balance());

		currency = new LabelItem();
		currency.setTitle(messages.currency());

		fax = new LabelItem();
		fax.setTitle(messages.faxNumber());

		vendorsince = new LabelItem();
		vendorsince.setTitle(messages.payeeSince(Global.get().Vendor()));

		webpageadress = new LabelItem();
		webpageadress.setTitle(messages.webPageAddress());

		openingBalance = new AmountLabel(messages.balanceAsOf());

		notes = new LabelItem();
		notes.setTitle(messages.notes());

		DynamicForm leftform = new DynamicForm();
		DynamicForm rightform = new DynamicForm();

		leftform.setFields(name, balance, openingBalance, currency, vendorsince);

		rightform.setFields(email, fax, webpageadress, notes);

		HorizontalPanel hp = new HorizontalPanel();
		HorizontalPanel headingPanel = new HorizontalPanel();
		headingPanel.addStyleName("customers_detail_panel");
		heading = new Label(messages.payeeDetails(Global.get().Vendors())
				+ " :");
		headingPanel.add(heading);
		vendName = new Label();
		vendName.setText(messages.noPayeeSelected(Global.get().Vendor()));
		headingPanel.add(heading);
		headingPanel.add(vendName);
		headingPanel.setCellWidth(heading, "50%");
		headingPanel.setCellWidth(vendName, "50%");
		add(headingPanel);
		hp.add(leftform);
		hp.add(rightform);
		leftform.setCellSpacing(10);
		rightform.setCellSpacing(10);
		hp.setCellWidth(leftform, "50%");
		hp.setCellWidth(rightform, "50%");

		add(hp);
		headingPanel.setWidth("100%");
		hp.setWidth("100%");
		this.setWidth("100%");
		hp.getElement().getParentElement().addClassName("details-Panel");
	}

	protected void showVendorDetails(ClientVendor selectedVendor) {
		if (selectedVendor != null) {
			vendName.setText(selectedVendor.getName());
			name.setValue(selectedVendor.getName());

			email.setValue(selectedVendor.getEmail());

			balance.setAmount(selectedVendor.getBalance());

			currency.setValue(Accounter.getCompany()
					.getCurrency(selectedVendor.getCurrency()).getFormalName());

			fax.setValue(selectedVendor.getFaxNo());

			vendorsince.setValue(new ClientFinanceDate(selectedVendor
					.getPayeeSince()).toString());

			webpageadress.setValue(selectedVendor.getWebPageAddress());

			openingBalance.setAmount(selectedVendor.getOpeningBalance());

			notes.setValue(selectedVendor.getMemo());

		} else {
			name.setValue("");
			email.setValue("");
			balance.setAmount(0.00);
			currency.setValue("");
			fax.setValue("");
			vendorsince.setValue("");
			webpageadress.setValue("");
			openingBalance.setAmount(0.00);
			notes.setValue("");
		}
	}

	public void setVendor(ClientVendor vendor) {
		this.selectedVendor = vendor;
	}

	public ClientVendor getVendor() {
		return selectedVendor;
	}

}
