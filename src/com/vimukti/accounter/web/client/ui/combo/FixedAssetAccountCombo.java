/**
 * 
 */
package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.HistoryTokenUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.company.NewAccountAction;
import com.vimukti.accounter.web.client.ui.core.CompanyActionFactory;

/**
 * @author Raj Vimal
 * 
 */
public class FixedAssetAccountCombo extends AccountCombo {

	private ArrayList<ClientAccount> assetAccounts;

	/**
	 * @param title
	 */
	public FixedAssetAccountCombo(String title) {
		super(title);
	}

	@Override
	public List<ClientAccount> getAccounts() {
		assetAccounts = new ArrayList<ClientAccount>();

		for (ClientAccount account : Accounter.getCompany()
				.getActiveAccounts()) {
			if (account.getType() == ClientAccount.TYPE_FIXED_ASSET)
				assetAccounts.add(account);
		}
		return assetAccounts;
	}

	@Override
	public SelectItemType getSelectItemType() {
		return SelectItemType.ACCOUNT;
	}

	@Override
	public void onAddNew() {
		NewAccountAction action = CompanyActionFactory.getNewAccountAction();
		action.setAccountTypes(UIUtils
				.getOptionsByType(AccountCombo.FIXEDASSET_COMBO));
		action.setActionSource(this);
		HistoryTokenUtils.setPresentToken(action, null);
		action.run(null, true);

	}

}
