package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;

public class AccountTerminalogyOption extends AbstractPreferenceOption {

	private static AccountTerminalogyOptionUiBinder uiBinder = GWT
			.create(AccountTerminalogyOptionUiBinder.class);
	@UiField
	Label accountHeaderLabel;
	@UiField
	RadioButton acountRadioButton;
	@UiField
	RadioButton leagandRadioButton;
	@UiField
	Label accountdescriptionLabel;
	@UiField
	RadioButton catgeroyRadioButton;

	interface AccountTerminalogyOptionUiBinder extends
			UiBinder<Widget, AccountTerminalogyOption> {
	}

	public AccountTerminalogyOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	public void initData() {
		int referAccounts = getCompanyPreferences().getReferAccounts();
		switch (referAccounts) {
		case ClientAccount.ACCOUNT:
			acountRadioButton.setValue(true);
			break;
		case ClientAccount.LEGAND:
			leagandRadioButton.setValue(true);
			break;
		case ClientAccount.CATEGORY:
			catgeroyRadioButton.setValue(true);
		}
	}

	public void createControls() {
		accountHeaderLabel.setText(messages.useTerminologyFor(Global.get()
				.account()));
		accountdescriptionLabel.setText(constants.accountDescription());
		accountdescriptionLabel.setStyleName("organisation_comment");
		acountRadioButton.setText(constants.account());
		leagandRadioButton.setText(constants.Ledger());
		catgeroyRadioButton.setText(constants.Category());

	}

	@Override
	public String getTitle() {
		return "Account Terminology";
	}

	@Override
	public void onSave() {
		if (acountRadioButton.getValue()) {
			getCompanyPreferences().setReferAccounts(ClientAccount.ACCOUNT);
		} else if (leagandRadioButton.getValue()) {
			getCompanyPreferences().setReferAccounts(ClientAccount.LEGAND);
		} else if (catgeroyRadioButton.getValue()) {
			getCompanyPreferences().setReferAccounts(ClientAccount.CATEGORY);
		}

	}

	@Override
	public String getAnchor() {
		return "Account Terminology";
	}

}
