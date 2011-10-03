/**
 * 
 */
package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author vimukti2
 * 
 */
public class CustomerAndVendorsSettingsOption extends AbstractPreferenceOption {

	private static CustomerAndVendorsSettingsOptionUiBinder uiBinder = GWT
			.create(CustomerAndVendorsSettingsOptionUiBinder.class);

	RadioButton onepeTransactionRadioButton;
	Label oneperTransactionLabel;
	RadioButton oneperdetaillineRadioButton;
	Label oneperdetaillineLabel;
	@UiField
	CheckBox enableTaxCheckbox;
	@UiField
	Label enableTaxLabel;
	@UiField
	Label taxItemTransactionLabel;
	@UiField
	CheckBox trackCheckbox;
	@UiField
	Label trackLabel;
	@UiField
	Label trackCheckBoxDescLabel;
	@UiField
	VerticalPanel hidePanel;
	@UiField
	VerticalPanel radioButtonPanel;

	interface CustomerAndVendorsSettingsOptionUiBinder extends
			UiBinder<Widget, CustomerAndVendorsSettingsOption> {
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
	public CustomerAndVendorsSettingsOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	public CustomerAndVendorsSettingsOption(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));

	}

	public void initData() {
		trackCheckbox.setValue(getCompanyPreferences().isTrackTax());
		hidePanel.setVisible(getCompanyPreferences().isTrackTax());
		enableTaxCheckbox.setValue(getCompanyPreferences().isTrackPaidTax());
		if (getCompanyPreferences().isTaxPerDetailLine())
			oneperdetaillineRadioButton.setValue(true);
		else
			onepeTransactionRadioButton.setValue(true);
	}

	public void createControls() {

		onepeTransactionRadioButton = new RadioButton(constants.taxCode(),
				constants.onepertransaction());
		oneperdetaillineRadioButton = new RadioButton(constants.taxCode(),
				constants.oneperdetailline());
		oneperdetaillineLabel = new Label();
		oneperTransactionLabel = new Label();

		radioButtonPanel.add(oneperdetaillineRadioButton);
		radioButtonPanel.add(oneperdetaillineLabel);

		radioButtonPanel.add(onepeTransactionRadioButton);
		radioButtonPanel.add(oneperTransactionLabel);

		trackCheckbox.setText(constants.chargeOrTrackTax());
		trackCheckBoxDescLabel.setText(constants.descChrageTrackTax());
		trackCheckBoxDescLabel.setStyleName("organisation_comment");
		taxItemTransactionLabel.setText(constants.taxtItemTransaction());
		oneperTransactionLabel.setText(constants.oneperDescription());
		oneperdetaillineLabel.setText(constants.oneperDetailDescription());
		oneperTransactionLabel.setStyleName("organisation_comment");
		oneperdetaillineLabel.setStyleName("organisation_comment");
		enableTaxCheckbox.setText(constants.enableTracking());
		enableTaxLabel.setText(constants.enableTrackingDescription());
		enableTaxLabel.setStyleName("organisation_comment");

		trackCheckbox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hidePanel.setVisible(trackCheckbox.getValue());

			}
		});

	}

	@Override
	public String getTitle() {

		return constants.customerAndvendorSettings();
	}

	@Override
	public void onSave() {
		getCompanyPreferences().setTaxTrack(trackCheckbox.getValue());
		getCompanyPreferences().setTaxPerDetailLine(
				oneperdetaillineRadioButton.getValue());
		getCompanyPreferences().setTrackPaidTax(enableTaxCheckbox.getValue());
	}

	@Override
	public String getAnchor() {
		return constants.customerAndvendorSettings();
	}

}
