package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class InvoicePrintDialog extends BaseDialog {
	private String description;

	public InvoicePrintDialog(String title, String desc, String description) {
		super(title, desc);
		this.description = description;
		createControls();
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

	}

	private void createControls() {

		Label label = new Label(description);

		label.setWidth("70%");

		DynamicForm dynamicForm = new DynamicForm();

		VerticalPanel comboPanel = new VerticalPanel();
		HorizontalPanel buttonPanel = new HorizontalPanel();

		comboPanel.add(label);
		comboPanel.add(dynamicForm);
		comboPanel.add(buttonPanel);
		cancelBtn.setVisible(false);

		setBodyLayout(comboPanel);
	}

	@Override
	protected boolean onOK() {
		return true;
	}

}
