package com.vimukti.accounter.web.client.ui.companypreferences.panels;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;

public class CustomerAndvendorSettingsPanel extends AbstractCompanyInfoPanel {
	private RadioGroupItem chargeTaxGroupItem, trackTimeGroupItem,
			managingBillsGroupItem, createEstimatesGroupItem,
			usingStatementsGroupItem;

	public CustomerAndvendorSettingsPanel() {
		super();
		createControls();
	}

	private void createControls() {
		VerticalPanel mainPanel = new VerticalPanel();

		DynamicForm chargeTaxForm = new DynamicForm();
		DynamicForm trackTimeForm = new DynamicForm();
		DynamicForm managingBillForm = new DynamicForm();
		DynamicForm createEstimatesForm = new DynamicForm();
		DynamicForm usingStatementsForm = new DynamicForm();

		LabelItem chargeTaxLabelItem = new LabelItem();
		LabelItem trackTimeLabelItem = new LabelItem();
		LabelItem managingBillLabelItem = new LabelItem();
		LabelItem createEstimatesLabelItem = new LabelItem();
		LabelItem usingStatementsLabelItem = new LabelItem();

		chargeTaxLabelItem.setValue(constants.doyouchargesalestax());
		trackTimeLabelItem.setValue(constants.doyouwantTrackTime());
		managingBillLabelItem.setValue(constants.managingBills());
		createEstimatesLabelItem.setValue(constants
				.wanttoCreateEstimatesInAccounter());
		usingStatementsLabelItem.setValue(constants.doyouWantToUseStatements());

		chargeTaxGroupItem = new RadioGroupItem();
		trackTimeGroupItem = new RadioGroupItem();
		managingBillsGroupItem = new RadioGroupItem();
		createEstimatesGroupItem = new RadioGroupItem();
		usingStatementsGroupItem = new RadioGroupItem();

		chargeTaxGroupItem.setGroupName(constants.doyouchargesalestax());
		chargeTaxGroupItem.setValue(constants.yes(), constants.no());
		chargeTaxGroupItem.setVertical(false);

		trackTimeGroupItem.setGroupName(constants.doyouwantTrackTime());
		trackTimeGroupItem.setValue(constants.yes(), constants.no());
		trackTimeGroupItem.setVertical(false);

		managingBillsGroupItem.setGroupName(constants.managingBills());
		managingBillsGroupItem.setValue(constants.yes(), constants.no());
		managingBillsGroupItem.setVertical(false);

		createEstimatesGroupItem.setGroupName(constants
				.wanttoCreateEstimatesInAccounter());
		createEstimatesGroupItem.setValue(constants.yes(), constants.no());
		createEstimatesGroupItem.setVertical(false);

		usingStatementsGroupItem.setGroupName(constants
				.doyouWantToUseStatements());
		usingStatementsGroupItem.setValue(constants.yes(), constants.no());
		usingStatementsGroupItem.setVertical(false);

		chargeTaxForm.setFields(chargeTaxLabelItem, chargeTaxGroupItem);
		trackTimeForm.setFields(trackTimeLabelItem, trackTimeGroupItem);
		managingBillForm.setFields(managingBillLabelItem,
				managingBillsGroupItem);
		createEstimatesForm.setFields(createEstimatesLabelItem,
				createEstimatesGroupItem);
		usingStatementsForm.setFields(usingStatementsLabelItem,
				usingStatementsGroupItem);

		mainPanel.add(chargeTaxForm);
		mainPanel.add(trackTimeForm);
		mainPanel.add(managingBillForm);
		mainPanel.add(createEstimatesForm);
		mainPanel.add(usingStatementsForm);

		add(mainPanel);
	}

	@Override
	public void onLoad() {
		if (companyPreferences.isDoYouChargesalesTax())
			chargeTaxGroupItem.setValue(constants.yes());
		else
			chargeTaxGroupItem.setValue(constants.no());

		if (companyPreferences.isDoYouKeepTrackOfTime())
			trackTimeGroupItem.setValue(constants.yes());
		else
			trackTimeGroupItem.setValue(constants.no());

		if (companyPreferences.isDoyouKeepTrackofBills())
			managingBillsGroupItem.setValue(constants.yes());
		else
			managingBillsGroupItem.setValue(constants.no());
		if (companyPreferences.isDoyouwantEstimates())
			createEstimatesGroupItem.setValue(constants.yes());
		else
			createEstimatesGroupItem.setValue(constants.no());
		if (companyPreferences.isDoyouwantstatements())
			usingStatementsGroupItem.setValue(constants.yes());
		else
			usingStatementsGroupItem.setValue(constants.no());

	}

	@Override
	public void onSave() {
		if (chargeTaxGroupItem.getValue().equals(constants.yes()))
			companyPreferences.setDoYouPaySalesTax(true);
		else
			companyPreferences.setDoYouChargesalesTax(false);

		if (trackTimeGroupItem.getValue().equals(constants.yes()))
			companyPreferences.setDoYouKeepTrackOfTime(true);
		else
			companyPreferences.setDoYouKeepTrackOfTime(false);

		if (managingBillsGroupItem.getValue().equals(constants.yes()))
			companyPreferences.setDoyouKeepTrackofBills(true);
		else
			companyPreferences.setDoyouKeepTrackofBills(false);

		if (createEstimatesGroupItem.getValue().equals(constants.yes()))
			companyPreferences.setDoyouwantEstimates(true);
		else
			companyPreferences.setDoyouwantEstimates(false);

		if (usingStatementsGroupItem.getValue().equals(constants.yes()))
			companyPreferences.setDoyouwantstatements(true);
		else
			companyPreferences.setDoyouwantstatements(false);

	}
}
