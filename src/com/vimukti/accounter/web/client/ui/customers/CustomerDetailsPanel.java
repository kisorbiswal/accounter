package com.vimukti.accounter.web.client.ui.customers;

import java.util.Set;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;

public class CustomerDetailsPanel extends VerticalPanel {
	ClientCustomer selectedCustomer;
	LabelItem name, email, currency, fax, customersince, webpageadress, notes,
			address;
	AmountLabel balance, openingBalance;
	Label heading, custname;
	private ClientAddress payeeAddress;
	private Set<ClientAddress> addressListOfCustomer;
	private DynamicForm leftform, rightform;
	protected static final AccounterMessages messages = Global.get().messages();

	public CustomerDetailsPanel(ClientCustomer clientCustomer) {
		this.selectedCustomer = clientCustomer;
		createControls();
		showCustomerDetails(clientCustomer);
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

		customersince = new LabelItem();
		customersince.setTitle(messages.payeeSince(Global.get().Customer()));

		webpageadress = new LabelItem();
		webpageadress.setTitle(messages.webPageAddress());

		notes = new LabelItem();
		notes.setTitle(messages.notes());

		address = new LabelItem();
		address.setTitle(messages.address());

		openingBalance = new AmountLabel(messages.balanceAsOf());

		leftform = new DynamicForm();
		rightform = new DynamicForm();

		leftform.setFields(name, balance, openingBalance, currency,
				customersince);

		rightform.setFields(email, fax, webpageadress, notes, address);
		rightform.addStyleName("customers_detail_rightpanel");

		HorizontalPanel hp = new HorizontalPanel();
		HorizontalPanel headingPanel = new HorizontalPanel();
		headingPanel.addStyleName("customers_detail_panel");
		heading = new Label(messages.payeeDetails(Global.get().Customers())
				+ " :");
		custname = new Label();
		custname.setText(messages.noPayeeSelected(Global.get().Customer()));
		headingPanel.add(heading);
		headingPanel.add(custname);
		headingPanel.setCellWidth(heading, "50%");
		headingPanel.setCellWidth(custname, "50%");
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

	protected void showCustomerDetails(ClientCustomer selectedCustomer) {
		if (selectedCustomer != null) {
			addressListOfCustomer = selectedCustomer.getAddress();
			custname.setText(selectedCustomer.getName());
			name.setValue(selectedCustomer.getName());

			email.setValue(selectedCustomer.getEmail());

			balance.setAmount(selectedCustomer.getBalance());

			currency.setValue(Accounter.getCompany()
					.getCurrency(selectedCustomer.getCurrency())
					.getFormalName());

			fax.setValue(selectedCustomer.getFaxNo());
			customersince.setValue(UIUtils
					.getDateByCompanyType(new ClientFinanceDate(
							selectedCustomer.getPayeeSince())));

			webpageadress.setValue(selectedCustomer.getWebPageAddress());

			openingBalance.setAmount(selectedCustomer.getOpeningBalance());

			notes.setValue(selectedCustomer.getMemo());
			notes.getMainWidget().getElement().getParentElement()
					.addClassName("customer-detail-notespanel");

			payeeAddress = getAddress(ClientAddress.TYPE_BILL_TO);
			if (payeeAddress != null) {

				address.setValue(getValidAddress(payeeAddress));

			} else
				address.setValue("");

		} else {
			name.setValue("");
			email.setValue("");
			balance.setAmount(0.00);
			currency.setValue("");
			fax.setValue("");
			customersince.setValue("");
			webpageadress.setValue("");
			openingBalance.setAmount(0.00);
			notes.setValue("");
			address.setValue("");
		}
	}

	public void setCustomer(ClientCustomer customer) {
		this.selectedCustomer = customer;
		showCustomerDetails(customer);
	}

	public ClientCustomer getCustomer() {
		return selectedCustomer;
	}

	public ClientAddress getAddress(int type) {
		for (ClientAddress address : addressListOfCustomer) {
			if (address.getType() == type) {
				return address;
			}

		}
		return null;
	}

	protected String getValidAddress(ClientAddress address) {
		String toToSet = new String();
		if (address.getAddress1() != null && !address.getAddress1().isEmpty()) {
			toToSet = address.getAddress1().toString() + "," + "\n";
		}

		if (address.getStreet() != null && !address.getStreet().isEmpty()) {
			toToSet += address.getStreet().toString() + "," + "\n";
		}

		if (address.getCity() != null && !address.getCity().isEmpty()) {
			toToSet += address.getCity().toString() + "," + "\n";
		}

		if (address.getStateOrProvinence() != null
				&& !address.getStateOrProvinence().isEmpty()) {
			toToSet += address.getStateOrProvinence() + "," + "\n";
		}
		if (address.getZipOrPostalCode() != null
				&& !address.getZipOrPostalCode().isEmpty()) {
			toToSet += address.getZipOrPostalCode() + "," + "\n";
		}
		if (address.getCountryOrRegion() != null
				&& !address.getCountryOrRegion().isEmpty()) {
			toToSet += address.getCountryOrRegion() + ".";
		}
		return toToSet;
	}

}
