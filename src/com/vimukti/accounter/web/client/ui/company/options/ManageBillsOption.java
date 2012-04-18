package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.ui.Accounter;

public class ManageBillsOption extends AbstractPreferenceOption {

	private static ManageBillsOptionUiBinder uiBinder = GWT
			.create(ManageBillsOptionUiBinder.class);
	@UiField
	Label managingBillLabelItem;
	@UiField
	RadioButton managingBillYesRadioButton;
	@UiField
	RadioButton managingBillNoRadioButton;
	@UiField
	Label managingBilldescritionLabel;
	@UiField
	CheckBox isPriceLevelsEnabled;
	@UiField
	CheckBox purchaseOrderCheckBox;

	interface ManageBillsOptionUiBinder extends
			UiBinder<Widget, ManageBillsOption> {
	}

	public ManageBillsOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	public void initData() {
		if (getCompanyPreferences().isKeepTrackofBills()) {
			managingBillYesRadioButton.setValue(true);
			managingBillNoRadioButton.setValue(false);
		} else {
			managingBillYesRadioButton.setValue(false);
			managingBillNoRadioButton.setValue(true);
		}
		if (getCompanyPreferences().isPricingLevelsEnabled()) {
			isPriceLevelsEnabled.setValue(true);
		} else {
			isPriceLevelsEnabled.setValue(false);
		}
		purchaseOrderCheckBox.setValue(getCompanyPreferences()
				.isPurchaseOrderEnabled());
	}

	public void createControls() {
		managingBillLabelItem.setText(messages.managingBills());
		managingBilldescritionLabel.setText(messages.managingBillDescription());
		managingBilldescritionLabel.setStyleName("organisation_comment");

		managingBillYesRadioButton.setText(messages.yes());
		managingBillNoRadioButton.setText(messages.no());
		isPriceLevelsEnabled.setText(messages.enabled() + " "
				+ messages.priceLevel());
		isPriceLevelsEnabled.setStyleName("header");
		purchaseOrderCheckBox.setValue(Accounter
				.hasPermission(Features.PURCHASE_ORDER));
		purchaseOrderCheckBox.setText(messages.enablePreference(messages
				.purchaseOrder()));
		purchaseOrderCheckBox.setStyleName("bold");

	}

	public ManageBillsOption(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public String getTitle() {
		return messages.managingBillsTitle();
	}

	@Override
	public void onSave() {
		if (managingBillYesRadioButton.getValue()) {
			getCompanyPreferences().setKeepTrackofBills(true);
		} else {
			getCompanyPreferences().setKeepTrackofBills(false);
		}
		if (isPriceLevelsEnabled.getValue()) {
			getCompanyPreferences().setPricingLevelsEnabled(true);
		} else {
			getCompanyPreferences().setPricingLevelsEnabled(false);
		}
		getCompanyPreferences().setPurchaseOrderEnabled(
				purchaseOrderCheckBox.getValue());
	}

	@Override
	public String getAnchor() {
		return messages.managingBillsTitle();
	}

}
