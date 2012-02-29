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
import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.ui.Accounter;

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
	@UiField
	CheckBox useDelayedCharges;
	@UiField
	VerticalPanel hiddenPanel;

	@UiField
	RadioButton dontInclude;
	@UiField
	RadioButton includeAccepted;
	@UiField
	RadioButton includePendingAndAccepted;

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
		trackEstimateHeader.setText(messages.trackingEstimates());
		yesRadioButton.setText(messages.yes());
		noRadioButton.setText(messages.no());
		useDelayedCharges.setText(messages.delayedCharges());
		useDelayedCharges.setStyleName("bold");
		useDelayedCharges.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (Accounter.hasPermission(
						Features.CREDITS_CHARGES)) {
				} else {
					useDelayedCharges.setValue(false);
					Accounter.showSubscriptionWarning();
				}

			}
		});
		hiddenPanel.setVisible(getCompanyPreferences().isDoyouwantEstimates());

		noRadioButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hiddenPanel.setVisible(false);
			}
		});

		yesRadioButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hiddenPanel.setVisible(true);
				includePendingAndAccepted.setValue(true);
			}
		});

		dontInclude.setText(messages.dontWantToIncludeEstimates());
		includeAccepted.setText(messages.includeAcceptedEstimates());
		includePendingAndAccepted.setText(messages
				.includePendingAndAcceptedEstimates());
	}

	@Override
	public String getAnchor() {
		return messages.trackingEstimates();
	}

	@Override
	public String getTitle() {
		return messages.trackingEstimates();
	}

	@Override
	public void initData() {
		if (getCompanyPreferences().isDoyouwantEstimates()) {
			yesRadioButton.setValue(true);
		} else {
			noRadioButton.setValue(true);
		}

		if (getCompanyPreferences().isDoyouwantEstimates()) {
			if (getCompanyPreferences().isDontIncludeEstimates()) {
				dontInclude.setValue(true);
				includeAccepted.setValue(false);
				includePendingAndAccepted.setValue(false);
			} else if (getCompanyPreferences().isIncludeAcceptedEstimates()) {
				includeAccepted.setValue(true);
				dontInclude.setValue(false);
				includePendingAndAccepted.setValue(false);
			} else if (getCompanyPreferences()
					.isIncludePendingAcceptedEstimates()) {
				includePendingAndAccepted.setValue(true);
				dontInclude.setValue(false);
				includeAccepted.setValue(false);
			}
		}

		useDelayedCharges.setValue(getCompanyPreferences()
				.isDelayedchargesEnabled());
	}

	@Override
	public void onSave() {
		getCompanyPreferences()
				.setDoyouwantEstimates(yesRadioButton.getValue());
		getCompanyPreferences().setDelayedchargesEnabled(
				useDelayedCharges.getValue());
		if (noRadioButton.getValue()) {
			getCompanyPreferences().setDontIncludeEstimates(false);
			getCompanyPreferences().setIncludeAcceptedEstimates(false);
			getCompanyPreferences().setIncludePendingAcceptedEstimates(false);
		}
		if (getCompanyPreferences().isDoyouwantEstimates()) {
			if (dontInclude.getValue()) {
				getCompanyPreferences().setDontIncludeEstimates(true);
				getCompanyPreferences().setIncludeAcceptedEstimates(false);
				getCompanyPreferences().setIncludePendingAcceptedEstimates(
						false);
			} else if (includeAccepted.getValue()) {
				getCompanyPreferences().setIncludeAcceptedEstimates(true);
				getCompanyPreferences().setDontIncludeEstimates(false);
				getCompanyPreferences().setIncludePendingAcceptedEstimates(
						false);
			} else if (includePendingAndAccepted.getValue()) {
				getCompanyPreferences()
						.setIncludePendingAcceptedEstimates(true);
				getCompanyPreferences().setDontIncludeEstimates(false);
				getCompanyPreferences().setIncludeAcceptedEstimates(false);
			}
		}
	}

}
