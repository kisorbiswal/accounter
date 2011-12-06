package com.vimukti.accounter.web.client.portlet;

import com.vimukti.accounter.web.client.core.ClientPortletConfiguration;
import com.vimukti.accounter.web.client.core.ClientPortletPageConfiguration;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.BankAccountsPortlet;
import com.vimukti.accounter.web.client.ui.BankingPortlet;
import com.vimukti.accounter.web.client.ui.ExpenseClaimPortlet;
import com.vimukti.accounter.web.client.ui.MoneyComingPortlet;
import com.vimukti.accounter.web.client.ui.MoneyGoingPortlet;
import com.vimukti.accounter.web.client.ui.Portlet;
import com.vimukti.accounter.web.client.ui.QuickLinksPortlet;
import com.vimukti.accounter.web.client.ui.RecentTransactionsPortlet;
import com.vimukti.accounter.web.client.ui.WhoIOwePortlet;
import com.vimukti.accounter.web.client.ui.WhoOwesMePortlet;

public class PortletFactory {

	private static PortletFactory factory;

	public static final String BANK_ACCOUNT = "BANK_ACCOUNT";
	public static final String MONEY_COMING = "MONEY_COMING";
	public static final String MONEY_GOING = "MONEY_GOING";
	public static final String EXPENSES_CLAIM = "EXPENSES_CLAIM";
	public static final String WHO_I_OWE = "WHO_I_OWE";
	public static final String WHO_OWES_ME = "WHO_OWES_ME";
	public static final String RECENT_TRANSACTIONS = "RECENT_TRANSACTIONS";
	// public static final String MESSAGES_AND_TASKS = "Messages And Tasks";
	public static final String QUICK_LINKS = "QUICK_LINKS";
	public static final String BANKING = "BANKING";

	public static PortletFactory get() {
		if (factory == null) {
			factory = new PortletFactory();
		}
		return factory;
	}

	public Portlet createPortlet(ClientPortletConfiguration pc,
			PortletPage portletPage) {
		String name = pc.getName();
		Portlet portlet = null;
		if (name.equals(BANK_ACCOUNT)) {
			portlet = new BankAccountsPortlet(pc);
		} else if (name.equals(MONEY_COMING)) {
			portlet = new MoneyComingPortlet(pc);
		} else if (name.equals(MONEY_GOING)) {
			portlet = new MoneyGoingPortlet(pc);
		} else if (name.equals(EXPENSES_CLAIM)) {
			portlet = new ExpenseClaimPortlet(pc);
		} else if (name.equals(WHO_I_OWE)) {
			portlet = new WhoIOwePortlet(pc);
		} else if (name.equals(WHO_OWES_ME)) {
			portlet = new WhoOwesMePortlet(pc);
		} else if (name.equals(RECENT_TRANSACTIONS)) {
			portlet = new RecentTransactionsPortlet(pc);
		} else if (name.equals(QUICK_LINKS)) {
			portlet = new QuickLinksPortlet(pc);
		} else if (name.equals(BANKING)) {
			portlet = new BankingPortlet(pc);
		}
		/*
		 * else if (name.equals(Portlet.MESSAGES_AND_TASKS)) { portlet = new
		 * MessagesAndTasksPortlet(pc); }
		 */
		return portlet;
	}

	public String getPortletName(ClientPortletConfiguration pc) {
		AccounterMessages messages = Accounter.messages();
		String portletName = "";
		String name = pc.getName();
		if (name.equals(BANK_ACCOUNT)) {
			portletName = messages.bankAccount();
		} else if (name.equals(MONEY_COMING)) {
			portletName = messages.moneyComingIn();
		} else if (name.equals(MONEY_GOING)) {
			portletName = messages.moneyGoingOut();
		} else if (name.equals(EXPENSES_CLAIM)) {
			portletName = messages.expenseClaims();
		} else if (name.equals(WHO_I_OWE)) {
			portletName = messages.whoIOwe();
		} else if (name.equals(WHO_OWES_ME)) {
			portletName = messages.whoOwesMe();
		} else if (name.equals(RECENT_TRANSACTIONS)) {
			portletName = messages.recentTransactions();
		} else if (name.equals(QUICK_LINKS)) {
			portletName = messages.quickLinks();
		} else if (name.equals(BANKING)) {
			portletName = messages.banking();
		}
		return portletName;
	}

	public ClientPortletPageConfiguration getDefaultConfiguration(String page) {
		if (page.equals(PortletPage.DASHBOARD)) {
			return new ClientPortletPageConfiguration(2,
					new String[][] { { BANKING, EXPENSES_CLAIM },
							{ MONEY_COMING, MONEY_GOING } });
		}
		return null;
	}

	public ClientPortletPageConfiguration getPrefferedConfiguration(String page) {
		if (page.equals(PortletPage.DASHBOARD)) {
			return new ClientPortletPageConfiguration(2,
					new String[][] {
							{ BANKING, EXPENSES_CLAIM, WHO_I_OWE,
									RECENT_TRANSACTIONS, },
							{ BANK_ACCOUNT, MONEY_COMING, MONEY_GOING,
									WHO_OWES_ME, QUICK_LINKS } });
		}
		return null;
	}
}
