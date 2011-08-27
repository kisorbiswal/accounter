package com.vimukti.accounter.web.client.ui.companypreferences.panels;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;

public class AgeingAndSellingDetailsPanel extends VerticalPanel {
	private RadioGroupItem ageingRadioGroup, sellingRadioGroup;

	public AgeingAndSellingDetailsPanel(
			ClientCompanyPreferences companyPreferences) {
		createControls();
	}

	private void createControls() {
		ageingRadioGroup = new RadioGroupItem(Accounter.constants()
				.ageingDetails());
		ageingRadioGroup.setWidth(100);
		ageingRadioGroup.setValues(getClickHandler(), Accounter.constants()
				.ageingforduedate(), Accounter.constants()
				.ageingfortransactiondate());

		sellingRadioGroup = new RadioGroupItem(Accounter.constants()
				.sellingDetails());
		sellingRadioGroup.setValues(getClickHandler(), Accounter.constants()
				.services(), Accounter.constants().products(), Accounter
				.constants().both());

		DynamicForm radioGroupForm = new DynamicForm();
		radioGroupForm.setWidth("80%");
		radioGroupForm.getCellFormatter().setWidth(0, 0, "225px");
		radioGroupForm.getCellFormatter().addStyleName(1, 0, "memoFormAlign");
		radioGroupForm.getCellFormatter().addStyleName(2, 0, "memoFormAlign");
		radioGroupForm.setFields(ageingRadioGroup, sellingRadioGroup);

		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.add(radioGroupForm);

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
}
