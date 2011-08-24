/**
 * 
 */
package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;

/**
 * @author Administrator
 * 
 */
public class SetupReferPage extends AbstractSetupPage {

	private static SetupReferPageUiBinder uiBinder = GWT
			.create(SetupReferPageUiBinder.class);
	@UiField
	VerticalPanel mainViewPanel;
	@UiField
	Label headerLabel;
	@UiField
	Label supplierLabel;
	@UiField
	Label customerLabel;
	@UiField
	Label accountLabel;
	@UiField
	ListBox accountListBox;
	@UiField
	Label customerCommentLabel;
	@UiField
	Label supplierCommentLabel;
	@UiField
	Label accountCommentLabel;
	@UiField
	ListBox customerListBox;
	@UiField
	ListBox supplierListBox;

	interface SetupReferPageUiBinder extends UiBinder<Widget, SetupReferPage> {
	}

	/**
	 * Because this class has a default constructor, it can be used as a binder
	 * template. In other words, it can be used in other *.ui.xml files as
	 * follows: <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 * xmlns:g="urn:import:**user's package**">
	 * <g:**UserClassName**>Hello!</g:**UserClassName> </ui:UiBinder> Note that
	 * depending on the widget that is used, it may be necessary to implement
	 * HasHTML instead of HasText.
	 */
	public SetupReferPage() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	@Override
	protected void createControls() {
		headerLabel.setText(accounterConstants.howDoYouRefer());
		// adding Items to customer list box
		customerListBox.addItem(Global.get().Customer());
		customerListBox.addItem(accounterConstants.Client());
		customerListBox.addItem(accounterConstants.Tenant());
		customerListBox.addItem(accounterConstants.Donar());
		customerListBox.addItem(accounterConstants.Guest());
		customerListBox.addItem(accounterConstants.Member());
		customerListBox.addItem(accounterConstants.Patitent());

		supplierListBox.addItem(Accounter.messages().Supplier(
				Global.get().Vendor()));
		supplierListBox.addItem(accounterConstants.Vendor());

		accountListBox.addItem(accounterConstants.Account());
		accountListBox.addItem(accounterConstants.Ledger());

		customerLabel.setText(Global.get().Customer());
		supplierLabel.setText(Accounter.messages().Supplier(
				Global.get().Vendor()));
		accountLabel.setText(accounterConstants.Account());

		customerCommentLabel.setText(Accounter.messages()
				.howDoYouReferYourCustoemrs(Global.get().customer()));
		supplierCommentLabel.setText(Accounter.messages()
				.howDoYouReferYourSuppliers(Global.get().vendor()));
		accountCommentLabel.setText(Accounter.messages()
				.howDoYouReferYourAccounts(Global.get().customer()));

	}

	@Override
	public boolean canShow() {
		return true;
	}

	public void onLoad() {

		int referCustomers = preferences.getReferCustomers();
		int referSuplliers = preferences.getReferVendors();
		int referAccounts = preferences.getReferAccounts();

		if (referCustomers != 0)
			customerListBox.setSelectedIndex(referCustomers);
		if (referAccounts != 0)
			accountListBox.setSelectedIndex(referAccounts);
		if (referSuplliers != 0)
			supplierListBox.setSelectedIndex(referSuplliers);
	}

	@Override
	public void onSave() {
		int customer = customerListBox.getSelectedIndex();
		int suplier = supplierListBox.getSelectedIndex();
		int accounts = accountListBox.getSelectedIndex();
		if (customer != 0)
			preferences.setReferCustomers(customer);
		if (suplier != 0)
			preferences.setReferVendors(suplier);
		if (accounts != 0)
			preferences.setReferAccounts(accounts);
	}

	@Override
	protected boolean validate() {
		if (customerListBox.getSelectedIndex() == -1
				&& supplierListBox.getSelectedIndex() == -1
				&& accountListBox.getSelectedIndex() == -1) {
			Accounter.showError(accounterConstants.howDoYouRefer() + " "
					+ Accounter.messages().customers(Global.get().customer())
					+ " " + Global.get().vendor() + " "
					+ Accounter.messages().accounts(Global.get().account())
					+ "?");
			return false;
		} else if (customerListBox.getSelectedIndex() == -1
				&& supplierListBox.getSelectedIndex() == -1) {
			Accounter.showError(accounterConstants.howDoYouRefer() + " "
					+ Accounter.messages().customers(Global.get().customer())
					+ " " + Global.get().vendor() + "?");
			return false;
		} else if (supplierListBox.getSelectedIndex() == -1
				&& accountListBox.getSelectedIndex() == -1) {
			Accounter.showError(accounterConstants.howDoYouRefer() + " "
					+ Global.get().vendor() + " "
					+ Accounter.messages().accounts(Global.get().account())
					+ "?");
			return false;
		} else if (customerListBox.getSelectedIndex() == -1
				&& accountListBox.getSelectedIndex() == -1) {
			Accounter.showError(accounterConstants.howDoYouRefer() + " "
					+ Accounter.messages().customers(Global.get().customer())
					+ " "
					+ Accounter.messages().accounts(Global.get().Account()));
			return false;
		} else if (customerListBox.getSelectedIndex() == -1) {
			Accounter.showError(Accounter.messages()
					.howDoYouReferYourCustoemrs(Global.get().customer()));
			return false;
		} else if (supplierListBox.getSelectedIndex() == -1) {
			Accounter.showError(Accounter.messages()
					.howDoYouReferYourSuppliers(Global.get().vendor()));
			return false;
		} else if (accountListBox.getSelectedIndex() == -1) {
			Accounter.showError(Accounter.messages().howDoYouReferYourAccounts(
					Global.get().Account()));
			return false;
		} else {
			return true;
		}
	}

}
