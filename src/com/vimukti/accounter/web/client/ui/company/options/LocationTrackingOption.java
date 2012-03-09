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
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientLocation;
import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;

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
	FlowPanel radioButtonsPanel;
	@UiField
	FlowPanel hpanel;
	@UiField
	FlowPanel radioButtonPanel;
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
		return messages.locationTracking(Global.get().Location());
	}

	@Override
	public String getAnchor() {
		return messages.locationTracking(Global.get().Location());
	}

	interface LocationTrackingPageUiBinder extends
			UiBinder<Widget, LocationTrackingOption> {
	}

	public void initData() {
		long locationType = getCompanyPreferences().getLocationTrackingId();
		locationTrackingCheckBoxLabel.setText(messages.locationTracking(Global
				.get().Location()));
		radioButtonsHeaderLabel.setText(messages.useTerminologyFor(Global.get()
				.Location()));
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
		locationTrackingDescriptionLabel.setText(messages
				.locationTrackingDescription());
		locationTrackingDescriptionLabel.setStyleName("organisation_comment");
		locationRadioButton.setName(messages.locationGroup());
		locationRadioButton.setHTML(messages.location());
		buisinessRadioButton.setName(messages.locationGroup());
		buisinessRadioButton.setHTML(messages.buisiness());
		departmentRadioButton.setName(messages.locationGroup());
		departmentRadioButton.setHTML(messages.department());
		divisionRadioButton.setName(messages.locationGroup());
		divisionRadioButton.setHTML(messages.division());
		propertyRadioButton.setName(messages.locationGroup());
		propertyRadioButton.setHTML(messages.property());
		storeRadioButton.setName(messages.locationGroup());
		storeRadioButton.setHTML(messages.store());
		territoryRadioButton.setName(messages.locationGroup());
		territoryRadioButton.setHTML(messages.territory());
		// radioButtonPanel
		// .getElement()
		// .getStyle()
		// .setPaddingLeft(
		// (messages.useTerminologyFor(Global.get().Location())
		// .length() * 6), Unit.PX);
		// hpanel.setCellWidth(locationTrackingCheckBoxItm, "20px");

		locationTrackingCheckBoxItm.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (Accounter.hasPermission(Features.LOCATION)) {
					radioButtonsPanel.setVisible(locationTrackingCheckBoxItm
							.getValue());
				} else {
					locationTrackingCheckBoxItm.setValue(false);
					Accounter.showSubscriptionWarning();
				}
			}
		});
	}

}
