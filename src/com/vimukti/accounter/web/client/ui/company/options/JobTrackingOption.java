package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class JobTrackingOption extends AbstractPreferenceOption {

	@UiField
	CheckBox jobTrackingCheckBox;
	@UiField
	Label jobTrackingCheckBoxLabel;
	@UiField
	Label jobTrackingdescriptionLabel;

	private static JobTrackingOptionUiBinder uiBinder = GWT
			.create(JobTrackingOptionUiBinder.class);

	interface JobTrackingOptionUiBinder extends
			UiBinder<Widget, JobTrackingOption> {
	}

	public JobTrackingOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	@Override
	public String getTitle() {
		return messages.jobTracking();
	}

	@Override
	public void onSave() {
		getCompanyPreferences().setJobTrackingEnabled(
				jobTrackingCheckBox.getValue());
	}

	@Override
	public String getAnchor() {
		return messages.jobTracking();
	}

	@Override
	public void createControls() {
		jobTrackingCheckBox.setText(messages.jobTracking());

		// jobTrackingCheckBox.addClickHandler(new ClickHandler() {
		// @Override
		// public void onClick(ClickEvent event) {
		// if (Accounter.hasPermission(Features.JOB_COSTING)) {
		//
		// } else {
		// jobTrackingCheckBox.setValue(false);
		// Accounter.showSubscriptionWarning();
		// }
		//
		// }
		// });
	}

	@Override
	public void initData() {
		jobTrackingCheckBox.setValue(getCompanyPreferences()
				.isJobTrackingEnabled());
	}

}
