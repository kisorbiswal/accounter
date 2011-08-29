package com.vimukti.accounter.web.client.ui.companypreferences.panels;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;

public class CustomerAndvendorSettingsPanel extends AbstractCompanyInfoPanel {

	public CustomerAndvendorSettingsPanel(
			ClientCompanyPreferences companyPreferences) {
		createControls();
	}

	private void createControls() {
		VerticalPanel mainPanel = new VerticalPanel();

		AccounterConstants constants = Accounter.constants();

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

		RadioGroupItem chargeTaxGroupItem = new RadioGroupItem();
		RadioGroupItem trackTimeGroupItem = new RadioGroupItem();
		RadioGroupItem managingBillsGroupItem = new RadioGroupItem();
		RadioGroupItem createEstimatesGroupItem = new RadioGroupItem();
		RadioGroupItem usingStatementsGroupItem = new RadioGroupItem();

		chargeTaxGroupItem.setValue(constants.yes(), constants.no());

		trackTimeGroupItem.setValue(constants.yes(), constants.no());

		managingBillsGroupItem.setValue(constants.yes(), constants.no());

		createEstimatesGroupItem.setValue(constants.yes(), constants.no());

		usingStatementsGroupItem.setValue(constants.yes(), constants.no());

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
		// TODO Auto-generated method stub

	}

	@Override
	public void onSave() {
		// TODO Auto-generated method stub

	}
}
