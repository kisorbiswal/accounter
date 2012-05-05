package com.vimukti.accounter.web.client.ui.company.options;

import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;

public class JobTrackingOption extends AbstractPreferenceOption {

	CheckboxItem jobTrackingCheckBox;

	LabelItem jobTrackingdescriptionLabel;

	StyledPanel mainPanel;

	public JobTrackingOption() {
		super("");
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

		jobTrackingCheckBox = new CheckboxItem(messages.jobTracking(),
				"jobTrackingCheckBox");
		mainPanel = new StyledPanel("JobTrackingOption");
		mainPanel.add(jobTrackingCheckBox);
		add(mainPanel);
	}

	@Override
	public void initData() {
		jobTrackingCheckBox.setValue(getCompanyPreferences()
				.isJobTrackingEnabled());
	}

}
