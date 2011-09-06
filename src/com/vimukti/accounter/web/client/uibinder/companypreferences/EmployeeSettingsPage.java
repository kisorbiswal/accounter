package com.vimukti.accounter.web.client.uibinder.companypreferences;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class EmployeeSettingsPage extends AbstractCompanyInfoPanel {

	private static EmployeeSettingsPageUiBinder uiBinder = GWT
			.create(EmployeeSettingsPageUiBinder.class);

	interface EmployeeSettingsPageUiBinder extends
			UiBinder<Widget, EmployeeSettingsPage> {
	}

	public EmployeeSettingsPage() {
		initWidget(uiBinder.createAndBindUi(this));
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
