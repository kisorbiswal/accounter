package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.Accounter;

public class AccountNumbersOption extends AbstractPreferenceOption {

	private static AccountNumbersOptionUiBinder uiBinder = GWT
			.create(AccountNumbersOptionUiBinder.class);
	@UiField
	CheckBox accountNumbersCheckBox;
	@UiField
	CheckBox showAccountNumbersCheckBox;
	@UiField
	Label accountNumbersLabel;
	@UiField
	Label showAccountNumbers;

	interface AccountNumbersOptionUiBinder extends
			UiBinder<Widget, AccountNumbersOption> {
	}

	public AccountNumbersOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	private void createControls() {
		accountNumbersLabel.setText(Accounter.constants().Accounnumbers());
		showAccountNumbers
				.setText(Accounter.constants().showAccounterNumbers());

	}

	private void initData() {
		// TODO Auto-generated method stub

	}

	public AccountNumbersOption(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onSave() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getAnchor() {
		// TODO Auto-generated method stub
		return null;
	}

}
