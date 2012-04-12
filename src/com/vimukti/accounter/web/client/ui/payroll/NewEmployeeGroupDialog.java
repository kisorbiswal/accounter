package com.vimukti.accounter.web.client.ui.payroll;

import com.vimukti.accounter.web.client.core.ClientEmployeeGroup;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class NewEmployeeGroupDialog extends BaseDialog<ClientEmployeeGroup> {

	private TextItem nameItem;
	private DynamicForm form;

	public NewEmployeeGroupDialog(String title) {
		super(title);
		this.getElement().setId("NewEmployeeGroupDialog");
		createControls();
	}

	public void createControls() {

		nameItem = new TextItem(messages.employeeGroup(), "nameItem");
		form = new DynamicForm("form");
		form.add(nameItem);
		bodyLayout.add(form);
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		return result;
	}

	@Override
	protected boolean onOK() {
		updateData();
		return true;
	}

	private void updateData() {
		ClientEmployeeGroup group = new ClientEmployeeGroup();
		group.setName(nameItem.getValue());
		saveOrUpdate(group);
	}

	@Override
	public void setFocus() {
		nameItem.setFocus();
	}

}
