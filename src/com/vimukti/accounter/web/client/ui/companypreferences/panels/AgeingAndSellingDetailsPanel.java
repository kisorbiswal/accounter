package com.vimukti.accounter.web.client.ui.companypreferences.panels;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;

public class AgeingAndSellingDetailsPanel extends AbstractCompanyInfoPanel {
	private RadioGroupItem ageingRadioGroup, sellingRadioGroup;

	public AgeingAndSellingDetailsPanel() {
		super();
		createControls();
	}

	private void createControls() {

		ageingRadioGroup = new RadioGroupItem();
		ageingRadioGroup.setGroupName(constants.ageing());
		ageingRadioGroup.setValues(getClickHandler(), constants
				.ageingforduedate(), constants.ageingfortransactiondate());

		VerticalPanel ageingPanel = new VerticalPanel();
		VerticalPanel sellingPanel = new VerticalPanel();

		Label ageingLabel = new Label(Accounter.constants().ageingDetails());
		Label sellingsLabel = new Label(Accounter.constants().sellingDetails());

		sellingRadioGroup = new RadioGroupItem();
		sellingRadioGroup.setGroupName(constants.sell());
		sellingRadioGroup.setValues(getClickHandler(), Accounter.constants()
				.services(), Accounter.constants().products(), Accounter
				.constants().bothServiceProducts());

		DynamicForm ageingGroupForm = new DynamicForm();
		ageingGroupForm.setFields(ageingRadioGroup);

		DynamicForm sellingGroupForm = new DynamicForm();
		sellingGroupForm.setFields(sellingRadioGroup);

		ageingPanel.add(ageingLabel);
		ageingPanel.add(ageingGroupForm);
		ageingLabel.addStyleName("header");
		ageingPanel.setStyleName("companyInfoPanel");

		sellingPanel.add(sellingsLabel);
		sellingPanel.add(sellingGroupForm);
		sellingsLabel.addStyleName("header");
		sellingPanel.setStyleName("companyInfoPanel");

		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.add(ageingPanel);
		mainPanel.add(sellingPanel);

		mainPanel.setSize("100%", "100%");
		mainPanel.setSpacing(8);
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
			sellingRadioGroup.setValue(constants.bothServiceProducts());
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

		if (sellingRadioGroup.getValue().equals(constants.bothServiceProducts())) {
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
