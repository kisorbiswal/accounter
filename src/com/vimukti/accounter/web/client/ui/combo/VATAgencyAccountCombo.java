package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CompanyActionFactory;

public class VATAgencyAccountCombo extends AccountCombo {

	private ArrayList<ClientAccount> accounts;

	public VATAgencyAccountCombo(String title) {
		super(title);
	}

	@Override
	public String getDefaultAddNewCaption() {
		return comboConstants.newAccount();
	}

	@Override
	protected String getDisplayName(ClientAccount object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {
		Action action = CompanyActionFactory.getNewAccountAction();
		action.setActionSource(this);
		action.run(null, true);

	}

	@Override
	protected List<ClientAccount> getAccounts() {
		accounts = new ArrayList<ClientAccount>();

		for (ClientAccount account : FinanceApplication.getCompany()
				.getActiveAccounts()) {
			if (Arrays.asList(ClientAccount.TYPE_INCOME,
					ClientAccount.TYPE_EXPENSE,
					ClientAccount.TYPE_OTHER_CURRENT_LIABILITY,
					ClientAccount.TYPE_OTHER_CURRENT_ASSET,
					ClientAccount.TYPE_FIXED_ASSET).contains(account.getType()))
				accounts.add(account);
		}
		return accounts;
	}

	@Override
	public SelectItemType getSelectItemType() {
		return SelectItemType.ACCOUNT;
	}

}
