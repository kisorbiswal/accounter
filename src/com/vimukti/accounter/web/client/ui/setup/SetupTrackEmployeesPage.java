package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ui.Accounter;

public class SetupTrackEmployeesPage extends AbstractSetupPage {
	VerticalPanel vPanel;
	Label question1, question2;
	HTML bottemDescription;
	RadioButton yesRadioButton1, noRadioButton1, yesRadioButton2,
			noRadioButton2;
	CheckBox employesCheckBox, contractorscCheckBox;
	Image payrollImage;
	HorizontalPanel hpanel;

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
		return vPanel;
	}

	private void creatControls() {
		vPanel = new VerticalPanel();
		question1 = new Label(this.accounterConstants.doYouhavEmployes());
		yesRadioButton1 = new RadioButton(this.accounterConstants.yes());
		vPanel.add(yesRadioButton1);
		employesCheckBox = new CheckBox(
				this.accounterConstants.wehavW2Employes());
		vPanel.add(employesCheckBox);
		contractorscCheckBox = new CheckBox(
				this.accounterConstants.wehavContractors());
		vPanel.add(contractorscCheckBox);
		noRadioButton1 = new RadioButton(this.accounterConstants.no());
		vPanel.add(noRadioButton1);

		// here for second question

		question2 = new Label(this.accounterConstants.trackEmployeeExpenses());
		yesRadioButton2 = new RadioButton(this.accounterConstants.yes());
		vPanel.add(yesRadioButton2);
		noRadioButton2 = new RadioButton(this.accounterConstants.no());
		vPanel.add(noRadioButton2);
		hpanel = new HorizontalPanel();
		bottemDescription = new HTML(
				this.accounterConstants.accounterPayrollDescription());
		hpanel.add(bottemDescription);
		payrollImage = new Image(Accounter.getFinanceImages().balnkImage());
		// TODo we have to give the Image
		// resource here .I given a example image
		hpanel.add(payrollImage);
		vPanel.add(hpanel);

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
		preferences.setHaveEpmloyees(yesRadioButton1.getValue());
		preferences.setTrackEmployeeExpenses(yesRadioButton2.getValue());
		preferences.setHaveW_2Employees(employesCheckBox.getValue());
		preferences.setHave1099contractors(contractorscCheckBox.getValue());
	}

	@Override
	public boolean doShow() {
		// TODO Auto-generated method stub
		return false;
	}

}
