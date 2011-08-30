package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ui.Accounter;

public class SetupTrackEmployeesPage extends AbstractSetupPage {
	VerticalPanel mainPanel, vPanel_1, vPanel_2, checkBoxPanel;
	Label question1, question2;
	HTML bottemDescription;
	RadioButton yesRadioButton1, noRadioButton1, yesRadioButton2,
			noRadioButton2;
	CheckBox employesCheckBox, contractorscCheckBox;
	Image payrollImage;
	HorizontalPanel hpanel;
	FlexTable mainTable;

	public SetupTrackEmployeesPage() {

		// TODO Auto-generated constructor stub
	}

	@Override
	public String getHeader() {
		return this.accounterConstants.doyouHaveEmployees();
	}

	@Override
	public VerticalPanel getPageBody() {
		creatControls();
		return mainPanel;
	}

	private void creatControls() {
		mainPanel = new VerticalPanel();
		vPanel_1 = new VerticalPanel();
		vPanel_2 = new VerticalPanel();
		checkBoxPanel = new VerticalPanel();
		mainTable = new FlexTable();

		question1 = new Label(this.accounterConstants.doyouHaveEmployees());
		yesRadioButton1 = new RadioButton("topRadioGroup",
				this.accounterConstants.yes());
		vPanel_1.add(yesRadioButton1);
		employesCheckBox = new CheckBox(this.accounterConstants
				.wehavW2Employes());
		checkBoxPanel.add(employesCheckBox);
		contractorscCheckBox = new CheckBox(this.accounterConstants
				.wehavContractors());
		checkBoxPanel.add(contractorscCheckBox);
		noRadioButton1 = new RadioButton("topRadioGroup",
				this.accounterConstants.no());
		vPanel_1.add(checkBoxPanel);
		vPanel_1.add(noRadioButton1);

		// here for second question

		question2 = new Label(this.accounterConstants.trackEmployeeExpenses());
		question2.addStyleName("setup_header_label");
		yesRadioButton2 = new RadioButton("bottomRadioGroup",
				this.accounterConstants.yes());
		vPanel_2.add(yesRadioButton2);
		noRadioButton2 = new RadioButton("bottomRadioGroup",
				this.accounterConstants.no());
		vPanel_2.add(noRadioButton2);
		hpanel = new HorizontalPanel();
		bottemDescription = new HTML(this.accounterConstants
				.accounterPayrollDescription());
		hpanel.add(bottemDescription);
		payrollImage = new Image(Accounter.getFinanceImages().balnkImage());
		// TODo we have to give the Image
		// resource here .I given a example image
		hpanel.add(payrollImage);
		vPanel_2.add(hpanel);

		vPanel_1.addStyleName("setuppage_employe_body");

		mainTable.setWidget(0, 0, vPanel_1);

		if (noRadioButton1.getValue()) {
			mainTable.remove(question2);
			mainTable.remove(vPanel_2);
		}
		yesRadioButton1.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				if (!question2.getClass().equals("setup_header_label")) {
					question2.addStyleName("setup_header_label");
				}
				mainTable.setWidget(1, 0, question2);
				mainTable.setWidget(2, 0, vPanel_2);
			}
		});
		noRadioButton1.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				mainTable.remove(question2);
				mainTable.remove(vPanel_2);
			}
		});

		checkBoxPanel.addStyleName("employee_checkPanel");
		checkBoxPanel.setSpacing(10);
		vPanel_2.setSpacing(10);
		mainPanel.add(mainTable);
	}

	@Override
	public void onLoad() {
		if (preferences.isHaveEpmloyees()) {
			yesRadioButton1.setValue(true);
		} else {
			noRadioButton1.setValue(true);
		}

		if (preferences.isTrackEmployeeExpenses()) {
			yesRadioButton2.setValue(true);
		} else {
			noRadioButton2.setValue(true);
		}
		if (preferences.isHaveW_2Employees()) {
			employesCheckBox.setValue(true);
		} else {
			employesCheckBox.setValue(false);
		}

		if (preferences.isHave1099contractors()) {
			contractorscCheckBox.setValue(true);
		} else {
			contractorscCheckBox.setValue(false);
		}

	}

	@Override
	public void onSave() {
		if (yesRadioButton1.getValue()) {
			if ((employesCheckBox.getValue() || contractorscCheckBox.getValue())) {
				preferences.setHaveEpmloyees(yesRadioButton1.getValue());
				preferences
						.setTrackEmployeeExpenses(yesRadioButton2.getValue());
				preferences.setHaveW_2Employees(employesCheckBox.getValue());
				preferences.setHave1099contractors(contractorscCheckBox
						.getValue());
			} else {
				Accounter.showError("Please select emlployee type..");
			}

		} else {
			preferences.setHaveEpmloyees(yesRadioButton1.getValue());
			preferences.setTrackEmployeeExpenses(yesRadioButton2.getValue());
			preferences.setHaveW_2Employees(employesCheckBox.getValue());
			preferences.setHave1099contractors(contractorscCheckBox.getValue());
		}

	}

	@Override
	public boolean doShow() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return false;
	}

}
