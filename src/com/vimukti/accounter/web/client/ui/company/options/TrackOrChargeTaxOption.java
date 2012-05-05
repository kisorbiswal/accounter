/**
 * 
 */
package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;

/**
 * @author vimukti2
 * 
 */
public class TrackOrChargeTaxOption extends AbstractPreferenceOption {

	CheckboxItem trackCheckbox;

	LabelItem trackCheckBoxDescLabel;

	LabelItem taxItemTransactionLabel;

	LabelItem oneperdetaillineLabel;
	LabelItem oneperTransactionLabel;

	RadioGroupItem onepeTransactionRadioGroup;

	CheckboxItem enableTaxCheckbox;
	LabelItem enableTaxLabel;

	CheckboxItem enableTaxTdsCheckbox;
	LabelItem enableTDSdecs;

	StyledPanel hidePanel;

	StyledPanel radioButtonPanel;

	StyledPanel mainPanel;

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
		super("");
		createControls();
		initData();
	}

	public void initData() {
		trackCheckbox.setValue(getCompanyPreferences().isTrackTax());
		hidePanel.setVisible(getCompanyPreferences().isTrackTax());
		enableTaxCheckbox.setValue(getCompanyPreferences().isTrackPaidTax());
		if (getCompanyPreferences().isTaxPerDetailLine())
			onepeTransactionRadioGroup.setValue(messages.onepertransaction());
		else
			onepeTransactionRadioGroup.setValue(messages.oneperdetailline());
		if (getCompany().getCountryPreferences().isTDSAvailable()) {
			enableTaxTdsCheckbox.setValue(getCompanyPreferences()
					.isTDSEnabled());
		}
	}

	public void createControls() {
		mainPanel = new StyledPanel("trackOrChargeTaxOption");
		trackCheckbox = new CheckboxItem(messages.chargeOrTrackTax(),
				"trackCheckbox");

		trackCheckBoxDescLabel = new LabelItem(messages.descChrageTrackTax(),
				"organisation_comment");

		taxItemTransactionLabel = new LabelItem(messages.taxtItemTransaction(),
				"taxItemTransactionLabel");

		oneperTransactionLabel = new LabelItem(messages.oneperDescription(),
				"organisation_comment");
		oneperdetaillineLabel = new LabelItem(
				messages.oneperDetailDescription(), "organisation_comment");

		onepeTransactionRadioGroup = new RadioGroupItem();
		onepeTransactionRadioGroup.setShowTitle(false);
		onepeTransactionRadioGroup.setGroupName("onepeTransactionRadioGroup");
		onepeTransactionRadioGroup.setValueMap(messages.onepertransaction(),
				messages.oneperdetailline());
		onepeTransactionRadioGroup
				.setDefaultValue(messages.onepertransaction());

		enableTaxCheckbox = new CheckboxItem(messages.enableTracking(),
				"enableTaxCheckbox");

		trackCheckbox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hidePanel.setVisible(trackCheckbox.getValue());

			}
		});
		enableTaxLabel = new LabelItem(messages.enableTrackingDescription(),
				"organisation_comment");

		enableTaxTdsCheckbox = new CheckboxItem(messages.enableTDS(),
				"enableTaxTdsCheckbox");
		enableTDSdecs = new LabelItem(messages.enbleTDSdescription(),
				"organisation_comment");

		enableTaxTdsCheckbox.setVisible(getCompany().getCountryPreferences()
				.isTDSAvailable());
		enableTDSdecs.setVisible(getCompany().getCountryPreferences()
				.isTDSAvailable());

		mainPanel.add(trackCheckbox);
		mainPanel.add(trackCheckBoxDescLabel);

		hidePanel = new StyledPanel("trackTaxhidePanel");
		hidePanel.add(taxItemTransactionLabel);
		hidePanel.add(oneperTransactionLabel);
		hidePanel.add(oneperdetaillineLabel);
		hidePanel.add(onepeTransactionRadioGroup);
		hidePanel.add(enableTaxCheckbox);
		hidePanel.add(enableTaxLabel);
		hidePanel.add(enableTaxTdsCheckbox);
		hidePanel.add(enableTDSdecs);
		mainPanel.add(hidePanel);

	}

	@Override
	public String getTitle() {

		return messages.trackOrChargeTax();
	}

	@Override
	public void onSave() {
		getCompanyPreferences().setTaxTrack(trackCheckbox.getValue());
		if (onepeTransactionRadioGroup.getValue().equals(
				messages.onepertransaction())) {
			getCompanyPreferences().setTaxPerDetailLine(true);
		} else {
			getCompanyPreferences().setTaxPerDetailLine(false);
		}
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
