package com.vimukti.accounter.web.client.uibinder.companypreferences;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientLocation;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class LocationTrackingPage extends AbstractCompanyInfoPanel {

	private static LocationTrackingPageUiBinder uiBinder = GWT
			.create(LocationTrackingPageUiBinder.class);
	// @UiField
	// Label locationTrackingRadioLabel;
	// @UiField
	// CheckBox locationTrackingRadioItm;
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

	interface LocationTrackingPageUiBinder extends
			UiBinder<Widget, LocationTrackingPage> {
	}

	public LocationTrackingPage() {
		initWidget(uiBinder.createAndBindUi(this));
		createcontrols();
	}

	private void createcontrols() {
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
		hpanel.setCellWidth(locationTrackingCheckBoxItm, "20px");

		locationTrackingCheckBoxItm.addClickListener(new ClickListener() {
			@Override
			public void onClick(Widget sender) {
				radioButtonsPanel.setVisible(locationTrackingCheckBoxItm
						.getValue());
			}
		});
	}

	@Override
	public void onLoad() {
		long locationType = companyPreferences.getLocationTrackingId();
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
		boolean isLocationEnabled = companyPreferences
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
		companyPreferences
				.setLocationTrackingEnabled(locationTrackingCheckBoxItm
						.getValue());
		if (buisinessRadioButton.getValue()) {
			companyPreferences.setLocationTrackingId(ClientLocation.BUSINESS);
		} else if (departmentRadioButton.getValue()) {
			companyPreferences.setLocationTrackingId(ClientLocation.DEPARTMENT);
		} else if (divisionRadioButton.getValue()) {
			companyPreferences.setLocationTrackingId(ClientLocation.DIVISION);
		} else if (locationRadioButton.getValue()) {
			companyPreferences.setLocationTrackingId(ClientLocation.LOCATION);
		} else if (propertyRadioButton.getValue()) {
			companyPreferences.setLocationTrackingId(ClientLocation.PROPERTY);
		} else if (storeRadioButton.getValue()) {
			companyPreferences.setLocationTrackingId(ClientLocation.STORE);
		} else if (territoryRadioButton.getValue()) {
			companyPreferences.setLocationTrackingId(ClientLocation.TERRITORY);
		} else {
			companyPreferences.setLocationTrackingId(ClientLocation.LOCATION);
		}
	}

}
