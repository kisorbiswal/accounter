package com.vimukti.accounter.web.client.ui.company.options;

import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;

public class ManageBillsOption extends AbstractPreferenceOption {

	LabelItem managingBillLabelItem;

	RadioGroupItem managingBillRadioGroup;

	LabelItem managingBilldescritionLabel;

	CheckboxItem isPriceLevelsEnabled;

	CheckboxItem purchaseOrderCheckBox;

	StyledPanel mainPanel;

	public ManageBillsOption() {
		super("");
		createControls();
		initData();
	}

	public void initData() {
		if (getCompanyPreferences().isKeepTrackofBills()) {
			managingBillRadioGroup.setValue(messages.yes());
		} else {
			managingBillRadioGroup.setValue(messages.yes());
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
		managingBillLabelItem = new LabelItem(messages.managingBills(),
				"managingBillLabelItem");
		managingBilldescritionLabel = new LabelItem(
				messages.managingBillDescription(),
				"managingBilldescritionLabel");
		managingBilldescritionLabel.setStyleName("organisation_comment");

		managingBillRadioGroup = new RadioGroupItem();
		managingBillRadioGroup.setGroupName("managingBillRadioGroup");
		managingBillRadioGroup.setShowTitle(false);

		managingBillRadioGroup.setValueMap(messages.yes(), messages.no());
		managingBillRadioGroup.setDefaultValue(messages.ageingforduedate());

		isPriceLevelsEnabled = new CheckboxItem(messages.enabled() + " "
				+ messages.priceLevel(), "isPriceLevelsEnabled");
		isPriceLevelsEnabled.setStyleName("header");
		purchaseOrderCheckBox = new CheckboxItem(
				messages.enablePreference(messages.purchaseOrder()),
				"purchaseOrderCheckBox");
		purchaseOrderCheckBox.setVisible(Accounter
				.hasPermission(Features.PURCHASE_ORDER));
		purchaseOrderCheckBox.setStyleName("bold");

		mainPanel = new StyledPanel("manageBillsOption");
		mainPanel.add(managingBillLabelItem);
		mainPanel.add(managingBilldescritionLabel);
		mainPanel.add(managingBillRadioGroup);
		mainPanel.add(isPriceLevelsEnabled);
		mainPanel.add(purchaseOrderCheckBox);
		add(mainPanel);

	}

	@Override
	public String getTitle() {
		return messages.managingBillsTitle();
	}

	@Override
	public void onSave() {
		if (managingBillRadioGroup.getValue().equals(messages.yes())) {
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
