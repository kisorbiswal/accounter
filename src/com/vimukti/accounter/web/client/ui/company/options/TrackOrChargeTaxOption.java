/**
 * 
 */
package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
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
			onepeTransactionRadioGroup.setValue(messages.oneperdetailline());
		else
			onepeTransactionRadioGroup.setValue(messages.onepertransaction());

		if (getCompany().getCountryPreferences().isTDSAvailable()) {
			enableTaxTdsCheckbox.setValue(getCompanyPreferences()
					.isTDSEnabled());
		}
	}

	public void createControls() {
		trackCheckbox = new CheckboxItem(messages.chargeOrTrackTax(), "header");

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

		trackCheckbox.addChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				hidePanel.setVisible(trackCheckbox.getValue());
			}
		});
		enableTaxLabel = new LabelItem(messages.enableTrackingDescription(),
				"organisation_comment");

		enableTaxTdsCheckbox = new CheckboxItem(messages.enableTDS(), "header");
		enableTDSdecs = new LabelItem(messages.enbleTDSdescription(),
				"organisation_comment");

		enableTaxTdsCheckbox.setVisible(getCompany().getCountryPreferences()
				.isTDSAvailable());
		enableTDSdecs.setVisible(getCompany().getCountryPreferences()
				.isTDSAvailable());

		add(trackCheckbox);
		add(trackCheckBoxDescLabel);

		hidePanel = new StyledPanel("trackTaxhidePanel");
		hidePanel.add(taxItemTransactionLabel);
		hidePanel.add(oneperTransactionLabel);
		hidePanel.add(oneperdetaillineLabel);
		hidePanel.add(onepeTransactionRadioGroup);
		hidePanel.add(enableTaxCheckbox);
		hidePanel.add(enableTaxLabel);
		hidePanel.add(enableTaxTdsCheckbox);
		hidePanel.add(enableTDSdecs);
		add(hidePanel);

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
			getCompanyPreferences().setTaxPerDetailLine(false);
		} else {
			getCompanyPreferences().setTaxPerDetailLine(true);
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
