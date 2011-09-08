package com.vimukti.accounter.admin.client;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.PasswordItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class AddNewAdminUserDialog extends AdminBaseDialog<ClientAdminUser> {
	DynamicForm form;
	TextItem userNameBox, emailIdBox;
	PasswordItem passwordBox;

	public AddNewAdminUserDialog(String text) {
		super(text, "");
		createControls();
		center();
	}

	public void createControls() {
		VerticalPanel vbodyLayout = new VerticalPanel();
		form = new DynamicForm();
		userNameBox = new TextItem(AdminHomePage.getAdminConstants().username());
		emailIdBox = new TextItem(AdminHomePage.getAdminConstants().emailID());
		passwordBox = new PasswordItem(AdminHomePage.getAdminConstants().password());
		form.setFields(userNameBox, emailIdBox, passwordBox);
		vbodyLayout.add(form);
		setBodyLayout(vbodyLayout);
	}

	protected void addNewAdminUser() {
		ClientAdminUser clientUser = new ClientAdminUser();
		clientUser.setName(UIUtils.toStr(userNameBox.getValue()));
		clientUser.setEmailId(UIUtils.toStr(emailIdBox.getValue()));
		clientUser.setPassword(UIUtils.toStr(passwordBox.getValue()));
		saveOrUpdate(clientUser);

	}

	@Override
	protected boolean onOK() {
		addNewAdminUser();
		return true;
	}
}
