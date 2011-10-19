package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

public class TrackEstimatesOption extends AbstractPreferenceOption {

	private static TrackEstimatesOptionUiBinder uiBinder = GWT
			.create(TrackEstimatesOptionUiBinder.class);
	@UiField
	Label trackEstimateHeader;
	@UiField
	Label trackEstimateDescLabel;
	@UiField
	RadioButton yesRadioButton;
	@UiField
	RadioButton noRadioButton;

	interface TrackEstimatesOptionUiBinder extends
			UiBinder<Widget, TrackEstimatesOption> {
	}

	public TrackEstimatesOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	public TrackEstimatesOption(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void createControls() {
		trackEstimateHeader.setText(constants.trackingEstimates());
		yesRadioButton.setText(constants.yes());
		noRadioButton.setText(constants.no());
	}

	@Override
	public String getAnchor() {
		return constants.trackingEstimates();
	}

	@Override
	public String getTitle() {
		return constants.trackingEstimates();
	}

	@Override
	public void initData() {
		if (getCompanyPreferences().isDoyouwantEstimates()) {
			yesRadioButton.setValue(true);
		} else {
			noRadioButton.setValue(true);
		}
	}

	@Override
	public void onSave() {
		getCompanyPreferences()
				.setDoyouwantEstimates(yesRadioButton.getValue());
	}

}
