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
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.StyledPanel;

/**
 * @author vimukti2
 * 
 */
public class TrackOrChargeTaxOption extends AbstractPreferenceOption {

	private static TrackOrChargeTaxOptionUiBinder uiBinder = GWT
			.create(TrackOrChargeTaxOptionUiBinder.class);

	@UiField
	RadioButton onepeTransactionRadioButton;
	@UiField
	Label oneperTransactionLabel;
	@UiField
	RadioButton oneperdetaillineRadioButton;
	@UiField
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
	FlowPanel hidePanel;
	@UiField
	FlowPanel radioButtonPanel;
	@UiField
	CheckBox enableTaxTdsCheckbox;
	@UiField
	Label enableTDSdecs;

	interface TrackOrChargeTaxOptionUiBinder extends
			UiBinder<Widget, TrackOrChargeTaxOption> {
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
	public TrackOrChargeTaxOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	public TrackOrChargeTaxOption(String firstName) {
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
		if (getCompany().getCountryPreferences().isTDSAvailable()) {
			enableTaxTdsCheckbox.setValue(getCompanyPreferences()
					.isTDSEnabled());
		}
	}

	public void createControls() {

		trackCheckbox.setText(messages.chargeOrTrackTax());
		trackCheckBoxDescLabel.setText(messages.descChrageTrackTax());

		taxItemTransactionLabel.setText(messages.taxtItemTransaction());

		onepeTransactionRadioButton.setText(messages.onepertransaction());
		oneperTransactionLabel.setText(messages.oneperDescription());

		oneperdetaillineRadioButton.setText(messages.oneperdetailline());
		oneperdetaillineLabel.setText(messages.oneperDetailDescription());

		enableTaxCheckbox.setText(messages.enableTracking());
		enableTaxLabel.setText(messages.enableTrackingDescription());

		oneperdetaillineRadioButton.setName(messages.taxCode());
		onepeTransactionRadioButton.setName(messages.taxCode());

		trackCheckBoxDescLabel.setStyleName("organisation_comment");
		oneperTransactionLabel.setStyleName("organisation_comment");
		oneperdetaillineLabel.setStyleName("organisation_comment");
		enableTaxLabel.setStyleName("organisation_comment");

		trackCheckbox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hidePanel.setVisible(trackCheckbox.getValue());

			}
		});
		enableTaxTdsCheckbox.setText(messages.enableTDS());
		enableTDSdecs.setText(messages.enbleTDSdescription());
		enableTDSdecs.setStyleName("organisation_comment");
		enableTaxTdsCheckbox.setVisible(getCompany().getCountryPreferences()
				.isTDSAvailable());
		enableTDSdecs.setVisible(getCompany().getCountryPreferences()
				.isTDSAvailable());
		// enableTaxTdsCheckbox.setVisible(false);
		// enableTDSdecs.setVisible(false);
	}

	@Override
	public String getTitle() {

		return messages.trackOrChargeTax();
	}

	@Override
	public void onSave() {
		getCompanyPreferences().setTaxTrack(trackCheckbox.getValue());
		getCompanyPreferences().setTaxPerDetailLine(
				oneperdetaillineRadioButton.getValue());
		getCompanyPreferences().setTrackPaidTax(enableTaxCheckbox.getValue());
		if (getCompany().getCountryPreferences().isTDSAvailable()) {
			getCompanyPreferences()
					.setTDSEnabled(
							trackCheckbox.getValue()
									&& enableTaxTdsCheckbox.getValue());
		}
	}

	@Override
	public String getAnchor() {
		return messages.trackOrChargeTax();
	}

}
