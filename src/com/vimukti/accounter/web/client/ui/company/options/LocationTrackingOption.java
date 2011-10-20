package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientLocation;
import com.vimukti.accounter.web.client.ui.Accounter;

public class LocationTrackingOption extends AbstractPreferenceOption {

	@UiField
	RadioButton buisinessRadioButton;
	@UiField
	RadioButton locationRadioButton;
	@UiField
	RadioButton departmentRadioButton;
	@UiField
	RadioButton divisionRadioButton;
	@UiField
	RadioButton propertyRadioButton;
	@UiField
	RadioButton storeRadioButton;
	@UiField
	RadioButton territoryRadioButton;
	@UiField
	Label radioButtonsHeaderLabel;
	@UiField
	CheckBox locationTrackingCheckBoxItm;
	@UiField
	Label locationTrackingCheckBoxLabel;
	@UiField
	VerticalPanel radioButtonsPanel;
	@UiField
	HorizontalPanel hpanel;
	@UiField
	VerticalPanel radioButtonPanel;
	@UiField
	Label locationTrackingDescriptionLabel;
	private static LocationTrackingOptionUiBinder uiBinder = GWT
			.create(LocationTrackingOptionUiBinder.class);

	interface LocationTrackingOptionUiBinder extends
			UiBinder<Widget, LocationTrackingOption> {
	}

	public LocationTrackingOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	@Override
	public String getTitle() {
		return Accounter.messages().locationTracking(Global.get().Location());
	}

	@Override
	public String getAnchor() {
		return Accounter.messages().locationTracking(Global.get().Location());
	}

	interface LocationTrackingPageUiBinder extends
			UiBinder<Widget, LocationTrackingOption> {
	}

	public void initData() {
		long locationType = getCompanyPreferences().getLocationTrackingId();
		locationTrackingCheckBoxLabel.setText(Accounter.messages()
				.locationTracking(Global.get().Location()));
		radioButtonsHeaderLabel.setText(Accounter.messages().useTerminologyFor(
				Global.get().Location()));
		switch ((int) locationType) {
		case ClientLocation.BUSINESS:
			buisinessRadioButton.setValue(true);
			break;
		case ClientLocation.DEPARTMENT:
			departmentRadioButton.setValue(true);
			break;
		case ClientLocation.DIVISION:
			divisionRadioButton.setValue(true);
			break;
		case ClientLocation.LOCATION:
			locationRadioButton.setValue(true);
			break;
		case ClientLocation.PROPERTY:
			propertyRadioButton.setValue(true);
			break;
		case ClientLocation.STORE:
			storeRadioButton.setValue(true);
			break;
		case ClientLocation.TERRITORY:
			territoryRadioButton.setValue(true);
			break;
		}
		boolean isLocationEnabled = getCompanyPreferences()
				.isLocationTrackingEnabled();
		if (isLocationEnabled) {
			locationTrackingCheckBoxItm.setValue(true);
			radioButtonsPanel
					.setVisible(locationTrackingCheckBoxItm.getValue());
		} else {
			radioButtonsPanel
					.setVisible(locationTrackingCheckBoxItm.getValue());
		}

	}

	@Override
	public void onSave() {
		getCompanyPreferences().setLocationTrackingEnabled(
				locationTrackingCheckBoxItm.getValue());
		if (buisinessRadioButton.getValue()) {
			getCompanyPreferences().setLocationTrackingId(
					ClientLocation.BUSINESS);
		} else if (departmentRadioButton.getValue()) {
			getCompanyPreferences().setLocationTrackingId(
					ClientLocation.DEPARTMENT);
		} else if (divisionRadioButton.getValue()) {
			getCompanyPreferences().setLocationTrackingId(
					ClientLocation.DIVISION);
		} else if (locationRadioButton.getValue()) {
			getCompanyPreferences().setLocationTrackingId(
					ClientLocation.LOCATION);
		} else if (propertyRadioButton.getValue()) {
			getCompanyPreferences().setLocationTrackingId(
					ClientLocation.PROPERTY);
		} else if (storeRadioButton.getValue()) {
			getCompanyPreferences().setLocationTrackingId(ClientLocation.STORE);
		} else if (territoryRadioButton.getValue()) {
			getCompanyPreferences().setLocationTrackingId(
					ClientLocation.TERRITORY);
		} else {
			getCompanyPreferences().setLocationTrackingId(
					ClientLocation.LOCATION);
		}
	}

	@Override
	public void createControls() {
		locationTrackingDescriptionLabel.setText(constants
				.locationTrackingDescription());
		locationTrackingDescriptionLabel.setStyleName("organisation_comment");
		locationRadioButton.setName(Accounter.constants().locationGroup());
		locationRadioButton.setHTML(Accounter.constants().location());
		buisinessRadioButton.setName(Accounter.constants().locationGroup());
		buisinessRadioButton.setHTML(Accounter.constants().buisiness());
		departmentRadioButton.setName(Accounter.constants().locationGroup());
		departmentRadioButton.setHTML(Accounter.constants().department());
		divisionRadioButton.setName(Accounter.constants().locationGroup());
		divisionRadioButton.setHTML(Accounter.constants().division());
		propertyRadioButton.setName(Accounter.constants().locationGroup());
		propertyRadioButton.setHTML(Accounter.constants().property());
		storeRadioButton.setName(Accounter.constants().locationGroup());
		storeRadioButton.setHTML(Accounter.constants().store());
		territoryRadioButton.setName(Accounter.constants().locationGroup());
		territoryRadioButton.setHTML(Accounter.constants().territory());
		radioButtonPanel.getElement().getStyle()
				.setPaddingLeft(
						(messages.useTerminologyFor(Global.get().Location())
								.length() * 6), Unit.PX);
		hpanel.setCellWidth(locationTrackingCheckBoxItm, "20px");

		locationTrackingCheckBoxItm.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				radioButtonsPanel.setVisible(locationTrackingCheckBoxItm
						.getValue());
			}
		});
	}

}
