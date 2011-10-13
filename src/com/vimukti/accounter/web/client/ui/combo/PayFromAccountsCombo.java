package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.company.NewAccountAction;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;

public class PayFromAccountsCombo extends AccountCombo {

	private List<ClientAccount> payFromAccounts;

	public PayFromAccountsCombo(String title, boolean b) {
		super(title, b);
		initCombo(getAccounts());
	}

	public PayFromAccountsCombo(String title) {
		super(title);
		initCombo(getAccounts());
	}

	public List<ClientAccount> getAccounts() {
		payFromAccounts = new ArrayList<ClientAccount>();

		for (ClientAccount account : getCompany().getActiveAccounts()) {
			if (Arrays.asList(ClientAccount.TYPE_BANK,
			// ClientAccount.TYPE_CASH,
			// ClientAccount.TYPE_CREDIT_CARD,
			// ClientAccount.TYPE_LONG_TERM_LIABILITY,
			// ClientAccount.TYPE_OTHER_CURRENT_LIABILITY,
					ClientAccount.TYPE_OTHER_CURRENT_ASSET).contains(
					account.getType()))
				// if (!account.getName().equalsIgnoreCase(
				// Accounter.constants().pendingItemReceipts())
				// && !account.getName().equalsIgnoreCase(
				// Accounter.constants().salesTaxPayable())) {
				payFromAccounts.add(account);
			// }
		}
		return payFromAccounts;
	}

	public void setAccounts() {
		payFromAccounts = new ArrayList<ClientAccount>();

		for (ClientAccount account : getCompany().getActiveAccounts()) {
			if (Arrays.asList(ClientAccount.TYPE_BANK,
			// ClientAccount.TYPE_CASH,
					ClientAccount.TYPE_CREDIT_CARD, ClientAccount.TYPE_PAYPAL,
					// ClientAccount.TYPE_LONG_TERM_LIABILITY,
					// ClientAccount.TYPE_OTHER_CURRENT_LIABILITY,
					ClientAccount.TYPE_OTHER_CURRENT_ASSET
			// ClientAccount.TYPE_FIXED_ASSET
					).contains(account.getType()))
				// if (!account.getName().equalsIgnoreCase(
				// Accounter.constants().pendingItemReceipts())
				// && !account.getName().equalsIgnoreCase(
				// Accounter.constants().salesTaxPayable())) {

				payFromAccounts.add(account);
			// }
		}
		this.initCombo(payFromAccounts);
	}

	public void setDefaultPayFromAccount() {
		/* Default deposit in account is set to Bank Current Account */
		List<ClientAccount> accounts = getCompany().getAccounts();
		for (ClientAccount account : accounts) {
			if (account.getNumber().equals("1100")) {
				this.addItemThenfireEvent(account);
				break;
			}
		}

	}

	@Override
	public void onAddNew() {
		NewAccountAction action = ActionFactory.getNewAccountAction();
		// action.setAccountTypes(UIUtils
		// .getOptionsByType(AccountCombo.PAY_FROM_COMBO));
		action.setCallback(new ActionCallback<ClientAccount>() {

			@Override
			public void actionResult(ClientAccount result) {
				addItemThenfireEvent(result);
			}
		});
		action.setAccountTypes(Arrays.asList(ClientAccount.TYPE_BANK,
		// ClientAccount.TYPE_CASH,
		// ClientAccount.TYPE_CREDIT_CARD,
				ClientAccount.TYPE_OTHER_CURRENT_ASSET));

		action.run(null, true);

	}

	@Override
	public void addItemThenfireEvent(ClientAccount obj) {
		if (Arrays.asList(
		// ClientAccount.TYPE_BANK, ClientAccount.TYPE_CASH,
		// ClientAccount.TYPE_CREDIT_CARD,
		// ClientAccount.TYPE_LONG_TERM_LIABILITY,
		// ClientAccount.TYPE_OTHER_CURRENT_LIABILITY,
				ClientAccount.TYPE_OTHER_CURRENT_ASSET
		// ClientAccount.TYPE_FIXED_ASSET
				).contains(obj.getType())) {
			super.addItemThenfireEvent(obj);
		}

	}

	@Override
	public void init() {
		super.init();
		setDefaultPayFromAccount();
	}

}
