/**
 * 
 */
package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

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
	Grid referPanel;
	@UiField
	ListBox customerListBox;
	@UiField
	Label supplierLabel;
	@UiField
	Label customerLabel;
	@UiField
	ListBox supplierListBox;
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
	protected void onLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onSave() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createControls() {
		headerLabel.setText(accounterConstants.howDoYouRefer());
		// adding Items to customer list box
		customerListBox.addItem(accounterConstants.customers());
		customerListBox.addItem(accounterConstants.clients());
		customerListBox.addItem(accounterConstants.tenants());

		supplierListBox.addItem(accounterConstants.suppliers());
		supplierListBox.addItem(accounterConstants.vendors());

		accountListBox.addItem(accounterConstants.accounts());
		accountListBox.addItem(accounterConstants.legands());

		customerLabel.setText(accounterConstants.customer());
		supplierLabel.setText(accounterConstants.supplier());
		accountLabel.setText(accounterConstants.account());

		customerCommentLabel.setText(accounterConstants
				.howDoYouReferYourCustoemrs());
		supplierCommentLabel.setText(accounterConstants
				.howDoYouReferYourSuppliers());
		accountCommentLabel.setText(accounterConstants
				.howDoYouReferYourAccounts());

	}

	@Override
	public boolean doShow() {
		return true;
	}

}
