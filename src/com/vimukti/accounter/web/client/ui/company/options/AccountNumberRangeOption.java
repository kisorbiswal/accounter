package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class AccountNumberRangeOption extends AbstractPreferenceOption {

	private static AccountNumberRangeOptionUiBinder uiBinder = GWT
			.create(AccountNumberRangeOptionUiBinder.class);

	interface AccountNumberRangeOptionUiBinder extends
			UiBinder<Widget, AccountNumberRangeOption> {
	}

	public AccountNumberRangeOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	@UiField
	CheckBox accountNumberRangeCheck;
	@UiField
	Label label;

	public AccountNumberRangeOption(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public String getTitle() {

		return messages.accounNumberRange();
	}

	@Override
	public void onSave() {
		getCompanyPreferences().setIsAccountNumberRangeCheck(
				accountNumberRangeCheck.getValue());

	}

	@Override
	public String getAnchor() {
		return messages.company();
	}

	@Override
	public void createControls() {
		label.setText(messages.enableOrDisableTheAccountNumberRangeChecking());
		accountNumberRangeCheck.setText(messages.accountNumberRangeCheck());
	}

	@Override
	public void initData() {
		accountNumberRangeCheck.setValue(getCompanyPreferences()
				.isAccountnumberRangeCheckEnable());

	}

}
