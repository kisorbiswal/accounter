package com.vimukti.accounter.web.client.ui.company.options;

import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class AccountNumberRangeOption extends AbstractPreferenceOption {

	public AccountNumberRangeOption() {
		super("");
		createControls();
		initData();
	}

	CheckboxItem accountNumberRangeCheck;

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
		accountNumberRangeCheck = new CheckboxItem(
				messages.enableOrDisableTheAccountNumberRangeChecking(),
				"accountNumberRangeCheck");
		StyledPanel mainpanel = new StyledPanel("accountNumberRangeOption");
		mainpanel.add(accountNumberRangeCheck);
		add(mainpanel);
	}

	@Override
	public void initData() {
		accountNumberRangeCheck.setValue(getCompanyPreferences()
				.isAccountnumberRangeCheckEnable());

	}

}
