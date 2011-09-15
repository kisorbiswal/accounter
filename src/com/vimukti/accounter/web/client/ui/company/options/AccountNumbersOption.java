package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.Accounter;

public class AccountNumbersOption extends AbstractPreferenceOption {

	private static AccountNumbersOptionUiBinder uiBinder = GWT
			.create(AccountNumbersOptionUiBinder.class);
	@UiField
	CheckBox accountNumbersCheckBox;
	@UiField
	CheckBox showAccountNumbersCheckBox;

	interface AccountNumbersOptionUiBinder extends
			UiBinder<Widget, AccountNumbersOption> {
	}

	public AccountNumbersOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	public void createControls() {
		accountNumbersCheckBox.setText(Accounter.constants().Accounnumbers());
		showAccountNumbersCheckBox.setText(Accounter.constants()
				.showAccounterNumbers());

	}

	public void initData() {
		// TODO Auto-generated method stub

	}

	public AccountNumbersOption(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public String getTitle() {
		return "Account Numbers";
	}

	@Override
	public void onSave() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getAnchor() {
		return constants.company();
	}

}
