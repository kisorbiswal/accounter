package com.vimukti.accounter.web.client.ui.companypreferences.panels;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;

public class AgeingAndSellingDetailsPanel extends AbstractCompanyInfoPanel {
	private RadioGroupItem ageingRadioGroup, sellingRadioGroup;

	public AgeingAndSellingDetailsPanel() {
		super();
		createControls();
	}

	private void createControls() {

		ageingRadioGroup = new RadioGroupItem();
		ageingRadioGroup.setWidth(100);
		ageingRadioGroup.setValues(getClickHandler(), constants
				.ageingforduedate(), constants.ageingfortransactiondate());
		LabelItem ageingLabel = new LabelItem();
		LabelItem sellingsLabel = new LabelItem();

		ageingLabel.setValue(Accounter.constants().ageingDetails());
		sellingsLabel.setValue(Accounter.constants().sellingDetails());

		sellingRadioGroup = new RadioGroupItem();
		sellingRadioGroup.setValues(getClickHandler(), Accounter.constants()
				.services(), Accounter.constants().products(), Accounter
				.constants().both());

		DynamicForm ageingGroupForm = new DynamicForm();
		ageingGroupForm.setWidth("80%");
		ageingGroupForm.getCellFormatter().setWidth(0, 0, "225px");
		ageingGroupForm.getCellFormatter().addStyleName(1, 0, "memoFormAlign");
		ageingGroupForm.getCellFormatter().addStyleName(2, 0, "memoFormAlign");
		ageingGroupForm.setFields(ageingLabel, ageingRadioGroup);

		DynamicForm sellingGroupForm = new DynamicForm();
		ageingGroupForm.setWidth("80%");
		ageingGroupForm.getCellFormatter().setWidth(0, 0, "225px");
		ageingGroupForm.getCellFormatter().addStyleName(1, 0, "memoFormAlign");
		ageingGroupForm.getCellFormatter().addStyleName(2, 0, "memoFormAlign");
		sellingGroupForm.setFields(sellingsLabel, sellingRadioGroup);

		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.add(ageingGroupForm);
		mainPanel.add(sellingGroupForm);
		this.add(mainPanel);

	}

	private ClickHandler getClickHandler() {
		ClickHandler handler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

			}
		};
		return handler;
	}

	@Override
	public void onLoad() {
		if (companyPreferences.getAgeingFromTransactionDateORDueDate() == 1)
			ageingRadioGroup.setValue(constants.ageingforduedate());
		else
			ageingRadioGroup.setValue(constants.ageingfortransactiondate());
		if (companyPreferences.isSellServices()
				&& companyPreferences.isSellProducts())
			sellingRadioGroup.setValue(constants.both());
		else if (companyPreferences.isSellServices())
			sellingRadioGroup.setValue(constants.services());
		else
			sellingRadioGroup.setValue(constants.products());
	}

	@Override
	public void onSave() {
		if (ageingRadioGroup.getValue().equals(constants.ageingforduedate()))
			companyPreferences.setAgeingFromTransactionDateORDueDate(1);
		else
			companyPreferences.setAgeingFromTransactionDateORDueDate(2);

		if (sellingRadioGroup.getValue().equals(constants.both())) {
			companyPreferences.setSellServices(true);
			companyPreferences.setSellProducts(true);
		} else if (sellingRadioGroup.getValue().equals(constants.services())) {
			companyPreferences.setSellServices(true);
			companyPreferences.setSellProducts(false);
		} else {
			companyPreferences.setSellProducts(true);
			companyPreferences.setSellServices(false);
		}
	}
}
