package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class SetupTDSSelectionPage extends AbstractSetupPage {

	VerticalPanel mainPanel;
	Label headLabel;
	RadioButton yes;
	RadioButton no;
	TextItem TANNumber;
	DynamicForm dynamicForm;
	public static final String TDS = "tds";
	
	public SetupTDSSelectionPage()
	{
		super();
	}

	@Override
	public String getHeader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VerticalPanel getPageBody() {
		mainPanel = new VerticalPanel();
		headLabel = new Label();
		headLabel.setText("Do you want TDS");
		TANNumber = new TextItem("TAN Number");
		yes = new RadioButton(TDS, this.accounterConstants.yes());
		no = new RadioButton(TDS, this.accounterConstants.no());
		dynamicForm = new DynamicForm();
		dynamicForm.setFields(TANNumber);
		mainPanel.add(headLabel);
		mainPanel.add(dynamicForm);
		mainPanel.add(yes);
		mainPanel.add(no);
		return mainPanel;
	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSave() {

		if (yes.getValue())
			this.preferences.setTDSEnabled(true);
		else
			this.preferences.setTDSEnabled(false);

	}

	@Override
	public boolean doShow() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return false;
	}

}
