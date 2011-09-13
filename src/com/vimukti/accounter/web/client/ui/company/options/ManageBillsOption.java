package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

public class ManageBillsOption extends AbstractPreferenceOption {

	private static ManageBillsOptionUiBinder uiBinder = GWT
			.create(ManageBillsOptionUiBinder.class);
	@UiField
	Label managingBillLabelItem;
	@UiField
	RadioButton managingBillYesRadioButton;
	@UiField
	RadioButton managingBillNoRadioButton;

	interface ManageBillsOptionUiBinder extends
			UiBinder<Widget, ManageBillsOption> {
	}

	public ManageBillsOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	private void initData() {
		if (companyPreferences.isKeepTrackofBills()) {
			managingBillYesRadioButton.setValue(true);
			managingBillNoRadioButton.setValue(false);
		} else {
			managingBillYesRadioButton.setValue(false);
			managingBillNoRadioButton.setValue(true);
		}

	}

	private void createControls() {
		managingBillLabelItem.setText(constants.managingBills());

		managingBillYesRadioButton.setText(constants.yes());
		managingBillNoRadioButton.setText(constants.no());

	}

	public ManageBillsOption(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public String getTitle() {
		return constants.managingBillsTitle();
	}

	@Override
	public void onSave() {
		if (managingBillYesRadioButton.getValue()) {
			companyPreferences.setKeepTrackofBills(true);
		} else {
			companyPreferences.setKeepTrackofBills(false);
		}

	}

	@Override
	public String getAnchor() {
		return constants.managingBillsTitle();
	}

}
